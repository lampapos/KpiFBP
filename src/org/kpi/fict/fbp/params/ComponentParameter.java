package org.kpi.fict.fbp.params;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentParameter {

  /**
   * @return the parameter name
   */
  String name();

  /**
   * @return the parameter type (used for user input validation)
   */
  ParameterType type();

  /**
   * @return the default parameters value
   */
  String defaultValue();
}
