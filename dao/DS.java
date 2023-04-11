package dao;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DS {
	
	private String driver;
	private String url;
	private String user;
	private String mdp;
	
	public DS() {
		try (InputStream input = new FileInputStream("config.prop")) {

            Properties prop = new Properties();
            prop.load(input);

            driver = prop.getProperty("driver");
            url = prop.getProperty("url");
            user = prop.getProperty("login");
            mdp = prop.getProperty("password");
            
		} catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	public String getDriver() {
		return driver;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getMdp() {
		return mdp;
	}

	public Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(url,user,mdp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	

}