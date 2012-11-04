package edu.kpi.fbp.javafbp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.MustRun;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;
import com.jpmorrsn.fbp.engine.Priority;
import com.jpmorrsn.fbp.engine.SelfStarting;

import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ComponentParameters;

/**
 * Class which gives handy access to the component description (port names, array port sizes, parameters, etc).
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class ComponentDescriptor {

  private ComponentDescriptor() {

  }

  /**
   * Getter for component input port list.
   * @param componentClass the component class
   * @return the component input port list
   */
  public static List<InPort> getInputPorts(final Class<? extends Component> componentClass) {
    final List<InPort> ports = new ArrayList<InPort>();

    final InPort sinleInPort = componentClass.getAnnotation(InPort.class);
    if (sinleInPort != null) {
      ports.add(sinleInPort);
    }

    final InPorts inPorts = componentClass.getAnnotation(InPorts.class);
    if (inPorts != null) {
      ports.addAll(Arrays.asList(inPorts.value()));
    }

    return ports;
  }

  /**
   * Getter for component output port list.
   * @param componentClass the component class
   * @return the component output port list
   */
  public static List<OutPort> getOutputPorts(final Class<? extends Component> componentClass) {
    final List<OutPort> ports = new ArrayList<OutPort>();

    final OutPort singlePort = componentClass.getAnnotation(OutPort.class);
    if (singlePort != null) {
      ports.add(singlePort);
    }

    final OutPorts outPorts = componentClass.getAnnotation(OutPorts.class);
    if (outPorts != null) {
      ports.addAll(Arrays.asList(outPorts.value()));
    }

    return ports;
  }

  /**
   * Getter for component parameters list.
   * @param componentClass the component class
   * @return the component parameters list
   */
  public static List<ComponentParameter> getParameters(final Class<? extends Component> componentClass) {
    final List<ComponentParameter> params = new ArrayList<ComponentParameter>();

    final ComponentParameter singlePort = componentClass.getAnnotation(ComponentParameter.class);
    if (singlePort != null) {
      params.add(singlePort);
    }

    final ComponentParameters outPorts = componentClass.getAnnotation(ComponentParameters.class);
    if (outPorts != null) {
      params.addAll(Arrays.asList(outPorts.value()));
    }

    return params;
  }

  /**
   * Getter for component description.
   * @param componentClass the component class
   * @return the component description
   */
  public static String getComponentDescription(final Class<? extends Component> componentClass) {
    final ComponentDescription desc = componentClass.getAnnotation(ComponentDescription.class);
    if (desc == null) {
      return null;
    } else {
      return desc.value();
    }
  }

  /**
   * Is component self starting.
   * @param componentClass the component class
   * @return if this component is self-starting
   */
  public static boolean isComponentSelfStarting(final Class<? extends Component> componentClass) {
    final SelfStarting isSelfStarting = componentClass.getAnnotation(SelfStarting.class);

    if (isSelfStarting == null) {
      return false;
    } else {
      return isSelfStarting.value();
    }
  }

  /**
   * Is component must run.
   * @param componentClass the component class
   * @return if this component is self-starting
   */
  public static boolean isComponentMustRun(final Class<? extends Component> componentClass) {
    final MustRun isMustRun = componentClass.getAnnotation(MustRun.class);

    if (isMustRun == null) {
      return false;
    } else {
      return isMustRun.value();
    }
  }

  /**
   * Getter for component priority.
   * @param componentClass the component class
   * @return if priority isn't defined for the component so "-1" is returned
   */
  public static int getComponentPriority(final Class<? extends Component> componentClass) {
    final Priority priority = componentClass.getAnnotation(Priority.class);

    if (priority == null) {
      return -1;
    } else {
      return priority.value();
    }
  }

}
