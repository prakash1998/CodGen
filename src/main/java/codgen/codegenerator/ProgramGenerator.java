package codgen.codegenerator;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codgen.codegenerator.database.connection.DatabaseConnection;
import codgen.codegenerator.exception.FileException;
import codgen.codegenerator.programs.api.DefClassGenerator;
import codgen.codegenerator.programs.api.RestAPIGenerator;

public class ProgramGenerator {

    public static void main(String[] args) throws  Exception{

        String path = "src/main/java/codgen";
        String databaseTableName = "UNIT";

        generateDefClass(new DatabaseConnection("localhost","XE","PMGR","PMGR",49191),
                databaseTableName,path);
//        generateApi(databaseTableName,path,"UNITID","EFFECTIVEDATE");

    }
    
    public static List<String> getAllTables(DatabaseConnection connection){
        List<String> tableNameList = new ArrayList<String>();

        String query = "select table_name from user_tables order by table_name";

        Connection conn = null;
        try {
            conn = connection.getOracleConnection();
            Statement statement = null;
            try {

                statement = conn.createStatement();

                ResultSet rs = statement.executeQuery(query);
                
                while(rs.next()) {
                	tableNameList.add(rs.getString(1));
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
        return tableNameList;
    }
    
    public static void generateDefClass(DatabaseConnection connection,String databaseTableName,String path) throws Exception {
        File directory = null;
        try {
            directory = createFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(directory != null){
            DefClassGenerator.generateClassFor(connection,databaseTableName,directory);
        }

    }

    public static void generateApi(DatabaseConnection connection, String databaseTableName, String path, String ... primaryKeyValues) throws Exception {
        File directory = null;
        try {
            directory = createFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(directory != null){
            List<String> primaryKeys = Arrays.asList(primaryKeyValues);
            RestAPIGenerator.generate(connection,databaseTableName,directory,primaryKeys);
        }

    }




    private static File createFile(String path) throws FileException  {
        File file = new File(path);

        if(!file.exists()){
            throw new FileException("Please create directory for generate files - " +path);
        }
        return file;
    }
}
