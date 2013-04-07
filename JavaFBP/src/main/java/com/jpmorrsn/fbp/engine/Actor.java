/*
 * Copyright (C) J.P. Morrison, Enterprises, Ltd. 2009, 2012 All Rights Reserved. 
 */
package com.jpmorrsn.fbp.engine;


public abstract class Actor {

  static final String copyright = "Copyright 2007, 2012, J. Paul Morrison.  At your option, you may copy, "
      + "distribute, or make derivative works under the terms of the Clarified Artistic License, "
      + "based on the Everything Development Company's Artistic License.  A document describing "
      + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. "
      + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

  String name;

  Class type;

  protected ActorDriver mother;

  Packet[] pa = null;

  protected Actor() {
    super();
  }

  protected abstract void execute(Packet p, String s);

  protected void run(final Packet p, final Packet[] p0, final String s) {
    pa = p0;
    execute(p, s);
  }

  protected void drop(final Packet p) {
    p.clearOwner();
  }

  protected Packet create(final Object o) {
    Packet p = new Packet(o, Thread.currentThread());
    return p;
  }

  protected void send(final Packet p, final int i) {
    if (pa == null) {
      FlowError.complain(mother.getName() + " specified null array for " + name);
    }
    pa[i] = p;
  }

}
