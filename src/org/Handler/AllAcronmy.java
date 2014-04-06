package org.Handler;
/**
 * 这是一个用于生成缩略词的类
 * 我们从已有字符串的首字母+3的长度遍历到长度3
 * @author Carolz
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.algorithm.*;

public class AllAcronmy {
	private HashSet<String> acronmyList = new HashSet<String>();
	private ArrayList<Words> rankedList = new ArrayList<Words>();
	private ArrayList<Integer> fixed;
	private TrieTree sourceTree = null; 
	private int categ = 0;
	public AllAcronmy(){
		
	}
	public AllAcronmy(ArrayList<String> wordList, ArrayList<Integer> fixed, TrieTree source, int categ,double tfidf){
		setFixed(fixed);
		setCateg(categ);
		setTrieTree(source);
		String str = new String("");
		for(int i = 0; i < wordList.size(); i++){
			str += wordList.get(i);
		}
		int len = wordList.size();
		if(len <= 4) len+=3;
		else if(len > 8) len = 7;
		for(int length = len-3; length < len+3; length++){
			getSubsequence("", str, length, 0);
		}
		rank(tfidf);
		/*
		//test
		Iterator<String> iter = acronmyList.iterator();
		while(iter.hasNext()){
			System.out.println(iter.next());
		}*/
	}
	private void getSubsequence(String word, String charList, int length, int which){
		if(length == 0){
			if(sourceTree.getWord(word) != null){
				acronmyList.add(word);
			}
			return;
		}
		if(charList.length() < length) return;
		getSubsequence(word+charList.substring(0, 1), charList.substring(1), length-1, which+1);
		if(!fixed.contains(Integer.valueOf(which))){//固定字母一定要选！
			getSubsequence(word, charList.substring(1), length, which+1);
		}
	}
	public void rank(double sourceTFIDF){
		DBLinker db = new DBLinker();
		Connection conn = db.getConnect();
		
		Iterator<String> iter = acronmyList.iterator();
		try {
			while(iter.hasNext()){
				String word = iter.next();
				String sql = "select tf" + categ + " from tf where words='"+ word +"';";
				ResultSet rs = db.search(conn, sql);
				double tfidf = 0;
				if(rs.next())
					tfidf = Double.valueOf(rs.getString(1));
				sql = "select idf from idf where words ='" + word + "';";
				rs = db.search(conn, sql);
				if(rs.next())
					tfidf *= Double.valueOf(rs.getString(1));
				//这里找的是相对大小
				rankedList.add(new Words(word, Math.abs(sourceTFIDF-tfidf)));
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(rankedList);
		
		//test
		for(int i = 0; i < rankedList.size(); i++){
			System.out.println(rankedList.get(i).getWord()+":"+rankedList.get(i).getTFIDF());
		}
	}
	

	private void setFixed(ArrayList<Integer> fixed){
		this.fixed = fixed;
	}
	private void setTrieTree(TrieTree source){
		sourceTree = source;
	}
	private void setCateg(int categ){
		this.categ = categ;
	}
	public ArrayList<Words> getRankedList(){
		return rankedList;
	}
}

