package com.jp.appreservas;

public class Ordenes {

	public Ordenes(int ot) {
		super();
		this.ot = ot;
	}
	public Ordenes(int ot, int idFoto1, int idFoto2, String placa, int resultado, int numRevision) {
		super();
		this.ot = ot;
		this.idFoto1 = idFoto1;
		this.idFoto2 = idFoto2;
		this.placa = placa;
		this.resultadoInt = resultado;
		this.numRevision = numRevision;
	}
	
	
	
	public int getOt() {
		return ot;
	}
	public int getIdFoto1() {
		return idFoto1;
	}
	public int getIdFoto2() {
		return idFoto2;
	}
	public String getPlaca() {
		return placa;
	}
	public int getResultado() {
		return resultadoInt;
	}
	public String getResultadoInt() {
		return resultado;
	}
	public int getNumRevision() {
		return numRevision;
	}
	public void setOt(int ot) {
		this.ot = ot;
	}
	public void setIdFoto1(int idFoto1) {
		this.idFoto1 = idFoto1;
	}
	public void setIdFoto2(int idFoto2) {
		this.idFoto2 = idFoto2;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public void setResultadoInt(int resultado) {
		this.resultadoInt = resultado;
	}
	public void setNumRevision(int numRevision) {
		this.numRevision = numRevision;
	}


	@Override
	public String toString() {
		return "Ordenes [ot=" + ot + ", idFoto1=" + idFoto1 + ", idFoto2=" + idFoto2 + ", placa=" + placa
				+ ", resultado=" + resultado + ", numRevision=" + numRevision + "]";
	}


	private int ot;
	private int idFoto1;
	private int idFoto2;
	private String placa;
	private String resultado;
	private int resultadoInt;
	private int numRevision;
	private Fecha fecha;
	public Fecha getFecha() {
		return fecha;
	}
	public void setFecha(Fecha fecha) {
		this.fecha = fecha;
	}
}
