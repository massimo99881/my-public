/*user actions*/
function mostraElezioniInCorsoPerUtente(tab,currentUser){
	
	$.ajax({
		type : 'POST',
		url : 'mostraElezioniInCorso',
		dataType : 'json',
		data : {
			type : tab
		},
		success : function(elezione) {
			var elezioneListContent = tableElezioneUserLayout(elezione,tab,currentUser);			
			$('#resultUserElezione'+tab).html(elezioneListContent);
			
		},
		error : function(e) {
			alert("Errore durante il caricamento della select ");
		}
	});	
}

function tableElezioneUserLayout(json,type,currentUser) {
	if(json==null || json.length == 0){
		return "<p>Nessun risultato</p>";
	}
	
	var elezioneListContent = "";
	
	elezioneListContent += "<div id='accordion_"+type+"'>";
	$.each(json,function(i, elezione) {
		var showControl = true;
		//non mostrare elezioni 'scadute' agli utenti ma solo ad admin
		
		if('PerVotare'==type && elezione.candidati==0){
			if('admin'!=currentUser)
				showControl = false;
				
		}
		
		if(showControl){
		//title
		elezioneListContent += "<h3 id='elezioneDivTitle_"+elezione.idElezione+"'><b >"+elezione.carica.titolo+"</b>";
		if('PerVotare'==type)
			elezioneListContent +=" - Termine votazioni il: "+formatDate(elezione.dataFine);
		if('PerCandidarsi'==type)
			elezioneListContent +=" - Inizio votazioni il: "+formatDate(elezione.dataIni);;		
		elezioneListContent += "</h3>";	
		
		//body
		elezioneListContent += "<div  >" ;
		//ini
		elezioneListContent += "   <div style='float:left;'>";		
		elezioneListContent += "      <i>"+ elezione.descr +"</i><br/>";
		
		if('PerVotare'==type){
		elezioneListContent += "      <br/>Votazioni a partire dal: "+ formatDate(elezione.dataIni);
		elezioneListContent += "      - Termine votazioni e inizio carica il: "+formatDate(elezione.dataFine);
		elezioneListContent += "      <br/>Votazione per eleggere la carica di \"" + elezione.carica.titolo + "\"";
		elezioneListContent += "      della durata di " + elezione.carica.durata + " anni " ;
		elezioneListContent += "      <br/>Persone che hanno votato: " + elezione.totVoti ;
		}
		
		if('PerCandidarsi'==type){
			
			elezioneListContent += "      <br/>E' possibile candidarsi a partire dal: "+ formatDate(elezione.dataCrea);
			elezioneListContent += "      - Termine candidature il: "+formatDate(elezione.dataIni);
			elezioneListContent += "      <br/>La persona eletta rimmarr&agrave; in carica per " + elezione.carica.durata + " anni.<br/>" ;
			elezioneListContent += "      Data fine mandato il " + formatDate(elezione.dataFineCarica) +"." ;
		}
		
		//elenco candidati per elezione
		if(elezione.candidati>0){
			
			elezioneListContent += "<br/>";
			elezioneListContent += "<table cellspacing='5'>" +
								   "<tr><td valign='top' >";
			elezioneListContent += "<a href='#' onclick='visualizzaCandidati(\""+elezione.idElezione+"\",\"elencoCandidati_"+elezione.idElezione+"\")' >Visualizza candidati</a><br/>";
			elezioneListContent += "</td><td>";
			elezioneListContent += "<select style='display:none;width:220px;' size='5' id='elencoCandidati_"+elezione.idElezione+"' ></select>";
			elezioneListContent += "</td></tr>";
			elezioneListContent += "</table>";
		}
		else if(elezione.candidati==0){
			elezioneListContent += "       -       ";
			
			//controllare che non si verifichi questo caso quando si è già in fase si votazione
			if('PerVotare'==type){
				//elezioneListContent +="<script>$('#elezioneDivTitle_"+elezione.idElezione+"').css({'color':'red !important'});</script>";
				elezioneListContent += "<b style='color:red;'>Non sono ancora presenti candidati</b><br/>";
			}
			else{
				elezioneListContent += "<b>Non sono ancora presenti candidati</b><br/>";
			}
		}
		
		elezioneListContent += "    </div>" ;
		//end ini
		
		//azioni utente: candidatura, voto
		elezioneListContent += "<div id='result_record_"+elezione.idElezione+"' style='float:right;'>" ;
		if('PerVotare'==type ){	
			elezioneListContent += 
			"<a href='#' id='votaButton_"+elezione.idElezione+"' onclick='showDialog(\""+elezione.idElezione+"\");'>vota</a> " ;
			elezioneListContent += 
			"<script>$('#votaButton_"+elezione.idElezione+"').button();</script>" ;
		}
		if('PerCandidarsi'==type /*&& !giaCandidato*/){
			elezioneListContent += 
			"<a href='#' id='candidatiButton_"+elezione.idElezione+"' onclick='doElAction(\""+elezione.idElezione+"\",\"registraCandidaturaUtente\",\"\");'>candidati</a> " ;			
			elezioneListContent += 
				"<script>$('#candidatiButton_"+elezione.idElezione+"').button();</script>";
		}
		elezioneListContent += "</div>" ;
		//end azioni utente
		
		elezioneListContent +="</div>";
		//end body
		
		}
		//end show control
	});

	elezioneListContent += "</div>";
	elezioneListContent += 
		"<script>" +
			"$( '#accordion_"+type+"' ).accordion({" +
				"	create: function( event, ui ) { "+
								"userControl(\""+type+"\",\"\",\"\");"+
							"},"+
				"	beforeActivate: function( event, ui ) { " +
								"userControl(\""+type+"\",\"\",\"\");" +
							"}"+
			"});" +
		"</script>";
	return elezioneListContent;
}

