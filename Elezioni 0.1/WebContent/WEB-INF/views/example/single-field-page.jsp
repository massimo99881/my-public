<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Single field page</title>
</head>
<body>
	<h1>Single form page</h1>
	<p>Don't forget: ${thought}</p>
	<form action="remember" method="post">
		<table>
			<tr>
				<td>To remember:</td>
				<td><input name="thoughtParam" type="text" size="40" /></td>
			</tr>
			<tr>
				<td><input value="submit" type="submit" /></td>
				<td></td>
			</tr>
		</table>
	</form>
	<br />
	<a href="${pageContext.request.contextPath}/">Main page</a>

</body>
</html>