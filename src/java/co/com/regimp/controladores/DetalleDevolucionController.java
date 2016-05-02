package co.com.regimp.controladores;

import co.com.regimp.modelos.DetalleDevolucion;
import co.com.regimp.controladores.util.JsfUtil;
import co.com.regimp.controladores.util.JsfUtil.PersistAction;
import co.com.regimp.operaciones.DetalleDevolucionFacade;

import java.io.Serializable;
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

@ManagedBean(name = "detalleDevolucionController")
@SessionScoped
public class DetalleDevolucionController implements Serializable {

    @EJB
    private co.com.regimp.operaciones.DetalleDevolucionFacade ejbFacade;
    private List<DetalleDevolucion> items = null;
    private List<DetalleDevolucion> filtered = null;
    private DetalleDevolucion selected;

    public DetalleDevolucionController() {
    }

    public DetalleDevolucion getSelected() {
        return selected;
    }

    public void setSelected(DetalleDevolucion selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DetalleDevolucionFacade getFacade() {
        return ejbFacade;
    }

    public DetalleDevolucion prepareCreate() {
        selected = new DetalleDevolucion();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DetalleDevolucionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DetalleDevolucionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DetalleDevolucionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<DetalleDevolucion> getItems() {
        
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

    public List<DetalleDevolucion> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<DetalleDevolucion> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<DetalleDevolucion> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<DetalleDevolucion> filtered) {
        this.filtered = filtered;
    }

    @FacesConverter(forClass = DetalleDevolucion.class)
    public static class DetalleDevolucionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DetalleDevolucionController controller = (DetalleDevolucionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "detalleDevolucionController");
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
            if (object instanceof DetalleDevolucion) {
                DetalleDevolucion o = (DetalleDevolucion) object;
                return getStringKey(o.getIdDetalleDevolucion());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DetalleDevolucion.class.getName()});
                return null;
            }
        }

    }

}
