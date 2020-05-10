/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.time.LocalDate;
import java.util.Properties;
import javax.swing.JOptionPane;
import lk.IdeaPOS.Model.DatabaseSetting;
import org.w3c.tools.codec.Base64Encoder;

/**
 *
 * @author root
 */
public class frmDbConfigController {

    public boolean ConfigDB(DatabaseSetting setting) {
        //get currnt path
        String pwd = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        // Create Settings Folder
        File file = new File("Settings");
        file.mkdir();
        try {
            //
            if (JOptionPane.showConfirmDialog(null, "Are you Sure? (This will change the system Settings.)", "Warning", 0, 0) == JOptionPane.YES_OPTION) {
                OutputStream outputStream = new FileOutputStream(pwd + "/Settings/Config.properties");
                Properties properties = new Properties();
                properties.setProperty("db.Host", setting.getHost());
                properties.setProperty("db.User", setting.getUser());
                properties.setProperty("db.Pass", new Base64Encoder(setting.getPasswd()).processString());
                properties.setProperty("db.Port", setting.getPort());
                properties.setProperty("db.DbName", setting.getDbName());
                properties.setProperty("db.backupPath", setting.getBackupPath());
                properties.setProperty("db.mysqlDir", setting.getMysqlDir());
                properties.store(outputStream, LocalDate.now().toString());
                JOptionPane.showMessageDialog(null, "Successfuly Configured.!", "Information", 1);
            }
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return false;
    }
}
