/**
 * 
 */
window.onload=function(){
	//pone a la escucha los botones de accion 
	var filas=document.getElementById("BodyTablaPrincipal").children;
	for (i=document.getElementById("BodyTablaPrincipal").childElementCount-1;i>0;i--){
		filas[i].onclick=seleccionaFila;
		if(document.getElementById("U"+i+"btn")!=null){
			document.getElementById("U"+i+"btn").onclick=seleccionaFila;
		}

	}
	
	if (window.history.replaceState) { // elimina el reenvio de formularios
	    window.history.replaceState(null, null, window.location.href);
	}
	
//	pone el selector de fecha en el menu
	$('#Fecha').datepicker({
		uiLibrary: 'bootstrap4',
		format: "yyyy-mm-dd",
		minDate: "2018-11-23",
		showRightIcon: false,
		header: true,
		weekStartDay: 1,
		maxDate: function() {
			var date = new Date();
			date.setDate(date.getDate()+7);
			return new Date(date.getFullYear(), date.getMonth(), date.getDate());
		}
	});
	today=new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
	
	FormularioObject=new Form();

	
	//brinda el foco en lel campo placa
	$("#myModal").on("shown.bs.modal",function(){
			$("#PlacaNueva").focus();		
	})
	
	//resalta la hora actual
	ResaltaHoraActual();
	
	//recarga la pagina cuando ya se ha cerrado la sesion
	window.setTimeout("location.reload()",3660000);
	
	//establece la escucha del select de mostrar solo citas pendientes
	mostrarPendientes.onchange=function(){
		document.cookie="mostrarPendientes="+this.checked;
	};
	
	//establece la escucha del boton principal agregar cita
	addBtn.onclick=botonAgregarListener;
	
}

ResaltaHoraActual = function(){
	//si el dia mostrado no es el actual, no se resalta ninguna hora
	var ahora=new Date();
	b=new Date($("#Fecha").val().split("-")[0],$("#Fecha").val().split("-")[1]-1,$("#Fecha").val().split("-")[2]);
	c=new Date(ahora.getFullYear(),ahora.getMonth(),ahora.getDate());
	if(b.getTime()!=c.getTime()){
		return;
	}
	
	//recorre la lista hasta que encuentra una reserva.
	var filas=BodyTablaPrincipal.children.length-1;
	Hora1=new Date();
	j=1;
	while($("#U"+j+"btn").length==0){
		j++;
		if (filas>j){
			return;
		}
	}
	
	//recorre las reservas existentes hasta encontrar la fila a resaltar
	VectorHoraSiguiente=$("#U"+j+"btn").attr("data-app-hora").split(":");
	Hora1.setHours(VectorHoraSiguiente[0]);
	Hora1.setMinutes(VectorHoraSiguiente[1]);
	Hora1.setSeconds(VectorHoraSiguiente[2]);
	for (i=j;i<filas;i++){
		if(i+1>filas){
			Hora2=new Date(Hora1.getTime());
			Hora2.setMinutes(0);
			Hora2.setHours(Hora1.getHours()+1);
		}
		else{
			Hora2=new Date();
			if($("#U"+(i+1)+"btn").length!=0){
				VectorHoraSiguiente=$("#U"+(i+1)+"btn").attr("data-app-hora").split(":");		
				Hora2.setHours(VectorHoraSiguiente[0]);
				Hora2.setMinutes(VectorHoraSiguiente[1]);
				Hora2.setSeconds(VectorHoraSiguiente[2]);
			}
			else{
				Hora2=Hora1;
			}
		}
		
		if(ahora>=Hora1&&ahora<Hora2){
			if(idHoraResaltada=="#U"+i){
				return;
			}
			if(idHoraResaltada!=""){
				$(idHoraResaltada).removeClass("progress-bar-striped bg-secondary progress-bar-animated");
				idHoraResaltada="#U"+i;
				$(idHoraResaltada).addClass("progress-bar-striped bg-secondary progress-bar-animated");
				window.setTimeout(ResaltaHoraActual,60000);
			}
			else{
				idHoraResaltada="#U"+i;
				$(idHoraResaltada).addClass("progress-bar-striped bg-secondary progress-bar-animated");
				window.setTimeout(ResaltaHoraActual,60000);
				$('html, body').animate({
					scrollTop: $(".progress-bar-striped").offset().top-90
				}, 500);
			}
			
			break;
		}
		Hora1=Hora2;
		
	}
	console.log(idHoraResaltada);
	
}
idHoraResaltada="";

var FormularioObject;
var idSeleccionado="";

function botonAgregarListener(){
	FormularioObject.reset();
	FormularioObject.FieldHora.attr("value",Boton.getAttribute("data-app-hora"));
	FormularioObject.FieldHora.val(Boton.getAttribute("data-app-hora"));
	console.log("agregar");
}


