<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:jsp="http://java.sun.com/JSP/Page">

<jsp:include page="/WEB-INF/views/jspf/initial_import.jsp" />


<%@page import="it.elezioni.util.Constants"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ page session="false"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/pda/pda.css"
	type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.js"></script>

<script>

var italia = '<%=Constants.ITALIA%>';

$(document).ready(function() {
   $("#pdaDetailParentDIV").scrollTop(0);

   $("#pdaSaveBotton").button();
   $("#pdaSaveValidateBotton").button();
   $("#pdaCancelBotton").button();
   
   $("#pdaSaveBotton").css({
       padding: '0.2em 0.2em'
   });
   $("#pdaCancelBotton").css({
       padding: '0.2em 0.2em'
   });
   
   $("#pdaSaveValidateBotton").css({
       padding: '0.2em 0.2em'
   });

   $('#pdaSaveBotton').bind('click',submitPda);
   $('#pdaSaveValidateBotton').bind('click',submitPdaSelectedValidate);
   $('#pdaCancelBotton').bind('click',returnOffersTable);

   if( $('#sedeAttivazioneAltra').is(':checked')) {
       enableDisableSedeAttivazioneAltra();
   }
  

   if($('#pdaClienteSedeLPiano').val() == "null") {
       $('#pdaClienteSedeLPiano').val("");
   }

   <c:if test="${(pdaForm.fromScript != 'true')}">
       <spring:hasBindErrors name="pdaForm">
           uiUtils.showErrorDialog("Errori nella compilazione della PDA", "PDA non salvata ,sono presenti errori nella pagina ,controllare e correggere");
       </spring:hasBindErrors>
       <c:if test="${(errorMsg != null)}">
           uiUtils.showErrorDialog("Attenzione : errore nel salvataggio della PDA ","${errorMsg}");
       </c:if>
   </c:if>

               

   $('.numberOnly').keydown(function (event) {
       if ((!event.shiftKey && !event.ctrlKey && !event.altKey) &&
           ((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105)))
       // 0-9 or numpad 0-9, disallow shift, ctrl, and alt
       {
           // check textbox value now and tab over if necessary
       }
       else if (event.keyCode != 8 && event.keyCode != 13 && event.keyCode != 46 && event.keyCode != 37
           && event.keyCode != 39 && event.keyCode != 9 && event.keyCode != 109
           && event.keyCode != 189 && event.keyCode != 110 && event.keyCode != 190)
       // not backsapce (8), enter (13), del (46), left arrow (37), right arrow (39), tab (9), negetive sign (- : 109, 189), or point (. : 110, 190)
       {
           event.preventDefault();
       }
       // else the key should be handled normally
   });

 
//    $("#pdaAttoreFDtaScad").datepicker({
//        dateFormat:'dd-mm-yy',
//        showOtherMonths:true,
//        selectOtherMonths:true,
//        changeMonth: true,
//        changeYear: true,
//         yearRange: "-100:+30"

//    });

     
 
   if( $("#pdaData").val() == null || $("#pdaData").val() == "") {
      $("#pdaData").val($.datepicker.formatDate('dd-mm-yy', new Date()));
   }
   $("#pdaData").datepicker({
       dateFormat:'dd-mm-yy',
       showOtherMonths:true,
       selectOtherMonths:true,
       changeMonth: true,
       changeYear: true,
        yearRange: "-100:+4"

   });

  <c:if test="${(pdaForm.fromScript != 'true')}">

       $('#formPda').ajaxForm({
           beforeSubmit : function(){

           },
           success : function(){

           },
           target : "#pdaDetail",
           forceSync : true,
           url : '<spring:url value="/pda/scriviAggiornaPda"/>',
           contentType: 'application/x-www-form-urlencoded;charset=UTF-8'
       });


  </c:if>

});
      
       
  function submitPda() {
      $(this).button("disable");
      $('#formPda').submit();
  }
  function submitPdaSelectedValidate() {
      $(this).button("disable");
      $('#formPda').addHidden("selectedValidate","true").submit();
  }

  function returnOffersTable() {
      document.location.href='main#externalDiv/createpaneloffers';
  }

  function submitReturnPdaForm() {
      $('#formRitornaPda').submit();
  }

  

  function getIdPda(){
      return $("#pdaIdPda").val();
  }

 $.fn.addHidden = function (name, value) {
     return this.each(function () {
         var input = $("<input>").attr("type", "hidden").attr("name", name).val(value);
         $(this).append($(input));
     });
 };

 $(window).bind('resize', function() {
     $("#pdaDetailParentDIV").width('auto');
     $("#pdaDetailParentDIV").height($(window).height()-100);
 }).trigger('resize');

</script>


