package co.com.regimp.controladores;

import Encripcion.Encriptar;
import co.com.regimp.modelos.Usuario;
import co.com.regimp.controladores.util.JsfUtil;
import co.com.regimp.controladores.util.JsfUtil.PersistAction;
import co.com.regimp.operaciones.EmpleadoFacade;
import co.com.regimp.operaciones.UsuarioFacade;
import java.io.IOException;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
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
import javax.servlet.http.HttpSession;

@ManagedBean(name = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    @EJB
    private co.com.regimp.operaciones.UsuarioFacade ejbFacade;
    @EJB
    EmpleadoFacade ejbEmpleado;
    private List<Usuario> items = null;
    private Usuario selected = new Usuario();
    private String contrasenaEncriptada;
    private Usuario u = new Usuario();
    EmpleadoController e = new EmpleadoController();
    private String nuevaClave;

    public UsuarioController() {
    }

    public void generarNuevaClave(Usuario id) {
        try {
            String nuevaClave = UUID.randomUUID().toString();
            System.out.println(nuevaClave);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public void recibirUsuario() {
        try {
            int u = ejbFacade.cambioContrasena(selected.getNombreUsuario());
            if (u == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "¡ERROR!", "El usuario o la contraseña no coinciden con ninguna cuenta"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "¡Actualizado!", "Su contraseña ha sido cambiada"));
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public String validado() {
        try {
            selected.setContrasena(Encriptar.encriptaEnMD5(contrasenaEncriptada));
            contrasenaEncriptada = null;
            u = ejbFacade.listaDeUsuario(selected.getNombreUsuario(), selected.getContrasena());
            if (u != null) {
                if (u.getRolidRol().getIdRol().equals(1)) {
                    return "welcomePrimefaces?faces-redirect=true";
                }
                if (u.getRolidRol().getIdRol().equals(2)) {
                    return "welcomePrimefaces?faces-redirect=true";
                }

            }
        } catch (Exception e) {
            throw e;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "¡ERROR!", "El usuario o la contraseña no coinciden con ninguna cuenta"));
        return null;

    }

    public void validarSesion() {
        if (u.getNombreUsuario() == null) {
            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/faces/Login.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cerrarSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(false);
        HttpSession httpSession = (HttpSession) session;
        httpSession.invalidate();
    }

    public Usuario getSelected() {
        return selected;
    }

    public void setSelected(Usuario selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UsuarioFacade getFacade() {
        return ejbFacade;
    }

    public Usuario prepareCreate() {
        selected = new Usuario();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
    }

    public void destroy() {
        ejbFacade.Eliminar(selected.getIdUsuario());
    }

    public List<Usuario> getItems() {
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

    public List<Usuario> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Usuario> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public co.com.regimp.operaciones.UsuarioFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(co.com.regimp.operaciones.UsuarioFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public String getContrasenaEncriptada() {
        return contrasenaEncriptada;
    }

    public void setContrasenaEncriptada(String contrasenaEncriptada) {
        this.contrasenaEncriptada = contrasenaEncriptada;
    }

    public Usuario getU() {
        return u;
    }

    public void setU(Usuario u) {
        this.u = u;
    }

    @FacesConverter(forClass = Usuario.class)
    public static class UsuarioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuarioController controller = (UsuarioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuarioController");
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
            if (object instanceof Usuario) {
                Usuario o = (Usuario) object;
                return getStringKey(o.getIdUsuario());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Usuario.class.getName()});
                return null;
            }
        }

    }

}
