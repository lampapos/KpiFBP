package edu.kpi.fbp.params;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Parameter which shows where component source code may be found.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentUrl {
  /** The URL where component source code may be found. */
  String value();
}
