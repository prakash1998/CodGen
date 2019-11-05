package codgen.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

import codgen.PROP;
import codgen.control.DatabaseConfigControl;
import codgen.view.interfaces.ParentWindow;
import net.miginfocom.swing.MigLayout;

public class DataBaseConfigWindow extends ParentWindow<DatabaseConfigControl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblDatabaseHost;
	private JLabel lblSid;
	private JLabel lblUserName;
	private JLabel lblPassword;
	private JLabel lblPort;
	private JTextField textFieldHost;
	private JTextField textFieldSID;
	private JTextField textFieldUserName;
	private JFormattedTextField formattedTextFieldPort;
	private JTextField textFieldPassword;
	private JButton btnSave;

	public DataBaseConfigWindow(DatabaseConfigControl control) {

		super(control);

		contentPane.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));

		lblDatabaseHost = new JLabel("DataBase Host :");
		contentPane.add(lblDatabaseHost, "cell 0 0,alignx trailing");

		textFieldHost = new JTextField();
		contentPane.add(textFieldHost, "cell 1 0,growx");
		textFieldHost.setColumns(10);

		lblSid = new JLabel("SID :");
		contentPane.add(lblSid, "cell 0 1,alignx trailing");

		textFieldSID = new JTextField();
		contentPane.add(textFieldSID, "cell 1 1,growx");
		textFieldSID.setColumns(10);

		lblUserName = new JLabel("User Name :");
		contentPane.add(lblUserName, "cell 0 2,alignx trailing");

		textFieldUserName = new JTextField();
		contentPane.add(textFieldUserName, "cell 1 2,growx");
		textFieldUserName.setColumns(10);

		lblPassword = new JLabel("Password :");
		contentPane.add(lblPassword, "cell 0 3,alignx trailing");

		textFieldPassword = new JTextField();
		contentPane.add(textFieldPassword, "cell 1 3,growx");
		textFieldPassword.setColumns(10);

		lblPort = new JLabel("Port :");
		contentPane.add(lblPort, "cell 0 4,alignx trailing");

		formattedTextFieldPort = new JFormattedTextField(NumberFormat.getIntegerInstance());
		contentPane.add(formattedTextFieldPort, "cell 1 4,growx");

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveData();
				control.navigateToHome();
			}
		});
		contentPane.add(btnSave, "cell 1 5");
	}

	private void saveData() {
		Properties properties = new Properties();
		properties.setProperty("host", this.textFieldHost.getText());
		properties.setProperty("sid", this.textFieldSID.getText());
		properties.setProperty("userName", this.textFieldUserName.getText());
		properties.setProperty("password", this.textFieldPassword.getText());
		properties.setProperty("port", this.formattedTextFieldPort.getText());
		PROP.setProperty(PROP.DB_CONFIG, properties);
	}

	@Override
	protected String screenTitle() {
		return "DataBase Configuration";
	}

	@Override
	protected void onload() {
		Properties props = PROP.getInstance(PROP.DB_CONFIG);
		this.textFieldHost.setText(props.getProperty("host"));
		this.textFieldSID.setText(props.getProperty("sid"));
		this.textFieldUserName.setText(props.getProperty("userName"));
		this.textFieldPassword.setText(props.getProperty("password"));
		this.formattedTextFieldPort.setText(props.getProperty("port"));
	}

	@Override
	protected void onunload() {
		// TODO Auto-generated method stub

	}

}
