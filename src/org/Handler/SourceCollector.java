package org.Handler;
/**
 * ����һ�����������ַ�������
 * �����ַ����е�ÿһ�����ʱ�ȡ������input����
 * ÿһ���ַ�����allChar����������������
 * ȥֹͣ��֮��ĵ��ʷ���noStop�����ڷ���
 * @author Carolz
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.jasper.tagplugins.jstl.core.Remove;

import com.mysql.jdbc.Connection;

public class SourceCollector {
	final int MAXN = 30;//���֧��30����������
	ArrayList<String> input = new ArrayList<String>();//����������ַ���
	ArrayList<Character> allChar = new ArrayList<Character>();//����������ַ�
	ArrayList<String> noStop = new ArrayList<String>();//����������ַ�����ȥֹͣ�ʣ�
	ArrayList<Integer> fixed = new ArrayList<Integer>();//�̶���ĸ��λ��
	ArrayList<Integer> noStopLocate = new ArrayList<Integer>();//��ֹͣ�ʵ�����ĸλ��
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
		delStopWord();//ȥֹͣ��
	}
	private void delStopWord(){
		try {
			DBLinker db = new DBLinker();
			Connection conn = (Connection)db.getConnect();
			int f_location = 0;
			for(int i = 0; i < input.size(); i++){
				String sql = "select * from stopwords where words = '" + input.get(i) + "';";
				ResultSet rs = db.search(conn, sql);
				if(!rs.next()){//���ֹͣ�ʱ���û�������
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