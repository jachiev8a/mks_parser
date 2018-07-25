/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mks;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author uidj5418
 */
public class MksCtcModule {
    
    private String mksModulePath = null;
    private String mksModuleName = null;
    private String ctcModulePath = null;
    
    private String ctcOutputDirectory = null;
    
    private static String MKS_MODULE_NAME_PATTERN = 
        "pkg\\/[\\w]*\\/([\\w]*)\\/\\~[\\w]*.sw.pkg.([\\w]*)";
    
    public MksCtcModule(String mksModulePath, String ctcModulePath){
        this.mksModulePath = mksModulePath;
        this.ctcModulePath = ctcModulePath;
        this.mksModuleName = this.getModuleNameFromMksPath(mksModulePath);
    }
    
    private String getModuleNameFromMksPath(String mksModulePath){
        String moduleName="";
        // Create a Pattern object
        Pattern re = Pattern.compile(MKS_MODULE_NAME_PATTERN);
        // Now create matcher object.
        Matcher matcher = re.matcher(mksModulePath);
        if (matcher.find()) {
           moduleName = matcher.group(1);
        }
        else {
           Logger.getGlobal().warning("RE NOT FOUND");
        }
        return moduleName;
    }

    /**
     * @return the ctcOutputDirectory
     */
    public String getCtcOutputDirectory() {
        return ctcOutputDirectory;
    }

    /**
     * @param ctcOutputDirectory the ctcOutputDirectory to set
     */
    public void setCtcOutputDirectory(String ctcOutputDirectory) {
        this.ctcOutputDirectory = ctcOutputDirectory;
    }
    
    /**
     * @return the mksModulePath
     */
    public String getMksModulePath() {
        return mksModulePath;
    }

    /**
     * @param mksModulePath the mksModulePath to set
     */
    public void setMksModulePath(String mksModulePath) {
        this.mksModulePath = mksModulePath;
    }

    /**
     * @return the mksModuleName
     */
    public String getMksModuleName() {
        return mksModuleName;
    }

    /**
     * @param mksModuleName the mksModuleName to set
     */
    public void setMksModuleName(String mksModuleName) {
        this.mksModuleName = mksModuleName;
    }

    /**
     * @return the ctcModulePath
     */
    public String getCtcModulePath() {
        return ctcModulePath;
    }

    /**
     * @param ctcModulePath the ctcModulePath to set
     */
    public void setCtcModulePath(String ctcModulePath) {
        this.ctcModulePath = ctcModulePath;
    }
    
}
