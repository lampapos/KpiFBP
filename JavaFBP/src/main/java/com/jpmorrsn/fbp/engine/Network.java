package com.jpmorrsn.fbp.engine;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The abstract class which all flow networks extend directly or indirectly. A
 * specific flow network must override the <code>define()</code> method, which
 * is written using the <i>mini-language</i> (actually just highly restricted
 * Java invoking the <code>protected</code> methods of this class). The
 * mini-language specifies what threads are to be created using which
 * components, and what connections are established between the ports of those
 * components.
 */

public abstract class Network extends Component {

  /***************************************************************************
   * Copyright 2007, ..., 2012, J. Paul Morrison. At your option, you may copy,
   * distribute, or make derivative works under the terms of the Clarified
   * Artistic License, based on the Everything Development Company's Artistic
   * License. A document describing this License may be found at
   * http://www.jpaulmorrison.com/fbp/artistic2.htm. THERE IS NO WARRANTY; USE
   * THIS PRODUCT AT YOUR OWN RISK.
   */

  protected static int DEBUGSIZE = 1;

  protected static int PRODUCTIONSIZE = 10;

  //static int defaultCapacity = DEBUGSIZE; // change this when you go to production
  static int defaultCapacity = PRODUCTIONSIZE; // use this one for production

  public boolean tracing = false;

  public static boolean traceLocks = false;

  final String traceLockFile = "fulltrace.txt";

  static final boolean forceConsole = false; // for debugging only

  BufferedWriter traceWriter = null; // trace buffered writer

  //public boolean deadlockTest = false;

  public boolean deadlockTest = true;

  public boolean active = false; // used for deadlock detection

  private Map<String, Component> components = Collections.synchronizedMap(new HashMap<String, Component>());

  CountDownLatch cdl = null;

  HashMap<Component, TimeoutHandler> timeouts = new HashMap<Component, TimeoutHandler>();

  HashMap<String, Object> globals = new HashMap<String, Object>();

  volatile boolean deadlock = false;

  String name;

  private Exception error;

  private boolean abort = false;

  Vector<String> msgs = null; // for use by listCompStatus()

  private boolean useConsole = false;

  private final String tracePath = "";

  static LinkedList<BufferedWriter> traceFileList = new LinkedList<BufferedWriter>();

  // public static Network network;

  private final Map<String, BigInteger> IPCounts = Collections.synchronizedMap(new HashMap<String, BigInteger>());

  File propertiesFile = null;

  HashMap<String, String> properties = new HashMap<String, String>();

  AtomicInteger sends, receives, creates, drops;

  /**
   * Drive define method of network
   * @throws Exception
   */

  protected void callDefine() throws Exception {
    // don't turn every exception into a FlowError
    //    try {
    define();
    //    } catch (Throwable t) {
    //      FlowError.complain(t.getMessage());
    //    }
  }

  /**
   * Returns a Component class object, given the component name in the network
   */

  protected final Component component(final String nme) {
    final Component comp = getComponents().get(nme);
    if (comp == null) {
      FlowError.complain("Reference to unknown component " + nme);
    }
    return comp;
  }

  /**
   * Stores the Component class object with its network name in the Hashtable
   * called 'components'
   */

  protected final Component component(final String nme, final Class tpe) {

    if (getComponents().get(nme) != null) {
      FlowError.complain("Attempt to redefine component " + nme);
    }

    final Pattern p = Pattern.compile("^([_ \\p{N}\\p{L}]+)(\\[(\\d+)\\])?$");
    final Matcher ma = p.matcher(nme);
    if (!ma.matches()) {
      FlowError.complain("Invalid process name (only underscores, blanks, letters and numbers allowed): " + nme);
    }
    Component comp = null;
    try {
      comp = (Component) tpe.newInstance();
    } catch (final IllegalAccessException e) {
      FlowError.complain("Illegal access to component: " + nme);
      return null; // unreachable
    } catch (final InstantiationException e) {
      FlowError.complain("Cannot instantiate component: " + nme);
      return null; // unreachable
    }

    comp.setName(nme);
    comp.type = tpe;
    getComponents().put(nme, comp);
    comp.mother = this;
    Network m = this;
    while (true) {
      if (!(m instanceof SubNet)) {
        comp.network = m;
        break;
      }
      m = m.mother;
    }

    comp.status = StatusValues.NOT_STARTED;

    comp.buildAnnotations();

    return comp;
  }

