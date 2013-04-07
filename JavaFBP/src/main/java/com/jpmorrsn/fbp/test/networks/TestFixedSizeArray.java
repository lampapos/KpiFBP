/*
 * Copyright (C) J.P. Morrison, Enterprises, Ltd. 2009, 2012 All Rights Reserved.
 */
package com.jpmorrsn.fbp.test.networks;


import com.jpmorrsn.fbp.components.Discard;
import com.jpmorrsn.fbp.engine.Network;
import com.jpmorrsn.fbp.test.components.GenerateFixedSizeArray;


public class TestFixedSizeArray extends Network {

  @Override
  protected void define() {
    component("Generate", GenerateFixedSizeArray.class);
    component("Discard", Discard.class);
    component("Discard2", Discard.class);
    connect("Generate.OUT", "Discard.IN");
    connect("Generate.OUT[1]", "Discard2.IN");

    initialize("100", component("Generate"), port("COUNT"));
  }

  public static void main(final String[] argv) throws Exception {
    new TestFixedSizeArray().go();

  }

}
