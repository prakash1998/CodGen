package codgen.codegenerator;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import codgen.codegenerator.database.connection.DatabaseConnection;
import codgen.codegenerator.database.connection.DatabaseStatement;
import codgen.codegenerator.database.query.TableColumn;
import codgen.codegenerator.database.query.TableInformationFetcher;
import codgen.codegenerator.exception.FileException;
import codgen.codegenerator.programs.api.DefClassGenerator;
import codgen.codegenerator.programs.api.RestAPIGenerator;

public class ProgramGenerator {

	public static void main(String[] args) throws Exception {

		String path = "src/main/java/codgen/test";
		String databaseTableName = "UNIT";
		
		
		DatabaseConnection connection = new DatabaseConnection("localhost", "XE", "PMGR", "PMGR", 49191);
		
		List<String> allTables = getAllTables(connection);
		
		List<String> someTables = allTables.stream().limit(2).collect(Collectors.toList());
		
		someTables.forEach( table -> {
	        generateApi( connection,table,path);
		});

//		generateDefClass(connection, databaseTableName, path);


	}

	public static List<String> getAllTables(DatabaseConnection connection) {
		List<String> tableNameList = new ArrayList<String>();

		String query = "select table_name from user_tables order by table_name";

		Optional<RowSetDynaClass> mayBeResultSet = DatabaseStatement.getResultSet(connection, query);

		mayBeResultSet.ifPresent(rs -> {
			
			List<DynaBean> rows  = rs.getRows();
			
			rows.forEach(row -> {	
				tableNameList.add((String)row.get("TABLE_NAME"));
			});
		
		});

		return tableNameList;
	}

	public static void generateDefClass(DatabaseConnection connection, String databaseTableName, String path)
			throws Exception {
		File directory = null;
		try {
			directory = createFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (directory != null) {
			DefClassGenerator.generateClassFor(connection, databaseTableName, directory);
		}

	}

	public static String getGeneratedDefClass(DatabaseConnection connection, String databaseTableName)
			throws Exception {
		return DefClassGenerator.getGeneratedClassFor(connection, databaseTableName);
	}

	public static void generateApi(DatabaseConnection connection, String databaseTableName, String path) {
		File directory = null;
		try {
			directory = createFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (directory != null) {
			List<String> primaryKeys = ProgramGenerator.getPrimaryKeys(connection, databaseTableName);
			RestAPIGenerator.generate(connection, databaseTableName, directory, primaryKeys);
		}

	}

	public static List<String> getPrimaryKeys(DatabaseConnection connection, String databaseTableName) {
		List<TableColumn> columnInfo = TableInformationFetcher.fetchPrimaryKeys(connection, databaseTableName);
		return columnInfo.stream().filter(TableColumn::isPrimaryKey).map(TableColumn::getColumnName)
				.collect(Collectors.toList());
	}

	private static File createFile(String path) throws FileException {
		File file = new File(path);

		if (!file.exists()) {
			throw new FileException("Please create directory for generate files - " + path);
		}
		return file;
	}
}