  /**
   * Connect an output port of one component to an input port of another,
   * specifying a connection capacity.
   * The following connect()'s are all various combinations of String notation and Component/Port notation
   *   plus optional capacity, and optional IP counting
   */

  protected final void connect(final Component sender, final Port outP, final Component receiver, final Port inP,
      final int size, final boolean IPCount) {

    int cap = size;
    if (size == 0) {
      cap = defaultCapacity;
    }

    if (outP.displayName.equals("*")) {
      outP.displayName = "*OUT";
    }
    if (inP.displayName.equals("*")) {
      inP.displayName = "*IN";
    }

    /* start processing output port */

    OutputPort op = null;
    if (!outP.displayName.substring(0, 1).equals("*")) {
      op = sender.outputPorts.get(outP.name); // try to find output port with port name - no index
      if (op == null) {
        FlowError.complain("Output port not defined in metadata: " + sender.getName() + "." + outP.displayName);
      }

      // at this point, op may contain:
      //  - an OutArray, if the metadata specified an array port
      //  - a NullOutputPort, if the metadata did not specify array port
      //  - an OutputPort, if a previous connect specified a source port with the same name, and no index
      //  - null

      if (op instanceof OutArray && outP.index == -1) {
        outP.index = 0;
        outP.displayName += "[0]";
      }

      if (outP.index > -1 && !(op instanceof OutArray)) {
        FlowError
            .complain("Output port not defined as array in metadata: " + sender.getName() + "." + outP.displayName);
      }
    }

    Class tp = null;
    if (op != null) {
      tp = op.type;
    }

    op = sender.outputPorts.get(outP.displayName); // try to find output port with port name - with index

    // at this point, op may contain:
    //  - an OutputPort, if a previous connect specified a source port with the same name and index
    //  - a NullOutputPort, if the port is an array port, with setDimension specified  OR
    //                      if the metadata did not specify array port
    //  - null

    //if (!(op instanceof NullOutputPort) && !(op instanceof OutArray) && op.cnxt != null) {
    if (op != null && !(op instanceof NullOutputPort)) {
      FlowError.complain("Multiple connections from same output port:" + sender.getName() + ' ' + outP.displayName);
      //}
    }

    op = new OutputPort();
    op.type = tp; //  ???? experimental code copying type info from NullOutputPort or OutArray generated by Component.procOpty

    op.port = outP;
    op.setSender(sender);
    op.name = outP.displayName;
    //op.optional = x;
    op.fullName = sender.getName() + "." + op.name;
    sender.outputPorts.put(op.name, op);

    /* start processing input port */

    InputPort ip = null;
    if (!inP.displayName.substring(0, 1).equals("*")) {
      ip = receiver.inputPorts.get(inP.name);

      // at this point, ip may contain:
      //  - a ConnArray
      //  - a Connection, if a previous connect specified a destination port with the same name, and no index
      //  - an InitializationConnection, if a previous initialize specified a destination port with the same name, and no index
      //  - a NullConnection
      //  - null

      if (ip == null) {
        FlowError.complain("Input port not defined in metadata: " + receiver.getName() + "." + inP.displayName);
      }

      if (ip instanceof ConnArray) {
        tp = ((ConnArray) ip).type;
      } else if (ip instanceof Connection) {
        tp = ((Connection) ip).type;
      } else if (ip instanceof InitializationConnection) {
        tp = ((InitializationConnection) ip).type;
      } else if (ip instanceof NullConnection) {
        tp = ((NullConnection) ip).type;
      }

      if (ip instanceof ConnArray && inP.index == -1) {
        inP.index = 0;
        inP.displayName += "[0]";
      }

      if (inP.index > -1 && !(ip instanceof ConnArray)) {
        FlowError
            .complain("Input port not defined as array in metadata: " + receiver.getName() + "." + inP.displayName);
      }
    }

    ip = receiver.inputPorts.get(inP.displayName); // try to find output port with port name - with index

    // at this point, ip may contain:
    //  - a Connection, if a previous connect specified a destination port with the same name and index
    //  - an InitializationConnection, if a previous initialize specified a destination port with the same name and index
    //  - a NullConnection, if the port is an array port, with setDimension specified  OR
    //                      if the metadata did not specify array port
    //  - null

    Connection c;
    if (ip instanceof Connection) {
      if (size != 0 && size != cap) {
        FlowError.complain("Connection capacity does not agree with previous specification\n " + receiver.getName()
            + "." + inP.displayName);
      }
      c = (Connection) ip;
    } else {
      if (ip instanceof InitializationConnection) {
        FlowError.complain("Mixed connection to input port: " + receiver.getName() + "." + inP.displayName);
      }
      c = new Connection(cap);
      c.type = tp;
      c.setPort(inP);
      c.setReceiver(receiver);
      //c.name = in.displayName;
      c.IPCount = IPCount;
      c.setName(receiver.getName() + "." + inP.displayName);
      receiver.inputPorts.put(inP.displayName, c);
    }

    c.bumpSenderCount();
    op.cnxt = c;
    c.receiver = receiver;

  }

