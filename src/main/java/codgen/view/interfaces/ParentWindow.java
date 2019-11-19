package codgen.view.interfaces;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import codgen.control.interfaces.ParentControl;

public abstract class ParentWindow<C extends ParentControl<?>> extends JFrame {
	
	protected C control;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel northPane;
	private JPanel westPane;
	private JPanel southPane;
	private JPanel eastPane;
	private JPanel centerPane;
	private JLabel lblCodeGenerator;
	protected JPanel headerPane;
	protected JPanel contentPane;
	protected JPanel footerPane;
	protected JPanel leftPane;
	protected JPanel rightPane;
	
	
	private JLabel lblScreentitle;
	private JSeparator separator;
	private JSeparator separator_1;
	
	protected abstract String screenTitle();
	
	public ParentWindow(C c) {
		this.control = c;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// java - get screen size using the Toolkit class
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setSize(new Dimension(600, 600));
		// Determine the new location of the window
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (screenSize.width - w) / 2;
		int y = (screenSize.height - h) / 2;

		// Move the window
		this.setLocation(x, y);
		getContentPane().setLayout(new BorderLayout(0, 0));

		northPane = new JPanel();
		getContentPane().add(northPane, BorderLayout.NORTH);

		lblCodeGenerator = new JLabel("Code Generator");
		lblCodeGenerator.setFont(new Font("Tahoma", Font.BOLD, 40));
		northPane.add(lblCodeGenerator);

		westPane = new JPanel();
		getContentPane().add(westPane, BorderLayout.WEST);

		southPane = new JPanel();
		getContentPane().add(southPane, BorderLayout.SOUTH);

		eastPane = new JPanel();
		getContentPane().add(eastPane, BorderLayout.EAST);

		centerPane = new JPanel();
		getContentPane().add(centerPane, BorderLayout.CENTER);
		centerPane.setLayout(new BorderLayout(0, 0));

		headerPane = new JPanel();
		centerPane.add(headerPane, BorderLayout.NORTH);
		headerPane.setLayout(new BoxLayout(headerPane, BoxLayout.Y_AXIS));
		
		separator_1 = new JSeparator();
		headerPane.add(separator_1);
		
		this.setTitle(this.screenTitle());
		lblScreentitle = new JLabel(this.screenTitle());
		lblScreentitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblScreentitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		headerPane.add(lblScreentitle);
		
		separator = new JSeparator();
		headerPane.add(separator);

		contentPane = new JPanel();
		centerPane.add(contentPane, BorderLayout.CENTER);

		footerPane = new JPanel();
		centerPane.add(footerPane, BorderLayout.SOUTH);

		leftPane = new JPanel();
		centerPane.add(leftPane, BorderLayout.WEST);

		rightPane = new JPanel();
		centerPane.add(rightPane, BorderLayout.EAST);

	}
	
	protected abstract void onload();
	
	protected abstract void onunload();
	
	public void showWindow() {
		this.setVisible(true);
		onload();
	}
	
	public void closeWindow() {
		onunload();
		this.dispose();
	}

}
