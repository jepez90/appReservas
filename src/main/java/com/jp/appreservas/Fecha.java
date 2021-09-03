package com.jp.appreservas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Fecha extends GregorianCalendar {

    public Fecha() {
        super();
        setTimeZone();
    }

    public Fecha(long timeInMilliseconds) {
        super();
        this.setTimeInMillis(timeInMilliseconds);
        setTimeZone();
    }

    public Fecha(String fechaString) {
        super();
        setTimeZone();
        fechaString = fechaString.trim();
        if (fechaString.length() <= 8 && fechaString.indexOf(':') != -1) {//es solo hora
            try {
                this.setTimeInMillis(formatoHora.parse(fechaString).getTime());
            } catch (ParseException e) {
                System.out.println("Error constructor Fecha: " + e.getMessage());
            }
        } else if (fechaString.length() <= 10 && fechaString.indexOf('-') != -1) {
            try {
                this.setTimeInMillis(formatoFecha.parse(fechaString).getTime());
            } catch (ParseException e) {
                System.out.println("Error constructor Fecha: " + e.getMessage());
            }
        } else {
            try {
                this.setTimeInMillis(formatoFechayHora.parse(fechaString).getTime());
            } catch (ParseException e) {
                System.out.println("Error constructor Fecha: " + e.getMessage());
            }

        }

    }

    private void setTimeZone() {
        this.setTimeZone(tz);
        formatoFecha.setTimeZone(tz);
        formatoHora.setTimeZone(tz);
        formatoFechayHora.setTimeZone(tz);
        formatoMinutos.setTimeZone(tz);
    }

    public String getFecha() {
        return formatoFecha.format(this.getTime());
    }

    public String getFechayHora() {
        return formatoFechayHora.format(this.getTime());
    }

    public int getDia() {
        return get(Calendar.DAY_OF_MONTH);
    }

    public int getMes() {
        return get(Calendar.MONTH);
    }

    public int getAnio() {
        return get(Calendar.YEAR);
    }

    public String getHora() {
        return formatoHora.format(this.getTime());
    }

    public String getHoraMil() {
        return formatoHora.format(this.getTime());
    }

    public int getHoraDia() {
        return (get(Calendar.HOUR) == 0) ? 12 : get(Calendar.HOUR);
    }

    public String getAmPm() {
        return (this.get(Calendar.AM_PM) == Calendar.AM) ? "am" : "pm";
    }

    public int getHoraDiaMil() {
        return get(Calendar.HOUR_OF_DAY);
    }

    public String getMinutos() {
        return formatoMinutos.format(this.getTime());
    }

    public void add(Fecha fecha) {
        this.setTimeInMillis(this.getTimeInMillis() + fecha.getTimeInMillis() + tz.getRawOffset());
    }

    public java.sql.Date getDateSql() {
        return new java.sql.Date(this.getTimeInMillis());
    }

    public boolean isInInterval(Fecha fecha) {//fecha de la cual se quiere saber si está en el intervalo de this

        if (this.before(fecha) || (this.getTimeInMillis() == fecha.getTimeInMillis())) {
            Fecha fechaMayor = new Fecha(this.getTimeInMillis());
            fechaMayor.add(Calendar.MINUTE, interval);
            if (fecha.before(fechaMayor)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void next() {//agrega el intervalo a la fecha actual
        this.add(Calendar.MINUTE, interval);
    }

    public void setInterval(int intervalInMinutes) {
        interval = intervalInMinutes;
    }

    @Override
    public String toString() {
        return "Fecha [" + this.getFechayHora() + "]";
    }

    private SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatoFechayHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat formatoMinutos = new SimpleDateFormat("mm");
    //private SimpleDateFormat formatoHoraMil=new SimpleDateFormat("hh:mm:ss ");
    private TimeZone tz = TimeZone.getTimeZone("GMT-5");
    private int interval = 10; /// intervalo en minutos
    private static final long serialVersionUID = 1L;
}
