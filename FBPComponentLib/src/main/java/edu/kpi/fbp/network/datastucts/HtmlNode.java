package edu.kpi.fbp.network.datastucts;

/**
 * Html node.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class HtmlNode {
  private int priority;
  private String html;

  public HtmlNode() {
    // do nothing
  }

  public HtmlNode(final int priority, final String html) {
    super();
    this.priority = priority;
    this.html = html;
  }

  /**
   * @return the HTML tag with body
   */
  public String getHtml() {
    return html;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(final int priority) {
    this.priority = priority;
  }

  @Override
  public String toString() {
    return getHtml();
  }
}
