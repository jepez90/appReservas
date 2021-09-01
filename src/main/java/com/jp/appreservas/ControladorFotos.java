package com.jp.appreservas;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
@WebServlet("/Fotos")
public class ControladorFotos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	//definir el datasource
	@Resource(name="jdbc/conecciones")
	private DataSource conexiones;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorFotos() {
        super();
        
        
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doGet(request, response);
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doPost(request, response);

		Map<String, String[]> Parametros;
		Parametros =request.getParameterMap();
		
		//se validan los parametros
		Fecha fecha= new Fecha();
		String placa ="";
		
		if(Parametros.containsKey("Placa")) {
			placa=Parametros.get("Placa")[0].toUpperCase();
		}
		if(Parametros.containsKey("Fecha")) {
			fecha=new Fecha(Parametros.get("Fecha")[0]);
		}
		
		if(Parametros.containsKey("a")) {
			int idImagen=Integer.valueOf(Parametros.get("a")[0]);

			if(Parametros.containsKey("b")) {
				int tipoFoto=Integer.valueOf(Parametros.get("b")[0]);
				//es una peticcion para cargar una imagen

				ModeloFotos modelo= new ModeloFotos(conexiones);
				byte[] imgData = modelo.getImage(idImagen,tipoFoto, fecha);
				  
				      response.setHeader("expires", "0"); 
				      response.setContentType("image/jpg"); 

				      OutputStream os = response.getOutputStream(); // output with the help of outputStream 
				      os.write(imgData); 
				      os.flush(); 
				      os.close(); 
				      return;
	
			}
		}
		
//		System.out.println("-----------------parametros de la peticion--------------------");
//		for(String clave:Parametros.keySet()) {
//			System.out.println("["+clave + " : " + Parametros.get(clave)[0]+"]");
//		}
//		System.out.println("-----------------end parametros--------------------");
		
		ArrayList<Ordenes> listado=new ArrayList<Ordenes>();
		
		ModeloFotos modelo= new ModeloFotos(conexiones);
		if(placa=="") {

			listado=modelo.getFotosList(fecha);
		}
		else {


			listado=modelo.getFotosList(fecha);

		}
		
		request.setAttribute("listado",listado);
		request.setAttribute("fecha",fecha);
		request.setAttribute("placa",placa);
		RequestDispatcher disp=request.getRequestDispatcher("/ListadoFotos.jsp");
		disp.forward(request, response);
	}
	

	
}


 