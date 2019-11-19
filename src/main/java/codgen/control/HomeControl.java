package codgen.control;

import codgen.control.interfaces.ParentControl;
import codgen.view.HomeWindow;

public class HomeControl extends ParentControl<HomeWindow>{

	@Override
	protected HomeWindow window() {
		return new HomeWindow(this);
	}
	
	public void navigateToDatabaseConfig() {
		DatabaseConfigControl.getInstance().openWindow();
		this.closeWindow();
	}
	
	public void navigateToDefClassGeneration() {
		DefClassGenerateControl.getInstance().openWindow();
		this.closeWindow();
	}
	
	public void navigateToRestApiGeneration() {
		RestApiGenerateControl.getInstance().openWindow();
		this.closeWindow();
	}
	
	private static HomeControl self;
	
	public static HomeControl getInstance() {
		self = self == null ? new HomeControl() : self;
		return self;
	}
}
