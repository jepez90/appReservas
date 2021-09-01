<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	%><%@ page import="appReservas.*"
	%><%@ page import="java.sql.*" 
	%><%@ page import="java.util.*" 
	%><%
@SuppressWarnings("unchecked")
ArrayList<Ordenes> listado=(ArrayList<Ordenes>)request.getAttribute("listado");
Fecha fecha=(Fecha)request.getAttribute("fecha");
String placa=(String) request.getAttribute("placa");
	
String titulo;
titulo=fecha.getFecha();
%><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
 <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<!--   <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"> -->
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
    
<script type="text/javascript">	

document.addEventListener("DOMContentLoaded", function(event) {
	$('#Fecha').datepicker({
		uiLibrary: 'bootstrap4',
		format: "yyyy-mm-dd",
		minDate: "2018-11-23",
		showRightIcon: false,
		header: true,
		weekStartDay: 1,
		maxDate: function() {
			var date = new Date();
			return new Date(date.getFullYear(), date.getMonth(), date.getDate());
		}
	});
	
	
    document.getElementById("galeryX").addEventListener("click", cierraGaleria);

    var flechas=document.getElementsByClassName("arrow");
    flechas[0].addEventListener("click", cambiaImagen);
    flechas[1].addEventListener("click", cambiaImagen);

    
    var contenedorThumbs=document.getElementsByName("img");
    for (var i=0;i<contenedorThumbs.length;i++){
		contenedorThumbs[i].addEventListener("click", abreImagen);
    }

    
    //evita que la imagen se agrande al establecer el maxWidth= a ancho de la imagen
   //document.getElementById("galeryBoxImage").addEventListener("load", function(){
//	document.getElementById("galeryBoxImage").style.maxWidth=document.getElementById("galeryBoxImage").naturalHeight+"px";
  //  });
  
})
function abreImagen(ev){
    var nuevasrc=ev.target.src;
    abreGaleria();
    poneNuevaImagen(nuevasrc);
}

function cierraGaleria(){
    document.getElementById("galeryBox").style.display="none";
    document.getElementById("galerySkin").style.display="none";   
    document.removeEventListener("keydown",Tecleado);
}
function abreGaleria(){
    document.getElementById("galeryBox").style.display="flex";
    document.getElementById("galerySkin").style.display="flex";
    document.addEventListener("keydown",Tecleado);
    document.getElementById("galeryNumber").innerText="-";
    
    
}
function poneNuevaImagen(nuevasrc){
    document.getElementById("galeryBoxImage").src="";
    document.getElementById("linkExt").href=nuevasrc;
    var s=nuevasrc.split("&");
    console.log(s);
    document.getElementById("linkExt").attributes.download.value=s[3].substring(2) + "-"+s[1].substring(2) + ".jpg" ;
    document.getElementById("galeryBoxImage").src=nuevasrc;
    
 
}

function cambiaImagen(ev){
    var contenedorThumbs=document.getElementsByName("img");
    for (var i=0;i<contenedorThumbs.length;i++){

		if(contenedorThumbs[i].src==document.getElementById("galeryBoxImage").src){
		     if (ev.target.id=="arrowLeft" || ev.target.id=="arrowLeftSVG"){
				 if(i==0){
				     i=contenedorThumbs.length-1;
				 }
				 else{
				     i=i-1;
				 }
		    }
		    else{
				if(i==(contenedorThumbs.length-1)){
				    i=0;
				}
				else{
				    i=i+1;
				}
		    }
		    var nuevasrc=contenedorThumbs[i].src;
		    poneNuevaImagen(nuevasrc);
		    document.getElementById("galeryNumber").innerText=i + " de " + contenedorThumbs.length;
		    break;
		}
		  
    }
    
   
}
function Tecleado(elEvento) {
	
	// 37   39
	var e = window.event || elEvento;
	var contenedorThumbs=document.getElementsByName("img");
    for (var i=0;i<contenedorThumbs.length;i++){

		if(contenedorThumbs[i].src==document.getElementById("galeryBoxImage").src){
		     if (e.keyCode==37){
				 if(i==0){
				     i=contenedorThumbs.length-1;
				 }
				 else{
				     i=i-1;
				 }
		    }
		    else if(e.keyCode==39){
				if(i==(contenedorThumbs.length-1)){
				    i=0;
				}
				else{
				    i=i+1;
				}
		    }
		    else{
		    	return;
		    }
		    var nuevasrc=contenedorThumbs[i].src;
		    poneNuevaImagen(nuevasrc);
		    document.getElementById("galeryNumber").innerText=i + " de " + contenedorThumbs.length;
		    break;
		}
		  
    }
	}

</script>
<link rel="shortcut icon" href="imagenes/faviconG.png">

<style type="text/css">


.Tlabel {
	display: inline-block;
}
.Tcontent {
	width: 100px;
	font-weight: bold;
	display: inline-block;
}
.card-body img{
	border: solid 1px black;
	border-radius: 3px;
	 width:400px;
}
.card-body img:hover{
	transform: scale(1.1);
	z-index: 100;
	position: relative;
}



