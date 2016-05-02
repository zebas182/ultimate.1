package co.com.regimp.controladores;

import co.com.regimp.modelos.Devolucion;
import co.com.regimp.controladores.util.JsfUtil;
import co.com.regimp.controladores.util.JsfUtil.PersistAction;
import co.com.regimp.modelos.DetalleDevolucion;
import co.com.regimp.modelos.Producto;
import co.com.regimp.operaciones.DevolucionFacade;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "devolucionController")
@SessionScoped
public class DevolucionController implements Serializable {

    @EJB
    private co.com.regimp.operaciones.DevolucionFacade ejbFacade;
    @EJB
    private co.com.regimp.operaciones.ProductoFacade ejbProducto;
    @EJB
    private co.com.regimp.operaciones.DetalleDevolucionFacade ejbDetalle;
    private co.com.regimp.controladores.UsuarioController usucontroller;
    private List<Devolucion> items = null;
    private Devolucion selected = new Devolucion();
    private Producto producto = new Producto();
    private List<DetalleDevolucion> detalleDevolucion = new ArrayList();
    private String UnidadDeMedida = null;
    private String observaciones = null;
    private DetalleDevolucion det;
    private int cantidadDevueltos = 0;
    private int result = 0;
    private int almacen = 0;
    private Date actual = new Date();
    private int idUsuario = 0;
    private String proveedor;
    private List<Producto> productos = null;
    private List<Producto> medida;
    private int cantidadStock = 0;

    public DevolucionController() {
        this.usucontroller = new UsuarioController();
    }

    public void limpiar() {
        ejbProducto.limpiarCon();
        detalleDevolucion.clear();
        UnidadDeMedida = null;
        producto = null;
        cantidadDevueltos = 0;
        cantidadStock = 0;
        observaciones = null;
        selected.setEmpleado(null);
    }

    public void cargarCantidad(ValueChangeEvent value) {
        producto = (Producto) value.getNewValue();
        cantidadStock = ejbProducto.stock(producto.getIdProducto());
        medida = ejbProducto.UnidadesDeMedida(producto);
    }

    public void cargarProducto(ValueChangeEvent value) {
        proveedor = (String) value.getNewValue();
        productos = ejbProducto.ProductosString(proveedor);
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

    public void carrito() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath() + "/faces/Admin/devolucion/Carrito.xhtml");
    }

    public void carritoCliente() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath() + "/faces/Admin/devolucion/CarritoCliente.xhtml");
    }

    public void AgregarDevCliente() {
        det = new DetalleDevolucion();
        det.setProductoidProducto(producto);
        det.setUnidadDeMedida(UnidadDeMedida);
        det.setCantidadProductos(cantidadDevueltos);
        det.setObservaciones(observaciones);
        UnidadDeMedida = "";
        cantidadDevueltos = 0;
        observaciones = null;
        cantidadStock = 0;
        producto = null;
        detalleDevolucion.add(det);
        det = null;
    }

    public void RegistrarCliente() {
        try {
            selected.setFechaDevolucion(actual);
            selected.setProveedor("Cliente");
            selected.setEstado(true);
            ejbFacade.create(selected);
            for (DetalleDevolucion det : detalleDevolucion) {
                det.setDevolucionidDevolucion(selected);
                ejbDetalle.create(det);
                ejbProducto.AgregarCantidadProducto(det.getProductoidProducto().getIdProducto(), det.getCantidadProductos());
            }
            detalleDevolucion.clear();
            UnidadDeMedida = "";
            UnidadDeMedida = "";
            cantidadDevueltos = 0;
            observaciones = null;
            cantidadStock = 0;
            producto = null;
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void Agregar() {
        det = new DetalleDevolucion();
        det.setProductoidProducto(producto);
        det.setUnidadDeMedida(UnidadDeMedida);
        det.setCantidadProductos(cantidadDevueltos);
        det.setObservaciones(observaciones);
        almacen = ejbProducto.stock(det.getProductoidProducto().getIdProducto());

        if (almacen == 0) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El Stock Esta Vacio, No puedes devolver productos");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            ejbProducto.limpiarControl(det.getProductoidProducto().getIdProducto());
        } else {
            result = ejbProducto.QuitarCantidad(det.getProductoidProducto().getIdProducto(), det.getCantidadProductos());
            if (result <= almacen) {

                if (det.getCantidadProductos() > 0) {
                    if (result < 0) {
                        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se puede devolver más productos de los que hay en el stock");
                        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
                    } else {
                        UnidadDeMedida = null;
                        producto = null;
                        cantidadDevueltos = 0;
                        observaciones = null;
                        detalleDevolucion.add(det);
                        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto Agregado Exitosamente", "");
                        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
                    }
                } else {
                    FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Precaución", "La Cantidad Vendida debe ser mayor a 0");
                    FacesContext.getCurrentInstance().addMessage(null, facesMsg);
                }
            } else {
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se puede devolver más productos de los que hay en el stock");
                FacesContext.getCurrentInstance().addMessage(null, facesMsg);

            }
        }

        det = null;
    }

    public void limpiarAgrega() {
        ejbProducto.limpiarCon();
        UnidadDeMedida = "";
        cantidadDevueltos = 0;
        producto = null;
        cantidadStock = 0;
        cantidadDevueltos = 0;
        observaciones = null;
    }

    public void Registrar() {
        try {
            selected.setFechaDevolucion(actual);
            selected.setEstado(true);
            ejbFacade.create(selected);
            for (DetalleDevolucion det : detalleDevolucion) {
                det.setDevolucionidDevolucion(selected);
                ejbDetalle.create(det);
                ejbProducto.QuitarCantidaddef(det.getProductoidProducto().getIdProducto(), det.getCantidadProductos());
                ejbProducto.limpiarControl(det.getProductoidProducto().getIdProducto());
            }
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Productos Devueltos Exitosamente", "");
            FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
            detalleDevolucion.clear();
            UnidadDeMedida = null;
            cantidadDevueltos = 0;
            cantidadStock = 0;
            selected.setEmpleado(null);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public Devolucion getSelected() {
        return selected;
    }

    public void setSelected(Devolucion selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DevolucionFacade getFacade() {
        return ejbFacade;
    }

    public Devolucion prepareCreate() {
        selected = new Devolucion();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DevolucionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DevolucionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DevolucionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Devolucion> getItems() {
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

    public List<Devolucion> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Devolucion> getItemsAvailableSelectOne() {
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public DetalleDevolucion getDet() {
        return det;
    }

    public void setDet(DetalleDevolucion det) {
        this.det = det;
    }

    public int getCantidadDevueltos() {
        return cantidadDevueltos;
    }

    public void setCantidadDevueltos(int cantidadDevueltos) {
        this.cantidadDevueltos = cantidadDevueltos;
    }

    public List<DetalleDevolucion> getDetalleDevolucion() {
        return detalleDevolucion;
    }

    public void setDetalleDevolucion(List<DetalleDevolucion> detalleDevolucion) {
        this.detalleDevolucion = detalleDevolucion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Producto> getMedida() {
        return medida;
    }

    public void setMedida(List<Producto> medida) {
        this.medida = medida;
    }

    public int getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(int cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public Date getActual() {
        return actual;
    }

    public void setActual(Date actual) {
        this.actual = actual;
    }

    @FacesConverter(forClass = Devolucion.class)
    public static class DevolucionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DevolucionController controller = (DevolucionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "devolucionController");
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
            if (object instanceof Devolucion) {
                Devolucion o = (Devolucion) object;
                return getStringKey(o.getIdDevolucion());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Devolucion.class.getName()});
                return null;
            }
        }

    }

}
