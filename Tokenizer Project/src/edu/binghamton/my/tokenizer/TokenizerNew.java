package edu.binghamton.my.tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class TokenizerNew {

	//private static final String HYPHEN = "-";
	//private static final String SPACE = " ";
	
	public static void main(String[] args) {
		File myFile = new File("C:\\Users\\Ishh\\Downloads\\Assignments\\IR\\Project Part 1\\NYT19981018(1).0123");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(myFile)));
			String line = "";
			
			while ((line = br.readLine()) != null) {
				String data = line.replaceAll("\\<.*?>", "").replaceAll("\\-", " ").toLowerCase();
				String[] words = data.split(" ");
				
				for (String word : words) {
					processWord(word);
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
	}

	private static void processWord(String word) {
		word = removeParanthesisAndQuotes(word);
		
		word = removeSpecialCharsAtStartAndEndOfWord(word); // removes , . ? : ; ! chars
		
		word = performStemming(word);
		
		if (isStopWord(word) || word.trim().length() < 2) {
			return;
		}
		
		System.out.println(word);
	}

	private static String removeSpecialCharsAtStartAndEndOfWord(String word) {
		if (word.endsWith(",") || word.endsWith(".") || word.endsWith("?") 
				|| word.endsWith(":") || word.endsWith(";") || word.endsWith("!")) {
			word = word.substring(0, word.length() - 1);
		}
		
		if (word.startsWith(",") || word.startsWith(".") || word.startsWith("?") 
				|| word.startsWith(":") || word.startsWith(";") || word.startsWith("!")) {
			word = word.substring(1);
		}

		return word;
	}

	private static String performStemming(String word) {
		if (word.endsWith("ies") && (!word.endsWith("aies") || !word.endsWith("aies"))) {
			word = word.substring(0, word.length() - 3) + "y";
		} else if (word.endsWith("es") && (!word.endsWith("ees") || !word.endsWith("oes"))) {
			word = word.substring(0, word.length() - 2) + "e";
		} else if (word.endsWith("s") && (!word.endsWith("us") || !word.endsWith("ss"))) {
			word = word.substring(0, word.length() - 1);
		}
		return word;
	}

	private static String removeParanthesisAndQuotes(String data) {
		return data.replaceAll("\'", "").replaceAll("\"", "").replaceAll("\\{", "")
				.replaceAll("\\[", "").replaceAll("\\(", "").replaceAll("\\)", "")
				.replaceAll("\\]", "").replaceAll("\\{", "");
	}

	private static boolean isStopWord(String word) {
		return (word.equalsIgnoreCase("and") || word.equalsIgnoreCase("an")
				|| word.equalsIgnoreCase("by") || word.equalsIgnoreCase("from")
				|| word.equalsIgnoreCase("of") || word.equalsIgnoreCase("the")
				|| word.equalsIgnoreCase("in") || word.equalsIgnoreCase("with"));
	}

}
