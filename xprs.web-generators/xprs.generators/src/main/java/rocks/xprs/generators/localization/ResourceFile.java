/*
 * The MIT License
 *
 * Copyright 2017 Ralph Borowski.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rocks.xprs.generators.localization;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Auxilliary class to help creating resource files for translations.
 *
 * @author Ralph Borowski
 */
public class ResourceFile implements Closeable {

  private final File file;
  private final Properties resources;

  /**
   * Read a resource file from a XML.
   *
   * @param file the file
   * @throws IOException if the given file could not be read
   */
  public ResourceFile(File file) throws IOException {
    this.file = file;
    resources = new Properties();
    
    if (file.exists()) {
      resources.loadFromXML(new FileInputStream(file));
    }
  }

  /**
   * Add entry but don't override existing ones.
   *
   * @param key the key
   * @param value the translation
   */
  public void add(String key, String value) {
    if (!resources.containsKey(key)) {
      resources.setProperty(key, value);
    }
  }

  /**
   * Add entry and override if already exists.
   *
   * @param key the key
   * @param value the translation
   */
  public void override(String key, String value) {
    resources.setProperty(key, value);
  }

  /**
   * Saves changes and closes file.
   *
   * @throws IOException if file could not be written
   */
  @Override
  public void close() throws IOException {
    resources.storeToXML(new FileOutputStream(file), null);
  }

  /**
   * Separate a camel case word.
   *
   * @param string the camel case word (e.g. class or attribute name)
   * @return the separated words
   */
  public static String separateCamelCase(String string) {
    String label = string.substring(0,1).toUpperCase() + string.substring(1);
    return label.replaceAll("(\\p{Lu})", " $1").trim();
  }
  
  /**
   * Generates a label out of a snake case word (usually used in enums). 
   * The result of "SNAKE_CASE" would be "snake case".
   * 
   * @param string the source string
   * @return a converted string
   */
  public static String convertSnakeCaseToLabel(String string) {
    return string.toLowerCase().replace("_", " ");
  }

  /**
   * Adds missing entries from the master resource to the slave resource. Copied
   * entries are marked with ***.
   *
   * @param masterResource the resource with all keys
   * @param slaveResource the resource with missing keys
   * @throws IOException if one of the resources could not be opened or written
   */
  public static void syncKeys(String masterResource, String slaveResource) throws IOException {
    Properties master = new Properties();
    master.load(new FileInputStream(masterResource));

    Properties slave = new Properties();
    slave.load(new FileInputStream(slaveResource));

    for (String key : master.stringPropertyNames()) {
      if (!slave.containsKey(key)) {
        slave.setProperty(key, "***" + master.getProperty(key) + "***");
      }
    }

    slave.store(new FileOutputStream(slaveResource), null);
  }
}
