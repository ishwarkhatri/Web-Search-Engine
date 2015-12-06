package edu.binghamton.my.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Document extends DocNumber implements Comparable<Document> {

	private int documentNumber;

	private String docPath;

	private String headline;

	private long wordCount;

	private String snippet;

	public Document(String[] data) {
		this.documentNumber = Integer.parseInt(data[0]);
		this.wordCount = Long.parseLong(data[1]);
		this.headline = data[2];
		this.snippet = data[3];
		this.docPath = data[4];
	}

	public Document(File docPath) throws IOException {
		this.documentNumber = ++globalDocCount;
		this.docPath = docPath.getAbsolutePath();
		init(docPath);
	}

	private void init(File docPath) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(docPath)));
		String line;
		while((line = reader.readLine()) != null) {
			if(line.equalsIgnoreCase("<headline>")) {
				this.headline = reader.readLine().replaceAll(",", "").trim();
			}
			if(line.equalsIgnoreCase("<text>")) {
				this.snippet = reader.readLine().replaceAll("\\<.*?>", "").replaceAll(",", "").trim();
				while(this.snippet != null && this.snippet.split(" ").length < 40) {
					this.snippet += " " + reader.readLine().replaceAll("\\<.*?>", "").replaceAll(",", "").trim();
				}
				//If asked make snippet size as exact 40 words

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

	public String getDocPath() {
		return docPath;
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

	@Override
	public String toString() {
		return this.getDocumentNumber() + "," 
				+ this.getWordCount() + "," 
				+ this.getHeadline() + "," 
				+ this.getSnippet() + "," 
				+ this.getDocPath();
	}

}