/*estilos de la galeria*/
.galeryBox {
    position: fixed;
    width: 100%;
    height: 100%;
    top: 0;
    z-index: 1023;
    left: 0;
    display: none;
    flex-direction: row;
    justify-content: center;
    align-content: center;
    align-items: center;
    background-color: rgba(10, 10, 10, 0.8);
    
}
.galeryBox img {
    width: 90%;
    border: solid 2px aliceblue;
    box-shadow: 0px 0px 20px 10px black;
    border-radius: 3px;
}
.galerySkin{
    position: fixed;
    width: 100%;
    height: 100%;
    top: 0;
    z-index: 1024;
    left: 0;
    display: none;
    flex-direction: row;
}
.galerySkin .arrow{
    order: 0;
    flex: 1 1 5px;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-content: center;
    align-items: center;
    transition: all .3s ease-in-out;
}
.galerySkin .center{
    order: 0;
    flex: 4 1 auto;
    align-self: auto;
    text-align: center;
}

.galerySkin .center div:hover, .galerySkin .arrow:hover{
    background-color: rgba(250, 250,250, 0.6);
    transition: all .3s ease-in-out;
}
#galeryX{
    transition: all .3s ease-in-out;
    top: 10px;
    right: 10px;
    position: absolute;
}
#galeryNumber{
	position: absolute;
	left: 10px;
	top: 10px;
	font-size: 1.2em;
	color: beige;
}
</style>

<title>Reservas: <%= titulo %></title>
</head>
<body>
	<div class="container-fluid">
		<h1 class="text-center"><a href="" class="text-decoration-none">Listado De FOTOS</a></h1>
	</div>
	<nav class="navbar navbar-expand-sm justify-content-center bg-success navbar-dark sticky-top"  >
		<form class="form-inline" method="post" name="FormCabeceraListado"
			autocomplete="off" title="Filtros" id="FormCabeceraListado">
			 
<%-- 			<label for="Placa" class="mr-sm-1 mb-0">Placa</label> 
			<input name="Placa" type="text" 
				autofocus="autofocus" class="form-control  mr-sm-3 form-control-sm" id="Placa" 
				pattern="([A-Za-z]{3}[0-9]{3}|[A-Za-z]{3}[0-9]{2}|[A-Za-z]{3}[0-9]{2}[A-Za-z]{1})" tabindex="1"
				title="El valor placa no es válido" size="8" maxlength="6" value="<%= placa%>"> --%>
			
			<label for="Fecha" class="mr-sm-1 mb-0 ">Fecha</label> 
			<input type="text" required="required" class="form-control mr-sm-3 form-control-sm" id="Fecha"
				pattern="[2][0][0-3][0-9]-[0-1][0-9]-[0-3][0-9]" value="<%=fecha.getFecha() %>" tabindex="2"
				size="10" name="Fecha" >
			<input name="Buscar" type="submit" class="btn btn-outline-dark mr-sm-2 "
				id="Buscar" tabindex="3" title="Actualizar Listado" /> 
		</form >
	</nav>
	<div class="container" style=" max-width: 950px">		
		
	<div id="accordion">
	
<% int i=0; for (Ordenes o:listado){ i++; %>
	    <div class="card">
	      <div class="card-header">
	        <a class="card-link" data-toggle="collapse" href="#collapse<%=o.getIdFoto1() %>">
	          <span class="Tlabel">Placa:&nbsp; </span><span class="Tcontent"> <%=o.getPlaca() %></span>
	          <span class="Tlabel">Fecha:&nbsp; </span><span class="Tcontent"  style="width:200px"> <%=o.getFecha().getFechayHora() %></span>
	          <span class="Tlabel"># Revision: &nbsp;</span><span class="Tcontent"> <%=o.getNumRevision() %></span>
	          <span class="Tlabel">Resultado:&nbsp;</span><span class="Tcontent"> <%=o.getResultadoInt() %></span>
	        </a>
	      </div>
	      <div id="collapse<%=o.getIdFoto1() %>" class="collapse" data-parent="#accordion">
	        <div class="card-body">
	          <img src="Fotos?a=<%=o.getIdFoto1() %>&b=1&Fecha=<%=fecha.getFecha() %>&c=<%=o.getPlaca() %>" alt="Foto 1" name="img" />
	          <img src="Fotos?a=<%=o.getIdFoto1() %>&b=2&Fecha=<%=fecha.getFecha() %>&c=<%=o.getPlaca() %>" alt="Foto 2" name="img" />
	        </div>
	      </div>
	    </div>
<% } %>    
	    
	    
	    
	  </div>
		
		
	</div>

	<div class="galeryBox" id="galeryBox" style="display: none;">
		<img src="https://i.imgur.com/1ToKdv7.jpg" title="Image" id="galeryBoxImage" style="max-width: 750px;">
	</div>
	<div class="galerySkin" id="galerySkin" style="display: none;">
	  <div class="arrow" id="arrowLeft">
	    <svg width="50px" height="60px" id="arrowLeftSVG">
	      <polygon style="fill:#FFF;stroke:black;stroke-width:3;fill-rule:evenodd;" points="30,2 14,27 30,52 14,27"></polygon>
	      &gt;
	    </svg>
	  </div>
	  <div class="center"><div  id="galeryNumber" >-</div><a href="" target="_blank" id="linkExt" download="img.jpg">Descargar Foto</a>
	    <div  id="galeryX" >
	      <svg width="25px" height="25px">
		<polygon points="5,5 21,21 13,13 5,21 21,5 13,13" style="fill:#FFF;stroke:black;stroke-width:3;"></polygon></svg>
	      </div>
	      
	  </div>
	  <div class="arrow" id="arrowRight">
	    <svg width="50px" height="60px">
	      <polygon style="fill:#FFF;stroke:black;stroke-width:3;fill-rule:evenodd;" points="14,2 30,27 14,52 30,27"></polygon>
	      &gt;
	    </svg>
	  </div>
	</div>
</body>
</html>
