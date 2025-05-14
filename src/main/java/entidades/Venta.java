package entidades;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jfervic933
 */
@Entity
@Table(name = "venta")
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v"),
    @NamedQuery(name = "Venta.findById", query = "SELECT v FROM Venta v WHERE v.id = :id"),
    @NamedQuery(name = "Venta.findByFecha", query = "SELECT v FROM Venta v WHERE v.fecha = :fecha")})
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    // En este caso Venta es la propietaria de la relación con Cliente @JoinColumn
    // Esta tabla tiene la clave foránea a Cliente
    @JoinColumn(name = "idcliente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cliente cliente;

    // Cada DetalleVenta tiene un atributo llamado idventa de la Venta asociada
    // Relación bidireccional con Venta. Esta entidad no es propietaria
    // de la relación (no tiene la clave foránea a DetalleVenta)
    // cascade = CascadeType.Persist hace que las operaciones de persistencia
    // se propaguen a DetalleVenta
    @OneToMany(mappedBy = "idventa", cascade = CascadeType.PERSIST)
    private Collection<Detalleventa> detalleventaCollection;

    public Venta() {
    }

    public Venta(Integer id) {
        this.id = id;
    }

    // Este lo creo yo
    public Venta(Date fecha, Cliente cliente) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.detalleventaCollection = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    // getFecha en formato LocalDateTime para poder usarlo en Java 8
    // y posteriores
    public LocalDateTime getFechaLocalDateTime() {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        // Convertir LocalDateTime a Date
        this.fecha = Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Cliente getIdcliente() {
        return cliente;
    }

    public void setIdcliente(Cliente idcliente) {
        this.cliente = idcliente;
    }

    public Collection<Detalleventa> getDetalleventaCollection() {
        return detalleventaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        String tmp = "";
        for (Detalleventa detalle : detalleventaCollection) {
            tmp += detalle + "\n";
        }
        return "Venta{" + "id=" + id + ", fecha=" + fecha + ", idcliente=" + cliente.getNif()
                + ", detalleventaCollection=\n" + tmp + '}';
    }

    /*
            En JPA hay que gestionar las relaciones a nivel de aplicación.
            Hay que sincronizar ambos lados en una relación bidireccional. 
            Esto significa que si añades una entidad relacionada a la colección
            de una entidad propietaria, también debes establecer la referencia 
            a la entidad propietaria en la entidad relacionada.
     */
    public void setDetalleventaCollection(Collection<Detalleventa> detalleventaCollection) {
        this.detalleventaCollection = detalleventaCollection;
        // A cada detalleVenta de la lista le indico que su venta es esta
        // Para que haya una sincronización bidireccional
        for (Detalleventa detalleventa : detalleventaCollection) {
            detalleventa.setVenta(this);
        }
    }

    // Añade un detalle venta a la colección de esta venta
    public void addDetalleVenta(Detalleventa detalleVenta) {
        this.detalleventaCollection.add(detalleVenta);
        // Sincronización bidireccional con detalleVenta
        detalleVenta.setVenta(this);
    }

    public void removeDetalleVenta(Detalleventa detalleVenta) {
        // Se borra el detalleVenta de esta venta
        this.detalleventaCollection.remove(detalleVenta);
        // Se sincroniza la venta rompiendo la relación bidireccional
        detalleVenta.setVenta(null);
    }

}