  protected final void connect(final Component sender, final Port outP, final String receiver, final int size,
      final boolean IPCount) {
    String[] parts;
    parts = cPSplit(receiver);
    connect(sender, outP, component(parts[0]), port(parts[1]), size, IPCount);
  }

  protected final void connect(final String sender, final Component receiver, final Port inP, final int size,
      final boolean IPCount) {
    String[] parts;
    parts = cPSplit(sender);
    connect(component(parts[0]), port(parts[1]), receiver, inP, size, IPCount);
  }

  protected final void connect(final String sender, final String receiver, final int size, final boolean IPCount) {
    String[] sParts;
    sParts = cPSplit(sender);
    String[] rParts;
    rParts = cPSplit(receiver);
    connect(component(sParts[0]), port(sParts[1]), component(rParts[0]), port(rParts[1]), size, IPCount);
  }

  /**
   * Same but with capacity and count parameter reversed
   */

  protected final void connect(final Component sender, final Port outP, final Component receiver, final Port inP,
      final boolean IPCount, final int size) {
    connect(sender, outP, receiver, inP, size, IPCount);
  }

  protected final void connect(final Component sender, final Port outP, final String receiver, final boolean IPCount,
      final int size) {
    connect(sender, outP, receiver, size, IPCount);
  }

  protected final void connect(final String sender, final Component receiver, final Port inP, final boolean IPCount,
      final int size) {
    connect(sender, receiver, inP, size, IPCount);
  }

  protected final void connect(final String sender, final String receiver, final boolean IPCount, final int size) {
    connect(sender, receiver, size, IPCount);
  }

  /**
   * Connect an output port of one component to an input port of another,
   * using default capacity, but specifying IPCount
   */
  protected final void connect(final Component sender, final Port outP, final Component receiver, final Port inP,
      final boolean IPCount) {
    connect(sender, outP, receiver, inP, 0, IPCount);
  }

  protected final void connect(final Component sender, final Port outP, final String receiver, final boolean IPCount) {
    connect(sender, outP, receiver, 0, IPCount);
  }

  protected final void connect(final String sender, final Component receiver, final Port inP, final boolean IPCount) {
    connect(sender, receiver, inP, 0, IPCount);
  }

  protected final void connect(final String sender, final String receiver, final boolean IPCount) {
    connect(sender, receiver, 0, IPCount);
  }

  /**
  * Connect an output port of one component to an input port of another,
  * using default IPCount, but specifying capacity
  */

  protected final void connect(final Component sender, final Port outP, final Component receiver, final Port inP,
      final int size) {
    connect(sender, outP, receiver, inP, size, false);
  }

  protected final void connect(final Component sender, final Port outP, final String receiver, final int size) {
    connect(sender, outP, receiver, size, false);
  }

  protected final void connect(final String sender, final Component receiver, final Port inP, final int size) {
    connect(sender, receiver, inP, size, false);
  }

  protected final void connect(final String sender, final String receiver, final int size) {
    connect(sender, receiver, size, false);
  }

  /**
   * Connect an output port of one component to an input port of another,
   * using default IPCount and capacity
   */

  protected final void connect(final Component sender, final Port outP, final Component receiver, final Port inP) {
    connect(sender, outP, receiver, inP, 0, false);
  }

  protected final void connect(final Component sender, final Port outP, final String receiver) {
    connect(sender, outP, receiver, 0, false);
  }

  protected final void connect(final String sender, final Component receiver, final Port inP) {
    connect(sender, receiver, inP, 0, false);
  }

