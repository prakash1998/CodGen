package codgen;

import codgen.control.HomeControl;

public class ApplicationLoader {

	public static void main(String[] args) {
		PROP.init();
		System.out.println(PROP.getProperty(PROP.DB_CONFIG,"AppName"));
		System.out.println(PROP.getProperty(PROP.APP,"AppName"));
		PROP.setProperty(PROP.APP,"AppName","dddd");
		System.out.println(PROP.getProperty(PROP.APP,"AppName"));
		new HomeControl().openWindow();
	}

	public static void exitApp() {
		System.exit(0);
	}

}
