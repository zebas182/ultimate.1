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
@Table(name = "despacho")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Despacho.findAll", query = "SELECT d FROM Despacho d"),
    @NamedQuery(name = "Despacho.findByIdDespacho", query = "SELECT d FROM Despacho d WHERE d.idDespacho = :idDespacho"),
    @NamedQuery(name = "Despacho.findByFechaDespacho", query = "SELECT d FROM Despacho d WHERE d.fechaDespacho = :fechaDespacho"),
    @NamedQuery(name = "Despacho.findByEstado", query = "SELECT d FROM Despacho d WHERE d.estado = :estado"),
    @NamedQuery(name = "Despacho.findByEmpleado", query = "SELECT d FROM Despacho d WHERE d.empleado = :empleado")})
public class Despacho implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDespacho")
    private Integer idDespacho;
    @Column(name = "fecha_Despacho")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDespacho;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "empleado")
    private String empleado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "despachoidDespacho")
    private Collection<DetalleDespacho> detalleDespachoCollection;

    public Despacho() {
    }

    public Despacho(Integer idDespacho) {
        this.idDespacho = idDespacho;
    }

    public Despacho(Integer idDespacho, Date fechaDespacho, boolean estado, String empleado) {
        this.idDespacho = idDespacho;
        this.fechaDespacho = fechaDespacho;
        this.estado = estado;
        this.empleado = empleado;
    }

    public Integer getIdDespacho() {
        return idDespacho;
    }

    public void setIdDespacho(Integer idDespacho) {
        this.idDespacho = idDespacho;
    }

    public Date getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(Date fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    @XmlTransient
    public Collection<DetalleDespacho> getDetalleDespachoCollection() {
        return detalleDespachoCollection;
    }

    public void setDetalleDespachoCollection(Collection<DetalleDespacho> detalleDespachoCollection) {
        this.detalleDespachoCollection = detalleDespachoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDespacho != null ? idDespacho.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Despacho)) {
            return false;
        }
        Despacho other = (Despacho) object;
        if ((this.idDespacho == null && other.idDespacho != null) || (this.idDespacho != null && !this.idDespacho.equals(other.idDespacho))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.regimp.modelos.Despacho[ idDespacho=" + idDespacho + " ]";
    }
    
}
