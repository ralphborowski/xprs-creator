/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.HashMap;
import java.util.Map;
import rocks.xprs.db.Entity;
import rocks.xprs.web.Controller;

/**
 *
 * @author rborowski
 */
public class DefaultPageRegister {

  private Map<Class<? extends Entity>, Class<? extends Controller>> detailsPages = new HashMap<>();
  private Map<Class<? extends Entity>, Class<? extends Controller>> editPages = new HashMap<>();
  private Map<Class<? extends Entity>, Class<? extends Controller>> deleteActions = new HashMap<>();

  public static void register(Class<? extends Entity> entityClass,
          Class<? extends Controller> detailsPage, Class<? extends Controller> editPage,
          Class<? extends Controller> deleteAction) {
    DefaultPageRegisterHolder.INSTANCE.detailsPages.put(entityClass, detailsPage);
    DefaultPageRegisterHolder.INSTANCE.editPages.put(entityClass, editPage);
    DefaultPageRegisterHolder.INSTANCE.deleteActions.put(entityClass, deleteAction);
  }

  public static Class<? extends Controller> getDetailsUrl(Entity entity) {
    return DefaultPageRegisterHolder.INSTANCE.detailsPages.get(entity.getClass());
  }

  public static Class<? extends Controller> getDetailsUrl(Class<? extends Entity> entityClass) {
    return DefaultPageRegisterHolder.INSTANCE.detailsPages.get(entityClass);
  }

  public static Class<? extends Controller> getEditUrl(Entity entity) {
    return DefaultPageRegisterHolder.INSTANCE.editPages.get(entity.getClass());
  }

  public static Class<? extends Controller> getEditUrl(Class<? extends Entity> entityClass) {
    return DefaultPageRegisterHolder.INSTANCE.editPages.get(entityClass);
  }

  public static Class<? extends Controller> getDeleteAction(Entity entity) {
    return DefaultPageRegisterHolder.INSTANCE.deleteActions.get(entity.getClass());
  }

  public static Class<? extends Controller> getDeleteAction(Class<? extends Entity> entityClass) {
    return DefaultPageRegisterHolder.INSTANCE.deleteActions.get(entityClass);
  }

  private static class DefaultPageRegisterHolder {
    private static final DefaultPageRegister INSTANCE = new DefaultPageRegister();
  }
}
