package edu.kpi.fbp.utils;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

/**
 * Utility class which contains method for serialization and deserialization of network model.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class XmlIo {
  private XmlIo() {
    // do nothing
  }

  /**
   * Object serialization.
   * @param obj serialized object
   * @return serialized XML object presentation
   */
  public static String serialize(final Object obj) {
    final XStream stream = new XStream();
    stream.autodetectAnnotations(true);

    final StringWriter sw = new StringWriter();
    stream.marshal(obj,  new PrettyPrintWriter(sw));

    return sw.toString();
  }

  /**
   * Object deserialization.
   * @param in the input stream
   * @param type deserialized object type
   * @param <T> deserialized object type
   * @return deserialized object
   */
  @SuppressWarnings("unchecked")
  public static <T> T deserialize(final InputStream in, final Class<T> type) {
    final XStream stream = new XStream();
    stream.processAnnotations(type);

    return (T) stream.fromXML(in);
  }

  /**
   * Object deserialization.
   * @param in the input file
   * @param type deserialized object type
   * @param <T> deserialized object type
   * @return deserialized object
   */
  @SuppressWarnings("unchecked")
  public static <T> T deserialize(final File in, final Class<T> type) {
    final XStream stream = new XStream();
    stream.processAnnotations(type);

    return (T) stream.fromXML(in);
  }

}
