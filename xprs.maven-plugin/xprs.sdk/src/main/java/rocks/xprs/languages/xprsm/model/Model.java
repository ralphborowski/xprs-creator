package rocks.xprs.languages.xprsm.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Represents a model.
 *
 * @author Ralph Borowski
 */
public class Model extends CompoundType {

  private boolean isAbstract;
  private Model inheritsFrom;
  private List<Attribute> attributes = new LinkedList<>();
  private List<OnDelete> onDelete = new LinkedList<>();

  /**
   * Returns only inherited attributes.
   *
   * @return a list of inherited attributes or an empty list
   */
  public Set<Attribute> getInheritedAttributes() {
    if (this.getInheritsFrom() != null) {
      return this.getInheritsFrom().getAllAttributes();
    }
    return Collections.EMPTY_SET;
  }

  /**
   * Return all attributes (own and inherited).
   *
   * @return a list with all attributes.
   */
  public Set<Attribute> getAllAttributes() {
    Set<Attribute> allAttributes = new LinkedHashSet<>();
    allAttributes.addAll(getInheritedAttributes());
    allAttributes.addAll(this.getAttributes());
    return allAttributes;
  }

  /**
   * Returns, if a model has an attribute with the given name.
   *
   * @param name the attribute's name
   * @return true, if found
   */
  public boolean hasAttribute(String name) {
    for (Attribute a : getAllAttributes()) {
      if (a.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns a set of types of the attributes directly defined in the model.
   *
   * @return a set of types
   */
  public Set<Type> getTypes() {
    Set<Type> types = new HashSet<>();
    for (Attribute a : getAttributes()) {
      if (a.getType() != null && !types.contains(a.getType())) {
        types.add(a.getType());
      }
    }
    return types;
  }

  /**
   * Returns a set of types of all inherited attributes in the model.
   *
   * @return a set of types
   */
  public Set<Type> getInheritedTypes() {

    Set<Type> inheritedTypes = new HashSet<>();
    for (Attribute a : getInheritedAttributes()) {
      if (!inheritedTypes.contains(a.getType())) {
        inheritedTypes.add(a.getType());
      }
    }
    return inheritedTypes;
  }

  /**
   * Returns a set with all types of all attributes in the model.
   *
   * @return
   */
  public Set<Type> getAllTypes() {
    Set<Type> allTypes = new HashSet<>();
    allTypes.addAll(getInheritedTypes());
    allTypes.addAll(getTypes());

    return allTypes;
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the isAbstract
   */
  public boolean isAbstract() {
    return isAbstract;
  }

  /**
   * @param isAbstract the isAbstract to set
   */
  public void setAbstract(boolean isAbstract) {
    this.isAbstract = isAbstract;
  }

  /**
   * @return the inheritsFrom
   */
  public Model getInheritsFrom() {
    return inheritsFrom;
  }

  /**
   * @param inheritsFrom the inheritsFrom to set
   */
  public void setInheritsFrom(Model inheritsFrom) {
    this.inheritsFrom = inheritsFrom;
  }

  /**
   * @return the attributes
   */
  public List<Attribute> getAttributes() {
    return attributes;
  }

  /**
   * @param attributes the attributes to set
   */
  public void setAttributes(List<Attribute> attributes) {
    this.attributes = attributes;
  }

  /**
   * @return the onDelete
   */
  public List<OnDelete> getOnDelete() {
    return onDelete;
  }

  /**
   * @param onDelete the onDelete to set
   */
  public void setOnDelete(List<OnDelete> onDelete) {
    this.onDelete = onDelete;
  }
//</editor-fold>
}
