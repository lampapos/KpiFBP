package edu.kpi.fbp.params;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import edu.kpi.fbp.javafbp.ParameterizedComponent;


/**
 * Parameters store.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@XStreamAlias("ParametersStore")
public class ParametersStore {
  /** Parameters store in form of map <component name, component parameters bundle>. */
  private Map<String, ParameterBundle> store;

  /**
   * Utility method which gives array of сomponent parameters.
   * @param componentClass the component class
   * @return the component class parameters
   */
  public static List<ComponentParameter> getComponentClassParameters(
      final Class<? extends ParameterizedComponent> componentClass) {
    final List<ComponentParameter> res = new ArrayList<ComponentParameter>();

    if (componentClass.isAnnotationPresent(ComponentParameters.class)) {
      final ComponentParameters paramsAnnotation = componentClass.getAnnotation(ComponentParameters.class);
      res.addAll(Arrays.asList(paramsAnnotation.value()));
    }

    if (componentClass.isAnnotationPresent(ComponentParameter.class)) {
      final ComponentParameter paramsAnnotation = componentClass.getAnnotation(ComponentParameter.class);
      res.add(paramsAnnotation);
    }

    return res;
  }

  /**
   * Component parameters getter.
   * @param componentName component name
   * @param componentClass component class
   * @return component parameters (all parameters. If some parameters aren't in store we use default values).
   */
  public ParameterBundle getComponentParameters(final String componentName,
      final Class<? extends ParameterizedComponent> componentClass) {
    final List<ComponentParameter> classParameters = getComponentClassParameters(componentClass);

    ParameterBundle definedParameters = store.get(componentName);
    if (definedParameters == null) {
      definedParameters = new ParameterBundle(new ArrayList<Parameter>());
    }

    final List<Parameter> res = new ArrayList<Parameter>();

    for (final ComponentParameter param : classParameters) {
      if (definedParameters.contains(param.name())) {
        res.add(definedParameters.get(param.name()));
      } else {
        res.add(new Parameter(param.name(), param.defaultValue()));
      }
    }

    return new ParameterBundle(res);
  }

  /**
   * Parameter store builder.
   * @author Pustovit Michael, pustovitm@gmail.com
   */
  public static class Builder {

    /** Result store. */
    private final Map<String, ParameterBundle> store;

    /** Default constructor. */
    public Builder() {
      store = new HashMap<String, ParameterBundle>();
    }

    /**
     * Add component configuration.
     * @param componentName the component name
     * @param parameters the component parameter list
     * @return the builder for chaining
     */
    public Builder addComponentConfiguration(final String componentName, final List<Parameter> parameters) {
      store.put(componentName, new ParameterBundle(parameters));
      return this;
    }

    /**
     * @return the builded instance
     */
    public ParametersStore build() {
      final ParametersStore res = new ParametersStore();

      res.store = store;

      return res;
    }

  }
}
