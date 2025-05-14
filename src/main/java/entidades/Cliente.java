
package entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

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

/**
 *
 * @author jfervic933
 */
@Entity
@Table(name = "cliente")

// Las named queries son consultas predefinidas que se pueden usar
// en el código sin necesidad de escribir la consulta completa
// Están escritas en el formato JPQL (Java Persistence Query Language)
// No son objeto de estudio en nuestro curso, pero se incluyen para que
// veas que existen y que se pueden usar
// Las siguientes consultas son sencillas y fáciles de entender
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findById", query = "SELECT c FROM Cliente c WHERE c.id = :id"),
    @NamedQuery(name = "Cliente.findByNombre", query = "SELECT c FROM Cliente c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cliente.findByNif", query = "SELECT c FROM Cliente c WHERE c.nif = :nif")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "nif")
    private String nif;
    
    // Cada Venta tiene un atributo Cliente que realiza esa venta
    // Relación bidireccional con Venta. Esta entidad no es propietaria
    // de la relación (no tiene la clave foránea de Venta)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.PERSIST) 
    private Collection<Venta> ventaCollection;

    public Cliente() {
    }

    public Cliente(Integer id) {
        this.id = id;
    }

    public Cliente(Integer id, String nif) {
        this.id = id;
        this.nif = nif;
        this.ventaCollection = new ArrayList<>();
    }
    
    public Cliente(String nombre, String nif){
        this.nombre = nombre;
        this.nif = nif;
        this.ventaCollection = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Collection<Venta> getVentaCollection() {
        return ventaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        String tmp = "";
        for (Venta venta : ventaCollection) {
            tmp+=venta+"\n";
        }
        return "Cliente{" + "id=" + id + ", nombre=" + nombre + ", nif=" + nif + ", ventas cliente=\n" + tmp + '}';
    }
    
    public void setVentaCollection(Collection<Venta> ventaCollection) {
        this.ventaCollection = ventaCollection;
        for (Venta venta : ventaCollection) {
            venta.setIdcliente(this);
        }
    }
    
    public void addVenta(Venta venta){
        this.ventaCollection.add(venta);
        venta.setIdcliente(this);
    }
    
    public void removeVenta(Venta venta){
        this.ventaCollection.remove(venta);
        venta.setIdcliente(null);
    }
  
}
