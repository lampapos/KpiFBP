package edu.kpi.fbp.utils;

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
   * @throws Exception any exception
   */
  public static void startNetwork(final NetworkModel model) throws Exception {
    startNetwork(model, model.getParameters());
  }

  /**
   * Start network with defined parameters.
   * @param model the network object model
   * @param parametersStore the parameter store (can be null)
   * @throws Exception any exception
   */
  public static void startNetwork(final NetworkModel model, final ParametersStore parametersStore) throws Exception {
    if (parametersStore.getNetworkHash() != model.getUniqueCode()) {
      throw new IllegalArgumentException("Unique network code and parameter store code aren't equal.");
    }

    new UniversalModel(parametersStore, model).go();
  }

  /**
   * Universal model which defines itself.
   * @author Pustovit Michael, pustovitm@gmail.com
   */
  private static class UniversalModel extends ParameterizedNetwork {
    /** Network object model. */
    private final NetworkModel model;

    public UniversalModel(final ParametersStore paramStore, final NetworkModel model) {
      super(paramStore);
      this.model = model;
    }

    @Override
    protected void define() throws Exception {
      for (final ComponentModel comp : model.getComponents()) {
        component(comp.getName(), Class.forName(comp.getClassName()));
      }

      for (final LinkModel link : model.getLinks()) {
        connect(link.getFrom(), link.getTo());
      }
    }

  }
}
