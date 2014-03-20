package org.algorithm;

import java.util.Comparator;

public class TrieTree {
	private TrieNode root = new TrieNode();
	private Comparator<String> word_comparator = new Comparator<String>(){
		@Override
		public int compare(String s1, String s2) {
			// TODO Auto-generated method stub
			return s1.compareTo(s2);
		}
	};
	public TrieTree(){
		
	}
	public void addNode(String word){
		TrieNode parent_node = root;
		for(int i = 0; i < word.length(); i++){
			if(!parent_node.next().containsKey(new Character(word.charAt(i)))){
				parent_node.next().put(new Character(word.charAt(i)), new TrieNode(word.charAt(i), null));
			}
			parent_node = parent_node.next().get(new Character(word.charAt(i)));
		}
		parent_node.setWord(word);
	}
	public TrieNode getNode(String key){
		if(key.equals("")) return root;
		TrieNode result = root;
		for(int i = 0; i < key.length(); i++){
			if(!result.next().containsKey(new Character(key.charAt(i)))) return null;
			result = result.next().get(new Character(key.charAt(i)));
		}
		return result;
	}
	public String getWord(String key){
		TrieNode node = getNode(key);
		if(node == null || node.getWord() == null) return null;
		return node.getWord();
	}
	
}
