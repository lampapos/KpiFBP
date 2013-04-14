package edu.kpi.fbp.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;

/**
 * Utility methods.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class Utils {

  private Utils() {
    // do nothing
  }

  /**
   * @param compDirs the component directories
   * @return the classloader which can load component classes
   */
  public static URLClassLoader getJarsClassLoader(final File... compDirs) {
    final List<File> jars = new ArrayList<>();
    for (final File f : compDirs) {
      jars.addAll(Arrays.asList(f.listFiles()));
    }

    final List<URL> jarsUrls = new ArrayList<URL>();
    for (final File f : jars) {
      if (f.getName().endsWith(".jar")) {
        JarFile jar = null;
        try {
          jar = new JarFile(f);
          jarsUrls.add(new URL("jar", "", "file:" + f.getAbsolutePath() + "!/"));
        } catch (final IOException e) {
          System.err.println("Something wrong with " + f.getName() + " component jar file.");
        } finally {
          try {
            jar.close();
          } catch (final IOException e) {
            e.printStackTrace();
          }
        }
      }
    }

    return new URLClassLoader(jarsUrls.toArray(new URL [0]));
  }

}
