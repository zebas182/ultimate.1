/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.regimp.modelos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Brayan
 */
@Entity
@Table(name = "detalle_devolucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleDevolucion.findAll", query = "SELECT d FROM DetalleDevolucion d"),
    @NamedQuery(name = "DetalleDevolucion.findByIdDetalleDevolucion", query = "SELECT d FROM DetalleDevolucion d WHERE d.idDetalleDevolucion = :idDetalleDevolucion"),
    @NamedQuery(name = "DetalleDevolucion.findByCantidadProductos", query = "SELECT d FROM DetalleDevolucion d WHERE d.cantidadProductos = :cantidadProductos"),
    @NamedQuery(name = "DetalleDevolucion.findByObservaciones", query = "SELECT d FROM DetalleDevolucion d WHERE d.observaciones = :observaciones")})
public class DetalleDevolucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalle_Devolucion")
    private Integer idDetalleDevolucion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_Productos")
    private int cantidadProductos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "Devolucion_idDevolucion", referencedColumnName = "idDevolucion")
    @ManyToOne(optional = false)
    private Devolucion devolucionidDevolucion;
    @JoinColumn(name = "Producto_idProducto", referencedColumnName = "idProducto")
    @ManyToOne(optional = false)
    private Producto productoidProducto;

    public DetalleDevolucion() {
    }

    public DetalleDevolucion(Integer idDetalleDevolucion) {
        this.idDetalleDevolucion = idDetalleDevolucion;
    }

    public DetalleDevolucion(Integer idDetalleDevolucion, int cantidadProductos, String observaciones) {
        this.idDetalleDevolucion = idDetalleDevolucion;
        this.cantidadProductos = cantidadProductos;
        this.observaciones = observaciones;
    }

    public Integer getIdDetalleDevolucion() {
        return idDetalleDevolucion;
    }

    public void setIdDetalleDevolucion(Integer idDetalleDevolucion) {
        this.idDetalleDevolucion = idDetalleDevolucion;
    }

    public int getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(int cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Devolucion getDevolucionidDevolucion() {
        return devolucionidDevolucion;
    }

    public void setDevolucionidDevolucion(Devolucion devolucionidDevolucion) {
        this.devolucionidDevolucion = devolucionidDevolucion;
    }

    public Producto getProductoidProducto() {
        return productoidProducto;
    }

    public void setProductoidProducto(Producto productoidProducto) {
        this.productoidProducto = productoidProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleDevolucion != null ? idDetalleDevolucion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleDevolucion)) {
            return false;
        }
        DetalleDevolucion other = (DetalleDevolucion) object;
        if ((this.idDetalleDevolucion == null && other.idDetalleDevolucion != null) || (this.idDetalleDevolucion != null && !this.idDetalleDevolucion.equals(other.idDetalleDevolucion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.regimp.modelos.DetalleDevolucion[ idDetalleDevolucion=" + idDetalleDevolucion + " ]";
    }
    
}
