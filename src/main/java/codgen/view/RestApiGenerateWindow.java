package codgen.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import codgen.control.RestApiGenerateControl;
import codgen.view.interfaces.ParentWindow;
import net.miginfocom.swing.MigLayout;

public class RestApiGenerateWindow extends ParentWindow<RestApiGenerateControl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblChooseTable;
	private JComboBox<String> comboBoxTables;
	private JButton btnGenerate;
	private JButton btnCancel;
	private JLabel lblRestApiPath;
	private JTextField textFieldRestApiPath;
	private JLabel lblTransactiional;
	private JRadioButton rdbtnYes;
	private JRadioButton rdbtnNo;

	public RestApiGenerateWindow(RestApiGenerateControl c) {
		super(c);
		contentPane.setLayout(new MigLayout("", "[400,grow][]", "[][][][][][][]"));

		lblChooseTable = new JLabel("Choose Table :");
		contentPane.add(lblChooseTable, "cell 0 0,alignx left");

		comboBoxTables = new JComboBox<String>();
		comboBoxTables.setBackground(Color.WHITE);
		contentPane.add(comboBoxTables, "cell 0 1,growx");

		btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBoxTables.getSelectedIndex() >= 0 && !textFieldRestApiPath.getText().isEmpty()) {
					String tableName = comboBoxTables.getSelectedItem().toString();
					String apiPath = textFieldRestApiPath.getText();
					control.navigateToRestApiViewer(tableName,apiPath,rdbtnYes.isSelected());
				} else {
					JOptionPane.showMessageDialog(null, "Please provide both path and table");
				}
			}
		});
		contentPane.add(btnGenerate, "flowx,cell 0 6");
		
		lblRestApiPath = new JLabel("Rest Api path :");
		contentPane.add(lblRestApiPath, "cell 0 2");
		
		textFieldRestApiPath = new JTextField();
		contentPane.add(textFieldRestApiPath, "cell 0 3,growx");
		textFieldRestApiPath.setColumns(10);
		
		lblTransactiional = new JLabel("Transactiional :");
		contentPane.add(lblTransactiional, "cell 0 4");
		

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.navigateToHome();
			}
		});
		contentPane.add(btnCancel, "cell 0 6");
		
		ButtonGroup transactionGroup = new ButtonGroup();
		
		rdbtnYes = new JRadioButton("Yes");
		transactionGroup.add(rdbtnYes);
		contentPane.add(rdbtnYes, "flowx,cell 0 5");
		
		rdbtnNo = new JRadioButton("No");
		rdbtnNo.setSelected(true);
		transactionGroup.add(rdbtnNo);
		contentPane.add(rdbtnNo, "cell 0 5");

	}

	@Override
	protected String screenTitle() {
		return "Generate Rest Api";
	}

	@Override
	protected void onload() {
		List<String> tables = control.getAllTables();
		for (String table : tables)
			this.comboBoxTables.addItem(table);

	}

	@Override
	protected void onunload() {
		this.comboBoxTables.removeAllItems();
		this.textFieldRestApiPath.setText("");
		this.rdbtnNo.setSelected(true);
	}

}
