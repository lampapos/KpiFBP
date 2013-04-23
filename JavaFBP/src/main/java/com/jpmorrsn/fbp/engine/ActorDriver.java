/*
 * Copyright (C) J.P. Morrison, Enterprises, Ltd. 2008, 2012 All Rights Reserved. 
 */
package com.jpmorrsn.fbp.engine;


import java.util.HashMap;
import java.util.Stack;


/**
 * Prototype component to simulate zero-buffering
 * 1) A list of alternating class names and classes, followed by free form
 *    actor network definition, is presented at the ACTS input (IIP) port
 *   and an array of classes is built from this
 * 2) The actors are identified by an index into the class list
 * 3) ActorDriver builds a HashMap relating actor number to integer arrays relating a 
 *   port number to a particular actor.
 */
@ComponentDescription("Simulate zero-buffering")
@InPorts({ @InPort(value = "IN", description = "Input port", type = Object.class),
    @InPort(value = "ACTS", description = "Input port", type = Object.class) })
@OutPort(value = "OUT", arrayPort = true, optional = true, description = "Output port, if connected", type = Object.class)
@MustRun
public class ActorDriver extends Component {

  static final String copyright = "Copyright 2007, 2008, 2012, J. Paul Morrison.  At your option, you may copy, "
      + "distribute, or make derivative works under the terms of the Clarified Artistic License, "
      + "based on the Everything Development Company's Artistic License.  A document describing "
      + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. "
      + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

  InputPort inport, actsport;

  private OutputPort[] outportArray;

  double _timeout = 10.0; // 10 secs