function seleccionaFila(e){
	//console.log(e);
	var Padre=e.target;
	var Boton;
	while (Padre.nodeName!="TR"){
		if(Padre.nodeName=="BUTTON"){
			Boton=Padre;
		}
		Padre=Padre.parentElement;
	}

	if(Boton){
		if(Boton.getAttribute("data-toggle")!="dropdown"){//es un boton para agregar cita
			FormularioObject.reset();
			FormularioObject.FieldHora.attr("value",Boton.getAttribute("data-app-hora"));
			FormularioObject.FieldHora.val(Boton.getAttribute("data-app-hora"));
			FormularioObject.ActivaFecha(false);
		}
		else{//el boton desplega un dropdown
			for(i=0; i<4;i++){
				var link = Boton.nextElementSibling.children[i];
				if(link.nodeName=="A"){
					if(link.getAttribute("data-app-accion")==1){//nueva cita a la misma hora
						link.onclick=function(e){
							var val=e.target.parentNode.previousElementSibling.getAttribute("data-app-hora");
							val=val.substr(0,4)+"5:00" ;
							FormularioObject.reset();
							FormularioObject.FieldHora.attr("value",val);
							FormularioObject.FieldHora.val(val);
							FormularioObject.ActivaFecha(false);
						};
					}
					else if(link.getAttribute("data-app-accion")==4){
						link.onclick=function(e){
							$("#ModalEliminar").modal("show");
							$('#idReserva').val(e.target.parentNode.previousElementSibling.getAttribute("data-app-id"));
							$("#preguntaEliminar").text(e.target.parentNode.previousElementSibling.parentElement.nextElementSibling.textContent);
							$("#PlacaE").val(e.target.parentNode.previousElementSibling.parentElement.nextElementSibling.textContent);
							
						}
					}
					else{//editar cita

						link.onclick=function(e){

							FormularioObject.AccionActual=this.getAttribute("data-app-accion");
							FormularioObject.reset();
							FormularioObject.ActivaPlaca(false);
							FormularioObject.setEstado("PlacaNueva","Loading");
							$.post( "Reservas" ,{"accion":4,"IdReserva":e.target.parentNode.previousElementSibling.getAttribute("data-app-id")}, $.proxy(function(result,status){
								this.setEstado("PlacaNueva","Ok");
								if(this.AccionActual==2){//edita una cita existente
									this.FieldPlacaNuevaId.val(result.Data[0][0]);
									$("#FormTitle").text("Editar Reserva de la  placa " + result.Data[0][1]);									
								}
								else{//genera una nueva cita con los datoas existentes
									this.FieldFechaNueva.val("");
									this.FieldPlacaNuevaId.val(0);
								}
								this.ActivaFecha(true);
								this.ActivaHora(true);
								if(this.AccionActual==3){
									this.FieldHora.val("");
									
								}
								this.ActivaPlaca(false);
								this.FieldHora.val(result.Data[0][4]);
								this.FieldPlacaNueva.val(result.Data[0][1]);
								this.FieldTipoRevision.val(result.Data[0][5]);
								this.llenaPropietario(result.Data[0][2]);
								if(result.Data[0][2][0]!=result.Data[0][3][0]){
									this.llenaConductor(result.Data[0][3]);
									$('#infoProp').collapse('show');
									this.FieldEsMismoConductor.prop('checked',false);

								}
							},FormularioObject),"json");

						}

					}
				}
			}

		}

	}

	//cambia el background de la fila d=seleccionada
	if (idSeleccionado!=""){
		$(idSeleccionado).removeClass("table-success");
	}
	idSeleccionado="#"+Padre.id;
	$(idSeleccionado).addClass("table-success");
	

	
}



