package edu.kpi.fbp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Network structure.
 *
 * $$Модель структури мережі.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@XStreamAlias("network")
public class NetworkModel {
  /** The network name. */
  @XStreamAsAttribute
  @XStreamAlias("name")
  private final String networkName;

  /** Network components. */
  private final List<ComponentModel> components;

  /** Network links. */
  private final List<LinkModel> links;

  /** Extra network info (graphic information, etc). */
  private final Map<String, Object> extra;

  /**
   * Default constructor.
   * @param networkName the network name
   * @param components the list of network components
   * @param links the list of network links
   * @param extra the extra network information
   */
  public NetworkModel(
      final String networkName,
      final List<ComponentModel> components,
      final List<LinkModel> links,
      final Map<String, Object> extra) {
    this.networkName = networkName;
    this.components = components;
    this.links = links;
    if (extra != null) {
      this.extra = extra;
    } else {
      this.extra = new HashMap<String, Object>();
    }
  }

  /**
   * @return the network name
   */
  public String getNetworkName() {
    return networkName;
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
