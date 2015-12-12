package edu.binghamton.my.main;

import static edu.binghamton.my.common.Utilities.echo;

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

import edu.binghamton.my.common.Constants;
import edu.binghamton.my.model.Dictionary;
import edu.binghamton.my.model.Document;
import edu.binghamton.my.model.Posting;
import edu.binghamton.my.tokenizer.Tokenizer;

public class DictionaryGenerator {

	private static List<Document> documentList = new ArrayList<Document>();
	private static Map<String,Dictionary> dictionaryMap = new HashMap<String, Dictionary>();
	private static long totalWordCount;

	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			echo(Constants.FILE_PATH_ABSENT_ERROR_MESSAGE);
			return;
		}

		echo(Constants.PROJECT_START_MESSAGE);
		String path = args[0];

		echo(Constants.FILE_INFO_FETCHING_MESSAGE + path);
		File file = new File(path);
		List<File> filePaths = new ArrayList<File>();

		//Retrieve all file paths present in directory
		Set<String> fileNames = new HashSet<String>();
		getFiles(file, fileNames, filePaths);
		echo(filePaths.size() + Constants.FILES_TO_PROCESS_MESSAGE);

		echo(Constants.EXECUTION_START_MESSAGE);
		echo(Constants.TOKENIZING_INFO_MESSAGE);

		Document doc = null;
		for(File f : filePaths) {
			doc = new Document(f);
			List<String> tokens = Tokenizer.tokenize(f);
			doc.setWordCount(tokens.size());
			documentList.add(doc);
			totalWordCount += tokens.size();
			addTokensToDictionary(tokens, doc.getDocumentNumber());
		}

		echo(Constants.TASK_COMPLETED_MESSAGE);
		echo(Constants.EXPORTING_DOC_FILE_MESSAGE);
		//Output document file
		outputDocumentsInfo();

		echo(Constants.EXPORTING_DICTIONARY_AND_POSTINGS_FILE_MESSAGE);
		//Output Dictionary and Postings files
		outputDictionaryAndPostings();

		echo(Constants.EXPORTING_SUMMARY_FILE_MESSAGE);
		//Output Summary file
		outputSummaryFile();

		echo(Constants.FILES_EXPORT_COMPLETED_MESSAGE);
		echo(Constants.PROGRAM_END_MESSGAE);

	}

	private static void outputSummaryFile() throws IOException {
		BufferedWriter bw3 = null;
		try{
			bw3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Constants.SUMMARY_FILE_NAME)));
			bw3.write("Total word count: " + totalWordCount);
		} finally {
			bw3.flush();
			bw3.close();
		}
		
	}

	private static void outputDictionaryAndPostings() throws IOException {
		BufferedWriter dictFileBuffWriter = null;
		BufferedWriter postingsFileBuffWriter = null;
		
		try {
			dictFileBuffWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Constants.DICTIONARY_FILE_NAME)));
			postingsFileBuffWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Constants.POSTINGS_FILE_NAME)));
			List<String> termList = new ArrayList<String>();

			//Save all terms from dictionary to a list
			termList.addAll(dictionaryMap.keySet());

			//Sort the list alphabetically
			Collections.sort(termList);

			long offSet = 0;
			for(int i = 0; i < termList.size(); i++) {
				//Get dictionary of term and write information in file
				Dictionary dict = dictionaryMap.get(termList.get(i));
				dictFileBuffWriter.write(dict.getTerm() + "," + dict.getCollectionFrequencyOfTerm() + "," + dict.getDocumentFrequencyOfTerm() + "," + offSet + "\n");

				//Increment offset for the term
				offSet += dict.getDocumentFrequencyOfTerm();

				//Get postings of a term and sort alphabetically
				List<Posting> postings = dict.getPostings();
				Collections.sort(postings);

				//Write postings to file
				for(Posting p : postings) {
					postingsFileBuffWriter.write(p.getDocumentId() + "," + p.getTermFrequency() + "\n");
				}
			}
		} finally {
			dictFileBuffWriter.flush();
			dictFileBuffWriter.close();
			postingsFileBuffWriter.flush();
			postingsFileBuffWriter.close();
		}
	}

	private static void outputDocumentsInfo() throws IOException {
		BufferedWriter docsInfoBuffWriter = null;
		try {
			docsInfoBuffWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Constants.DOCUMENTS_FILE_NAME)));
			Collections.sort(documentList);
			for(Document doc : documentList) {
				docsInfoBuffWriter.write(doc.toString() + "\n");
			}
		} finally {
			docsInfoBuffWriter.flush();
			docsInfoBuffWriter.close();
		}
	}

	private static void addTokensToDictionary(List<String> tokens, int documentNumber) {
		for(String term : tokens) {
			Dictionary termDictionary = dictionaryMap.get(term);
			if(termDictionary == null) {
				termDictionary = new Dictionary(term, documentNumber);

				Posting posting = new Posting(documentNumber, 1);
				termDictionary.getPostings().add(posting);

				/*Set<Integer> docIds = new HashSet<Integer>();
				docIds.add(documentNumber);*/

				dictionaryMap.put(term, termDictionary);
			} else {
				termDictionary.incrementCollectionFrequency();
				termDictionary.addDocIdOfTerm(documentNumber);
				termDictionary.incrementDocumentFrequency();

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

	/**
	 * This method stores file paths recursively digging into sub-directories
	 * @param docsPath A given path of a directory or file
	 * @param filePaths A list containing path of all files present in a given directory
	 */
	private static void getFiles(File path, Set<String> fileNameSet, List<File> filePaths) {
		if(path.isDirectory()) {
			File[] subFiles = path.listFiles();
			for (File file : subFiles)
				getFiles(file, fileNameSet, filePaths);
		} else {
			//if(!"".equalsIgnoreCase(path.getName()) && !fileNameSet.contains(path.getName()))
				filePaths.add(path);
		}
	}

}
