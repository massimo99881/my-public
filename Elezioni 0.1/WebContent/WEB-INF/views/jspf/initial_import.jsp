<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>

<link rel="stylesheet" href="<spring:url value="/css/app.css"/>" type="text/css"/>
<link rel="stylesheet" href="<spring:url value="/css/elezioni.css"/>" type="text/css"/>
<link rel="stylesheet" href="<spring:url value="/css/table.css"/>" type="text/css"/>
<link rel="stylesheet" href="<spring:url value="/css/tabs.css"/>" type="text/css"/>
<link rel="stylesheet" href="<spring:url value="/css/jqueryui/development-bundle/themes/base/jquery.ui.all.css"/>" type="text/css"/>

<script type="text/javascript" src="<spring:url value="/js/moment.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/js/json/json2.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/js/jq/jquery-1.7.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/js/jq/jquery.ui.timepicker.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/js/jq/jqueryui/js/jquery-ui-1.10.0.custom.js"/>"></script>
<script type="text/javascript" src="<spring:url value='/js/jquery.form.js'/>"></script>
<script type="text/javascript" src="<spring:url value="/js/common.js"/>"></script>

	<div style="float:right;margin-right:5px;">
	<s:message code="language" />	<a href="?lang=en">en</a> |
	<a href="?lang=it">it</a>
	</div>

<c:if test="${guestUsr == null}" >

	<c:if test="${utente == null || utente.idUtente == null}">
		<center>
		<div id="loginFormUtente" style="margin-top:100px;clear:both;">
			<fieldset>
				<h3>
					<s:message code="elezioni.title" />				
				</h3>
			</fieldset><br/>
		
		
			<a href="esterno"><fmt:message key="esternalaccess.title"></fmt:message></a><br/><br/>
			<form:form action="loginutente" commandName="utente">
				<table>
					<tr><td>account:</td></tr>
					<tr><td><form:input path="account" /></td></tr>
					<tr><td>password:</td></tr>
					<tr><td><form:password path="password" /></td></tr>
					
					<tr><td><input type="submit" value="Login" /></td></tr>
				</table>
			</form:form>
		</div>
		</center>
	</c:if>
	
	<c:if test="${utente != null && utente.idUtente != null}">
		<div style="float:right;">	
			<font style="color: green;">utente loggato</font> | <a href="logoututente">Logout</a>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
	</c:if>
</c:if>	