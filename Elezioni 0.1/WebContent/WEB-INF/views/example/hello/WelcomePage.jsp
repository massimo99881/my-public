
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
 "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JSTL Core Tags Example</title>
</head>
<body>

	Prova i18n:
	<br />
	<h3>
		<s:message code="label.title" /><br/>
		fmt:message: <fmt:message key="label.title"></fmt:message>
	</h3>

	<a href="?lang=en">en</a> |
	<a href="?lang=it">it</a>
	<br/>
	<br/>

	<p>1.Semplice stampa del valore precedentemente impostato:</p><br/>
	<c:set var="test" value="JSTL Core Tags"></c:set>
	test=<c:out value="${test}"></c:out>
	<br/>
	<br/>
	
	2.Controllo variabile di sessione:
	<br /> UserName:
	<c:out value="${logged.userName}" default="Not Available"
		escapeXml="true"></c:out>
	<br/>
	<br/>
	
	3.IF Jstl test:<br/>
	<c:set var="weight" value="10.05" />
	weight=<c:out value="${weight}" />
	<br/>
	<br/>
	
	if weight>0 stampa a video il suo valore:<br/>
	<c:set var="weight" value="10" />
	<c:if test="${weight > 0}">
		<c:out value="${weight}" />
	</c:if>
	<br/>
	<br/>
	
	if weight != null stampa l'operazione weight * 0.453592:
	<br/>
	<br/>
	<c:if test="${weight != null}">
	 	Weight of the product is ${weight * 0.453592} kgs. 
	</c:if>



	<br/>
	<br/>
	<c:set var="weight" />
	<c:if test="${weight != null && !empty weight}" >
	  not null and not empty
	</c:if>
	
	<br/>
	<br/>
	Scenario 1 (Weight is not available):
	<!--Weight is not available-->
	
	<c:choose>
		<c:when test="${weight == null}">
             Weight is not provided for this product.
        </c:when>
		<c:when test="${weight <= 0}">
             Incorrect weight. It can not be zero or negative.
        </c:when>
		<c:otherwise>
             Weight of the product is <c:out value="${weight}" /> lbs.
        </c:otherwise>
	</c:choose>

	<br/>
	<br/>
	Scenario 2 (Weight is available but negative value):
	<!--Weight is available but negative value-->
	<c:set var="weight" value="-6" />
		<c:choose>
			<c:when test="${weight == null}">
	             Weight is not provided for this product.
	        </c:when>
			<c:when test="${weight <= 0}">
	             Incorrect weight. It can not be zero or negative.
	        </c:when>
			<c:otherwise>
            	 Weight of the product is <c:out value="${weight}" /> lbs.
        	</c:otherwise>
		</c:choose>

	<br/>
	<br/>
    Scenario 3 (Weight is available but zero value):
    <!--Weight is available but zero value-->
		<c:set var="weight" value="0" />
			<c:choose>
				<c:when test="${weight == null}">
		             Weight is not provided for this product.
		        </c:when>
				<c:when test="${weight <= 0}">
		             Incorrect weight. It can not be zero or negative.
		        </c:when>
				<c:otherwise>
		             Weight of the product is <c:out value="${weight}" /> lbs.
		        </c:otherwise>
			</c:choose>
	<br/>
	<br/>
    Scenario 4 (Weight is available and it is 10 lbs):
    <!--Weight is available and it is 10 lbs.-->
	<c:set var="weight" value="10" />
	<c:choose>
		<c:when test="${weight == null}">
             Weight is not provided for this product.
		</c:when>
		<c:when test="${weight <= 0}">
             Incorrect weight. It can not be zero or negative.
        </c:when>
		<c:otherwise>
             Weight of the product is <c:out value="${weight}" /> lbs.
        </c:otherwise>
	</c:choose>
	
	<br/>
	<br/>
	
	Scenario 5 : exception
	<br/>
	<c:set var="firstNumber" value="10" />
	<c:set var="secondNumber" value="0" />
	<c:catch var="exception">
   		<c:out value="${firstNumber/ secondNumber}"/> 
	</c:catch>
	  
	<c:if test="${exception != null}">
	    Exception thrown is <c:out value="${exception}"/> 
	</c:if>

	<br/>
	<br/>
	Scenario 6: remove Tag
	<br/>
	<c:set var="price" value="10.0" scope="session" />
    price=<c:out value="${price}"/>
    <br/>
    
    <c:remove var="price" />
    price=<c:out value="${price}"/>
    <br/>
    <br/>
    
    ForTokens example
    <br/>
    <c:forTokens delims="," items="25,50,75,100" var="number">
    	<c:out value="${number}" default="25"></c:out>
    </c:forTokens>
	<br/>
    <br/>
    
    <%--URL Param Example
    <br/>
    <c:url value="http://localhost:8080/Elezioni_0.1/compare" var="storeLink">
    
    	<c:param name="input1" value="massimo"></c:param>
     	<c:param name="input2" value="sara"></c:param> 
    </c:url>
    <c:import url="${storeLink}"></c:import>
    
    <br/>
    <br/>--%>
