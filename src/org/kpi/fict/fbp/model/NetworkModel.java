package org.kpi.fict.fbp.model;

import java.util.List;
import java.util.Map;

/**
 * Network structure.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class NetworkModel {
  /** Network components. */
  private final List<ComponentModel> components;

  /** Network links. */
  private final List<LinkModel> links;

  /** Extra network info (graphic information, etc). */
  private final Map<String, Object> extra;

  /**
   * Default constructor.
   * @param components the list of network components
   * @param links the list of network links
   * @param extra the extra network information
   */
  public NetworkModel(final List<ComponentModel> components,
      final List<LinkModel> links,
      final Map<String, Object> extra) {
    this.components = components;
    this.links = links;
    this.extra = extra;
  }

  /**
   * @return the network components
   */
  public final List<ComponentModel> getComponents() {
    return components;
  }

  /**
   * @return the network links
   */
  public final List<LinkModel> getLinks() {
    return links;
  }

  /**
   * @return the network extra information
   */
  public final Map<String, Object> getExtra() {
    return extra;
  }

  @Override
  public String toString() {
    return "NetworkModel [components=" + components + ", links=" + links + ", extra=" + extra + "]";
  }

}
