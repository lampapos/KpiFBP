package edu.kpi.fbp.params;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.Packet;

/**
 * The parameter utils class.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class ParamUtils {
  private ParamUtils() {
    // do nothing
  }

  /**
   * Utility method for handy parameter initialization.
   *
   * @param component component with such port
   * @param port the port from which parameter will be read
   *
   * @param <T> the init value type
   * @return the init value
   */
  public static <T> T readParam(final Component component, final InputPort port) {
    @SuppressWarnings("unchecked")
    final Packet<T> pack = port.receive();
    final T res = pack.getContent();
    component.drop(pack);
    port.close();
    return res;
  }
}
