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
@Table(name = "detalle_despacho")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleDespacho.findAll", query = "SELECT d FROM DetalleDespacho d"),
    @NamedQuery(name = "DetalleDespacho.findByIdDetalleDespacho", query = "SELECT d FROM DetalleDespacho d WHERE d.idDetalleDespacho = :idDetalleDespacho"),
    @NamedQuery(name = "DetalleDespacho.findByUnidadDeMedida", query = "SELECT d FROM DetalleDespacho d WHERE d.unidadDeMedida = :unidadDeMedida"),
    @NamedQuery(name = "DetalleDespacho.findByPrecioVentaUnidad", query = "SELECT d FROM DetalleDespacho d WHERE d.precioVentaUnidad = :precioVentaUnidad"),
    @NamedQuery(name = "DetalleDespacho.findByCantidadVendida", query = "SELECT d FROM DetalleDespacho d WHERE d.cantidadVendida = :cantidadVendida"),
    @NamedQuery(name = "DetalleDespacho.findByPrecioTotal", query = "SELECT d FROM DetalleDespacho d WHERE d.precioTotal = :precioTotal")})
public class DetalleDespacho implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalle_Despacho")
    private Integer idDetalleDespacho;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "unidad_De_Medida")
    private String unidadDeMedida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_Venta_Unidad")
    private int precioVentaUnidad;
    @Column(name = "cantidad_Vendida")
    private Integer cantidadVendida;
    @Column(name = "precio_Total")
    private Integer precioTotal;
    @JoinColumn(name = "Despacho_idDespacho", referencedColumnName = "idDespacho")
    @ManyToOne(optional = false)
    private Despacho despachoidDespacho;
    @JoinColumn(name = "Producto_idProducto", referencedColumnName = "idProducto")
    @ManyToOne(optional = false)
    private Producto productoidProducto;

    public DetalleDespacho() {
    }

    public DetalleDespacho(Integer idDetalleDespacho) {
        this.idDetalleDespacho = idDetalleDespacho;
    }

    public DetalleDespacho(Integer idDetalleDespacho, String unidadDeMedida, int precioVentaUnidad) {
        this.idDetalleDespacho = idDetalleDespacho;
        this.unidadDeMedida = unidadDeMedida;
        this.precioVentaUnidad = precioVentaUnidad;
    }

    public Integer getIdDetalleDespacho() {
        return idDetalleDespacho;
    }

    public void setIdDetalleDespacho(Integer idDetalleDespacho) {
        this.idDetalleDespacho = idDetalleDespacho;
    }

    public String getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(String unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public int getPrecioVentaUnidad() {
        return precioVentaUnidad;
    }

    public void setPrecioVentaUnidad(int precioVentaUnidad) {
        this.precioVentaUnidad = precioVentaUnidad;
    }

    public Integer getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(Integer cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public Integer getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Integer precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Despacho getDespachoidDespacho() {
        return despachoidDespacho;
    }

    public void setDespachoidDespacho(Despacho despachoidDespacho) {
        this.despachoidDespacho = despachoidDespacho;
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
        hash += (idDetalleDespacho != null ? idDetalleDespacho.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleDespacho)) {
            return false;
        }
        DetalleDespacho other = (DetalleDespacho) object;
        if ((this.idDetalleDespacho == null && other.idDetalleDespacho != null) || (this.idDetalleDespacho != null && !this.idDetalleDespacho.equals(other.idDetalleDespacho))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.regimp.modelos.DetalleDespacho[ idDetalleDespacho=" + idDetalleDespacho + " ]";
    }
    
}
