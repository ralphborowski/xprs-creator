/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${project.basePackage}.api;

import javax.ws.rs.ApplicationPath;

/**
 * ApplicationConfig class for ${toProjectShortName(project.basePackage)?lowerCase}
 */
@ApplicationPath("/api")
public class ApplicationConfig extends AbstractApplicationConfig {

  public ApplicationConfig() {
    super();
  }
}


