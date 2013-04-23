package com.jpmorrsn.fbp.engine;


/**
 * This class provides connections that hold just a single object at network
 * setup time. The <code>initialize</code> statement in the network
 * configuration mini-language creates instances of this class. It is a
 * degenerate form of Connection.
 * <p>
 * This class implements a type of parametrization of components - the
 * "parameter", which can be any object type, is associated with a port, and is
 * turned into a Packet when the first receive to that port is issued. This
 * occurs once per activation of that component. From the component's point of
 * view, it looks like a normal data stream containing one Packet.
 */

final class InitializationConnection implements InputPort {

  /***************************************************************************
   * Copyright 2007, 2012, J. Paul Morrison. At your option, you may copy,
   * distribute, or make derivative works under the terms of the Clarified
   * Artistic License, based on the Everything Development Company's Artistic
   * License. A document describing this License may be found at
   * http://www.jpaulmorrison.com/fbp/artistic2.htm. THERE IS NO WARRANTY; USE
   * THIS PRODUCT AT YOUR OWN RISK.
   */
  Component receiver; // The receiver to deliver to.

  // Packet packet;

  Object content; // object passed to it by initialize statement

  boolean closed = false;

  String name;

  Port port;

  Class type;

  // Network network;

  /**
   * Create an InitializationConnection: requires a content and a receiver.
   */

  InitializationConnection(final Object cont, final Component newReceiver) {

    content = cont; // store object
    receiver = newReceiver;
  }

  /**
   * The maximum number of packets available in an InitializationConnection
   * must be 1.
   */
  public int capacity() {
    return 1;
  }

  /**
   * Close Initialization Connection
   */
  public void close() {
    closed = true;
  }

  public boolean closed() {
    return closed;
  }

  /**
   * Return 1 as number of packets in InitializationConnection.
   * 
   * @return int
   */
  public int count() {
    return 1;
  }

  /**
   * Invoked to get receiver.
   */

  public Component getReceiver() {
    return receiver;
  }

  /**
   * The receive function of an InitializationConection. Returns null after
   * the packet has been delivered (because the Packet is set to null). You
   * get one copy per activation
   * 
   * Warning: the object contained in this packet must not be modified.
   * 
   * See InputPort.receive.
   */
  public Packet receive() {
    Packet p;

    if (!closed()) {
      p = new Packet(content, getReceiver());
      getReceiver().network.receives.getAndIncrement();
      getReceiver().mother.traceFuncs(getName() + ": Received: " + p.toString());
      close(); // not sure what this will do to subnets, etc.
    } else {
      p = null;
      // p.setOwner(receiver);
      // content = null;
    }

    return p;
  }

  public void setReceiver(final Component newReceiver) {
    receiver = newReceiver;
  }

  public String getName() {
    return name;
  }

  /**
   * Invoked to tell us the type of packet content being sent or expected. The
   * receiver's type must be a supertype of content, or the network is
   * ill-formed.
   
  public void setType(Class type) {

    if (type == null)
      return;

    if (type == Object.class)
      return;

    //
     * if (!(type.isAssignableFrom(content.getClass())))
     * FlowError.complain("Connection type mismatch");
     //
  }
  */

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#setType(java.lang.Class)
   */
  @SuppressWarnings("unused")
  public void setType(final Class tp) {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#setName(java.lang.String)
   */
  public void setName(final String n) {
    name = n;

  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#getPort()
   */
  public Port getPort() {
    return port;
  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#setPort(com.jpmorrsn.fbp.engine.Port)
   */
  public void setPort(final Port p) {
    port = p;
  }

}
