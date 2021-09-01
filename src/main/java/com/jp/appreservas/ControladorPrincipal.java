package com.jp.appreservas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class Controlador
 */
//agrega reservas sin hacer login
@WebServlet("/Reservas")
public class ControladorPrincipal extends HttpServlet {

    private static final long serialVersionUID = 1L;

    //definir el datasource
    @Resource(name = "jdbc/conecciones")
    private DataSource conexiones;
    private Map<String, String[]> Parametros;
    private ArrayList<String[]> Alertas = new ArrayList<String[]>();
    private Reserva reserva = new Reserva(0);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorPrincipal() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        //comprobamos inicio de sesion

        String Metodo = request.getMethod();
        Parametros = request.getParameterMap();
        System.out.println("-----------------parametros de la peticion--------------------");
        for (String clave : Parametros.keySet()) {
            System.out.println("[" + clave + " : " + Parametros.get(clave)[0] + "]");
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
        //Validamos los parametros ingresados
        if (Metodo.equalsIgnoreCase("POST")) {
            validaDatos();
        } else {
            Cliente clientevacio = new Cliente();
            clientevacio.setDoc("");
            clientevacio.setNombres("");
            clientevacio.setCelular("");

            reserva.setPlaca("");
            reserva.setPropietario(clientevacio);
            reserva.setConductor(clientevacio);
            reserva.setTipoDeRevisionId(0);
            reserva.setTipoDeVehiculoId(0);
        }

        request.setAttribute("Alertas", Alertas);
        request.setAttribute("Reserva", reserva);

        RequestDispatcher disp = request.getRequestDispatcher("/VistaNuevaReservaSinLogin.jsp");
        disp.forward(request, response);

    }

    private boolean validaDatos() {
        String[] parametrosEsperados = new String[]{
            "Tipo",
            "FechaNueva",
            "Hora",
            "PlacaNueva",
            "TipoRevision",
            "Cedula",
            "Nombre",
            "Celular",
            "CedulaC",
            "NombreC",
            "CelularC"};
        String[] RegExpParametros = new String[]{
            "",
            "[2][0][0-3][0-9]-[0-1][0-9]-[0-3][0-9]",
            "[0-2][0-9]:[0-5][0-9]:[0-5][0-9]",
            "",
            "",
            "\\d{5,10}",
            "\\D{6,100}",
            "\\d{10}",
            "\\d{5,10}",
            "\\D{6,100}",
            "\\d{10}"};

        boolean existenerrores = false;
        Cliente propietario = new Cliente();
        Cliente conductor = new Cliente();

        //primero se verifica sy los parametros estan completso
        for (int i = 0; i < parametrosEsperados.length; i++) {
            System.out.println(i);
            String p = parametrosEsperados[i];
            if (!Parametros.containsKey(p)) {
                Alertas.add(new String[]{"Error", "Parametros incompletos o inconsistentes para esta peticion: " + p});
                existenerrores = true;
            } else {
                String Parametro = Parametros.get(p)[0];
                if (Parametro.equals("")) {
                    if (!p.substring(p.length() - 1).equalsIgnoreCase("C")) {
                        Alertas.add(new String[]{"Error", "Parametros incompletos o inconsistentes para esta peticion 2: " + p});
                        existenerrores = true;
                    }
                } else {
                    if (RegExpParametros[i] != "") {
                        if (!Pattern.matches(RegExpParametros[i], Parametro)) {
                            Alertas.add(new String[]{"Error", "Parametros incompletos o inconsistentes para esta peticion 3: " + p});
                            existenerrores = true;
                        }
                    }
                }
            }
        }

        if (Parametros.containsKey("Tipo")) {
            reserva.setTipoDeVehiculo(Parametros.get("Tipo")[0]);
            if (reserva.getTipoDeVehiculo().equalsIgnoreCase("Motocicleta")) {
                reserva.setTipoDeVehiculoId(1);
            } else if (reserva.getTipoDeVehiculo().equalsIgnoreCase("Liviano")) {
                reserva.setTipoDeVehiculoId(2);
            } else {
                reserva.setTipoDeVehiculoId(0);
                Alertas.add(new String[]{"Error", "Parametros incompletos o inconsistentes para esta peticion 3: Tipo"});
                existenerrores = true;
            }
        }

        if (Parametros.containsKey("FechaNueva")) {
            reserva.setFechaReserva(new Fecha(Parametros.get("FechaNueva")[0]));
        }

        if (Parametros.containsKey("Hora")) {
            reserva.setHoraReserva(new Fecha(Parametros.get("Hora")[0]));
        }

        if (Parametros.containsKey("PlacaNueva")) {
            reserva.setPlaca(Parametros.get("PlacaNueva")[0]);
            if (reserva.getTipoDeVehiculoId() == 1) {
                //valida el patron de placa para la moto
                if (!Pattern.matches("([A-Za-z]{3}[0-9]{2}|[A-Za-z]{3}[0-9]{2}[A-Za-z]{1})", reserva.getPlaca())) {
                    Alertas.add(new String[]{"Error", "Parametros incompletos o inconsistentes para esta peticion 3: " + reserva.getPlaca()});
                    existenerrores = true;
                }
            } else {
                //valida el patron de placa para el carro
                if (!Pattern.matches("[A-Za-z]{3}[0-9]{3}", reserva.getPlaca())) {
                    Alertas.add(new String[]{"Error", "Parametros incompletos o inconsistentes para esta peticion 3: " + reserva.getPlaca()});
                    existenerrores = true;
                }
            }

        }

        if (Parametros.containsKey("TipoRevision")) {
            reserva.setTipoDeRevision(Parametros.get("TipoRevision")[0]);
            if (reserva.getTipoDeRevision().equalsIgnoreCase("Inspeccion")) {
                reserva.setTipoDeRevisionId(1);
            } else if (reserva.getTipoDeRevision().equalsIgnoreCase("Reinspeccion")) {
                reserva.setTipoDeRevisionId(2);
            } else if (reserva.getTipoDeRevision().equalsIgnoreCase("Preventiva")) {
                reserva.setTipoDeRevisionId(3);
            } else {
                reserva.setTipoDeRevisionId(0);
                Alertas.add(new String[]{"Error", "Parametros incompletos o inconsistentes para esta peticion 3: TipoRevision"});
                existenerrores = true;
            }

        }

        //datos del cliente
        //valida cedula
        if (Parametros.containsKey("Cedula")) {
            propietario.setDoc(Parametros.get("Cedula")[0]);
        }

        //nombre
        if (Parametros.containsKey("Nombre")) {
            propietario.setNombres(Parametros.get("Nombre")[0]);
        }

        //celular
        if (Parametros.containsKey("Celular")) {
            propietario.setCelular(Parametros.get("Celular")[0]);
        }

        //datos de conductor si existem
        if (Parametros.containsKey("EsMismoConductor")) {
            conductor = propietario;
        } else {
            //valida cedula
            if (Parametros.containsKey("CedulaC")) {
                conductor.setDoc(Parametros.get("CedulaC")[0]);
            }

            //nombre
            if (Parametros.containsKey("NombreC")) {
                conductor.setNombres(Parametros.get("NombreC")[0]);
            }

            //celular
            if (Parametros.containsKey("CelularC")) {
                conductor.setCelular(Parametros.get("CelularC")[0]);
            }
        }

        reserva.setPropietario(propietario);
        reserva.setConductor(conductor);

        return existenerrores;
    }
}
