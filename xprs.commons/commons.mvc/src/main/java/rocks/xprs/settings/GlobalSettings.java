/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.settings;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rborowski
 */
public class GlobalSettings {

  private static final Logger LOG = Logger.getLogger(GlobalSettings.class.getName());

  private final Map<String, Setting> settings = new HashMap<>();

  public GlobalSettings() {
    reload();
  }

  private void reload() {
    File configFolder = new File("./config");
    if (configFolder.exists() && configFolder.isDirectory()) {
      for (File configFile : configFolder.listFiles(f -> f.getName().endsWith(".properties"))) {
        try (FileReader reader = new FileReader(configFile, StandardCharsets.UTF_8)) {
          Properties p = new Properties();
          p.load(reader);

          for (Map.Entry<Object, Object> e : p.entrySet()) {
            settings.put(String.valueOf(e.getKey()), new Setting(String.valueOf(e.getValue())));
          }
        } catch (IOException ex) {
          LOG.log(Level.WARNING,
                  String.format("Config file %s could not be read.", configFile.getAbsolutePath()),
                  ex);
        }
      }
    } else {
      LOG.log(Level.WARNING,
              String.format("Config folder %s not found.", configFolder.getAbsolutePath()));
    }
  }

  public static boolean isDebug() {
    return get("application.isDebug", "false").toBoolean();
  }

  public static String getDefaultLocale() {
    return get("application.defaultLocale", "de-DE").toString();
  }


  public static Setting get(String key) {
    return GlobalSettingsHolder.INSTANCE.settings.get(key);
  }

  public static Setting get(String key, String defaultValue) {
    Setting setting = get(key);
    if (setting != null) {
      return setting;
    }
    return new Setting(defaultValue);
  }

  private static class GlobalSettingsHolder {

    private static final GlobalSettings INSTANCE = new GlobalSettings();
  }
}
