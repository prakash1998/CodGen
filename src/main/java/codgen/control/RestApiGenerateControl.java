package codgen.control;

import java.util.List;

import codgen.control.interfaces.ParentControl;
import codgen.view.RestApiGenerateWindow;

public class RestApiGenerateControl extends ParentControl<RestApiGenerateWindow> {

	@Override
	protected RestApiGenerateWindow window() {
		return new RestApiGenerateWindow(this);
	}

	public List<String> getAllTables() {
		return DefClassGenerateControl.getInstance().getAllTables();
	}

	public void navigateToRestApiViewer(String tableName , String apiPath, boolean transactional) {
		RestApiGeneratedCodeControl.getInstance().resetParameters(tableName, apiPath, transactional);
		RestApiGeneratedCodeControl.getInstance().openWindow();
		this.closeWindow();
	}

	private static RestApiGenerateControl self;

	public static RestApiGenerateControl getInstance() {
		self = self == null ? new RestApiGenerateControl() : self;
		return self;
	}
}
