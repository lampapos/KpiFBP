package com.jpmorrsn.fbp.test.networks;


import com.jpmorrsn.fbp.engine.Network;


public class FBPNetworkTemplate extends Network {

  static final String copyright = "...";

  @Override
  protected void define() {
    /* fill in your network definition here */

  }

  public static void main(final String[] argv) throws Exception {
    new FBPNetworkTemplate().go();
  }
}
