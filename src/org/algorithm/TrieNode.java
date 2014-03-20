package org.algorithm;

import java.util.TreeMap;

public class TrieNode extends Node{
	protected TreeMap<Character, TrieNode> next = new TreeMap<Character, TrieNode>();
	public TrieNode(){
		
	}
	public TrieNode(char ch,String wd){
		character = ch;
		word = wd;
	}
	public TreeMap<Character, TrieNode> next(){
		return next;
	}
}
