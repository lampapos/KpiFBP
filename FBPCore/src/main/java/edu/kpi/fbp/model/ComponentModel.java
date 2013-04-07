package edu.kpi.fbp.model;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Component model.
 *
 * $$Модель дескриптора компоненту мережі.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@XStreamAlias("component")
public class ComponentModel {
  /** The component Java class name. */
  @XStreamAsAttribute
  private final String className;
  /** The component name (must be unique within single network). */
  @XStreamAsAttribute
  private final String name;
  /** Port sizes map (<Port name, Port size>). */
  private final Map<String, Integer> portSizes;
  /** The component source URL - where we can find component sources. */
  @XStreamAsAttribute
  private final String sourceUrl;

  /**
   * @param className the component Java class name $$повне ім'я Java-класу, що відповідає даному компоненту$$
   * @param name the component name (must be unique within single network) $$ім'я компоненту (має бути унікальним у межах однієї мережі)$$
   * @param portSizes port sizes map (<Port name, Port size>) $$словник з розмірами портів-масивів$$
   * @param sourceUrl the component source URL - where we can find component sources $$URL адреса сховища, де можна знайти JAR-архів, що містить класс компонента$$
   */
  public ComponentModel(
      final String className,
      final String name,
      final Map<String, Integer> portSizes,
      final String sourceUrl) {
    this.className = className;
    this.name = name;
    this.portSizes = portSizes;
    this.sourceUrl = sourceUrl;
  }

  /**
   * @return the component Java class name $$повне ім'я Java-класу, що відповідає даному компоненту$$
   */
  public final String getClassName() {
    return className;
  }

  /**
   * @return the component name (must be unique within single network) $$ім'я компоненту (має бути унікальним у межах однієї мережі)$$
   */
  public final String getName() {
    return name;
  }

  /**
   * @return port sizes map (<Port name, Port size>)  $$словник з розмірами портів-масивів$$
   */
  public final Map<String, Integer> getPortSizes() {
    return portSizes;
  }

  /**
   * @return the component source URL - where we can find component sources $$URL адреса сховища, де можна знайти JAR-архів, що містить класс компонента$$
   */
  public final String getSourceUrl() {
    return sourceUrl;
  }

  @Override
  public String toString() {
    return "ComponentModel [className=" + className + ", name=" + name + ", portSizes=" + portSizes + ", sourceUrl="
        + sourceUrl + "]";
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
    result = prime * result + ((className == null) ? 0 : className.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((portSizes == null) ? 0 : portSizes.hashCode());
    result = prime * result + ((sourceUrl == null) ? 0 : sourceUrl.hashCode());
    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ComponentModel other = (ComponentModel) obj;
    if (className == null) {
      if (other.className != null) {
        return false;
      }
    } else if (!className.equals(other.className)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (portSizes == null) {
      if (other.portSizes != null) {
        return false;
      }
    } else if (!portSizes.equals(other.portSizes)) {
      return false;
    }
    if (sourceUrl == null) {
      if (other.sourceUrl != null) {
        return false;
      }
    } else if (!sourceUrl.equals(other.sourceUrl)) {
      return false;
    }
    return true;
  }
  //CHECKSTYLE:ON

}
