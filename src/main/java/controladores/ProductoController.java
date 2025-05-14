
package controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import entidades.Producto;

/**
 *
 * @author jfervic933
 */
public class ProductoController {
    private final EntityManagerFactory emf;

    public ProductoController() {
        // Nombre de la unidad de persistencia definida en persistence.xml
        this.emf = Persistence.createEntityManagerFactory("ventas");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Crea un nuevo producto en la base de datos.
     * @param producto El cliente a crear.
     */
    public void create(Producto producto) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(producto);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear el producto", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Busca un producto por su ID.
     * @param id El ID del producto.
     * @return El producto encontrado o null si no existe.
     */
    public Producto findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todos los productos de la base de datos.
     * @return Una lista de productos.
     */
    public List<Producto> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Producto.findAll", Producto.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza un producto existente.
     * @param producto El producto a actualizar.
     */
    public void update(Producto producto) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(producto);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar el producto", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Elimina un producto de la base de datos.
     * @param id El ID del producto a eliminar.
     */
    public void delete(Integer id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Producto producto = em.find(Producto.class, id);
            if (producto != null) {
                em.remove(producto);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar el producto", ex);
        } finally {
            em.close();
        }
    }
    
     public void deleteAll() {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createNativeQuery("delete from producto").executeUpdate();
            em.createNativeQuery("alter table empresa_ventas.producto AUTO_INCREMENT = 1").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar todos los productos", ex);
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
