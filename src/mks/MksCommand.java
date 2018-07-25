/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author uidj5418
 */
public class MksCommand {
    
    private static final String     MKS_ARG_SECRET_PATTERN = "--password\\s?=";
    private static final String     MKS_ARG_SECRET_PASSWORD = "--password=****";
    private static final Pattern    RE = Pattern.compile(MKS_ARG_SECRET_PATTERN);
    private static Matcher          MATCHER;
    
    public  static String MKS_PROJECT_EXT               = "project.pj";
    
    public static final int     EXIT_CODE_SUCCESS = 0;
    public static final int     EXIT_CODE_FAIL = 1;
    
    private String              commandString = null;
    private ArrayList<String>   commandList = new ArrayList<>();
    private String              response = null;
    private int                 exitCode = -1;
    
    public MksCommand(String... commandArgs) {
        for( String cmd : commandArgs ){
            this.commandList.add(cmd);
        }
    }

    public void executeCommand() {
        try {
            // Log command string
            Logger.getGlobal().info(String.format("Sending command: %s", this.toString()));
            
            // call process object
            ProcessBuilder builder = new ProcessBuilder(this.getCommandList());
            builder.redirectErrorStream(true);
            Process cmdProcess = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
            
            // string to hold command responses
            String line;
            String commandResponse="";
            while (true) {
                line = r.readLine();
                if (line == null) { break; }
                commandResponse = commandResponse + line + "\r\n";
            }
            
            // store command response
            this.setResponse(commandResponse);
            Logger.getGlobal().info(String.format("CMD Response: %s",this.getResponse()));
            
            // waiting for Exit Code from command
            int exitVal = cmdProcess.waitFor();
            this.setExitCode(exitVal);
            
            // log error code from command
            Logger.getGlobal().info(String.format("Exited with error code: %s", this.getExitCode()));
            
        } catch (Exception e) {
            Logger.getGlobal().warning(String.format("Command Error: %s", e.toString()));
            e.printStackTrace();
        }
    }
    
    /**
     * 
     */
    public boolean hasFailed() {
        if(this.getExitCode() != EXIT_CODE_SUCCESS){
            return true;
        }
        return false;
    }
    
    /**
     * @return the commandString
     */
    public String getCommandString() {
        return commandString;
    }

    /**
     * @param commandString the commandString to set
     */
    public void setCommandString(String commandString) {
        this.commandString = commandString;
    }

    /**
     * @return the commandList
     */
    public ArrayList<String> getCommandList() {
        return commandList;
    }

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @return the exitCode
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * @param exitCode the exitCode to set
     */
    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }
    
    @Override
    public String toString(){
        String line="";
        for( String stringArg : this.getCommandList() ){
            MATCHER = RE.matcher(stringArg);
            if (MATCHER.find()) {
                line = line + MKS_ARG_SECRET_PASSWORD + " ";
            }
            else{
                line = line + stringArg + " ";
            }
        }
        return line;
    }
    
}
