<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

	<div class="header well">
		<div class="page-header">
			<h1>
				Ajax Demo <small>Form validation</small>
			</h1>
		</div>

		<ul>
			<li><a href="welcome">1. Welcome i18n example, JSTL and JSP samples</a></li>
			<li><a href="HelloWorld">2. HelloWorld example</a></li>
			<li><a href="contacts">3. Semplice ContactForm di esempio</a></li>
			<li><a href="caricaRegistrationCall">4. Database Management
					example (MyBatis)</a></li>
			<li><a href="loginform">5.Login Form Example</a></li>
			
			<li><a href="AddUser">6.Ajax request : AddUser</a></li>
			<li><a href="AddUserValidation">7.Ajax request and Validation From JSON: AddUserValidation</a></li>
			<li><a href="pda/caricaPda">8.DAO implementation (Reitek's architecture)</a></li>
		</ul>

		
		<h1>Saving Session Example</h1>
		<p>This is Home page.</p>
		<p>Don't forget: ${thought}</p>
		<p>The last student was: ${personObj.firstName}, ${personObj.age}</p>

		<p>
			<a href="person-form">Person page</a> <br /> <a href="single-field">Single
				field page</a>
		</p>

		

	</div>
	
	<div >
		<br />
		<p>From Login Form Example : current user [${logged.userName}]</p>

		<c:choose>
			<c:when test="${logged.userName != null && logged.loggedIn == true}">
				<b style="color:green;">user logged</b>
				<a href="">go to site</a>
			</c:when>
			<c:otherwise>
	             <b style="color:red;">user not logged in</b>
	        </c:otherwise>
		</c:choose>
	</div>
