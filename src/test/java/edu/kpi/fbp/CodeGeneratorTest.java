package edu.kpi.fbp;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.utils.CodeGenerator;
import edu.kpi.fbp.utils.XmlIo;

/**
 * Code generation sample.
 *
 * $$Приклад роботи кодогенератора.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class CodeGeneratorTest {
  /** Result code. */
  private static final String RESULT_CODE =
            "import edu.kpi.fbp.javafbp.ParameterizedNetwork;\n"
          + "import edu.kpi.fbp.params.ParametersStore;\n"
          + "/**\n"
          + " * Generated network.\n"
          + " */\n"
          + "public class SampleNetwork extends ParameterizedNetwork {\n"
          + "  /**\n"
          + "   * @param paramStore the parameters store\n"
          + "   */\n"
          + "  public SampleNetwork(final ParametersStore paramStore) {\n"
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
          + "    component(\"_Generate\", edu.kpi.fbp.network.Generator.class);\n"
          + "    component(\"_Sum\", edu.kpi.fbp.network.Summator.class);\n"
          + "    component(\"_Print_result\", edu.kpi.fbp.network.PrintResult.class);\n"
          + "\n"
          + "    // Links\n"
          + "    connect(\"_Generate.OUT\", \"_Sum.IN\");\n"
          + "    connect(\"_Sum.OUT\", \"_Print_result.IN\");\n"
          + "  }\n"
          + "}";

  /**
   * Entry point.
   */
  @Test
  public void codeGenerationTest() {
    final NetworkModel deserializedModel = XmlIo.deserialize(new File("src/test/resources/out.xml"), NetworkModel.class);
    Assert.assertEquals(RESULT_CODE, CodeGenerator.generate(deserializedModel));
  }
}
