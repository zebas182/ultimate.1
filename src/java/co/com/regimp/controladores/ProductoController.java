package co.com.regimp.controladores;

import co.com.regimp.modelos.Producto;
import co.com.regimp.controladores.util.JsfUtil;
import co.com.regimp.controladores.util.JsfUtil.PersistAction;
import co.com.regimp.modelos.Proveedor;
import co.com.regimp.operaciones.ProductoFacade;
import Reportes.ReportesRegimp;
import java.io.IOException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import servlets.reporteProductos;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@ManagedBean(name = "productoController")
@ViewScoped
public class ProductoController implements Serializable {

    @EJB
    private co.com.regimp.operaciones.ProductoFacade ejbFacade;
    @EJB
    private co.com.regimp.operaciones.ProveedorFacade ejbProveedor;
    private Reportes.ReportesRegimp reportesRegimp;
    private List<Producto> items = null;
    private Producto selected;
    private List<Proveedor> listProveedor = null;
    private List<Producto> filteredProductos=null;
    
    public ProductoController() {
        selected=new Producto();
        reportesRegimp = new ReportesRegimp();
    }

   
    public void ReporteStockProducto() throws SQLException, JRException, IOException, NamingException {
        //Fill Map with params values
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        ServletOutputStream out = response.getOutputStream();
        
        //Connect with local datasource
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("jdbc_Regimp");
        Connection conexion = null;
        conexion = ds.getConnection();
        conexion.setAutoCommit(true);
//        JasperReport reporte = null;
//        reporte = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\alber\\Documents\\NetBeansProjects\\UltimatePrueba\\ultimate.1\\web\\WEB-INF\\StockProducto.jasper");
//        
        response.addHeader("Content-disposition",
                "attachment; filename=reporte.pdf");
        response.setContentType("application/pdf");

        JasperPrint jasperPrint = JasperFillManager.fillReport("C:\\Users\\alber\\Documents\\NetBeansProjects\\UltimatePrueba\\ultimate.1\\src\\java\\Reportes\\StockProducto.jasper", null, conexion);
        JRExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        exporter.exportReport();
        
        System.out.println("cosa");
     
        
        FacesContext.getCurrentInstance().responseComplete();
        
    }

//    public void reportes() {
//        reportesRegimp.ReporteStockProducto();
//    }
    public Producto getSelected() {
        return selected;
    }

    public void setSelected(Producto selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProductoFacade getFacade() {
        return ejbFacade;
    }

    public Producto prepareCreate() {
        selected = new Producto();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProductoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProductoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProductoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Producto> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            selected.setEstado(true);
            selected.setControl(0);
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

    public List<Producto> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Producto> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Proveedor> getItemsAvailableSelectOneProveedor() {
        return ejbProveedor.findAll();
    }

    public List<Proveedor> getListProveedor() {
        return listProveedor;
    }

    public void setListProveedor(List<Proveedor> listProveedor) {
        this.listProveedor = listProveedor;
    }

    public List<Producto> getFilteredProductos() {
        return filteredProductos;
    }

    public void setFilteredProductos(List<Producto> filteredProductos) {
        this.filteredProductos = filteredProductos;
    }

    @FacesConverter(forClass = Producto.class)
    public static class ProductoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductoController controller = (ProductoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productoController");
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
            if (object instanceof Producto) {
                Producto o = (Producto) object;
                return getStringKey(o.getIdProducto());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Producto.class.getName()});
                return null;
            }
        }

    }

}
