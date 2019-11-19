package codgen.control.interfaces;

import codgen.control.HomeControl;
import codgen.view.interfaces.ParentWindow;

public abstract class ParentControl<W extends ParentWindow<?>> {
	
	protected W window;
	
	public ParentControl() {
		this.window = window();
	}
	
	protected abstract W window();
	
	public void openWindow() {
		window.showWindow();
	}
	
	public void closeWindow() {
		window.closeWindow();
	}

	public void navigateToHome() {
		HomeControl.getInstance().openWindow();
		this.closeWindow();
	}
	
}
