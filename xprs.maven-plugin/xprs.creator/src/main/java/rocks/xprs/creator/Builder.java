package rocks.xprs.creator;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rocks.xprs.creator.generator.Context;
import rocks.xprs.creator.generator.Generator;
import rocks.xprs.languages.xprsm.model.CompoundType;

/**
 * The builder class runs the selected generators on the models.
 * @author Ralph Borowski
 */
public class Builder implements Closeable {

  private final Context context;
  private final Map<String, Generator> generatorInstances = new HashMap();

  /**
   * Create a builder in a given context.
   * @param context the current context
   */
  public Builder(Context context) {
    this.context = context;
  }

  /**
   * Run a list of generators on a list of models.
   * @param compoundType the models and enums
   * @param generators the generators
   */
  public void build(List<CompoundType> compoundType, List<Class> generators) {
    for (Class g : generators) {
      System.out.println("Running " + g.getName());
      for (CompoundType m : compoundType) {
        System.out.println("  on " + m.getName());
        try {
          build(m, g);
        } catch (InstantiationException | IllegalAccessException | IOException ex) {
          System.out.println("Error while executing generator " + g.getName() + " on type " + m.getName() + ".");
        }
      }
    }
  }

  /**
   * Runs a single generator on a single model.
   * @param compoundType the model or enum
   * @param generator the generator
   * @throws InstantiationException if the generator can't be instantiated.
   * @throws IllegalAccessException if the empty constructor is private.
   */
  private void build(CompoundType compoundType, Class generator) throws InstantiationException, IllegalAccessException, IOException {

    // check if generator has already been instanciated
    if (!this.generatorInstances.containsKey(generator.getName())) {

      // init generator and add it to the cache
      Generator generatorInstance = (Generator) generator.newInstance();
      generatorInstance.init(context);
      this.generatorInstances.put(generator.getName(), generatorInstance);
    }

    // apply generator on model
    Generator generatorInstance = (Generator) this.generatorInstances.get(generator.getName());
    generatorInstance.generate(compoundType);
  }

  /**
   * Close all generators after the build has finished.
   * @throws java.io.IOException if a generator could not be closed
   */
  @Override
  public void close() throws IOException {
    for (Generator g : this.generatorInstances.values()) {
      g.close();
    }
  }
}