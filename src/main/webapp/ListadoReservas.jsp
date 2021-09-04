<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
    import="com.jp.appreservas.*, java.sql.*, java.util.*"%><%
        
    @SuppressWarnings(  "unchecked")
    ArrayList<Reserva> reservas = (ArrayList<Reserva>) request.getAttribute("listado");

    Usuario usuario = (Usuario) request.getSession().getAttribute("UserLoguedNow");

    Horario horario = (Horario) request.getAttribute("Horario");

    String placa = (String) request.getAttribute("Placa");

    boolean mostrarPendientes = (boolean) request.getAttribute("mostrarPendientes");


    ArrayList<String[]> alertas = (ArrayList<String[]>) request.getAttribute("Mensaje");
    
    int tipo = (int) request.getAttribute("tipo");

    String titulo;
    if (placa.equals("")) {
        titulo = horario.getFecha().getFecha();
    } else {
        titulo = placa;
    }

    Fecha ahora = new Fecha();
%><!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <!--   <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"> -->
        <!-- Bootstrap -->
        <script src="js/jquery.min.js"></script> 
        <link rel="stylesheet" href="Styles/bootstrap.min.css">
        <script src="js/popper.min.js"></script>
        <script src="js/bootstrap.min.js"></script>

        <!-- datepicker https://gijgo.com/datepicker/ -->  
        <script src="js/gijgo.min.js" type="text/javascript"></script>
        <link href="Styles/gijgo.min.css" rel="stylesheet" type="text/css" />

        <!-- font awesome 4.6 https://fontawesome.com/v4.7.0/ --> 
        <link href="Styles/font-awesome.min.css" rel="stylesheet" type="text/css" /> 
        <!-- 	 <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" /> -->

        <!-- autocomplete https://github.com/devbridge/jQuery-Autocomplete-->
        <script src="js/jquery.autocomplete.js" type="text/javascript"></script>
        <script src="js/jquery.mockjax	.js" type="text/javascript"></script>

        <!--[if lt IE 9]>
             <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
             <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
           <![endif]-->

        <script src="js/scripts.js" type="text/javascript"></script>
        <link rel="shortcut icon" href="imagenes/faviconG.png">

        <style type="text/css">
            .horaCero{border-top:solid 2px;}
            .noEstado{margin-right: 30px}
            .autocomplete-suggestions { overflow: auto; border: 1px solid #CBD3DD; background: #FFF; }
            .autocomplete-suggestion { overflow: hidden; padding: 1px 15px; white-space: nowrap; color: #6c757d;  border-radius: 4px; border: 1px solid #beccd9; }
            .autocomplete-selected { background: #F0F0F0; }
            .autocomplete-suggestions strong { color: #029cca; font-weight: bold; }

        </style>

        <title>Reservas: <%= titulo%></title>
    </head>
    <body>
        <div class="container-fluid">
            <h1 class="text-center"><a href="" class="text-decoration-none">Listado De Reservas</a></h1>
        </div>


        <nav class="navbar navbar-expand-sm justify-content-center bg-success navbar-dark sticky-top"  >
            <form class="form-inline" method="post" name="FormCabeceraListado"
                  autocomplete="off" title="Filtros" id="FormCabeceraListado">
                <label for="Placa" class="mr-sm-1 mb-0">Placa</label> 
                <input name="Placa" type="text" 
                       autofocus="autofocus" class="form-control  mr-sm-3 form-control-sm" id="Placa" 
                       pattern="([A-Za-z]{3}[0-9]{3}|[A-Za-z]{3}[0-9]{2}|[A-Za-z]{3}[0-9]{2}[A-Za-z]{1})" tabindex="1"
                       title="El valor placa no es válido" size="8" maxlength="6" value="<%= placa%>">

                <label for="Fecha" class="mr-sm-1 mb-0 ">Fecha</label> 
                <input type="text" required="required" class="form-control mr-sm-3 form-control-sm" id="Fecha"
                       pattern="[2][0][0-3][0-9]-[0-1][0-9]-[0-3][0-9]" value="<%= horario.getFecha().getFecha()%>" tabindex="2"
                       size="10" name="Fecha" >
                <input name="submit" type="submit" class="btn btn-outline-dark mr-sm-2  <% if (tipo == 2) { %>font-weight-bold<% }%>"
                       id="Livianos" tabindex="3" title="Actualizar Listado"
                       value="Livianos" /> 
                <input name="submit" type="submit"
                       class="btn btn-outline-dark mr-sm-3 mr-lg-5 <% if (tipo == 1) { %>font-weight-bold<% }%>" id="Motocicletas" tabindex="4"
                       title="Actualizar Listado" value="Motocicletas">
                <div class="dropdown">
                    <i class=" user fa-user-circle-o fa fa-2x dropdown-toggle" data-toggle="dropdown"></i>
                    <div class="dropdown-menu">
                        <h4 class="dropdown-header text-center font-weight-bold"><i class=" fa-address-card fa"></i> <%=usuario.getNombre()%></h4>
                        <span class="dropdown-item-text small">Sesion iniciada a las <%=usuario.getLoggedAt().getHora()%></span>
                        <div class="dropdown-divider"></div>
                        <a href="Admin?a=edit"  class="dropdown-item" ><i class="fa fa-pencil-square-o"></i> Editar</a> 
                        <button class="dropdown-item" name="UFLog" type="submit" form="UF"><i class="fa fa-sign-in"></i> Cerrar Sesion</button>
                        <div class="dropdown-divider"></div>
                        <%if (usuario.getIsAdmin() == 0) { %> 
                        <span class="dropdown-item disabled"><i class="fa-user-plus fa"></i> Agregar un nuevo Usuario</span>
                        <%} else {%>
                        <a href="Admin?a=add"  class="dropdown-item" ><i class="fa-user-plus fa"></i> Agregar un nuevo Usuario</a>
                        <%}%>
                    </div>
                </div> 
                <!-- 				<input type="hidden"  name="accion" id="accion" value="0"> -->
            </form >
            <form method="post" id="UF" name="UF">
            </form>

        </nav>

        <div class="container" style=" max-width: 950px">
            <% if (alertas != null) {%>
            <%     for (String[] a : alertas) {%>
            <div class="alert <%=(a[0].equals("Exito")) ? "alert-success" : ""%> <%=(a[0].equals("Error")) ? "alert-danger" : ""%> alert-dismissible fade show">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <strong><%= a[0]%>!</strong> <%= a[1]%>.
            </div>
            <%      }%>
            <%}%>






            <div class="table-responsive">
                <table border="1" class="table table-bordered table-hover table-sm ">
                    <tbody  id="BodyTablaPrincipal">
                        <tr>
                            <%if (!placa.equals("")) {%>
                            <th class="text-sm-center" scope="col">FECHA</th>
                            <th class="text-sm-center"  scope="col">HORA</th>
                                <% } else {%>
                            <th class="text-sm-center" colspan="2" scope="col">HORA</th>
                                <% }%>
                            <th class="text-sm-center" scope="col">ACCION</th>
                            <th class="text-sm-center" scope="col">PLACA</th>
                            <th class="text-sm-center" scope="col">TIPO DE REVISION</th>
                            <th class="text-sm-center" scope="col">CEDULA</th>
                            <th class="text-sm-center" scope="col">NOMBRE</th>
                            <th class="text-sm-center" scope="col">CELULAR</th>
                        </tr>
                        <% int i = 0;%>
                        <%for (Reserva r : reservas) {
                                i++;
                                //SI ES UNA RESERVA VACIA
                                if (r.getId() == 0) {%>
                            <tr id="U<%= i%>" class="<%
                                if (r.getEnListaBorde() != 0) { %>horaCero<%}%>">
                            <%//-------------celda de fecha o de hora rowspan----------------
                                if (!placa.equals("")) {%>
                            <td class="text-sm-center" ><%= r.getFechaReserva().getFecha()%></td>
                            <% } else {
                                if (r.getEnListaBorde() != 0) {%>
                            <td class="text-sm-center align-middle" rowspan="<%=r.getEnListaBorde()%>" data-toggle="tooltip" title="Total reservas en esta hora: <%=r.getEnListaBorde()%>" ><%= r.getHoraReserva().getHoraDia() + " " + r.getHoraReserva().getAmPm()%></td>
                            <% }
                                }
                                if (placa.equals("")) {%>
                            <td class="text-sm-center"><%= r.getHoraReserva().getHoraDia() + ":" + r.getHoraReserva().getMinutos()%></td>
                            <% } else {%>
                            <td class="text-sm-center" ><%= r.getHoraReserva().getHora()%></td>
                            <% }

                                //-------------celda de accion----------------
                                if (r.getEnListaAccion() == 3) { //accion agregar %>
                            <td class="text-sm-center">
                                <button type="button"  id="U<%= i%>btn" class="btn btn-outline-success btn-sm btn-block"  data-toggle="modal" data-target="#myModal"  data-app-hora="<%=r.getHoraReserva().getHora()%>"   style="padding:1px;">
                                    <i class="fa fa-plus"></i>
                                </button>

                            </td>
                            <% } else {//no hacer nada%>
                            <td class="text-sm-center"></td>
                            <% }
                                //-------------celdas de informacion----------------
                            %>
                            <td class="text-sm-center" ></td>
                            <td class="text-sm-center" ></td>
                            <td class="text-sm-center" ></td>
                            <td class="text-sm-center" ></td>
                            <td class="text-sm-center" ></td>
                        </tr>
                        
                        <% }else {// si es una reserva no vacia%>
                        <tr  id="U<%= i%>" class=" <% 
						if(r.getEnListaBorde()!=0){ %>horaCero <% }%>">
                            <%//-------------celda de fecha o de hora rowspan----------------%>
                            <%if (!placa.equals("")) {%>
                            <td class="text-sm-center" ><%= r.getFechaReserva().getFecha() %></td>
                            <% }else{ if(r.getEnListaBorde()!=0){%>
                            <td class="text-sm-center  align-middle" rowspan="<%=r.getEnListaBorde() %>" data-toggle="tooltip" title="Total reservas en esta hora: <%=r.getEnListaBorde() %>"><%= r.getHoraReserva().getHoraDia()+" "+r.getHoraReserva().getAmPm()%></td>
                            <% }}
                                if (placa.equals("")) {%>
                            <td class="text-sm-center"><%= r.getHoraReserva().getHoraDia() + ":" + r.getHoraReserva().getMinutos()%></td>
                            <% } else {%>
                            <td class="text-sm-center" ><%= r.getHoraReserva().getHora()%></td>
                            <% }

                                //-------------celda de accion----------------
                                if (r.getEnListaAccion() == 1) {//accion dropdown para registro pasado%>
                            <td class="text-sm-center dropdown">
                                <button type="button"  id="U<%= i%>btn" data-toggle="dropdown"  data-app-hora="<%=r.getHoraReserva().getHora()%>"  data-app-hora="<%=r.getHoraReserva().getHora()%>" data-app-id="<%=r.getId()%>"   class="btn btn-outline-success btn-sm btn-block" style="padding:1px;" >
                                    <i class="fa fa-angle-double-down"></i>
                                </button>
                                <div class="dropdown-menu">
                                    <% if (r.getFechaReserva().getFecha().equals(ahora.getFecha())) { %>
                                    <a class="dropdown-item " data-app-accion="1" data-toggle="modal"  href="#myModal">Marcar como Ingresado</a> 
                                    <%} else {%>
                                    <span class="dropdown-item disabled" style="cursor: not-allowed;pointer-events: all;">Marcar como Ingresado</span>
                                    <%} %>
                                    <span class="dropdown-item disabled" style="cursor: not-allowed;pointer-events: all;">Modificar Esta Cita</span>
                                    <a class="dropdown-item" data-app-accion="3" data-toggle="modal" href="#myModal">Agendar nueva cita para este vehículo</a>
                                    <span class="dropdown-item disabled" style="cursor: not-allowed;pointer-events: all;">Eliminar Esta Cita</span>
                                </div>
                            </td>
                            <% } else {//accion dropdown para registro futuro %>
                            <td class="text-sm-center dropdown">
                                <button type="button"   id="U<%= i%>btn"  data-toggle="dropdown" data-app-hora="<%=r.getHoraReserva().getHora()%>" data-app-id="<%=r.getId()%>" class="btn btn-outline-success btn-sm btn-block" style="padding:1px;">
                                    <i class="fa fa-angle-double-down"></i>
                                </button>
                                <div class="dropdown-menu">
                                    <% if (r.getFechaReserva().getFecha().equals(ahora.getFecha())) { %>
                                    <a class="dropdown-item " data-app-accion="1" data-toggle="modal"  href="#myModal">Marcar como Ingresado</a> 
                                    <%} else {%>
                                    <span class="dropdown-item disabled" style="cursor: not-allowed;pointer-events: all;">Marcar como Ingresado</span>
                                    <%} %>
                                    <a class="dropdown-item " data-app-accion="2" data-toggle="modal"  href="#myModal">Modificar Esta Cita</a>
                                    <span class="dropdown-item disabled" style="cursor: not-allowed;pointer-events: all;" >Agendar nueva cita para este vehículo</span>
                                    <a class="dropdown-item " data-app-accion="4">Eliminar Esta Cita</a>
                                </div>
                            </td>
                            <% }

                                //-------------celdas de informacion----------------
                            %>
                            <td class="text-sm-center" ><%= r.getPlaca()%></td>
                            <td class="text-sm-center" ><%= r.getTipoDeRevision()%></td>
                            <td class="text-sm-center" ><%= r.getPropietario().getDoc()%></td>
                            <td class="text-sm-center" ><%= r.getPropietario().getNombres()%></td>
                            <td class="text-sm-center" ><%= r.getPropietario().getCelular()%></td>
                        </tr>
                        <% }
                            }%>
                    </tbody>
                </table>

                <%if (i == 0) {%><div class=" text-center bg-danger text-white"><%=horario.getComentario()%></div><%}%>
                <div class=" text-center">&copy;2020</div>

            </div>
        </div>



        <!-- The Modal eliminar registro -->
        <div class="modal fade" id="ModalEliminar">
            <div class="modal-dialog modal-sm modal-dialog-centered">
                <div class="modal-content">

                    <!-- Modal Header -->
                    <div class="modal-header">
                        <h4 class="modal-title">Eliminar Reserva</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>

                    <!-- Modal body -->
                    <div class="modal-body text-sm-center">
                        <i class="fa-exclamation-triangle fa fa-3x text-danger"></i><br />En realidad desea eliminar la reserva de la placa <span id="preguntaEliminar">?</span>  
                    </div>

                    <!-- Modal footer -->
                    <form class="modal-footer form-row" method="post" accept-charset="utf-8" title="Eliminar registro" >
                        <div class="col">
                            <input type="submit" name="eliminar" id="eliminar"
                                   class=" form-control btn btn-danger" value="Eliminar" tabindex="17">
                        </div>

                        <div class="col">
                            <input type="button" name="button7" id="button7" class="form-control btn btn-secondary" 
                                   value="Cancelar" tabindex="18" data-dismiss="modal">
                        </div>

                        <input type="hidden" name="idReserva" id="idReserva" value="0">
                        <input type="hidden" name="Fecha" id="FechaE" value="<%= horario.getFecha().getFecha()%>">
                        <input type="hidden"  name="accion" id="accionE" value="6">
                        <input type="hidden"  name="Placa" id="PlacaE" value="">
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal nuevo registro -->
        <div class="modal fade" id="myModal" role="dialog" style="visibility:block">
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
                                <label for="Tipo" class="control-label col-sm-4">Tipo:</label>
                                <div class="col-sm-8 input-group">
                                    <input type="text" name="Tipo" required="required" id="Tipo" class="form-control mb-2 form-control-sm noEstado" tabindex="7"
                                           readonly="readonly" value="<%=(tipo == 1) ? "Motocicleta" : "Liviano"%>" >
                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-4 " for="FechaNueva">Fecha:</label>
                                <div class="col-sm-8 input-group">
                                    <input type="text" name="FechaNueva" required="required" id="FechaNueva" 
                                           class="form-control mb-2 form-control-sm noEstado" readonly="readonly" tabindex="5"
                                           pattern="[2][0][0-3][0-9]-[0-1][0-9]-[0-3][0-9]" value="<%= horario.getFecha().getFecha()%>">

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
                                <label for="PlacaNueva" class="control-label col-sm-4">Placa
                                    del vehículo:</label>
                                <div class="col-sm-8 input-group">
                                    <input name="PlacaNueva" type="text" autofocus="autofocus"
                                           required="required" class="form-control mb-2 form-control-sm" id="PlacaNueva"
                                           pattern="<% if (tipo == 1) { %>([A-Za-z]{3}[0-9]{2}|[A-Za-z]{3}[0-9]{2}[A-Za-z]{1})<% } else {%>[A-Za-z]{3}[0-9]{3}<% }%>"
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


    </body>
</html>
