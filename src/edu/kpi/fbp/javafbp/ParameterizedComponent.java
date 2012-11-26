package edu.kpi.fbp.javafbp;

import com.jpmorrsn.fbp.engine.Component;

import edu.kpi.fbp.params.ParameterBundle;

/**
 * Base class for all parameterized components.
 *
 * $$Базовий клас для параметризованих компонентів.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public abstract class ParameterizedComponent extends Component {

  /**
   * This method should initialize parameters values.
   *
   * $$Цей метод має бути перевизначений у класі-реалізаціх і при його визові, параметри будуть передані до компоненту.$$
   *
   * @param bundle the parameter bundle
   */
  public abstract void setParameters(final ParameterBundle bundle);

}
