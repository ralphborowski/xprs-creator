/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author borowski
 */
public class Settings {

  private final File settingsFile;
  private final Properties settings = new Properties();

  public Settings(File settingsFile) {
    this.settingsFile = settingsFile;

    try {
      settings.loadFromXML(new FileInputStream(settingsFile));
    } catch (IOException ex) {
      Logger.getLogger(Settings.class.getName()).log(Level.SEVERE,
              String.format("Could not read settings file %s.",
                      settingsFile.getAbsolutePath()), ex);
    }
  }

  public Setting get(String name) {
    String s = settings.getProperty(name);
    return s == null ? new Setting(null) : new Setting(s);
  }

  public void set(String name, Object object) {

    if (object instanceof Integer) {
      settings.setProperty(name, ((Integer) object).toString());
    } else if (object instanceof Long) {
      settings.setProperty(name, ((Long) object).toString());
    } else if (object instanceof Float) {
      settings.setProperty(name, ((Float) object).toString());
    } else if (object instanceof Double) {
      settings.setProperty(name, ((Double) object).toString());
    } else if (object instanceof BigDecimal) {
      settings.setProperty(name, ((BigDecimal) object).toString());
    } else if (object instanceof Character) {
      settings.setProperty(name, ((Character) object).toString());
    } else if (object instanceof Boolean) {
      settings.setProperty(name, ((Boolean) object).toString());
    } else if (object instanceof String) {
      settings.setProperty(name, (String) object);
    } else if (object instanceof Date) {
      settings.setProperty(name, SimpleDateFormat.getDateTimeInstance().format((Date) object));
    }
  }

  public void save() {
    try {
      settings.storeToXML(new FileOutputStream(settingsFile), "", StandardCharsets.UTF_8);
    } catch (IOException ex) {
      Logger.getLogger(Settings.class.getName()).log(Level.SEVERE,
              String.format("Could not write settings file %s.",
                      settingsFile.getAbsolutePath()), ex);
    }
  }

}
