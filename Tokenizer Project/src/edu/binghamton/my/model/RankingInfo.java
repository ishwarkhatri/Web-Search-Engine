package edu.binghamton.my.model;

public class RankingInfo implements Comparable<RankingInfo> {

	double rank;

	int docId;

	String docPath;

	String headline;

	String computedSimilarity;

	String snippet;

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getComputedSimilarity() {
		return computedSimilarity;
	}

	public void setComputedSimilarity(String computedSimilarity) {
		this.computedSimilarity = computedSimilarity;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public int compareTo(RankingInfo o) {
		if(this.rank < o.rank)
			return 1;
		if(this.rank > o.rank)
			return -1;
		return 0;
	}

}
