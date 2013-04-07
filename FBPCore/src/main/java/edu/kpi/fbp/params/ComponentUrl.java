package edu.kpi.fbp.params;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Parameter which shows where component source code may be found.
 *
 * $$Анотація, що містить URL сховища, де може бути знайдено JAR архів з даним компонетом.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentUrl {
  /**
   *  The URL where component source code may be found.
   *  $$URL сховища, де може бути знайдено JAR архів з даним компонетом.$$
   */
  String value();
}
