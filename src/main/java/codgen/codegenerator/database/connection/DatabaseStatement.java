package codgen.codegenerator.database.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.apache.commons.beanutils.RowSetDynaClass;

public class DatabaseStatement {

	public static Optional<RowSetDynaClass> getResultSet(DatabaseConnection connection, String query) {
		Connection conn = connection.getOracleConnection();
		Statement statement = null;
		try {
			statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return Optional.of(new RowSetDynaClass(resultSet,false));
		} catch (SQLException e) {
			System.out.println("problem while querying the database please check table name");
			e.printStackTrace();
		} finally {
			connection.closeConnection();
		}
		return Optional.empty();
	}

}
