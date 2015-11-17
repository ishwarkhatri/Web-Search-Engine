package edu.binghamton.my.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Document extends DocNumber implements Comparable<Document> {

	private int documentNumber;

	private String docID;

	private String headline;

	private long wordCount;

	private String snippet;

	public Document(File docPath) throws IOException {
		this.documentNumber = ++globalDocCount;
		this.docID = docPath.getName();
		init(docPath);
	}

	private void init(File docPath) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(docPath)));
		String line;
		while((line = reader.readLine()) != null) {
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

	public int compareTo(Document doc) {
		if(this.documentNumber > 1)
			return 1;
		if(this.documentNumber < 1)
			return -1;
		return 0;
	}

	public String getDocID() {
		return docID;
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
