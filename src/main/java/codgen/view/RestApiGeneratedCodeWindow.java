package codgen.view;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import codgen.control.RestApiGeneratedCodeControl;
import codgen.view.interfaces.ParentWindow;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RestApiGeneratedCodeWindow extends ParentWindow<RestApiGeneratedCodeControl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPaneSaveApi;
	private JTextArea textAreaSaveApi;
	private JScrollPane scrollPaneGetDataApi;
	private JTextArea textAreaGetApi;
	private JScrollPane scrollPaneDefClass;
	private JTextArea textAreaDefClass;
	private JScrollPane scrollPaneDeleteApi;
	private JTextArea textAreaDeleteApi;
	private JTabbedPane tabbedPaneGetData;
	private JTabbedPane tabbedPaneSaveData;
	private JTabbedPane tabbedPaneDeleteData;
	private JScrollPane scrollPaneGetDataDao;
	private JTextArea textAreaGetDao;
	private JScrollPane scrollPaneSaveDao;
	private JTextArea textAreaSaveDao;
	private JScrollPane scrollPaneDeleteDao;
	private JTextArea textAreaDeleteDao;
	private JButton btnHome;
	
	
	public RestApiGeneratedCodeWindow(RestApiGeneratedCodeControl control) {
		super(control);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		scrollPaneDefClass = new JScrollPane();
		tabbedPane.addTab("Def Class", null, scrollPaneDefClass, null);
		
		textAreaDefClass = new JTextArea();
		scrollPaneDefClass.setViewportView(textAreaDefClass);
		
		tabbedPaneGetData = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Get Api", null, tabbedPaneGetData, null);

		scrollPaneGetDataApi = new JScrollPane();
		tabbedPaneGetData.addTab("Get Data Api", null, scrollPaneGetDataApi, null);
		
		textAreaGetApi = new JTextArea();
		scrollPaneGetDataApi.setViewportView(textAreaGetApi);
		
		scrollPaneGetDataDao = new JScrollPane();
		tabbedPaneGetData.addTab("Get Data Dao", null, scrollPaneGetDataDao, null);
		
		textAreaGetDao = new JTextArea();
		scrollPaneGetDataDao.setViewportView(textAreaGetDao);
		
		tabbedPaneSaveData = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Save Api", null, tabbedPaneSaveData, null);
		
		scrollPaneSaveApi = new JScrollPane();
		tabbedPaneSaveData.addTab("Save Api", null, scrollPaneSaveApi, null);
		
		textAreaSaveApi = new JTextArea();
		scrollPaneSaveApi.setViewportView(textAreaSaveApi);
		
		scrollPaneSaveDao = new JScrollPane();
		tabbedPaneSaveData.addTab("Save Dao", null, scrollPaneSaveDao, null);
		
		textAreaSaveDao = new JTextArea();
		scrollPaneSaveDao.setViewportView(textAreaSaveDao);
		
		tabbedPaneDeleteData = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Delete Api", null, tabbedPaneDeleteData, null);
		
		scrollPaneDeleteApi = new JScrollPane();
		tabbedPaneDeleteData.addTab("Delete Api", null, scrollPaneDeleteApi, null);
		
		textAreaDeleteApi = new JTextArea();
		scrollPaneDeleteApi.setViewportView(textAreaDeleteApi);
		
		scrollPaneDeleteDao = new JScrollPane();
		tabbedPaneDeleteData.addTab("Delete Dao", null, scrollPaneDeleteDao, null);
		
		textAreaDeleteDao = new JTextArea();
		scrollPaneDeleteDao.setViewportView(textAreaDeleteDao);
		
		btnHome = new JButton("Home");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.navigateToHome();
			}
		});
		footerPane.add(btnHome);
	}

	@Override
	protected String screenTitle() {
		return "View Rest Api";
	}

	@Override
	protected void onload() {
		textAreaDefClass.setText(control.getDefClass());
		textAreaGetDao.setText(control.getGetDataDao());
		textAreaGetApi.setText(control.getGetDataApi());
	}

	@Override
	protected void onunload() {
		// TODO Auto-generated method stub
		
	}
}
