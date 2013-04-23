package edu.kpi.fbp.network.components;


import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.MustRun;
import com.jpmorrsn.fbp.engine.Packet;



/**
 * Block that can print into console.
 *
 * $$Блок, що може виводити у консоль.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@ComponentDescription("Displays packets on Swing EditorPane")
@MustRun
@InPorts({ @InPort(value = "IN", description = "Packets to be displayed", type = Object.class) })
public class ToString extends Component {

  /** Input port. */
  InputPort inport;

  @Override
  protected void execute() {
    @SuppressWarnings("rawtypes")
    Packet p;
    while ((p = inport.receive()) != null) {
      System.out.println(p.getContent().toString());
      drop(p);
    }
  }

  @Override
  protected void openPorts() {
    inport = openInput("IN");
  }
}
