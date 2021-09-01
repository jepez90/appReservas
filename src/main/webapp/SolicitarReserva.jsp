<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	%><%@ page import="appReservas.*"%><%
	boolean isEditUser=false;
	Usuario usuario=null;
	if(request.getAttribute("Usuario")!=null){
		isEditUser=true;
		usuario=(Usuario)request.getAttribute("Usuario");
	}
	boolean error=false;
	String errorMsg="";
	if(request.getAttribute("error")!=null){
		error=true;
		errorMsg=(String)request.getAttribute("error");
	}
	boolean exito=false;
	if(request.getAttribute("exito")!=null){
		exito=true;
		errorMsg=(String)request.getAttribute("exito");
	}
	int tipo=1;
	
	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Iniciar Sesión - CDA SANTA LIBRADA</title>
	
  <script src="js/jquery.min.js"></script> 
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <script src="js/bootstrap.min.js"></script>
  
<link rel="shortcut icon" href="imagenes/faviconG.png">
  
<style class="cp-pen-styles">
.btn {
	display: inline-block;
	*display: inline;
	*zoom: 1;
	padding: 4px 10px 4px;
	margin-bottom: 0;
	font-size: 13px;
	line-height: 18px;
	color: #333333;
	text-align: center;
	text-shadow: 0 1px 1px rgba(255, 255, 255, 0.75);
	vertical-align: middle;
	background-color: #f5f5f5;
	background-image: -moz-linear-gradient(top, #ffffff, #e6e6e6);
	background-image: -ms-linear-gradient(top, #ffffff, #e6e6e6);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#ffffff),
		to(#e6e6e6));
	background-image: -webkit-linear-gradient(top, #ffffff, #e6e6e6);
	background-image: -o-linear-gradient(top, #ffffff, #e6e6e6);
	background-image: linear-gradient(top, #ffffff, #e6e6e6);
	background-repeat: repeat-x;
	filter: progid:dximagetransform.microsoft.gradient(startColorstr=#ffffff,
		endColorstr=#e6e6e6, GradientType=0);
	border-color: #e6e6e6 #e6e6e6 #e6e6e6;
	border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
	border: 1px solid #e6e6e6;
	-webkit-border-radius: 4px;
	-moz-border-radius: 4px;
	border-radius: 4px;
	-webkit-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px
		rgba(0, 0, 0, 0.05);
	-moz-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px
		rgba(0, 0, 0, 0.05);
	box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px
		rgba(0, 0, 0, 0.05);
	cursor: pointer;
	*margin-left: .3em;
}

.btn:hover, .btn:active, .btn.active, .btn.disabled, .btn[disabled] {
	background-color: #e6e6e6;
}

.btn-large {
	padding: 9px 14px;
	font-size: 15px;
	line-height: normal;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
}

.btn:hover {
	color: #333333;
	text-decoration: none;
	background-color: #e6e6e6;
	background-position: 0 -15px;
	-webkit-transition: background-position 0.1s linear;
	-moz-transition: background-position 0.1s linear;
	-ms-transition: background-position 0.1s linear;
	-o-transition: background-position 0.1s linear;
	transition: background-position 0.1s linear;
}

.btn-primary, .btn-primary:hover {
	text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
	color: #ffffff;
}

.btn-primary.active {
	color: rgba(255, 255, 255, 0.75);
}

.btn-primary {
	background-color: #4a77d4;
	background-image: -moz-linear-gradient(top, #6eb6de, #4a77d4);
	background-image: -ms-linear-gradient(top, #6eb6de, #4a77d4);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#6eb6de),
		to(#4a77d4));
	background-image: -webkit-linear-gradient(top, #6eb6de, #4a77d4);
	background-image: -o-linear-gradient(top, #6eb6de, #4a77d4);
	background-image: linear-gradient(top, #6eb6de, #4a77d4);
	background-repeat: repeat-x;
	filter: progid:dximagetransform.microsoft.gradient(startColorstr=#6eb6de,
		endColorstr=#4a77d4, GradientType=0);
	border: 1px solid #3762bc;
	text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.4);
	box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px
		rgba(0, 0, 0, 0.5);
}

.btn-primary:hover, .btn-primary:active, .btn-primary.active,
	.btn-primary.disabled, .btn-primary[disabled] {
	filter: none;
	background-color: #4a77d4;
}

.btn-block {
	width: 100%;
	display: block;
}

* {
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	-ms-box-sizing: border-box;
	-o-box-sizing: border-box;
	box-sizing: border-box;
}

html {
	width: 100%;
	height: 100%;
}

body {
	background-color: #6FBF08;
	/* For browsers that do not support gradients */
	background-image: linear-gradient(to bottom right, #6FBF08, white);
}

.login {
	position: absolute;
	top: 20px;
	left: 50%;
	margin-top: 0;
	margin-right: 0;
	margin-left: -150px;
	margin-bottom: 0;
	width: 300px;
	height: 350px;
}

input {
	width: 100%;
	margin-bottom: 10px;
	background: rgba(0, 0, 0, 0.3);
	border: none;
	outline: none;
	padding: 10px;
	font-size: 15px;
	color: #fff;
	text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.3);
	border: 1px solid rgba(0, 0, 0, 0.3);
	border-radius: 4px;
	box-shadow: inset 0 -5px 45px rgba(100, 100, 100, 0.2), 0 1px 1px
		rgba(255, 255, 255, 0.2);
	-webkit-transition: box-shadow .5s ease;
	-moz-transition: box-shadow .5s ease;
	-o-transition: box-shadow .5s ease;
	-ms-transition: box-shadow .5s ease;
	transition: box-shadow .5s ease;
}

.login img {
	margin-left: -20px;
	margin-bottom: -30px;
}

input:focus {
	box-shadow: inset 0 -5px 45px rgba(100, 100, 100, 0.4), 0 1px 1px
		rgba(255, 255, 255, 0.2);
}
</style>

</head>
<body>
	<div class="login">
		<a href="/appReservas/"><img src="imagenes/LOGO2.png" width="300" height="219" alt="" /></a>
		
 <%if(error){ %><div class="alert alert-danger alert-dismissible fade show">
		    						<button type="button" class="close" data-dismiss="alert">&times;</button>
		   							<strong>Error!</strong> <%=errorMsg %>.
		   							</div> <%}%>
		   				<%if(exito){ %><div class="alert alert-success alert-dismissible fade show">
		    						<button type="button" class="close" data-dismiss="alert">&times;</button>
		   							<strong>Exito!</strong> <%=errorMsg %>.
		   							</div> <%}%>


		<form method="post" autocomplete="off">
		
			<input type="hidden" name="userId" value="<%=(isEditUser)?usuario.getId():"0" %>">
			<% if(isEditUser){ %>
				<input type="hidden" name="editUser" value="0" >
			<% }else{ %>
				<input type="hidden" name="newUser" value="0" >
			<% }%>
			<input type="text" name="u" value="<%=(isEditUser)?usuario.getUserName():"" %>" class="" <%if(isEditUser){ %>readonly="readonly" <%} %>placeholder="Usuario" tabindex="1" autofocus="autofocus" required="required" pattern="[A-z0-9]{4,50}" maxlength="50" title="El Nombre de Usuario no es válido" />
			<input type="tel" name="Cedula"  value="<%=(isEditUser)?usuario.getCedula():"" %>" class="" <%if(isEditUser){ %>readonly="readonly" <%} %> placeholder="Cedula" tabindex="2" required="required" pattern="\d{7,10}" maxlength="10" title="La cédula ingresada es incorrecta"  />
			<input type="text" name="Nombres"  value="<%=(isEditUser)?usuario.getNombre():"" %>" class="" placeholder="Nombre" tabindex="2" required="required" maxlength="100" pattern="\D{5,100}" title="El nombre ingresado no es válido" />
			<input type="password" name="p" id="p" placeholder="Contraseña" tabindex="3" required="required" />
			<input type="password" name="Rep" id="Rep"  placeholder="Contraseña" tabindex="4"	required="required" />
			<button type="submit" id="enviar" class="btn btn-primary btn-block btn-large" tabindex="5">Ingresar</button>
		</form>
	</div>
	
	
	
	<!-- Modal nuevo registro -->
	<div class="" id="myModal" role="" style="visibility:block">
		<div class="modal-dialog ">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="FormTitle">Registrar Nueva Reserva</h4>
       				<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				
				<div class="modal-body">

					<form method="post" autocomplete="off" id="NuevoRegistroForm" title="Añadir registro">
						<div class="row">
							<label class="control-label col-sm-4 " for="FechaNueva">Fecha:</label>
							<div class="col-sm-8 input-group">
								<input type="text" name="FechaNueva" required="required" id="FechaNueva" 
								class="form-control mb-2 form-control-sm noEstado" readonly="readonly" tabindex="5"
									pattern="[2][0][0-3][0-9]-[0-1][0-9]-[0-3][0-9]" value="<% //horario.getFecha().getFecha() %>">
								
							</div>
						</div>

						<div class="row">
							<label class="control-label col-sm-4" for="Hora">Hora:</label>
							<div class="col-sm-8 input-group">
								<input class="form-control mb-2 form-control-sm" name="Hora" type="text" id="Hora"
									pattern="[0-2][0-9]:[0-5][0-9]:[0-5][0-9]" title="Hora"  readonly="readonly" tabindex="6"
									size="8" maxlength="8" value="20:00:00"  required="required">
								<div class="input-group-prepend  mb-2">
							      <i id="HoraStatus" class="invisible spinner-grow"></i>
							    </div>
							</div>
						</div>

						<div class="row">
							<label for="Tipo" class="control-label col-sm-4">Tipo:</label>
							<div class="col-sm-8 input-group">
								<input type="text" name="Tipo" required="required" id="Tipo" class="form-control mb-2 form-control-sm noEstado" tabindex="7"
									  readonly="readonly" value="<%=(tipo==1)?"Motocicleta":"Liviano"%>" >
								</div>
						</div>
						
						<div class="row">
							<label for="PlacaNueva" class="control-label col-sm-4">Placa
								del vehículo:</label>
							<div class="col-sm-8 input-group">
								<input name="PlacaNueva" type="text" autofocus="autofocus"
									required="required" class="form-control mb-2 form-control-sm" id="PlacaNueva"
									pattern="<% if(tipo==1){ %>([A-Za-z]{3}[0-9]{2}|[A-Za-z]{3}[0-9]{2}[A-Za-z]{1})<% }else{%>[A-Za-z]{3}[0-9]{3}<% }%>"
									tabindex="8" title="La placa ingresada es inválida" size="8"
									maxlength="6">
								<div class="input-group-prepend  mb-2">
							      <i id="PlacaNuevaStatus" class="invisible spinner-grow"></i>
							    </div>
							</div>
						</div>
						<div class="row">
							<label for="TipoRevision" class="control-label col-sm-4">Tipo
								de revision:</label>
							<div class="col-sm-8 input-group">
								<select name="TipoRevision" required="required"
									class="form-control mb-2 form-control-sm noEstado" id="TipoRevision" tabindex="9">
									<option value="1">Inspección</option>
									<option value="2">Reinspección</option>
									<option value="3">Preventiva</option>
								</select>

							</div>
						</div>


						<div class="row">
							<label for="Cedula" class="control-label col-sm-4">Cédula Propietrario:</label>
							<div class="col-sm-8 input-group">
								<input name="Cedula" type="tel" required="required"
									class="form-control mb-2 form-control-sm" id="Cedula"
									pattern="\d{5,10}" maxlength="10"
									title="La cédula ingresada es incorrecta" tabindex="10">
									<div class="input-group-prepend  mb-2">
							      <i id="CedulaStatus" class="invisible spinner-grow"></i>
								    </div>
							</div>
						</div>

						<div class="row">
							<label for="Nombre" class="control-label col-sm-4">Nombre:</label>
							<div class="col-sm-8 input-group">
								<input name="Nombre" type="text" required="required"
									class="form-control mb-2 form-control-sm noEstado" id="Nombre" maxlength="100" tabindex="11"
									pattern="\D{5,100}" title="El nombre ingresado no es válido">

							</div>
						</div>

						<div class="row">
							<label for="Celular" class="control-label col-sm-4">Celular:</label>
							<div class="col-sm-8 input-group">
								<input name="Celular" type="tel" required="required"
									class="form-control mb-2 form-control-sm noEstado" id="Celular" maxlength="10" tabindex="12"
									pattern="\d{7,10}"
									title="El celular o telefono ingresado no es válido">

							</div>
						</div>
						<div class="row">
							<div class="col-sm-4 input-group">
							</div>
							<div class="custom-control custom-checkbox col-sm-8 mb-2">
							    <input type="checkbox" checked="checked"  tabindex="13" class="custom-control-input" id="EsMismoConductor" name="EsMismoConductor"  data-toggle="collapse" data-target="#infoProp">
							    <label class="custom-control-label" for="EsMismoConductor"  data-toggle="collapse" data-target="#demo">El Propietario es el mismo Conductor</label>
						    </div>
						  </div> 
						

						<div id="infoProp" class="collapse">
							<div class="row">
								<label for="CedulaC" class="control-label col-sm-4">Cédula Conductor:</label>
								<div class="col-sm-8 input-group">
									<input name="CedulaC" type="tel"
										class="form-control mb-2 form-control-sm" id="CedulaC"
										pattern="\d{5,10}" maxlength="10"
										title="La cédula ingresada es incorrecta"  tabindex="14">
										<div class="input-group-prepend  mb-2">
								      <i id="CedulaCStatus" class="invisible spinner-grow"></i>
									    </div>
								</div>
							</div>
	
							<div class="row">
								<label for="NombreC" class="control-label col-sm-4">Nombre:</label>
								<div class="col-sm-8 input-group">
									<input name="NombreC" type="text" 
										class="form-control mb-2 form-control-sm noEstado" id="NombreC" maxlength="100"  tabindex="15"
										pattern="\D{5,100}" title="El nombre ingresado no es válido">

								</div>
							</div>
	
							<div class="row">
								<label for="CelularC" class="control-label col-sm-4">Celular:</label>
								<div class="col-sm-8 input-group">
									<input name="CelularC" type="tel" 
										class="form-control mb-2 form-control-sm noEstado" id="CelularC" maxlength="10"  tabindex="16"
										pattern="\d{7,10}"
										title="El celular o telefono ingresado no es válido">
									
								</div>
							</div>
						</div> 
						
						<div class="row">
							<div class="col-sm-6 text-center">
								<input type="submit" name="submit" id="submit"
									class="btn btn-success" value="Registrar" tabindex="17">
							</div>
							<div class="col-sm-6 text-center">
								<input type="button" name="button6" id="button6"
									class="btn btn-danger" value="Cancelar" tabindex="18"
									data-dismiss="modal">
							</div>
						</div>


						<input type="hidden" name="PropietarioId" id="PropietarioId"  value="0">
						<input type="hidden" name="ConductorId" id="ConductorId" value="0">
						<input type="hidden" name="idPlaca" id="idPlaca" value="0">
						<input type="hidden"  name="accion" id="accion" value="1">
					</form>

				</div>
			</div>

		</div>
	</div>
	
	
	
	<script type="text/javascript">
		window.onload=function(){
			document.getElementById("enviar").onclick=function(e){
				if(document.getElementById("Rep").value!=document.getElementById("p").value){
					document.getElementById("Rep").value=""
					alert("la contraseña no coincide");
					return false;
				}
			};
		}
	</script>
</body>
</html>