  protected final void connect(final String sender, final String receiver) {
    connect(sender, receiver, 0, false);
  }

  // splits a string into component part and port part

  String[] cPSplit(final String s) {
    final int i = s.indexOf(".");
    if (i < 0) {
      FlowError.complain("Invalid receiver string: " + s);
    }
    final String[] p = { s.substring(0, i), s.substring(i + 1) };
    return p;
  }

  protected abstract void define() throws Exception;

  public Iterator<Component> enumerateComponents() {
    /*
     * Make a copy of the concurrent component list to prevent
     * ConcurrentModificationExceptions during building the network. This can
     * occur when enumerateComponents() is called from another thread during
     * this phase.
     *
     * components is a Map returned by Collections.synchronizedMap() - see
     * declaration above. All operations are implicitly synchronized except
     * iterating over the items of the map. Therefore we have to synchronize
     * the following access.
     */
    final ArrayList<Component> currentComponents = new ArrayList<Component>();
    synchronized (getComponents()) {
      for (final Component component : getComponents().values()) {
        currentComponents.add(component);
      }
    }
    return currentComponents.iterator();
  }

  /**
   * Execute method used by network being used as if it were a component
   */

  @Override
  public void execute() throws Exception {
    // overridden by specific networks
  }

  /**
  * Execute network as a whole
   * @throws Exception
  */
  public final void go() throws Exception {

    receives = new AtomicInteger(0);
    sends = new AtomicInteger(0);
    creates = new AtomicInteger(0);
    drops = new AtomicInteger(0);

    final long now = System.currentTimeMillis();

    network = this;
    //  setTracePath("/");  //used for testing

    name = this.getClass().getName();
    int i = name.lastIndexOf(".");
    if (i > -1) {
      name = name.substring(i + 1);
    }
    setName(name); // set Thread name

    readPropertiesFile();

    final String p = properties.get("tracing");
    if (p != null && p.equals("true")) {
      tracing = true;
    }

    try {
      callDefine();
      for (final Component comp : getComponents().values()) {
        comp.checkOutputPorts();
      }
      active = true;
      initiate();

      waitForAll();

    } catch (final FlowError e) {
      final String s = "Flow Error :" + e;
      System.out.println("Network: " + s);
      System.out.flush();
      // rethrow the exception for external error handling
      // in case of a deadlock: deadlock is the cause
      throw e;
    }

    if (error != null) {
      // throw the exception which caused the network to stop
      throw error;
    }

    // if (debugging) {
    final long duration = System.currentTimeMillis() - now;
    final long s = duration / 1000;
    final long ms = duration % 1000;
    final String mss = "000";
    final String ms2 = mss.concat(String.valueOf(ms));
    i = ms2.length();
    final String ms3 = ms2.substring(i - 3, i);
    traceFuncs("Run complete.  Time: " + s + '.' + ms3 + " seconds");
    closeTraceFiles();
    System.out.println("Run complete.  Time: " + s + '.' + ms3 + " seconds");
    System.out.println("Counts: C: " + creates + ", D: " + drops + ", S: " + sends + ", R (non-null): " + receives);
    System.out.flush();

    // ps.close();

    // }

  }

  void indicateTerminated(final Component comp) {
    synchronized (comp) {
      comp.status = StatusValues.TERMINATED;
    }
    traceFuncs(comp.getName() + ": Terminated");

    cdl.countDown();
    // net.interrupt();
  }

  /**
   * Build InitializationConnection object
   */

