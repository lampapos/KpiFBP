package edu.kpi.fbp.params;

public interface ParameterAdapter<ParamType> {
  ParamType convert(String param);
  boolean validate(String param);
}
