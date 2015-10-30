package edu.binghamton.my.tokenizer;

public class StringBuilderWrapper {

	StringBuilder stringBuilder;
	
	public StringBuilderWrapper() {
		stringBuilder = new StringBuilder();
	}
	
	public StringBuilderWrapper(String initString) {
		stringBuilder = new StringBuilder();
		stringBuilder.append(initString);
	}
	
	public String toString() {
		return stringBuilder.toString();
	}
	
	public void append(String stringToAppend) {
		stringBuilder.append(stringToAppend);
	}
	
	public void replaceAllOccurances(String partToReplace, String replacingString) {
		int startIndex;
		while ((startIndex = stringBuilder.indexOf(partToReplace)) > -1) {
			int endIndex = startIndex + partToReplace.length();
			stringBuilder.replace(startIndex, endIndex, replacingString);
		}
	}
	
	public void removeAllOccurancesOf(String partToReplace) {
		String data = this.toString();
		stringBuilder.setLength(0);

		String[] words = data.split(" ");
		for (String word : words) {
			if (!word.equalsIgnoreCase(partToReplace)) {
				this.append(word + " ");
			}
		}
	}

	public void removeAllOccurancesOf(String[] stopWordsArray) {
		for (String strToRemove : stopWordsArray) {
			this.removeAllOccurancesOf(strToRemove);
		}
		
	}
}
