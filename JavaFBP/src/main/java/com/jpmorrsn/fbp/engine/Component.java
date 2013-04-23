package com.jpmorrsn.fbp.engine;


import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * All components must extend this class, defining its two abstract methods
 * <code>openPorts<code> and <code>execute</code>.
 *
 * There will be an instance of this class for every node in the network.
 */

public abstract class Component extends Thread {

  /***************************************************************************
   * Copyright 2008, 2012, J. Paul Morrison. At your option, you may copy,
   * distribute, or make derivative works under the terms of the Clarified
   * Artistic License, based on the Everything Development Company's Artistic
   * License. A document describing this License may be found at
   * http://www.jpaulmorrison.com/fbp/artistic2.htm. THERE IS NO WARRANTY; USE
   * THIS PRODUCT AT YOUR OWN RISK.
   */

  // All the input ports are stored here, keyed by name.
  HashMap<String, InputPort> inputPorts = new HashMap<String, InputPort>();

  // All the output ports are stored here, keyed by name.
  HashMap<String, OutputPort> outputPorts = new HashMap<String, OutputPort>();

  // Input port attributes (metadata)
  //HashMap<String, InPort> inputPortAttrs = new HashMap<String, InPort>();

  // Output port attributes (metadata)
  //HashMap<String, OutPort> outputPortAttrs = new HashMap<String, OutPort>();

  /**
   * This is a stack which is made available to each component.
   */

  Stack<Packet> stack = new Stack<Packet>();

  // This is the automatic input port named "*IN"
  InputPort autoInput;

  // This is the automatic output port named "*OUT"
  OutputPort autoOutput;

  // This is a count of packets owned by this component.
  // Whenever the component deactivates, the count must be zero.
  int packetCount = 0;

  // This field maintains the Component status

  public enum StatusValues {
    NOT_STARTED, ACTIVE, DORMANT, SUSP_RECV, SUSP_SEND, TERMINATED, LONG_WAIT, ERROR;

    private int value;

    public int getValue() {
      return value;
    }
  }

  StatusValues status;

  // public static Network network;

  public Class type;

  boolean mustrun = false; // set by MustRun annotation

  boolean selfStarting = false; // set by SelfStarting annotation

  int priority;

  boolean autoStarting = false; // horrible word, but had to be changed to avoid conflict with selfStarting!

  public Network mother = null; // added for subnet support

  TimeoutHandler timeout = null;

  ReentrantLock goLock;

  Condition canGo;

  public Network network;

  InputPort curInPort = null;

  OutputPort curOutPort = null;

  public Component() {
    super();
    priority = Thread.NORM_PRIORITY;
    goLock = new ReentrantLock();
    canGo = goLock.newCondition();

  }

  protected void buildAnnotations() {
    final MustRun mr = this.getClass().getAnnotation(MustRun.class);
    if (mr == null) {
      mustrun = false;
    } else {
      mustrun = mr.value();
    }
    final SelfStarting ss = this.getClass().getAnnotation(SelfStarting.class);
    if (ss == null) {
      selfStarting = false;
    } else {
      selfStarting = ss.value();
    }

    final InPort ipt = this.getClass().getAnnotation(InPort.class);
    if (ipt != null) {
      procIpt(ipt);
    }

    final OutPort opt = this.getClass().getAnnotation(OutPort.class);
    if (opt != null) {
      procOpt(opt);
    }

    final InPorts ipts = this.getClass().getAnnotation(InPorts.class);
    if (ipts != null) {
      for (final InPort ip2 : ipts.value()) {
        procIpt(ip2);
      }
    }

    final OutPorts opts = this.getClass().getAnnotation(OutPorts.class);
    if (opts != null) {
      for (final OutPort op2 : opts.value()) {
        procOpt(op2);
      }
    }

    final Priority prio = this.getClass().getAnnotation(Priority.class);
    if (prio == null) {
      priority = Thread.NORM_PRIORITY;
    } else {
      priority = prio.value();
    }

  }

