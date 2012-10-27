package edu.kpi.fbp.params;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;


/**
 * Component parameter.
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
   * @param pName the parameter name
   * @param pValue the parameter value
   */
  public Parameter(final String pName, final String pValue) {
    this.name = pName;
    this.value = pValue;
  }

  /**
   * @return the name
   */
  public final String getName() {
    return name;
  }

  /**
   * @return the parameter value
   */
  public final String getValue() {
    return value;
  }
}
