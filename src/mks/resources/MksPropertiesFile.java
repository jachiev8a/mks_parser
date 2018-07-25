/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mks.resources;

import utils.PropertiesFile;

/**
 *
 * @author uidj5418
 */
public class MksPropertiesFile extends PropertiesFile {
    
    public MksPropertiesFile(String propertiesFileName) {
        super(propertiesFileName);
    }
    
    public void printProperties(){
        System.out.println(this.properties.getProperty("HostName"));
        System.out.println(this.properties.getProperty("Port"));
    }
    
}