  protected final void initialize(final Object content, final Component receiver, final Port inP) {
    // if (inName.equals("*")) inName = "*IN";
    //String inName = inPort.displayName;

    InputPort ip = null;
    if (!inP.displayName.substring(0, 1).equals("*")) {
      ip = receiver.inputPorts.get(inP.name); // try to get entry with no index
      // at this point, ip may contain:
      //  - a ConnArray
      //  - a Connection, if a previous connect specified a destination port with the same name, and no index
      //  - an InitializationConnection, if a previous initialize specified a destination port with the same name, and no index
      //  - null

      if (ip == null) {
        FlowError.complain("Input port not defined in metadata: " + receiver.getName() + "." + inP.displayName);
      }

      if (ip instanceof ConnArray && inP.index == -1) {
        inP.index = 0;
        inP.displayName += "[0]";
      }

      if (inP.index > -1 && !(ip instanceof ConnArray)) {
        FlowError
            .complain("Input port not defined as array in metadata: " + receiver.getName() + "." + inP.displayName);
      }
    }
    ip = receiver.inputPorts.get(inP.displayName); // try to get entry for indexed name

    // at this point, ip may contain:
    //  - a Connection, if a previous connect specified a destination port with the same name and index
    //  - an InitializationConnection, if a previous initialize specified a destination port with the same name and index
    //  - a NullConnection, if the port is an array port, with setDimension specified  OR
    //                      if the metadata did not specify array port
    //  - null

    if (ip != null) {
      if (ip instanceof Connection || ip instanceof ConnArray) {
        FlowError.complain("IIP port cannot be shared: " + receiver.getName() + "." + inP.displayName);
      }
      if (ip instanceof InitializationConnection) {
        FlowError.complain("IIP port already used: " + receiver.getName() + "." + inP.displayName);
      }
    }

    final InitializationConnection ic = new InitializationConnection(content, receiver);
    ic.setName(receiver.getName() + "." + inP.displayName);
    //ic.network = this;

    ic.setPort(inP);

    receiver.inputPorts.put(inP.displayName, ic);
  }

  protected final void initialize(final Object content, final String receiver) {
    final String parts[] = cPSplit(receiver);
    initialize(content, component(parts[0]), port(parts[1]));
  }

  /**
   * Go through components opening ports, and activating those which are
   * self-starting (have no input connections)
   */
  void initiate() {

    cdl = new CountDownLatch(getComponents().size());

    for (final Component comp : getComponents().values()) {
      comp.openPorts();
    }

    final ArrayList<Component> selfStarters = new ArrayList<Component>();
    for (final Component comp : getComponents().values()) {
      comp.autoStarting = true;

      if (!comp.selfStarting) {
        for (final InputPort port : comp.inputPorts.values()) {
          if (port instanceof Connection) {
            comp.autoStarting = false;
            break;
          }
        }
      }

      if (comp.autoStarting) {
        selfStarters.add(comp);
      }
    }

    for (final Component comp : selfStarters) {
      comp.activate();
    }
  }

  /**
   * Interrupt all components
   */

  public void interruptAll() {

    System.out.println("*** Crashing whole application!");
    System.out.flush();

    System.exit(0); // trying this - see if more friendly!

  }

  /**
   * method to open ports for subnet
   */

  @Override
  protected void openPorts() { // dropped final - should never get executed

  }

  /**
   * method to register a port name for subnet
   */

  protected final Port port(final String nme) {

    return new Port(nme, -1);

  }

  /**
   * method to register an array port with index
   */

  protected final Port port(final String nme, final int index) {
    final int i = nme.indexOf("*");
    if (i > 0) { //  if asterisk in name, must be in first position
      FlowError.complain("Stray * in port name " + nme);
    }
    return new Port(nme, index);
  }

  /**
   * Test if network as a whole has terminated
   */

  void waitForAll() {

    boolean possibleDeadlock = false;

    final long freq = 500L; // check every .5 second
    //long freq = 600000L; // check every 10 mins.

    while (true) {
      boolean res = true;
      // GenTraceLine("Starting await");
      try {
        if (deadlockTest) {
          res = cdl.await(freq, TimeUnit.MILLISECONDS);
        } else {
          cdl.await();
          res = true;
        }
      } catch (final InterruptedException e) {
        FlowError.complain("Network " + getName() + " interrupted");
        break; // unreachable
      }
      if (res) {
        break;
      }

      // if an error occurred, skip deadlock testing
      if (error != null) {
        break;
      }

      // if the network was aborted, skip deadlock testing
      if (abort) {
        break;
      }

      // time elapsed
      if (!deadlockTest) {
        continue;
      }

      // enabled
      testTimeouts(freq);
      if (active) {
        active = false; // reset flag every 1/2 sec
      } else if (!possibleDeadlock) {
        possibleDeadlock = true;
      } else {
        deadlock = true; // well, maybe
        // so test state of components
        msgs = new Vector<String>();
        msgs.add("Network has deadlocked"); // add in case msgs are printed
        if (listCompStatus(msgs)) { // if true, it is a deadlock
          //          interruptAll();
          for (final String m : msgs) {
            System.out.println(m);
          }
          // FlowError.Complain("Deadlock detected");
          System.out.println("*** Deadlock detected in Network ");
          System.out.flush();
          // terminate the net instead of crashing the application
          terminate();
          // tell the caller a deadlock occurred
          FlowError.complain("Deadlock detected in Network");
          break;
        }
        // one or more components haven't started or
        // are in a long wait
        deadlock = false;
        possibleDeadlock = false;

      }
    } // while

    for (final Component c : getComponents().values()) {
      try {
        c.join();
      } catch (final InterruptedException e) {
        FlowError.complain("Component " + c.getName() + " interrupted");
        break; // unreachable
      }
    }
  }

