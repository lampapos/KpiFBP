package com.jpmorrsn.fbp.test.networks;


import com.jpmorrsn.fbp.components.Balloon;
import com.jpmorrsn.fbp.components.Generate;
import com.jpmorrsn.fbp.engine.Network;
import com.jpmorrsn.fbp.test.components.CheckBallooning;


public class TestBalloon extends Network {

  static final String copyright = "Copyright 2007, 2008, 2012, J. Paul Morrison.  At your option, you may copy, "
      + "distribute, or make derivative works under the terms of the Clarified Artistic License, "
      + "based on the Everything Development Company's Artistic License.  A document describing "
      + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. "
      + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

  @Override
  protected void define() {

    connect(component("Generate", Generate.class), port("OUT"), component("Balloon", Balloon.class), port("IN"));

    connect(component("Balloon"), port("OUT"), component("Check", CheckBallooning.class), port("IN"), 1);

    initialize("200", component("Generate"), port("COUNT"));

  }

  public static void main(final String[] argv) throws Exception {
    new TestBalloon().go();
  }
}