  void procIpt(final InPort ipt) {
    if (!(ipt.value().equals("") ^ ipt.valueList().length == 0)) {
      FlowError.complain(getName() + ": @InPort must have value or valueList, but not both");
    }
    String s;
    if (!ipt.value().equals("")) {
      s = ipt.value();
    } else {
      s = ipt.valueList()[0];
    }
    if (ipt.fixedSize() && !ipt.arrayPort()) {
      FlowError.complain(getName() + "." + s + ": @InPort specified fixedSize but not arrayPort");
    }
    if (!ipt.value().equals("")) {
      if (ipt.setDimension() > 0 && !ipt.value().endsWith("*")) {
        FlowError.complain(getName() + "." + s
            + ": @InPort specified setDimension but value string did not end with asterisk");
      }
      procIptx(ipt.value(), ipt);
    } else {
      boolean asterisk_found = false;
      for (final String t : ipt.valueList()) {
        if (t.endsWith("*")) {
          asterisk_found = true;
        }
        procIptx(t, ipt);
      }
      if (!asterisk_found && ipt.setDimension() > 0) {
        FlowError.complain(getName() + "." + s
            + ": @InPort specified setDimension but valueList did not contain any strings ending with asterisks");
      }
    }
  }

  void procIptx(final String s, final InPort ipt) {
    int i = ipt.setDimension();
    String t = s;
    if (!s.endsWith("*")) {
      i = 0;
    } else {
      t = s.substring(0, s.length() - 1);
      if (i == 0) {
        FlowError.complain(getName() + "." + s
            + ": Asterisk specified on input port name, but setDimension was not specified");
      }
    }

    if (i == 0) {
      procIpty(t, ipt);
    } else {
      for (int j = 0; j < i; j++) {
        procIpty(t + j, ipt);
      }
    }
  }

  void procIpty(final String s, final InPort ipt) {
    if (ipt.arrayPort()) {
      final ConnArray ca = new ConnArray();
      inputPorts.put(s, ca);
      ca.fixedSize = ipt.fixedSize();
      ca.name = s;
      ca.type = ipt.type();

    } else {
      final NullConnection nc = new NullConnection();
      nc.setName(s);
      nc.setReceiver(this);
      nc.type = ipt.type();
      inputPorts.put(s, nc);
    }
  }

  void procOpt(final OutPort opt) {
    if (!(opt.value().equals("") ^ opt.valueList().length == 0)) {
      FlowError.complain(getName() + ": @OutPort must have value or valueList, but not both");
    }
    String s;
    if (!opt.value().equals("")) {
      s = opt.value();
    } else {
      s = opt.valueList()[0];
    }
    if (opt.fixedSize() && !opt.arrayPort()) {
      FlowError.complain(getName() + "." + s + ": @OutPort specified fixedSize but not arrayPort");
    }
    if (!opt.value().equals("")) {
      if (opt.setDimension() > 0 && !opt.value().endsWith("*")) {
        FlowError.complain(getName() + "." + s
            + ": @OutPort specified setDimension but value string did not end with asterisk");
      }
      procOptx(opt.value(), opt);
    } else {
      boolean asterisk_found = false;
      for (final String t : opt.valueList()) {
        if (t.endsWith("*")) {
          asterisk_found = true;
        }
        procOptx(t, opt);
      }
      if (!asterisk_found && opt.setDimension() > 0) {
        FlowError.complain(getName() + "." + s
            + ": @OutPort specified setDimension but valueList did not contain any strings ending with asterisks");
      }
    }
  }

  void procOptx(final String s, final OutPort opt) {
    int i = opt.setDimension();
    String t = s;
    if (!s.endsWith("*")) {
      i = 0;
    } else {
      t = s.substring(0, s.length() - 1);
      if (i == 0) {
        FlowError.complain(getName() + "." + s
            + ": Asterisk specified on output port name, but setDimension was not specified");
      }
    }
    if (i == 0) {
      procOpty(t, opt);
    } else {
      for (int j = 0; j < i; j++) {
        procOpty(t + j, opt);
      }
    }
  }

