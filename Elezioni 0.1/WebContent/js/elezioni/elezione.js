
function creaElezione(){

	var elezione = {
			dataIni: $('#dataIni').val(),
			dataFine: $('#dataFine').val(),
			descr: $('#descr').val(),
			idCarica: $('#carica').val()
		};	
	$.ajax({				
		url : 'creaElezione',
		type : 'POST',				 
		data : elezione,
		success : function(result) {
			resetValueForRegistraElezione();
			$('#resultCreaElezione').html(result);
		},
		error : function(e) {
			alert("Errore durante l\'operazione");
		}
	});
		
//		else{
//			var elezioneForm = {
//					dataIni: $('#dataIni').val(),
//					dataFine: $('#dataFine').val(),
//					descr: $('#descr').val(),
//					
//					titolo: $("#creaDaElezTitolo").val(),
//					durata: $("#creaDaElezDurata").val()
//				};
//			$.ajax({				
//				url : 'creaElezioneConCarica',
//				type : 'POST',	
//				data : elezioneForm,
//				success : function(result) {
//					resetValueForRegistraElezione();
//					$('#resultCreaElezione').html(result);
//				},
//				error: function (xhr, ajaxOptions, thrownError) {
//			        alert("Errore durante l\'operazione. " + thrownError);
//			    }
//			});
//		}
		
}

function resetValueForRegistraElezione(){
	$('#descr').val('');
	$('#dataIni').val('');
	$('#dataFine').val('');
	$('#creaDaElezTitolo').val('');
	$('#creaDaElezDurata').val('');
	
	$("#carica").html("<option value='' selected='selected'> </option>");
	aggiornaSelectCarica();
	
	//per impostare a un valore la select..
	//$("#carica option[value= ]").attr('selected', 'selected');
	
	$('#creaCaricaDiv').hide();
}

function deleteElezione(idE,caricaTitolo){
	if(!window.confirm('Sicuri di voler procedere?'))
		return;
	
	$.ajax({
		type : 'POST',
		url : 'deleteElezioneById',
		dataType : 'json',
		data : {
			idElezione: idE
		},
		success : function(data) {
			cercaElezioni();
			alert('Elezione eliminata. La carica \"'+caricaTitolo+'\" e\" di nuovo disponibile.');
			
		},
		error : function(e) {
			alert("Errore durante l\'operazione");
		}
	});
}

function disattivaElezione(idE,caricaTitolo){
	if(!window.confirm('Sicuri di voler procedere?'))
		return;
	
	$.ajax({
		type : 'POST',
		url : 'disattivaElezioneById',
		dataType : 'json',
		data : {
			idElezione: idE
		},
		success : function(data) {
			mostraElezioniSenzaCandidati();
			alert('Elezione disattivata.');
			
		},
		error : function(e) {
			alert("Errore durante l\'operazione");
		}
	});
}

function cercaElezioni(){
	$.ajax({
		type : 'POST',
		url : 'cercaElezioneByTitolo',
		dataType : 'json',
		data : {
			elezioneDescr : $('#elezione_descr').val()
		},
		success : function(data) {
			var elezioneListContent = tableElezioneLayout(data);
			$('#resultElezione').html(elezioneListContent);
		},
		error : function(e) {
			alert("Errore durante l\'operazione");
		}
	});
}

function tableElezioneLayout(json) {
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
	elezioneListContent += "<th>elimina</th>";
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
		elezioneListContent += "<td id='c_delete'><a href='#' onclick='deleteElezione(\""+elezione.idElezione+"\",\""+elezione.carica.titolo+"\")' >delete</a></td>";
		elezioneListContent += "</tr>";
	});

	elezioneListContent += "</table>";
	return elezioneListContent;
}