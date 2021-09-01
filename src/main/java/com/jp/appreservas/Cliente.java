package com.jp.appreservas;

public class Cliente {
	
	public Cliente(int tipoDocId, String doc, String nombres, String celular) {
		this.tipoDocId = tipoDocId;
		this.doc = doc;
		this.nombres = nombres;
		this.celular = celular;
	}
	
	public Cliente() {
		
	}

	public int getId() {
		return id;
	}
	public int getTipoDocId() {
		return tipoDocId;
	}
	public String getTipoDoc() {
		return tipoDoc;
	}
	public String getDoc() {
		return doc;
	}
	public String getNombres() {
		return nombres;
	}
	public String getCelular() {
		return celular;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public void setTipoDocId(int tipoDocId) {
		this.tipoDocId = tipoDocId;
	}
	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	@Override
	public String toString() {
		return "Cliente [id=" + id + ", tipoDocId=" + tipoDocId + ", tipoDoc=" + tipoDoc + ", doc=" + doc + ", nombres="
				+ nombres + ", celular=" + celular + "]";
	}
	private int id;
	private int tipoDocId;
	private String tipoDoc;
	private String doc;
	private String nombres;
	private String celular;
}