<form:form id="formPda" action="scriviAggiornaPda" method="post"
	commandName="pdaForm" cssClass="pdaForm">


	<form:hidden path="pda.idPda" id="pdaIdPda" />

	<form:hidden path="fromScript" />
	<form:hidden path="token" />

	<table>
		<tr>
			<td>
				<fieldset name="datiDellaSedeLegaleFieldSet">
					<legend>DATI DELLA SEDE LEGALE</legend>
					<div>
						<label class="mandatory" for="pdaClienteLRagSoc">Ragione
							Sociale:*</label>
						<form:input path="pdaClienteSedeL.ragSoc" id="pdaClienteLRagSoc" />
						<form:errors path="pdaClienteSedeL.ragSoc"
							cssClass="error-valid-field ui-state-error ui-corner-all" />
					</div>


					<div>
						<label class="mandatory" for="pdaClienteSedeLIndirizzo">Indirizzo:*</label>
						<form:input path="pdaClienteSedeL.indirizzo"
							id="pdaClienteSedeLIndirizzo" />
						<form:errors path="pdaClienteSedeL.indirizzo"
							cssClass="error-valid-field ui-state-error ui-corner-all" />
					</div>
					<div>
						<label class="mandatory" for="pdaClienteSedeLNCivico">N°:*</label>
						<form:input path="pdaClienteSedeL.nCivico"
							id="pdaClienteSedeLNCivico" />
						<form:errors path="pdaClienteSedeL.nCivico"
							cssClass="error-valid-field ui-state-error ui-corner-all" />
					</div>

				</fieldset>
			</td>

			<td>
				<fieldset name="datiDellaSedeDiAttivazioneFieldSet"
					id="datiDellaSedeDiAttivazioneFieldSet">
					<legend>
						DATI DELLA SEDE DI ATTIVAZIONE <br />(da compilare solo se la sede
						di attivazione è diversa da <br />sede legale)
					</legend>
					<div>
						<form:checkbox path="sedeAttivazioneAltra"
							id="sedeAttivazioneAltra" value="true" />
						Selezionare per attivare i campi (i campi con * sono obbligatori
						solo se attivi)
						<form:errors path="sedeAttivazioneAltra"
							cssClass="error-valid-field ui-state-error ui-corner-all" />
					</div>
					<br />
					<div>
						<label class="" for="pdaClienteSedeARagsoc">Nome Sede:*</label>
						<form:input path="pdaClienteSedeA.ragSoc"
							id="pdaClienteSedeARagsoc" disabled="true" />
						<form:errors path="pdaClienteSedeA.ragSoc"
							cssClass="error-valid-field ui-state-error ui-corner-all" />
					</div>
					<div>
						<label class="" for="pdaClienteSedeAIndirizzo">Indirizzo:*</label>
						<form:input path="pdaClienteSedeA.indirizzo"
							id="pdaClienteSedeAIndirizzo" disabled="true" />
						<form:errors path="pdaClienteSedeA.indirizzo"
							cssClass="error-valid-field ui-state-error ui-corner-all" />
					</div>
					<div>
						<label class="" for="pdaClienteSedeANCivico">N°:*</label>
						<form:input path="pdaClienteSedeA.nCivico"
							id="pdaClienteSedeANCivico" disabled="true" />
						<form:errors path="pdaClienteSedeA.nCivico"
							cssClass="error-valid-field ui-state-error ui-corner-all" />
					</div>

				</fieldset>
			</td>
		</tr>
	</table>
	
	<table>
		<tr>
			<td>

				<fieldset name="dataELuogoFieldSet">
					<legend>DATA E LUOGO FIRMA</legend>
					<div>
						<label class="" for="pdaData">Data firma:*</label>
						<form:input path="pda.data" id="pdaData" readonly="true" />
						<form:errors path="pda.data"
							cssClass="error-valid-field ui-state-error ui-corner-all" />
					</div>
					<div>
						<label class="" for="pdaLuogo">Luogo firma:*</label>
						<form:input path="pda.luogo" id="pdaLuogo" />
						<form:errors path="pda.luogo"
							cssClass="error-valid-field ui-state-error ui-corner-all" />
					</div>
				</fieldset>

			</td>
		</tr>
	</table>

</form:form>

<div class="saveBottonDiv bottonDivLeft">
	<input id="pdaSaveBotton" type="button" value="SALVA E PROSEGUI" />
</div>

<div class="saveBottonDiv bottonDivLeft">
	<input id="pdaSaveValidateBotton" type="button" value="SALVA E VALIDA" />
</div>

<c:if test="${(pdaForm.fromScript == 'false')}">
	<div class="returnBottonDiv bottonDivLeft">
		<input id="pdaCancelBotton" type="button" value="ANNULLA" />
	</div>
</c:if>