  void procOpty(final String s, final OutPort opt) {
    if (opt.arrayPort()) {
      final OutArray oa = new OutArray();
      outputPorts.put(s, oa);
      oa.fixedSize = opt.fixedSize();
      oa.optional = opt.optional();
      oa.name = s;
      oa.type = opt.type();
    } else {
      final OutputPort op = new NullOutputPort();
      op.optional = opt.optional();
      op.type = opt.type();
      op.setSender(this);
      outputPorts.put(s, op);
      op.name = s;
    }
  }

  /**
   * This method is called from other parts of the system to activate this
   * Component if it needs to be. This will start its thread if needed, and if
   * already started, will notify() it.
   */
  /* synchronized */void activate() {

    if (isTerminated()) {
      return;
    }
    if (!isAlive()) {
      setPriority(priority);
      start();
    } else {
      mother.traceLocks("act - lock " + getName());
      try {
        goLock.lockInterruptibly();

        if (status == StatusValues.DORMANT) {
          canGo.signal();
          mother.traceLocks("act - signal " + getName());
        }
      } catch (final InterruptedException e) {
        return;
      } finally {
        goLock.unlock();
        mother.traceLocks("act - unlock " + getName());
      }
    }
  }

  /**
   * This method creates a new Packet using a type and String, say for
   * brackets
   *
   * @return Packet
   * @param s
   *            java.lang.String
   */
  public Packet create(final int newType, final String s) {
    network.creates.getAndIncrement();
    return new Packet(newType, s, this);
  }

  /**
   * This method creates a Packet pointing at an Object
   *
   * @return Packet
   * @param o
   *            java.lang.Object
   */
  public Packet create(final Object o) {
    network.creates.getAndIncrement();
    return new Packet(o, this);
  }

  /**
   * Drop Packet and clear owner reference. Note: Java will not reclaim the
   * space until after all references to this packet have been cleared -
   * ideally, one should not use this knowledge, but bright people probably
   * will!
   */
  public void drop(final Packet p) {
    network.drops.getAndIncrement();

    if (p == null) {
      FlowError.complain("Null packet reference in 'drop' method call: " + getName());
    }

    if (this != p.owner) {
      FlowError.complain("Packet not owned by current component, or component has terminated");
    }

    p.clearOwner();
  }

  /**
   * Push Packet onto stack and clear owner reference.
   */
  public void push(final Packet p) {
    if (p == null) {
      FlowError.complain("Null packet reference in 'push' method call: " + getName());
    }

    if (this != p.owner) {
      FlowError.complain("Packet not owned by current component, or component has terminated");
    }
    stack.push(p);

    p.clearOwner();
  }

  /**
   * Pop Packet off stack or return null if empty.
   */
  public Packet pop() {

    if (stack.size() == 0) {
      return null;
    }

    final Packet p = stack.pop();
    p.setOwner(this);
    return p;
  }

  /**
   * Return current size of stack.
   */
  public int stackSize() {
    return stack.size();
  }

  /**
   * This method is invoked for each activation of the Component. The
   * component is activated when data arrives at an input port, and executes
   * until it returns. If all upstream components have closed down, it can
   * close down; otherwise it is reactivated when more data arrives.
   */
  // void doActivation() throws Throwable {
  // }
  /**
   * Components override this method with their program. This method
   * is called from a private thread.
   *
   * @exception Throwable
   *                if there is any error or exception in the program; this is
   *                a kludge until we work out how to integrate exceptions
   *                properly.
   */

  protected abstract void execute() throws Exception;

  protected HashMap<String, InputPort> getInports() {
    return inputPorts;
  }

  protected HashMap<String, OutputPort> getOutports() {
    return outputPorts;
  }

