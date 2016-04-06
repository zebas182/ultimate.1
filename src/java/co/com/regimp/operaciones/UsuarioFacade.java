/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.regimp.operaciones;

import co.com.regimp.modelos.Email;
import co.com.regimp.modelos.Empleado;
import co.com.regimp.modelos.Usuario;
import java.util.List;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Brayan
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "Ultimate.1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario listaDeUsuario(String usuario, String clave) {
        try {
            Usuario u = (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE u.nombreUsuario = :Usuario and u.contrasena = :clave and u.estado=1").setParameter("Usuario", usuario).setParameter("clave", clave).getSingleResult();
            if (u != null) {
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Empleado consultarEmpleado(Usuario u) {
        try {

            return (Empleado) em.createQuery("SELECT e FROM Empleado e WHERE e.usuarioidUsuario.idUsuario = :id").setParameter("id", u.getIdUsuario()).getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Usuario> ListaUsuariosDisponibles() {
        try {
            List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u where u.estado=true").getResultList();
            if (lista != null) {
                return lista;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario Eliminar(int id) {
        try {
            em.createQuery("update Usuario u set u.estado=0 where u.idUsuario=:id").setParameter("id", id).executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario registrar(String nombreUsuario) {
        try {
            return (Usuario) em.createQuery("SELECT U FROM Usuario U WHERE U.nombreUsuario=:nombreUsuario").setParameter("nombreUsuario", nombreUsuario).getSingleResult();

        } catch (Exception e) {
        }
        return null;
    }

    public int cambioContrasena(String nombre) {
        int u = (int) em.createQuery("Select u.idUsuario from Usuario u where u.nombreUsuario=:nombre ").setParameter("nombre", nombre).getSingleResult();
        String co = (String) em.createQuery("Select e.correo from Empleado e where e.usuarioidUsuario.idUsuario=:id ").setParameter("id", u).getSingleResult();

        if (u != 0) {

            String nuevaClave = UUID.randomUUID().toString();
            Email email = new Email();
            String clave = "28032016Regimp";
            String de = "regimpequipo@outlook.com";
            String mensaje = "Por solicitud del usuario recibimos una petición de cambio de contraseña, A continuación procederemos a facilitarle su nueva contraseña \n"
                    + "Contraseña: ".concat(nuevaClave);
            String asunto = "Cambio de contraseña regimp";
            boolean resultado = email.enviarCorreo(de, clave, co, mensaje, asunto);
            String cifrado = Encripcion.Encriptar.encriptaEnMD5(nuevaClave);
            em.createQuery("UPDATE Usuario U set U.contrasena =:contrasena where U.nombreUsuario=:nombre").setParameter("contrasena", cifrado).setParameter("nombre", nombre).executeUpdate();
            return 1;
        }
        return 0;
    }

    public void cambiar_contrasena(String contrasena, int id) {
        em.createQuery("UPDATE Usuario u set U.contrasena=:contrasena where u.idUsuario=:id").setParameter("contrasena", contrasena).setParameter("id", id).executeUpdate();
    }
}
