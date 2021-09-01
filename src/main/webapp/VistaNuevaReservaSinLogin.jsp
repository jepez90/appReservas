<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	%><%@ page import="appReservas.*"
	%><%@ page import="java.sql.*" 
	%><%@ page import="java.util.*" 
%><%
String Metodo=request.getMethod();
ArrayList<String[]> Alertas=(ArrayList<String[]>)request.getAttribute("Alertas");
Reserva reserva=(Reserva)request.getAttribute("Reserva");

%><!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<title>Solicitar Cita - CDA SANTA LIBRADA</title>
	
   <!-- Bootstrap -->
  <script src="js/jquery.min.js"></script> 
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <script src="js/popper.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
  
  <!-- datepicker https://gijgo.com/datepicker/ -->  
  <script src="js/gijgo.min.js" type="text/javascript"></script>
  <link href="css/gijgo.min.css" rel="stylesheet" type="text/css" />

<!-- font awesome 4.6 https://fontawesome.com/v4.7.0/ --> 
<link href="css/font-awesome.min.css" rel="stylesheet" type="text/css" /> 
<!-- 	 <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" /> -->

<!-- autocomplete https://github.com/devbridge/jQuery-Autocomplete-->
	<script src="js/jquery.autocomplete.js" type="text/javascript">	</script>
	<script src="js/jquery.mockjax	.js" type="text/javascript">	</script>
	
 <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  
<link rel="shortcut icon" href="imagenes/faviconG.png">
  
<style>

