package edu.kpi.fbp;

/**
 * Single network starter.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class Start {

  /**
   * @param args
   */
  public static void main(final String[] args) {
    try {
      new SampleNetwork(null).go();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

}
