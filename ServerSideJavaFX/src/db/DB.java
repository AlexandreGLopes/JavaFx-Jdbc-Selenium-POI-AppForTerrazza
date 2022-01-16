package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DB {

	private static Logger logger = LogManager.getLogger(DB.class);

	private static Connection conn = null;

	public static Connection getConnection() {
		// Mudança aqui, antes nós verificávamos se a conexxão estava apenas nula. O
		// problema é que o servidor fica em pé initerruptamente e por algum motivo a
		// conexão estava fechando e estavamos recebendo uma exception. Agora estamos
		// verificando se a Conexão está nula ou fechada. se estiver nula ou fechada nós
		// vamos passar uma nova conexão para o CostumerDaoImplJDBC. Lá nós adicionamos
		// um método que faz o trabalho de verficar a conexão antes de fazer o trabalho
		// dos PreparedStatements e ResultStatements
		try {
			if (conn == null || conn.isClosed()) {
				try {
					Properties props = loadProperties();
					String url = props.getProperty("dburl");
					conn = DriverManager.getConnection(url, props);
					System.out.println("Conexão com o BD estabelecida.");
				} catch (SQLException e) {
					logger.error(e.getMessage());
					throw new DbException(e.getMessage());
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new DbException(e.getMessage());
			}
		}
	}

	private static Properties loadProperties() {
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fs);
			return props;
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new DbException(e.getMessage());
		}
	}

	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new DbException(e.getMessage());
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new DbException(e.getMessage());
			}
		}
	}
}
