/*
 * Copyright (C) J.P. Morrison, Enterprises, Ltd. 2008, 2012 All Rights Reserved. 
 */
package com.jpmorrsn.fbp.engine;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Port {

  /* *
   * Copyright 2007,..., 2012, J. Paul Morrison.  At your option, you may copy, 
   * distribute, or make derivative works under the terms of the Clarified Artistic License, 
   * based on the Everything Development Company's Artistic License.  A document describing 
   * this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. 
   * THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.
   * */

  String displayName;

  String name;

  int index;

  Port(final String n, final int i) {

    name = n;
    index = i; // -1 if array port

    if (n.startsWith("*")) {
      displayName = name;
      return;
    }

    //Pattern p = Pattern.compile("^(\\w+)(\\[(\\d+)\\])?$");    
    Pattern p = Pattern.compile("^([_ \\p{N}\\p{L}]+)(\\[(\\d+)\\])?$");
    Matcher m = p.matcher(n);
    if (!m.matches()) {
      FlowError.complain("Invalid port name: " + n);
    }

    if (m.group(2) != null) {
      if (i > -1) {
        FlowError.complain("Cannot specify element number twice: " + n + ", index:" + i);
      }
      name = m.group(1);
      index = Integer.parseInt(m.group(3));
    }
    if (index == -1) {
      displayName = name;
    } else {
      displayName = String.format("%1$s[%2$d]", name, index);
    }
  }
}
