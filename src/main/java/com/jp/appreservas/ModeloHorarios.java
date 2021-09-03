package com.jp.appreservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javax.sql.DataSource;

public class ModeloHorarios {

    private DataSource conexiones;

    public ModeloHorarios(DataSource conexiones) {
        this.conexiones = conexiones;
    }

    public Horario getHorario(Fecha fecha, int tipoDeVehiculo) {
        Connection conexion;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql;

        Horario horario = null;
        int DiaEnSemana;
        DiaEnSemana = fecha.get(Calendar.DAY_OF_WEEK);
        if (DiaEnSemana == 1) {
            DiaEnSemana = 7;
        } else {
            DiaEnSemana--;
        }

        sql = "SELECT * FROM HORARIOS H WHERE H.Fecha=? OR (H.Dia=? AND H.PorDefecto=1 AND H.Fecha<?) ORDER BY H.Id DESC LIMIT 1";
        try {
            conexion = conexiones.getConnection();
            st = conexion.prepareStatement(sql);
            st.setDate(1, fecha.getDateSql());
            st.setInt(2, DiaEnSemana);
            st.setDate(3, fecha.getDateSql());
            rs = st.executeQuery();
            if (rs.next()) {
                horario = new Horario(
                        fecha,
                        new Fecha(rs.getTime("HoraInicio").getTime()),
                        new Fecha(rs.getTime("HoraFinal").getTime())
                );
                horario.setDiaEnSemana(DiaEnSemana);
                if (rs.getInt("PorDefecto") == 1) {
                    horario.setPorDefecto(true);
                } else {
                    horario.setPorDefecto(false);
                }
                horario.setComentario(rs.getString("Comentario"));
            } else {
                horario = new Horario(
                        fecha,
                        new Fecha("00:00:00"),
                        new Fecha("00:00:00")
                );
                horario.setComentario(rs.getString("Error"));
            }
            sql = "SELECT HoraDelDia, Minuto  FROM reservasxhora r WHERE r.TipoDeVehiculoId=? order by HoraDelDia, Minuto";
            st = conexion.prepareStatement(sql);
            st.setInt(1, tipoDeVehiculo);
            rs = st.executeQuery();

            int horaInicio = horario.getHoraInicio().get(Calendar.HOUR_OF_DAY);
            int horaFin = horario.getHoraFin().get(Calendar.HOUR_OF_DAY);

            rs.last();
            byte[][] horas = new byte[horaFin][60];
            rs.first();
            int min = 0;
            int hora = 0;
            while (rs.next()) {
                if (rs.getByte("HoraDelDia") <= horaFin) {
                    if (hora != rs.getByte("HoraDelDia")) {
                        horas[hora][min] = (byte) (60 - min);
                    } else {
                        horas[hora][min] = (byte) (rs.getByte("Minuto") - min);
                    }
                    min = rs.getByte("Minuto");
                    hora = rs.getByte("HoraDelDia");
                }
            }
            horas[hora][min] = (byte) (60 - min);
            for (int i = horaInicio; i < horaFin; i++) {
                if (horas[i][0] == 0) {
                    horas[i] = horas[0];
                }
            }
            horario.setIntervalos(horas);
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return horario;

    }

}
