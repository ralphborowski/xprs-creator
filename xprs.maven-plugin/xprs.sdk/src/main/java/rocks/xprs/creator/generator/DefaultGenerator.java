package rocks.xprs.creator.generator;

import java.io.IOException;
import rocks.xprs.languages.xprsm.model.CompoundType;

/**
 * A default generator doing nothing.
 * @author Ralph Borowski
 */
public class DefaultGenerator implements Generator {
  
  protected Context context;
  
  @Override
  public void init(Context context) {
    this.context = context;
  }
  
  @Override
  public void generate(CompoundType compoundType) throws IOException {
  
  }
  
  @Override
  public void close() {
  
  }
}