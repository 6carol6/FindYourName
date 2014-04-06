package org.Handler;
/**
 * 这是一个整理输入字符串的类
 * 输入字符串中的每一个单词被取出放入input数组
 * 每一个字符放入allChar，用于整理子序列
 * 去停止词之后的单词放入noStop，用于分类
 * @author Carolz
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.jasper.tagplugins.jstl.core.Remove;

import com.mysql.jdbc.Connection;

public class SourceCollector {
	final int MAXN = 30;//最多支持30个单词输入
	ArrayList<String> input = new ArrayList<String>();//输入的所有字符串
	ArrayList<Character> allChar = new ArrayList<Character>();//输入的所有字符
	ArrayList<String> noStop = new ArrayList<String>();//输入的所有字符串（去停止词）
	ArrayList<Integer> fixed = new ArrayList<Integer>();//固定字母的位置
	ArrayList<Integer> noStopLocate = new ArrayList<Integer>();//非停止词的首字母位置
	public SourceCollector(){
		
	}
	public void init(String inputString){
		String[] messwords = new String[MAXN];
		inputString = inputString.trim();
		messwords = inputString.split(" ");
		//fixed.add(Integer.valueOf(0));
		for(int i = 0; i < messwords.length; i++){
			input.add(messwords[i]);
			//fixed.add(fixed.get(i)+Integer.valueOf(messwords[i].length()));
		}
		//fixed.remove(messwords.length);
		char temp;
		for(int i=0;i<inputString.length();i++){
			if((temp = inputString.charAt(i)) != ' '){
				allChar.add(Character.valueOf(temp));
			}
		}
		delStopWord();//去停止词
	}
	private void delStopWord(){
		try {
			DBLinker db = new DBLinker();
			Connection conn = (Connection)db.getConnect();
			int f_location = 0;
			for(int i = 0; i < input.size(); i++){
				String sql = "select * from stopwords where words = '" + input.get(i) + "';";
				ResultSet rs = db.search(conn, sql);
				if(!rs.next()){//如果停止词表里没有这个词
					noStop.add(input.get(i));
					noStopLocate.add(Integer.valueOf(i));
					if(fixed.isEmpty())
						fixed.add(Integer.valueOf(f_location));
					else if(Math.random() < 0.8)
						fixed.add(Integer.valueOf(f_location));
				}
				f_location += input.get(i).length();
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<Character> getAllChar(){
		return allChar;
	}
	public ArrayList<String> getInput(){
		return input;
	}
	public ArrayList<String> getNoStop(){
		return noStop;
	}
	public ArrayList<Integer> getFixed(){
		return fixed;
	}
	public ArrayList<Integer> getNoStopLocate(){
		return noStopLocate;
	}
}