/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.regimp.operaciones;

import co.com.regimp.modelos.DetallePedido;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Brayan
 */
@Stateless
public class DetallePedidoFacade extends AbstractFacade<DetallePedido> {
    @PersistenceContext(unitName = "Ultimate.1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetallePedidoFacade() {
        super(DetallePedido.class);
    }

        public List<DetallePedido> getLista(Integer id) {
        return (List<DetallePedido>) em.createQuery("SELECT D FROM DetallePedido D WHERE D.pedidoidPedido.idPedido=:id").setParameter("id", id).getResultList();
    }
    
   public void actualizarTotal(int total,int id){
   em.createQuery("UPDATE DetallePedido D SET D.precioTotal = :total WHERE D.idDetallePedido =:id").setParameter("total", total).setParameter("id", id).executeUpdate();
   }
}