  protected int getPacketCount() {
    return packetCount;
  }

  /**
   * Obtain status of component
   *
   * @return int
   */
  protected StatusValues getStatus() {
    return status;
  }

  /**
   * This method returns <code> true </code> if the component has terminated
   *
   * @return boolean
   */
  public boolean isTerminated() {
    return status == StatusValues.TERMINATED;
  }

  /**
   * This method returns <code> true </code> if the component has an error
   *
   * @return boolean
   */
  protected boolean hasError() {
    return status == StatusValues.ERROR;
  }

  /**
   * Components call this method from their <code>openPorts</code> method to open
   * an InputPort, either a regular port or a parameter port.
   *
   * @param name
   *            the name of the InputPort
   * @return the InputPort, which should be assigned to an instance variable
   *         of the component
   */

  protected final InputPort openInput(final String name) {
    if (name.startsWith("*")) {
      FlowError.complain("Attempt to open * port: " + this.getName() + "." + name);
    }
    final InputPort port = inputPorts.get(name);
    if (port == null) {
      FlowError.complain("Unknown input port: " + this.getName() + "." + name);
    }

    if (port instanceof ConnArray) {
      FlowError.complain("Port specified as array in metadata: " + this.getName() + "." + name);
    }

    return port;

  }

  /**
   * Components call this method from their <code>openPorts</code> method to open
   * an array of InputPorts. If arraySize is not specified, the size of the Array is determined by the
   * network to which this component belongs.
   *
   * @param name of the InputPort array, and optional arraySize
   * @return the InputPort[] object, which should be assigned to an instance
   *         variable.
   */

  // Array ports are stored individually in the inputPorts
  // and outputPorts HashMaps with names like NAME[X], where
  // X is the index number. In the case where index is 0, the string
  // may be either NAME or NAME[0].
  // We need to run through inputPorts twice to find out
  // the largest index number (but not less than zero)
  // so we can create a fixed-size Java array to return.
  protected final InputPort[] openInputArray(final String name, final int arraySize) {
    if (name.startsWith("*")) {
      FlowError.complain("Attempt to open * port: " + this.getName() + "." + name);
    }
    final InputPort port = inputPorts.get(name);
    if (port == null) {
      FlowError.complain("Unknown input port: " + this.getName() + "." + name);
    }

    if (!(port instanceof ConnArray)) {
      FlowError.complain("Port not specified as array in metadata: " + this.getName() + "." + name);
    }
    int maxval = -1;
    final HashMap<Integer, InputPort> arrayMap = new HashMap<Integer, InputPort>();

    ConnArray ca = null;
    for (final Map.Entry<String, InputPort> kvp : inputPorts.entrySet()) {

      //Port pt = new Port(kvp.getKey(), -1);

      if (kvp.getValue() instanceof ConnArray) {
        final ConnArray cas = (ConnArray) kvp.getValue();
        if (cas.name.equals(name)) {
          ca = cas;
        }
        continue;
      }

      final Port pt = kvp.getValue().getPort();
      if (pt == null || !pt.name.equals(name)) {
        continue;
      }

      final int i = pt.index;

      if (i > maxval) {
        maxval = i;
      }
      arrayMap.put(new Integer(i), kvp.getValue());
    }
    if (ca == null) {
      FlowError.complain("Port not defined as in input array in metadata: " + this.getName() + "." + name);
    } else {
      if (!(ca.fixedSize ^ arraySize == 0)) {
        FlowError.complain("Array port fixedSize option in metadata doesn't match specified size: " + this.getName()
            + "." + name);
      }
      inputPorts.remove(ca.getName());
    }

    if (arraySize > 0 && maxval >= arraySize) {
      FlowError.complain("Number of elements specified for array port less than actual number used: " + this.getName()
          + "." + name);
    }
    int x = arraySize;
    if (x == 0) {
      x = maxval + 1;
    }

    if (x == 0) {
      return null;
    }

    final InputPort[] array = new InputPort[x];

    for (int i = 0; i < array.length; i++) {

      final InputPort ip = arrayMap.get(new Integer(i));
      if (ip != null) {
        array[i] = ip;
      } else {
        array[i] = new NullConnection();
        array[i].setPort(new Port(name, i));
        array[i].setReceiver(this);
      }

    }
    return array;
  }

