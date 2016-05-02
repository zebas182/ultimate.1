/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.regimp.operaciones;

import co.com.regimp.modelos.Producto;
import co.com.regimp.modelos.Proveedor;
import java.util.Date;
import java.util.List;
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

    public List<Producto> UnidadesDeMedida(Producto producto) {
        return (List<Producto>) em.createQuery("SELECT P.unidadDeMedida FROM Producto P WHERE P.nombreProducto=:producto").setParameter("producto", producto.getNombreProducto()).getResultList();
    }

    public List<Producto> Productos(Proveedor proveedor) {
        return (List<Producto>) em.createQuery("SELECT P FROM Producto P WHERE P.proveedor=:proveedor").setParameter("proveedor", proveedor.getNombreProveedor()).getResultList();
    }

    public List<Producto> ProductosString(String proveedor) {
        return (List<Producto>) em.createQuery("SELECT P FROM Producto P WHERE P.proveedor=:proveedor").setParameter("proveedor", proveedor).getResultList();
    }

    public void limiteStock(int cantidad, int id) {
        int control = (int) em.createQuery("select p.control FROM Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
        int total = control - cantidad;
        em.createQuery("UPDATE Producto p SET P.control=:total WHERE p.idProducto=:id").setParameter("total", total).setParameter("id", id).executeUpdate();
    }

    public int AgregarCantidad(int id, int cantidad, int precio, int cantidadVieja) {

        int can = (int) em.createQuery("select p.cantidadStock from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
        int precant = can + cantidad;
        int cantidadTotal = precant - cantidadVieja;
        System.out.println(cantidadTotal + " " + cantidadVieja + " ");
        return em.createQuery("Update Producto u set U.cantidadStock=:cantidadTotal where u.idProducto=:id").setParameter("cantidadTotal", cantidadTotal).setParameter("id", id).executeUpdate();
    }

    public int AgregarCantidadProducto(int id, int cantidad) {
        int can = (int) em.createQuery("select p.cantidadStock from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
        int catidadTotal = can + cantidad;
        return em.createQuery("Update Producto u set U.cantidadStock=:cantidadTotal where u.idProducto=:id").setParameter("cantidadTotal", catidadTotal).setParameter("id", id).executeUpdate();
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
        try {

            int can = (int) em.createQuery("select p.cantidadStock from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
            int precant = can - cantidad;

            int canCon = (int) em.createQuery("select p.control from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
            almacen = canCon;
            almacen = almacen + cantidad;
            em.createQuery("Update Producto p set p.control=:control WHERE p.idProducto=:id").setParameter("control", almacen).setParameter("id", id).executeUpdate();
            almacen = 0;
            int canControl = (int) em.createQuery("select p.control from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();

            if (precant < 0) {
                return -1;
            } else {
                return canControl;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return -2;
    }

    public void QuitarCantidaddef(int id, int cantidad) {
        int can = (int) em.createQuery("select p.cantidadStock from Producto p where p.idProducto=:id").setParameter("id", id).getSingleResult();
        int precant = can - cantidad;
        em.createQuery("Update Producto u set U.cantidadStock=:cantidadTotal where u.idProducto=:id").setParameter("cantidadTotal", precant).setParameter("id", id).executeUpdate();
    }

    public List verificarReporteEmpleado(String Fecha, String Empleado) {
        try {
//            em.createQuery("SELECT P FROM Producto P INNER JOIN DetalleDespacho D ON D.productoidProducto.idProducto=P.idProducto INNER JOIN Despacho E ON D.despachoidDespacho.idDespacho = E.idDespacho WHERE E.fechaDespacho LIKE  '"+Fecha+"%'").getResultList();
            return (List) em.createQuery("SELECT D FROM Despacho D INNER JOIN DetalleDespacho E ON D.idDespacho=E.despachoidDespacho.idDespacho INNER JOIN Producto P ON E.productoidProducto.idProducto=P.idProducto WHERE D.fechaDespacho LIKE  '" + Fecha + "%'").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
