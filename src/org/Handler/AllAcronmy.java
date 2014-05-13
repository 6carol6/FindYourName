package org.Handler;
/**
 * ����һ�������������Դʵ���
 * ���Ǵ������ַ���������ĸ+3�ĳ��ȱ���������3
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
	private boolean[] hasBeenFirst = new boolean[26];
	private WordnetAccess access = new WordnetAccess();
	
	public AllAcronmy(){
		
	}
	public AllAcronmy(ArrayList<String> wordList, ArrayList<Integer> fixed, TrieTree source, int categ,double tfidf){
		
		//����1��ÿ������ȡһ���ֳ����������һ�����ʾ�Ҫ
	//	getSubWord("", wordList);
		
		
		//����2�������������е�������
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
			for(int i = 0; i < 26; i++){
				hasBeenFirst[i] = false;
			}
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
	private void getSubWord(String word, ArrayList<String> wordList){
		if(wordList.size() == 0){
			//�ж��ǲ���һ���ʣ�����ǣ��ͼ���
			if(access.isWord(word))
				rankedList.add(new Words(word, 0));
			return;
		}

		boolean[] hasBeenFirst = new boolean[26];
		for(int j = 0; j < 26; j++){
			hasBeenFirst[j] = false;
		}
		for(int i = 0; i < wordList.get(0).length(); i++){
			if(hasBeenFirst[wordList.get(0).charAt(i)-'a'] ==  true){
				continue;
			}else{
				hasBeenFirst[wordList.get(0).charAt(i)-'a'] = true;
			}
			getSubWord(word+wordList.get(0).charAt(i),  new ArrayList<String>(wordList.subList(1, wordList.size())));
		}
	}
	private void getSubsequence(String word, String charList, int length, int which){
		/*//�ظ�����ĸ��ѡ���������еģ�����
		if(length == 0){
			if(sourceTree.getWord(word) != null){
				acronmyList.add(word);
			}
			return;
		}
		if(charList.length() < length) return;
		
		if(word.equals("")){
			if(hasBeenFirst[charList.charAt(0)-'a'] ==  true){
				System.out.println(charList.charAt(0)+"�Ѿ���������ĸ��");
				getSubsequence(word, charList.substring(1), length);
				return;
			}else{
				System.out.println(charList.charAt(0));
				hasBeenFirst[charList.charAt(0)-'a'] = true;
			}
		}
		getSubsequence(word+charList.substring(0, 1), charList.substring(1), length-1);
		getSubsequence(word, charList.substring(1), length);
		*/
		//�̶�����ĸ��
		if(length == 0){
			if(sourceTree.getWord(word) != null){
				acronmyList.add(word);
			}
			return;
		}
		if(charList.length() < length) return;
		getSubsequence(word+charList.substring(0, 1), charList.substring(1), length-1, which+1);
		if(!fixed.contains(Integer.valueOf(which))){//�̶���ĸһ��Ҫѡ��
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
				//�����ҵ�����Դ�С
				rankedList.add(new Words(word, Math.abs(sourceTFIDF-tfidf)));
				//������ֻ������Ƶ��
				//rankedList.add(new Words(word, tfidf));
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