body {
	background-color: #6FBF08;
	/* For browsers that do not support gradients */
	background-image: linear-gradient(to bottom right, #6FBF08, white);
	
	width: 100%;
	height: 100%;
}
.maxContainerWidth{
	max-width: 600px;
	}

.autocomplete-suggestions { overflow: auto; border: 1px solid #CBD3DD; background: #FFF; }
.autocomplete-suggestion { overflow: hidden; padding: 1px 15px; white-space: nowrap; color: #6c757d;  border-radius: 4px; border: 1px solid #beccd9; }
.autocomplete-selected { background: #F0F0F0; }
.autocomplete-suggestions strong { color: #029cca; font-weight: bold; }

</style>
</head>

<body>
	<div class="container maxContainerWidth">

<h1>
	  <img class="img" src="imagenes/LOGO2.png" width="200" height="146" alt=""/>
	<br />Reservas CDA SANTA LIBRADA</h1>
	  <p>Bienvenido al sistema de reservas del CDA SANTA LIBRADA. Por favor, diligencie el siguiente formulario para solicitar una cita para su Revision Técnico Mecánica.</p>
	</div>

<div class="container-sm border maxContainerWidth">

	<% if(Alertas!=null){ for(String[] a:Alertas){%>
	<div class="alert <%=(a[0].equals("Exito"))?"alert-success":""%> <%=(a[0].equals("Error"))?"alert-danger":""%> alert-dismissible fade show">
	    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    <strong><%= a[0]%>!</strong> <%= a[1]%>.
	</div>
  	<% }}%>
	<script>
	
var tipoIsSelected=false;	
var FechaIsSelected=false;
$(document).ready(
	function(){
		//cuando se cambia la seleccion de tipo
		$("input[name=Tipo]").change(function () {
			//cambia el patron de validacion del campo placa
			if($(this).val()=="Motocicleta"){
				PlacaNueva.pattern="([A-Za-z]{3}[0-9]{2}|[A-Za-z]{3}[0-9]{2}[A-Za-z]{1})";
			}
			else{
				PlacaNueva.pattern="[A-Za-z]{3}[0-9]{3}";
			}
			//valida si se puede activar el autocompletado de hora
			tipoIsSelected=true;
			ActivaAutocompletado();
			$('#Hora').val("");
			//oculta el feedback de validacion
			if(radioChecked($("input[name=Tipo]"))==""){
	  			$("#TipoInvalidFeedback").show();
	    	}
			else{
	  			$("#TipoInvalidFeedback").hide();
			}
		});
		 
		//	pone el selector de fecha en el menu
		$('#FechaNueva').datepicker({
			uiLibrary: 'bootstrap4',
			format: "yyyy-mm-dd",
			minDate: function() {
				var date = new Date();
				date.setDate(date.getDate());
				return new Date(date.getFullYear(), date.getMonth(), date.getDate());
			},
			showRightIcon: false,
			header: false,
			weekStartDay: 1,
			maxDate: function() {
				var date = new Date();
				date.setDate(date.getDate()+7);
				return new Date(date.getFullYear(), date.getMonth(), date.getDate());
			},
			 change: function (e) {
				 //valida si se puede activar el autocompletado
				 FechaIsSelected=true;
	 	         ActivaAutocompletado();
	 	         
	 	         //realiza la validacion del campo
	 	        if($("#FechaNueva").val()==""){
		  			$("#FechaNuevaInvalidFeedback").show();
		  			$("#FechaNueva").addClass("is-invalid");
		  			$("#FechaNueva").removeClass("is-valid");
		         }
		  		else{
		  			$("#FechaNuevaInvalidFeedback").hide();
		  			$("#FechaNueva").removeClass("is-invalid");
		  			$("#FechaNueva").addClass("is-valid");
		  		}
	         },
	         
		});
		
		//pone el autocompletado de la hora
		$('#Hora').autocomplete({
			serviceUrl: 'Gestion',
			minChars:0,
			dataType:"json",
			type:"post",
			paramName:"Hora",
			showNoSuggestionNotice:true,
			noSuggestionNotice:"No hay disponibilidad este dia",
			onSelect: function (suggestion) {	
				//borra la validacion
		    	  if($("#Hora").val()==""){
			  			$("#Hora").next().show();
			  			$("#Hora").addClass("is-invalid");
			  			$("#Hora").removeClass("is-valid");
			         }
			  		else{
			  			$("#Hora").next().hide();
			  			$("#Hora").removeClass("is-invalid");
			  			$("#Hora").addClass("is-valid");
			  		}

			},
			/*onSearchStart:$.proxy(function () {this.setEstado("Hora","Loading")},this),
			onSearchComplete:$.proxy(function (query, suggestions) {
				this.setEstado("Hora","NotSet");
			},this),
			onSelect: $.proxy(function (suggestion) {	
		    	  if($("#FechaNueva").val()==""){
			  			$("#FechaNuevaInvalidFeedback").show();
			  			$("#FechaNueva").addClass("is-invalid");
			  			$("#FechaNueva").removeClass("is-valid");
			  			valid=false;
			         }
			  		else{
			  			$("#FechaNuevaInvalidFeedback").hide();
			  			$("#FechaNueva").removeClass("is-invalid");
			  			$("#FechaNueva").addClass("is-valid");
			  		}

			},this),
			onSearchError:$.proxy(function (query, jqXHR, textStatus, errorThrown) {
				this.setEstado("Hora","Error");		
			},this),*/
			transformResult: function(datos) {
				var a=[];

				console.log(datos);
				for(var i=0;i<datos.Longitud;i++){
					a[i]={"value":datos.Data[i][0],"data":datos.Data[i]}
				}
				return {"suggestions": a};
			}
		});
		//desactiva por defecto el autocompletado
		$('#Hora').autocomplete().disable();


		
		

		
		//establece el estado no requerido cuando los campos del propietario se ocultan
		$("#infoProp").on('show.bs.collapse', function(){
			$("#CedulaC").attr("required",true);
			$("#NombreC").attr("required",true);
			$("#CelularC").attr("required",true);
		});

		//establece el estado requerido cuando los campos del propietario se muestran
		$("#infoProp").on('hide.bs.collapse', function(){
			$("#CedulaC").attr("required",false);
			$("#NombreC").attr("required",false);
			$("#CelularC").attr("required",false);
		});

	}		);



// Disable form submissions if there are invalid fields
(function() {
  'use strict';
  window.addEventListener('load', function() {
    // Get the forms we want to add validation styles to
    var forms = document.getElementsByClassName('needs-validation');
    // Loop over them and prevent submission
    var validation = Array.prototype.filter.call(forms, function(form) {
      form.addEventListener('submit', function(event) {
    	  var valid=true;
    	  
    	  //valida el radiobutton de Tipo
    	  if(radioChecked($("input[name=Tipo]"))==""){
  			$("#TipoInvalidFeedback").show();
    	  }
    	  else{
    		  $("#TipoInvalidFeedback").hide();
    	  }
    	  
    	  //valida el campo fecha
    	  if($("#FechaNueva").val()==""){
	  			$("#FechaNuevaInvalidFeedback").show();
	  			$("#FechaNueva").addClass("is-invalid");
	  			$("#FechaNueva").removeClass("is-valid");
	  			valid=false;
	         }
	  		else{
	  			$("#FechaNuevaInvalidFeedback").hide();
	  			$("#FechaNueva").removeClass("is-invalid");
	  			$("#FechaNueva").addClass("is-valid");
	  		}
    	  
    	//valida el campo hora
    	  if($("#Hora").val()==""){
	  			$("#Hora").next().show();
	  			$("#Hora").addClass("is-invalid");
	  			$("#Hora").removeClass("is-valid");
	  			valid=false;
	         }
	  		else{
	  			$("#Hora").next().hide();
	  			$("#Hora").removeClass("is-invalid");
	  			$("#Hora").addClass("is-valid");
	  		}
        if (form.checkValidity() === false || valid==false) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add('was-validated');
     }, false);
    });
  }, false);
})();

function radioChecked(seleccion){//retorna el radio seleccionado de una coleccion
	var retorno="";
	seleccion.each(function(){
		if($(this).prop("checked")){
			retorno= $(this).val();
		}
	});
	return retorno;

}

ActivaAutocompletado=function(){
		if(tipoIsSelected & FechaIsSelected){
			$('#Hora').autocomplete('setOptions', {params:{"Tipo":radioChecked($("input[name=Tipo]")),"accion":5,"Fecha":$("#FechaNueva").val()},});
			$('#Hora').autocomplete().clearCache();
			$('#Hora').val("");
			$('#Hora').autocomplete().enable();
  			$("#Hora").next().show();
  			$("#Hora").addClass("is-invalid");
  			$("#Hora").removeClass("is-valid");
			
		}
}



</script>
	

		<form method="post" autocomplete="off" id="NuevoRegistroForm" title="Añadir registro" class="needs-validation" novalidate>
			<div class="row mb-2 mt-2">
				<label for="Tipo" class="control-label col-sm-4">Tipo:</label>
				<div class="col-sm-8 input-group" title="Seleccione una opción">
					<div class="custom-control custom-radio custom-control-inline" >
						<input type="radio" class="custom-control-input" tabindex="1" id="TipoLiviano" name="Tipo"  
						value="Liviano" required <% if(reserva.getTipoDeVehiculoId()==2){ %>checked<%} %>>
						<label class="custom-control-label" for="TipoLiviano">Liviano</label>
					</div>
					<div class="custom-control custom-radio custom-control-inline">
						<input type="radio" class="custom-control-input" tabindex="2"  id="TioMotocicleta" name="Tipo"  
						value="Motocicleta"  title="Seleccione una opción" <% if(reserva.getTipoDeVehiculoId()==1){ %>checked<%} %>>
					 	<label class="custom-control-label" for="TioMotocicleta">Motocicleta</label>
					</div>
					<div class="invalid-feedback" id="TipoInvalidFeedback">Debe seleccionar un Tipo De Vehículo</div>
				</div>
			</div>

			<div class="row mb-2">
				<label class="control-label col-sm-4 " for="FechaNueva">Fecha:</label>
				<div class="col-sm-8 input-group">
					<input type="text" name="FechaNueva" required="required" id="FechaNueva" 
					class="form-control form-control-sm text-center" readonly="readonly" tabindex="3"
							value="<%if(reserva.getFechaReserva()!=null) { %><%= reserva.getFechaReserva().getFecha() %><%} %>" pattern="[2][0][0-3][0-9]-[0-1][0-9]-[0-3][0-9]">
						<div class="invalid-feedback" id="FechaNuevaInvalidFeedback">Debe seleccionar una fecha</div>
				</div>
			</div>

			<div class="row mb-2">
				<label class="control-label col-sm-4" for="Hora">Hora:</label>
				<div class="col-sm-8 input-group">
					<input class="form-control form-control-sm text-center" name="Hora" type="text" id="Hora"
						pattern="[0-2][0-9]:[0-5][0-9]:[0-5][0-9]" readonly="readonly" title="Hora"  tabindex="4"
						value="<%if(reserva.getHoraReserva()!=null) { %><%= reserva.getHoraReserva().getHoraMil() %><%} %>" size="8" maxlength="8" required="required">
					<div class="invalid-feedback">Debe seleccionar una hora</div>
				</div>
			</div>

			<div class="row mb-2">
				<label for="PlacaNueva" class="control-label col-sm-4">Placa del vehículo:</label>
				<div class="col-sm-8 input-group">
					<input name="PlacaNueva" type="text"
						required="required" class="form-control form-control-sm text-center" id="PlacaNueva" placeholder="PLACA"
						pattern="\D{5,6}" tabindex="5" title="Ingrese la Placa del vehículo" size="5"
						maxlength="6" value="<%= reserva.getPlaca() %>">
				      <div class="invalid-feedback">La Placa Digitada No es Válida</div>
				</div>
			</div>
			<div class="row mb-2 mt-2">
				<label for="TipoRevision" class="control-label col-sm-4">Tipo de revision:</label>
				<div class="col-sm-8 input-group"> 
					<div class="custom-control custom-radio custom-control-inline">
						<input type="radio" class="custom-control-input" id="customRadio1" tabindex="6" name="TipoRevision"  value="Inspeccion"
							 <% if(reserva.getTipoDeRevisionId()==1){ %>checked<%} %>>
						<label class="custom-control-label" for="customRadio1">Inspección</label>
					</div>
					<div class="custom-control custom-radio custom-control-inline">
						<input type="radio" class="custom-control-input" id="customRadio2" tabindex="8" name="TipoRevision"  value="Reinspeccion" 
						<% if(reserva.getTipoDeRevisionId()==2){ %>checked<%} %>>
						<label class="custom-control-label" for="customRadio2">Reinspección</label>
					</div>
					<div class="custom-control custom-radio custom-control-inline">
						<input type="radio" class="custom-control-input" id="customRadio3" tabindex="7" name="TipoRevision" value="Preventiva" 
						<% if(reserva.getTipoDeRevisionId()==3){ %>checked<%} %>>
						<label class="custom-control-label" for="customRadio3">Preventiva</label>
					</div>
					
				</div>
			</div>


			<div class="row mb-2">
				<label for="Cedula" class="control-label col-sm-4">Cédula Propietrario:</label>
				<div class="col-sm-8 input-group">
					<input name="Cedula" type="tel" required="required"
						class="form-control form-control-sm text-center" id="Cedula"
						pattern="\d{5,10}" value="<%= reserva.getPropietario().getDoc() %>" maxlength="10"
						title="Ingrese el numero de cedula del propietario" tabindex="9">
						
					<div class="invalid-feedback">La cédula ingresada no es válida</div>
				</div>
			</div>

			<div class="row mb-2">
				<label for="Nombre" class="control-label col-sm-4">Nombre:</label>
				<div class="col-sm-8 input-group">
					<input name="Nombre" type="text" required="required"
						value="<%= reserva.getPropietario().getNombres() %>" class="form-control form-control-sm text-center" id="Nombre" maxlength="100" tabindex="10"
						pattern="\D{6,100}" title="Ingrese el nombres y apellidos del propietario">
					<div class="invalid-feedback">El nombre ingresado no es válido</div>
				</div>
			</div>

			<div class="row mb-2">
				<label for="Celular" class="control-label col-sm-4">Celular:</label>
				<div class="col-sm-8 input-group">
					<input name="Celular" type="tel" required="required" class="form-control form-control-sm text-center" 
						id="Celular" value="<%= reserva.getPropietario().getCelular() %>" maxlength="10" tabindex="11" pattern="\d{10}"
						title="Ingrese un numero de celular">
					<div class="invalid-feedback">El número celular ingresado no es válido</div>

				</div>
			</div>
			<div class="row mb-2">
				<div class="col-sm-4 input-group">
				</div>
				<div class="custom-control custom-checkbox col-sm-8">
					<input type="checkbox" checked="checked" tabindex="12" class="custom-control-input" id="EsMismoConductor" 
					name="EsMismoConductor"  data-toggle="collapse" data-target="#infoProp">
					<label class="custom-control-label" for="EsMismoConductor"  data-toggle="collapse" data-target="#demo">El Propietario es el mismo Conductor</label>
				</div>
			  </div> 


			<div id="infoProp" class="collapse">
				<div class="row mb-2">
					<label for="CedulaC" class="control-label col-sm-4">Cédula Conductor:</label>
					<div class="col-sm-8 input-group">
						<input name="CedulaC" type="tel"
							class="form-control form-control-sm text-center" id="CedulaC"
							pattern="\d{5,10}" value="<%= reserva.getConductor().getDoc() %>" maxlength="10"
							title="Ingrese la cédula del conductor"  tabindex="13">
							<div class="invalid-feedback">La cédula ingresada no es válida</div>
					</div>
				</div>

				<div class="row mb-2">
					<label for="NombreC" class="control-label col-sm-4">Nombre:</label>
					<div class="col-sm-8 input-group">
						<input name="NombreC" type="text" 
							value="<%= reserva.getConductor().getNombres() %>" class="form-control form-control-sm text-center" id="NombreC" maxlength="100"  tabindex="14"
							pattern="\D{6,100}" title="Ingrese el nombres y apellidos del conductor">
						<div class="invalid-feedback">El nombre ingresado no es válido<br /></div>
					</div>
				</div>

				<div class="row mb-2">
					<label for="CelularC" class="control-label col-sm-4">Celular:</label>
					<div class="col-sm-8 input-group">
						<input name="CelularC" type="tel" 
							class="form-control form-control-sm text-center" id="CelularC" maxlength="10"  tabindex="15"
							pattern="\d{7,10}" value="<%= reserva.getConductor().getCelular() %>"
							title="Ingrese el numero de celular del conductor">
					<div class="invalid-feedback">El número celular ingresado no es válido<br /></div>
					</div>
				</div>
			</div> 

			<div class="row mb-2">
				<div class="col-sm-3 text-center">
					
				</div>
				<div class="col-sm-6 text-center">
					<input type="submit" name="submit" id="submit"
						class="btn btn-success" value="Registrar" tabindex="16">
				</div>
				<div class="col-sm-3 text-center">
					
				</div>
			</div>

		</form>


	</div>
	<div class="container-sm maxContainerWidth">
	  <p>&nbsp;</p><p>&nbsp;</p>
	</div>
	
	
<div class="container-sm border maxContainerWidth">

  	<div class="row">
	  <div class="col-sm-3">Volver</div>
	  <div class="col-sm-6">&copy; CDA SANTA LIBRADA 2020</div>
		<div class="col-sm-3" ><a href="Gestion" title="Ingresar Al Sitio: Solo Personal Del CDA">Ingresar Al Sitio</a></div>
    </div>


</div>
</body>
</html>
