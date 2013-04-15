package edu.kpi.fbp.params.adapters;

import edu.kpi.fbp.params.ParameterAdapter;

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
