package edu.binghamton.my.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import static edu.binghamton.my.common.Constants.*;
import static edu.binghamton.my.common.Utilities.*;

import edu.binghamton.my.common.Constants;
import edu.binghamton.my.model.Dictionary;
import edu.binghamton.my.model.Document;
import edu.binghamton.my.model.Posting;
import edu.binghamton.my.model.RankingInfo;
import edu.binghamton.my.tokenizer.Tokenizer;

public class QueryProcessor {

	private static final float W = 0.1f;
	private static int totalWordCountInCollection;
	private static List<Document> documentList = new ArrayList<Document>();
	private static Map<String,Dictionary> dictionaryMap = new HashMap<String, Dictionary>();

	public static void main(String[] args) {
		echo(QUERY_EXECUTION_START_MESSAGE);

		File dictionaryFile = new File("Dictionary.csv");
		File documentsFile = new File("Documents.csv");
		File postingsFile = new File("Postings.csv");
		File totWordCountFile = new File("Total.csv");
		File outputFile = new File("output.txt");

		BufferedReader reader = null;
		BufferedReader reader1 = null;
		try {
			//Read documents file
			echo(Constants.LOADING_DOCUMENTS_FILE_MESSAGE);
			reader = new BufferedReader(new FileReader(documentsFile));
			String line = "";
			Document doc = null;
			while((line = reader.readLine()) != null) {
				String[] inputs = line.split(",");
				doc = new Document(inputs);
				documentList.add(doc);
			}
			echo(Constants.FILE_LOADING_COMPLETED_MESSAGE);

			//Read word count file
			echo(Constants.LOADING_WORD_COUNT_FILE_MESSAGE);
			reader = new BufferedReader(new FileReader(totWordCountFile));
			line = reader.readLine();
			setWordCount(line);
			echo(Constants.FILE_LOADING_COMPLETED_MESSAGE);

			//Read dictionary and posting files
			echo(Constants.LOADING_DICTIONARY_AND_POSTINGS_FILES_MESSAGE);
			reader = new BufferedReader(new FileReader(dictionaryFile));
			reader1 = new BufferedReader(new FileReader(postingsFile));

			String[] inputs, inputs1;
			long noOfDocs;
			int docId, termFreq;
			while((line = reader.readLine()) != null) {
				if("".equalsIgnoreCase(line.trim())) {
					continue;
				}

				inputs = line.split(",");
				Dictionary dict = new Dictionary(inputs);
				noOfDocs = dict.getDocumentFrequencyOfTerm();

				Posting posting = null;
				List<Posting> postingList = new ArrayList<Posting>();
				for(int i=0; i<noOfDocs; i++) {
					//line = getPostingsOnOffset(reader1, dict.getOffset());
					//line = reader1.readLine();
					if(line != null && !"".equalsIgnoreCase(line.trim())) {
						inputs1 = line.split(",");
						docId = Integer.parseInt(inputs1[0]);
						termFreq = Integer.parseInt(inputs1[1]);
						posting = new Posting(docId, termFreq);
						postingList.add(posting);
						dict.addDocIdOfTerm(docId);
					}
				}
				dict.setPostings(postingList);
				dictionaryMap.put(dict.getTerm(), dict);
			}

			echo(Constants.FILE_LOADING_COMPLETED_MESSAGE);
		} catch(Exception e) {
			echoError(Constants.ERROR_LOADING_FILES_TO_MEMORY_MESSAGE + ":" + e.getMessage());
			System.exit(1);
		}

		//Start processing queries
		Scanner scan = null;
		BufferedWriter writer = null;
		try {
			scan = new Scanner(System.in);
			writer = new BufferedWriter(new FileWriter(outputFile));

			while(true) {
				echo(ENTER_QUERY_MESSAGE);
				String query = scan.nextLine();

				if(EXIT.equalsIgnoreCase(query)) {
					break;
				} else {
					String[] tokenizedQuery = Tokenizer.tokenize(query);
					List<RankingInfo> docsRankList = calculateDocumentRankingForQuery(tokenizedQuery);

					outputResults(writer, query, docsRankList);
				}

			}
		} catch(Exception e) {
			echo(QUERY_PROCESSING_ERROR_MESSAGE + ": " + e.getMessage());
		} finally {
			try {
				writer.flush();
				writer.close();
				scan.close();
			} catch (Exception e) {
				echoError(QUERY_PROCESSING_ERROR_MESSAGE + ": " + e.getMessage());
			}
		}
	}

