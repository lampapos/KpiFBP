package edu.kpi.fbp.javafbp;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.Network;

import edu.kpi.fbp.params.ParameterBundle;
import edu.kpi.fbp.params.ParametersStore;

/**
 * Extended network class componets of which uses parameters.
 *
 * $$Модернізований клас мережі JavaFBP.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public abstract class ParameterizedNetwork extends Network {

  /** Global parameter store. */
  private final ParametersStore parametersStore;

  /**
   * @param paramStore parameter store (if it's null therefore default values will be used)
   */
  public ParameterizedNetwork(final ParametersStore paramStore) {
    if (paramStore == null) {
      this.parametersStore = new ParametersStore.Builder().build();
    } else {
      this.parametersStore = paramStore;
    }
  }

  @Override
  protected void callDefine() throws Exception {
    super.callDefine();

    for (final Component comp : getComponents().values()) {
      if (comp instanceof ParameterizedComponent) {
        final ParameterizedComponent castedComp = (ParameterizedComponent) comp;
        final ParameterBundle parameters = parametersStore.getComponentParameters(comp.getName(), castedComp.getClass());
        castedComp.setParameters(parameters);
      }
    }
  }

}
