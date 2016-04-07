/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.regimp.modelos;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Brayan
 */
@Entity
@Table(name = "devolucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Devolucion.findAll", query = "SELECT d FROM Devolucion d"),
    @NamedQuery(name = "Devolucion.findByIdDevolucion", query = "SELECT d FROM Devolucion d WHERE d.idDevolucion = :idDevolucion"),
    @NamedQuery(name = "Devolucion.findByFechaDevolucion", query = "SELECT d FROM Devolucion d WHERE d.fechaDevolucion = :fechaDevolucion"),
    @NamedQuery(name = "Devolucion.findByEstado", query = "SELECT d FROM Devolucion d WHERE d.estado = :estado")})
public class Devolucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDevolucion")
    private Integer idDevolucion;
    @Column(name = "fecha_Devolucion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDevolucion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "empleado")
    private String empleado;
    @Size(min = 1, max = 45)
    @Column(name = "proveedor")
    private String proveedor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "devolucionidDevolucion")
    private Collection<DetalleDevolucion> detalleDevolucionCollection;

    public Devolucion() {
    }

    public Devolucion(Integer idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public Devolucion(Integer idDevolucion, Date fechaDevolucion, boolean estado) {
        this.idDevolucion = idDevolucion;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    public Integer getIdDevolucion() {
        return idDevolucion;
    }

    public void setIdDevolucion(Integer idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public boolean getEstado() {
        return estado;
    }

        public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }


    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<DetalleDevolucion> getDetalleDevolucionCollection() {
        return detalleDevolucionCollection;
    }

    public void setDetalleDevolucionCollection(Collection<DetalleDevolucion> detalleDevolucionCollection) {
        this.detalleDevolucionCollection = detalleDevolucionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDevolucion != null ? idDevolucion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Devolucion)) {
            return false;
        }
        Devolucion other = (Devolucion) object;
        if ((this.idDevolucion == null && other.idDevolucion != null) || (this.idDevolucion != null && !this.idDevolucion.equals(other.idDevolucion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.regimp.modelos.Devolucion[ idDevolucion=" + idDevolucion + " ]";
    }

}
