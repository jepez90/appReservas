package com.jp.appreservas;

public class Usuario {
	
	public Usuario() {
	}
	public Usuario(String userName) {
		this.userName = userName;
	}
	public Usuario(int id, String userName) {
		this.id = id;
		this.userName = userName;
	}
	public int getId() {
		return id;
	}
	public String getUserName() {
		return userName;
	}
	public long getCedula() {
		return cedula;
	}
	public String getNombre() {
		return nombre;
	}
	public String getPass() {
		return pass;
	}
	public String getOldPass() {
		return oldPass;
	}
	public Fecha getUltimoCambo() {
		return ultimoCambo;
	}
	public int getIsActivo() {
		return isActivo;
	}
	public int getIsAdmin() {
		return isAdmin;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setCedula(long cedula) {
		this.cedula = cedula;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}
	public void setUltimoCambo(Fecha ultimoCambo) {
		this.ultimoCambo = ultimoCambo;
	}
	public void setIsActivo(int isActivo) {
		this.isActivo = isActivo;
	}
	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Fecha getLoggedAt() {
		return loggedAt;
	}
	public void setLoggedAt(Fecha loggedAt) {
		this.loggedAt = loggedAt;
	}

	public int getCreadorId() {
		return CreadorId;
	}
	public void setCreadorId(int creadorId) {
		CreadorId = creadorId;
	}
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", userName=" + userName + ", cedula=" + cedula + ", nombre=" + nombre + ", pass="
				+ pass + ", oldPass=" + oldPass + ", ultimoCambo=" + ultimoCambo + ", isActivo=" + isActivo
				+ ", isAdmin=" + isAdmin + ", CreadorId=" + CreadorId + ", loggedAt=" + loggedAt + "]";
	}
	private int id=0;
	private String userName;
	private long cedula;
	private String nombre;
	private String pass;
	private String oldPass;
	private Fecha ultimoCambo;
	private int isActivo=0;
	private int isAdmin=0;
	private int CreadorId;
	private Fecha loggedAt;

	public static int MinCaracteresForU=4;
	public static int MinCaracteresForP=6;
}
