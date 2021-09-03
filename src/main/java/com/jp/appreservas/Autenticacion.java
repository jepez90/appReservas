package com.jp.appreservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class Autenticacion {

    public Autenticacion(DataSource conexiones, HttpSession sesion) {
        this.conexiones = conexiones;
        this.sesion = sesion;
    }

    public boolean isUserLogued() {
        if (sesion.getAttribute("isUserLogedNow") == null) {//sesion nueva
            sesion.setAttribute("isUserLogedNow", false);
            return false;
        } else {
            if ((boolean) sesion.getAttribute("isUserLogedNow")) { //sesion ya establecida usuario no logueado
                return (sesion.getAttribute("UserLoguedNow") != null);
            } else {
                return false;
            }
        }

    }

    public boolean logUser(HttpServletRequest request) {
        Map<String, String[]> Parametros = request.getParameterMap();

        //valida los parametros
        boolean arePramsOk = true;
        if (!Parametros.containsKey("u")) {
            arePramsOk = false;
        } else {
            if (Parametros.get("u")[0].length() < Usuario.MinCaracteresForU) {
                request.setAttribute("invalidData", true);
                arePramsOk = false;
            }
        }
        if (!Parametros.containsKey("p")) {
            arePramsOk = false;
        } else {
            if (Parametros.get("p")[0].length() < Usuario.MinCaracteresForU) {
                request.setAttribute("invalidData", true);
                arePramsOk = false;
            }
        }
        if (arePramsOk) {
            Usuario usuario = getUser(Parametros.get("u")[0], Parametros.get("p")[0]);
            if (usuario.getId() == 0) {
                request.setAttribute("invalidData", true);
                return false;
            } else if (usuario.getIsActivo() == 0) {
                request.setAttribute("isInactive", true);
                return false;
            } else {
                sesion.invalidate();
                sesion = request.getSession(true);
                usuario.setLoggedAt(new Fecha());
                sesion.setAttribute("isUserLogedNow", true);
                sesion.setAttribute("UserLoguedNow", usuario);
                return true;
            }
        } else {
            return false;
        }
    }

    public void logOutUser(HttpServletRequest request) {
        // TODO Auto-generated method stub
        if (request.getParameterMap().containsKey("UFLog")) {

            sesion.invalidate();
            sesion = request.getSession(true);
            sesion.setAttribute("isUserLogedNow", false);
            sesion.setAttribute("UserNameLoguedNow", null);
            System.out.println("log out");

        }

    }

    public int userEdit(HttpServletRequest request) {
        //1 enviar formulario de edicion
        //2 enviar formulario de nuevo usuario
        //3 se recibe formulario de edicion ok
        //4 se recibe formulario de edicion not ok
        Map<String, String[]> parametros = request.getParameterMap();
        Usuario UsuarioLoggeado = (Usuario) request.getSession().getAttribute("UserLoguedNow");

        if (parametros.containsKey("UFAgregar")) {

            System.out.println("lanzar formulario de agregar");

            return 2;
        }
        if (parametros.containsKey("UFUser")) {

            request.setAttribute("Usuario", request.getSession().getAttribute("UserLoguedNow"));

            return 1;
        }
        if (parametros.containsKey("editUser")) {

            Usuario usuario = new Usuario();
            if (parametros.containsKey("userId")) {
                usuario.setId(Integer.parseInt(parametros.get("userId")[0]));
            } else {
                return 4;
            }
            usuario.setUserName(UsuarioLoggeado.getUserName());

            if (parametros.containsKey("Nombres")) {
                if (Pattern.matches("\\D{5,100}", parametros.get("Nombres")[0])) {
                    usuario.setNombre(parametros.get("Nombres")[0].toUpperCase());
                } else {
                    return 4;
                }
            } else {
                return 4;
            }
            if (parametros.containsKey("p")) {
                if (parametros.get("p")[0].length() >= Usuario.MinCaracteresForP) {
                    usuario.setPass(parametros.get("p")[0]);
                } else {
                    return 4;
                }
            } else {
                return 4;
            }
            usuario.setIsActivo(1);
            usuario.setIsAdmin(UsuarioLoggeado.getIsAdmin());
            setUser(usuario);
            request.setAttribute("Usuario", getUser(usuario.getUserName(), usuario.getPass()));
            return 3;
            //_____________________________________________________________________________________________
        } else if (parametros.containsKey("newUser")) {

            if (UsuarioLoggeado.getIsAdmin() == 0) {
                return 4;
            }
            Usuario usuario = new Usuario();
            if (parametros.containsKey("u")) {
                if (Pattern.matches("[A-z0-9]{" + Usuario.MinCaracteresForU + ",50}", parametros.get("u")[0])) {
                    usuario.setUserName(parametros.get("u")[0].trim());
                } else {
                    return 4;
                }
            } else {
                return 4;
            }
            if (parametros.containsKey("Cedula")) {
                if (Pattern.matches("\\d{7,10}", parametros.get("Cedula")[0])) {
                    usuario.setCedula(Long.parseLong(parametros.get("Cedula")[0].trim()));
                } else {
                    return 4;
                }
            } else {
                return 4;
            }
            if (parametros.containsKey("Nombres")) {
                if (Pattern.matches("\\D{5,100}", parametros.get("Nombres")[0])) {
                    usuario.setNombre(parametros.get("Nombres")[0].trim());
                } else {
                    return 4;
                }
            } else {
                return 4;
            }
            if (parametros.containsKey("p")) {
                if (parametros.get("p")[0].length() >= Usuario.MinCaracteresForP) {
                    usuario.setPass(parametros.get("p")[0]);
                } else {
                    return 4;
                }
            } else {
                return 4;
            }
            usuario.setIsActivo(1);
            usuario.setIsAdmin(0);
            usuario.setCreadorId(UsuarioLoggeado.getId());
            if (setUser(usuario)) {
                request.setAttribute("Usuario", getUser(usuario.getUserName(), usuario.getPass()));
                return 3;
            } else {
                return 4;
            }

        }

        return 0;

    }

    private boolean setUser(Usuario u) {
        Connection conexion;
        PreparedStatement st;
        String sql;

        try {

            conexion = conexiones.getConnection();
            if (u.getId() == 0) {//inserta usuario
                sql = "INSERT INTO USUARIOS ( Usuario, Cedula, Nombre, Pass, OldPass, Activo, Admin, CreadorId) VALUES (?, ?, ?, AES_ENCRYPT(?,?), ?, ?, ?, ?) ";
                st = conexion.prepareStatement(sql);
                st.setString(1, u.getUserName());
                st.setLong(2, u.getCedula());
                st.setString(3, u.getNombre());
                st.setString(4, u.getUserName().toLowerCase());
                st.setString(5, u.getPass());
                st.setString(6, u.getOldPass());
                st.setInt(7, u.getIsActivo());
                st.setInt(8, u.getIsAdmin());
                st.setInt(9, u.getCreadorId());

            } else {
                sql = "UPDATE USUARIOS SET Nombre=?, Pass=AES_ENCRYPT(?,?), OldPass=?, Activo=?, Admin=? WHERE Id=?";
                st = conexion.prepareStatement(sql);
                st.setString(1, u.getNombre());
                st.setString(2, u.getUserName().toLowerCase());
                st.setString(3, u.getPass());
                st.setString(4, u.getOldPass());
                st.setInt(5, u.getIsActivo());
                st.setInt(6, u.getIsAdmin());
                st.setInt(7, u.getId());
            }
            st.executeUpdate();
            conexion.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Autenticacion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    private Usuario getUser(String u, String p) {
        Connection conexion;
        PreparedStatement st;
        ResultSet rs;
        Usuario usuario = new Usuario();
        String sql = "SELECT * FROM USUARIOS U WHERE U.Usuario=? AND U.Pass=AES_ENCRYPT(?,?) ";

        try {
            conexion = conexiones.getConnection();
            st = conexion.prepareStatement(sql);
            st.setString(1, u);
            st.setString(2, u.toLowerCase());
            st.setString(3, p);
            rs = st.executeQuery();
            if (rs.next()) {
                usuario.setId(rs.getInt("Id"));
                usuario.setUserName(rs.getString("Usuario"));
                usuario.setCedula(rs.getLong("Cedula"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setPass(rs.getString("Pass"));
                usuario.setOldPass(rs.getString("OldPass"));
                usuario.setUltimoCambo(new Fecha(rs.getDate("UltimoCambio").getTime()));

                usuario.setIsActivo(rs.getInt("Activo"));
                usuario.setIsAdmin(rs.getInt("Admin"));

                conexion.close();
                return usuario;
            } else {

                conexion.close();
                return usuario;
            }
        } catch (SQLException e) {
            Logger.getLogger(Autenticacion.class.getName()).log(Level.SEVERE, null, e);
            return usuario;
        }

    }
    private DataSource conexiones;
    private HttpSession sesion;
}
