<jsp:include page="../../WEB-INF/views/jspf/initial_import.jsp"/>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script type="text/javascript" src="<spring:url value="/js/elezioni/guest.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/js/elezioni/utente.js"/>"></script>
<script>

	$(document).ready(function() {
		
		$('#tabsGuest').tabs();
		
				
		$("#subtabsGuest" ).tabs({ 
// 			create:function(event,ui){                                                       
// 				mostraElezioniInCorso('','guest');                 
//             } ,
        
			select: function (event, ui) {
	            var active = $("#subtabsGuest").tabs("option", "active");
	            alert($("#subtabsGuest ul>li a").eq(active).attr('href')); 
		    },
			
			beforeActivate: function( event, ui ) {
				var id = ui.newTab.find("a").attr("href");
// 				if('#subtabsGuest-1'==id){
// 					mostraElezioniInCorso('','guest');
// 				}
				if('#subtabsGuest-2'==id){
					mostraStoricoElezioni();
				}
			},
			
			ajaxOptions: {
		        cache: false,
		        error: function (xhr, status, index, anchor) {
		            $(anchor.hash).html(
		      "Couldn't load this tab. We'll try to fix this as soon as possible. " +
		      "If this wouldn't be a demo.");
		        }
			}
		});
		
			
		//$("#tabsGuest").tabs("option", "active", 1);
		
		$( "#buttonRegistraUtente" ).button().click(function( event ) {
			registraUtente();
		});
		$( "#visualizzaSoloCariche" ).button().click(function( event ) {
			mostraElezioniInCorso('PerCandidarsi','guest');
			$( "#visualizzaSoloElezioni" ).css({'color':'black'});
			$( "#visualizzaSoloCariche" ).css({'color':'blue'});
		});
		$( "#visualizzaSoloElezioni" ).button().click(function( event ) {
			mostraElezioniInCorso('PerVotare','guest');
			$( "#visualizzaSoloElezioni" ).css({'color':'blue'});
			$( "#visualizzaSoloCariche" ).css({'color':'black'});
		});
		 
	});
</script>
<div style="float:left;margin-left:10px;">
<b><s:message code="elezioni.title" /></b> | <a href="${pageContext.request.contextPath}/loginutente">Login</a>
</div>
<div class="block" style="width:100%;">
	<div id="tabsGuest" >
		<ul>
			<li><a href="#tabsGuest-1">Elezioni</a></li>
			<li><a href="#tabsGuest-2">Registrati</a></li>
<!-- 			<li><a href="#tabsGuest-3">Altro</a></li> -->
		</ul>
		<div id="tabsGuest-1">
			<div id="subtabsGuest">
				<ul>
					<li><a href="#subtabsGuest-1" >Elezioni in corso</a></li>
					<li><a href="#subtabsGuest-2" >Storico elezioni</a></li>
					<!-- <li><a href="#subtabsGuest-3">test</a></li>  -->
				</ul>
				<div id="subtabsGuest-1">
					<br/><a href="#" id="visualizzaSoloCariche">candidature</a> | <a href="#" id="visualizzaSoloElezioni">votazioni</a><br/><br/>
					<div id="resultGuestElezione"></div>
				</div>
				<div id="subtabsGuest-2">
					<br/>storico elezioni passate:<br/><br/>
					<div id="resultGuestElezioneStorico"></div>
				</div>
				<!-- 
				<div id="subtabsGuest-3">
				</div>
				 -->
			</div>
		</div>
		<div id="tabsGuest-2">
			<div id="resultRegistrazioneUtente">
				<br/><b>Registrazione utente</b><br/><br/>
				<table>
					<tr>
						<td>nome</td>
						<td><input type="text" id="nomeUtente" /></td>
					</tr>
					<tr>
						<td>cognome</td>
						<td><input type="text" id="cognomeUtente" /></td>
					</tr>
					<tr>
						<td>email</td>
						<td><input type="text" id="emailUtente" /></td>
					</tr>
					<tr>
						<td>password</td>
						<td><input type="password" id="passwordUtente" /></td>
					</tr>
					<tr>
						<td colspan="2" >
							<input type="button" value="registrati" id="buttonRegistraUtente" />
						</td>
					</tr>
					<tr>
						<td colspan="2"><div id="infoRegistrazioneUtente"></div></td>
					</tr>
				</table>
			</div>
		</div>
<!-- 		<div id="tabsGuest-3">
			</div> 
-->
	</div>
</div>