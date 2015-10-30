package edu.binghamton.my.model;

public class Posting implements Comparable<Posting> {

	int documentId;

	int termFrequency;

	public Posting(int docId, int termFreq) {
		this.documentId = docId;
		this.termFrequency = termFreq;
	}

	public void incrementTermFrequency() {
		this.termFrequency++;
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public int getTermFrequency() {
		return termFrequency;
	}

	public void setTermFrequency(int termFrequency) {
		this.termFrequency = termFrequency;
	}

	public int compareTo(Posting p1) {
		if(this.documentId < p1.getDocumentId())
			return -1;
		if(this.documentId > p1.getDocumentId())
			return 1;
		return 0;
	}

}
