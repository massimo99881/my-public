
function creaCarica(){
	var carica = {
		titolo: $('#Ntitolo').val(),
		durata: $('#Ndurata').val()
	};
	
	$.ajax({
		type : 'POST',
		url : 'creaNuovaCarica',
		//dataType : 'json',
		data : carica,
		success : function(data) {		
			$('#Ntitolo').val('');
			$('#Ndurata').val('');
			$('#resultCreaCarica').html(data);
		},
		error : function(e) {
			alert("Errore durante l\'operazione");
		}
	});
}

function aggiornaSelectCarica(){
	
		$.ajax({
			type : 'POST',
			url : 'cercaCaricaSoloDescr',
			dataType : 'json',
			data : {
				titolo : ''
			},
			success : function(carica) {
				//console.log(carica);
				$.each(carica, function (index, value) {
					$("#carica").append('<option value="'+value.idCarica+'">'+value.titolo+' ('+value.durata+' anni)</option>');
				});
				
				$("#carica").append('<option value=\"creaNuovaCarica\" style=\"background: white;\" onclick=\"creaNuovaCarica();\">CREA NUOVA CARICA</option>');
			},
			error : function(e) {
				alert("Errore durante il caricamento della select ");
			}
		});
	
}

function cercaCaricaSoloDescr(){
	$.ajax({
		type : 'POST',
		url : 'cercaCaricaSoloDescr',
		dataType : 'json',
		data : {
			titolo : $('#titoloCarica').val()
		},
		success : function(data) {
			var caricaListContent = tableCaricaLayout(data,false);
			$('#resultCarica').html(caricaListContent);
		},
		error : function(e) {
			alert("Errore durante l\'operazione");
		}
	});
}

function cercaCarica() {
	$.ajax({
		type : 'POST',
		url : 'cercaCaricaAdmin',
		dataType : 'json',
		data : {
			titolo : $('#titoloCarica').val(),
			elezione : $('#descrElezione').val()
		},
		success : function(data) {
			var caricaListContent = tableCaricaLayout(data,true);
			$('#resultCarica').html(caricaListContent);
		},
		error : function(e) {
			alert("Errore durante l\'operazione");
		}
	});
}

function tableCaricaLayout(json,opt) {
	if(json==null || json.length == 0){
		return "<p>Nessun risultato</p>";
	}
	
	var caricaListContent = "";
	caricaListContent += "<table class='bordered'>";
	caricaListContent += "<tr>";
	caricaListContent += "<th>carica</th>";
	caricaListContent += "<th>durata</th>";
	if(opt){
		caricaListContent += "<th>elezione</th>";		
	}
	else{
		caricaListContent += "<th>modifica</th>";
		caricaListContent += "<th>cancella</th>";
	}
		
	caricaListContent += "</tr>";

	if(opt){
		$.each(json,function(i, elezione) {
				
			caricaListContent += "<tr>";
			caricaListContent += "<td id='c_titolo'>" + elezione.carica.titolo + "</td>";
			caricaListContent += "<td id='c_durata'>" + elezione.carica.durata+ "</td>";
			caricaListContent += "<td id='c_elezione'>" + elezione.descr + "</td>";				
			caricaListContent += "</tr>";		
		});
	}
	else{
		$.each(json,function(i, carica) {
			caricaListContent += "<tr>";
			caricaListContent += "<td id='c_titolo'>" + carica.titolo + "</td>";
			caricaListContent += "<td id='c_durata'>" + carica.durata+ "</td>";
			caricaListContent += "<td id='c_modifica'>";
			caricaListContent += "<a href='#' onclick='modificaCarica(\""+ carica.idCarica+ "\");' >modifica</a>";
			caricaListContent += "</td>";
			caricaListContent += "<td id='c_cancella'>";
			caricaListContent += "<a href='#' onclick='cancellaCarica(\""+ carica.idCarica+ "\");' >cancella</a>";
			caricaListContent += "</td>";
			caricaListContent += "</tr>";
		});
	}
		
	
	caricaListContent += "</table>";
	return caricaListContent;
}

function modificaCarica(idCarica){
	
	$.ajax({
		type : 'POST',
		url : 'cercaCaricaById',
		dataType : 'json',
		data : {
			idCarica : idCarica			
		},
		success : function(carica) {
			var div = "";
			div += "<table class='bordered'>"
				+ "<tr><td colspan='2'><h2>Modifica Carica</h2></td></tr>"
				    + "<tr>"
				    + "<td>Titolo</td>"
				    + "<td><input type='text' id='Modtitolo' value='"+carica.titolo+"' /></td>"
				    + "</tr>"
				    + "<tr>"
				    + "<td>Durata (anni)</td>"
				    + "<td><input type='text' id='Moddurata' value='"+carica.durata+"' /></td>"
				    + "</tr>"
				    + "<tr >"
				    + "<td colspan='2'>"
				    + "<input type='button' id='modCaricaButton' value='Modifica' />"
				    + "<input type='button' id='modCaricaAnnulla' value='Indietro' />"
				    + "</td>"
				    + "</tr>"
				    + "<tr >"
				    + "<td colspan='2' id='resultModCarica'></td>"
				    + "</tr>"
				    + "</table>";
			
			$('#resultCarica').hide();
			$('#resultCarica').html(div);
			$('#modCaricaAnnulla').button().click(function( event ) {
				cercaCaricaSoloDescr();
			});
			$('#modCaricaButton').button().click(function( event ) {
				if(!window.confirm('Sicuri di voler modificare?'))
					return;
				
				carica.titolo = $('#Modtitolo').val();
				carica.durata = $('#Moddurata').val();
				
				$.ajax({
					type : 'POST',
					url : 'modificaCarica',			
					data : carica,
					success : function(result) {
						$('#Modtitolo').val('');
						$('#Moddurata').val('');
						$('#resultModCarica').html(result);
					},
					error : function(e) {
						alert("Errore durante l\'operazione");
					}
				});
			});
			$('#resultCarica').show('slow');
		},
		error : function(e) {
			alert("Errore durante l\'operazione");
		}
	});
	
}

function cancellaCarica(idCarica){
	if(!window.confirm('Sicuri di voler cancellare?'))
		return;
	$.ajax({
		type : 'POST',
		url : 'cancellaCarica',
		dataType : 'json',
		data : {
			idCarica : idCarica,
			titolo : $('#titoloCarica').val()
			
		},
		success : function(data) {
			var caricaListContent = tableCaricaLayout(data);
			$('#resultCarica').html(caricaListContent);
		},
		error : function(e) {
			alert("Errore durante l\'operazione");
		}
	});
}