  protected final InputPort[] openInputArray(final String name) {
    return openInputArray(name, 0);
  }

  /**
   * Components call this method from their <code>openPorts</code> method to open
   * an output port.
   *
   * @param name
   *            the name of the OutputPort
   * @return the OutputPort, which should be assigned to an instance variable
   */

  protected final OutputPort openOutput(final String name) {
    if (name.startsWith("*")) {
      FlowError.complain("Attempt to open * port");
    }
    final OutputPort port = outputPorts.get(name);
    if (port == null) {
      FlowError.complain("Unknown output port: " + this.getName() + "." + name);
    }

    if (port instanceof OutArray) {
      FlowError.complain("Output port specified as array in metadata: " + this.getName() + "." + name);
    }

    if (port instanceof NullOutputPort && !port.optional) {
      FlowError.complain("Output port was specified as mandatory and was never connected: " + this.getName() + "."
          + name);
    }

    return port;
  }

  /**
   * Components call this method from their <code>openPorts</code> method to open
   * an array of OutputPorts. If arraySize is not specified, the size of the Array is determined by the
   * network to which this component belongs.
   *
   * @param name of the OutputPort array, and optional arraySize
   * @return the OutputPort[] object, which should be assigned to an instance
   *         variable.
   */

  protected final OutputPort[] openOutputArray(final String name, final int arraySize) {
    if (name.startsWith("*")) {
      FlowError.complain("Attempt to open * port: " + this.getName() + "." + name);
    }
    final OutputPort port = outputPorts.get(name);
    if (port == null) {
      FlowError.complain("Unknown output port: " + this.getName() + "." + name);
    }

    if (!(port instanceof OutArray)) {
      FlowError.complain("Port not specified as array in metadata: " + this.getName() + "." + name);
    }
    int maxval = -1;
    final HashMap<Integer, OutputPort> arrayMap = new HashMap<Integer, OutputPort>();

    OutArray oa = null;
    for (final Map.Entry<String, OutputPort> kvp : outputPorts.entrySet()) {
      //int i = -1;
      //Port pt = new Port(kvp.getKey(), -1);

      if (kvp.getValue() instanceof OutArray) {
        final OutArray oas = (OutArray) kvp.getValue();
        if (oas.name.equals(name)) {
          oa = oas;
        }
        continue;
      }

      final Port pt = kvp.getValue().port;
      if (pt == null || !pt.name.equals(name)) {
        continue;
      }

      final int i = pt.index;

      if (i > maxval) {
        maxval = i;
      }
      arrayMap.put(new Integer(i), kvp.getValue());
    }

    if (oa == null) {
      FlowError.complain("Port not defined as output array in metadata: " + this.getName() + "." + name);
    } else {
      if (!(oa.fixedSize ^ arraySize == 0)) {
        FlowError.complain("Array port fixedSize option in metadata doesn't match specified size: " + this.getName()
            + "." + name);
      }
      if (maxval == -1 && !oa.optional) {
        FlowError.complain("No elements defined in mandatory output array port: " + this.getName() + "." + name);
      }
      if (arraySize > 0 && maxval >= arraySize) {
        FlowError.complain("Number of elements specified for array port less than actual number used: "
            + this.getName() + "." + name);
      }

      outputPorts.remove(oa.name);
    }

    int x = arraySize;
    if (x == 0) {
      x = maxval + 1;
    }

    if (x == 0) {
      return null;
    }

    final OutputPort[] array = new OutputPort[x];

    for (int i = 0; i < array.length; i++) {
      array[i] = new NullOutputPort();
      array[i].setSender(this);

      final OutputPort op = arrayMap.get(new Integer(i));
      if (op != null) {
        array[i] = op;
      } else if (!oa.optional && arraySize > 0) {
        FlowError.complain("Mandatory output array port has missing elements: " + this.getName() + "." + name);
      }
    }

    return array;
  }

