package co.com.regimp.controladores;

import co.com.regimp.modelos.Pedido;
import co.com.regimp.controladores.util.JsfUtil;
import co.com.regimp.controladores.util.JsfUtil.PersistAction;
import co.com.regimp.modelos.DetallePedido;
import co.com.regimp.modelos.Producto;
import co.com.regimp.operaciones.PedidoFacade;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "pedidoController")
@SessionScoped
public class PedidoController implements Serializable {

    @EJB
    private co.com.regimp.operaciones.PedidoFacade ejbFacade;
    @EJB
    private co.com.regimp.operaciones.ProductoFacade ejbProducto;
    @EJB
    private co.com.regimp.operaciones.DetallePedidoFacade ejbDetalle;
    private List<Pedido> items = null;
    private Pedido selected = new Pedido();
    private Producto producto;
    private List<Producto> listProductos = null;
    private List<DetallePedido> detallePedido = new ArrayList();
    private List<DetallePedido> detallePedido2;
    private DetallePedido detalle = new DetallePedido();
    private int precioUnidad = 0;
    private int cantidadPedidos = 0;
    private int cantidadEntregados = 0;
    private String UnidadDeMedida = null;
    private Date fechaDeVencimiento = null;
    private Date fechaLote = null;
    private Pedido PedidoSeleccionado;
    private DetallePedido det;

    public PedidoController() {
    }

    public List<Producto> completeProducto(String query) {
        List<Producto> allProducto = ejbProducto.findAll();
        List<Producto> filteredProducto = new ArrayList<>();

        for (int i = 0; i < allProducto.size(); i++) {
            Producto skin = allProducto.get(i);
            if (skin.getNombreProducto().toLowerCase().startsWith(query)) {
                filteredProducto.add(skin);
            }
        }

        return filteredProducto;
    }

    public void PorIdPedido(ValueChangeEvent value) {
        PedidoSeleccionado = (Pedido) value.getNewValue();
        detallePedido2 = ejbFacade.PorIdPedido(PedidoSeleccionado);
    }

    public void Agregar() {
        det = new DetallePedido();
        det.setProductoidProducto(producto);
        det.setPrecioUnidadCompra(precioUnidad);
        det.setCantidadPedidos(cantidadPedidos);
        det.setProductoidProducto(producto);
        det.setPrecioUnidadCompra(precioUnidad);
        det.setUnidadDeMedida(UnidadDeMedida);
        det.setCantidadPedidos(cantidadPedidos);
        UnidadDeMedida = "";
        precioUnidad = 0;
        producto = null;
        cantidadPedidos = 0;
        detallePedido.add(det);
        det = null;

    }

    public void Registrar() {
        try {
            selected.setEstado(true);
            ejbFacade.create(selected);
            for (DetallePedido det : detallePedido) {
                det.setPedidoidPedido(selected);
                ejbDetalle.create(det);
            }
            detallePedido.clear();
            UnidadDeMedida = "";
            precioUnidad = 0;
            cantidadPedidos = 0;
            selected.setEmpleadoidEmpleado(null);
            selected.setProveedoridProveedor(null);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public Pedido getSelected() {
        return selected;
    }

    public void setSelected(Pedido selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PedidoFacade getFacade() {
        return ejbFacade;
    }

    public Pedido prepareCreate() {
        selected = new Pedido();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PedidoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PedidoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PedidoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Pedido> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
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

    public List<Pedido> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Pedido> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<Producto> getListProductos() {
        return listProductos;
    }

    public void setListProductos(List<Producto> listProductos) {
        this.listProductos = listProductos;
    }

    public List<DetallePedido> getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(List<DetallePedido> detallePedido) {
        this.detallePedido = detallePedido;
    }

    public List<DetallePedido> getDetallePedido2() {
        return detallePedido2;
    }

    public void setDetallePedido2(List<DetallePedido> detallePedido2) {
        this.detallePedido2 = detallePedido2;
    }

    public DetallePedido getDetalle() {
        return detalle;
    }

    public void setDetalle(DetallePedido detalle) {
        this.detalle = detalle;
    }

    public int getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(int precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public int getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(int cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }

    public int getCantidadEntregados() {
        return cantidadEntregados;
    }

    public void setCantidadEntregados(int cantidadEntregados) {
        this.cantidadEntregados = cantidadEntregados;
    }

    public String getUnidadDeMedida() {
        return UnidadDeMedida;
    }

    public void setUnidadDeMedida(String UnidadDeMedida) {
        this.UnidadDeMedida = UnidadDeMedida;
    }

    public Date getFechaDeVencimiento() {
        return fechaDeVencimiento;
    }

    public void setFechaDeVencimiento(Date fechaDeVencimiento) {
        this.fechaDeVencimiento = fechaDeVencimiento;
    }

    public Date getFechaLote() {
        return fechaLote;
    }

    public void setFechaLote(Date fechaLote) {
        this.fechaLote = fechaLote;
    }

    public Pedido getPedidoSeleccionado() {
        return PedidoSeleccionado;
    }

    public void setPedidoSeleccionado(Pedido PedidoSeleccionado) {
        this.PedidoSeleccionado = PedidoSeleccionado;
    }

    public DetallePedido getDet() {
        return det;
    }

    public void setDet(DetallePedido det) {
        this.det = det;
    }



    @FacesConverter(forClass = Pedido.class)
    public static class PedidoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PedidoController controller = (PedidoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "pedidoController");
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
            if (object instanceof Pedido) {
                Pedido o = (Pedido) object;
                return getStringKey(o.getIdPedido());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Pedido.class.getName()});
                return null;
            }
        }

    }

}
