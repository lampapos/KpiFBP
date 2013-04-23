/*
 * Copyright (C) J.P. Morrison, Enterprises, Ltd. 2009, 2012 All Rights Reserved. 
 */
package com.jpmorrsn.fbp.components;


import com.jpmorrsn.fbp.engine.ActorDriver;
import com.jpmorrsn.fbp.engine.Actor;
import com.jpmorrsn.fbp.engine.Packet;


public class OutActor extends Actor {

  @Override
  protected void execute(final Packet p, final String parm) {
    ActorDriver co = mother;
    int i = Integer.parseInt(parm);
    co.getOutportArray()[i].send(p); // send to arrayport element i
  }

}
