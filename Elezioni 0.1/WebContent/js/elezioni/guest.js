function mostraStoricoElezioni(){
	$.ajax({
		type : 'POST',
		url : 'mostraStoricoElezioniGuest',
		dataType : 'json',
//		data : {
//			titolo : ''
//		},
		success : function(elezione) {
			var elezioneListContent = tableElezioneGuestLayout(elezione,'storico');
			$('#resultGuestElezioneStorico').html(elezioneListContent);
		},
		error : function(e) {
			alert("Errore durante il caricamento della select ");
		}
	});
}

function mostraElezioniInCorso(tab,currentUser){
	$.ajax({
		type : 'POST',
		url : 'mostraElezioniInCorso',
		dataType : 'json',
		data : {
			type : tab
		},
		success : function(elezione) {
			var elezioneListContent ;
			if(tab==null || tab==''){
				elezioneListContent = tableElezioneGuestLayout(elezione,'incorso','',currentUser);
			}
			else{
				elezioneListContent = tableElezioneGuestLayoutSpecialized(elezione,tab,currentUser);
			}
			
			$('#resultGuestElezione').html(elezioneListContent);
		},
		error : function(e) {
			alert("Errore durante il caricamento della select ");
		}
	});
}

function tableElezioneGuestLayoutSpecialized(json,type,currentUser){
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
		
		elezioneListContent +="</div>";
		//end body
		
		}
		//end show control
		
	});

	elezioneListContent += "</div>";
	elezioneListContent += "<script>" +
			"$( '#accordion_"+type+"' ).accordion();</script>";
	return elezioneListContent;
}

function tableElezioneGuestLayout(json,opt,type,currentUser) {
	if(json==null || json.length == 0){
		return "<p>Nessun risultato</p>";
	}
	
	var elezioneListContent = "";
	
	elezioneListContent += "<div id='accordion_"+opt+"'>";
	$.each(json,function(i, elezione) {
		
		elezioneListContent += "<h3><b>"+elezione.carica.titolo+"</b>";
		if('incorso'==opt)
			elezioneListContent +=" - termine votazioni il: "+formatDate(elezione.dataFine)+"</h3>";
		if('storico'==opt)
			elezioneListContent +=" - in carica fino al "+formatDate(elezione.dataFineCarica)+"</h3>";
		elezioneListContent += "<div>";
		if('storico'==opt && isNotEmptyOrNull(elezione.nomeEletto)){			
			elezioneListContent += "<fieldset><b>"+elezione.nomeEletto+" "+elezione.cognomeEletto+"</b> &egrave; il vincitore</fieldset>";
		}
		elezioneListContent += "<i>"+ elezione.descr +"</i><br/>";
		elezioneListContent += "<br/>Votazioni a partire dal: "+ formatDate(elezione.dataIni);
		elezioneListContent += " - Inizio carica il: "+formatDate(elezione.dataFine);
		elezioneListContent += "<br/>Votazione per eleggere la carica di \"" + elezione.carica.titolo + "\"";
		elezioneListContent += " della durata di " + elezione.carica.durata + " anni " ;
		elezioneListContent += "<br/>Persone che hanno votato: " + elezione.totVoti ;
		if('incorso'==opt )
			if(elezione.candidati>0){
				elezioneListContent += "<br/>";
				elezioneListContent += "<a href='#' onclick='visualizzaCandidati(\""+elezione.idElezione+"\",\"elencoCandidati_"+elezione.idElezione+"\")' >Visualizza candidati</a><br/>";
				elezioneListContent += "<select style='display:none;' size='5' id='elencoCandidati_"+elezione.idElezione+"' ></select>";
			}
			else if(elezione.candidati==0){
				elezioneListContent += "       -       ";
				elezioneListContent += "<b>Non sono ancora presenti candidati</b><br/>";		
			}
		elezioneListContent += "</div>";
		
	});

	elezioneListContent += "</div>";
	elezioneListContent += "<script>$( '#accordion_"+opt+"' ).accordion();</script>";
	return elezioneListContent;
}