function userControl(type,result,idE){
	
	
	$.ajax({
		type : 'POST',
		url : 'verificaAzioniUtente',
		dataType : 'json',
		data : {
			titolo : type
		},
		success : function(utenti) {
			
			$.each(utenti, function (index, obj) {
				//for debug
				//console.log('id: #result_record_'+obj.idElezione+', '+obj.nrVoti+','+obj.voto);
				
				var elementId = '#result_record_'+obj.idElezione;
				
				if(isNotEmptyOrNull(obj.voto)){
					$(elementId).html('Hai gi&agrave; votato');
				}
				else {
					
					if(isNotEmptyOrNull(obj.nrVoti)) {
						//utente si è candidato
						
						if(obj.nrVoti == 0){
							$(elementId).html('Ti sei candidato. Nessun voto ricevuto.');
						}
						if(obj.nrVoti > 0){
							$(elementId).html('Ti sei candidato. Hai ricevuto '+obj.nrVoti+' voti.');
						}
					
					}
				}
					
				
			});
			
			if(isNotEmptyOrNull(result)){
				$('#result_record_'+idE).html(result);
				
				
			}
		},
		error : function(e) {
			alert("Errore durante il caricamento della select ");
		}
	});
	
	
}

function showDialog(idE)
{ 
	visualizzaCandidati(idE,'elencoCandidatiUser');
	
	$('#salvaVotoUtente').button().click(function( event ) {
		doElAction(idE,'registraVotoUtente','true');
		$('#selezioneCandidatiDialog').dialog('close');
	});
	
	$('#annullaVotoUtente').button().click(function( event ) {
		doElAction(idE,'registraVotoUtente','false');
		$('#selezioneCandidatiDialog').dialog('close');
	});
	//
	$('#selezioneCandidatiDialog').dialog('open');
}

function doElAction(idE,req, infvoto){
	//for debug
	console.log(idE+','+req+','+infvoto);
	
	if('registraCandidaturaUtente'==req){
		if(!window.confirm('Sicuri di voler procedere?'))
			return;
	}
	
	$.ajax({
		type : 'POST',
		url : req,
		data : {
			idElezione: idE	,
			haVotato: infvoto
		},
		success : function(result) {
			
			userControl(req,result,idE);
			if('registraCandidaturaUtente'==req)
				mostraElezioniInCorsoPerUtente('PerCandidarsi','user');
			
		},
		error : function(e) {
			$('#result_record_'+idE).html('Si &egrave; verificato un errore in fase di registrazione.');
		}
	});
}

