package edu.kpi.fbp.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import edu.kpi.fbp.model.NetworkModel;

/**
 * Executable network JAR builder.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class JarBuilder {
  /** Start class file name.  */
  private static final String START_JAVA = "Start.java";
  /** Network definition class file name.  */
  private static final String NETWORK_JAVA = "SampleNetwork.java";
  /** Gradle build config file name.  */
  private static final String BUILD_GRADLE = "build.gradle";
  /** Start class template resource file name.  */
  private static final String START_TEMPLATE_FILE_NAME = "start.java.template";
  /** Gradle build config template resource file name.  */
  private static final String GRADLE_TEMPLATE_FILE_NAME = "build.gradle.template";
  /** Network classes package path.  */
  private static final String PACKAGE_PATH = "/src/main/java/edu/kpi/fbp";
  /** Built JAR path.  */
  private static final String BUILT_RESULT_PATH = "/build/libs/SampleNetwork-1.0.jar";

  private JarBuilder() {
    // do nothing
  }

  private static void writeFileFromStream(final InputStream sourceStream, final String dirPath, final String fileName)
      throws IOException {
    final OutputStream buildConfigWriter =
        new BufferedOutputStream(new FileOutputStream(new File(dirPath + "/" + fileName)));
    int curByte;
    do {
      curByte = sourceStream.read();
      if (curByte >= 0) {
        buildConfigWriter.write(curByte);
      }
    } while (curByte >= 0);
    buildConfigWriter.flush();
    buildConfigWriter.close();
  }

  /**
   * Jar builder utility.
   * @param network the network model
   * @return the builded jar file
   * @throws IOException exceptions which can be raised during building
   */
  public static File buildJar(final NetworkModel network) throws IOException {
    if (network == null) {
      throw new IllegalArgumentException("Network model can't be null");
    }

    // Prepare directories
    final Path tempDirPath = Files.createTempDirectory("temp");
    final File deepPackage = new File(tempDirPath.toString() + PACKAGE_PATH);
    Files.createDirectories(deepPackage.toPath());

    System.out.println(tempDirPath.toString());

    final ClassLoader classLoader = JarBuilder.class.getClassLoader();

    final BufferedInputStream inGradleConfigStream =
        new BufferedInputStream(classLoader.getResourceAsStream(GRADLE_TEMPLATE_FILE_NAME));
    writeFileFromStream(inGradleConfigStream, tempDirPath.toString(), BUILD_GRADLE);

    final BufferedInputStream inStartTemplateStream =
        new BufferedInputStream(classLoader.getResourceAsStream(START_TEMPLATE_FILE_NAME));
    writeFileFromStream(inStartTemplateStream, deepPackage.getAbsolutePath(), START_JAVA);

    final InputStream networkClassInputStream = new ByteArrayInputStream(CodeGenerator.generate(network).getBytes());
    writeFileFromStream(networkClassInputStream, deepPackage.getAbsolutePath(), NETWORK_JAVA);

    // Configure the connector and create the connection
    final GradleConnector connector = GradleConnector.newConnector();
    connector.forProjectDirectory(tempDirPath.toFile());

    final ProjectConnection connection = connector.connect();
    try {
      connection.newBuild().forTasks("jar").run();
    } finally {
      // Clean up
      connection.close();
    }

    final File outJar = new File(tempDirPath.toString() + BUILT_RESULT_PATH);
    return outJar;
  }

  /**
   * @param network the network model
   * @param resultJar the result JAR path
   * @throws IOException some exceptions during building
   */
  public static void buildAndSaveJar(final NetworkModel network, final File resultJar) throws IOException {
    if (resultJar == null) {
      throw new IllegalArgumentException("Result jar file can't be null");
    }

    final File outJar = buildJar(network);
    final InputStream resInputStream = new BufferedInputStream(new FileInputStream(outJar));
    writeFileFromStream(resInputStream, resultJar.getParent(), resultJar.getName());
  }
}
