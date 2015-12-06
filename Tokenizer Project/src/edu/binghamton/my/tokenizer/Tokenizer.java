package edu.binghamton.my.tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

	public static String[] tokenize(String query) {
		String data = query.replaceAll("\\<.*?>", "").toLowerCase();
		
		data = removeHyphensAndUnderscores(data);
		
		data = removeSingleLengthCharsAndStopWords(data);
		
		data = removeParanthesisAndQuotes(data);

		//This method is introduced in part2
		data = removeSpecialCharacters(data);

		data = performStemming(data);

		String[] tokens = getFinalTerms(data);

		return tokens;
	}

	public static List<String> tokenize(File filePath) {
		String[] tokens = null;
		List<String> completeTokens = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			String line = "";
			
			while ((line = br.readLine()) != null) {
				tokens = tokenize(line);
				
				if(tokens.length != 0) {
					for(String term : tokens) {
						completeTokens.add(term);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
		}
		}
		return completeTokens;
	}

	private static String removeSpecialCharacters(String data) {
		return data.replaceAll("[^\\w\\s]", "");
	}

	private static String[] getFinalTerms(String data) {
		String[] words = data.split(" ");
		List<String> temp = new ArrayList<String>();
		
		for (String word : words) {
			if (word.trim().length() > 1) {
				temp.add(word);
			}
		}
		String[] finalWords = new String[temp.size()];

		for(int i = 0; i < temp.size(); i++) {
			finalWords[i] = temp.get(i);
		}

		return finalWords;
	}

	private static String performStemming(String data) {
		String[] words = data.split(" ");
		String transformedData = "";

		for (String word : words) {
			if (word.endsWith(",") || word.endsWith(".") || word.endsWith("?") 
					|| word.endsWith(":") || word.endsWith(";") || word.endsWith("!")) {
				word = word.substring(0, word.length() - 1);
			}

			if (word.startsWith(",") || word.startsWith(".") || word.startsWith("?") 
					|| word.startsWith(":") || word.startsWith(";") || word.startsWith("!")) {
				word = word.substring(1);
			}

			if (word.endsWith("ies") && !(word.endsWith("aies") || word.endsWith("aies"))) {
				word = word.substring(0, word.length() - 3) + "y";
			} else if (word.endsWith("es") && !(word.endsWith("ees") || word.endsWith("oes"))) {
				word = word.substring(0, word.length() - 2) + "e";
			} else if (word.endsWith("s") && !(word.endsWith("us") || word.endsWith("ss"))) {
				word = word.substring(0, word.length() - 1);
			}
			transformedData += word + " ";
		}
		return transformedData;
	}

	private static String removeParanthesisAndQuotes(String data) {
		return data.replaceAll("\'", "").replaceAll("\"", "").replaceAll("\\{", "")
				.replaceAll("\\[", "").replaceAll("\\(", "").replaceAll("\\)", "")
				.replaceAll("\\]", "").replaceAll("\\{", "");
	}

	private static String removeSingleLengthCharsAndStopWords(String data) {
		String[] words = data.split(" ");
		String transformedData = "";
		for (String word : words) {
			if (word.length() > 1 && !isStopWord(word)) {
				transformedData += word + " ";
			}
		}
		return transformedData;
	}

	private static boolean isStopWord(String word) {
		return (word.equalsIgnoreCase("and") || word.equalsIgnoreCase("an")
				|| word.equalsIgnoreCase("by") || word.equalsIgnoreCase("from")
				|| word.equalsIgnoreCase("of") || word.equalsIgnoreCase("the")
				|| word.equalsIgnoreCase("in") || word.equalsIgnoreCase("with"));
	}

	private static String removeHyphensAndUnderscores(String data) {
		return data.replaceAll("\\-", " ").replaceAll("\\_", "");
	}
}
