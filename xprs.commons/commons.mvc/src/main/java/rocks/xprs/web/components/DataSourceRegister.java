/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.HashMap;
import java.util.Map;
import rocks.xprs.db.Entity;
import rocks.xprs.db.EntityDAO;
import rocks.xprs.exceptions.ResourceNotFoundException;
import rocks.xprs.util.Strings;

/**
 *
 * @author borowski
 */
public class DataSourceRegister {

  private final Map<Class<? extends Entity>, Class<? extends EntityDAO>> register = new HashMap<>();
  private final Map<String, Class<? extends Entity>> entityIndex = new HashMap<>();

  private DataSourceRegister() {
  }

  public static void register(Class<? extends Entity> entityClass,
          Class<? extends EntityDAO> daoClass) {

    DataSourceRegisterHolder.INSTANCE.register.put(entityClass, daoClass);
    DataSourceRegisterHolder.INSTANCE.entityIndex.put(
            Strings.decapitalize(entityClass.getSimpleName()), entityClass);
  }

  public static void unregister(Class<? extends Entity> entityClass) {
    DataSourceRegisterHolder.INSTANCE.register.remove(entityClass);
    DataSourceRegisterHolder.INSTANCE.entityIndex.remove(
            Strings.decapitalize(entityClass.getSimpleName()));
  }

  public static EntityDAO getDao(Class<? extends Entity> entityClass)
          throws ResourceNotFoundException {

    if (entityClass == null) {
      throw new ResourceNotFoundException("Entity class is null.");
    }

    Class<? extends EntityDAO> daoClass = DataSourceRegisterHolder.INSTANCE.register.get(entityClass);

    if (daoClass == null) {
      throw new ResourceNotFoundException(String.format("EntityDAO for %s not found.",
              entityClass.getCanonicalName()));
    }

    try {
      return daoClass.getDeclaredConstructor().newInstance();
    } catch (ReflectiveOperationException ex) {
      throw new ResourceNotFoundException(String.format("Could not create DAO for %s (%s).",
              entityClass.getCanonicalName(), daoClass), ex);
    }
  }

  public static EntityDAO getDao(String entityClassName) throws ResourceNotFoundException {

    // try to get by full name
    Class entityClass;
    try {
      entityClass = Class.forName(entityClassName);
      if (!Entity.class.isAssignableFrom(entityClass)) {

        // drop if not of type Entity
        throw new ResourceNotFoundException(
                String.format("%s is not of type Entity.", entityClassName));
      }
    } catch (ClassNotFoundException ex) {
      entityClass = getEntityClass(entityClassName);
    }

    return getDao(entityClass);
  }

  public static Class<? extends Entity> getEntityClass(String entityName)
          throws ResourceNotFoundException {

    Class<? extends Entity> entityClass = DataSourceRegisterHolder.INSTANCE.entityIndex.get(
            Strings.decapitalize(entityName));

    if (entityClass == null) {
      throw new ResourceNotFoundException(String.format("Could not get entity class for %s.",
              Strings.decapitalize(entityName)));
    }

    return entityClass;
  }

  private static class DataSourceRegisterHolder {

    private static final DataSourceRegister INSTANCE = new DataSourceRegister();
  }
}
