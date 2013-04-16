package edu.kpi.fbp.params.adapters;


/**
 * Float adapter.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class FloatAdapter implements ParameterAdapter<Float> {

  @Override
  public Float convert(final String param) {
    return Float.parseFloat(param);
  }

  @Override
  public boolean validate(final String param) {
    // TODO Auto-generated method stub
    return false;
  }

}
