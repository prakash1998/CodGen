package codgen.control;

import codgen.control.interfaces.ParentControl;
import codgen.view.DataBaseConfigWindow;

public class DatabaseConfigControl extends ParentControl<DataBaseConfigWindow>{
	

	@Override
	protected DataBaseConfigWindow window() {
		return new DataBaseConfigWindow(this);
	}
	
	private static DatabaseConfigControl self;
	
	public static DatabaseConfigControl getInstance() {
		self = self == null ? new DatabaseConfigControl() : self;
		return self;
	}
}