  protected final OutputPort[] openOutputArray(final String name) {
    return openOutputArray(name, 0);
  }

  protected void longWaitStart(final double intvl) { // interval in seconds!
    timeout = new TimeoutHandler(intvl, this);
    addtoTimeouts(timeout);
  }

  /* synchronized */void addtoTimeouts(final TimeoutHandler t) {
    synchronized (network) {
      network.timeouts.put(this, t);
    }
    status = Component.StatusValues.LONG_WAIT;
  }

  /*
    protected String getFullName() {
      String s = getName();
      Network m = mother;
      while (true) {
        s = m.getName() + "." + s;
        if (!(m instanceof SubNet)) {
          break;
        }
        m = m.mother;
      }
      return s;
    }
    */

  protected void longWaitEnd() {
    timeout.dispose(this);
  }

  /**
   * Components override this method to open their ports. This method is called
   * from the network's main thread, and should only call
   * <code>openInput</code>, <code>openInputArray</code>,
   * <code>openOutput</code>,
   * <code>openOutputArray<code>, <code>InputPort.setType</code>,
   * and <code>OutputPort.setType</code>.
   */

  protected abstract void openPorts();

  protected void checkOutputPorts() {
    for (final Map.Entry<String, OutputPort> kvp : outputPorts.entrySet()) {
      if (kvp.getValue() instanceof NullOutputPort) {
        final NullOutputPort no = (NullOutputPort) kvp.getValue();
        if (no.optional) {
          continue;
        }

        FlowError.complain("Output port specified in metadata, but never used: " + /* getName() + "."  + */
        kvp.getValue().getName());
      }
    }
  }

  /**
   * This method must be <code>public</code> because <code>Component</code>
   * inherits from <code>Thread</code>, but should never be called except
   * by the JVM.
   *
   * If this method is started in 'terminating' mode, the code is not
   * executed, but downstream ports are still closed.
   */

  void closeAllPorts() {
    for (final OutputPort op : outputPorts.values()) {
      op.close();
    }
    for (final InputPort ip : inputPorts.values()) {
      ip.close();
    }
  }