  /**
   * Queries the status of the subnet's components.
   *
   * returns true if it is a deadlock, else false
   *
   * @param msgs the message vector for status lines
   */

  synchronized boolean listCompStatus(final Vector<String> mss) {

    // Messages are added to list, rather than written directly,
    // in case it is not a deadlock

    for (final Component comp : getComponents().values()) {
      if (comp instanceof SubNet) {
        // consider components of subnets
        final SubNet subnet = (SubNet) comp;
        if (!subnet.listCompStatus(mss)) {
          return false;
        }
      } else {
        if (comp.getStatus() == StatusValues.ACTIVE || comp.getStatus() == StatusValues.LONG_WAIT) {
          return false;
        }
        String st = comp.getStatus().toString();
        st = (st + "            ").substring(0, 13);
        String cn = comp.getName();
        if (st.trim().equals("SUSP_RECV")) {
          cn = comp.curInPort.getName();
        }
        if (st.trim().equals("SUSP_SEND")) {
          cn = comp.curOutPort.getName();
        }

        mss.add(String.format("--- %2$s     %1$s", cn, st));
      }
    }

    return true;

  }

  // called by WaitForAll method
  synchronized void testTimeouts(final long freq) {

    for (final TimeoutHandler t : timeouts.values()) {
      t.decrement(freq); // if negative, complain
    }

  }

  /**
   * Handles errors in the network.
   * @param e the exception which specifies the error
   */
  public void signalError(final Exception e) {
    // only react to the first error, the others presumably are inherited errors
    if (error == null) {
      // set the error field to let go() throw the exception
      error = e;
      // terminate the network's components
      for (final Component comp : getComponents().values()) {
        comp.terminate(StatusValues.ERROR);
      }
    }
  }

  public void putGlobal(final String s, final Object o) {
    globals.put(s, o);
  }

