/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.regimp.operaciones;

import co.com.regimp.controladores.UsuarioController;
import co.com.regimp.modelos.Empleado;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Brayan
 */
@Stateless
public class EmpleadoFacade extends AbstractFacade<Empleado> {

    @PersistenceContext(unitName = "Ultimate.1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpleadoFacade() {
        super(Empleado.class);
    }

   public String EmpleadoLogueado(int id){
   String nom=(String) em.createQuery("SELECT e.nombreEmpleado FROM Empleado e WHERE e.usuarioidUsuario.idUsuario=:id").setParameter("id", id).getSingleResult();
   return nom;
   }
   
}
