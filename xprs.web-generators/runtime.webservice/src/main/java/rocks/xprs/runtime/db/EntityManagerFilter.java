/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.runtime.db;

import java.io.IOException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author Ralph Borowski
 */
public class EntityManagerFilter implements Filter {
  
  private static final ThreadLocal<EntityManager> ENTITY_MANAGER_HOLDER = new ThreadLocal<>();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
        
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
          throws IOException, ServletException {
    
    try {
      // get entity manager and transaction
      EntityManager em = EntityManagerProvider.get();
      EntityTransaction tx = em.getTransaction();
      tx.begin();

      // save to thread local
      ENTITY_MANAGER_HOLDER.set(em);

      // do request
      Throwable thrownException = null;
      try {
        chain.doFilter(request, response);
      } catch (Throwable ex) {
        // catch all errors and try to close the database connection properly
        thrownException = ex;
      }
      
      
      // close transaction and entity manager
      if (tx.getRollbackOnly()) {
        tx.rollback();
      } else {
        tx.commit();
      }
      em.close();
      ENTITY_MANAGER_HOLDER.remove();
      
      // rethrow exception if any
      if (thrownException != null) {
        throw new ServletException(thrownException);
      }
    } catch (NamingException ex) {
      throw new ServletException("Misconfigured Persistence Unit.", ex);
    }
  }

  @Override
  public void destroy() {

  }
  
  public static EntityManager getEntityManager() {
    return ENTITY_MANAGER_HOLDER.get();
  }
}
