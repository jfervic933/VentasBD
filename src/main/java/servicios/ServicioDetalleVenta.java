
package servicios;

import controladores.DetalleventaController;

/**
 *
 * @author jfervic933
 */
public class ServicioDetalleVenta {
    
    private static final DetalleventaController dvc = new DetalleventaController();
    
    public static void borrarTodosDetallesVentas(){
        dvc.deleteAll();
    }
    
    public static void mostrarTodosDetallesVenta() {
        dvc.findAll().forEach(System.out::println);

    }
}
