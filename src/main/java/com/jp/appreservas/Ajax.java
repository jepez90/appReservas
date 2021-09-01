package com.jp.appreservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;

public class Ajax {
	private DataSource conexiones;

	public Ajax(DataSource conexiones) {
		this.conexiones = conexiones;
	}
	
	public ArrayList<Object[]> getPlacas(String placa, int tipo){

		Connection conexion;
		PreparedStatement st;
		ResultSet rs;
		String sql;	
		ArrayList<Object[]> lista=new ArrayList<Object[]>();
		int id;
		
		try {
			conexion=conexiones.getConnection();
			if(tipo==0) {//es una busqueda por id
				try {
					id=Integer.parseInt(placa);
				}catch(NumberFormatException e) {
					e.printStackTrace();
					id=0;
				}
				sql="SELECT * FROM reservas R JOIN CLIENTES P ON R.PropietarioId=P.Id JOIN CLIENTES C ON R.ConductorId=C.Id"
						+ " WHERE R.Id = ? ";
				st=conexion.prepareStatement(sql);
				st.setInt(1, id);
			}
			else {//es una busuqueda por placa
				System.out.println("tipo: " + tipo + " - Placa: "+placa.toUpperCase() );
				sql="SELECT * FROM reservas R JOIN CLIENTES P ON R.PropietarioId=P.Id JOIN CLIENTES C ON R.ConductorId=C.Id " + 
						" WHERE R.TipoDeVehiculoId=? AND R.Id IN (SELECT MAX(W.id) FROM reservas W WHERE W.Placa LIKE ? GROUP BY W.placa) LIMIT 10";
				st=conexion.prepareStatement(sql);
				st.setInt(1, tipo);
				st.setString(2,"%"+placa.toUpperCase()+"%");
			}
			
			st.execute();			
			rs=st.getResultSet();
			while(rs.next()) {
				Object[] datos=new Object[6];
				datos[0]=rs.getInt("R.Id");
				datos[1]=rs.getString("R.Placa");
				datos[2]=new Object[] {rs.getInt("P.Id"),rs.getString("P.Doc"),rs.getString("P.Nombres"),rs.getString("P.Celular")};
				datos[3]=new Object[] {rs.getInt("C.Id"),rs.getString("C.Doc"),rs.getString("C.Nombres"),rs.getString("C.Celular")};
				datos[4]=rs.getString("R.HoraReserva");
				datos[5]=rs.getString("R.TipoDeRevisionId");
				lista.add(datos);
			}
			conexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lista;
	}
	public ArrayList<Object[]> getCedulas(String cedula){
		//ptimero busca enla base de datos propia
				Connection conexion;
				PreparedStatement st;
				ResultSet rs;
				String sql;	
				ArrayList<Object[]> lista=new ArrayList<Object[]>();
				
				try {
					conexion=conexiones.getConnection();
					sql="SELECT * FROM clientes C WHERE C.Doc LIKE ? OR C.Nombres LIKE ? LIMIT 10";
					st=conexion.prepareStatement(sql);
					st.setString(1,"%" + cedula.toUpperCase()+"%");
					st.setString(2,"%"+cedula.toUpperCase()+"%");
					st.execute();			
					rs=st.getResultSet();
					while(rs.next()) {
						Object[] datos=new Object[] {rs.getInt("Id"),rs.getString("Doc"),rs.getString("Nombres"),rs.getString("Celular")};
						lista.add(datos);
					}

					conexion.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return lista;
	}
	

	public String getJSONString(ArrayList<Object[]> Data) {
				
		String resultado="{\"Longitud\":";
		
		if (Data==null || Data.size()==0) {
			return resultado+="0}";
		}else {
			resultado+=Data.size();
			resultado+=",\"Data\":";
		}
		String DatosString="[";
		
		for(Object[] s:Data) {//itera para cada objeto de la lista linea
			
			if(!DatosString.equals("[")) {
				DatosString+=",";
			}

			DatosString+=arrayToString(s);


		}
		resultado+=DatosString+"]}";
		//return "{}";
		return resultado;
		
		
//			Formato
//			{"Longitud": 1',
//				"Data":[
//					["ss",3,"sdvs".....],
//					["ss",3,"sdvs".....],
//					...
//				]
//			}
	}

	private String arrayToString(Object[] arr) {

		String Linea="[";
		for (Object o:arr) {//itera para cada elemento del objeto

			if(!Linea.equals("[")) {
				Linea+=",";
			}
			if(o.getClass()==Integer.class) {
				Linea+=o;
			}
			else if(o.getClass()==Object[].class) {
				Linea+=arrayToString((Object[])o);
			}
			else {
				Linea+="\""+o+"\"";
			}
		}

		Linea+="]";
		return Linea;
		
	}

	

}
