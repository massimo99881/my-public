<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="true"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Hello World with Spring 3 MVC</title>
	<script type="text/javascript" src="/resources/js/common.js" />
	<script type="text/javascript" src="<c:url value="/resources/js/registration.js"/>" />
	<script type="text/javascript">
		var projectUrl = '<c:url value="/"/>';
		if (projectUrl.indexOf(";", 0) != -1) {
			projectUrl = projectUrl.substring(0, projectUrl.indexOf(";", 0));
		}
	</script>
</head>
<body>
	<fieldset>
		<legend>Registration Form</legend>
		<center>
			<form:form commandName="carica" action="/Elezioni_0.1/caricaRegistrationCall/add" name="caricaForm">

				<form:hidden path="idCarica" />
				<table>
					<tr>
						<td colspan="2" align="left">
							<form:errors path="*" cssStyle="color : red;" />
						</td>
					</tr>
					<tr>
						<td>Descr :</td>
						<td><form:input path="descr" /></td>
					</tr>
					<tr>
						<td>Durata :</td>
						<td><form:input path="durata" /></td>
					</tr>
					<%-- 
					<tr>
						<td>Age :</td>
						<td><form:input path="age" /></td>
					</tr>
					<tr>
						<td>Sex :</td>
						<td><form:select path="sex">
								<form:option value="Male" />
								<form:option value="Female" />
							</form:select></td>
					</tr>
					--%>
					<tr>
						<td colspan="2">
						    <input type="submit" value="Save Changes" />&nbsp;
						    <input type="reset" name="newCarica" value="New Carica"
								   onclick="setAddForm();" disabled="disabled" /> &nbsp;
						    <input type="submit" name="deleteCarica" value="Delete Carica"
								   onclick="setDeleteForm();" disabled="disabled" />
						</td>
					</tr>
				</table>
			</form:form>
		</center>
	</fieldset>
	<c:if test="${!empty caricaList}">

		<br />
		<center>
			<table width="90%">
				<tr style="background-color: gray;">
					<th>Descr</th>
					<th>Durata</th>
				</tr>
				<c:forEach items="${caricaList}" var="carica">
					<tr style="background-color: silver;" id="${carica.idCarica}"
						onclick="setUpdateForm('${carica.idCarica}');">
						<td><c:out value="${carica.descr}" /></td>
						<td><c:out value="${carica.durata}" /></td>
					</tr>
				</c:forEach>
			</table>
		</center>
		<br />

	</c:if>
	
	<br/>
	<a href="${pageContext.request.contextPath}/">Main page</a>
</body>
</html>