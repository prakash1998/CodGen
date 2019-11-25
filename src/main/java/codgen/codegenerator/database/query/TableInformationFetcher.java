package codgen.codegenerator.database.query;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import codgen.codegenerator.database.connection.DatabaseConnection;

public class TableInformationFetcher {

    private static Class<?> getSqlToJavaClass(String sqlDataType , int length,int precesion){

        if(sqlDataType.equals("VARCHAR2")){
            if(length == 1){
                return Boolean.class;
            }else{
                return String.class;
            }
        }
        if(sqlDataType.equals("NUMBER")){
            if(precesion > 0){
                return BigDecimal.class;
            }else{
                return Integer.class;
            }
        }

        if(sqlDataType.equals("DATE")){
                return LocalDate.class;
        }

        return String.class;
    }

    public static List<TableColumn> fetch(DatabaseConnection connection,String databaseTableName)  {

        List<TableColumn> columnsList = new ArrayList<TableColumn>();

//        String query = " select * from " + databaseTableName + " fetch first 1 row only";
        String query = "SELECT A.COLUMN_ID,A.COLUMN_NAME,A.DATA_TYPE,A.DATA_LENGTH,A.DATA_PRECISION,A.NULLABLE,B.CONSTRAINT_NAME "
        		+ "FROM USER_TAB_COLS A LEFT JOIN ALL_CONS_COLUMNS B "
        		+ "ON  B.COLUMN_NAME = A.COLUMN_NAME "
        		+ "WHERE A.TABLE_NAME = '"+databaseTableName+
        		"' AND B.TABLE_NAME = '"+databaseTableName+
        		"' AND B.CONSTRAINT_NAME LIKE 'PK_%'" ;
  
        Connection conn = null;
        try {
            conn = connection.getOracleConnection();
            Statement statement = null;
            try {

                statement = conn.createStatement();

                ResultSet resultSet = statement.executeQuery(query);
                
                
                while(resultSet.next()) {
                	int columnId = resultSet.getInt("COLUMN_ID");
                	String columnName = resultSet.getString("COLUMN_NAME");
                	String dataType = resultSet.getString("DATA_TYPE");
                	int dataLength =  resultSet.getInt("DATA_LENGTH");
                	int dataPrecision =  resultSet.getInt("DATA_PRECISION");
                	boolean isNullable = resultSet.getString("NULLABLE").equals("Y");
                	boolean isPrimaryKey =  resultSet.getString("CONSTRAINT_NAME") != null;
                	Class<?> columnClass = getSqlToJavaClass(dataType,dataLength,dataPrecision);
					columnsList.add(
							new TableColumn(columnId, columnName, columnClass, isNullable, isPrimaryKey));
                }
                
//                ResultSetMetaData rsmd = resultSet.getMetaData();
//                int columnsNumber = rsmd.getColumnCount();
//
//                for (int i = 1; i <= columnsNumber; i++) {
//
//                    Class<?> columnClass = getSqlToJavaClass(rsmd.getColumnTypeName(i),rsmd.getColumnDisplaySize(i),rsmd.getPrecision(i));
//
//                    columnsList.add(new TableColumn(i,rsmd.getColumnName(i),
//                            columnClass,rsmd.isNullable(i) == 1,false));
//
//                }
                
            } catch (SQLException e ) {
                System.out.println("problem while querying the database please check table name");
                e.printStackTrace();
            } finally {
            	conn.close();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("problem while connecting to database");
            e.printStackTrace();
        }

        return columnsList;
    }
   
}
