package codgen.codegenerator.database.connection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;

public class ResultSetHolder {
	
	private HashMap<String, Integer> columnMap;	
	private Object[][] data;
	private int rowCount;
	private int columnCount;
	
	public ResultSetHolder(ResultSet resultSet) throws Exception {
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		this.columnCount = resultSetMetaData.getColumnCount();
		this.rowCount = this.resultSetRows(resultSet);
		
		data = new Object[this.rowCount][this.columnCount];
		
		for(int i=0;i<this.rowCount;i++) {
			resultSet.next();
			for(int j=0;j<this.columnCount;j++) {
				data[i][j] = resultSet.getObject(j+1);
			}
		}
		
	}
	
	private int resultSetRows(ResultSet resultSet) {
		int size = 0;
		try {
		    resultSet.last();
		    size = resultSet.getRow();
		    resultSet.beforeFirst();
		}
		catch(Exception ex) {
		    return 0;
		}
		return size;
	}
	

}
