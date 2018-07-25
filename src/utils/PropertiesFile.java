/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author uidj5418
 */
public class PropertiesFile {
    
    InputStream input = null;
    public java.util.Properties properties = null;
    public String propertiesFileName = null;
    
    public PropertiesFile(String propertiesFileName){
        this.propertiesFileName = propertiesFileName;
        this.properties = new java.util.Properties();
        this.parseProperties();
    }

    private void parseProperties() {
        try {
            input = new FileInputStream(this.propertiesFileName);
            // load a properties file
            this.properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
