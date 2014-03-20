package org.Handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.algorithm.*;

public class TrieTreeCreator {
	private DBLinker db = null;
	private Connection conn = null;
	TrieTree tree = null;
	public TrieTreeCreator(){
		
	}
	public TrieTreeCreator(int categ){
		db = new DBLinker();
		conn = db.getConnect();
		String sql = "select words from class" + categ + ";";
		ResultSet rs = db.search(conn, sql);
		tree = new TrieTree();
		try {
			while(rs.next()){
				tree.addNode(rs.getString(1));
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		//文本找词
		tree = new TrieTree();
      //  File file = new File("aa.txt"); //在E盘的根目录下确实有这个文件 
		File f = new File("paper1.txt");
		if(!f.exists()){
			System.out.println("该文件不存在>。<");
			System.exit(0);
		}

		try{
			Scanner input = new Scanner(f);
			while(input.hasNext()){
				String line = input.nextLine();
				String[] temp = line.split(" ");
				for(String word: temp){
					if(word.matches("[a-z|A-Z]*") && !word.equals(" ")){
						System.out.println(word);
						tree.addNode(word.toLowerCase());
					}
				}
			}
		}catch(FileNotFoundException e){
			System.out.println(e);
		}
		*/
	}
	public TrieTree getTree(){
		return tree;
	}
}
