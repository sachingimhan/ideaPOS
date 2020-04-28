/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author root
 */
public class DBUtil {

    private static DBUtil util;
    private final Connection connection;
    private final String url="jdbc:mysql://localhost:3306/ideaPOS";
    private final String user="root";
    private final String password="rockey@123";

    private DBUtil() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        connection=DriverManager.getConnection(url, user, password);
    }

    public static DBUtil getInstance() throws ClassNotFoundException, SQLException {
        return util == null ? util = new DBUtil() : util;
    }

    public Connection getConnection(){
        return connection;
    }
}
