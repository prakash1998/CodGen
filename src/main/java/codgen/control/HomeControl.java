package codgen.control;

import codgen.control.interfaces.ParentControl;
import codgen.view.HomeWindow;

public class HomeControl extends ParentControl<HomeWindow>{
	
	private DatabaseConfigControl databaseConfigControl = DatabaseConfigControl.getInstance();

	@Override
	protected HomeWindow window() {
		return new HomeWindow(this);
	}
	
	public void navigateToDatabaseConfig() {
		databaseConfigControl.openWindow();
		this.closeWindow();
	}
	
	private static HomeControl self;
	
	public static HomeControl getInstance() {
		return self == null ? new HomeControl() : self;
	}
}
