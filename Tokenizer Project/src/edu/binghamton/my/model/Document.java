package edu.binghamton.my.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Document extends DocId {

	int documentNumber;

	String headline;

	long wordCount;

	String snippet;

	public Document(File docPath) throws IOException {
		this.documentNumber = ++globalDocCount;
		init(docPath);
	}

	private void init(File docPath) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(docPath)));
		while(true) {
			String line = reader.readLine();
			if(line.equalsIgnoreCase("<headline>")) {
				this.headline = reader.readLine().replaceAll(",", "").trim();
			}
			if(line.equalsIgnoreCase("<body>")) {
				this.snippet = reader.readLine().replaceAll("\\<.*?>", "").replaceAll(",", "").trim();
			}
			if(this.headline != null && this.snippet != null) {
				break;
			}
		}
		reader.close();
	}

	public int getDocumentNumber() {
		return this.documentNumber;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public long getWordCount() {
		return wordCount;
	}
	public void setWordCount(long wordCount) {
		this.wordCount = wordCount;
	}
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	
}