<%--     <c:redirect url="http://www.google.it"></c:redirect> --%>
	Some function :
	<br/>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<c:out value="${fn:length('Hello')}"></c:out><br/>
	<c:out value="${fn:substring('Hello',1,3)}"></c:out><br/>
	<c:out value="${fn:trim(' Hello ')}"></c:out><br/>
	<c:out value="${fn:replace('Hello Jwalant','Jwalant','Bunty')}"></c:out><br/>
	<c:out value="${fn:contains('Hello', 'el')}"></c:out><br/>
	<c:out value="${fn:indexOf('Hello', 'llo')}"></c:out><br/>
	
	<c:set var="array1" value="${fn:split('Hi My name is Jwalant', ' ')}" />
  	<c:out value="${fn:join(array1, '-')}"></c:out>
  	<br/>
  	<br/>
  	
  	<c:set var="array1" value="${fn:startsWith('Hi My name is Jwalant', 'Hi ')}" /><br/>
  	
  	<c:out value="${fn:toLowerCase('Jwalant Patel')}"/><br/>
  	<c:out value="${fn:containsIgnoreCase('JwalantPatel', 'ntp')}"/><br/>
  	
  	Jstl escapeXML :<br/>
  	<c:set var="testString" 
  	value="This is <b>JSTL escapeXML example</b> in this JSTL function tutorial."></c:set>
	Without escapeXml function : ${testString}
	<br/>
	Using escapeXml function : ${fn:escapeXml(testString)}
	<br/>
	<br/>
	Formatting tags:
	<br/>
	
