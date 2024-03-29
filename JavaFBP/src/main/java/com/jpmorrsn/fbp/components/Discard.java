package com.jpmorrsn.fbp.components;


import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.Packet;


/**
 * Component to discard all incoming packets - mostly used for debugging
 * purposes.
 */

@ComponentDescription("Discards all incoming packets")
@InPort(value = "IN", description = "Stream of packets to be discarded")
public class Discard extends Component {

  static final String copyright = "Copyright 2007, 2012, J. Paul Morrison.  At your option, you may copy, "
      + "distribute, or make derivative works under the terms of the Clarified Artistic License, "
      + "based on the Everything Development Company's Artistic License.  A document describing "
      + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. "
      + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

  InputPort inport;

  @Override
  protected void execute() {
    Packet p = inport.receive();
    // while ((p = inport.receive()) != null) {
    drop(p);
    // }

  }

  @Override
  protected void openPorts() {
    inport = openInput("IN");

  }
}
