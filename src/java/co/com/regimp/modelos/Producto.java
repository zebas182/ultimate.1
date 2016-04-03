/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.regimp.modelos;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Brayan
 */
@Entity
@Table(name = "producto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findByIdProducto", query = "SELECT p FROM Producto p WHERE p.idProducto = :idProducto"),
    @NamedQuery(name = "Producto.findByNombreProducto", query = "SELECT p FROM Producto p WHERE p.nombreProducto = :nombreProducto"),
    @NamedQuery(name = "Producto.findByUnidadDeMedida", query = "SELECT p FROM Producto p WHERE p.unidadDeMedida = :unidadDeMedida"),
    @NamedQuery(name = "Producto.findByCantidadStock", query = "SELECT p FROM Producto p WHERE p.cantidadStock = :cantidadStock"),
    @NamedQuery(name = "Producto.findByEstado", query = "SELECT p FROM Producto p WHERE p.estado = :estado"),
    @NamedQuery(name = "Producto.findByProveedor", query = "SELECT p FROM Producto p WHERE p.proveedor = :proveedor")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idProducto")
    private Integer idProducto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre_Producto")
    private String nombreProducto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "unidad_De_Medida")
    private String unidadDeMedida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_Stock")
    private int cantidadStock;
    @Column(name = "control")
    private Integer control;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "proveedor")
    private String proveedor;
    @JoinColumn(name = "Categoria_idCategoria", referencedColumnName = "idCategoria")
    @ManyToOne(optional = false)
    private Categoria categoriaidCategoria;
    @JoinColumn(name = "Marca_idMarca", referencedColumnName = "idMarca")
    @ManyToOne(optional = false)
    private Marca marcaidMarca;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoidProducto")
    private Collection<DetalleDevolucion> detalleDevolucionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoidProducto")
    private Collection<DetalleDespacho> detalleDespachoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoidProducto")
    private Collection<DetallePedido> detallePedidoCollection;

    public Producto() {
    }

    public Producto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Producto(Integer idProducto, String nombreProducto, String unidadDeMedida, int cantidadStock, boolean estado, int iva, String proveedor) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.unidadDeMedida = unidadDeMedida;
        this.cantidadStock = cantidadStock;
        this.estado = estado;
        this.proveedor = proveedor;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(String unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public int getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(int cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public Categoria getCategoriaidCategoria() {
        return categoriaidCategoria;
    }

    public void setCategoriaidCategoria(Categoria categoriaidCategoria) {
        this.categoriaidCategoria = categoriaidCategoria;
    }

    public Marca getMarcaidMarca() {
        return marcaidMarca;
    }

    public void setMarcaidMarca(Marca marcaidMarca) {
        this.marcaidMarca = marcaidMarca;
    }

    @XmlTransient
    public Collection<DetalleDevolucion> getDetalleDevolucionCollection() {
        return detalleDevolucionCollection;
    }

    public void setDetalleDevolucionCollection(Collection<DetalleDevolucion> detalleDevolucionCollection) {
        this.detalleDevolucionCollection = detalleDevolucionCollection;
    }

    @XmlTransient
    public Collection<DetalleDespacho> getDetalleDespachoCollection() {
        return detalleDespachoCollection;
    }

    public void setDetalleDespachoCollection(Collection<DetalleDespacho> detalleDespachoCollection) {
        this.detalleDespachoCollection = detalleDespachoCollection;
    }

    @XmlTransient
    public Collection<DetallePedido> getDetallePedidoCollection() {
        return detallePedidoCollection;
    }

    public void setDetallePedidoCollection(Collection<DetallePedido> detallePedidoCollection) {
        this.detallePedidoCollection = detallePedidoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProducto != null ? idProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.idProducto == null && other.idProducto != null) || (this.idProducto != null && !this.idProducto.equals(other.idProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.regimp.modelos.Producto[ idProducto=" + idProducto + " ]";
    }

    public Integer getControl() {
        return control;
    }

    public void setControl(Integer control) {
        this.control = control;
    }

}