/*admin actions*/
function registraUtente(){
	$.ajax({
		type : 'POST',
		url : 'registraUtente',
		data : {
			nome: $('#nomeUtente').val(),
			cognome: $('#cognomeUtente').val(),
			account: $('#emailUtente').val(),
			password: $('#passwordUtente').val()
		},
		success : function(result) {
			$('#resultRegistrazioneUtente').html(result);
		},
		error : function(e) {
			$('#infoRegistrazioneUtente').html('Si &egrave; verificato un errore in fase di registrazione.');
		}
	});
}

function impostaStatoUtente(idUtenteParam, statoParam) {

	$.ajax({
		type : 'POST',
		url : 'cambiaStatoUtente',
		data : {
			idUtente : idUtenteParam,
			stato : statoParam
		},
		success : function() {
			visualizzaUtenti();
		},
		error : function(e) {
			alert(e);
		}
	});

}

function cercaUtenti() {
	$.ajax({
		type : 'POST',
		url : 'cercaUtenti',
		dataType : 'json',
		data : {
			nome : $('#nomeUtente').val(),
			cognome : $('#cognomeUtente').val()
		},
		success : function(data) {
			var userListContent = tableUtenteLayout(data);
			$('#resultUtente').html(userListContent);
		},
		error : function(e) {
			alert(e);
		}
	});
}

function tableUtenteLayout(json) {
	if(json==null || json.length == 0){
		return "<p>Nessun risultato</p>";
	}
	
	var userListContent = "";
	userListContent += "<table class='bordered'>";
	userListContent += "<tr>";
	userListContent += "<th>nome</th>";
	userListContent += "<th>cognome</th>";
	userListContent += "<th>account</th>";
	userListContent += "<th>password</th>";
	userListContent += "<th>stato</th>";
	userListContent += "</tr>";

	$.each(json,function(i, utente) {
		userListContent += "<tr>";
		userListContent += "<td id='u_nome'>" + utente.nome + "</td>";
		userListContent += "<td id='u_cognome'>" + utente.cognome+ "</td>";
		userListContent += "<td id='u_account'>" + utente.account+ "</td>";
		userListContent += "<td id='u_password'>" + utente.password+ "</td>";

		if (utente.attivo) {
			userListContent += "<td ><a href='#' onclick='impostaStatoUtente(\""+utente.idUtente+ "\",\"false\")' >attivo</a></td>";
		} else {
			userListContent += "<td><a href='#' onclick='impostaStatoUtente(\""+ utente.idUtente+ "\",\"true\")' >disattivo</a></td>";
		}
		userListContent += "</tr>";

	});

	userListContent += "</table>";
	return userListContent;
}

function visualizzaUtenti() {
	$.ajax({
		type : 'POST',
		url : 'visualizzaUtenti',
		dataType : 'json',

		success : function(data) {

			var userListContent = tableUtenteLayout(data);
			$('#resultUtente').html(userListContent);
		},
		error : function(e) {
			alert(e);
		}
	});
}

function tableUtenteStdLayout(json) {
	if(json==null || json.length == 0){
		return "<p>Nessun risultato</p>";
	}
	
	var userListContent = "";
	userListContent += "<table class='bordered'>";
	userListContent += "<tr>";
	userListContent += "<th>nome</th>";
	userListContent += "<th>cognome</th>";
	userListContent += "<th>account</th>";
	userListContent += "<th>password</th>";
	userListContent += "<th>stato</th>";
	userListContent += "</tr>";

	$.each(json,function(i, utente) {
		userListContent += "<tr>";
		userListContent += "<td id='u_nome'>" + utente.nome + "</td>";
		userListContent += "<td id='u_cognome'>" + utente.cognome+ "</td>";
		userListContent += "<td id='u_account'>" + utente.account+ "</td>";
		userListContent += "<td id='u_password'>" + utente.password+ "</td>";

		if (utente.attivo) {
			userListContent += "<td >attivo</td>";
		} else {
			userListContent += "<td>disattivo</td>";
		}
		userListContent += "</tr>";

	});

	userListContent += "</table>";
	return userListContent;
}