function Form(){
	this.Form=$("#NuevoRegistroForm");
	this.FieldFechaNueva=$("#FechaNueva"),
	this.FieldHora=$("#Hora");
	this.FieldTipo=$("#Tipo");
	this.FieldPlacaNueva=$("#PlacaNueva");
	this.FieldPlacaNuevaId=$("#idPlaca");
	this.FieldTipoRevision=$("#TipoRevision");
	this.FieldPropietarioId=$("#PropietarioId")
	this.FieldCedula=$("#Cedula");
	this.FieldNombre=$("#Nombre");
	this.FieldCelular=$("#Celular"); 
	this.FieldEsMismoConductor=$("#EsMismoConductor");
	this.FieldConductorId=$("#ConductorId")
	this.FieldCedulaC=$("#CedulaC");
	this.FieldNombreC=$("#NombreC");
	this.FieldCelularC=$("#CelularC");	
	this.ACProp_FieldSelectId;
	this.AccionActual=1;

	//establece el selector de fecha
	this.SelectorDeFecha;
	this.SelectorDeFecha=$('#FechaNueva').datepicker({
		uiLibrary: 'bootstrap4',
		format: "yyyy-mm-dd",
		minDate: today,
		showRightIcon: false,
		header: true,
		weekStartDay: 1,
		maxDate: function() {
			var date = new Date();
			date.setDate(date.getDate()+7);
			return new Date(date.getFullYear(), date.getMonth(), date.getDate());
		}
	});
	
	//establece el autocompletado para el campo Hora
	this.FieldHoraAC=this.FieldHora.autocomplete({
		serviceUrl: 'Reservas',
		minChars:0,
		dataType:"json",
		type:"post",
		paramName:"Hora",
		params:{"Tipo":this.FieldTipo.val(),"accion":5,"Fecha":this.FieldFechaNueva.val()},
		showNoSuggestionNotice:true,
		noSuggestionNotice:"No hay disponibilidad este dia",
		onSearchStart:$.proxy(function () {this.setEstado("Hora","Loading")},this),
		onSearchComplete:$.proxy(function (query, suggestions) {
			this.setEstado("Hora","NotSet");
		},this),
		onSelect: $.proxy(function (suggestion) {	
			this.setEstado("Hora","Ok"); 

		},this),
		onSearchError:$.proxy(function (query, jqXHR, textStatus, errorThrown) {
			this.setEstado("Hora","Error");		
		},this),
		transformResult: function(datos) {
			var a=[];

			console.log(datos);
			for(var i=0;i<datos.Longitud;i++){
				a[i]={"value":datos.Data[i][0],"data":datos.Data[i]}
			}
			return {"suggestions": a};
		}
	});

	//establece el autocompletado para el campo placa
	this.FieldPlacaNuevaAC=this.FieldPlacaNueva.autocomplete({
		serviceUrl: 'Reservas',
		minChars:4,
		dataType:"json",
		type:"post",
		paramName:"Placa",
		params:{"Tipo":this.FieldTipo.val(),"accion":2},
		showNoSuggestionNotice:true,
		noSuggestionNotice:"No Esta Registrada",
		onSearchStart:$.proxy(function () {this.setEstado("PlacaNueva","Loading")},this),
		onSearchComplete:$.proxy(function (query, suggestions) {
			if(this.FieldPlacaNueva.val()!=this.FieldPlacaNueva.attr("data-value-field")){
				this.reset();	  
				this.FieldPlacaNueva.val(query);
			}
		},this),
		onSelect: $.proxy(function (suggestion) {	
			this.FieldPlacaNueva.attr("data-value-field",suggestion.data[1]);
			this.setEstado("PlacaNueva","Ok");
			this.llenaPropietario(suggestion.data[2]);		 
			if(suggestion.data[2][0]!=suggestion.data[3][0]){
				this.llenaConductor(suggestion.data[3]);
				this.FieldEsMismoConductor.prop("checked",false);
				$('#infoProp').collapse('show');
			}
		},this),
		onSearchError:$.proxy(function (query, jqXHR, textStatus, errorThrown) {
			this.setEstado("Placa","Error");		
		},this),
		transformResult: function(response) {
			var a=[];

			for(var i=0;i<response.Longitud;i++){
				a[i]={"value":response.Data[i][1],"data":response.Data[i]}
			}
			return {"suggestions": a};
		}
	});

	


	//establece el autocompletar para el campo cedula
	this.FieldCedula.focus($.proxy(function(){this.ACCedula_FieldSelectId="Cedula";	},this));
	this.FieldCedulaC.focus($.proxy(function(){this.ACCedula_FieldSelectId="CedulaC";},this));

	$("#Cedula,#CedulaC").autocomplete({
		serviceUrl: 'Reservas',
		minChars:4,
		dataType:"json",
		paramName:"Cliente",
		params:{"accion":3},
		showNoSuggestionNotice:true,
		noSuggestionNotice:"Cedula No Registrada",
		type:"post",
		onSearchStart:$.proxy(function (params) {this.setEstado(this.ACCedula_FieldSelectId,"Loading");},this),
		onSearchComplete:$.proxy(function (query, suggestions) {
			if(this.ACCedula_FieldSelectId=="Cedula"){
				if(this.FieldCedula.val()!=this.FieldCedula.attr("data-value-field")){
					this.llenaPropietario(false);
				}
			}
			else{
				if(this.FieldCedulaC.val()!=this.FieldCedulaC.attr("data-value-field")){
					this.llenaConductor(false);
				}
			}
		},this),
		onSelect: $.proxy(function (suggestion) {
			(this.ACCedula_FieldSelectId=="Cedula")?this.llenaPropietario(suggestion.data):this.llenaConductor(suggestion.data);
		},this),

		onSearchError:$.proxy(function (query, jqXHR, textStatus, errorThrown) {
			(this.ACCedula_FieldSelectId=="Cedula")?this.setEstado("Cedula","Error"):this.setEstado("CedulaC","Error");
				
		},this),
		transformResult: function(response) {
			var a=[];
			for(var i=0;i<response.Longitud;i++){
				a[i]={"value":response.Data[i][1],"data":response.Data[i]}
			}
			return {"suggestions": a};
		}
	});



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


}



