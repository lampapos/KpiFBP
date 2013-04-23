package com.jpmorrsn.fbp.test.networks;


import java.io.File;

import com.jpmorrsn.fbp.components.Generate;
import com.jpmorrsn.fbp.components.ReadFile;
import com.jpmorrsn.fbp.components.WriteFile;
import com.jpmorrsn.fbp.components.WriteToConsole;
import com.jpmorrsn.fbp.engine.Network;


public class InfQueueAsMain extends Network {

  // This network contains the "infinite queue" as part of the application

  static final String copyright = "Copyright 2007, 2008, 2012, J. Paul Morrison.  At your option, you may copy, "
      + "distribute, or make derivative works under the terms of the Clarified Artistic License, "
      + "based on the Everything Development Company's Artistic License.  A document describing "
      + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. "
      + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

  @Override
  protected void define() {
    //tracing = true;
    component("Generate", Generate.class);
    connect(component("Generate"), port("OUT"), component("Write", WriteFile.class), port("IN"));
    initialize("40", component("Generate"), port("COUNT"));
    connect(component("Write"), port("*"), component("Read", ReadFile.class), port("*"));

    initialize("src\\com\\jpmorrsn\\fbp\\test\\data\\temp".replace("\\", File.separator), component("Write"),
        port("DESTINATION"));
    initialize("src\\com\\jpmorrsn\\fbp\\test\\data\\temp".replace("\\", File.separator), component("Read"),
        port("SOURCE"));
    //component("Display", WriteFile.class);   
    component("Display", WriteToConsole.class);
    connect(component("Read"), port("OUT"), component("Display"), port("IN"));
    //initialize("com\\jpmorrsn\\fbp\\test\\data\\output",    
    //    component("Display"),
    //    port("DESTINATION"));
  }

  public static void main(final String[] argv) throws Exception {
    for (int i = 0; i < 50; i++) {
      new InfQueueAsMain().go();
    }
  }
}
