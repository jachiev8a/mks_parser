/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author uidj5418
 */
public class FileUtils {
    
    public static String parseFileContent(String filename) {
        
        BufferedReader br = null;
        FileReader fr = null;
        String fileContent = null;
        
        try {

            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String sCurrentLine;

            if (br.ready()) {
                fileContent = "";
            }
            
            while ((sCurrentLine = br.readLine()) != null) {
                fileContent = fileContent + sCurrentLine;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            fileContent = null;
        }
        finally {
            try {

                if (br != null)
                        br.close();

                if (fr != null)
                        fr.close();

            } catch (IOException ex) {
                    ex.printStackTrace();

            }
        }
        return fileContent;
    }
    
}
