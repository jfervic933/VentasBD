
package daw;

import java.time.LocalDateTime;

import controladores.ClienteController;
import controladores.DetalleventaController;
import controladores.VentaController;
import entidades.Cliente;
import entidades.Detalleventa;
import entidades.Venta;
import servicios.ServicioCliente;
import servicios.ServicioDetalleVenta;
import servicios.ServicioProducto;
import servicios.ServicioVenta;

public class VentasBD {

    // Lo ideal sería crear un servicio para cada controlador, pero
    // para este ejercicio no es necesario. Para simplificar el código usamos
    // directamente los controladores
    private static final ClienteController cc = new ClienteController();
    // private static final ProductoController pc = new ProductoController(); // No se usa en este main
    private static final VentaController vc = new VentaController();
    private static final DetalleventaController dvc = new DetalleventaController();

    public static void main(String[] args) {

        // Preparamos la base de datos. En cada nueva ejecución se borran los
        // datos que hubiera, para facilitar la depuración
        prepararBaseDatos();

        System.out.println("Clientes en la base de datos ----------- ");
        ServicioCliente.mostrarTodosClientes();
        System.out.println("Productos en la base de datos ----------- ");
        ServicioProducto.mostrarTodosProductos();
        System.out.println("Ventas en la base de datos ----------- ");
        ServicioVenta.mostrarTodasVentas();

        // Insertamos una venta de ejemplo -----------------------------------------
        ServicioVenta.insertarVentaEjemplo();
        // Se muestran los datos de la base de datos
        System.out.println("Clientes en la base de datos con sus ventas ----------- ");
        ServicioCliente.mostrarTodosClientes();


        // Modificar el cliente con id 2 -------------------------------------------
        // Se busca el cliente por su id. Se modifica el nombre del cliente
        Cliente cliente = cc.findById(2);
        cliente.setNombre("nombre nuevo");
        cc.update(cliente);
        // Se muestran los datos de la base de datos
        System.out.println("Clientes en la base de datos con nombre modificado ----------- ");
        ServicioCliente.mostrarTodosClientes();


        // Modificar la fecha de la venta 1 del cliente 1 -------------------------
        // Se busca el cliente por su id
        Cliente clienteModificar = cc.findById(1);
        // Obtiene la venta del cliente a modificar, en este caso la primera venta, como
        // ejemplo
        Venta venta = clienteModificar.getVentaCollection().stream().toList().get(0);
        // Se modifica la fecha de la venta
        // Crear un LocalDateTime con la fecha específica que queremos poner (1 de enero
        // de 2000, 12:00:00 UTC)
        LocalDateTime localDateTime = LocalDateTime.of(2000, 1, 1, 12, 0, 0);
        // Actualiza la fecha de la venta
        venta.setFecha(localDateTime);
        // Actualiza la venta en la base de datos
        vc.update(venta);
        // Se muestran los datos de la base de datos
        System.out.println("Clientes en la base de datos con fecha de venta modificada ----------- ");
        ServicioCliente.mostrarTodosClientes();


        // Modificar la cantidad de producto del primer detalle de la venta 1 del
        // cliente 1 -------------------------
        // Se busca el cliente por su id
        Cliente clienteModificar2 = cc.findById(1);
        // Se busca la venta del cliente a modificar, en este caso la primera venta, a
        // modo de ejemplo
        Venta venta2 = clienteModificar2.getVentaCollection().stream().toList().get(0);
        // Se busca el detalle de la venta a modificar, en este caso el primer detalle,
        // a modo de ejemplo
        Detalleventa detalle = venta2.getDetalleventaCollection().stream().toList().get(0);
        // Se modifica la cantidad del detalle
        detalle.setCantidad(100000);
        // Actualiza el detalle en la base de datos
        dvc.update(detalle);
        // Se muestran los datos de la base de datos
        System.out.println("Clientes en la base de datos con detalle venta modificado ----------- ");
        ServicioCliente.mostrarTodosClientes();

        // Eliminar el primer detalle de la venta 1 del cliente 1  -------------------------
        // Se busca el cliente por su id
        Cliente clienteModificar3 = cc.findById(1);
        // Se busca la venta del cliente a modificar, en este caso la primera venta
        Venta venta3 = clienteModificar3.getVentaCollection().stream().toList().get(0);
        // Se busca el detalle de la venta a eliminar, en este caso el primer detalle
        // a modo de ejemplo
        Detalleventa detalle2 = venta3.getDetalleventaCollection().stream().toList().get(0);
        // Se elimina el detalle en la venta para mantener la relación bidireccional
        venta3.removeDetalleVenta(detalle2);
        // Se elimina el detalle de la base de datos
        dvc.delete(detalle2.getId());
        // Se actualiza la venta en la base de datos
        vc.update(venta3);
        // Se muestran los datos de la base de datos
        System.out.println("Clientes en la base de datos con detalle venta eliminado ----------- ");
        ServicioCliente.mostrarTodosClientes();
    }

    private static void borrarTodo() {
        // Empieza borrando los detalles venta, que contienen las claves foráneas
        // producto y venta
        ServicioDetalleVenta.borrarTodosDetallesVentas();
        // Sigue borrando las ventas, que contiene clave foránea a cliente
        ServicioVenta.borrarTodasVentas();
        // Ahora que no hay claves foráneas se pueden borrar clientes y productos
        ServicioCliente.borrarTodosClientes();
        ServicioProducto.borrarTodosProductos();
        System.out
                .println("Se han borrado todos los registros e inicializado las claves primarias de todas las tablas");
    }

    // Se borra todo, reinicia pk e inserta datos de ejemplo
    // de productos y clientes
    private static void prepararBaseDatos() {
        borrarTodo();
        ServicioCliente.insertarClientesEjemplo();
        ServicioProducto.insertarProductosEjemplo();
    }

}
