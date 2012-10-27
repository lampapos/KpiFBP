package org.kpi.fict.fbp.model;

/**
 * Network link model.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class LinkModel {
  /** The component where link begins. */
  final String fromComponent;
  /** The component port where link begins. */
  final String fromPort;
  /** The component where link ends. */
  final String toComponent;
  /** The component port where link ends. */
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

}
