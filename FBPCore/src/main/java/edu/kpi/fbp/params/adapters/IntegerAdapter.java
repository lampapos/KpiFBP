package edu.kpi.fbp.params.adapters;


/**
 * Integer adapter.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class IntegerAdapter implements ParameterAdapter<Integer> {

  @Override
  public Integer convert(final String param) {
    return Integer.parseInt(param);
  }

  @Override
  public boolean validate(final String param) {
    // TODO
    return false;
  }

}
