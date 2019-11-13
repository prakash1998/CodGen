package codgen.codegenerator.database.query;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

        String query = " select * from " + databaseTableName + " fetch first 1 row only";

        Connection conn = null;
        try {
            conn = connection.getOracleConnection();
            Statement statement = null;
            try {

                statement = conn.createStatement();

                ResultSet resultSet = statement.executeQuery(query);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

                for (int i = 1; i <= columnsNumber; i++) {

                    Class<?> columnClass = getSqlToJavaClass(rsmd.getColumnTypeName(i),rsmd.getColumnDisplaySize(i),rsmd.getPrecision(i));

                    columnsList.add(new TableColumn(i,rsmd.getColumnName(i),
                            columnClass,rsmd.isNullable(i) == 1));

                }
                
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
