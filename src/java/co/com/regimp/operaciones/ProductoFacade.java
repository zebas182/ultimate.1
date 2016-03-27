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
    private int almacen = 0;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductoFacade() {
        super(Producto.class);
    }

    public int AgregarCantidad(int id, int cantidad, int precio, int cantidadVieja) {

        int can = (int) em.createQuery("select p.cantidadStock from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
        int precant = can + cantidad;
        int cantidadTotal = precant - cantidadVieja;
        System.out.println(cantidadTotal + " " + cantidadVieja + " ");
        return em.createQuery("Update Producto u set U.cantidadStock=:cantidadTotal where u.idProducto=:id").setParameter("cantidadTotal", cantidadTotal).setParameter("id", id).executeUpdate();
    }

    public void limpiarCon() {
        em.createQuery("Update Producto p set p.control=:control ").setParameter("control", 0).executeUpdate();
    }

    public void limpiarControl(int id) {
        em.createQuery("Update Producto p set p.control=:control WHERE p.idProducto=:id").setParameter("control", 0).setParameter("id", id).executeUpdate();
    }

    public int stock(int id) {
        int stock = (int) em.createQuery("select p.cantidadStock from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
        return stock;
    }

    public int QuitarCantidad(int id, int cantidad) {

        int can = (int) em.createQuery("select p.cantidadStock from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
        int precant = can - cantidad;
        int canCon = (int) em.createQuery("select p.control from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
        almacen = canCon;
        almacen = almacen + cantidad;
        em.createQuery("Update Producto p set p.control=:control WHERE p.idProducto=:id").setParameter("control", almacen).setParameter("id", id).executeUpdate();
        almacen = 0;
        int canControl = (int) em.createQuery("select p.control from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();

        if (precant < 0) {
            return 0;
        } else {
            return canControl;
        }
    }

    public void QuitarCantidaddef(int id, int cantidad) {
        int can = (int) em.createQuery("select p.cantidadStock from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
        int precant = can - cantidad;
        em.createQuery("Update Producto u set U.cantidadStock=:cantidadTotal where u.idProducto=:id").setParameter("cantidadTotal", precant).setParameter("id", id).executeUpdate();
    }
}
