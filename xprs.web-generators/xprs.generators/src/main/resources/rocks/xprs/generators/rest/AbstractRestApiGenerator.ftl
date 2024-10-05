/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.api;

import ${toCanonicalClassName(model)};
import ${toCanonicalFilterClassName(model)};
import ${toCanonicalValidatorClassName(model)};<#list model.allTypes as type><#if isModel(type) && !toCanonicalClassName(type)?starts_with(model.project.basePackage)>
import ${toCanonicalClassName(type)};</#if></#list>
import ${toCanonicalDaoClassName(model)};<#list model.allTypes as type><#if isModel(type)>
import ${toCanonicalDaoClassName(type)};</#if></#list>
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import rocks.xprs.runtime.exceptions.InvalidDataException;
import rocks.xprs.runtime.exceptions.ResourceNotFoundException;
import rocks.xprs.runtime.db.PageableList;

/**
 * REST API for ${model.name}
 */
public abstract class Abstract${model.name}Resource {

  @PersistenceContext
  protected EntityManager em;
  
  /**
   * Get a list of ${toPlural(model.name?uncap_first)}.
   * @return a list of ${toPlural(model.name?uncap_first)}
   */
  @GET
  public List<${model.name}> index() {
    try (${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = new ${toPlural(model.name)}(em)) {
      return ${toPlural(model.name?uncap_first)}.list();
    }
  }

  /**
   * Get a filtered list of ${toPlural(model.name?uncap_first)}.
   * @param filter the ${model.name?uncap_first} filter
   * @return a list of ${toPlural(model.name?uncap_first)}
   */
  @POST
  @Path("/filter")
  public PageableList<${model.name}> filter(${model.name}Filter filter) {
    try (${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = new ${toPlural(model.name)}(em)) {
      return ${toPlural(model.name?uncap_first)}.list(filter);
    }
  }

  /**
   * Search ${toPlural(model.name?uncap_first)} by a given keyword.
   * @param keyword the keyword to search for
   * @return a list of ${toPlural(model.name?uncap_first)}
   */
  @GET
  @Path("/search")
  public List<${model.name}> search(@QueryParam("keyword") String keyword) {
    try (${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = new ${toPlural(model.name)}(em)) {
      return ${toPlural(model.name?uncap_first)}.search(keyword);
    }
  }
  
  /**
   * Get a single ${model.name?uncap_first}
   * @param id the ${model.name?uncap_first}'s id
   * @return the ${model.name?uncap_first}
   * @throws ResourceNotFoundException if a ${model.name?uncap_first} with the given id does not exist
   */
  @GET
  @Path("/{id}")
  public ${model.name} details(@PathParam("id") long id) throws ResourceNotFoundException {
    try (${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = new ${toPlural(model.name)}(em)) {
      return ${toPlural(model.name?uncap_first)}.get(id);
    }
  }
  
  /**
   * Creates a new ${model.name?uncap_first}.
   * @param ${model.name?uncap_first} the new ${model.name?uncap_first}
   * @return the updated ${model.name?uncap_first}
   * @throws ResourceNotFoundException if a related resource could not be found
   * @throws InvalidDataException if the ${model.name?uncap_first} couldn't be validated
   */
  @POST
  public ${model.name} create(${model.name} ${model.name?uncap_first}) throws ResourceNotFoundException, InvalidDataException {
  
    try (${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = new ${toPlural(model.name)}(em);<#list model.allTypes as type><#if isModel(type)>
             ${toPlural(type.name)} ${toPlural(type.name?uncap_first)} = new ${toPlural(type.name)}(em);</#if></#list>) {
      
      // validate input
      ${model.name}Validator ${model.name?uncap_first}Validator = new ${model.name}Validator();
      Map<String, List<String>> errors = ${model.name?uncap_first}Validator.validate(${model.name?uncap_first});
      
      if (!errors.isEmpty()) {
        throw new InvalidDataException("${model.name?uncap_first}.errors.validation", errors);
      }
      
      // update meta data
      ${model.name?uncap_first}.setCreateDate(new Date());
      ${model.name?uncap_first}.setModifyDate(${model.name?uncap_first}.getCreateDate());
      
      <#list model.allAttributes as attribute><#if isModel(attribute.type)>   
      if (${model.name?uncap_first}.get${attribute.name?cap_first}() != null) {
        ${model.name?uncap_first}.set${attribute.name?cap_first}(${toPlural(attribute.type.name?uncap_first)}.get(${model.name?uncap_first}.get${attribute.name?cap_first}().getId()));
      }</#if></#list>
      // save and return result
      return ${toPlural(model.name?uncap_first)}.save(${model.name?uncap_first});
    }
  }
  
  /**
   * Saves a changed ${model.name?uncap_first}.
   * @param ${model.name?uncap_first} the updated ${model.name?uncap_first}
   * @return the updated ${model.name?uncap_first} object
   * @throws ResourceNotFoundException if the ${model.name?uncap_first} does not exist
   * @throws InvalidDataException if the ${model.name?uncap_first} couldn't be validated
   */
  @PUT
  @Path("/{id}")
  public ${model.name} update(${model.name} ${model.name?uncap_first}) throws ResourceNotFoundException, InvalidDataException {
    try (${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = new ${toPlural(model.name)}(em)) {
    
      // validate input
      ${model.name}Validator ${model.name?uncap_first}Validator = new ${model.name}Validator();
      Map<String, List<String>> errors = ${model.name?uncap_first}Validator.validate(${model.name?uncap_first});
      
      if (!errors.isEmpty()) {
        throw new InvalidDataException("${model.name?uncap_first}.errors.validation", errors);
      }

      // update meta data
      ${model.name?uncap_first}.setModifyDate(new Date());

      // save and return result
      return ${toPlural(model.name?uncap_first)}.save(${model.name?uncap_first});
    }
  }
  
  /**
   * Deletes a ${model.name?uncap_first} by id.
   * @param id the ${model.name?uncap_first}'s id
   * @throws ResourceNotFoundException if the ${model.name?uncap_first} does not exist
   */
  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") long id) throws ResourceNotFoundException {
    try (${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = new ${toPlural(model.name)}(em)) {
      ${model.name} ${model.name?uncap_first} = ${toPlural(model.name?uncap_first)}.get(id);
      ${toPlural(model.name?uncap_first)}.delete(${model.name?uncap_first});
    }
  }
}