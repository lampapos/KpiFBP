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
   * @param networkName the network name $$Назва мережі$$
   * @param components the list of network components $$Перелік компонентів мережі$$
   * @param links the list of network links $$Перелік зв'язків мережі$$
   * @param extra the extra network information $$Додаткова інформація про мережу (наприклад, для відображення мережі)$$
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
   * @return the network name $$Назва мережі$
   */
  public String getNetworkName() {
    return networkName;
  }

  /**
   * @return the network components $$Перелік компонентів мережі$$
   */
  public final List<ComponentModel> getComponents() {
    return components;
  }

  /**
   * @return the network links $$Перелік зв'язків мережі$$
   */
  public final List<LinkModel> getLinks() {
    return links;
  }

  /**
   * @return the network extra information $$Додаткова інформація про мережу (наприклад, для відображення мережі)$$
   */
  public final Map<String, Object> getExtra() {
    return extra;
  }

  @Override
  public String toString() {
    return "NetworkModel [components=" + components + ", links=" + links + ", extra=" + extra + "]";
  }

}
