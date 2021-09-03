package com.jp.appreservas;

import java.io.IOException;
import java.util.Map;
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
@WebServlet("/Admin")
public class ControladorAdmin extends HttpServlet {

    private static final long serialVersionUID = 1L;

    //definir el datasource
    @Resource(name = "jdbc/conecciones")
    private DataSource conexiones;
    private Map<String, String[]> Parametros;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorAdmin() {
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
        System.out.println("-----------------         GESTION         --------------------");
        System.out.println("-----------------parametros de la peticion--------------------");
        for (String clave : Parametros.keySet()) {
            System.out.println("[" + clave + " : " + Parametros.get(clave)[0] + "]");
        }
        System.out.println("[Metodo de la peticion:" + Metodo + "]");
        System.out.println("-----------------------end parametros-------------------------");

    }
}
