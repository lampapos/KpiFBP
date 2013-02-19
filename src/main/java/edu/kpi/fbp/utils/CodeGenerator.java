package edu.kpi.fbp.utils;

import edu.kpi.fbp.model.ComponentModel;
import edu.kpi.fbp.model.LinkModel;
import edu.kpi.fbp.model.NetworkModel;

/**
 * Code generator which generates Java code from network object model.
 *
 * $$Кодогенератор: генерує файл вихідного коду мережі JavaFBP.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class CodeGenerator {
  /** Main template. */
  private static final String MAIN_TEMPLATE =
        "import edu.kpi.fbp.javafbp.ParameterizedNetwork;\n"
      + "import edu.kpi.fbp.params.ParametersStore;"
      + "\n"
      + "/**\n"
      + " * Generated network.\n"
      + " */\n"
      + "public class %1$s extends ParameterizedNetwork {\n"
      + "  /**\n"
      + "   * @param paramStore the parameters store\n"
      + "   */\n"
      + "  public %1$s(final ParametersStore paramStore) {\n"
      + "    super(paramStore);\n"
      + "  }\n"
      + "\n"
      + "  /**\n"
      + "   * Network definition.\n"
      + "   * @exception Exception any exceptions\n"
      + "   */\n"
      + "  @Override\n"
      + "  protected void define() throws Exception {\n"
      + "    // Components\n"
      + "%2$s\n"
      + "    // Links\n"
      + "%3$s"
      + "  }\n"
      + "}";

  /** Components and link definition indent. */
  private static final String INDENT = "    ";

  /** Component declaration template. */
  private static final String COMPONENT_TEMPLATE = "component(\"%1$s\", %2$s.class);";

  /** Link declaration template. */
  private static final String LINK_TEMPLATE = "connect(\"%1$s.%2$s\", \"%3$s.%4$s\");";

  /** Import declaration template. */
  private static final String IMPORT_TEMPLATE = "import %s;";

  private CodeGenerator() {
    // do nothing
  }

  private static String getComponentDefinitions(final NetworkModel model) {
    final StringBuilder builder = new StringBuilder();

    for (final ComponentModel comp : model.getComponents()) {
      builder.append(INDENT);
      builder.append(String.format(COMPONENT_TEMPLATE, comp.getName(), comp.getClassName()));
      builder.append("\n");
    }

    return builder.toString();
  }

  private static String getLinkDefinitions(final NetworkModel model) {
    final StringBuilder builder = new StringBuilder();

    for (final LinkModel link : model.getLinks()) {
      builder.append(INDENT);
      builder.append(
          String.format(
              LINK_TEMPLATE,
              link.getFromComponent(), link.getFromPort(),
              link.getToComponent(), link.getToPort()));
      builder.append("\n");
    }

    return builder.toString();
  }

//  private static String getImports(final NetworkModel model) {
//    final StringBuilder builder = new StringBuilder();
//
//    for (final ComponentModel comp : model.getComponents()) {
//      builder.append(String.format(IMPORT_TEMPLATE, comp.getClassName()));
//      builder.append("\n");
//    }
//
//    return builder.toString();
//  }

  /**
   * Generates Java code from network object model.
   * @param networkModel the network object model
   * @return the Java code string
   */
  public static String generate(final NetworkModel networkModel) {
//    final String imports = getImports(networkModel);
    final String networkName = networkModel.getNetworkName();
    final String components = getComponentDefinitions(networkModel);
    final String links = getLinkDefinitions(networkModel);

//    return String.format(MAIN_TEMPLATE, imports, networkName, components, links);
    return String.format(MAIN_TEMPLATE, networkName, components, links);
  }
}
