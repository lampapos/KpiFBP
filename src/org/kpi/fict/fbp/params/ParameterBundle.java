package org.kpi.fict.fbp.params;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


/**
 * Component parameter bundle (map, where key is parameter name).
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@XStreamAlias("bundle")
public class ParameterBundle {
  @XStreamImplicit
  private final List<Parameter> parameters;

  private transient Map<String, Parameter> store;

  /**
   * @param bParameters the parameter list
   */
  public ParameterBundle(final List<Parameter> bParameters) {
    this.parameters = bParameters;
    init();
  }

  private void init() {
    store = new HashMap<String, Parameter>();

    for (final Parameter param : parameters) {
      store.put(param.getName(), param);
    }
  }

  private Object readResolve() {
    init();
    return this;
  }

  public Parameter get(final String paramName) {
    return store.get(paramName);
  }

  public String getString(final String paramName) {
    final Parameter param = store.get(paramName);

    if (param != null) {
      return param.getValue();
    }

    return null;
  }

  public Double getDouble(final String paramName) {
    final String value = getString(paramName);

    if (value != null) {
      return Double.parseDouble(value);
    }

    return null;
  }

  public Long getLong(final String paramName) {
    final String value = getString(paramName);

    if (value != null) {
      return Long.parseLong(value);
    }

    return null;
  }

  public Integer getInt(final String paramName) {
    final String value = getString(paramName);

    if (value != null) {
      return Integer.parseInt(value);
    }

    return null;
  }

  public Boolean getBoolean(final String paramName) {
    final String value = getString(paramName);

    if (value != null) {
      return Boolean.parseBoolean(value);
    }

    return null;
  }

  /**
   * @param name parameter name
   * @return if bundle contains parameter with such name
   */
  public boolean contains(final String name) {
    return store.containsKey(name);
  }
}