  @Override
  protected void execute() {

    // start processing with classes[0]

    Packet opp = actsport.receive();
    if (opp == null) {
      return;
    }
    actsport.close();
    HashMap<String, Integer> hm = new HashMap<String, Integer>();
    Object[] array = (Object[]) opp.getContent();
    String freeform = (String) array[array.length - 1];
    Actor[] actors = new Actor[array.length / 2];
    Class[] classes = new Class[array.length / 2];

    int[] occs = new int[200]; // actor "occurrences" - contents is actno
    String[] params = new String[200];
    int[][] h1 = new int[200][200];
    for (int i = 0; i < occs.length; i++) {
      occs[i] = -1;
    }

    for (int i = 0; i < array.length - 1;) {
      hm.put((String) array[i], new Integer(i / 2));
      classes[i / 2] = (Class) array[i + 1];
      i += 2;
    }
    int socc = parse(freeform, occs, h1, hm, params); // return starting actor

    drop(opp);

    @SuppressWarnings("hiding")
    Stack<ToDo> stack = new Stack<ToDo>();

    for (int i = 0; i < classes.length; i++) {
      try {
        actors[i] = (Actor) classes[i].newInstance();

      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

      actors[i].type = classes[i];
      actors[i].name = type.getName();
      actors[i].mother = this;
    }

    Packet p;

    while ((p = inport.receive()) != null) {
      longWaitStart(_timeout);
      ToDo td = new ToDo(p, socc);
      stack.push(td);
      while (!stack.empty()) {
        td = stack.pop();
        int[] hx = h1[td.occno];
        Packet[] arr = null;
        if (hx != null) {
          arr = new Packet[hx.length];
        }
        actors[occs[td.occno]].run(td.pkt, arr, params[td.occno]);
        if (hx == null) {
          continue;
        }
        for (int i = 0; i < arr.length; i++) {
          if (arr[i] != null) {
            stack.push(new ToDo(arr[i], hx[i]));
          }
        }
      }
      longWaitEnd();
    }
  }

  @Override
  protected void openPorts() {
    inport = openInput("IN");
    actsport = openInput("ACTS");

    setOutportArray(openOutputArray("OUT"));

  }

  /**
   * @param oa the outportArray to set
   */
  public void setOutportArray(final OutputPort[] oa) {
    this.outportArray = oa;
  }

  /**
   * @return the outportArray
   */
  public OutputPort[] getOutportArray() {
    return outportArray;
  }

  int parse(final String s, final int[] occs, final int[][] h1, final HashMap<String, Integer> hm, final String[] params) {
    BabelParser2 bp = new BabelParser2(s);
    HashMap<String, Integer> hx = new HashMap<String, Integer>();
    int actno = -1;
    int socc = -1;
    int occno = 0;
    int occmax = 0;
    String param = null;
    String qual = null;
    int[] h2 = null;
    int portno = 0;
    while (true) {
      skip_blanks(bp);
      if (bp.finished()) {
        break;
      }

      while (true) { // find blank, left paren or hyphen
        if (bp.tc(' ', 'n')) {
          break;
        }
        if (bp.tc('(', 'n')) {
          break;
        }
        if (bp.tc('-', 'n')) {
          break;
        }
        if (bp.tc('.', 'n')) {
          break;
        }
        if (bp.tc(',', 'n')) {
          break;
        }
        if (!bp.tu()) {
          break;
        }
      }
      String sym = bp.getOutStr();
      actno = hm.get(sym);

      param = null;
      qual = null;
      //skip_blanks(bp);
      // scan off qualifier, if any
      if (bp.tc('.', 'o')) {
        while (true) {
          if (bp.tc('(', 'n')) {
            break;
          }
          if (bp.tc(' ', 'n')) {
            break;
          }
          if (bp.tc('-', 'n')) {
            break;
          }
          if (bp.tc(',', 'n')) {
            break;
          }
          bp.tu();
        }
        qual = bp.getOutStr().trim();
        skip_blanks(bp);
      }
      skip_blanks(bp);
      // scan off param, if any
      if (bp.tc('(', 'o')) {
        while (true) {
          if (bp.tc(')', 'o')) {
            break;
          }
          bp.tu();
        }
        param = bp.getOutStr().trim();
        skip_blanks(bp);
      }
      String sym2 = sym;
      if (qual != null) {
        sym2 += "." + qual;
      } else if (param != null) {
        sym2 += "/" + param;
      }
      Integer occx = hx.get(sym2);
      if (occx == null) {
        occno = occmax;
        hx.put(sym2, new Integer(occno));
        occmax++;
      } else {
        occno = occx.intValue();
      }
      occs[occno] = actno;
      params[occno] = param;

      if (socc == -1) {
        socc = occno;
      } else {
        h2[portno] = occno;
      }
      if ((h2 = h1[occno]) == null) {
        h2 = new int[100];
        h1[occno] = h2;
      }
      portno = 0;

      if (bp.tc(',', 'o')) {
        continue;
      }

      if (bp.tf()) {
        while (true) {
          if (!bp.tf()) {
            break;
          }
        }
        String port = bp.getOutStr();
        portno = Integer.parseInt(port);
        skip_blanks(bp);
      }

      bp.tc('-', 'o');
      bp.tc('>', 'o');
    }
    return socc;
  }

  void skip_blanks(final BabelParser2 bp) {
    while (true) { // skip blanks 
      if (!bp.tc(' ', 'o')) {
        break;
      }
    }
  }

  class ToDo {

    Packet pkt;

    int occno;

    ToDo(final Packet p, final int occ) {
      pkt = p;
      occno = occ;
    }
  }

  /** This class is based on a (language) parsing technique called Babel, that I learned in
  *  England many years ago!
  *  This class definitely has an internal state, so all users must create an instance of it.
  *  This class is almost the same as the original BabelParser class, except that it simply returns 
  *  false when requested to get more data...  
  **/

  public class BabelParser2 {

    static final String copyright1 = "Copyright 1999, 2000, 2001, 2012, J. Paul Morrison.  At your option, you may copy, "
        + "distribute, or make derivative works under the terms of the Clarified Artistic License, "
        + "based on the Everything Development Company's Artistic License.  A document describing "
        + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. "
        + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

    private final char input[];

    private final char output[];

    private int inputIndex;

    private int outputIndex;

    private final int endOfInput;

    /**
     * BabelParser constructor.
     */
    public BabelParser2(final String inStr) {
      super();
      inputIndex = 0;
      outputIndex = 0;
      output = new char[256];
      input = inStr.toCharArray();
      endOfInput = inStr.length();

    }

    public boolean finished() {
      return inputIndex >= endOfInput;
    }

    /**
     * Erase output stream
     */
    public void eraseOutput() {

      outputIndex = 0;

    }

    /**
     * Get another input Packet - return false if no more
     */
    boolean getMoreInput() {
      return false;
    }

    /**
     * Take output generated by syntax scan, and return as String
     * Output index is then set back to zero. 
     * @return java.lang.String
     */
    public String getOutStr() {
      String sym = null;
      if (outputIndex > 0) {
        try {
          sym = new String(output, 0, outputIndex);
        } catch (NullPointerException r) {
          System.out.println("RuntimeException:" + r);

        } catch (IndexOutOfBoundsException e) {
          System.out.println("RuntimeException:" + e);

        }
        outputIndex = 0;
      }
      return sym;
    }

    /**
     * This macro compares against a given character. Scanning is continuous from the end of one
     *  incoming packet to the start of the next one.  End of input results in a false result -
     *  i.e. end of input is considered to not match _any_ test character.
     */
    public boolean tc(final char x) {
      while (inputIndex >= endOfInput) {
        if (!getMoreInput()) {
          return false;
        }
      }
      if (input[inputIndex] != x) {
        return false;
      }
      output[outputIndex] = input[inputIndex];
      outputIndex++;
      inputIndex++;
      return true;
    }

    /**
     * Same as tc(char), but with modification (must be 'n' or 'o')
     * ('n' is equivalent to old Babel 'IO' - I- and O-modification)
     * @param x char
     * @param mod char
     */

    public boolean tc(final char x, final char mod) {

      while (inputIndex >= endOfInput) {
        if (!getMoreInput()) {
          return false;
        }
      }
      if (input[inputIndex] != x) {
        return false;
      }

      if (mod == 'o') {
        inputIndex++;
      }
      return true;
    }

    /**
     * This macro compares against a number (figure). Scanning is continuous from the end of one
     *  incoming packet to the start of the next one.  End of input results in a false result.
     */
    public boolean tf() {
      while (inputIndex >= endOfInput) {
        if (!getMoreInput()) {
          return false;
        }
      }
      if (!Character.isDigit(input[inputIndex])) {
        return false;
      }
      output[outputIndex] = input[inputIndex];
      outputIndex++;
      inputIndex++;
      return true;
    }

    /**
     * Same as tf(), but with modification (must be 'o' or 'n')
      * ('n' is equivalent to old Babel 'IO' - I- and O-modification)
     * @param mod char
     */

    public boolean tf(final char mod) {

      while (inputIndex >= endOfInput) {
        if (!getMoreInput()) {
          return false;
        }
      }
      if (!Character.isDigit(input[inputIndex])) {
        return false;
      }

      if (mod == 'o') {
        inputIndex++;
      }
      return true;
    }

    /**
     * This macro compares against a letter. Scanning is continuous from the end of one
     *  incoming packet to the start of the next one.  End of input results in a false result.
     */
    public boolean tl() {
      while (inputIndex >= endOfInput) {
        if (!getMoreInput()) {
          return false;
        }
      }
      if (!Character.isLetter(input[inputIndex])) {
        return false;
      }
      output[outputIndex] = input[inputIndex];
      outputIndex++;
      inputIndex++;
      return true;
    }

    /**
     * Same as tl(), but with modification (must be 'o' or 'n')
      * ('n' is equivalent to old Babel 'IO' - I- and O-modification)
     * @param mod char
     */

    public boolean tl(final char mod) {

      while (inputIndex >= endOfInput) {
        if (!getMoreInput()) {
          return false;
        }
      }
      if (!Character.isLetter(input[inputIndex])) {
        return false;
      }

      if (mod == 'o') {
        inputIndex++;
      }
      return true;
    }

    /** 
    * Babel 'universal comparator' - it is always true, unless we are at end of file 
    */
    public boolean tu() {
      while (inputIndex >= endOfInput) {
        if (!getMoreInput()) {
          return false;
        }
      }

      output[outputIndex] = input[inputIndex];
      outputIndex++;
      inputIndex++;
      return true;

    }

    /**
     * Same as tu(), but with modification (must be 'o' or 'n')
      * ('n' is equivalent to old Babel 'IO' - I- and O-modification)
     * @param mod char
     */
    public boolean tu(final char mod) {
      while (inputIndex >= endOfInput) {
        if (!getMoreInput()) {
          return false;
        }
      }

      if (mod == 'o') {
        inputIndex++;
      }
      return true;
    }
  }

}
