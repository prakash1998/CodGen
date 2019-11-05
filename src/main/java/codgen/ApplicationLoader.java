package codgen;

import codgen.control.HomeControl;

public class ApplicationLoader {

	public static void main(String[] args) {
		PROP.init();
		new HomeControl().openWindow();
	}

	public static void exitApp() {
		System.exit(0);
	}

}
