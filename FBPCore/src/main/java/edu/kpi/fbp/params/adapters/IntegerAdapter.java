package edu.kpi.fbp.params.adapters;

import edu.kpi.fbp.params.ParameterAdapter;

public class IntegerAdapter implements ParameterAdapter<Integer> {

  public Integer convert(final String param) {
    return Integer.parseInt(param);
  }

  public boolean validate(final String param) {
    // TODO Auto-generated method stub
    return false;
  }

}
