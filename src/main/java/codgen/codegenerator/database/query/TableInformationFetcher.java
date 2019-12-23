package codgen.codegenerator.database.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;
import org.joda.time.LocalDate;

import codgen.codegenerator.database.connection.DatabaseConnection;
import codgen.codegenerator.database.connection.DatabaseStatement;

public class TableInformationFetcher {

	private static Class<?> getSqlToJavaClass(String sqlDataType, int length, int precesion) {

		if (sqlDataType.equals("VARCHAR2")) {
			if (length == 1) {
				return Boolean.class;
			} else {
				return String.class;
			}
		}
		if (sqlDataType.equals("NUMBER")) {
			if (precesion > 0) {
				return BigDecimal.class;
			} else {
				return Integer.class;
			}
		}

		if (sqlDataType.equals("DATE")) {
			return LocalDate.class;
		}

		return String.class;
	}

	public static List<TableColumn> fetch(DatabaseConnection connection, String databaseTableName) {

		List<TableColumn> columnsList = new ArrayList<TableColumn>();

//        String query = "SELECT A.COLUMN_ID,A.COLUMN_NAME,A.DATA_TYPE,A.DATA_LENGTH,A.DATA_PRECISION,A.NULLABLE,B.CONSTRAINT_NAME "
//        		+ "FROM ( SELECT * FROM USER_TAB_COLS WHERE TABLE_NAME = '"+databaseTableName+"' ) A LEFT JOIN " 
//        		+"( SELECT * FROM ALL_CONS_COLUMNS WHERE TABLE_NAME = '"+databaseTableName+"' ) B "
//        		+"ON A.COLUMN_NAME = B.COLUMN_NAME";

		String query = "SELECT A.COLUMN_ID,A.COLUMN_NAME,A.DATA_TYPE,A.DATA_LENGTH,A.DATA_PRECISION,A.NULLABLE "
				+ "FROM  USER_TAB_COLS A WHERE A.TABLE_NAME = '" + databaseTableName + "' ORDER BY A.COLUMN_ID";

		Optional<RowSetDynaClass> mayBeResultSet = DatabaseStatement.getResultSet(connection, query);

		mayBeResultSet.ifPresent(resultSet -> {
			
			List<DynaBean> rows = resultSet.getRows();
			
			rows.forEach(row -> {
				int columnId = getInt(row.get("COLUMN_ID"));
				String columnName = getString(row.get("COLUMN_NAME"));
				String dataType = getString(row.get("DATA_TYPE"));
				int dataLength = getInt(row.get("DATA_LENGTH"));
				int dataPrecision =  getInt(row.get("DATA_PRECISION"));
				boolean isNullable = getString(row.get("NULLABLE")).equals("Y");
				Class<?> columnClass = getSqlToJavaClass(dataType, dataLength, dataPrecision);
				columnsList.add(new TableColumn(columnId, columnName, columnClass, isNullable, false));
			});
			
		});

		List<TableColumn> primaryKeys = fetchPrimaryKeys(connection, databaseTableName);
		return columnsList.stream().map(col -> {
			return primaryKeys.stream().filter(c -> c.getColumnId() == col.getColumnId()).findFirst().orElse(col);
		}).collect(Collectors.toList());
	}

	public static List<TableColumn> fetchPrimaryKeys(DatabaseConnection connection, String databaseTableName) {
		List<TableColumn> columnsList = new ArrayList<TableColumn>();

		String query = "SELECT A.COLUMN_ID,A.COLUMN_NAME,A.DATA_TYPE,A.DATA_LENGTH,A.DATA_PRECISION,A.NULLABLE "
				+ " FROM USER_TAB_COLS A LEFT JOIN " + " (SELECT cols.table_name, cols.column_name "
				+ " FROM all_constraints cons, all_cons_columns cols " + " WHERE cols.table_name = '"
				+ databaseTableName + "'" + " AND cons.constraint_type = 'P' "
				+ " AND cons.constraint_name = cols.constraint_name " + " AND cons.owner = cols.owner) B "
				+ " ON A.TABLE_NAME = B.TABLE_NAME " + " WHERE A.COLUMN_NAME = B.COLUMN_NAME ";

		Optional<RowSetDynaClass> mayBeResultSet = DatabaseStatement.getResultSet(connection, query);

		mayBeResultSet.ifPresent(resultSet -> {
			
			List<DynaBean> rows = resultSet.getRows();
			
			rows.forEach(row -> {
				int columnId = getInt(row.get("COLUMN_ID"));
				String columnName = getString(row.get("COLUMN_NAME"));
				String dataType = getString(row.get("DATA_TYPE"));
				int dataLength = getInt(row.get("DATA_LENGTH"));
				int dataPrecision =  getInt(row.get("DATA_PRECISION"));
				boolean isNullable = false;
				boolean isPrimaryKey = true;
				Class<?> columnClass = getSqlToJavaClass(dataType, dataLength, dataPrecision);
				columnsList.add(new TableColumn(columnId, columnName, columnClass, isNullable, isPrimaryKey));
			});
			
		});

		return columnsList;
	}
	
	public static int getInt(Object obj) {
		if(obj == null)
			return 0;
		return ((BigDecimal)obj).intValue();
	}
	
	public static String getString(Object obj) {
		if(obj == null)
			return "";
		return (String)obj;
	}
	
	public static double getDouble(Object obj) {
		if(obj == null)
			return 0;
		return ((BigDecimal)obj).doubleValue();
	}
}
