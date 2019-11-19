package codgen.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import codgen.PROP;
import codgen.codegenerator.ProgramGenerator;
import codgen.codegenerator.database.connection.DatabaseConnection;
import codgen.control.interfaces.ParentControl;
import codgen.view.DefClassGenerateWindow;

public class DefClassGenerateControl extends ParentControl<DefClassGenerateWindow> {

	@Override
	protected DefClassGenerateWindow window() {
		return new DefClassGenerateWindow(this);
	}

	public List<String> getAllTables() {
		Properties props = PROP.getInstance(PROP.DB_CONFIG);
		try {
			DatabaseConnection connection = new DatabaseConnection(props.getProperty("host"), props.getProperty("sid"),
					props.getProperty("userName"), props.getProperty("password"),
					Integer.parseInt(props.getProperty("port")));
			return ProgramGenerator.getAllTables(connection);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "inappropriate port!!!!! check database configuration");
			e.printStackTrace();
			this.closeWindow();
			this.navigateToHome();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "check database configuration");
			e.printStackTrace();
			this.closeWindow();
			this.navigateToHome();
		}
		return new ArrayList<String>();
	}

	public void generateDefClass(String tableName, String path) {
		Properties props = PROP.getInstance(PROP.DB_CONFIG);
		try {
			DatabaseConnection connection = new DatabaseConnection(props.getProperty("host"), props.getProperty("sid"),
					props.getProperty("userName"), props.getProperty("password"),
					Integer.parseInt(props.getProperty("port")));
			ProgramGenerator.generateDefClass(connection, tableName, path);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "inappropriate port!!!!! check database configuration");
			this.navigateToHome();
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "check database configuration");
			this.navigateToHome();
			e.printStackTrace();
		}
	}

	private static DefClassGenerateControl self;

	public static DefClassGenerateControl getInstance() {
		self = self == null ? new DefClassGenerateControl() : self;
		return self;
	}
}
