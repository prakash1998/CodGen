/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codgen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * PROP Singleton class used for reading properties files.
 *
 */
public class PROP {
	public static final String DB_CONFIG = "DB_CONFIG";
	public static final String APP = "APP";

	private static Map<String, Properties> props = new HashMap<String, Properties>();

	/**
	 * Private constructor so this class cannot be instantiated only by it self.
	 */
	private PROP() {
	}

	/**
	 * Lazy init for this Singleton Class.
	 *
	 * @return The Properties object.
	 */
	public static Properties getInstance(String fileName) {
		if (props.get(fileName) == null) {
			
			try (InputStream input = new FileInputStream("data/"+fileName+".properties")) {
				Properties properties = new Properties();
				// load app.properties :
				properties.load(input);
				props.put(fileName, properties);

			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "problem while reading property file "+ fileName);
				return new Properties();
			}
		}

		return props.get(fileName);
	}

	public static String getProperty(String fileName, String key) {
		return PROP.getInstance(fileName).getProperty(key);
	}

	public static void setProperty(String fileName, String key, String value) {
		try (OutputStream output = new FileOutputStream("data/"+fileName+".properties")) {
			PROP.getInstance(fileName).setProperty(key, value);
			props.get(fileName).store( output, null);
        } catch (IOException io) {
        	io.printStackTrace();
        	JOptionPane.showMessageDialog(null, "problem while writing to file " +fileName);
        }
	}
	
	public static void setProperty(String fileName, Properties properties) {
		try (OutputStream output = new FileOutputStream("data/"+fileName+".properties")) {
			properties.store(output, null);
			props.put(fileName, properties);
        } catch (IOException io) {
        	io.printStackTrace();
        	JOptionPane.showMessageDialog(null, "problem while writing to file " +fileName);
        }
	}

	/**
	 * Create the instance for the first time.
	 */
	public static void init() {
		for (Field f : PROP.class.getDeclaredFields()) {
			if (f.getType() == String.class)
				PROP.getInstance(f.getName());
		}
	}
}
