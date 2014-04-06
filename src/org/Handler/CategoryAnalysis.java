package org.Handler;
/**
 * ����һ�����ڷ������
 * @author Carolz
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

public class CategoryAnalysis {
	final int MAXCATEG = 29;//һ����20�����
	double tfidf = 0;
	int n = 0; //������
	public CategoryAnalysis(){
		
	}
	public int getCategory(ArrayList<String> input) throws SQLException{
		DBLinker db = new DBLinker();
		Connection conn = (Connection)db.getConnect();
		int category = 0;
		int tf_idf = 0;
		n = input.size();
		double[] tfidf = new double[MAXCATEG];
		//ÿ�����ʶ���ÿһ�������tfidf���������������ѭ��Ӧ����ÿһ�����ࡣ
		for(int i = 1; i <= MAXCATEG; i++){
			for(int j = 0; j < input.size(); j++){
				tfidf[i-1] += getTF(db, conn, input.get(j), i)*getIDF(db, conn, input.get(j));
			}
		}
		conn.close();
		
		//ѡ��tfidfֵ����������
		category = chooseTheMax(tfidf);
		for(int i = 0; i < MAXCATEG; i++){
			System.out.println(i+":"+tfidf[i]);
		}
		System.out.println("����ţ�" + category+"\n��ֹͣ������"+input.size());
		return category;
	}
	private double getTF(DBLinker db, Connection conn, String word, int categ){
		double tf = 0;
		String sql = "select * from tf where words = '" + word + "';";
		ResultSet rs = db.search(conn, sql);
		try {
			if(rs.next()){
				tf = Double.parseDouble(rs.getString(categ+1));
			}else{
				System.out.println("��ร����������>��<(TFֵ�鲻��)"+word);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tf;
	}
	private double getIDF(DBLinker db, Connection conn, String word){
		double idf = 0;
		String sql = "select * from idf where words = '" + word + "';";
		ResultSet rs = db.search(conn, sql);
		try {
			if(rs.next()){
				idf = Double.parseDouble(rs.getString(2));
			}else{
				System.out.println("��ร����������>��<(IDFֵ�鲻��)");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idf;
	}
	private int chooseTheMax(double[] tfidf){
		this.tfidf = tfidf[0];
		int categ = 0;
		for(int i = 0; i < tfidf.length; i++){
			if(this.tfidf < tfidf[i]){
				this.tfidf = tfidf[i];
				categ = i;
			}
		}
		this.tfidf = this.tfidf/n;
		return categ+1;
	}
	public double getTFIDF(){
		return tfidf;
	}
}
