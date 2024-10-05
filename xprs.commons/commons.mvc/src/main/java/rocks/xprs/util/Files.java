/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author borowski
 */
public class Files {

  public static boolean copy(File source, File target) throws IOException {

    if (!source.exists()) {
      return false;
    }

    boolean result = true;

    if (!target.getParentFile().exists()) {
      result &= target.getParentFile().mkdirs();
    }

    if (source.isDirectory()) {
      if (!target.exists()) {
        result &= target.mkdirs();
      }

      if (!target.isDirectory()) {
        return false;
      }

      for (File f : source.listFiles()) {
        result &= Files.copy(f, new File(target, f.getName()));
      }
    } else {
      java.nio.file.Files.copy(source.toPath(), target.toPath(),
              StandardCopyOption.COPY_ATTRIBUTES);
    }

    return result;

  }

  /**
   * Deletes a folder recursively.
   *
   * @param folder the folder to delete
   * @return true if delete was successful
   */
  public static boolean deleteFolder(File folder) {

    if (!folder.exists()) {
      return true;
    }

    boolean result = true;

    for (File f : folder.listFiles()) {
      if (f.getName().equals(".") || f.getName().equals("..")) {
        // don't follow links to same folder or parent folder
      } else if (f.isDirectory()) {
        result &= deleteFolder(f);
      } else {
        result &= f.delete();
      }
    }

    if (result) {
      result &= folder.delete();
    }

    return result;
  }

}
