package com.jp.appreservas;

public class Reserva{
	
	public Reserva(Fecha fecha, Fecha hora, String placa, int usuarioRegistraId, int tipoDeRevisionId, int tipoDeVehiculoId) {
		FechaReserva = fecha;
		HoraReserva = hora;
		Placa = placa;
		UsuarioRegistraId = usuarioRegistraId;
		TipoDeRevisionId =tipoDeRevisionId;
		TipoDeVehiculoId=tipoDeVehiculoId;
		
	}
	public Reserva(int id) {
		this.Id=id;
	}

	

	public int getId() {
		return Id;
	}
	public Fecha getFechaRegistado() {
		return FechaRegistado;
	}
	public Fecha getFechaActualizado() {
		return FechaActualizado;
	}
	public Fecha getFechaReserva() {
		return FechaReserva;
	}
	public Fecha getHoraReserva() {
		return HoraReserva;
	}
	public String getPlaca() {
		return Placa;
	}
		public int getUsuarioRegistraId() {
		return UsuarioRegistraId;
	}
	public int getTipoDeRevisionId() {
		return TipoDeRevisionId;
	}
	public int getTipoDeVehiculoId() {
		return TipoDeVehiculoId;
	}
	public String getTipoDeRevision() {
		return TipoDeRevision;
	}
	public String getTipoDeVehiculo() {
		return TipoDeVehiculo;
	}
	public int getIngresado() {
		return Ingresado;
	}
	public void setId(int id) {
		Id = id;
	}
	public void setFechaRegistado(Fecha fechaRegistado) {
		FechaRegistado = fechaRegistado;
	}
	public void setFechaActualizado(Fecha fechaActualizado) {
		FechaActualizado = fechaActualizado;
	}
	public void setFechaReserva(Fecha fechaReserva) {
		FechaReserva = fechaReserva;
	}
	public void setHoraReserva(Fecha horaReserva) {
		HoraReserva = horaReserva;
	}
	public void setPlaca(String placa) {
		Placa = placa;
	}
	public void setUsuarioRegistraId(int usuarioRegistraId) {
		UsuarioRegistraId = usuarioRegistraId;
	}
	public void setTipoDeRevisionId(int tipoDeRevisionId) {
		TipoDeRevisionId = tipoDeRevisionId;
	}
	public void setTipoDeVehiculoId(int tipoDeVehiculoId) {
		TipoDeVehiculoId = tipoDeVehiculoId;
	}
	public void setTipoDeRevision(String tipoDeRevision) {
		TipoDeRevision = tipoDeRevision;
	}
	public void setTipoDeVehiculo(String tipoDeVehiculo) {
		TipoDeVehiculo = tipoDeVehiculo;
	}
	public void setIngresado(int ingresado) {
		Ingresado = ingresado;
	}
	public Cliente getConductor() {
		return Conductor;
	}
	public void setPropietario(Cliente propietario) {
		this.Propietario = propietario;
	}
	public Cliente getPropietario() {
		return Propietario;
	}
	public void setConductor(Cliente conductor) {
		this.Conductor = conductor;
	}
	
	public int getConductorId() {
		return ConductorId;
	}
	public void setConductorId(int conductorId) {
		ConductorId = conductorId;
	}
	public int getPropietarioId() {
		return PropietarioId;
	}
	public void setPropietarioId(int propietarioId) {
		PropietarioId = propietarioId;
	}

	public int getEsActiva() {
		return EsActiva;
	}
	public void setEsActiva(int esActiva) {
		EsActiva = esActiva;
	}
	public int getEnListaBorde() {
		return EnListaBorde;
	}
	public void setEnListaBorde(int estadoEnLista) {
		this.EnListaBorde = estadoEnLista;
	}
	public int getEnListaAccion() {
		return EnListaAccion;
	}
	public void setEnListaAccion(int enListaAccion) {
		EnListaAccion = enListaAccion;
	}
	@Override
	public String toString() {
		return "Reserva [Id=" + Id + ", FechaRegistado=" + FechaRegistado + ", FechaActualizado=" + FechaActualizado
				+ ", FechaReserva=" + FechaReserva + ", HoraReserva=" + HoraReserva + ", Placa=" + Placa
				+ ", PropietarioId=" + PropietarioId + ", ConductorId=" + ConductorId + ", UsuarioRegistraId="
				+ UsuarioRegistraId + ", TipoDeRevisionId=" + TipoDeRevisionId + ", TipoDeVehiculoId="
				+ TipoDeVehiculoId + ", TipoDeRevision=" + TipoDeRevision + ", TipoDeVehiculo=" + TipoDeVehiculo
				+ ", Conductor=" + Conductor + ", Propietario=" + Propietario + ", EsActiva=" + EsActiva
				+ ", Ingresado=" + Ingresado + ", EnListaBorde=" + EnListaBorde + ", EnListaAccion=" + EnListaAccion
				+ "]";
	}

	private int Id;// INT(11) NOT NULL AUTO_INCREMENT,
	private Fecha FechaRegistado;// TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	private Fecha FechaActualizado;// TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	private Fecha FechaReserva;// DATE NOT NULL,
	private Fecha HoraReserva;// TIME NOT NULL,
	private String Placa;// VARCHAR(6) NOT NULL,
	private int PropietarioId;// INT(11) NULL DEFAULT NULL,
	private int ConductorId;// INT(11) NULL DEFAULT NULL,
	private int UsuarioRegistraId;// INT(11) NULL DEFAULT NULL
	private int TipoDeRevisionId;
	private int TipoDeVehiculoId;
	private String TipoDeRevision;
	private String TipoDeVehiculo;
	private Cliente Conductor;
	private Cliente Propietario;
	private int EsActiva=1;
	private int Ingresado=0;
	private int EnListaBorde=0;//establece la manera de mostrarla en el listado, solo en la vista
	private int EnListaAccion=0;//establece la manera de mostrarla en el listado, solo en la vista
	
}
