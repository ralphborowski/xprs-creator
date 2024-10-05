package rocks.xprs.runtime.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Date;
import rocks.xprs.runtime.exceptions.ResourceNotFoundException;

/**
 * Generic data access object (DAO) with basic functions.
 *
 * @author borowski
 * @param <T> the type of entities this DAO handles
 */
public abstract class DataItems<T extends DataItem> {

  @PersistenceContext
  protected EntityManager em;

  public abstract Class getEntityClass();

  public T get(long id) throws ResourceNotFoundException {

    // check if it is active
    T item = (T) em.find(getEntityClass(), id);

    if (item == null) {
      throw new ResourceNotFoundException("Ressource " + getEntityClass().getSimpleName() + " not found.");
    }

    return item;
  }

  public PageableList<? extends T> list() {

    // get items order by filters
    Query q = em.createQuery("SELECT s FROM " + getEntityClass().getSimpleName() + " s");
    PageableList<T> items = new PageableList<>(q.getResultList());

    return items;
  }

  public T save(T item) {

    // try to persist the item
    if (item.getId() != null) {
      item.setModifyDate(new Date());
      item = em.merge(item);
    } else {
      item.setCreateDate(new Date());
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

    item = em.merge(item);

    // remove item from persistence context
    em.remove(item);

    // save changes to the database
    em.flush();
  }

}