	private static void outputResults(BufferedWriter writer, String query, List<RankingInfo> docsRankList) throws IOException {
		Collections.sort(docsRankList);
		String data = Constants.QUERY_PRINT_MESSAGE + query + "\n";

		for(RankingInfo rankInfo : docsRankList) {
			data += Constants.DOCUMENT_PRINT_MESSAGE + rankInfo.getDocPath() + "\n";
			data += Constants.HEADLINE_PRINT_MESSAGE + rankInfo.getHeadline() + "\n";
			data += Constants.PROBABILITY_PRINT_MESSAGE + rankInfo.getRank() + "\n";
			data += Constants.SNIPPET_PRINT_MESSAGE + rankInfo.getSnippet() + "\n\n";
		}

		if(docsRankList.isEmpty()) {
			data += Constants.NO_RELEVANT_DOCS_PRESENT_MESSAGE + "\n\n";
		}
		echo(data);
		writer.write(data);
		writer.flush();
	}

	private static void setWordCount(String line) {
		totalWordCountInCollection = Integer.parseInt(line.split(" ")[3]);
	}

	private static List<RankingInfo> calculateDocumentRankingForQuery(String[] tokenizedQuery) {
		List<RankingInfo> rankingList = new ArrayList<RankingInfo>();
		Set<Integer> docIds = new TreeSet<Integer>();

		for(String term : tokenizedQuery) {
			Dictionary dict = dictionaryMap.get(term);
			if(dict == null) {
				echo(Constants.TERM_NOT_PRESENT_MESSAGE + " : " + term);
				continue;
			}

			List<Posting> postings = dict.getPostings();
			for(Posting posting : postings) {
				docIds.add(posting.getDocumentId());
			}
		}

		for(Integer docId : docIds) {
			double rank = 0;
			Document doc = getDocumentById(docId);

			for(String term : tokenizedQuery) {
				Dictionary dict = dictionaryMap.get(term);
				if(dict != null) {
					long collectionFreq = dict.getCollectionFrequencyOfTerm();
					int tf = getTermFrequencyFromPostingsByDocId(docId, dict.getPostings());

					long docWordCount = doc.getWordCount();

					rank += calculateRank(W, tf, docWordCount, collectionFreq, totalWordCountInCollection);
				}
			}

			RankingInfo rankInfo = new RankingInfo();
			rankInfo.setDocId(docId);
			rankInfo.setHeadline(doc.getHeadline());
			rankInfo.setRank(rank);
			rankInfo.setSnippet(doc.getSnippet());
			rankInfo.setDocPath(doc.getDocPath());
			rankingList.add(rankInfo);
		}

		return rankingList;
	}

	private static int getTermFrequencyFromPostingsByDocId(int docId, List<Posting> postings) {
		for(Posting posting : postings) {
			if(docId == posting.getDocumentId()) {
				return posting.getTermFrequency();
			}
		}
		return 0;
	}

	private static Document getDocumentById(Integer docId) {
		for(Document doc : documentList) {
			if(doc.getDocumentNumber() == docId) {
				return doc;
			}
		}
		return null;
	}

	private static double calculateRank(double w, double tf, double tdf, double tcf, double twc) {
		double value = (((1 - w) * (tf / tdf)) + (w * (tcf / twc)));
		double logValue = Math.log(value) / Math.log(2);
		return logValue;
	}
}
