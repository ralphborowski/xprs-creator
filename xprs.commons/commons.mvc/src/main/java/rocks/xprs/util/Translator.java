/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import rocks.xprs.web.Context;

/**
 *
 * @author borowski
 */
public class Translator {

  private static final Logger LOG = Logger.getLogger(Translator.class.getName());
  private static final ConcurrentHashMap<Locale, Translator> TRANSLATOR_CACHE
          = new ConcurrentHashMap<>();

  public static final Translator get(Locale locale) {
    if (!TRANSLATOR_CACHE.containsKey(locale)) {
      TRANSLATOR_CACHE.put(locale, new Translator(locale));
    }
    return TRANSLATOR_CACHE.get(locale);
  }

  private final Properties translations = new Properties();

  private Translator(Locale locale) {

    File langFolder = new File("resources/lang");

    // load language in general
    File genericLocale = new File(langFolder, locale.getLanguage());
    load(genericLocale);

    // override with more specific version
    File specificLocale = new File(langFolder, locale.getLanguage() + "_"
            + locale.getCountry());
    load(specificLocale);
  }

  private void load(File languageFolder) {
    LOG.log(Level.INFO,
            String.format("Trying to load language file %s.", languageFolder.getAbsolutePath()));

    if (languageFolder.exists() && languageFolder.isDirectory()) {
      File[] languageFiles = languageFolder.listFiles(
              filename -> filename.isFile() && filename.getName().endsWith(".properties"));

      for (File languageFile : languageFiles) {
        try ( FileInputStream fis = new FileInputStream(languageFile)) {
          translations.load(fis);
        } catch (IOException ex) {
          LOG.log(Level.SEVERE,
                  String.format("Could not read from language file %s.",
                          languageFile.getAbsolutePath()),
                  ex);
        }
      }
    }
  }

  public String get(String key) {
    return translations.getProperty(key, key);
  }

  public String get(String key, Object... values) {
    return String.format(Translator.get(Context.get().getLocale()).get(key), values);
  }

}
