package edu.kpi.fbp.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Network link model.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@XStreamAlias("link")
public class LinkModel {
  /** The component where link begins. */
  @XStreamAsAttribute
  final String fromComponent;
  /** The component port where link begins. */
  @XStreamAsAttribute
  final String fromPort;
  /** The component where link ends. */
  @XStreamAsAttribute
  final String toComponent;
  /** The component port where link ends. */
  @XStreamAsAttribute
  final String toPort;

  /**
   * @param fromComponent the component where link begins
   * @param fromPort the component port where link begins
   * @param toComponent the component where link ends
   * @param toPort the component port where link ends
   */
  public LinkModel(final String fromComponent, final String fromPort, final String toComponent, final String toPort) {
    this.fromComponent = fromComponent;
    this.fromPort = fromPort;
    this.toComponent = toComponent;
    this.toPort = toPort;
  }

  /**
   * @return the component where link begins
   */
  public final String getFromComponent() {
    return fromComponent;
  }

  /**
   * @return the component port where link begins
   */
  public final String getFromPort() {
    return fromPort;
  }

  /**
   * @return the component where link ends
   */
  public final String getToComponent() {
    return toComponent;
  }

  /**
   * @return the component port where link ends
   */
  public final String getToPort() {
    return toPort;
  }

  /**
   * @return the full "from" string (e.g. "Generator.OUT")
   */
  public final String getFrom() {
    return String.format("%s.%s", fromComponent, fromPort);
  }

  /**
   * @return the full "to" string (e.g. "Summator.IN")
   */
  public final String getTo() {
    return String.format("%s.%s", toComponent, toPort);
  }

  @Override
  public String toString() {
    return "LinkModel [fromComponent=" + fromComponent + ", fromPort=" + fromPort + ", toComponent=" + toComponent
        + ", toPort=" + toPort + "]";
  }

}
