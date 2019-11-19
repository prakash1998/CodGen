package codgen.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import codgen.control.HomeControl;
import codgen.view.interfaces.ParentWindow;
import net.miginfocom.swing.MigLayout;

public class HomeWindow extends ParentWindow<HomeControl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton btnGenerateDefClass;
	private JButton btnEditDatabaseParameter;
	private JButton btnExit;
	private JButton btnGenerateSimpleRest;
	
	
	public HomeWindow(HomeControl control) {
		super(control);

		contentPane.setLayout(new MigLayout("", "[grow]", "[25px][][25px][25px]"));
		
		btnGenerateDefClass = new JButton("Generate Def Class");
		btnGenerateDefClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.navigateToDefClassGeneration();
			}
		});
		contentPane.add(btnGenerateDefClass, "cell 0 0,alignx center,aligny center");
		
		btnEditDatabaseParameter = new JButton("Edit Database Parameter");
		btnEditDatabaseParameter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.navigateToDatabaseConfig();
			}
		});
		
		btnGenerateSimpleRest = new JButton("Generate Simple REST APIs");
		btnGenerateSimpleRest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.navigateToRestApiGeneration();
			}
		});
		contentPane.add(btnGenerateSimpleRest, "cell 0 1,alignx center");
		contentPane.add(btnEditDatabaseParameter, "cell 0 2,alignx center,aligny center");
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.closeWindow();
			}
		});
		contentPane.add(btnExit, "cell 0 3,alignx center,aligny center");


	}

	@Override
	protected String screenTitle() {
		return "Home";
	}

	@Override
	protected void onload() {
		
	}

	@Override
	protected void onunload() {
		// TODO Auto-generated method stub
		
	}
}
