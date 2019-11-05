package codgen.control;

import codgen.control.interfaces.ParentControl;
import codgen.view.DataBaseConfigWindow;

public class DatabaseConfigControl extends ParentControl<DataBaseConfigWindow>{
	

	@Override
	protected DataBaseConfigWindow window() {
		return new DataBaseConfigWindow(this);
	}
	
	public void navigateToHome() {
		HomeControl.getInstance().openWindow();
		this.closeWindow();
	}
	
	
	private static DatabaseConfigControl self;
	
	public static DatabaseConfigControl getInstance() {
		return self == null ? new DatabaseConfigControl() : self;
	}
}
