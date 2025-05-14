
package controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import entidades.Cliente;

/**
 * @author jfervic933
 */
public class ClienteController {

    private final EntityManagerFactory emf;

    public ClienteController() {
        // Nombre de la unidad de persistencia definida en persistence.xml
        this.emf = Persistence.createEntityManagerFactory("ventas");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Crea un nuevo cliente en la base de datos.
     *
     * @param cliente El cliente a crear.
     */
    public void create(Cliente cliente) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear el cliente", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Busca un cliente por su ID.
     *
     * @param id El ID del cliente.
     * @return El cliente encontrado o null si no existe.
     */
    public Cliente findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todos los clientes de la base de datos.
     *
     * @return Una lista de clientes.
     */
    public List<Cliente> findAll() {
        EntityManager em = getEntityManager();
        try {
            // Usa la named query definida en la entidad Cliente
            return em.createNamedQuery("Cliente.findAll", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza un cliente existente.
     *
     * @param cliente El cliente a actualizar.
     */
    public void update(Cliente cliente) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cliente);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar el cliente", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Elimina un cliente de la base de datos.
     *
     * @param id El ID del cliente a eliminar.
     */
    public void delete(Integer id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                em.remove(cliente);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar el cliente", ex);
        } finally {
            em.close();
        }
    }

    public void deleteAll() {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Ejemplo de uso de una consulta nativa para eliminar todos los registros
            // de la tabla cliente y reiniciar el contador de autoincremento
            // Una native query es una consulta SQL que se ejecuta directamente en la base de datos
            // sin pasar por el mapeo de entidades de JPA
            em.createNativeQuery("delete from cliente").executeUpdate();
            em.createNativeQuery("alter table empresa_ventas.cliente AUTO_INCREMENT = 1").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar todos los clientes", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Cierra el EntityManagerFactory cuando ya no se necesita.
     */
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
