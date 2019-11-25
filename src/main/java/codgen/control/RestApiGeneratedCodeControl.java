package codgen.control;

import java.util.Properties;

import javax.swing.JOptionPane;

import codgen.PROP;
import codgen.codegenerator.ProgramGenerator;
import codgen.codegenerator.database.connection.DatabaseConnection;
import codgen.codegenerator.programs.MethodGenerators;
import codgen.control.interfaces.ParentControl;
import codgen.view.RestApiGeneratedCodeWindow;

public class RestApiGeneratedCodeControl extends ParentControl<RestApiGeneratedCodeWindow>{

	@Override
	protected RestApiGeneratedCodeWindow window() {
		return new RestApiGeneratedCodeWindow(this);
	}
	
	private String tableName;
	private boolean transactional = false;
	
	public String getTableName() {
		return tableName;
	}

	public boolean isTransactional() {
		return transactional;
	}

	public String getApiPath() {
		return apiPath;
	}

	private String apiPath;
	
	public void resetParameters(String tableName , String apiPath, boolean transactional) {
		this.tableName = tableName;
		this.apiPath = apiPath;
		this.transactional = transactional;
	}
	
	public String getDefClass() {
		Properties props = PROP.getInstance(PROP.DB_CONFIG);
		try {
			DatabaseConnection connection = new DatabaseConnection(props.getProperty("host"), props.getProperty("sid"),
					props.getProperty("userName"), props.getProperty("password"),
					Integer.parseInt(props.getProperty("port")));
			return ProgramGenerator.getGeneratedDefClass(connection, this.tableName);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "inappropriate port!!!!! check database configuration");
			this.navigateToHome();
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "check database configuration");
			this.navigateToHome();
			e.printStackTrace();
		}
		return "Problem While generating Class";
	}
	
	public String getGetDataDao() {
		return MethodGenerators.generateGetDao(this.tableName);
	}
	
	public String getGetDataApi() {
		return MethodGenerators.generateGetApi(this.tableName , this.apiPath);
	}
	
	public String getSaveDataDao() {
		Properties props = PROP.getInstance(PROP.DB_CONFIG);
		try {
			DatabaseConnection connection = new DatabaseConnection(props.getProperty("host"), props.getProperty("sid"),
					props.getProperty("userName"), props.getProperty("password"),
					Integer.parseInt(props.getProperty("port")));
			return MethodGenerators.generateSaveDao(connection,this.tableName);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "inappropriate port!!!!! check database configuration");
			this.navigateToHome();
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "check database configuration");
			this.navigateToHome();
			e.printStackTrace();
		}
		return "Problem While generating Class";
	}
	
	
	public String getSaveDataApi() {
		return MethodGenerators.generateSaveApi(this.tableName , this.apiPath);
	}
	
	public String getDeleteDataDao() {
		Properties props = PROP.getInstance(PROP.DB_CONFIG);
		try {
			DatabaseConnection connection = new DatabaseConnection(props.getProperty("host"), props.getProperty("sid"),
					props.getProperty("userName"), props.getProperty("password"),
					Integer.parseInt(props.getProperty("port")));
			return MethodGenerators.generateDeleteDao(connection,this.tableName);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "inappropriate port!!!!! check database configuration");
			this.navigateToHome();
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "check database configuration");
			this.navigateToHome();
			e.printStackTrace();
		}
		return "Problem While generating Class";
	}
	
	public String getDeleteDataApi() {
		return MethodGenerators.generateDeleteApi(this.tableName , this.apiPath);
	}
	
	private static RestApiGeneratedCodeControl self;
	
	public static RestApiGeneratedCodeControl getInstance() {
		self = self == null ? new RestApiGeneratedCodeControl() : self;
		return self;
	}
}
