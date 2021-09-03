package com.jp.appreservas;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class Controlador
 */
@WebServlet("/Gestion")
public class ControladorGestion extends HttpServlet {

    private static final long serialVersionUID = 1L;

    //definir el datasource
    @Resource(name = "jdbc/conecciones")
    private DataSource conexiones;
    private Map<String, String[]> Parametros;
    private String placa;
    private Fecha fecha;
    private int tipoDeVehiculo;
    private boolean mostrarPendientes;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorGestion() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //comprobamos inicio de sesion
        //System.out.println(request.getContextPath());
        Autenticacion aut = new Autenticacion(conexiones, request.getSession());
        aut.logOutUser(request);
        if (!aut.isUserLogued()) {// si el usuario no est� logueado
            if (!aut.logUser(request)) {
                RequestDispatcher disp = request.getRequestDispatcher("/LogIn.jsp");
                disp.forward(request, response);
                return;
            }
        } else {// si el usuario est� logueado
            int estadoLoguin = aut.userEdit(request);
            if (estadoLoguin == 1 || estadoLoguin == 2) {//se envia el formulario de edit o new user
                RequestDispatcher disp = request.getRequestDispatcher("/User.jsp");
                disp.forward(request, response);
                return;
            } else if (estadoLoguin == 4) {//error al realizar el registro o edicion de usuario
                request.setAttribute("error", "Ocurrio un error al realizar la accion: Inconsistencia en los datos");
                RequestDispatcher disp = request.getRequestDispatcher("/User.jsp");
                disp.forward(request, response);
                return;
            } else if (estadoLoguin == 3) {//exito al realizar la edicion o registro de usuario
                request.setAttribute("exito", "Informacion Registrada correctamente");
                RequestDispatcher disp = request.getRequestDispatcher("/User.jsp");
                disp.forward(request, response);
                return;
            }
        }
        String Metodo = request.getMethod();
        Parametros = request.getParameterMap();
        System.out.println("-----------------parametros de la peticion--------------------");
        for (String clave : Parametros.keySet()) {
            System.out.println("[" + clave + " : " + Parametros.get(clave)[0] + "]");
            
            Logger.getLogger(Autenticacion.class.getName()).log(Level.SEVERE, "[{0} : {1}]", new Object[]{clave, Parametros.get(clave)[0]});
        }
        System.out.println("[Metodo de la peticion:" + Metodo + "]");
        System.out.println("-----------------end parametros--------------------");

        //Accion:
        //0 por defecto  cargar listado
        //1 registrar
        //2 buscar Placas registradas 
        //3 buscar clientes
        //4 buscar registros por id
        //5 busca reservas vacias por fecha
        //6 elimina reservas
        //declaramos e iniciamos variables con su valor por defecto
        int accion = 0;
        placa = "";
        fecha = new Fecha();
        tipoDeVehiculo = 2;

        //llenamos las variables con los valores enviados si existen
        if (Parametros.containsKey("accion")) {
            if (!Metodo.equals("GET")) {//si la peticion no es post, devolvera el listado de reservas
                accion = Integer.parseInt(Parametros.get("accion")[0]);
            }
        }

        if (Parametros.containsKey("Placa")) {
            placa = Parametros.get("Placa")[0].toUpperCase();
        } else if (Parametros.containsKey("PlacaNueva")) {
            placa = Parametros.get("PlacaNueva")[0].toUpperCase();
        }

        if (Parametros.containsKey("Fecha")) {
            fecha = new Fecha(Parametros.get("Fecha")[0]);
        } else if (Parametros.containsKey("FechaNueva")) {
            fecha = new Fecha(Parametros.get("FechaNueva")[0]);
        }

