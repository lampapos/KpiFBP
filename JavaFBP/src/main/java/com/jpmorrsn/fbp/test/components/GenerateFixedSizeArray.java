package com.jpmorrsn.fbp.test.components;


import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;


/** Component to generate a stream of 'n' packets, where 'n' is
 * specified in an InitializationConnection.
 */
@ComponentDescription("Generates stream of packets under control of a counter")
@OutPort(value = "OUT", fixedSize = true, description = "Generated stream", type = String.class, arrayPort = true /*, optional = true */)
@InPort(value = "COUNT", description = "Count of packets to be generated", type = String.class)
public class GenerateFixedSizeArray extends Component {

  static final String copyright = "Copyright 2007, 2012, J. Paul Morrison.  At your option, you may copy, "
      + "distribute, or make derivative works under the terms of the Clarified Artistic License, "
      + "based on the Everything Development Company's Artistic License.  A document describing "
      + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. "
      + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

  OutputPort outPortArray[];

  InputPort count;

  @Override
  protected void openPorts() {
    outPortArray = openOutputArray("OUT", 2);
    count = openInput("COUNT");
  }

  @Override
  protected void execute() {
    final Packet ctp = count.receive();
    if (ctp == null) {
      return;
    }
    count.close();

    String cti = (String) ctp.getContent();
    cti = cti.trim();
    int ct = 0;
    try {
      ct = Integer.parseInt(cti);
    } catch (final NumberFormatException e) {
      e.printStackTrace();
    }
    drop(ctp);

    final int no = outPortArray.length;

    for (int k = 0; k < no; k++) {
      // int k2 = k;
      for (int i = ct; i > 0; i--) {
        final String s = String.format("%1$06d", i) + "abcd";

        final Packet p = create(s);
        //  if (outPortArray[k].isConnected()) {
        outPortArray[k].send(p);
        //  } else {
        //    drop(p);
        //  }
      }
    }

  }

  String repeat(final String string, final int ct) {
    String result = "";
    for (int i = 0; i < ct; i++) {
      result = result + string;
    }
    return result;
  }

}
