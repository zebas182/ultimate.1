package co.com.regimp.controladores;

import co.com.regimp.modelos.DetallePedido;
import co.com.regimp.controladores.util.JsfUtil;
import co.com.regimp.controladores.util.JsfUtil.PersistAction;
import co.com.regimp.modelos.Pedido;
import co.com.regimp.operaciones.DetallePedidoFacade;
import co.com.regimp.operaciones.ProductoFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@ManagedBean(name = "detallePedidoController")
@SessionScoped
public class DetallePedidoController implements Serializable {

    @EJB
    private co.com.regimp.operaciones.DetallePedidoFacade ejbFacade = new DetallePedidoFacade();
    @EJB
    private co.com.regimp.operaciones.ProductoFacade ejbProducto = new ProductoFacade();
    private List<DetallePedido> items = null;
    private List<DetallePedido> filtered = null;
    private DetallePedido selected = new DetallePedido();
    private int cantidadNueva = 0;
    private int total = 0;
    private int viejo = 0;
    private Pedido pedido;
    private Date horaActual = new Date();
    public DetallePedidoController() {

    }

    public DetallePedido getSelected() {
        return selected;
    }

    public void setSelected(DetallePedido selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DetallePedidoFacade getFacade() {
        return ejbFacade;
    }

    public DetallePedido prepareCreate() {
        selected = new DetallePedido();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DetallePedidoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
            if (selected.getCantidadEntregados() == null) {
                selected.setCantidadEntregados(0);
                System.out.println(selected.getProductoidProducto().getIdProducto() + " " + cantidadNueva + " " + selected.getPrecioUnidadCompra() + " " + selected.getCantidadEntregados());
                ejbProducto.AgregarCantidad(selected.getProductoidProducto().getIdProducto(), cantidadNueva, selected.getPrecioUnidadCompra(), selected.getCantidadEntregados());
                selected.setCantidadEntregados(cantidadNueva);
                total = (selected.getCantidadEntregados() * selected.getPrecioUnidadCompra());
                System.out.println(total);
                selected.setPrecioTotal(total);
                persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DetallePedidoUpdated"));
                total = 0;
            } else {
                System.out.println(selected.getProductoidProducto().getIdProducto() + " " + cantidadNueva + " " + selected.getPrecioUnidadCompra() + " " + selected.getCantidadEntregados());
                ejbProducto.AgregarCantidad(selected.getProductoidProducto().getIdProducto(), cantidadNueva, selected.getPrecioUnidadCompra(), selected.getCantidadEntregados());
                selected.setCantidadEntregados(cantidadNueva);
                total = (selected.getCantidadEntregados() * selected.getPrecioUnidadCompra());
                System.out.println(total);
                selected.setPrecioTotal(total);
                persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DetallePedidoUpdated"));
                total = 0;
            }

       
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DetallePedidoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<DetallePedido> getItems() {
            items = getFacade().findAll();
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<DetallePedido> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<DetallePedido> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public int getCantidadNueva() {
        if (selected.getCantidadEntregados() != null) {
            return cantidadNueva = selected.getCantidadEntregados();
        } else {
            return cantidadNueva = 0;
        }

    }

    public void setCantidadNueva(int cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }

    public Date getHoraActual() {
        return horaActual;
    }

    public void setHoraActual(Date horaActual) {
        this.horaActual = horaActual;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<DetallePedido> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<DetallePedido> filtered) {
        this.filtered = filtered;
    }

    @FacesConverter(forClass = DetallePedido.class)
    public static class DetallePedidoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DetallePedidoController controller = (DetallePedidoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "detallePedidoController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof DetallePedido) {
                DetallePedido o = (DetallePedido) object;
                return getStringKey(o.getIdDetallePedido());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DetallePedido.class.getName()});
                return null;
            }
        }

    }

}
