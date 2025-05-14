
package servicios;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import controladores.ClienteController;
import controladores.ProductoController;
import controladores.VentaController;
import entidades.Cliente;
import entidades.Detalleventa;
import entidades.Producto;
import entidades.Venta;

/**
 *
 * @author jfervic933
 */

public class ServicioVenta {
    
    private static final VentaController vc = new VentaController();
    private static final ClienteController cc = new ClienteController();
    private static final ProductoController pc = new ProductoController();

    public static void insertarVentaEjemplo(){
        // Busco el cliente de id 1
        Cliente cliente = cc.findById(1);
        System.out.println("Insertando una venta para " + 
                cliente.getNombre() + " " +cliente.getNif());
        
        // Creo el objeto Venta con la fecha actual
        // y el cliente que la realiza
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        Instant ahora = fechaHoraActual.toInstant(ZoneOffset.UTC);
        Venta venta = new Venta(Date.from(ahora), cliente);
               
        // Se crean los objetos detalle venta con los productos y cantidades cualesquiera
        Detalleventa detalle = new Detalleventa();
        Producto p1 = pc.findById(1);
        detalle.setIdproducto(p1);
        detalle.setCantidad(12);
        detalle.setPrecioventa(p1.getPrecio());

        // Añade el detalle a la venta y la venta al detalle
        venta.addDetalleVenta(detalle);
        
        // Se crea otro detalle 
        Producto p2 = pc.findById(2);
        detalle = new Detalleventa();
        detalle.setIdproducto(p2);
        detalle.setCantidad(120);
        detalle.setPrecioventa(p2.getPrecio());
        
        // Añade el detalle a la venta y la venta al detalle
        venta.addDetalleVenta(detalle);

        // Añado la venta al cliente y a venta le indico que es de este cliente
        cliente.addVenta(venta);
        

        // Persisto la venta, que en cascada persistirá los detalleVenta
        vc.create(venta);

        // Actualizo el cliente con su nueva venta en el contexto de persistencia
        cc.update(cliente);
        
    }
    
    public static void borrarTodasVentas(){
        vc.deleteAll();
    }
    
    public static void mostrarTodasVentas() {
        vc.findAll().forEach(System.out::println);

    }
    
}