  /**
   * Shuts down the network.
   */
  public void terminate() {
    terminate(StatusValues.TERMINATED);
  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.Component#terminate(com.jpmorrsn.fbp.engine.Component.StatusValues)
   */
  @Override
  public void terminate(final StatusValues newStatus) {
    // prevent deadlock testing, components will be shut down anyway
    abort = true;
    for (final Component comp : getComponents().values()) {
      comp.terminate(newStatus);
    }
  }

  /**
   * Sets a new path for trace files. By default the current directory will be
   * used.
   * @param path the trace path to set
   * Not used!
  public void setTracePath(final String path) {
    if (path.endsWith(File.separator) || path.equals("")) {
      tracePath = path;
    } else {
      // append the file name separator if it is missing and the path is not
      // empty
      tracePath = path + File.separator;
    }
  }
  */
  /**
   * Generate a function trace line on trace file for network or subnet
   */
  public void traceFuncs(final String s) {
    if (tracing) {
      trace(s);
    }
  }

  /**
   * Generate a function trace line on trace file for network or subnet
   */
  public void traceLocks(final String s) {
    if (traceLocks) {
      trace(s);
    }
  }

  /**
   * Generate either kind of trace line on trace file for network or subnet
   */

  public synchronized void trace(final String s) {

    final Date date = new Date(); // create date for "now"
    final String str = "yyyy-MM-dd'T'HH:mm:ss:SSS";
    final SimpleDateFormat fmt = new SimpleDateFormat(str);
    final TimeZone timeZone = TimeZone.getTimeZone("UTC");
    fmt.setTimeZone(timeZone);
    final String dt = fmt.format(date);

    final String n = getTracingName();

    // forceConsole is used for debugging purposes to force writing to the console
    // useConsole will be set to true if the trace file could not be opened
    if (forceConsole || useConsole) {
      synchronized (network) {
        System.err.println(dt + " " + n + ": " + s);
        System.err.flush();
      }
      return;
    }
    if (traceWriter == null) {
      final String fileName = tracePath + n + '-' + traceLockFile;
      try {
        traceWriter = new BufferedWriter(new FileWriter(fileName));
      } catch (final IOException e) {
        // file cannot be created or opened - disable tracing
        synchronized (network) {
          System.err.println("Trace file " + fileName + " could not be opened - writing to console...");
          //tracing = false;
          System.err.println(dt + " " + n + ": " + s);
          System.err.flush();
        }
        // don't try to create or open the file on every invocation
        useConsole = true;
        return;
      }
      traceFileList.add(traceWriter);
      try {
        final SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String d = dateFormatGmt.format(new Date());
        traceWriter.write("Run date and time: " + d + " GMT\nJavaFBP Version: " + VersionAndTimestamp.getVersion()
            + "; Date: " + VersionAndTimestamp.getDate() + "\n");
      } catch (final IOException e) {
        //do nothing
      }
    }
    try {
      traceWriter.write(dt + " " + s + "\n");
      traceWriter.flush();
    } catch (final IOException e) {
      //do nothing
    }

  }

  /**
   * Retrieves the hierarchical network name (for tracing). Such a name looks like "rootNetwork.subnet1.subnet2...thisSubnet".
   * @return the network name
   */
  protected String getTracingName() {
    String s = "";
    Network m = mother;
    if (m == null) {
      return getName();
    }
    s = getName();
    while (true) {
      if (m == null) {
        break;
      }
      s = m.getName() + "." + s;
      m = m.mother;
    }
    return s;
  }

  /**
   * Closes all trace files.
   */
  private void closeTraceFiles() {
    for (final BufferedWriter x : traceFileList) {
      try {
        x.close();
      } catch (final IOException e) {
        //do nothing
      }
    }
  }

  /**
   * @param iPCounts the iPCounts to set
   **
  public void setIPCounts(final Map<String, BigInteger> iPCounts) {
    IPCounts = iPCounts;
  }
  */

  boolean readPropertiesFile() {

    if (propertiesFile == null) {
      final String uh = System.getProperty("user.home");
      propertiesFile = new File(uh + File.separator + "JavaFBPProperties.xml");
      if (!propertiesFile.exists()) {
        return false;
      }
    }
    BufferedReader in = null;
    String s = null;
    try {
      in = new BufferedReader(new FileReader(propertiesFile));
    } catch (final FileNotFoundException e) {
      return false;
    }
    // if (in == null)
    // return false;

    // boolean assoc = false;
    // String cd = null;
    while (true) {
      try {
        s = in.readLine();
      } catch (final IOException e) {
        // do nothing
      }
      if (s == null) {
        break;
      }
      s = s.trim();
      if (s.equals("<properties>") || s.equals("</properties>")) {
        continue;
      }
      if (s.startsWith("<?xml")) {
        continue;
      }

      if (s.startsWith("<!--")) {
        continue;
      }

      final int i = s.indexOf("<");
      final int j = s.indexOf(">");
      if (i > -1 && j > -1 && j > i + 1) {
        final String key = s.substring(i + 1, j);
        s = s.substring(j + 1);
        final int k = s.indexOf("<");
        if (k > 0) {
          s = s.substring(0, k).trim();
          properties.put(key, s);
        }
      }
    }

    return true;

  }

  /*

  void writePropertiesFile() {
    BufferedWriter out = null;

    try {
      out = new BufferedWriter(new FileWriter(propertiesFile));
      out.write("<?xml version=\"1.0\"?> \n");
      out.write("<properties> \n");
      for (String k : properties.keySet()) {
        String s = "<" + k + "> " + properties.get(k) + "</" + k + "> \n";
        out.write(s);
      }

      out.write("</properties> \n");
      // Close the BufferedWriter
      out.flush();
      out.close();

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  */

  /**
   * @return the iPCounts
   */
  public Map<String, BigInteger> getIPCounts() {
    return IPCounts;
  }

  /**
   * @param components the components to set
   */
  public void setComponents(final Map<String, Component> comps) {
    this.components = comps;
  }

  /**
   * @return the components
   */
  public Map<String, Component> getComponents() {
    return components;
  }

}