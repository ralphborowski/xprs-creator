 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rocks.xprs.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.io.Closeable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import rocks.xprs.exceptions.ResourceNotFoundException;

/**
 * Generic data access object (DAO) with basic functions.
 *
 * @author borowski
 * @param <T> the type of entities this DAO handles
 */
public abstract class EntityDAO<T extends Entity> implements Closeable {

  protected enum EntityManagerSource { SERVLETFILTER, SINGLETON }

  protected EntityManager em;
  private EntityManagerSource entityManagerSource = EntityManagerSource.SERVLETFILTER;
  private EntityTransaction transaction;

  public EntityDAO() {

    // get entity manager from servlet filter
    em = EntityManagerFilter.getEntityManager();
    if (em == null) {
      em = EntityManagerProvider.getInstance().getEntityManager();
      entityManagerSource = EntityManagerSource.SINGLETON;
      transaction = em.getTransaction();
      transaction.begin();
    }
  }

  public abstract Class getEntityClass();

  public T get(long id) throws ResourceNotFoundException {

    // check if it is active
    assert em.getTransaction().isActive();

    T item = (T) em.find(getEntityClass(), id);

    if (item == null) {
      throw new ResourceNotFoundException("Ressource " + getEntityClass().getSimpleName() + " not found.");
    }

    // apply automatic changes and return result
    onLoad(item);
    return item;
  }

  public T save(T item) {

    // apply automatic changes
    onSave(item);

    // try to persist the item
    if (item.getId() != null) {
      item.setModifyDate(LocalDateTime.now(ZoneId.systemDefault()));
      item = em.merge(item);
    } else {
      item.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()));
      item.setModifyDate(item.getCreateDate());
      em.persist(item);
    }

    // save changes to database
    em.flush();
    em.refresh(item);

    // return item with current changes
    return item;
  }

  public void delete(T item) {

    // remove item from persistence context
    assert em.getTransaction().isActive();
    em.remove(item);

    // save changes to the database
    em.flush();
  }

  public ResultList<? extends T> list() {

    // get items order by filters
    Query q = em.createQuery("SELECT s FROM " + getEntityClass().getSimpleName() + " s");
    ResultList<T> items = new ResultList<>(q.getResultList(), getEntityClass());

    for (T i : items) {
      onLoad(i);
    }

    return items;
  }

  @Override
  public void close() {
    if (entityManagerSource == EntityManagerSource.SINGLETON) {
      if (transaction.getRollbackOnly()) {
        transaction.rollback();
      } else {
        transaction.commit();
      }
    }
  }

  protected void onLoad(T entity) {
    // do nothing by default
  }
  protected void onSave(T entity) {
    // do nothing by default
  }

}