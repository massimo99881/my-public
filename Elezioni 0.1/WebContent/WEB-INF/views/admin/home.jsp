<jsp:include page="../../../WEB-INF/views/jspf/initial_import.jsp" />
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script type="text/javascript" src="<spring:url value="/js/elezioni/guest.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/js/elezioni/utente.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/js/elezioni/carica.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/js/elezioni/elezione.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/js/elezioni/admin.js"/>"></script>
<script>
	$(document).ready(function() {
		
		$("#tabsCarica").tabs();
		$("#subtabsCarica" ).tabs();
		$("#tabsElezione").tabs();
// 		$("#subtabsElezione" ).tabs();

		
		$( "#cercaUtenteButton" ).button().click(function( event ) {
			cercaUtenti();
		});		
				
		$( "#elezioniCercaButton" ).button().click(function( event ) {
			cercaElezioni();
		});
		
		$( "#caricheDispButton" ).button().click(function( event ) {
			cercaCaricaSoloDescr();
		});	
		
		$( "#caricheCercaButton" ).button().click(function( event ) {
			cercaCarica();
		});	
		 
		$( "#creaCarica" ).button().click(function( event ) {
			creaCarica();
		});
		
		
		$( "#creaElezione" ).button().click(function( event ) {
			creaElezione();
		});
		
		$("#subtabsElezione" ).tabs({  
			beforeActivate: function( event, ui ) { 
				
				$("#carica").html("<option value='' selected='selected'> </option>");
				
				var id = ui.newTab.find("a").attr("href");
				if('#subtabsElezione-2'==id){
					aggiornaSelectCarica();
				}
				if('#subtabsElezione-3'==id){
					//mostraElezioniInCorso('','admin');
				}
				if('#subtabsElezione-4'==id){
					mostraElezioniSenzaCandidati();
				}
				
			}
// 			,
//             activate:function(event,ui){                                                       
//             	var selected = ui.newTab.context.id;
                                                    
//             }                                                                          
         });
			
		$( "#dataIni" ).datepicker({ dateFormat: "dd/mm/yy" });
		$( "#dataFine" ).datepicker({ dateFormat: "dd/mm/yy" });
		
		$('#elezioniSaveValidateBotton').button();
		$('#elezioniSaveValidateBotton').bind('click',submitElezioniSelectedValidate);
		
		function submitElezioniSelectedValidate() {
            
            var valoreSelectCarica = $("#carica").val();
			if(isNotEmptyOrNull(valoreSelectCarica)){
				if(valoreSelectCarica!='creaNuovaCarica')
					creaElezione();
			}
            
			//$(this).button("disable");
            $('#formElez').addHidden("selectedValidate","true").submit();
        };
        
        $('#formElez').ajaxForm({
            beforeSubmit : function(){

            },
            success : function(){

            },
            target : "#resultCreaElezione",
            forceSync : true,
            url : 'creaElezioneConCarica',
            contentType: 'application/x-www-form-urlencoded;charset=UTF-8'
        });
        
        $.fn.addHidden = function (name, value) {
            return this.each(function () {
                var input = $("<input>").attr("type", "hidden").attr("name", name).val(value);
                $(this).append($(input));
            });
        };
        
        $( "#visualizzaSoloCariche" ).button().click(function( event ) {
			mostraElezioniInCorso('PerCandidarsi','admin');
			$( "#visualizzaSoloElezioni" ).css({'color':'black'});
			$( "#visualizzaSoloCariche" ).css({'color':'blue'});
		});
		$( "#visualizzaSoloElezioni" ).button().click(function( event ) {
			mostraElezioniInCorso('PerVotare','admin');
			$( "#visualizzaSoloElezioni" ).css({'color':'blue'});
			$( "#visualizzaSoloCariche" ).css({'color':'black'});
		});
		
		
		$("#subtabsUtente" ).tabs({  
			beforeActivate: function( event, ui ) { 

				var id = ui.newTab.find("a").attr("href");
				if("#subtabsUtente-2"==id){
					mostraUtentiMaiCandidati();
				}
				
				
			}                                                                       
         });
		
	});
	
	function controllaValoreSceltaCarica(){
		var valore = $("#carica").val();
		
		if(valore!='creaNuovaCarica'){
			$('#creaCaricaDiv').hide();
		}
		else{
			
		}
	}

	function creaNuovaCarica(){
		$('#creaCaricaDiv').show('slow');
	}
</script>
<div style="float:left;">	
		<b><s:message code="elezioni.title" /></b> | Benvenuto amministratore <b><c:out value="${utente.nome} ${utente.cognome}" /></b>
</div>
<div class="block" style="width:100%;">
	<div id="tabsCarica" >
		<ul>
			<li><a href="#tabs-1">Utenti</a></li>
			<li><a href="#tabs-2">Cariche</a></li>
			<li><a href="#tabs-3">Elezioni</a></li>
		</ul>
		<div id="tabs-1">
			<div id="subtabsUtente">
				<ul>
					<li><a href="#subtabsUtente-1">Attiva/Disattiva</a></li>
					<li><a href="#subtabsUtente-2" >Mai candidati</a></li>
