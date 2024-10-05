/*
 * The MIT License
 *
 * Copyright 2016 Ralph Borowski.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rocks.xprs.runtime.db;

import java.io.Closeable;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import rocks.xprs.runtime.exceptions.ResourceNotFoundException;

/**
 * Basic implementation of an entity DAO.
 * @author Ralph Borowski
 * @param <T> entity class this DAO is limited to
 */
public class EntityDAO<T extends DataItem> implements Closeable {

  protected EntityManager em;

  protected Class<T> entityClass;

  /**
   * Creates a new entity DAO for a given class.
   * @param entityClass the entity class of the results
   * @param em the shared entity manager
   */
  public EntityDAO(Class<T> entityClass, EntityManager em) {
    this.entityClass = entityClass;
    this.em = em;
  }

  /**
   * Get a single enitity.
   * @param id the entity's primary key
   * @return the entity for the given id
   * @throws ResourceNotFoundException if there is no entity with this id
   */
  public T get(Long id) throws ResourceNotFoundException {

    // try to get the entity from the database
    T entity = em.find(entityClass, id);

    // check if entity exists
    if (entity == null) {
      throw new ResourceNotFoundException(
              String.format("Could not find %s with id %s.",
                      entityClass.getSimpleName(), id));
    }

    // entity exists, return it
    return entity;
  }

  /**
   * Create or update an entity.
   * @param entity the entity
   * @return the entity with an updated id
   */
  public T save(T entity) {

    if (entity.getId() != null) {
      entity = em.merge(entity);
    } else {
      em.persist(entity);
    }

    return entity;
  }

  /**
   * List all entities of this type.
   * @return a list with all entities.
   */
  public List<T> list() {
    Query q = em.createQuery("SELECT i FROM " + entityClass.getSimpleName() + " i");
    return q.getResultList();
  }

  /**
   * Search in entities
   * @param keyword a keyword to search for
   * @return a list of search results
   */
  public List<T> search(String keyword) {
    return Collections.EMPTY_LIST;
  }

  /**
   * Delete an entity.
   * @param entity the entity
   */
  public void delete(T entity) {
    em.remove(entity);
  }

  /**
   * Commit transaction and close connection.
   */
  @Override
  public void close() {

  }
}