package edu.kpi.fbp.params;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Component parameter annotation.
 *
 * $$Аннотація параметру компонента.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentParameter {

  /**
   * The parameter name.
   */
  String name();

  /**
   * The parameter type (used for user input validation). ParameterType constants should be used.
   */
  ParameterType type();

  /**
   * The default parameter value.
   */
  String defaultValue();
}
