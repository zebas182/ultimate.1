/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.regimp.operaciones;

import co.com.regimp.modelos.Empleado;
import co.com.regimp.modelos.Usuario;
import java.util.List;
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

    public Usuario cambiarClave(String clave, int idUsuario) {
        try {
            em.createQuery("UPDATE Usuario u SET u.contrasena=:clave where u.idUsuario=:idUsuario").setParameter("clave", clave).setParameter("idUsuario", idUsuario).executeUpdate();
        } catch (Exception ex) {
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
}
