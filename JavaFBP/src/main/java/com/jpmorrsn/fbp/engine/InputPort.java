package com.jpmorrsn.fbp.engine;


/**
 * This class is used within components to declare instance variables
 * that hold input ports.  Such instance variables should be assigned
 * within the <code>openPorts</code> routine of the component and never
 * changed thereafter.  Packets can be received, and the status of the
 * port manipulated, using the API specified by this interface.
 * <p> InputPort is not a class, but an interface which is implemented
 * either by a Connection object or by an InitializationConnection object. 
 **/

public interface InputPort {

  /* *
     * Copyright 2007, 2012, J. Paul Morrison.  At your option, you may copy, 
     * distribute, or make derivative works under the terms of the Clarified Artistic License, 
     * based on the Everything Development Company's Artistic License.  A document describing 
     * this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. 
     * THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.
     * */

  /**
   * The maximum number of packets which this InputPort can hold.
   * @return maximum packet count
   **/

  public int capacity();

  /**
   * Close Connection (dummy method)
    */
  public void close();

  /** Return the number of packets currently at this Input Port.
  */
  //abstract int count();

  public String getName();

  public void setName(String n);

  public Port getPort();

  public void setPort(Port p);

  /**
   * Get receiving component
   **/

  public Component getReceiver(); // added for subnet support 

  /**
   * Receive the next available packet from this InputPort.
   * The thread is suspended if no packets are currently available.
   * At the end of input (when all upstream threads have closed their
   * connected OutputPorts), <code>null</code> is returned.
   * @return next packet, <code>null</code> if none
   **/

  public Packet receive();

  /**
   * Change the receiving component
   **/

  public void setReceiver(Component comp); // added for subnet support 

  /**
   * Specify the type of packet content that will be accepted from this
   * InputPort.  Specifying <code>null</code> is equivalent to specifying
   * <code>Object.class</code> -- in other words, any packet content is
   * acceptable.
   * @param type the class of acceptable packet content
    
  */
  public void setType(Class type);

}
