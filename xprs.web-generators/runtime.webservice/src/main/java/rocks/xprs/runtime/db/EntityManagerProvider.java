package rocks.xprs.runtime.db;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ralph Borowski
 */
public class EntityManagerProvider {
  
  public static final String PERSISTENCE_UNIT_REFERENCE = "xprsPersistenceUnit";

  /**
   * Returns an EntityManager for the default Persistence Unit. Make sure to handle the transaction
   * properly and close the EntityManager after use.
   * 
   * The reference has to be defined in a web.xml file like this:
   * 
   * <pre>
   *   <persistence-unit-ref>
   *     <persistence-unit-ref-name>xprsPersistenceUnit</persistence-unit-ref-name>
   *     <persistence-unit-name>NAME OF THE PERSISTENCE UNIT</persistence-unit-name>
   *   </persistence-unit-ref>
   * </pre>
   * 
   * @return an entity manager
   * @throws NamingException if the persistence unit is not configured properly
   */
  public static EntityManager get() throws NamingException {
    
    // get entity manager
    EntityManagerFactory emf = (EntityManagerFactory) new InitialContext()
            .lookup("java:comp/env/" + PERSISTENCE_UNIT_REFERENCE);
    return emf.createEntityManager();
  }

}
