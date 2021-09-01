package com.jp.appreservas;

import java.util.Calendar;

public class Horario {
	
public Horario(Fecha fecha, Fecha horaInicio, Fecha horaFin) {
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;

	}
public int getId() {
	return id;
}
public Fecha getFecha() {
	return fecha;
}
public Fecha getHoraInicio() {
	return horaInicio;
}
public Fecha getHoraFin() {
	return horaFin;
}
public int getDiaEnSemana() {
	return diaEnSemana;
}
public boolean isPorDefecto() {
	return porDefecto;
}
public String getComentario() {
	return Comentario;
}
public void setId(int id) {
	this.id = id;
}
public void setFecha(Fecha fecha) {
	this.fecha = fecha;
}
public void setHoraInicio(Fecha horaInicio) {
	this.horaInicio = horaInicio;
}
public void setHoraFin(Fecha horaFin) {
	this.horaFin = horaFin;
}
public void setDiaEnSemana(int diaEnSemana) {
	this.diaEnSemana = diaEnSemana;
}
public void setPorDefecto(boolean porDefecto) {
	this.porDefecto = porDefecto;
}
public void setComentario(String comentario) {
	Comentario = comentario;
}
public void setIntervalos(byte[][] intervalos) {
	this.intervalos = intervalos;
}
public int getIntervalo(Fecha hora){
	if (hora.get(Calendar.HOUR_OF_DAY)>=intervalos.length) {
		return 10;
	}
		return intervalos[hora.get(Calendar.HOUR_OF_DAY)][hora.get(Calendar.MINUTE)];

}
@Override
	public String toString() {
		return "Horario [id=" + id + ", fecha=" + fecha + ", horaInicio=" + horaInicio + ", horaFin=" + horaFin
				+ ", diaEnSemana=" + diaEnSemana + ", porDefecto=" + porDefecto + ", Comentario=" + Comentario + "]";
	}
private int id;
private Fecha fecha;
private Fecha horaInicio;
private Fecha horaFin;
private int diaEnSemana;
private boolean porDefecto;
private String Comentario;
private byte[][] intervalos;
}
