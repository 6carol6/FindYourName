package org.Handler;

import java.io.Serializable;
import java.util.Comparator;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Comparison;

/**
 * 这是一个用于储存单词及其TF-IDF的类
 * 便于对输出结果进行排序
 * @author Carolz
 *
 */
public class Words implements Comparable{
	private String word;
	private double tfidf;
	public Words(){
		
	}
	public Words(String word, double tfidf){
		this.word = word;
		this.tfidf = tfidf;
	}
	public String getWord(){
		return word;
	}
	public double getTFIDF(){
		return tfidf;
	}
	@Override
	public int compareTo(Object compareWord) {
		// TODO Auto-generated method stub
		double tfidf1 = this.getTFIDF();
		double tfidf2 = ((Words) compareWord).getTFIDF();
		if(tfidf1 > tfidf2) return 1;
		else if(tfidf1 == tfidf2) return 0;
		else return -1;
	}
}

