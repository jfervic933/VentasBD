
package controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import entidades.Detalleventa;

/**
 * @author jfervic933
 */
public class DetalleventaController {

    private final EntityManagerFactory emf;

    public DetalleventaController() {
        // Nombre de la unidad de persistencia definida en persistence.xml
        this.emf = Persistence.createEntityManagerFactory("ventas");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Crea un nuevo detalle de venta en la base de datos.
     *
     * @param detalleventa El detalle de venta a crear.
     */
    public void create(Detalleventa detalleventa) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(detalleventa);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear el detalle de venta", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Busca un detalle de venta por su ID.
     *
     * @param id El ID del detalle de venta.
     * @return El detalle de venta encontrado o null si no existe.
     */
    public Detalleventa findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleventa.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todos los detalles de venta de la base de datos.
     *
     * @return Una lista de detalles de venta.
     */
    public List<Detalleventa> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Detalleventa.findAll", Detalleventa.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza un detalle de venta existente.
     *
     * @param detalleventa El detalle de venta a actualizar.
     */
    public void update(Detalleventa detalleventa) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(detalleventa);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar el detalle de venta", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Elimina un detalle de venta de la base de datos.
     *
     * @param id El ID del detalle de venta a eliminar.
     */
    public void delete(Integer id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Detalleventa detalleventa = em.find(Detalleventa.class, id);
            if (detalleventa != null) {
                em.remove(detalleventa);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar el detalle de venta", ex);
        } finally {
            em.close();
        }
    }

    public void deleteAll() {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createNativeQuery("delete from detalleventa").executeUpdate();
            // Reinicia el auto increment de la tabla
            em.createNativeQuery("alter table empresa_ventas.detalleventa AUTO_INCREMENT = 1").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar todos los detalleventa", ex);
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
