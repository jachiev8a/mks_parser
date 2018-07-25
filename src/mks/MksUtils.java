/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.FileUtils;

/**
 *
 * @author uidj5418
 */
public class MksUtils {
    
    public static final int ARG_TYPE_NORMAL = 0;
    public static final int ARG_TYPE_SECRET = 1;

    private static String MKS_CMD_SI                    = "si";
    private static String MKS_CMD_SI_CONNECT            = "connect";
    private static String MKS_CMD_SI_SANDBOXES          = "sandboxes";
    private static String MKS_CMD_SI_DROPSANDBOX        = "dropsandbox";
    private static String MKS_CMD_SI_CREATESANDBOX      = "createsandbox";
    
    private static String MKS_ARG_BATCH                 = "--batch";
    private static String MKS_ARG_YES                   = "--yes";
    private static String MKS_ARG_CONFIRM               = "--confirm";
    private static String MKS_ARG_RECURSE               = "--recurse";
    private static String MKS_ARG_POPULATE              = "--populate";
    private static String MKS_ARG_SHUTDOWN              = "--shutdown";
    
    private static String MKS_ARG_DELETE_ALL            = "--delete=all";
    private static String MKS_ARG_FORCE_YES             = "--forceConfirm=yes";
    private static String MKS_ARG_FORCE_POLICY          = "-f";
    private static String MKS_ARG_EXIT                  = "exit";
    
    private static String MKS_ARG_PROJECT               = "--project=\"%s\"";
    private static String MKS_ARG_HOSTNAME              = "--hostname=%s";
    private static String MKS_ARG_PORT                  = "--port=%s";
    private static String MKS_ARG_USER                  = "--user=%s";
    private static String MKS_ARG_PASSWORD              = "--password=%s";
    
    //
    private static String JSON_ARRAY_MODULES            = "modules";
    private static String JSON_OBJ_MKS_PATH             = "mksPath";
    private static String JSON_OBJ_CTC_FILE             = "ctcFile";
    
    public  static String MKS_PROJECT_EXT               = "project.pj";
    
    // original: ([\w]*[\/\\]|[\\\\])*[\w]*([\/\\]|[\\\\])project.pj$
    public  static String MKS_PROJECT_EXT_PATTERN            = 
        "([\\w]*[\\/\\\\]|[\\\\\\\\])*[\\w]*([\\/\\\\]|[\\\\\\\\])project.pj$";
    
    public MksCommand connect(String serverName, String port){
        MksCommand cmd = new MksCommand(
            MKS_CMD_SI,
            MKS_CMD_SI_CONNECT,
            MKS_ARG_BATCH,
            String.format(MKS_ARG_HOSTNAME,serverName),
            String.format(MKS_ARG_PORT,port)
        );
        cmd.executeCommand();
        return cmd;
    }
    
    public MksCommand connect(String serverName, String port, String user, String password){
        MksCommand cmd = new MksCommand(
            MKS_CMD_SI,
            MKS_CMD_SI_CONNECT,
            MKS_ARG_BATCH,
            String.format(MKS_ARG_HOSTNAME,serverName),
            String.format(MKS_ARG_PORT,port),
            String.format(MKS_ARG_USER,user),
            String.format(MKS_ARG_PASSWORD,password)
        );
        cmd.executeCommand();
        return cmd;
    }
    
    public MksCommand showSandboxesRegistered(){
        // si sandboxes
        MksCommand cmd = new MksCommand(
            MKS_CMD_SI,
            MKS_CMD_SI_SANDBOXES
        );
        cmd.executeCommand();
        return cmd;
    }
    
    public MksCommand dropsandbox(String sandboxLocation){
        // ==========================================================
        // si dropsandbox --confirm --delete=none c:/Aurora_Program/project.pj
        // ==========================================================
        String sandboxLocationCopy = sandboxLocation;
        // Create a Pattern object (search for 'project.pj')
        Pattern re = Pattern.compile(MKS_PROJECT_EXT_PATTERN);
        // Now create matcher object.
        Matcher matcher = re.matcher(sandboxLocation);
        if (!matcher.find()) {
            sandboxLocationCopy = sandboxLocationCopy +
                File.separator +
                MKS_PROJECT_EXT;
        }
        MksCommand cmd = new MksCommand(
            MKS_CMD_SI,
            MKS_CMD_SI_DROPSANDBOX,
            MKS_ARG_FORCE_POLICY,
            MKS_ARG_CONFIRM,
            MKS_ARG_FORCE_YES,
            MKS_ARG_YES,
            MKS_ARG_BATCH,
            MKS_ARG_DELETE_ALL,
            sandboxLocationCopy
        );
        cmd.executeCommand();
        return cmd;
    }
    
