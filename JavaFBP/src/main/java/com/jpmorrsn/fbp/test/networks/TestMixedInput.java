package com.jpmorrsn.fbp.test.networks;


import com.jpmorrsn.fbp.components.Concatenate;
import com.jpmorrsn.fbp.components.Generate;
import com.jpmorrsn.fbp.components.WriteToConsole;
import com.jpmorrsn.fbp.engine.Network;


public class TestMixedInput extends Network {

  static final String copyright = "Copyright 2007, 2008, ... 2012, J. Paul Morrison.  At your option, you may copy, "
      + "distribute, or make derivative works under the terms of the Clarified Artistic License, "
      + "based on the Everything Development Company's Artistic License.  A document describing "
      + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. "
      + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

  @Override
  protected void define() {
    connect(component("Generate", Generate.class), port("OUT"), component("Concat", Concatenate.class), port("IN"));
    connect("Concat.OUT", component("Write", WriteToConsole.class), port("IN"));
    initialize("testing", "Concat.IN[0]");
    initialize("100", component("Generate"), port("COUNT"));
  }

  public static void main(final String[] argv) throws Exception {
    new TestMixedInput().go();
  }
}
