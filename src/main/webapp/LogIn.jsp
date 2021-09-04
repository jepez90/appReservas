<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	%><%@ page import="com.jp.appreservas.*"%><%
	boolean invalidData=(request.getAttribute("invalidData")!=null)?(boolean)request.getAttribute("invalidData"):false;
	boolean isInactive=(request.getAttribute("isInactive")!=null)?(boolean)request.getAttribute("isInactive"):false;
	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Iniciar Sesión - CDA SANTA LIBRADA</title>
	
  <script src="js/jquery.min.js"></script> 
  <link rel="stylesheet" href="Styles/LogIn.css">
  <link rel="stylesheet" href="Styles/bootstrap.min.css">
  <script src="js/bootstrap.min.js"></script>
  
<link rel="shortcut icon" href="imagenes/faviconG.png">
  

</head>
<body>
	<div class="login">
		<img src="imagenes/LOGO2.png" width="300" height="219" alt="" />
		
		    <%if(invalidData){ %><div class="alert alert-danger alert-dismissible fade show">
		    						<button type="button" class="close" data-dismiss="alert">&times;</button>
		   							<strong>Error!</strong> Usuario o contraseña incorrecta.
		   							</div> <%}%>
		    <%if(isInactive){ %><div class="alert alert-danger alert-dismissible fade show">
		    						<button type="button" class="close" data-dismiss="alert">&times;</button>
		   							<strong>Error!</strong> El usuario no tiene autorizacion para ingresar.
		   							</div> <%}%>

		<form method="post" autocomplete="off">
			<input type="text" name="u" class="" placeholder="Usuario" tabindex="1" autofocus="autofocus" required="required" />
			<input type="password" name="p" placeholder="Contraseña" tabindex="2"
				required="required" />
			<button type="submit" class="btn btn-primary btn-block btn-large" tabindex="3">Ingresar</button>
		</form>
	</div>
</body>
</html>
