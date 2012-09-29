package org.kpi.fict.fbp;


import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.MustRun;
import com.jpmorrsn.fbp.engine.Packet;



@ComponentDescription("Displays packets on Swing EditorPane")
@MustRun
@InPorts({ @InPort(value = "IN", description = "Packets to be displayed", type = String.class) })
public class PrintResult extends Component {

  InputPort inport;

  @Override
  protected void execute() {
    @SuppressWarnings("rawtypes")
    Packet p;
    while ((p = inport.receive()) != null) {
      System.out.println(1111111 + "" + p.getContent());
      drop(p);
    }
  }

  @Override
  protected void openPorts() {
    inport = openInput("IN");
  }
}