        if (Parametros.containsKey("submit")) {
            if (Parametros.get("submit")[0].equalsIgnoreCase("Motocicletas")) {
                tipoDeVehiculo = 1;
            }
        }
        if (!placa.equals("")) {// si existe el parametro de placa, se sobreescribe tipo de vehiculo encontrado en submit
            tipoDeVehiculo = 2;
            if (placa.length() != 6) {
                if (placa.length() == 5) {// es una placa de motocicleta de 5 caracteres
                    tipoDeVehiculo = 1;
                }
                // si no tiene 5 o 6 caracteres no es una placa valida, por lo tanto se toma 2 por defecto
            } else {//si el 6 digito no es numero es motocicleta
                try {
                    Integer.parseInt(placa.substring(5));
                } catch (NumberFormatException e) {
                    tipoDeVehiculo = 1;
                }
            }
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            boolean existCookie = false;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("mostrarPendientes")) {
                    mostrarPendientes = (cookie.getValue().equals("true"));
                    existCookie = true;
                }
            }
            if (!existCookie) {
                Cookie cookie = new Cookie("mostrarPendientes", "false");
                mostrarPendientes = false;
                cookie.setMaxAge(24 * 60 * 60);
                response.addCookie(cookie);
            }
        } else {
            Cookie cookie = new Cookie("mostrarPendientes", "false");
            mostrarPendientes = false;
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);
        }

        request.setAttribute("mostrarPendientes", mostrarPendientes);

        if (Parametros.containsKey("Tipo")) {
            if (Parametros.get("Tipo")[0].equalsIgnoreCase("Motocicleta")) {
                tipoDeVehiculo = 1;
            } else {
                tipoDeVehiculo = 2;
            }
        }

        System.out.println("Accion: " + accion + ", Tipo: " + tipoDeVehiculo + ", Fecha: " + fecha.getFecha() + ", Placa: " + placa + ", switch: " + mostrarPendientes);

        switch (accion) {
            case 1:
                //registra informacion de una reserva, junto con Su propietario y conductor
                nuevoregistro(request, response);
                placa = "";
                //carga de vuelta el listado de reservas
                cargaListadoDeReservas(request, response);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                //Ejecuta las peticiones de informacion recibidas via ajax
                //la respuesta se escribe en la funcion
                peticionAjax(response, accion);
                break;
            case 6:
                //es una peticion de eliminacion de reserva
                eliminaReserva(request, response);
                placa = "";
                cargaListadoDeReservas(request, response);
                break;
            case 0:
            default:
                //por defecto, Unicamente carga el listado de reservas
                //ejecuta este codigo para cargar el listado de reservas
                cargaListadoDeReservas(request, response);
                break;
        }

    }

    private void eliminaReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ModeloReserva modelo = new ModeloReserva(conexiones);
        int idReserva = 0;
        if (Parametros.containsKey("idReserva")) {
            try {
                idReserva = Integer.parseInt(Parametros.get("idReserva")[0]);
            } catch (NumberFormatException e) {
                idReserva = 0;
                modelo.setAlert(new String[]{"Error", "No podemos eliminar esta reserva"});
            }
        }
        if (idReserva != 0) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("UserLoguedNow");
            Reserva r = new Reserva(idReserva);
            r.setPlaca(placa);
            r.setUsuarioRegistraId(usuario.getId());
            r.setEsActiva(0);
            modelo.saveToDTB(r);
        }

        request.setAttribute("Mensaje", modelo.getAlerts());

    }

    private void peticionAjax(HttpServletResponse response, int accion) {
        // TODO Auto-generated method stub

        response.setContentType("application/json");
        PrintWriter w = null;
        try {
            w = response.getWriter();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            
            Logger.getLogger(Autenticacion.class.getName()).log(Level.SEVERE, null, e);
        }
        Ajax ajax = new Ajax(conexiones);
        String result;

        switch (accion) {
            case 2://busca placa
                result = ajax.getJSONString(ajax.getPlacas(Parametros.get("Placa")[0], tipoDeVehiculo));
                break;
            case 3://busca cliente
                result = ajax.getJSONString(ajax.getCedulas(Parametros.get("Cliente")[0]));
                break;
            case 4://busca reserva por id
                result = ajax.getJSONString(ajax.getPlacas(Parametros.get("IdReserva")[0], 0));
                break;
            default://busca listado de reservas vacias
                ModeloReserva modeloR = new ModeloReserva(conexiones);
                List<Reserva> listadoReservas;
                ArrayList<Object[]> listado = new ArrayList<>();
                String HoraBuscada;
                Fecha horaInicio;
                Fecha horaFin;
                Fecha hora;
                Fecha ahora;

                ModeloHorarios modeloHorarios = new ModeloHorarios(conexiones);
                Horario horario = modeloHorarios.getHorario(fecha, tipoDeVehiculo);

                horaInicio = horario.getHoraInicio();
                horaFin = horario.getHoraFin();
                ahora = new Fecha();

                if (Parametros.containsKey("Hora")) {
                    HoraBuscada = Parametros.get("Hora")[0];
                    if (HoraBuscada.length() > 4) {//no se filtran
                        HoraBuscada = "";
                    }
                } else {
                    HoraBuscada = "";
                }

                if (ahora.getFecha().contentEquals(fecha.getFecha())) {
                    ahora.next();
                    ahora.next();
                    hora = new Fecha(ahora.getHora().substring(0, 4) + "0:00");
                } else {
                    hora = horaInicio;
                }

                listadoReservas = modeloR.getReservas(fecha, "", tipoDeVehiculo);

                while (!hora.isInInterval(horaFin)) {
                    if (hora.after(horaFin)) {
                        break;
                    }
                    boolean ocupada = false;

                    for (Reserva r1 : listadoReservas) {
                        if (hora.isInInterval(r1.getHoraReserva())) {//existe una reserva en esta hora
                            ocupada = true;
                        }
                    }

                    if (!ocupada) {
                        if (HoraBuscada.equals("")) {
                            listado.add(new String[]{hora.getHora()});
                        } else {//filtra la hora con el texto ingresato como horabuscada
                            if (hora.getHora().lastIndexOf(HoraBuscada) != -1) {
                                listado.add(new String[]{hora.getHora()});
                            }
                        }
                    }

                    hora.next();
                    hora.setInterval(horario.getIntervalo(hora));
                }
                result = ajax.getJSONString(listado);
                break;
        }

        w.println(result);
    }

    private void nuevoregistro(HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub
        ModeloReserva modelo = new ModeloReserva(conexiones);

        Fecha hora = new Fecha();
        String placa = "";
        int idReserva = 0;
        String tipoDeRevision = "";
        String tipoDeVehiculoString = "";
        int propietarioId = 0;
        String cedula = "";
        String nombre = "";
        String celular = "";
        int conductorId = 0;
        int tipoDeRevisionId;
        boolean esMismoConductor = true;
        int UsuarioRegistraId = 1;
        int tipoDoc = 1;

        String[] parametrosEsperados = new String[]{
            "FechaNueva",
            "Hora",
            "Tipo",
            "PlacaNueva",
            "idPlaca",
            "TipoRevision",
            "PropietarioId",
            "Cedula",
            "Nombre",
            "Celular",
            "ConductorId",
            "CedulaC",
            "NombreC",
            "CelularC"};

        //primero se verifica sy los parametros estan completso
        for (String p : parametrosEsperados) {
            if (!Parametros.containsKey(p)) {
                modelo.setAlert(new String[]{"Error", "Parametros incompletos o inconsistentes para esta peticion" + p});
                request.setAttribute("Mensaje", modelo.getAlerts());
                return;
            } else {
                if (Parametros.get(p)[0].equals("")) {
                    if (!p.substring(p.length() - 1).equalsIgnoreCase("C")) {
                        modelo.setAlert(new String[]{"Error", "Parametros incompletos o inconsistentes para esta peticion 2"});
                        request.setAttribute("Mensaje", modelo.getAlerts());
                        return;
                    }
                }
            }

        }
        Usuario usuario = (Usuario) request.getSession().getAttribute("UserLoguedNow");

        //Asigna los par�metros
        fecha = new Fecha(Parametros.get("FechaNueva")[0]);
        hora = new Fecha(Parametros.get("Hora")[0]);
        placa = Parametros.get("PlacaNueva")[0].toUpperCase().trim();
        tipoDeVehiculoString = Parametros.get("Tipo")[0];
        if (tipoDeVehiculoString.equals("Motocicleta")) {
            tipoDeVehiculo = 1;
        } else {
            tipoDeVehiculo = 2;
        }

        try {
            propietarioId = Integer.parseInt(Parametros.get("PropietarioId")[0]);
            conductorId = Integer.parseInt(Parametros.get("ConductorId")[0]);
            tipoDeRevisionId = Integer.parseInt(Parametros.get("TipoRevision")[0]);
            idReserva = Integer.parseInt(Parametros.get("idPlaca")[0]);
        } catch (NumberFormatException e) {
            modelo.setAlert(new String[]{"Error", "Parametros incompletos o inconsistentes para esta peticion:" + e.getMessage()});
            request.setAttribute("Mensaje", modelo.getAlerts());

            Logger.getLogger(Autenticacion.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        if (Parametros.containsKey("EsMismoConductor")) { // establece si es mismo conductor
            esMismoConductor = Parametros.get("EsMismoConductor")[0].equalsIgnoreCase("on");
        } else {//se debe actualizar el propietario
            esMismoConductor = false;
        }

        Reserva reserva = new Reserva(fecha, hora, placa, UsuarioRegistraId, tipoDeRevisionId, tipoDeVehiculo);
        reserva.setId(idReserva);
        reserva.setTipoDeRevision(tipoDeRevision);
        reserva.setTipoDeVehiculo(tipoDeVehiculoString);

        cedula = Parametros.get("Cedula")[0].trim();
        nombre = Parametros.get("Nombre")[0].toUpperCase().trim();
        celular = Parametros.get("Celular")[0].trim();
        Cliente cliente = new Cliente(tipoDoc, cedula, nombre, celular);
        cliente.setId(propietarioId);

        reserva.setPropietarioId(propietarioId);
        reserva.setPropietario(cliente);

        if (!esMismoConductor) {
            cedula = Parametros.get("CedulaC")[0].trim();
            nombre = Parametros.get("NombreC")[0].toUpperCase().trim();
            celular = Parametros.get("CelularC")[0].trim();
            cliente = new Cliente(tipoDoc, cedula, nombre, celular);
            cliente.setId(conductorId);

            reserva.setConductorId(conductorId);
            reserva.setConductor(cliente);
        } else {
            reserva.setConductorId(propietarioId);
        }
        reserva.setUsuarioRegistraId(usuario.getId());

        System.out.print("Nueva Reserva: ");
        System.out.println(reserva);

        modelo.saveToDTB(reserva);
        request.setAttribute("Mensaje", modelo.getAlerts());

    }

    private void cargaListadoDeReservas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Reserva> listadoReservas;
        List<Reserva> listado = new ArrayList<>();
        ModeloHorarios modeloHorarios = new ModeloHorarios(conexiones);
        Horario horario;

        Fecha horaInicio;
        Fecha horaFin;
        Fecha hora;
        Reserva r;
        int horaCompleta = 0;
        Fecha ahora = new Fecha();

        fecha = new Fecha(fecha.getFecha());
        horario = modeloHorarios.getHorario(fecha, tipoDeVehiculo);

        horaInicio = horario.getHoraInicio();
        horaFin = new Fecha(horario.getHoraFin().getTimeInMillis());

        hora = new Fecha(horaInicio.getTimeInMillis());

        ModeloReserva modeloR;
        modeloR = new ModeloReserva(conexiones);

        ArrayList<String[]> alerts = (ArrayList<String[]>) request.getAttribute("Mensaje");
        if (alerts != null) {
            for (String[] a : alerts) {
                modeloR.setAlert(a);
            }
        }

        listadoReservas = modeloR.getReservas(fecha, placa, tipoDeVehiculo);

        if (placa.equals("")) {

            int rNumber = 0;
            //-------------------------------------------------------------------------------
            //primero agregamos las reservas que pudieran estar antes de la hora de apertura
            int rowspan = 0;
            Reserva rRowspan = null;
            while (true) {
                if (listadoReservas.size() <= rNumber) {
                    break;
                }
                r = listadoReservas.get(rNumber);
                if (r.getHoraReserva().before(horaInicio)) {
                    r.getFechaReserva().add(r.getHoraReserva());
                    if (r.getFechaReserva().before(ahora) && !r.getFechaReserva().isInInterval(ahora)) {
                        r.setEnListaAccion(1);
                    } else {
                        r.setEnListaAccion(2);
                    }

                    listado.add(r);
                    rowspan++;
                    rNumber++;
                } else {
                    break;
                }

            }

            if (rowspan != 0) {
                listado.get(0).setEnListaBorde(rowspan);
                rowspan = 0;
            }

            //-------------------------------------------------------------------------------
            //luego incluimos las reservas dentro del horario del CDA
            hora.setInterval(horario.getIntervalo(hora));
            while (!hora.isInInterval(horaFin)) {
                boolean agregado = false;
                while (true) {
                    if (listadoReservas.size() <= rNumber) {
                        break;
                    }
                    r = listadoReservas.get(rNumber);
                    if (hora.isInInterval(r.getHoraReserva())) {//se debe ingrresar la reserva
                        //compara si es anterior o futura
                        r.getFechaReserva().add(r.getHoraReserva());
                        if (r.getFechaReserva().before(ahora) && !r.getFechaReserva().isInInterval(ahora)) {
                            r.setEnListaAccion(1);
                        } else {
                            r.setEnListaAccion(2);
                        }

                        //comprueba si es un cambio de hora para establecer el rowspan
                        if (horaCompleta != r.getHoraReserva().get(Calendar.HOUR_OF_DAY)) {
                            horaCompleta = r.getHoraReserva().get(Calendar.HOUR_OF_DAY);
                            if (rRowspan != null) {
                                rRowspan.setEnListaBorde(rowspan);
                                rowspan = 0;
                            }
                            rRowspan = r;
                        }

                        listado.add(r);
                        rowspan++;
                        agregado = true;
                        rNumber++;
                    } else {
                        break;
                    }
                }
                if (!agregado) {
                    r = new Reserva(0);
                    r.setHoraReserva(new Fecha(hora.getTimeInMillis()));
                    r.setFechaReserva(new Fecha(fecha.getTimeInMillis()));
                    r.getFechaReserva().add(hora);

                    //compara si es anterior o futura
                    if (r.getFechaReserva().before(ahora) && !r.getFechaReserva().isInInterval(ahora)) {
                        r.setEnListaAccion(0);
                    } else {
                        r.setEnListaAccion(3);
                    }

                    //comprueba si es un cambio de hora para establecer el colspan
                    if (horaCompleta != r.getHoraReserva().get(Calendar.HOUR_OF_DAY)) {
                        horaCompleta = r.getHoraReserva().get(Calendar.HOUR_OF_DAY);
                        if (rRowspan != null) {
                            rRowspan.setEnListaBorde(rowspan);
                            rowspan = 0;
                        }
                        rRowspan = r;
                    }

                    listado.add(r);
                    rowspan += 1;
                }
                if (rRowspan == null) {
                    rRowspan = listado.get(0);
                }

                hora.next();
                hora.setInterval(horario.getIntervalo(hora));

            }
            if (rRowspan != null) {
                rRowspan.setEnListaBorde(rowspan);
            }

            //-------------------------------------------------------------------------------
            //por ultimo incluye las reservas que puedan estar por fuera del horario de cierre
            rowspan = 0;
            rRowspan = null;
            while (true) {
                if (listadoReservas.size() <= rNumber) {
                    break;
                }
                r = listadoReservas.get(rNumber);
                if (r.getHoraReserva().after(horaFin)) {//se debe ingrresar la reserva
                    //compara si es anterior o futura
                    r.getFechaReserva().add(r.getHoraReserva());
                    if (r.getFechaReserva().before(ahora) && !r.getFechaReserva().isInInterval(ahora)) {
                        r.setEnListaAccion(1);
                    } else {
                        r.setEnListaAccion(2);
                    }
                    if (rRowspan == null) {
                        rRowspan = r;
                    }
                    listado.add(r);
                    rowspan++;
                    rNumber++;
                } else {
                    break;
                }
            }
            if (rowspan != 0) {
                rRowspan.setEnListaBorde(rowspan);
            }
        } else {//si el listado es de reservas de una placa
            for (Reserva r1 : listadoReservas) {
                //compara si es anterior o futura
                r1.getFechaReserva().add(r1.getHoraReserva());
                if (r1.getFechaReserva().before(ahora)) {
                    r1.setEnListaAccion(1);
                } else {
                    r1.setEnListaAccion(2);
                }
                listado.add(r1);
            }
        }

        request.setAttribute("Placa", placa);
        request.setAttribute("Horario", horario);
        request.setAttribute("Fecha", fecha);
        request.setAttribute("listado", listado);
        request.setAttribute("tipo", tipoDeVehiculo);
        request.setAttribute("Mensaje", modeloR.getAlerts());
        RequestDispatcher disp = request.getRequestDispatcher("/ListadoReservas.jsp");
        disp.forward(request, response);
    }

}
