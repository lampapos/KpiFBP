package com.jpmorrsn.fbp.components;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.Connection;
import com.jpmorrsn.fbp.engine.FlowError;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;


/**
 * Component to copy all incoming packets - balloons if output blocked.
 * Note: this has not been tested in production - more research is needed to determine if this is 
 * the right approach to the problem it is trying to address.
 * If the limit logic on the list size is correct, it should be moved into
 * an IIP...
 */
@ComponentDescription("Balloon if output blocked")
@OutPort("OUT")
@InPort("IN")
public class Balloon extends Component {

  static final String copyright = "Copyright 2009, 2012, J. Paul Morrison.  At your option, you may copy, "
      + "distribute, or make derivative works under the terms of the Clarified Artistic License, "
      + "based on the Everything Development Company's Artistic License.  A document describing "
      + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. "
      + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

  InputPort inport;

  OutputPort outport;

  Connection cnxt;

  LinkedBlockingQueue<Packet> ll = new LinkedBlockingQueue<Packet>(50);

  @Override
  protected void execute() {
    Runnable runnable = new Unloader();
    Thread th = new Thread(runnable);
    int priority = th.getPriority();
    th.setPriority(Math.min(priority + 20, Thread.MAX_PRIORITY));
    th.start();
    Packet p;
    while (null != (p = inport.receive())) {
      try {
        ll.put(p);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      ll.put(new Packet(Integer.MAX_VALUE, null, this));
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    } // end of data

    try {
      th.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void openPorts() {
    inport = openInput("IN");

    outport = openOutput("OUT");
    cnxt = outport.getConnection();
    if (cnxt.getCapacity() != 1) {
      FlowError.complain("Downstream capacity of Balloon must be 1");
    }

  }

  class Unloader implements Runnable {

    public void run() {
      Packet p = null;
      try {
        p = ll.poll(36000L, TimeUnit.SECONDS); // wait for 1 hr
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      while (Integer.MAX_VALUE != p.getType()) {
        outport.send(p);
        try {
          p = ll.poll(36000L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      drop(p);
    }
  }
}
