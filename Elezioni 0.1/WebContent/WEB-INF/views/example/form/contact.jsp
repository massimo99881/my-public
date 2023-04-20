<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Spring 3 MVC Series - Contact Manager</title>
</head>
<body>
 
 
 
<form:form method="post" action="addContact" commandName="commandNameContact">
 
    <table>
    <tr>
        <td><form:label path="firstname"><spring:message code="label.firstname"/></form:label></td>
        <td><form:input path="firstname" /></td>
    </tr>
    <tr>
        <td><form:label path="lastname"><spring:message code="label.lastname"/></form:label></td>
        <td><form:input path="lastname" /></td>
    </tr>
    <tr>
        <td><form:label path="email"><spring:message code="label.email"/></form:label></td>
        <td><form:input path="email" /></td>
    </tr>
    <tr>
        <td><form:label path="telephone"><spring:message code="label.telephone"/></form:label></td>
        <td><form:input path="telephone" /></td>
    </tr>
    <tr>
        <td colspan="2">
            <input type="submit" value="<spring:message code="label.addcontact"/>"/>
        </td>
    </tr>
</table> 
     
</form:form>

<a href="${pageContext.request.contextPath}/">Main page</a>
</body>
</html>