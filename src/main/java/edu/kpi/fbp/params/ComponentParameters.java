package edu.kpi.fbp.params;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Component parameters list annotation.
 *
 * $$Аннотація списку параметрів компонента (містить список анотацій ComponentParameter).$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentParameters {

  /**
   * Component parameter. If component has single parameter so this annotation may be not used.
   * $$Параметри компонента. Якщо у компонента тільки один параметр, то дану анотацію використовувати не обов'язково.$$
   */
  ComponentParameter[] value();
}
