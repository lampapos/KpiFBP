package edu.kpi.fbp.params;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;


/**
 * Component parameter.
 *
 * $$Параметр компонента.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@XStreamAlias("parameter")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "value" })
public class Parameter {

  /** Parmeter name. */
  @XStreamAsAttribute
  private final String name;

  /** Parameter value. */
  private final String value;

  /**
   * Constructs component parameter.
   * $$Конструктор для параметра компоненту.$$
   * @param pName the parameter name $$ім'я параметру$$
   * @param pValue the parameter value $$назва параметру$$
   */
  public Parameter(final String pName, final String pValue) {
    this.name = pName;
    this.value = pValue;
  }

  /**
   * @return the name $$ім'я параметру$$
   */
  public final String getName() {
    return name;
  }

  /**
   * @return the parameter value $$назва параметру$$
   */
  public final String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "Parameter [name=" + name + ", value=" + value + "]";
  }
}