<!-- 					<li><a href="#subtabsUtente-3"></a></li>  -->
				</ul>
				<div id="subtabsUtente-1">
					
					<div id="searchUserMask">
					
						<h3>Ricerca utente:</h3>
						<table>
							<tr>
								<td>Nome</td>
								<td><input type="text" id="nomeUtente" /></td>
				
								<td>Cognome</td>
								<td><input type="text" id="cognomeUtente" /></td>
				
								<td><input type="button" value="cerca" id="cercaUtenteButton" />
							</tr>
						</table>
					</div>
					
					<div id="resultUtente">
					</div>	
					
				</div>
				<div id="subtabsUtente-2">
					<h3>Utenti che non si sono ancora candidati a una elezione</h3>
					<div id="resultUtentiMaiCandidati"></div>
				</div>
				<!--  
				<div id="subtabsUtente-3">
					test
				</div>
				 -->
			</div>	
			
		</div>
		<div id="tabs-2">
			<div id="subtabsCarica">
				<ul>
					<li><a href="#subtabsCarica-1">Visualizza</a></li>
					<li><a href="#subtabsCarica-2" >Crea carica</a></li>
<!-- 					<li><a href="#subtabsCarica-3"></a></li>  -->
				</ul>
				<div id="subtabsCarica-1">
					<div id="searchCaricaMask">
						<table style="float:left;">
							<tr>
								<td>Cariche disponibili</td>
								<td><input type="text" id="titoloCarica" /></td>
								<td><input type="button" value="cerca" id="caricheDispButton" />
							</tr>
						</table>
						<table style="float:left;margin-left:30px;">
							<tr>
								<td>Cerca carica per Elezione</td>
								<td><input type="text" id="descrElezione" /></td>
								<td><input type="button" value="cerca" id="caricheCercaButton" />
								
							</tr>
						</table>									
					</div>	
					<div id="resultCarica" class="block">
					</div>	
					
				</div>
				<div id="subtabsCarica-2">
					<h3>Crea una nuova carica</h3>
					
					<table style="border: 0 !important">
						<tr>
							<td>Titolo</td>
							<td><input type="text" id="Ntitolo" /></td>
						</tr>
						<tr>
							<td>Durata (n°anni)</td>
							<td><input type="text" id="Ndurata" /></td>
						</tr>
						<tr >
							<td colspan="2">
								<input type="button" id="creaCarica" value="Crea" />
							</td>
						</tr>
						<tr >
							<td colspan="2" id="resultCreaCarica"></td>
						</tr>
					</table>
				</div>
				<!--  
				<div id="subtabsCarica-3">
					test
				</div>
				 -->
			</div>			
					
		</div>
		<div id="tabs-3">
			<div id="subtabsElezione">
				<ul>
					<li><a href="#subtabsElezione-1">Cerca </a></li>
					<li><a href="#subtabsElezione-2" >Crea elezione</a></li>
					<li><a href="#subtabsElezione-3">Maschera utenti</a></li>
					<li><a href="#subtabsElezione-4">Senza candidati</a></li>
				</ul>
				<div id="subtabsElezione-1">
					<div id="searchElezioneMask">
						<table style="float:left;">
							<tr>
								<td>Nome elezione: </td>
								<td><input type="text" id="elezione_descr" /></td>
								<td><input type="button" value="cerca" id="elezioniCercaButton" />
							</tr>
						</table>
													
					</div>	
					<div id="resultElezione" class="block">
					</div>	
					
				</div>
				<div id="subtabsElezione-2">
					<table>
						<tr>
							<td ><h2>Crea una nuova elezione</h2>
							<p> Ricorda : le candidature saranno effettuate dalla data di 
							registrazione della carica, fino al giorno precedente la data di inizio votazioni.
							</p>
							</td>
						</tr>
					</table>
					<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
					<form:form id="formElez" method="POST" commandName="elezioneForm" action="creaElezioneConCarica">
					<form:errors path="*" cssClass="errorblock" element="div" />
					<table>
						<tr>
							<td>descrizione</td>
							<td><form:input path="descr" /></td>
							<td><form:errors path="descr" cssClass="error" /></td>
						</tr>
						<tr>
							<td>data inizio votazioni</td>
							<td><form:input path="dataIni" /></td>
							<td><form:errors path="dataIni" cssClass="error" /></td>
						</tr>
						<tr>
							<td>data inizio carica</td>
							<td><form:input path="dataFine" /></td>
							<td><form:errors path="dataFine" cssClass="error" /></td>
						</tr>
						
						<tr>
							<td valign="top">carica</td>
							<td><div class="block"><select onChange="controllaValoreSceltaCarica()" id="carica"></select></div>
								<div class="block" style="display:none;" id="creaCaricaDiv">
									<div class="block">
										
										<table style="border: 0 !important">
											<tr>
												<td>Titolo</td>											
												<td><form:input path="titolo" /></td>
												<td><form:errors path="titolo" cssClass="error" /></td>
											</tr>
											<tr>
												<td>Durata (n°anni)</td>
												<td><form:input path="durata" /></td>
												<td><form:errors path="durata" cssClass="error" /></td>
											</tr>
										</table>
									</div>
									
								</div>
								
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<input type="button" value="salva e valida" id="elezioniSaveValidateBotton" />
								<div id="resultCreaElezione"></div><br/>
							</td>
							
						</tr>
					</table>
					</form:form>
				</div>
				
				<div id="subtabsElezione-3">
					<h3>Elenco elezioni in corso:</h3>
					<br/><a href="#" id="visualizzaSoloCariche">periodo candidature</a> | <a href="#" id="visualizzaSoloElezioni">periodo votazioni</a><br/><br/>
					<div id="resultGuestElezione"></div>
				</div>
				
				<div id="subtabsElezione-4">
					<h3>Elenco elezioni che non hanno avuto alcun candidato:</h3>
					<div id="resultElezioneSenzaCandidato"></div>
				</div>
				 
			</div>
			
		</div>
	</div>
	

</div>
