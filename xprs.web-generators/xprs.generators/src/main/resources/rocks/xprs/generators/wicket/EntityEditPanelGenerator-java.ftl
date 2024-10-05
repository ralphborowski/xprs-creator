/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.gui.panels.${model.name?uncap_first};

import ${toCanonicalClassName(model)};

/**
 * Edit panel for ${model.name?uncap_first}.
 */
public class ${model.name?cap_first}EditPanel extends Abstract${model.name?cap_first}EditPanel {

  public ${model.name?cap_first}EditPanel(String id) {
    super(id);
  }
  
  public ${model.name?cap_first}EditPanel(String id, ${model.name} ${model.name?uncap_first}) {
    super(id, ${model.name?uncap_first});
  }  
}