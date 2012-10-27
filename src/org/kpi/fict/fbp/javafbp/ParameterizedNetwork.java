package org.kpi.fict.fbp.javafbp;


import org.kpi.fict.fbp.params.ParameterBundle;
import org.kpi.fict.fbp.params.ParametersStore;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.Network;

/**
 * Extended network class componets of which uses parameters.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public abstract class ParameterizedNetwork extends Network {

  /** Global parameter store. */
  private final ParametersStore parametersStore;

  /**
   * @param paramStore parameter store
   */
  public ParameterizedNetwork(final ParametersStore paramStore) {
    this.parametersStore = paramStore;
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
