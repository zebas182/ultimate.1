/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.regimp.operaciones;

import co.com.regimp.modelos.Producto;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Brayan
 */
@Stateless
public class ProductoFacade extends AbstractFacade<Producto> {

    @PersistenceContext(unitName = "Ultimate.1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductoFacade() {
        super(Producto.class);
    }

    public int AgregarCantidad(int id, int cantidad, int precio, int cantidadVieja) {

        int can = (int) em.createQuery("select p.cantidadStock from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
        int precant= can + cantidad;
        int cantidadTotal = precant-cantidadVieja;
        System.out.println(cantidadTotal+" "+cantidadVieja+" ");
        return em.createQuery("Update Producto u set U.cantidadStock=:cantidadTotal where u.idProducto=:id").setParameter("cantidadTotal", cantidadTotal).setParameter("id", id).executeUpdate();
    }
}
