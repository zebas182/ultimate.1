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
import javax.swing.JOptionPane;

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

    public Empleado buscarID(String correo) {
        try {
            Empleado e = (Empleado) em.createQuery("SELECT e FROM Empleado e WHERE e.correo=:correo").setParameter("correo", correo).getSingleResult();
            if (e != null) {
                UsuarioController uc = new UsuarioController(); 
                uc.generarNuevaClave(e);
                return e;
            }
        } catch (Exception ex) {
           ex.getMessage();
        }
        return null;
    }

}
