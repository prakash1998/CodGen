package codgen.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import codgen.PROP;
import codgen.control.DefClassGenerateControl;
import codgen.view.interfaces.ParentWindow;
import net.miginfocom.swing.MigLayout;

public class DefClassGenerateWindow extends ParentWindow<DefClassGenerateControl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblChooseTable;
	private JComboBox<String> comboBoxTables;
	private JLabel lblChooseDefClass;
	private JComboBox<String> comboBoxFolderPaths;
	private JButton btnAdd;
	private Properties filePaths;
	private JButton btnGenerate;
	private JButton btnCancel;

	public DefClassGenerateWindow(DefClassGenerateControl c) {
		super(c);
		contentPane.setLayout(new MigLayout("", "[400][]", "[][][][][][]"));

		lblChooseTable = new JLabel("Choose Table :");
		contentPane.add(lblChooseTable, "cell 0 0,alignx left");

		comboBoxTables = new JComboBox<String>();
		comboBoxTables.setBackground(Color.WHITE);
		contentPane.add(comboBoxTables, "cell 0 1,growx");

		lblChooseDefClass = new JLabel("Def Class DestinationDirectory :");
		contentPane.add(lblChooseDefClass, "cell 0 2,alignx left");

		comboBoxFolderPaths = new JComboBox<String>();
		comboBoxFolderPaths.setBackground(Color.WHITE);
		contentPane.add(comboBoxFolderPaths, "cell 0 3,growx");

		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("select folder");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
					System.out.println("getSelectedFile() : " + chooser.getSelectedFile());

					String path = chooser.getSelectedFile().toString();
					int sizeBeforeAdd = filePaths.keySet().size();
					filePaths.setProperty(path, ".");
					if (filePaths.keySet().size() > sizeBeforeAdd) {
						comboBoxFolderPaths.addItem(path);
						PROP.setProperty(PROP.SAVED_PATHS, filePaths);
					}
				} else {
					JOptionPane.showMessageDialog(null, "No directory selected");
				}
			}
		});
		contentPane.add(btnAdd, "cell 1 3");

		btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBoxTables.getSelectedIndex() >= 0 && comboBoxFolderPaths.getSelectedIndex() >= 0) {
					String tableName = comboBoxTables.getSelectedItem().toString();
					String path = comboBoxFolderPaths.getSelectedItem().toString();
					control.generateDefClass(tableName, path);
				} else {
					JOptionPane.showMessageDialog(null, "Please select both path and table");
				}
			}
		});
		contentPane.add(btnGenerate, "flowx,cell 0 5");

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.navigateToHome();
			}
		});
		contentPane.add(btnCancel, "cell 0 5");

	}

	@Override
	protected String screenTitle() {
		return "Generate Def Class";
	}

	@Override
	protected void onload() {
		List<String> tables = control.getAllTables();
		for (String table : tables)
			this.comboBoxTables.addItem(table);

		this.filePaths = PROP.getInstance(PROP.SAVED_PATHS);
		for (Object path : this.filePaths.keySet())
			this.comboBoxFolderPaths.addItem(path.toString());
	}

	@Override
	protected void onunload() {
		this.comboBoxTables.removeAllItems();
	}

}
