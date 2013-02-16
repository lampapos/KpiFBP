package edu.kpi.fbp.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.jpmorrsn.fbp.engine.Component;

/**
 * Component observer utility - observes component library and gives map class.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class ComponentsObserver {
  /** Class suffix length. */
  private static final int CLASS_SUFFIX_LENGTH = 6;

  /** Components directory. */
  private final File compDir;

  private ComponentsObserver(final File componentLibDir) {
    this.compDir = componentLibDir;
  }

  /**
   * Factory method.
   * @param componentLibDir component lib directory
   * @return the component observer instance
   */
  public static ComponentsObserver create(final File componentLibDir) {
    if (!componentLibDir.isDirectory()) {
      throw new IllegalArgumentException("Component library must be directory.");
    }

    return new ComponentsObserver(componentLibDir);
  }

  /**
   * @return the map of all available components
   */
  @SuppressWarnings("unchecked")
  public Map<String, ComponentClassDescriptor> getAvailableComponentsSet() {
    final File [] jars = compDir.listFiles();

    final List<URL> jarsUrls = new ArrayList<URL>();
    final Map<String, JarFile> classNames = new HashMap<String, JarFile>();
    for (final File f : jars) {
      if (f.getName().endsWith(".jar")) {
        JarFile jar = null;
        try {
          jar = new JarFile(f);
          jarsUrls.add(new URL("jar", "", "file:" + f.getAbsolutePath() + "!/"));
          final Enumeration<JarEntry> entries = jar.entries();
          while (entries.hasMoreElements()) {
            final JarEntry e = entries.nextElement();
            if (e.getName().endsWith(".class")) {
              classNames.
                put(e.getName().replace("/", ".").substring(0, e.getName().length() - CLASS_SUFFIX_LENGTH), jar);
            }
          }
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

    final URLClassLoader cl = new URLClassLoader(jarsUrls.toArray(new URL [0]));
    final Map<String, ComponentClassDescriptor> classMap = new HashMap<String, ComponentClassDescriptor>();

    for (final Entry<String, JarFile> entry : classNames.entrySet()) {
      Class<? extends Component> clazz;
      try {
        clazz = (Class<? extends Component>) cl.loadClass(entry.getKey());
        if (Component.class.isAssignableFrom(clazz)) {
          classMap.put(entry.getKey(), new ComponentClassDescriptor(clazz, entry.getValue()));
        }
      } catch (final ClassNotFoundException e) {
        System.err.println("Can't load class " + entry.getKey());
        e.printStackTrace();
      }
    }

    try {
      cl.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return classMap;
  }

  /**
   * Component descriptor.
   * @author Pustovit Michael, pustovitm@gmail.com
   */
  public static class ComponentClassDescriptor {
    /** Component jar. */
    private final JarFile componentJar;

    /** Component class. */
    private final Class<? extends Component> componentClass;

    /**
     * @param componentClass the component class
     * @param componentJar the jar which contains this class
     */
    public ComponentClassDescriptor(final Class<? extends Component> componentClass, final JarFile componentJar) {
      this.componentClass = componentClass;
      this.componentJar = componentJar;
    }

    /**
     * @return the component class
     */
    public Class<? extends Component> getComponentClass() {
      return componentClass;
    }

    /**
     * @return the components jar
     */
    public JarFile getComponentJar() {
      return componentJar;
    }

    @Override
    public String toString() {
      return "ComponentDescriptor [componentJar=" + componentJar + ", componentClass=" + componentClass + "]";
    }
  }
}
