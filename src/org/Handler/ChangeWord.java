package org.Handler;

import java.util.ArrayList;

public class ChangeWord {
	ArrayList<String> input;
	WordnetAccess access;
	public ChangeWord(){		
	}
	public ChangeWord(ArrayList<String> words){
		access = new WordnetAccess();
		this.input = words;
	}
	public ArrayList<ArrayList<String>> getChange(){
		ArrayList<ArrayList<String>> changed = new ArrayList<ArrayList<String>>();
		changed.add(input);
		create(changed, 0, 0);
		return changed;
	}
	private void create(ArrayList<ArrayList<String>> changed, int index, int count){
		ArrayList<String> synonyms = access.getSynonyms(input.get(index));
		for(int i = 0; i < synonyms.size(); i++){
			ArrayList<String> temp = new ArrayList<String>();
			for(int j = 0; j < index; j++){
				temp.add(changed.get(count).get(j));
			}
			temp.add(synonyms.get(i));//第0个是这个词本身
			if(index == input.size()-1){
				if(!changed.contains(temp)){
					changed.add(temp);
					count++;
				}
			}else{
				create(changed, index+1, count);
			}
		}
	}
}
