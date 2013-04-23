package edu.kpi.fbp.network.components;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Scanner;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;

import edu.kpi.fbp.network.datastucts.Column;
import edu.kpi.fbp.network.datastucts.NamedArray;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ParamUtils;
import edu.kpi.fbp.params.ParameterType;

/**
 * TSV files reader.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPort(value = TsvReader.PORT_FILE_URL, type = String.class)
@OutPort(value = TsvReader.PORT_OUT_COLUMNS, arrayPort = true, type = NamedArray.class)
@ComponentParameter(port = TsvReader.PORT_FILE_URL, type = ParameterType.STRING, defaultValue = "")
public class TsvReader extends Component {
  private static final String SEPARATOR = "[ \\t]+";
  private static final String NULL_ENTRY = "*";

  public static final String PORT_FILE_URL = "fileUrl";
  public static final String PORT_OUT_COLUMNS = "COLUMNS";

  private InputPort inPortFileUrl;
  private OutputPort[] outPorts;

  @Override
  protected void execute() throws Exception {
    final String fileUrl = ParamUtils.readParam(this, inPortFileUrl);

    if (fileUrl.isEmpty()) {
      System.err.println("TSVREADER: In file path can't be empty");
    }

    final URL tsvFileUrl = new URL(fileUrl);

    final Scanner scanner = new Scanner(new BufferedInputStream(tsvFileUrl.openStream()));
    final String[] titles = scanner.nextLine().split(SEPARATOR);
    final Column[] columns = new Column[titles.length];

    for (int i = 0; i < titles.length; i++) {
      columns[i] = new Column(titles[i]);
    }

    while (scanner.hasNextLine()) {
      final String line = scanner.nextLine();

      final String[] entries = line.split(SEPARATOR);

      for (int i = 0; i < entries.length && i < titles.length; i++) {
        if (entries[i].equals(NULL_ENTRY)) {
          columns[i].add(null);
        } else {
          columns[i].add(Float.parseFloat(entries[i]));
        }
      }
    }

    scanner.close();

    for (final OutputPort port : outPorts) {
      for (final Column col : columns) {
        port.send(create(col));
      }

      port.close();
    }
  }

  @Override
  protected void openPorts() {
    inPortFileUrl = openInput(PORT_FILE_URL);
    outPorts = openOutputArray(PORT_OUT_COLUMNS);
  }
}
