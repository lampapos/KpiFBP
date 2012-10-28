package edu.kpi.fbp.model;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Component model.
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
   * @param className the component Java class name
   * @param name the component name (must be unique within single network)
   * @param portSizes port sizes map (<Port name, Port size>)
   * @param sourceUrl the component source URL - where we can find component sources
   */
  public ComponentModel(final String className,
      final String name,
      final Map<String, Integer> portSizes,
      final String sourceUrl) {
    this.className = className;
    this.name = name;
    this.portSizes = portSizes;
    this.sourceUrl = sourceUrl;
  }

  /**
   * @return the component Java class name
   */
  public final String getClassName() {
    return className;
  }

  /**
   * @return the component name (must be unique within single network)
   */
  public final String getName() {
    return name;
  }

  /**
   * @return port sizes map (<Port name, Port size>)
   */
  public final Map<String, Integer> getPortSizes() {
    return portSizes;
  }

  /**
   * @return the component source URL - where we can find component sources
   */
  public final String getSourceUrl() {
    return sourceUrl;
  }

  @Override
  public String toString() {
    return "ComponentModel [className=" + className + ", name=" + name + ", portSizes=" + portSizes + ", sourceUrl="
        + sourceUrl + "]";
  }

}
