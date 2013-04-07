package com.jpmorrsn.fbp.test.networks;


import com.jpmorrsn.fbp.engine.Network;


public class TestInfiniteQueue extends Network {

  String description = "Test Infinite Queue";

  @Override
  protected void define() {
    component("__ Generate", com.jpmorrsn.fbp.components.Generate.class);
    component("_ Infinite_  Queue", com.jpmorrsn.fbp.test.networks.InfiniteQueue.class);
    component("__  Display", com.jpmorrsn.fbp.components.WriteToConsole.class);
    connect(component("_ Infinite_  Queue"), port("OUT"), component("__  Display"), port("IN"));
    initialize("100", component("__ Generate"), port("COUNT"));
    connect(component("__ Generate"), port("OUT"), component("_ Infinite_  Queue"), port("IN"));
    initialize("temp.data", component("_ Infinite_  Queue"), port("TEMPFILENAME"));

  }

  public static void main(final String[] argv) throws Exception {
    // try this test 50 times!
    for (int i = 0; i < 50; i++) {
      new TestInfiniteQueue().go();
    }
  }

}
