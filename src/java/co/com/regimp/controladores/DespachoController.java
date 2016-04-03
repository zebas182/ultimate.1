package co.com.regimp.controladores;

import co.com.regimp.modelos.Despacho;
import co.com.regimp.controladores.util.JsfUtil;
import co.com.regimp.controladores.util.JsfUtil.PersistAction;
import co.com.regimp.modelos.DetalleDespacho;
import co.com.regimp.modelos.Producto;
import co.com.regimp.operaciones.DespachoFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;


@ManagedBean(name = "despachoController")
@SessionScoped
public class DespachoController implements Serializable {

    @EJB
    private co.com.regimp.operaciones.DespachoFacade ejbFacade;
    @EJB
    private co.com.regimp.operaciones.ProductoFacade ejbProducto;
    @EJB
    private co.com.regimp.operaciones.DetalleDespachoFacade ejbDetalle;
    @EJB
    private co.com.regimp.operaciones.EmpleadoFacade ejbEmpleado;
    private List<Despacho> items = null;
    private Despacho selected = new Despacho();
    private List<DetalleDespacho> detalleDespacho = new ArrayList();
    private List<DetalleDespacho> detalleDespacho2;
    private Producto producto;
    private String UnidadDeMedida = null;
    private int precioUnidadVenta = 0;
    private int cantidadVendidos = 0;
    private DetalleDespacho det;
    private Producto productoSeleccionado;
    private int result = 0;
    private int almacen = 0;
    
    public DespachoController() {
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

    public void limpiar() {
        ejbProducto.limpiarCon();
        detalleDespacho.clear();
        UnidadDeMedida = "";
        precioUnidadVenta = 0;
        producto = null;
        cantidadVendidos = 0;
        selected.setEmpleado(null);
    }

    public void limpiarAgrega() {
        ejbProducto.limpiarCon();
        UnidadDeMedida = "";
        precioUnidadVenta = 0;
        producto = null;
        cantidadVendidos = 0;
        selected.setEmpleado(null);
    }
    
    public void Agregar() {
        det = new DetalleDespacho();
        det.setProductoidProducto(producto);
        det.setPrecioVentaUnidad(precioUnidadVenta);
        det.setUnidadDeMedida(UnidadDeMedida);
        det.setCantidadVendida(cantidadVendidos);
        det.setPrecioTotal(precioUnidadVenta * cantidadVendidos);
        almacen = ejbProducto.stock(det.getProductoidProducto().getIdProducto());

        if (almacen == 0) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El Stock Esta Vacio, Haga Pedido");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            ejbProducto.limpiarControl(det.getProductoidProducto().getIdProducto());
        } else {
            result = ejbProducto.QuitarCantidad(det.getProductoidProducto().getIdProducto(), det.getCantidadVendida());
            if (result <= almacen) {

                if (det.getCantidadVendida() > 0) {
                    if (result < 0) {
                        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se puede despachar más productos de los que hay en el stock");
                        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
                        ejbProducto.limiteStock(det.getCantidadVendida(), det.getProductoidProducto().getIdProducto());
                    } else {
                        UnidadDeMedida = null;
                        precioUnidadVenta = 0;
                        producto = null;
                        cantidadVendidos = 0;
                        detalleDespacho.add(det);
                        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto Agregado Exitosamente", "");
                        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
                    }
                } else {
                    FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Precaución", "La Cantidad Vendida debe ser mayor a 0");
                    FacesContext.getCurrentInstance().addMessage(null, facesMsg);
                }
            } else {
                ejbProducto.limiteStock(det.getCantidadVendida(), det.getProductoidProducto().getIdProducto());
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se puede despachar más productos de los que hay en el stock");
                FacesContext.getCurrentInstance().addMessage(null, facesMsg);
                
            }
        }

        det = null;
    }

    public void Registrar() {
        try {
            selected.setEstado(true);
            ejbFacade.create(selected);
            for (DetalleDespacho det : detalleDespacho) {
                det.setDespachoidDespacho(selected);
                ejbDetalle.create(det);
                ejbProducto.QuitarCantidaddef(det.getProductoidProducto().getIdProducto(), det.getCantidadVendida());
                ejbProducto.limpiarControl(det.getProductoidProducto().getIdProducto());
            }
            detalleDespacho.clear();
            UnidadDeMedida = "";
            precioUnidadVenta = 0;
            cantidadVendidos = 0;
            selected.setEmpleado("");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public Despacho getSelected() {
        return selected;
    }

    public void setSelected(Despacho selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DespachoFacade getFacade() {
        return ejbFacade;
    }

    public Despacho prepareCreate() {
        selected = new Despacho();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DespachoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DespachoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DespachoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Despacho> getItems() {
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

    public List<Despacho> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Despacho> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getUnidadDeMedida() {
        return UnidadDeMedida;
    }

    public void setUnidadDeMedida(String UnidadDeMedida) {
        this.UnidadDeMedida = UnidadDeMedida;
    }

    public int getPrecioUnidadVenta() {
        return precioUnidadVenta;
    }

    public void setPrecioUnidadVenta(int precioUnidadVenta) {
        this.precioUnidadVenta = precioUnidadVenta;
    }

    public int getCantidadVendidos() {
        return cantidadVendidos;
    }

    public void setCantidadVendidos(int cantidadVendidos) {
        this.cantidadVendidos = cantidadVendidos;
    }

    public List<DetalleDespacho> getDetalleDespacho() {
        return detalleDespacho;
    }

    public void setDetalleDespacho(List<DetalleDespacho> detalleDespacho) {
        this.detalleDespacho = detalleDespacho;
    }

    public DetalleDespacho getDet() {
        return det;
    }

    public void setDet(DetalleDespacho det) {
        this.det = det;
    }

    public List<DetalleDespacho> getDetalleDespacho2() {
        return detalleDespacho2;
    }

    public void setDetalleDespacho2(List<DetalleDespacho> detalleDespacho2) {
        this.detalleDespacho2 = detalleDespacho2;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getAlmacen() {
        return almacen;
    }

    public void setAlmacen(int almacen) {
        this.almacen = almacen;
    }


    @FacesConverter(forClass = Despacho.class)
    public static class DespachoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DespachoController controller = (DespachoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "despachoController");
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
            if (object instanceof Despacho) {
                Despacho o = (Despacho) object;
                return getStringKey(o.getIdDespacho());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Despacho.class.getName()});
                return null;
            }
        }

    }

}
