
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jfervic933
 */
@Entity
@Table(name = "producto")
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findById", query = "SELECT p FROM Producto p WHERE p.id = :id"),
    @NamedQuery(name = "Producto.findByDescripcion", query = "SELECT p FROM Producto p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Producto.findByPrecio", query = "SELECT p FROM Producto p WHERE p.precio = :precio"),
    @NamedQuery(name = "Producto.findByStock", query = "SELECT p FROM Producto p WHERE p.stock = :stock")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "precio")
    private Float precio;
    @Column(name = "stock")
    private Integer stock;
    
    // Se omite la relación de Producto hacia detalleVenta para que no sea
    // bidireccional, simplemente con fines didácticos para que comprobéis
    // que no siempre es obligatorio que las relaciones sean en ambas direcciones
    // Al comantar esta relación, no se cargarían los detalleVenta asociados
    // a este producto
    //    @OneToMany(mappedBy = "idproducto")
    //    private Collection<Detalleventa> detalleventaCollection;
    
    public Producto() {
    }

    public Producto(Integer id) {
        this.id = id;
    }

    public Producto(String descripcion, float precio, int stock) {
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
//        this.detalleventaCollection = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

//    public Collection<Detalleventa> getDetalleventaCollection() {
//        return detalleventaCollection;
//    }
//
//    public void setDetalleventaCollection(Collection<Detalleventa> detalleventaCollection) {
//        this.detalleventaCollection = detalleventaCollection;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Producto{" + "id=" + id + ", descripcion=" + descripcion + ", precio=" + precio + ", stock=" + stock + "}";
    }

}
