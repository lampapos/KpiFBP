/*
 * Copyright (C) J.P. Morrison, Enterprises, Ltd. 2009, 2012 All Rights Reserved. 
 */
package com.jpmorrsn.fbp.test.components;


import com.jpmorrsn.fbp.engine.Actor;
import com.jpmorrsn.fbp.engine.Packet;


public class Act2 extends Actor {

  @SuppressWarnings("unused")
  @Override
  protected void execute(final Packet p, final String st) {
    String s = (String) p.getContent();
    s += "defg";
    drop(p);
    send(create(s), 0);
  }

}
