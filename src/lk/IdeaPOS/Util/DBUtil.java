/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import lk.IdeaPOS.Model.DatabaseSetting;

/**
 *
 * @author root
 */
public class DBUtil {

    private static DBUtil util;
    private Connection connection = null;
//    private final String url="jdbc:mysql://localhost:3306/ideaPOS";
//    private final String user="root";
//    private final String password="rockey@123";

    private DBUtil() throws ClassNotFoundException, NullPointerException, SQLException {
        DatabaseSetting rp = readProperties();
        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + rp.getHost() + ":" + rp.getPort() + "/" + rp.getDbName() + "", rp.getUser(), rp.getPasswd());
    }

    public static DBUtil getInstance() throws ClassNotFoundException, SQLException {
        return util == null ? util = new DBUtil() : util;
    }

    public Connection getConnection() {
        return connection;
    }

    private DatabaseSetting readProperties() {
        //get currnt path
        String pwd = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        try {
            File file = new File(pwd + "/Settings/Config.properties");
            if (file.exists() && file.isFile()) {
                InputStream inputStream = new FileInputStream(pwd + "/Settings/Config.properties");
                Properties p = new Properties();
                p.load(inputStream);
                System.out.println("Wor");
                return new DatabaseSetting(
                        p.getProperty("db.Host"),
                        p.getProperty("db.User"),
                        p.getProperty("db.Pass"),
                        p.getProperty("db.Port"),
                        p.getProperty("db.DbName")
                );
            } else {
                MessageBox.showErrorMessage("Database Configuratin Not Found", "Error");
            }
        } catch (IOException ex) {
            MessageBox.showErrorMessage(ex.getLocalizedMessage(), "Error");
        }
        return null;
    }
}
