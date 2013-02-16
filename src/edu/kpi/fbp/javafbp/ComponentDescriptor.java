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
 *
 * $$Клас, що надає зручний доступ до опису компонента (наприклад до імен портів, розміру портів-массивів, параметрів
 * компонента, тощо).$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class ComponentDescriptor {

  /** the component input port list $$список вхідних портів компонента$$. */
  private final List<InPort> inPorts;

  /** the component output port list $$список вихідних портів компонента$$. */
  private final List<OutPort> outPorts;

  /** the component parameters list $$список параметрів компонента$$. */
  private final List<ComponentParameter> parameters;

  /** the component description $$опис компоненту$$. */
  private final String description;

  /** if this component is self-starting $$чи э компонент самостартуючим$$. */
  private final boolean isComponentSelfStarting;

  /** if this component must run $$чи має компонент бути запущений хочаб один раз$$. */
  private final boolean isMustRun;

  /**
   * if priority isn't defined for the component so "-1" is returned.
   * $$приорітет виконання компоненту, або -1 якщо даний параметр не зазначено$$
   */
  private final int priority;

  /**
   * Default constructor.
   * @param componentClass the component class $$класс компонента, для якого будується дескриптор$$
   */
  public ComponentDescriptor(final Class<? extends Component> componentClass) {
    inPorts = ComponentDescriptorUtils.getInputPorts(componentClass);
    outPorts = ComponentDescriptorUtils.getOutputPorts(componentClass);
    parameters = ComponentDescriptorUtils.getParameters(componentClass);
    description = ComponentDescriptorUtils.getComponentDescription(componentClass);
    isComponentSelfStarting = ComponentDescriptorUtils.isComponentSelfStarting(componentClass);
    isMustRun = ComponentDescriptorUtils.isComponentMustRun(componentClass);
    priority = ComponentDescriptorUtils.getComponentPriority(componentClass);
  }

  /**
   * @return the component input port list $$список вхідних портів компонента$$
   */
  public List<InPort> getInPorts() {
    return inPorts;
  }

  /**
   * @return the component output port list $$список вихідних портів компонента$$
   */
  public List<OutPort> getOutPorts() {
    return outPorts;
  }

  /**
   * @return the component parameters list $$список параметрів компонента$$
   */
  public List<ComponentParameter> getParameters() {
    return parameters;
  }

  /**
   * @return the component description $$опис компоненту$$
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return if this component is self-starting $$чи э компонент самостартуючим$$
   */
  public boolean isComponentSelfStarting() {
    return isComponentSelfStarting;
  }

  /**
   * @return if this component must run $$чи має компонент бути запущений хочаб один раз$$
   */
  public boolean isMustRun() {
    return isMustRun;
  }

  /**
   * @return if priority isn't defined for the component so "-1" is returned
   *         $$приорітет виконання компоненту, або -1 якщо даний параметр не зазначено$$
   */
  public int getPriority() {
    return priority;
  }

  /**
   * Utility class which contains instrument for {@link ComponentDescriptor} constructing.
   *
   * $$Утилітарний клас, що надає інструменти для побудови дескриптора компонента.$$
   *
   * @author Pustovit Michael, pustovitm@gmail.com
   */
  public static final class ComponentDescriptorUtils {

    private ComponentDescriptorUtils() {
      // utils class mustn't have public constructor
    }

    /**
     * Getter for component input port list.
     *
     * $$Віддати список вхідних портів компонента.$$
     *
     * @param componentClass the component class $$клас компонента$$
     * @return the component input port list $$список вхідних портів компонента$$
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
     *
     * $$Віддати список вихідних портів компонента.$$
     *
     * @param componentClass the component class $$клас компонента$$
     * @return the component output port list $$список вихідних портів компонента$$
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
     * $$Віддати список параметрів компонента.$$
     *
     * @param componentClass the component class $$клас компонента$$
     * @return the component parameters list $$список параметрів компонента$$
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
     * $$Віддати опис компонента.$$
     *
     * @param componentClass the component class $$клас компонента$$
     * @return the component description $$опис компоненту$$
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
     * $$Чи э компонент самостартуючим? (Це параметр будь-якого компонента у JavaFBP).$$
     *
     * @param componentClass the component class $$клас компонента$$
     * @return if this component is self-starting $$чи э компонент самостартуючим$$
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
     * $$Чи має компонент бути запущений хочаб один раз?$$
     *
     * @param componentClass the component class $$клас компонента$$
     * @return if this component must run $$чи має компонент бути запущений хочаб один раз$$
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
     * $$Віддати пріоритет виконання потоку компонента.$$
     * @param componentClass the component class $$клас компонента$$
     * @return if priority isn't defined for the component so "-1" is returned
     *        $$приорітет виконання компоненту, або -1 якщо даний параметр не зазначено$$
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
}
