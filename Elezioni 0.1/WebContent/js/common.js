function changeSelectValue(selectNode, value){
	for(var index=0;index<selectNode.length;index++){
		if(selectNode[index].value == value)
			selectNode.selectedIndex = index;
	}	
}

function isNotEmptyOrNull(value){
	if(value == null )
		return false;
	
	if(value != null )
	{
		var tmp = $.trim(value);
		if(tmp.length == 0)
			return false;
	}
		
	return true;
}

function formatDate(value){
	if(isNotEmptyOrNull(value)){
		return moment(value).format("DD/MM/YYYY");
	}
	return '';
}

function visualizzaCandidati(idE,div){
	//alert('visualizzaCandidati: idElezione = ' + idE+ '; div='+div);
	var elementId = '#'+div;
	$(elementId).html('');
	$.ajax({
		type : 'POST',
		url : 'candidatiListByIdElezione',
		dataType : 'json',
		data : {
			idElezione : idE
		},
		success : function(utente) {
			if('elencoCandidatiUser'==div)
				$(elementId).append('<option>seleziona il candidato</option>');
			$.each(utente, function (index, value) {
				$(elementId).append('<option value=\"'+value.idUtente+'\">'+value.nome+' '+value.cognome+' </option>');
			});
			$(elementId).show('slow');
		},
		error : function(e) {
			alert("Errore durante il caricamento della select ");
		}
	});
}