<%@page import="edu.mit.jwi.IDictionary"%>
<%@ page language="java" import="java.util.*,org.Handler.*,org.algorithm.*;" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<jsp:useBean id="sourceCollector" class="org.Handler.SourceCollector" scope="page">
</jsp:useBean>
<jsp:useBean id="category" class="org.Handler.CategoryAnalysis" scope="page">
</jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Find Your Name</title>
<link href="./index.css" media="all" rel="stylesheet" type="text/css">
<script type="text/javascript"> 
function getMessage()
{
	var MESS=document.form.message.value;
	
	if(MESS==""||MESS==" ")
	{
		alert("请输入字符串");
		return false;
	}return true;
	
}

</script>
</head>
<body>
<div id="main">
<h1>Find Your Name</h1>
<form name="form" method="POST">
	<input type="text" name="message" />
	<input type="submit" value="Find!" onclick="return getMessage()"/>
	<input type="reset" value="Reset"/>
	<div id = "result">
	<%
		String input =  request.getParameter("message");
		if(input != null){
			input = input.toLowerCase();
			sourceCollector.init(input);
			//用uclassify分类
			/*category.getCategory(sourceCollector.getNoStop());
			String categUclassify = category.getCategUclassify();*/
			//用tf-idf的方法要构建树
			int categ = category.getCategory(sourceCollector.getNoStop());
			double tfidf = category.getTFIDF();
			TrieTreeCreator ttc = new TrieTreeCreator(categ);
			TrieTree tree = ttc.getTree();
			
			
			ChangeWord change = new ChangeWord(sourceCollector.getNoStop());
			//array保存各种替换后的词组（第0个是原词组）
			ArrayList<ArrayList<String>> array = change.getChange();
			ArrayList<String> str = sourceCollector.getInput();
			ArrayList<Integer> noStopWordLocate = sourceCollector.getNoStopLocate();
			for(int i = 0; i < array.size(); i++){
				for(int j = 0; i != 0 && j < noStopWordLocate.size(); j++){
					//换词
					str.set(noStopWordLocate.get(j).intValue(), array.get(i).get(j));
				}
				if(i != 0) out.print("推荐句子:");
				for(int j = 0; j < str.size(); j++){
					out.println(str.get(j));
				}
				out.print("<br />");
				//用tf-idf方法要在树上找词
				AllAcronmy acr = new AllAcronmy(str, sourceCollector.getFixed(), tree, categ, tfidf);
				
				//AllAcronmy acr = new AllAcronmy(str, sourceCollector.getFixed(), categUclassify);
				
				ArrayList<Words> list = acr.getRankedList();
				for(int j = 0; j < list.size(); j++){
					out.println(list.get(j).getWord()+"<br />");
				}
			}
		}
	%>
	</div>
</form>
</div>
</body>
</html>