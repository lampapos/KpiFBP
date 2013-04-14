package edu.kpi.fbp.utils;

import java.io.File;

import edu.kpi.fbp.javafbp.ParameterizedNetwork;
import edu.kpi.fbp.model.ComponentModel;
import edu.kpi.fbp.model.LinkModel;
import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.params.ParametersStore;

/**
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class NetworkStarter {

  private NetworkStarter() {
    // do nothing
  }

  /**
   * Start network with default parameters values.
   * @param model the network object model
   * @param componentDir the directory which contains component lib jars
   * @param libsDir the directory which contains all network components dependency libraries
   * @throws Exception any exception
   */
  public static void startNetwork(
      final NetworkModel model,
      final File componentDir,
      final File libsDir) throws Exception {
    new UniversalModel(model.getParameters(), model, Utils.getJarsClassLoader(componentDir, libsDir)).go();
  }

  /**
   * Start network with defined parameters.
   * @param model the network object model
   * @param parametersStore the parameter store
   * @param componentDir the directory which contains component lib jars
   * @param libsDir the directory which contains all network components dependency libraries
   * @throws Exception any exception
   */
  public static void startNetwork(
      final NetworkModel model,
      final ParametersStore parametersStore,
      final File componentDir,
      final File libsDir) throws Exception {
    if (parametersStore != null && parametersStore.getNetworkHash() != model.getUniqueCode()) {
      throw new IllegalArgumentException("Unique network code and parameter store code aren't equal.");
    }

    new UniversalModel(parametersStore, model, Utils.getJarsClassLoader(componentDir, libsDir)).go();
  }

  /**
   * Universal model which defines itself.
   * @author Pustovit Michael, pustovitm@gmail.com
   */
  private static class UniversalModel extends ParameterizedNetwork {
    /** Network object model. */
    private final NetworkModel model;

    private final ClassLoader classLoader;

    public UniversalModel(final ParametersStore paramStore, final NetworkModel model, final ClassLoader classLoader) {
      super(paramStore);
      this.model = model;
      this.classLoader = classLoader;
    }

    @Override
    protected void define() throws Exception {
      for (final ComponentModel comp : model.getComponents()) {
        component(comp.getName(), classLoader.loadClass(comp.getClassName()));
      }

      for (final LinkModel link : model.getLinks()) {
        connect(link.getFrom(), link.getTo());
      }
    }

  }
}
