package edu.kpi.fbp.javafbp;



import com.jpmorrsn.fbp.engine.Component;

import edu.kpi.fbp.params.ParameterBundle;



/**
 * Base class for all parameterized components.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public abstract class ParameterizedComponent extends Component {

  /**
   * This method should initialize parameters values.
   * @param bundle the parameter bundle
   */
  public abstract void setParameters(final ParameterBundle bundle);

}
