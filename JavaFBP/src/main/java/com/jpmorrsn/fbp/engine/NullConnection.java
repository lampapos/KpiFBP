package com.jpmorrsn.fbp.engine;


/** This is used in a network definition when a port is not connected
*/

final class NullConnection implements InputPort {

  /* *
     * Copyright 2007, 2012, J. Paul Morrison.  At your option, you may copy, 
     * distribute, or make derivative works under the terms of the Clarified Artistic License, 
     * based on the Everything Development Company's Artistic License.  A document describing 
     * this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. 
     * THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.
     * */
  // static NullConnection singleton = new NullConnection();
  public NullConnection() {
    // do nothing
  }

  /** Return capacity of 0
  */

  String name;

  Port port;

  Component receiver;

  Class type;

  public int capacity() {
    return 0;
  }

  public void close() {
    // do nothing
  }

  public int count() {
    return 0;
  }

  public String getName() {
    return name;
  }

  public Component getReceiver() { //added for subnet support 
    return receiver;
  }

  public boolean isClosed() {
    return true;
  }

  public Packet receive() {
    return null;
  }

  public void setReceiver(final Component newReceiver) { // added for subnet support 
    receiver = newReceiver;
  }

  @SuppressWarnings("unused")
  public void setType(final Class tp) {
    //
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
