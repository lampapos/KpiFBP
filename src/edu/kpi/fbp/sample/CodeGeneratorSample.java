package edu.kpi.fbp.sample;

import java.io.File;

import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.utils.CodeGenerator;
import edu.kpi.fbp.utils.XmlIo;

/**
 * Code generation sample.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class CodeGeneratorSample {
  private CodeGeneratorSample() {
    // do nothing
  }

  /**
   * Entry point.
   * @param args terminal args
   */
  public static void main(final String[] args) {
    final NetworkModel deserializedModel = XmlIo.deserialize(new File("res/out.xml"), NetworkModel.class);

    System.out.println(CodeGenerator.generate(deserializedModel));
  }
}
