package rocks.xprs.generators.localization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import rocks.xprs.creator.generator.Context;
import rocks.xprs.creator.generator.Generator;
import rocks.xprs.languages.xprsm.model.Attribute;
import rocks.xprs.languages.xprsm.model.CompoundType;
import rocks.xprs.languages.xprsm.model.Model;

/**
 *
 * @author rborowski
 */
public class ResourceKeysGenerator implements Generator {

  private Context context;

  @Override
  public void init(Context context) {
    this.context = context;
  }

  @Override
  public void generate(CompoundType ct) throws IOException {

  }

  @Override
  public void close() throws IOException {

    File target = new File(context.getSourceFolder(),
            "resources/TranslationKeys.txt");

    try (FileOutputStream fos = new FileOutputStream(target);
            PrintWriter writer = new PrintWriter(fos, true, StandardCharsets.UTF_8)) {

      for (Model m : context.getProject().getModels()) {
        String decapitalizedModelName = m.getName().substring(0,1).toLowerCase(Locale.ENGLISH)
                + m.getName().substring(1);

        for (Attribute a : m.getAllAttributes()) {
          writer.append(decapitalizedModelName).append(".attributes.").append(a.getName()).println();
        }
      }
    }
  }

}
