package edu.kpi.fbp.javafbp;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.Packet;

/**
 * Base class for all parameterized components.
 *
 * $$Базовий клас для параметризованих компонентів.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public abstract class ParameterizedComponent extends Component {
  /**
   * Utility method for handy parameter initialization.
   * @param port the port from which parameter will be read
   * @param <T> the init value type
   * @return the init value
   */
  protected <T> T readParam(final InputPort port) {
    @SuppressWarnings("unchecked")
    final Packet<T> pack = port.receive();
    final T res = pack.getContent();
    drop(pack);
    port.close();
    return res;
  }
}
