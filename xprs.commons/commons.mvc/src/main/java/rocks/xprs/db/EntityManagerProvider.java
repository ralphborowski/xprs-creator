/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import rocks.xprs.web.Extension;

/**
 *
 * @author borowski
 */
public class EntityManagerProvider implements Extension {

  private static EntityManagerProvider INSTANCE;
  public static final EntityManagerProvider getInstance() {
    if (INSTANCE == null) {
      new EntityManagerProvider().init();
    }

    return INSTANCE;
  }

  private EntityManagerFactory emf;

  @Override
  public void init() {
    emf = Persistence.createEntityManagerFactory("persistenceUnit");
    EntityManagerProvider.INSTANCE = this;
  }

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  @Override
  public void destroy() {
    emf.close();
  }

}
