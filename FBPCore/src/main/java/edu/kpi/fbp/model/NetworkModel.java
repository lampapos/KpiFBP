package edu.kpi.fbp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import edu.kpi.fbp.params.ParametersStore;

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

  private final ParametersStore parameters;

  /** Extra network info (graphic information, etc). */
  private final Map<String, Object> extra;

  /**
   * Default constructor.
   * @param networkName the network name $$Назва мережі$$
   * @param components the list of network components $$Перелік компонентів мережі$$
   * @param links the list of network links $$Перелік зв'язків мережі$$
   * @param parameters the network components parameters
   * @param extra the extra network information $$Додаткова інформація про мережу (наприклад, для відображення мережі)$$
   */
  public NetworkModel(
      final String networkName,
      final List<ComponentModel> components,
      final List<LinkModel> links,
      final ParametersStore parameters,
      final Map<String, Object> extra) {
    this.networkName = networkName;
    this.components = components;
    this.links = links;
    this.parameters = parameters;
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
   * @return the network parameter store
   */
  public ParametersStore getParameters() {
    return parameters;
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


  /**
   * Object hash code (but hash function doesn't include "extra" fields).
   * @return the unique network code
   */
  public int getUniqueCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((components == null) ? 0 : components.hashCode());
    result = prime * result + ((links == null) ? 0 : links.hashCode());
    result = prime * result + ((networkName == null) ? 0 : networkName.hashCode());
    return result;
  }

  // Automatically generated hashTo and equals
  //CHECKSTYLE:OFF
  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((components == null) ? 0 : components.hashCode());
    result = prime * result + ((extra == null) ? 0 : extra.hashCode());
    result = prime * result + ((links == null) ? 0 : links.hashCode());
    result = prime * result + ((networkName == null) ? 0 : networkName.hashCode());
    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final NetworkModel other = (NetworkModel) obj;
    if (components == null) {
      if (other.components != null)
        return false;
    } else if (!components.equals(other.components))
      return false;
    if (extra == null) {
      if (other.extra != null)
        return false;
    } else if (!extra.equals(other.extra))
      return false;
    if (links == null) {
      if (other.links != null)
        return false;
    } else if (!links.equals(other.links))
      return false;
    if (networkName == null) {
      if (other.networkName != null)
        return false;
    } else if (!networkName.equals(other.networkName))
      return false;
    return true;
  }
  // CHECKSTYLE:ON

}
