package edu.kpi.fbp.params;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


/**
 * Component parameter bundle (map, where key is parameter name).
 *
 * $$Словник, що у якості ключа використовує назву параметра компонента, а у якості значення - дескриптор параметру.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@XStreamAlias("bundle")
public class ParameterBundle {
  /** Parameters list. */
  @XStreamImplicit
  private final List<Parameter> parameters;

  /** Store. We transform parameters list in map <parameter name, parameter value>. */
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

  /**
   * Parameter descriptor getter.
   *
   * $$Віддати описувач параметру за його строковим ім'ям.$$
   *
   * @param paramName the parameter name
   * @return parameter descriptor.
   */
  public Parameter get(final String paramName) {
    return store.get(paramName);
  }

  /**
   * Parameter value getter.
   *
   * $$Отримати значення параметру, приведене до типу String.$$
   *
   * @param paramName the parameter name $$ім'я параметру$$
   * @return the parameter value casted to string $$значення параметру, приведене до типу String$$
   */
  public String getString(final String paramName) {
    final Parameter param = store.get(paramName);

    if (param != null) {
      return param.getValue();
    }

    return null;
  }

  /**
   * Parameter value getter.
   *
   * $$Отримати значення параметру, приведене до типу Double.$$
   *
   * @param paramName the parameter name $$ім'я параметру$$
   * @return the parameter value casted to double
   */
  public Double getDouble(final String paramName) {
    final String value = getString(paramName);

    if (value != null) {
      return Double.parseDouble(value);
    }

    return null;
  }

  /**
   * Parameter value getter.
   *
   * $$Отримати значення параметру, приведене до типу Long.$$
   *
   * @param paramName the parameter name $$ім'я параметру$$
   * @return the parameter value casted to long
   */
  public Long getLong(final String paramName) {
    final String value = getString(paramName);

    if (value != null) {
      return Long.parseLong(value);
    }

    return null;
  }

  /**
   * Parameter value getter.
   *
   * $$Отримати значення параметру, приведене до типу Integer.$$
   *
   * @param paramName the parameter name $$ім'я параметру$$
   * @return the parameter value casted to integer
   */
  public Integer getInt(final String paramName) {
    final String value = getString(paramName);

    if (value != null) {
      return Integer.parseInt(value);
    }

    return null;
  }

  /**
   * Parameter value getter.
   *
   * $$Отримати значення параметру, приведене до типу Boolean.$$
   *
   * @param paramName the parameter name $$ім'я параметру$$
   * @return the parameter value casted to boolean
   */
  public Boolean getBoolean(final String paramName) {
    final String value = getString(paramName);

    if (value != null) {
      return Boolean.parseBoolean(value);
    }

    return null;
  }

  /**
   * If this bundle contains parameter with defined name.
   *
   * $$Чи містить данний словник параметр зі вказанним ім'ям.$$
   *
   * @param name parameter name $$ім'я параметру$$
   * @return if bundle contains parameter with such name $$чи містить данний словник параметр зі вказанним ім'ям$$
   */
  public boolean contains(final String name) {
    return store.containsKey(name);
  }

  /**
   * @return the parameters list
   */
  public List<Parameter> getParameters() {
    return parameters;
  }
}
