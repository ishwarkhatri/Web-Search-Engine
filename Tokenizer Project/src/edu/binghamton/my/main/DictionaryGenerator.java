package edu.binghamton.my.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.binghamton.my.model.Dictionary;
import edu.binghamton.my.model.Document;
import edu.binghamton.my.model.Posting;
import edu.binghamton.my.tokenizer.Tokenizer;

public class DictionaryGenerator {

	private static List<Document> documentList = new ArrayList<Document>();
	private static Map<String,Dictionary> dictionaryMap = new HashMap<String, Dictionary>();
	private static long totalWordCount = 0;

	public static void main(String[] args) throws IOException {
		String path = args[0];
		File docsPath = new File(path);
		List<File> filePaths = new ArrayList<File>();

		getFiles(docsPath, filePaths);
		for(File f : filePaths) {
			Document doc = new Document(f);
			List<String> tokens = Tokenizer.tokenize(f);
			doc.setWordCount(tokens.size());
			documentList.add(doc);
			totalWordCount += tokens.size();
			writeToDictionary(tokens, doc.getDocumentNumber());
		}

		//Output document file
		BufferedWriter bw = null;
		BufferedWriter bw2 = null;
		BufferedWriter bw3 = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Documents.txt")));
			for(Document doc : documentList) {
				bw.write(doc.getDocumentNumber() + "," + doc.getHeadline() + "," + doc.getWordCount() + "," + doc.getSnippet() + "\n");
			}
		} finally {
			bw.flush();
			bw.close();
		}
		
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Dictionary.txt")));
			bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Postings.txt")));
			bw3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Total.txt")));

			List<String> termList = new ArrayList<String>();
			termList.addAll(dictionaryMap.keySet());
			Collections.sort(termList);
			long offSet = 0;
			for(int i = 0; i < termList.size(); i++) {
				Dictionary dict = dictionaryMap.get(termList.get(i));
				bw.write(dict.getTerm() + "," + dict.getCollectionFrequencyOfTerm() + "," + dict.getDocumentFrequencyOfTerm() + "," + offSet + "\n");
				offSet += dict.getDocumentFrequencyOfTerm();

				List<Posting> postings = dict.getPostings();
				Collections.sort(postings);
				for(Posting p : postings) {
					bw2.write(p.getDocumentId() + "," + p.getTermFrequency() + "\n");
				}
			}
			bw3.write("Total word count: " + totalWordCount);
		} finally {
			bw.flush();
			bw2.flush();
			bw3.flush();
			bw.close();
			bw2.close();
			bw3.close();
		}
	}

	private static void writeToDictionary(List<String> tokens, int documentNumber) {
		for(String term : tokens) {
			Dictionary termDictionary = dictionaryMap.get(term);
			if(termDictionary == null) {
				termDictionary = new Dictionary(term, documentNumber);

				Posting posting = new Posting(documentNumber, 1);
				termDictionary.getPostings().add(posting);

				Set<Integer> docIds = new HashSet<Integer>();
				docIds.add(documentNumber);

				dictionaryMap.put(term, termDictionary);
			} else {
				termDictionary.incrementCollectionFrequency();
				termDictionary.addDocIdOfTerm(documentNumber);

				boolean docPresent = false;
				for(Posting p : termDictionary.getPostings()) {
					if(p.getDocumentId() == documentNumber) {
						p.incrementTermFrequency();
						docPresent = true;
						break;
					}
				}

				if(!docPresent) {
					Posting newPosting = new Posting(documentNumber, 1);
					termDictionary.getPostings().add(newPosting);
				}
			}
		}
	}

	private static void getFiles(File docsPath, List<File> filePaths) {
		if(docsPath.isDirectory()) {
			File[] subFiles = docsPath.listFiles();
			for (File file : subFiles)
				getFiles(file, filePaths);
		} else {
			filePaths.add(docsPath);
		}
	}

}
