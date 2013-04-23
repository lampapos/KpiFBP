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
   * $$Назва параметру.$$
   */
  String port();

  /**
   * The parameter type (used for user input validation). ParameterType constants should be used.
   * $$Тип параметру (використовується при валідації значення параметра). У якості типу має бути використана константа з ParameterType.$$
   */
  ParameterType type();

  /**
   * The default parameter value.
   * $$Значення параметра за замовчуванням.$$
   */
  String defaultValue();
}
