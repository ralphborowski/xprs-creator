/**
 * This software was created with xprs.
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.dao;

import jakarta.ejb.Stateless;
import jakarta.inject.Named;

/**
 * DAO class for ${model.name?uncap_first}
 */
@Named
@Stateless
public class ${toPlural(model.name)} extends Abstract${toPlural(model.name)} {

}