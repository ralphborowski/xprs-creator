/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.model;

import jakarta.persistence.Entity;

/**
 * Model class for ${model.name?uncap_first}
 */
@Entity
public class ${model.name?cap_first} extends Abstract${model.name?cap_first} {

  private static final long serialVersionUID = 1L;
  
}