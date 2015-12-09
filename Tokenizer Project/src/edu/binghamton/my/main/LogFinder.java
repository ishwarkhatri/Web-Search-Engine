package edu.binghamton.my.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.binghamton.my.tokenizer.Tokenizer;

public class LogFinder {

	public static void main(String[] args) {
		//log2(0.9(9/239)+0.1(24/954)+ log2(0.9(1/239)+0.1(4/954) =
		//-12.6802
		File f = new File("C:\\Users\\Ishh\\Downloads\\Assignments\\IR\\Project part 2\\docs\\d30003t\\APW19981017.0151");
		BufferedReader reader = null;
		try {
			String line = "";
			reader = new BufferedReader(new FileReader(f));
			//while((line = reader.readLine()) != null && !line.equalsIgnoreCase("<text>"));
			List<String> list = new ArrayList<String>();
			while((line = reader.readLine()) !=  null) {
				list.addAll(Arrays.asList(Tokenizer.tokenize(line)));
				/*line = line.replaceAll("\\<.*?>", "");
				if(line.trim().length() > 0) {
					line = removeStopWords(line.trim());
					list.addAll(Arrays.asList(line.split(" ")));
				}*/
			}
			for(String s : list) {
				System.out.println(s);
			}
			System.out.println(list.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String removeStopWords(String data) {
		String[] words = data.split(" ");
		String transformedData = "";
		for (String word : words) {
			if (word.trim().length() > 0 && !isStopWord(word)) {
				transformedData += word + " ";
			}
		}
		return transformedData;
	}

	private static boolean isStopWord(String word) {
		return (word.equalsIgnoreCase("and") || word.equalsIgnoreCase("an")
				|| word.equalsIgnoreCase("by") || word.equalsIgnoreCase("from")
				|| word.equalsIgnoreCase("of") || word.equalsIgnoreCase("the")
				|| word.equalsIgnoreCase("in") || word.equalsIgnoreCase("with")
				|| word.equalsIgnoreCase("a"));
	}
}
