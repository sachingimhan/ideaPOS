/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Model;

/**
 *
 * @author root
 */
public class DatabaseSetting {
    private String host;
    private String user;
    private String passwd;
    private String port;
    private String dbName;

    public DatabaseSetting() {
    }

    public DatabaseSetting(String host, String user, String passwd, String port, String dbName) {
        this.host = host;
        this.user = user;
        this.passwd = passwd;
        this.port = port;
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "DatabaseSetting{" + "host=" + host + ", user=" + user + ", passwd=" + passwd + ", port=" + port + ", dbName=" + dbName + '}';
    }
    
    
}