<%-- 	<fmt:formatNumber var="${product.price}" type="NUMBER" maxFractionDigits="2"></fmt:formatNumber> --%>

	minFractionDigits: <fmt:formatNumber value="4578.87" type="NUMBER" minFractionDigits="4"></fmt:formatNumber><br/>
	maxFractionDigits: <fmt:formatNumber value="4578.8743" type="NUMBER" maxFractionDigits="2"></fmt:formatNumber><br/>
	minIntegerDigits: <fmt:formatNumber value="8.8743" type="NUMBER" minIntegerDigits="3"></fmt:formatNumber><br/>
	maxIntegerDigits: <fmt:formatNumber value="4578.8743" type="NUMBER" maxIntegerDigits="2"></fmt:formatNumber><br/>

	<fmt:setLocale value="en_US"/>
    CURRENCY: <fmt:formatNumber value="4578.59" type="CURRENCY"></fmt:formatNumber><br/>
    
    groupingUsed: <fmt:formatNumber value="4578.59" type="NUMBER" groupingUsed="TRUE"></fmt:formatNumber><br/>
    PERCENT: <fmt:formatNumber value="0.785" type="PERCENT" minFractionDigits="3"></fmt:formatNumber><br/>
    
    Setting Locale to US:
    <fmt:setLocale value="en_US"/>
    <fmt:formatNumber value="0.452" type="CURRENCY" minFractionDigits="3"></fmt:formatNumber>
    <br/>
    Setting Locale to UK:
    <fmt:setLocale value="en_GB"/>
    <fmt:formatNumber value="0.452" type="CURRENCY" minFractionDigits="3"></fmt:formatNumber>
 	<br/>
 	Setting Locale to India:
    <fmt:setLocale value="en_IN"/>
    <fmt:formatNumber value="0.452" type="CURRENCY" minFractionDigits="3"></fmt:formatNumber>
    <br/>
    <br/>
    fmt:parseNumber: 
    <!-- Setting Locale to US -->
    <fmt:parseNumber value="456.1432" var="test" integerOnly="TRUE" type="NUMBER"></fmt:parseNumber>
    <c:out value="${test}"></c:out>
    <br/>
    SQL tags:
    <%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
    
    <sql:setDataSource driver="org.postgresql.Driver"
     				   url="jdbc:postgresql://127.0.0.1:5432/elezioni"
     				   var="localSource" 
     				   user="postgres"  
     				   password="password" />

	<sql:query dataSource="${localSource}" 
	           sql="SELECT * FROM carica " 
	           var="result" />
	
	<h2>JSP Script</h2>
	<%!
	  int i = 15;
	  String name = "Jwalant";
	%>
	<br/>
	<%=name %>
	<br/>
	<%@page import="java.util.Date"%>
	<%
    Date date = new Date();
    out.println("Current Time is "+date);
    %>
	<!-- This is a JSP Page -->
		
	<%-- This is a Hidden Comment.  --%>
	 <jsp:useBean id="carica" scope="request" class="it.elezioni.data.example.Carica">   
        <jsp:setProperty name="carica" property="durata" value="3" />
     </jsp:useBean>
	<br/>
	<br/>
	<%
    double num = Math.random();
    if (num > 0.95) {
  %>
      <h2>You'll have a luck day!</h2><p>(<%= num %>)</p>
  <%
    } else {
  %>
      <h2>Well, life goes on ... </h2><p>(<%= num %>)</p>
  <%
    }
    
    out.write("<html>\r\n  ");
    num = Math.random();
    if (num > 0.95) {
       out.write("<h2>You will have a luck day!");
       out.write("</h2><p>(");
       out.print( num );
       out.write(")</p>\r\n");
    } else {
       out.write("\r\n    ");
       out.write("<h2>Well, life goes on ... ");
       out.write("</h2><p>(");
       out.print( num );
       out.write(")</p>\r\n  ");
    }
    out.write("<a href=\"");
    out.print( request.getRequestURI() );
    out.write("\">");
    out.write("<h3>Try Again</h3></a>\r\n");
    out.write("</html>\r\n");
  %>
  
  
  <br/>
  <br/>
  
  <h3>Choose an author:</h3>
  <form method="get">
    <input type="checkbox" name="author" value="Tan Ah Teck">Tan
    <input type="checkbox" name="author" value="Mohd Ali">Ali
    <input type="checkbox" name="author" value="Kumar">Kumar
    <input type="submit" value="Query">
  </form>
 
  <%
  String[] authors = request.getParameterValues("author");
  if (authors != null) {
  %>
    <h3>You have selected author(s):</h3>
    <ul>
  <%
      for (int i = 0; i < authors.length; ++i) {
  %>
        <li><%= authors[i] %></li>
  <%
      }
  %>
    </ul>
    <a href="<%= request.getRequestURI() %>">BACK</a>
  <%
  }
  %>
  	<br/>
  	<br/>
  	<p>The square root of 5 is <%= Math.sqrt(5) %></p>
	
	<p>Current time is: <%=  new java.util.Date() %></p>
	
	<br/>
	<br/>
	<%@ include file="query.jsp" %>
	
	<br/><a href="${pageContext.request.contextPath}/">Main page</a>
</body>
</html>