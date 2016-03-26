/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.regimp.operaciones;

import co.com.regimp.modelos.DetallePedido;
import co.com.regimp.modelos.Pedido;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Brayan
 */
@Stateless
public class PedidoFacade extends AbstractFacade<Pedido> {
    @PersistenceContext(unitName = "Ultimate.1PU")
    private EntityManager em;
    @EJB
    private co.com.regimp.operaciones.DetallePedidoFacade detalleFacade;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PedidoFacade() {
        super(Pedido.class);
    }

    public Pedido registrar() {
        try {

            Pedido Ultimo = (Pedido) em.createQuery("SELECT MAX(P.idPedido) FROM Pedido P").getSingleResult();
            return Ultimo;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    
    public List<DetallePedido> PorIdPedido(Pedido pedido) {
        try {
            return (List<DetallePedido>) detalleFacade.getLista(pedido.getIdPedido());
        } catch (Exception e) {
            e.getStackTrace();
        }
        
      return null;
    }
}
