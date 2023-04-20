<jsp:include page="../../../WEB-INF/views/jspf/initial_import.jsp"/>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script type="text/javascript" src="<spring:url value="/js/elezioni/utente.js"/>"></script>

<div style="float:left;">	
		<b><s:message code="elezioni.title" /></b> | Benvenuto <b><c:out value="${utente.nome} ${utente.cognome}" /></b>
</div>
<div class="block" style="width:100%;">
	<div id="tabsUser" >
		<ul>
			<li><a href="#tabsUser-1">Elezioni</a></li>
			
		</ul>
		<div id="tabsUser-1">
			<div id="subtabsUser">
				<ul>
					<li><a href="#subtabsUser-1">Candidati</a></li>
					
					<li><a href="#subtabsUser-2">Vota</a></li>
				</ul>
				<div id="subtabsUser-1">
					<br/>risultano in corso le seguenti elezioni:<br/><br/>
					<div id="resultUserElezionePerCandidarsi">					
					</div>
				</div>
				
				
				<div id="subtabsUser-2">
					<br/>risultano in corso le seguenti elezioni:<br/><br/>
					<div id="resultUserElezionePerVotare">					
					</div>
				</div>
			</div>
		</div>
		
	</div>
</div>


<div id="selezioneCandidatiDialog" style="display:none;" title="Vota il candidato">
	<div style='clear:both;align:center;'>
	<br/>
	<br/>
		<select id="elencoCandidatiUser"></select>
		<br/>
		<br/>
		<input type="button" id="salvaVotoUtente" value="vota" />
		<input type="button" id="annullaVotoUtente" value="scheda nulla" />
		<br/>
		<br/>
		<br/>
	</div>
	<input style="float:right;" type="button" onclick="$('#selezioneCandidatiDialog').dialog('close');" value="chiudi" />
	
</div>
<script>
	$(document).ready(function() {
		$('#tabsUser').tabs();
		
		$("#subtabsUser" ).tabs({ 
			create:function(event,ui){                                                       
				mostraElezioniInCorsoPerUtente('PerCandidarsi','user');
            } ,
//             load:function(event,ui){                                                       
//             	alert('load');
                                                    
//             } ,
// 			activate:function(event,ui){                                                       
//             	alert('activate');
                                                    
//             } ,
			beforeActivate: function( event, ui ) {
				var id = ui.newTab.find("a").attr("href");
				if('#subtabsUser-1'==id){
					mostraElezioniInCorsoPerUtente('PerCandidarsi','user');
				}
				if('#subtabsUser-2'==id){
					mostraElezioniInCorsoPerUtente('PerVotare','user');
				}
			}
		});
		
		$("#selezioneCandidatiDialog").dialog({			
			autoOpen: false,
		    height:250,
		    width: 300,
		    modal: true	
		});
		
			
	});
</script>