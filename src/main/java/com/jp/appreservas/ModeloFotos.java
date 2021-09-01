package com.jp.appreservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

public class ModeloFotos {

	public ModeloFotos(DataSource conexiones) {
		this.conexiones = conexiones;
	}
	
	private DataSource conexiones;
	private Connection conexion;

	public ArrayList<Ordenes> getFotosList(Fecha fecha) {
		// TODO Auto-generated method stub
		String sql="";
		PreparedStatement st=null;
		ResultSet rs=null;
		ArrayList<Ordenes> listado=new ArrayList<Ordenes>();
		String año=String.valueOf(fecha.getAnio());
		String mes;
		if((fecha.getMes()+1)<10) {
			mes="0" + String.valueOf(fecha.getMes()+1);
		}
		else{
			mes=String.valueOf(fecha.getMes()+1);
		}
		String añomes=año + mes;
			try {
				conexion=conexiones.getConnection();
					sql="select o.ot_id, o.ot_fecharegistro, v.veh_placa, v.veh_codificacion, o.ot_resultado, o.ot_tiporevision, i.ima_id from ordenes_trabajo" + añomes + " as o \r\n" + 
							"join vehiculo v on o.veh_id=v.veh_id \r\n" +  
							"join imagen" + añomes + " as i on i.ot_id=o.ot_id \r\n" + 
							" where o.ot_status=4 and DATE(o.ot_fecharegistro)=Date(?) and v.veh_tipo='LIVIANO'";
				//System.out.println(sql);
				st=conexion.prepareStatement(sql);
				st.setDate(1, fecha.getDateSql());
				rs=st.executeQuery();
				rs.last();
				listado=new ArrayList<Ordenes>(rs.getRow());
				rs.beforeFirst();
				//recorre el resultset
				while(rs.next()) {
					Ordenes o=new Ordenes(rs.getInt("ot_id"));
					o.setPlaca(rs.getString("veh_placa"));
					o.setNumRevision(rs.getInt("ot_tiporevision"));
					if(rs.getInt("ot_resultado")==1) {
						o.setResultado("Aprobado");
					}
					else {
						o.setResultado("Reprobado");
					}
					o.setIdFoto1(rs.getInt("ima_id"));
					o.setFecha(new Fecha(rs.getTimestamp("ot_fecharegistro").getTime()));
					listado.add(o);
				}
				

				conexion.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			return listado;
			
				
	}
	

	public ArrayList<Ordenes> getFotosList(String placa) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getImage(int a, int b, Fecha fecha) {
		// TODO Auto-generated method stub
		
		String sql="";
		PreparedStatement st=null;
		ResultSet rs=null;
		byte[] imgData=null;
		String año=String.valueOf(fecha.getAnio());
		String mes;
		if((fecha.getMes()+1)<10) {
			mes="0" + String.valueOf(fecha.getMes()+1);
		}
		else{
			mes=String.valueOf(fecha.getMes()+1);
		}
		String codificacion=año + mes;
		
		if(b==1) {
			sql="select i.ima_imagen imagenData from imagen"+codificacion+" i where i.ima_id=?";
		}
		else {
			sql="select i.ima2_imagen imagenData from imagen2"+ codificacion +" i where i.ima2_id=?";
		}
		try {
			conexion=conexiones.getConnection();	
			st=conexion.prepareStatement(sql);
			st.setInt(1, a);
			rs=st.executeQuery();
			if (rs.next()) { 
			      imgData = rs.getBytes("imagenData");
			}
			//System.out.println(a + " " + b);
			//System.out.println(imgData);
			conexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			return imgData;

		
	}



}
