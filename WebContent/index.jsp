<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'index.jsp' starting page</title>
  </head>
  <body>
    <form margin="300px" method="get" action="/CollectionPlus/my/signin.do">
    	用户名:<input type="text" name="account"><br>
    	密码:&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="password"><br>
    	<input type="submit" value="登陆"><br>
    </form>
    <%String info = (String)request.getAttribute("info");
    if (info!=null){%>
    	<p><%=info%></p>
    <%}%>  
  </body>
</html>
