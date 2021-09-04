package com.jp.appreservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

public class ModeloReserva {

    public ModeloReserva(DataSource conexiones) {
        this.conexiones = conexiones;
    }

    public List<Reserva> getReservas(Fecha fecha, String placa, int tipo) {

        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";

        //ejecuta la busqueda
        try {
            conexion = conexiones.getConnection();
            if (placa.equals("")) {
                sql = "SELECT * FROM reservas R JOIN TipoDeRevision TR ON TR.Id=R.TipoDeRevisionId "
                        + "JOIN TipoDeVehiculo TV ON TV.Id=R.TipoDeVehiculoId "
                        + " WHERE R.Activa=1 AND R.FechaReserva= ? AND R.TipoDeVehiculoId=? ORDER BY R.FechaReserva, R.HoraReserva  ";
                st = conexion.prepareStatement(sql);
                st.setDate(1, fecha.getDateSql());
                st.setInt(2, tipo);

            } else {
                sql = "SELECT * FROM reservas R JOIN TipoDeRevision TR ON TR.Id=R.TipoDeRevisionId "
                        + "JOIN TipoDeVehiculo TV ON TV.Id=R.TipoDeVehiculoId "
                        + "WHERE R.Activa=1 AND R.Placa= ? ORDER BY R.FechaReserva, R.HoraReserva ";
                st = conexion.prepareStatement(sql);
                st.setString(1, placa);
            }
            rs = st.executeQuery();
            //declaro el arraylist con el tama�o del resultset
            rs.last();
            ArrayList<Reserva> listado = new ArrayList<>(rs.getRow());
            rs.beforeFirst();
            //recorre el resultset
            while (rs.next()) {
                Reserva r = new Reserva(rs.getInt("Id"));
                r.setFechaRegistado(new Fecha(rs.getTimestamp("FechaRegistrado").getTime()));
                r.setFechaActualizado(new Fecha(rs.getTimestamp("FechaActualizado").getTime()));
                r.setFechaReserva(new Fecha(rs.getTimestamp("FechaReserva").getTime()));
                r.setHoraReserva(new Fecha(rs.getTime("HoraReserva").getTime()));
                r.setPlaca(rs.getString("Placa"));
                r.setIngresado(rs.getInt("Ingresado"));
                r.setPropietarioId(rs.getInt("PropietarioId"));
                r.setConductorId(rs.getInt("ConductorId"));
                r.setUsuarioRegistraId(rs.getInt("UsuarioRegistraId"));
                r.setTipoDeRevisionId(rs.getInt("TipoDeRevisionId"));
                r.setTipoDeVehiculoId(rs.getInt("TipoDeVehiculoId"));
                r.setTipoDeRevision(rs.getString("TipoDeRevision"));
                r.setTipoDeVehiculo(rs.getString("TipoDeVehiculo"));
                r.setEsActiva(rs.getInt("Activa"));

                Cliente c = getCliente(r.getPropietarioId());
                r.setPropietario(c);
                if (r.getPropietarioId() == r.getConductorId()) {
                    r.setConductor(c);
                } else {
                    c = getCliente(r.getConductorId());
                    r.setConductor(c);
                }
                listado.add(r);

            }
            conexion.close();
            return listado;
        } catch (SQLException e) {
            e.printStackTrace();
            this.setAlert(new String[]{"Error", e.getMessage()});
            return null;
        }

    }

    public Cliente getCliente(int Id) {

        PreparedStatement st;
        ResultSet rs;
        String sql;
        Cliente c;

        if (Id == 0) {
            return null;
        }
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = conexiones.getConnection();
            }

            sql = "SELECT * FROM clientes C JOIN TipoDoc TD ON C.TipoDocId=TD.Id WHERE C.Id=? ";
            st = conexion.prepareStatement(sql);
            st.setInt(1, Id);
            st.execute();

