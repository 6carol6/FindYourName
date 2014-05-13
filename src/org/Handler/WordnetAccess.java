package org.Handler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISenseKey;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class WordnetAccess {
	IDictionary dict;
	public WordnetAccess(){
		String wnhome = System.getenv("WNHOME");
		String path = wnhome + File.separator + "dict";
		URL url;
		try {
			url = new URL("file", null, path);
			dict = new Dictionary(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<String> getSynonyms(String word){
		ArrayList<String> synonyms = new ArrayList<String>();
		try {
			dict.open();
			IIndexWord idxWord = dict.getIndexWord(word, POS.NOUN);//只换名词
			if(idxWord != null){
				IWordID wordID = idxWord.getWordIDs().get(0);
				IWord iword = dict.getWord(wordID);
				ISynset synset = iword.getSynset();
				for(IWord w : synset.getWords()){
					//System.out.println(w.getLemma().toString());//test
					synonyms.add(w.getLemma().toString());
				}
			}
			dict.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return synonyms;
	}
	public boolean isWord(String str){
		try{
			dict.open();
			IIndexWord idxWordN = dict.getIndexWord(str, POS.NOUN);
			if(idxWordN != null){
				dict.close();
				return true;
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		dict.close();
		return false;
	}
	public boolean isNeg(String word){
		try{
			dict.open();
			IIndexWord idxWordN = dict.getIndexWord(word, POS.ADJECTIVE);
			if(idxWordN != null){
				IWord wd = dict.getWord((ISenseKey) idxWordN.getID());
				//wd.
				//还木有写完。。。。
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		dict.close();
		return false;
	}
	/*
	public static void main(String[] args){
		WordnetAccess access = new WordnetAccess();
		if(access.isWord("happy")) System.out.println("yes");
	}*/
}