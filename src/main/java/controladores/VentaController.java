
package controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import entidades.Venta;

/**
 * @author jfervic933
 */
public class VentaController {

    private final EntityManagerFactory emf;

    public VentaController() {
        // Nombre de la unidad de persistencia definida en persistence.xml
        this.emf = Persistence.createEntityManagerFactory("ventas");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Crea una nueva venta en la base de datos.
     * @param venta La venta a crear.
     */
    public void create(Venta venta) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(venta);
            tx.commit();
            
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear la venta", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Busca una venta por su ID.
     * @param id El ID de la venta.
     * @return La venta encontrada o null si no existe.
     */
    public Venta findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todas las ventas de la base de datos.
     * @return Una lista de ventas.
     */
    public List<Venta> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Venta.findAll", Venta.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza una venta existente.
     * @param venta La venta a actualizar.
     */
    public void update(Venta venta) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(venta);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar la venta", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Elimina una venta de la base de datos.
     * @param id El ID de la venta a eliminar.
     */
    public void delete(Integer id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Venta venta = em.find(Venta.class, id);
            if (venta != null) {
                em.remove(venta);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar la venta", ex);
        } finally {
            em.close();
        }
    }

     public void deleteAll() {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createNativeQuery("delete from venta").executeUpdate();
            em.createNativeQuery("alter table empresa_ventas.venta AUTO_INCREMENT = 1").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar todos las ventas", ex);
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
    
    public void sincronizar(){
        getEntityManager().flush();
    }
}
