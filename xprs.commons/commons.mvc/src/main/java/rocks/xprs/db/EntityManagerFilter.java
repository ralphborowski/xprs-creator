/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 */
public class EntityManagerFilter implements Filter {

  private static final ThreadLocal<EntityManager> ENTITY_MANAGER_HOLDER
          = new ThreadLocal<EntityManager>();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {

    try {

      // get entity manager
      EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
      EntityTransaction transaction = em.getTransaction();
      transaction.begin();
      ENTITY_MANAGER_HOLDER.set(em);

      // run servlet
      chain.doFilter(request, response);

      // commit or rollback transaction
      if (transaction.getRollbackOnly()) {
        transaction.rollback();
      } else {
        transaction.commit();
      }
    } catch (IOException ex) {
      throw new ServletException(ex);
    } finally {
      ENTITY_MANAGER_HOLDER.remove();
    }
  }

  @Override
  public void destroy() {

  }

  public static EntityManager getEntityManager() {
    return ENTITY_MANAGER_HOLDER.get();
  }
}
