package edu.kpi.fbp.params.adapters;

/**
 * Init values are string values so they should be converted in the port type object. Parameter adapters make this
 * conversion.
 *
 * @param <ParamType> the port type
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public interface ParameterAdapter<ParamType> {
  /**
   * @param param the string init value representation
   * @return converted init value
   */
  ParamType convert(String param);
  /**
   * @param param the init string representation
   * @return if this value is valid (canbe converted)
   */
  boolean validate(String param);
}
