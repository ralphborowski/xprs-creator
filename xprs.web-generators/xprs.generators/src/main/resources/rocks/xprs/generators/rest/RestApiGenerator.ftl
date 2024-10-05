/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.api;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * REST API for ${model.name?uncap_first}
 */
@Stateless
@Path("/${model.name?uncap_first}")
@Produces({"text/xml", "application/json"})
@Consumes({"text/xml", "application/json"})
public class ${model.name}Resource extends Abstract${model.name}Resource {
  
}