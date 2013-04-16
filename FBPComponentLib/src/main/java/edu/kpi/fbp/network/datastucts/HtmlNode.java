package edu.kpi.fbp.network.datastucts;

/**
 * Html node.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class HtmlNode {
  private int priority;
  private String html;

  /**
   * Default constructor.
   */
  public HtmlNode() {
    // do nothing
  }

  /**
   * @param priority the node report priority
   * @param html the html body
   */
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

  /**
   * @return the node report priority
   */
  public int getPriority() {
    return priority;
  }

  /**
   * @param priority the node report priority
   */
  public void setPriority(final int priority) {
    this.priority = priority;
  }

  @Override
  public String toString() {
    return getHtml();
  }
}
