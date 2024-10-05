package rocks.xprs.creator.generator;

import java.io.IOException;
import rocks.xprs.languages.xprsm.model.CompoundType;

/**
 * Interface all generators have to implement. Generators are only instantiated once per build and shares for all models.
 * @author Ralph Borowski
 */
public interface Generator {

  /**
   * Called once per build when the generator is called for the first time.
   * @param context the context
   */
  public void init(Context context);
  
  /**
   * Called on every model.
   * @param compoundType the model or enum
   * @throws java.io.IOException  if template could not be processed or result could not be written
   */
  public void generate(CompoundType compoundType) throws IOException;
  
  /**
   * Called after the build has finished.
   * @throws java.io.IOException if generator could not be closed
   */
  public void close() throws IOException;
  
}
