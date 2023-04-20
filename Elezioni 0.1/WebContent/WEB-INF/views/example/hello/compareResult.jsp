<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title>Result</title>
	</head>
	<body>
		<h1><c:out value="${output}"></c:out></h1>
		<br/>
		<a href="${pageContext.request.contextPath}/">Main page</a>
	</body>
</html>
