package edu.kpi.fbp.params.adapters;


/**
 * Boolean adapter.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class BooleanAdapter implements ParameterAdapter<Boolean> {

  @Override
  public Boolean convert(final String param) {
    return Boolean.parseBoolean(param);
  }

  @Override
  public boolean validate(final String param) {
    // TODO Auto-generated method stub
    return false;
  }

}
