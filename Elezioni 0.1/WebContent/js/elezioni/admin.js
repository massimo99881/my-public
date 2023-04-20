function mostraUtentiMaiCandidati(){
	$.ajax({
		type : 'POST',
		url : 'utentiMaiCandidati',
		
		success : function(data) {		
			var userListContent = tableUtenteStdLayout(data);
			$('#resultUtentiMaiCandidati').html(userListContent);
		},
		error : function(e) {
			alert("Errore durante l\'operazione");
		}
	});
}

function mostraElezioniSenzaCandidati(){
	$.ajax({
		type : 'POST',
		url : 'elezioniSenzaCandidati',
		
		success : function(data) {		
			var elezioneListContent = tableElezioneSenzaCandidatiLayout(data);
			$('#resultElezioneSenzaCandidato').html(elezioneListContent);
		},
		error : function(e) {
			alert("Errore durante l\'operazione");
		}
	});
}

function tableElezioneSenzaCandidatiLayout(json) {
	if(json==null || json.length == 0){
		return "<p>Nessun risultato</p>";
	}
	
	var elezioneListContent = "";
	elezioneListContent += "<table class='bordered'>";
	elezioneListContent += "<tr>";
	elezioneListContent += "<th>carica</th>";
	elezioneListContent += "<th>elezione</th>";
	elezioneListContent += "<th>registrata</th>";
	elezioneListContent += "<th>termine cand</th>";
	elezioneListContent += "<th>termine votaz</th>";
	
	elezioneListContent += "<th>durata</th>";
	elezioneListContent += "<th>termine carica</th>";
	elezioneListContent += "<th>stato</th>";
	elezioneListContent += "<th>disattiva</th>";
	elezioneListContent += "</tr>";

	$.each(json,function(i, elezione) {
		elezioneListContent += "<tr>";
		elezioneListContent += "<td id='c_titolo'>" + elezione.carica.titolo + "</td>";
		elezioneListContent += "<td id='e_descr'>" + elezione.descr + "</td>";
		elezioneListContent += "<td id='e_dataCrea'>" + formatDate(elezione.dataCrea) + "</td>";
		elezioneListContent += "<td id='e_dataIni'>" + formatDate(elezione.dataIni)+ "</td>";
		elezioneListContent += "<td id='e_dataFine'>" + formatDate(elezione.dataFine)+ "</td>";
		elezioneListContent += "<td id='c_durata'>" + elezione.carica.durata + "</td>";
		elezioneListContent += "<td id='c_term'>" + formatDate(elezione.dataFineCarica) + "</td>";
//		elezioneListContent += "<td id='c_modifica'>";
//		elezioneListContent += "<a href='#' onclick='modificaCarica(\""+ carica.idCarica+ "\")' >modifica</a>";
//		elezioneListContent += "</td>";
//		elezioneListContent += "<td id='c_cancella'>";
//		elezioneListContent += "<a href='#' onclick='cancellaCarica(\""+ carica.idCarica+ "\")' >cancella</a>";
//		elezioneListContent += "</td>";
		elezioneListContent += "<td id='e_stato'>" + elezione.stato + "</td>";
		elezioneListContent += "<td id='c_disattiva'><a href='#' onclick='disattivaElezione(\""+elezione.idElezione+"\",\""+elezione.carica.titolo+"\")' >disattiva</a></td>";
		elezioneListContent += "</tr>";
	});

	elezioneListContent += "</table>";
	return elezioneListContent;
}