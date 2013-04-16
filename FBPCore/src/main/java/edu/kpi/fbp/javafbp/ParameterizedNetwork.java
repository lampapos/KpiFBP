package edu.kpi.fbp.javafbp;

import java.text.MessageFormat;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.Network;

import edu.kpi.fbp.params.Parameter;
import edu.kpi.fbp.params.ParameterBundle;
import edu.kpi.fbp.params.ParametersStore;
import edu.kpi.fbp.params.adapters.BooleanAdapter;
import edu.kpi.fbp.params.adapters.FloatAdapter;
import edu.kpi.fbp.params.adapters.IntegerAdapter;
import edu.kpi.fbp.params.adapters.ParameterAdapter;
import edu.kpi.fbp.params.adapters.StringAdapter;

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
      this.parametersStore = new ParametersStore.Builder(0).build();
    } else {
      this.parametersStore = paramStore;
    }
  }

  @Override
  protected void callDefine() throws Exception {
    super.callDefine();

    for (final Component comp : getComponents().values()) {
        final ComponentDescriptor desc = ComponentDescriptor.buildDescriptor(comp.getClass());
        final ParameterBundle parameters = parametersStore.getComponentParameters(comp.getName(), desc);

        for (final InPort port : desc.getInPorts()) {
          if (parameters.contains(port.value())) {
            try {
              final Parameter param = parameters.get(port.value());

              final ParameterAdapter<?> adapter = getParameterAdapter(port.type());
              if (adapter == null) {
                System.err.println("There isn't converter for type " + port.type());
              }

              initialize(adapter.convert(param.getValue()),
                  MessageFormat.format("{0}.{1}", comp.getName(), port.value()));
            } catch (final Exception e) {
              // port connected and cann't be initialized
              System.out.println("Port " + MessageFormat.format("{0}.{1}", comp.getName(), port.value())
                  + " connected so it hasn't been initialized.");
            }
          }
        }
    }
  }

  private ParameterAdapter<?> getParameterAdapter(final Class<?> portType) {
    if (portType.equals(Integer.class)) {
      return new IntegerAdapter();
    }

    if (portType.equals(Boolean.class)) {
      return new BooleanAdapter();
    }

    if (portType.equals(String.class)) {
      return new StringAdapter();
    }

    if (portType.equals(Float.class)) {
      return new FloatAdapter();
    }

    return null;
  }

}
