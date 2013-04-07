/*
 * Copyright (C) J.P. Morrison, Enterprises, Ltd. 2009, 2012 All Rights Reserved. 
 */
package com.jpmorrsn.fbp.engine;


//import java.util.ArrayList;

public class ConnArray implements InputPort {

  boolean fixedSize;

  String name;

  Class type;

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#capacity()
   */
  public int capacity() {
    // TODO Auto-generated method stub
    return 0;
  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#close()
   */
  public void close() {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#count()
   */
  public int count() {
    // TODO Auto-generated method stub
    return 0;
  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#getName()
   */
  public String getName() {
    return name;
  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#getReceiver()
   */
  public Component getReceiver() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#receive()
   */
  public Packet receive() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#setReceiver(com.jpmorrsn.fbp.engine.Component)
   */
  @SuppressWarnings("unused")
  public void setReceiver(final Component comp) {
    // TODO Auto-generated method stub

  }

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
  @SuppressWarnings("unused")
  public void setName(final String n) {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#getPort()
   */
  public Port getPort() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.jpmorrsn.fbp.engine.InputPort#setPort(com.jpmorrsn.fbp.engine.Port)
   */
  @SuppressWarnings("unused")
  public void setPort(final Port p) {
    // TODO Auto-generated method stub

  }

}