            rs = st.getResultSet();
            if (rs.next()) {

                c = new Cliente(rs.getInt("TipoDocId"), rs.getString("Doc"), rs.getString("Nombres"), rs.getString("Celular"));
                c.setId(rs.getInt("C.Id"));
                c.setTipoDoc(rs.getString("Sigla"));

                return c;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            this.setAlert(new String[]{"Error", e.getMessage()});
            return null;

        }

    }

    public void saveToDTB(Reserva r) {

        Connection conexion = null;
        PreparedStatement st;
        ResultSet rs;
        String sqlInsertReserva;
        String sqlInsertCliente;
        String sqlUpdateCliente;
        String sqlObtieneIdCliente;
        String sqlUpdateReserva;

        sqlInsertReserva = "INSERT INTO RESERVAS ( FechaReserva, HoraReserva, Placa, PropietarioId ,ConductorId, TipoDeRevisionId,TipoDeVehiculoId, UsuarioRegistraId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        sqlUpdateReserva = "UPDATE RESERVAS R SET R.FechaReserva=?,R.HoraReserva=?, R.PropietarioId=?, R.ConductorId=?,"
                + "R.UsuarioRegistraId=?,R.TipoDeRevisionId=?,R.Ingresado=?,R.UsuarioRegistraId=? WHERE R.Id=?";
        sqlInsertCliente = "INSERT INTO CLIENTES (TipoDocId, Doc, Nombres, Celular) VALUES(?, ?, ?, ?)";
        sqlObtieneIdCliente = "SELECT last_insert_id() AS Id";
        sqlUpdateCliente = "UPDATE CLIENTES SET TipoDocId=?, Doc=?, Nombres=?, Celular=? WHERE Id=?";

        String[] dias = new String[]{"", "Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sábado"};
        String[] meses = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        try {
            conexion = conexiones.getConnection();
            conexion.setAutoCommit(false);
            //inserta propietario si no existe
            if (r.getEsActiva() == 1) {
                if (r.getPropietario().getId() == 0) {
                    st = conexion.prepareStatement(sqlInsertCliente);
                    st.setInt(1, r.getPropietario().getTipoDocId());
                    st.setString(2, r.getPropietario().getDoc());
                    st.setString(3, r.getPropietario().getNombres());
                    st.setString(4, r.getPropietario().getCelular());
                    st.executeUpdate();
                    rs = st.executeQuery(sqlObtieneIdCliente);
                    if (rs.next()) {
                        if (rs.getInt("Id") != 0) {
                            r.setPropietarioId(rs.getInt("Id"));
                        } else {
                            setAlert(new String[]{"Error", "No se pudo obtener el id del Propietario Registrado, intentelo de nuevo"});
                            return;
                        }
                    }
                } //Actualiza el propietario si ya existe
                else {
                    st = conexion.prepareStatement(sqlUpdateCliente);
                    st.setInt(1, r.getPropietario().getTipoDocId());
                    st.setString(2, r.getPropietario().getDoc());
                    st.setString(3, r.getPropietario().getNombres());
                    st.setString(4, r.getPropietario().getCelular());
                    st.setInt(5, r.getPropietario().getId());
                    st.executeUpdate();

                }
                if (r.getConductor() == null) {//es el mismo propietario
                    r.setConductorId(r.getPropietarioId());
                } else {//se debe insertar o actualizar el conductor				
                    //inserta conductor si no existe
                    if (r.getConductor().getId() == 0) {
                        st = conexion.prepareStatement(sqlInsertCliente);
                        st.setInt(1, r.getConductor().getTipoDocId());
                        st.setString(2, r.getConductor().getDoc());
                        st.setString(3, r.getConductor().getNombres());
                        st.setString(4, r.getConductor().getCelular());
                        st.executeUpdate();
                        rs = st.executeQuery(sqlObtieneIdCliente);
                        if (rs.next()) {
                            if (rs.getInt("Id") != 0) {
                                r.setConductorId(rs.getInt("Id"));
                            } else {
                                setAlert(new String[]{"Error", "No se pudo obtener el id del Conductor Registrado, intentelo de nuevo"});
                                return;
                            }
                        }
                    } //Actualiza el conductor si ya existe
                    else {
                        st = conexion.prepareStatement(sqlUpdateCliente);
                        st.setInt(1, r.getConductor().getTipoDocId());
                        st.setString(2, r.getConductor().getDoc());
                        st.setString(3, r.getConductor().getNombres());
                        st.setString(4, r.getConductor().getCelular());
                        st.setInt(5, r.getConductor().getId());
                        st.executeUpdate();

                    }
                }
            }

            if (r.getId() == 0) {// Inserta la reserva si no existe

                st = conexion.prepareStatement(sqlInsertReserva);
                st.setDate(1, r.getFechaReserva().getDateSql());
                st.setString(2, r.getHoraReserva().getHora());
                st.setString(3, r.getPlaca());
                st.setInt(4, r.getPropietarioId());
                st.setInt(5, r.getConductorId());
                st.setInt(6, r.getTipoDeRevisionId());
                st.setInt(7, r.getTipoDeVehiculoId());
                st.setInt(8, r.getUsuarioRegistraId());
                st.executeUpdate();
            } else {//actualiza la reserva existente
                if (r.getEsActiva() == 1) {
                    st = conexion.prepareStatement(sqlUpdateReserva);
                    st.setDate(1, r.getFechaReserva().getDateSql());
                    st.setString(2, r.getHoraReserva().getHora());
                    st.setInt(3, r.getPropietarioId());
                    st.setInt(4, r.getConductorId());
                    st.setInt(5, r.getUsuarioRegistraId());
                    st.setInt(6, r.getTipoDeRevisionId());
                    st.setInt(7, r.getIngresado());
                    st.setInt(8, r.getUsuarioRegistraId());
                    st.setInt(9, r.getId());
                    st.executeUpdate();
                } else {//cambie al estado de la reserva a eliminada (esActiva=0)
                    st = conexion.prepareStatement("UPDATE reservas R SET R.Activa=0, R.UsuarioRegistraId=? WHERE R.Id=?");
                    st.setInt(1, r.getUsuarioRegistraId());
                    st.setInt(2, r.getId());
                    st.executeUpdate();
                }
            }
            if (r.getId() == 0) {
                setAlert(new String[]{"Exito", "La reserva para la Placa " + r.getPlaca() + " Fue registrada correctamente para el dia " + dias[r.getFechaReserva().get(Calendar.DAY_OF_WEEK)] + " " + r.getFechaReserva().get(Calendar.DAY_OF_MONTH) + " de " + meses[r.getFechaReserva().get(Calendar.MONTH)] + " a las " + r.getHoraReserva().getHora()});
            } else {
                if (r.getEsActiva() == 0) {
                    setAlert(new String[]{"Exito", "La reserva para la Placa " + r.getPlaca() + " Fue Eliminada correctamente"});
                } else {
                    setAlert(new String[]{"Exito", "La reserva para la Placa " + r.getPlaca() + " Fue Actualizada correctamente para el dia " + dias[r.getFechaReserva().get(Calendar.DAY_OF_WEEK)] + " " + r.getFechaReserva().get(Calendar.DAY_OF_MONTH) + " de " + meses[r.getFechaReserva().get(Calendar.MONTH)] + " a las " + r.getHoraReserva().getHora()});
                }

            }
            conexion.commit();
            conexion.close();
        } catch (SQLException e) {
            try {
                conexion.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            setAlert(new String[]{"Error", e.getMessage()});
        }

    }

    public ArrayList<String[]> getAlerts() {
        return alert;
    }

    public void setAlert(String[] a) {

        this.alert.add(a);
    }

    private DataSource conexiones;
    private Connection conexion;

    private ArrayList<String[]> alert = new ArrayList<>();

}