    public MksCommand createsandbox(String sandboxProject, String directory){
        // si createsandbox --project="#/Top/Sub" sandbox
        MksCommand cmd = new MksCommand(
            MKS_CMD_SI,
            MKS_CMD_SI_CREATESANDBOX,
            MKS_ARG_RECURSE,
            MKS_ARG_POPULATE,
            MKS_ARG_BATCH,
            MKS_ARG_YES,
            MKS_ARG_FORCE_YES,
            String.format(MKS_ARG_PROJECT,sandboxProject),
            directory
        );
        cmd.executeCommand();
        return cmd;
    }
    
    public MksCommand exitClient(){
        MksCommand cmd = new MksCommand(
            MKS_CMD_SI,
            MKS_ARG_EXIT,
            MKS_ARG_SHUTDOWN
        );
        cmd.executeCommand();
        return cmd;
    }
    
    public MksCommand sendCommand(String... commandArgs){
        MksCommand cmd = new MksCommand(commandArgs);
        runMksCommand(cmd);
        return cmd;
    }
    
    private MksCommand runMksCommand(MksCommand mksCmd) {
        try {
            //
            Logger.getGlobal().info(String.format("Sending command: %s", mksCmd.toString()));
            
            ProcessBuilder builder = new ProcessBuilder(mksCmd.getCommandList());
            builder.redirectErrorStream(true);
            Process cmdProcess = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
            String line;
            String commandResponse="";
            while (true) {
                line = r.readLine();
                if (line == null) { break; }
                commandResponse = commandResponse + line + "\r\n";
            }
            // store command response
            mksCmd.setResponse(commandResponse);
            Logger.getGlobal().info(String.format("CMD Response: %s",mksCmd.getResponse()));
            
            // waiting for Exit Code from command
            int exitVal = cmdProcess.waitFor();
            mksCmd.setExitCode(exitVal);
            
            Logger.getGlobal().info(String.format("Exited with error code: %s", mksCmd.getExitCode()));
        } catch (Exception e) {
            Logger.getGlobal().warning(String.format("Command Error: %s", e.toString()));
            e.printStackTrace();
        }
        return mksCmd;
    }

    public ArrayList<MksCtcModule> getMksCtcModuleListFromJson(String jsonFilename) {
        
        String jsonFileContent = FileUtils.parseFileContent(jsonFilename);
        
        JSONObject obj = new JSONObject(jsonFileContent);
        
        JSONArray jsonArrayModules = obj.getJSONArray(JSON_ARRAY_MODULES);

        ArrayList<MksCtcModule> moduleList = new ArrayList<>();
        
        for (int i = 0; i < jsonArrayModules.length(); i++)
        {
            String moduleMksPath = jsonArrayModules.getJSONObject(i).getString(JSON_OBJ_MKS_PATH);
            String moduleCtcFile = jsonArrayModules.getJSONObject(i).getString(JSON_OBJ_CTC_FILE);
            
            Logger.getGlobal().info(String.format("MKS Path Module [%d]: %s",i,moduleMksPath));
            Logger.getGlobal().info(String.format("CTC File Module [%d]: %s",i,moduleCtcFile));
            Logger.getGlobal().info(" ------------------------------------------------ ");
            
            MksCtcModule module = new MksCtcModule(moduleMksPath, moduleCtcFile);
            moduleList.add(module);
        }
        return moduleList;
    }
    
    public HashMap parseModuleList(String filename){
        
        HashMap<String, String> moduleMap = new HashMap<String, String>();
        
        BufferedReader br = null;
        FileReader fr = null;

        try {

            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String[] parts = sCurrentLine.split(",");
                moduleMap.put(parts[0].trim(), parts[1].trim());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
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
        return moduleMap;
    }
    
}