  @Override
  public final void run() {
    // try {
    try {
      if (isTerminated() || hasError()) {
        if (goLock.isHeldByCurrentThread()) {
          goLock.unlock();
          mother.traceLocks("run - unlock " + getName());
        }
        return;
      }

      // if (this instanceof MustRun) mustrun = true;

      status = StatusValues.ACTIVE;
      mother.traceFuncs(getName() + ": Started");

      autoInput = inputPorts.get("*IN");
      autoOutput = outputPorts.get("*OUT");
      //  try {
      //    goLock.lockInterruptibly();
      //   mother.traceLocks("run - lock " + getName());
      //  } catch (InterruptedException e) {
      //   return;
      //  }
      //   try {

      if (autoInput != null) {
        final Packet p = autoInput.receive();
        if (p != null) {
          drop(p);
        }
        autoInput.close();
      }

      InputStates ist = null;
      if (selfStarting) {
        autoStarting = true;
      } else {

        ist = new InputStates(inputPorts);

      }
      // ist will be non-null if !selfStarting
      while (autoStarting || !ist.allDrained || autoInput != null || ist.allDrained && mustrun) {

        autoInput = null;
        if (isTerminated()) {
          break;
        }

        packetCount = 0;

        mother.traceFuncs(getName() + ": Activated");

        /** ************************************ */
        execute();
        /** ************************************ */

        mother.traceFuncs(getName() + ": Deactivated");

        if (packetCount != 0) {
          mother.traceFuncs(this.getName() + " deactivated holding " + packetCount + " packets");
          FlowError.complain(packetCount + " packets not disposed of during component deactivation of "
              + this.getName());
        }

        for (final InputPort ip : inputPorts.values()) {
          if (ip instanceof InitializationConnection) {
            final InitializationConnection icp = (InitializationConnection) ip;
            if (!icp.closed()) {
              FlowError.complain("Component deactivated with IIP port not closed: " + getName());
            }
          }
        }
        mustrun = false;
        selfStarting = false;

        if (autoStarting) {
          break;
        }
        ist = new InputStates(inputPorts);

        if (ist.allDrained) {
          break;
        }

      } // while

      if (autoOutput != null) {
        //Packet p = create("");
        //autoOutput.send(p);
        autoOutput.close();
      }

      closeAllPorts();

      if (stack.size() != 0) {
        FlowError.complain("Component terminated with stack not empty: " + getName());
      }
      mother.indicateTerminated(this);

    } catch (final ComponentException e) {
      if (e.getValue() > 0) {
        mother.traceFuncs(getName() + " - Component exception: " + e.getValue());
        if (e.getValue() > 999) {
          System.err.println(getName() + " terminated with exception code " + e.getValue());

          if (mother != null) {
            mother.signalError(e);
          }
          closeAllPorts();
        }
      }
      throw new ThreadDeath();

    } catch (final Exception e) {
      // don't tell the mother if we are already in the ERROR or TERMINATE state
      // because then the mother told us to terminate
      if (isTerminated() || hasError()) {
        // if we are in the TERMINATED or ERROR state we terminated intentionally
        return;
      }
      // an error occurred in this component
      status = StatusValues.ERROR;
      // tell the mother

      if (mother != null) {
        mother.signalError(e);
      }
      closeAllPorts();
      throw new ThreadDeath();

    } catch (final ThreadDeath e) {
      closeAllPorts();

    } catch (final Throwable e) {
      closeAllPorts();
      throw new ThreadDeath();
    }

  }

  class InputStates {

    // true if all connected input ports are closed and empty
    boolean allDrained;

    // true if any connected input port has data
    boolean hasData;

    // Get state of all ports

    InputStates(final HashMap<String, InputPort> inports) throws InterruptedException {

      try {
        mother.traceLocks("ist - lock " + getName());
        goLock.lockInterruptibly();
        while (true) {
          allDrained = true;
          hasData = false;
          for (final InputPort inp : inports.values()) {
            if (!(inp instanceof Connection)) {
              continue;
            }
            final Connection c = (Connection) inp;
            //  synchronized (c) {
            //allDrained &= c.isDrained();
            allDrained &= c.usedSlots == 0 && c.senderCount == 0;
            //hasData |= !c.isEmpty();
            hasData |= c.usedSlots > 0;
            //  }
          } //for
          // if (hasData) {
          //  mother.traceFuncs("hasData " + getName());
          // }
          // if (allDrained) {
          //    mother.traceFuncs("allDrained " + getName());
          //  }
          if (hasData || allDrained) {
            break;
          }

          //          try {
          status = StatusValues.DORMANT;
          mother.traceFuncs(getName() + ": Dormant");
          mother.traceLocks("ist - await " + getName());
          canGo.await();
          mother.traceLocks("ist - await ended" + getName());
          //          } catch (InterruptedException e) {
          // do nothing
          //          }

          status = StatusValues.ACTIVE;
          mother.traceFuncs(getName() + ": Active");
        }
      }

      finally {
        goLock.unlock();
        mother.traceLocks("ist - unlock " + getName());
      }

    } // while
  }

  public Object getGlobal(final String s) {
    return network.globals.get(s);
  }

  /**
   * Terminates the component.
   *
   * @param newStatus the new status of the component (mostly TERMINATED or ERROR)
   */
  public void terminate(final StatusValues newStatus) {
    status = newStatus;
    interrupt();
  }
}