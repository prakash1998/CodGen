package codgen.codegenerator.programs.api;

import java.io.File;
import java.util.List;

import codgen.codegenerator.database.connection.DatabaseConnection;

public class RestAPIGenerator {

    public static void generate(DatabaseConnection connection, String databaseTableName, File path , List<String> primaryKeys) {
        DefClassGenerator.generateClassFor(connection,databaseTableName,new File(path.getAbsolutePath()+"/immutables")) ;
        ApiClassGenerator.generate(databaseTableName,new File(path.getAbsolutePath()+"/api"));
        DAOClassGenerator.generate(databaseTableName,new File(path.getAbsolutePath()+"/access"),primaryKeys);
    }


}
