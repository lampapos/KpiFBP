package edu.kpi.fbp.utils;

import java.util.Iterator;
import java.util.Map.Entry;

import edu.kpi.fbp.model.ComponentModel;
import edu.kpi.fbp.model.LinkModel;
import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.params.Parameter;
import edu.kpi.fbp.params.ParameterBundle;
import edu.kpi.fbp.params.ParametersStore;

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
        "package edu.kpi.fbp;"
      + "\n"
      + "import java.util.ArrayList;\n"
      + "import java.util.List;\n"
      + "\n"
      + "import edu.kpi.fbp.javafbp.ParameterizedNetwork;\n"
      + "import edu.kpi.fbp.params.Parameter;\n"
      + "import edu.kpi.fbp.params.ParametersStore;\n"
      + "\n"
      + "/**\n"
      + " * Generated network.\n"
      + " */\n"
      + "public class SampleNetwork extends ParameterizedNetwork {\n"
      + "  /**\n"
      + "   * @param paramStore the parameters store\n"
      + "   */\n"
      + "  public SampleNetwork(final ParametersStore paramStore) {\n"
      + "    super(getParameterStore(paramStore));\n"
      + "  }\n"
      + "\n"
      + "  private static ParametersStore getParameterStore(final ParametersStore paramStore) {\n"
      + "    if (paramStore == null) {\n"
      + "%1$s\n"
      + "    } else {\n"
      + "      return paramStore;\n"
      + "    }\n"
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

  private static final String PARAMETER_STORE_CREATION_PATTERN = "final ParametersStore.Builder paramStoreBuilder = new ParametersStore.Builder(0);";
  private static final String LIST_NAME_PATTERN = "parametersOf%s";
  private static final String NEW_LIST_PATTERN = "final List<Parameter> %s = new ArrayList<Parameter>();";
  private static final String ADD_TO_LIST_PATTERN = "%1$s.add(new Parameter(\"%2$s\", \"%3$s\"));";
  private static final String PARAM_STORE_ADD_COMPONENT_PATTERN = "paramStoreBuilder.addComponentConfiguration(\"%1$s\", %2$s);";
  private static final String PARAM_STORE_BUILD_PATTERN = "final ParametersStore pStore = paramStoreBuilder.build();";
  private static final String RETURN_PATTERN = "return pStore;";

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

  private static String getParameterStoreDefinition(final NetworkModel model) {
    final ParametersStore paramStore = model.getParameters();

    if (paramStore == null) {
      return "return null;";
    }

    final Iterator<Entry<String, ParameterBundle>> iter = paramStore.getStoreComponentParametersSet().iterator();

    final String indent = INDENT + INDENT;

    final StringBuilder s = new StringBuilder();

    s.append(indent);
    s.append(PARAMETER_STORE_CREATION_PATTERN);
    s.append("\n\n");

    while (iter.hasNext()) {
      final Entry<String, ParameterBundle> entry = iter.next();

      final String listName = String.format(LIST_NAME_PATTERN, entry.getKey());

      s.append(indent);
      s.append(String.format(NEW_LIST_PATTERN, listName));
      s.append("\n\n");

      for (final Parameter param : entry.getValue().getParameters()) {
        s.append(indent);
        s.append(String.format(ADD_TO_LIST_PATTERN, listName, param.getName(), param.getValue()));
        s.append("\n");
      }

      s.append("\n");
      s.append(indent);
      s.append(String.format(PARAM_STORE_ADD_COMPONENT_PATTERN, entry.getKey(), listName));
      s.append("\n");
    }

    s.append("\n");
    s.append(indent);
    s.append(PARAM_STORE_BUILD_PATTERN);
    s.append("\n");
    s.append(indent);
    s.append(RETURN_PATTERN);
    s.append("\n");

    return s.toString();
  }

  /**
   * Generates Java code from network object model.
   * @param networkModel the network object model
   * @return the Java code string
   */
  public static String generate(final NetworkModel networkModel) {
    final String components = getComponentDefinitions(networkModel);
    final String links = getLinkDefinitions(networkModel);
    final String params = getParameterStoreDefinition(networkModel);

    return String.format(MAIN_TEMPLATE, params, components, links);
  }
}
