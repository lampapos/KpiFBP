package com.jpmorrsn.fbp.components;


import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.Packet;


/** Component to do simple stack testing.
*/

public class TestStack extends Component {

  static final String copyright = "Copyright 2007, 2012, J. Paul Morrison.  At your option, you may copy, "
      + "distribute, or make derivative works under the terms of the Clarified Artistic License, "
      + "based on the Everything Development Company's Artistic License.  A document describing "
      + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. "
      + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

  @Override
  protected void execute() {
    Packet p = create("");
    for (int i = 0; i < 50; i++) {
      push(p);
      p = pop();
    }
    drop(p);
  }

  @Override
  protected void openPorts() {
    // do nothing
  }
}
