package edu.binghamton.my.tokenizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TokenizerPart1 {

	public static void main(String[] args) {
		File myFile = new File("C:\\Users\\Ishh\\Downloads\\Assignments\\IR\\Project Part 1\\NYT19981018(1).0123");
		File outputFile = new File("output.txt");
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(myFile)));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			String line = "";
			
			while ((line = br.readLine()) != null) {
				String data = line.replaceAll("\\<.*?>", "").toLowerCase();
				
				data = removeHyphens(data);
				
				data = removeSingleLengthCharsAndStopWords(data);
				
				data = removeParanthesisAndQuotes(data);
				
				data = performStemming(data);

				outputData(data, bw);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.flush();
				bw.close();
				br.close();
			} catch (IOException e) {
			}
		}
	}

	private static void outputData(String data, BufferedWriter bw) {
		String[] words = data.split(" ");
		for (String word : words) {
			if (word.trim().length() > 1) {
				try {
					bw.write(word + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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

			if (word.endsWith("ies") && (!word.endsWith("aies") || !word.endsWith("aies"))) {
				word = word.substring(0, word.length() - 3) + "y";
			} else if (word.endsWith("es") && (!word.endsWith("ees") || !word.endsWith("oes"))) {
				word = word.substring(0, word.length() - 2) + "e";
			} else if (word.endsWith("s") && (!word.endsWith("us") || !word.endsWith("ss"))) {
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

	private static String removeHyphens(String data) {
		return data.replaceAll("\\-", " ");
	}

}
