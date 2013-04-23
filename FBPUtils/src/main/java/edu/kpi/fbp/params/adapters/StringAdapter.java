package edu.kpi.fbp.params.adapters;


/**
 * String adapter.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class StringAdapter implements ParameterAdapter<String> {

  @Override
  public String convert(final String param) {
    return param;
  }

  @Override
  public boolean validate(final String param) {
    // TODO Auto-generated method stub
    return false;
  }


}