Form.prototype.ActivaFecha=function(estado){
	if(estado==true){
		//this.FieldFechaNueva.attr("readonly",false);
		this.SelectorDeFecha.off("open");
		//actualiza los parametroa del autocompletado al cambiar la fecha
		this.FieldFechaNueva.on("change",$.proxy(function(e){
			this.FieldHoraAC.autocomplete('setOptions', {params:{"Tipo":this.FieldTipo.val(),"accion":5,"Fecha":this.FieldFechaNueva.val()}});
			this.FieldHora.val("");
			this.FieldHoraAC.autocomplete().clearCache();
			this.setEstado("Hora","notSet");
		},this));
	}
	else{
		this.SelectorDeFecha.on("open",$.proxy(function(e){this.SelectorDeFecha.close();},this));
		//this.FieldFechaNueva.attr("readonly",true);
		this.FieldFechaNueva.off("change");
		this.setEstado("Hora","notSet");
	}
}
Form.prototype.ActivaHora=function(estado){
	if(estado){
		//this.FieldHora.prop("readonly",false);
		this.FieldHoraAC.autocomplete().enable();
		//this.SelectorDeFecha.off("open");
	}
	else{
		//this.SelectorDeFecha.on("open",$.proxy(function(e){this.SelectorDeFecha.close();},this));
		//this.FieldHora.prop("readonly",true);
		this.FieldHoraAC.autocomplete().disable();
	}
}
Form.prototype.ActivaPlaca=function(estado){
	if(estado==true){
		this.FieldPlacaNueva.prop("readonly",false);
		this.FieldPlacaNueva.autocomplete().enable();
	}
	else{
		this.FieldPlacaNueva.prop("readonly",true);
		this.FieldPlacaNueva.autocomplete().disable();
	}
}
Form.prototype.llenaPropietario=function(Datos){
	var llenar=true
	if(Datos==false){
		llenar=false;
		this.setEstado("Cedula","NotSet");
	}
	else{
		this.setEstado("Cedula","Ok");
		this.FieldCedula.attr("data-value-field",Datos[1]);
		this.FieldCedula.val(Datos[1]);
	}
	this.FieldPropietarioId.val(llenar?Datos[0]:0);
	this.FieldNombre.val(llenar?Datos[2]:"");
	this.FieldCelular.val(llenar?Datos[3]:"");
}
Form.prototype.llenaConductor=function(Datos){
	var llenar=true
	if(Datos==false){
		llenar=false;
		this.setEstado("CedulaC","NotSet");
	}
	else{
		this.setEstado("CedulaC","Ok");
		this.FieldCedulaC.attr("data-value-field",Datos[1])
		this.FieldCedulaC.val(Datos[1]);
	}
	this.FieldConductorId.val(llenar?Datos[0]:0);
	this.FieldNombreC.val(llenar?Datos[2]:"");
	this.FieldCelularC.val(llenar?Datos[3]:"");

}


Form.prototype.setEstado=function (idInput,estado){

//	estado:
//	Loading
//	Ok
//	NotOk
//	NotSet
	switch(estado){
	case "Loading":
		$("#"+idInput+"Status").attr("class","spinner-grow text-success");
		break;
	case "Ok":
		$("#"+idInput+"Status").attr("class","fa  fa-2x fa-check text-success");
		break;
	case "NotOk":
		$("#"+idInput+"Status").attr("class","fa fa-2x fa-times text-danger");
		break;
	case "Error":
		$("#"+idInput+"Status").attr("class","fa fa-2x fa-exclamation-circle text-danger");
		break;
	case "NotSet":
	default:
		$("#"+idInput+"Status").attr("class","fa fa-2x fa-check text-success invisible");
	break;
	}

}
Form.prototype.reset=function(){

	this.setEstado("PlacaNueva","NotSet");
	$("#FormTitle").text("Registrar Nueva Reserva");
	this.ActivaFecha(false);
	this.ActivaHora(false);
	this.ActivaPlaca(true);
	this.FieldPlacaNuevaId.val(0);
	this.llenaPropietario(false);
	this.llenaConductor(false);
	this.FieldCedula.attr("data-value-field",'');
	this.FieldCedulaC.attr("data-value-field",'');
	$('#infoProp').collapse('hide');
	$('PropietarioId').val("0");
	$('ConductorId').val("0");
	$('idPLaca').val("0");
	$('accion').val("1");
	document.getElementById("NuevoRegistroForm").reset();	
};

