package edu.binghamton.my.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dictionary extends GlobalOffset {

	private String term;

	private long collectionFrequencyOfTerm;

	private Set<Integer> docIdsOfTerm = new HashSet<Integer>();

	private int offset;
	
	private List<Posting> postings = new ArrayList<Posting>();

	public Dictionary(String term, int docId) {
		this.term = term;
		this.collectionFrequencyOfTerm++;
		this.docIdsOfTerm.add(docId);
		this.offset = globalOffset++;
	}

	public void incrementCollectionFrequency() {
		this.collectionFrequencyOfTerm++;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public long getCollectionFrequencyOfTerm() {
		return collectionFrequencyOfTerm;
	}

	public void setCollectionFrequencyOfTerm(long collectionFrequencyOfTerm) {
		this.collectionFrequencyOfTerm = collectionFrequencyOfTerm;
	}

	public long getDocumentFrequencyOfTerm() {
		return docIdsOfTerm.size();
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public List<Posting> getPostings() {
		return postings;
	}

	public void setPostings(List<Posting> postings) {
		this.postings = postings;
	}

	public void addDocIdOfTerm(int documentNumber) {
		this.docIdsOfTerm.add(documentNumber);
		this.offset++;
	}

}
