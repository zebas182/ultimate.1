/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.regimp.modelos;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Brayan
 */
@Entity
@Table(name = "detalle_pedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallePedido.findAll", query = "SELECT d FROM DetallePedido d"),
    @NamedQuery(name = "DetallePedido.findByIdDetallePedido", query = "SELECT d FROM DetallePedido d WHERE d.idDetallePedido = :idDetallePedido"),
    @NamedQuery(name = "DetallePedido.findByUnidadDeMedida", query = "SELECT d FROM DetallePedido d WHERE d.unidadDeMedida = :unidadDeMedida"),
    @NamedQuery(name = "DetallePedido.findByPrecioUnidadCompra", query = "SELECT d FROM DetallePedido d WHERE d.precioUnidadCompra = :precioUnidadCompra"),
    @NamedQuery(name = "DetallePedido.findByCantidadPedidos", query = "SELECT d FROM DetallePedido d WHERE d.cantidadPedidos = :cantidadPedidos"),
    @NamedQuery(name = "DetallePedido.findByFechaDeVencimiento", query = "SELECT d FROM DetallePedido d WHERE d.fechaDeVencimiento = :fechaDeVencimiento"),
    @NamedQuery(name = "DetallePedido.findByCantidadEntregados", query = "SELECT d FROM DetallePedido d WHERE d.cantidadEntregados = :cantidadEntregados"),
    @NamedQuery(name = "DetallePedido.findByFechaLote", query = "SELECT d FROM DetallePedido d WHERE d.fechaLote = :fechaLote"),
    @NamedQuery(name = "DetallePedido.findByPrecioTotal", query = "SELECT d FROM DetallePedido d WHERE d.precioTotal = :precioTotal")})
public class DetallePedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalle_Pedido")
    private Integer idDetallePedido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "unidad_De_Medida")
    private String unidadDeMedida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_Unidad_Compra")
    private int precioUnidadCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_Pedidos")
    private int cantidadPedidos;
    @Column(name = "fecha_De_Vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaDeVencimiento;
    @Column(name = "cantidad_Entregados")
    private Integer cantidadEntregados;
    @Column(name = "fecha_Lote")
    @Temporal(TemporalType.DATE)
    private Date fechaLote;
    @Column(name = "Precio_Total")
    private Integer precioTotal;
    @JoinColumn(name = "Pedido_idPedido", referencedColumnName = "idPedido")
    @ManyToOne(optional = false)
    private Pedido pedidoidPedido;
    @JoinColumn(name = "Producto_idProducto", referencedColumnName = "idProducto")
    @ManyToOne(optional = false)
    private Producto productoidProducto;

    public DetallePedido() {
    }

    public DetallePedido(Integer idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public DetallePedido(Integer idDetallePedido, String unidadDeMedida, int precioUnidadCompra, int cantidadPedidos) {
        this.idDetallePedido = idDetallePedido;
        this.unidadDeMedida = unidadDeMedida;
        this.precioUnidadCompra = precioUnidadCompra;
        this.cantidadPedidos = cantidadPedidos;
    }

    public Integer getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(Integer idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public String getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(String unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public int getPrecioUnidadCompra() {
        return precioUnidadCompra;
    }

    public void setPrecioUnidadCompra(int precioUnidadCompra) {
        this.precioUnidadCompra = precioUnidadCompra;
    }

    public int getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(int cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }

    public Date getFechaDeVencimiento() {
        return fechaDeVencimiento;
    }

    public void setFechaDeVencimiento(Date fechaDeVencimiento) {
        this.fechaDeVencimiento = fechaDeVencimiento;
    }

    public Integer getCantidadEntregados() {
        return cantidadEntregados;
    }

    public void setCantidadEntregados(Integer cantidadEntregados) {
        this.cantidadEntregados = cantidadEntregados;
    }

    public Date getFechaLote() {
        return fechaLote;
    }

    public void setFechaLote(Date fechaLote) {
        this.fechaLote = fechaLote;
    }

    public Integer getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Integer precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Pedido getPedidoidPedido() {
        return pedidoidPedido;
    }

    public void setPedidoidPedido(Pedido pedidoidPedido) {
        this.pedidoidPedido = pedidoidPedido;
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
        hash += (idDetallePedido != null ? idDetallePedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallePedido)) {
            return false;
        }
        DetallePedido other = (DetallePedido) object;
        if ((this.idDetallePedido == null && other.idDetallePedido != null) || (this.idDetallePedido != null && !this.idDetallePedido.equals(other.idDetallePedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.regimp.modelos.DetallePedido[ idDetallePedido=" + idDetallePedido + " ]";
    }
    
}
