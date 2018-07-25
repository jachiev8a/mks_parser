/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import mks.MksCommand;
import mks.MksCtcModule;
import mks.MksUtils;

/**
 *
 * @author uidj5418
 */
public class Kpi_mks_parser {
    
    private static String JIRASERVER;
    private static String PROJECT;
    private static String ISSUETYPE;
    private static String FIELDS;
    private static String JIRAUSER;
    private static String JIRAPASS;
    private static String RESULTPATH;
    
//    private static String JIRASERVER ="jira-id.conti.de:7070";
//    private static String PROJECT = "UFC";
//    private static String ISSUETYPE = "10102";
//    private static String FIELDS = "customfield_11335[value],customfield_11307[value],status[name],created[]";
//    private static String JIRAUSER = "uia83750";
//    private static String JIRAPASS = "MPbHfp595C";
//    private static String RESULTPATH = "output.xml";
    
    private static final String ARGHELP = ""
        + "To run this program you should add X arguments:\r\n"
        + "MKS_Parser [SERVERNAME] [PORT] [ISSUETYPE] [FIELDS] [JIRAUSER] [JIRAPASSWORD] [RESULTPATH]\r\n"
        + "\t SERVERNAME = ims-mks server name (default = ims-id)\r\n"
        + "\t PORT = value TCP/IP port number of server (default = 7001)\r\n"
        + "\t ISSUETYPE = Issue key type to be searched\r\n"
        + "\t FIELDS = Fields to return separated by comma field1[valueName],field2[valueName],field3[valueName]\r\n"
        + "\t JIRAUSER = Jira user uid with rights on the project\r\n"
        + "\t JIRAPASSWORD = Jira user password\r\n"
        + "\t RESULTPATH = Fully qualified directory to store results\r\n";
    
    private static final int MAX_NUMBER_ARGS = 5;
    
    public static void main(String[] args) throws Exception {
        
        /*
        if(args.length != MAX_NUMBER_ARGS)
        {
            System.out.println("Arguments count incorrect");
            System.out.print(ARGHELP);
            System.exit(1);
        }

        for (String arg : args) {
            if(arg.equals("help"))
            {
                System.out.print(ARGHELP);
                System.exit(1);
            }
        }
        */
        
        String server = "ims-id";
        String port = "7001";
        
        //String user = "uia83750";
        String user = "uidj5418";
        //String user = "uidu076z";
        //String user = "invalid";
        String pass = "invalid";
        
        String proj = "/id/fca_IC01/sw/pkg/bsp/altiawm/~fca_IC01.sw.pkg.altiawm_COMMON_Var2/project.pj";
        
        String sandboxOutputDirectory = "D:/TEMP/_ctc/mks/";
        
        String sandboxDirName = "sandbox_fca_dt_test/";
        
        String sandboxProjectName = sandboxDirName+"project.pj";
        
        String ctcFilePath = "prv/metrics/CTC/ALTIAWM_CTC_REPORT_23_02_2017.zip";
        
        String moduleListFile = "D:/TEMP/module_list.txt";
        String moduleListJsonFile = "D:/TEMP/JSON_OUT.json";
        
        // Create logger and set level
        Logger.getGlobal().setLevel(Level.INFO);
        
        
        // create MKS Manager object to use utilities
        MksUtils mksManager = new MksUtils();
        
        ArrayList<MksCtcModule> moduleList = new ArrayList<>();
        
        // map
        /*
        HashMap moduleMap = mksManager.parseModuleList(moduleListFile);
        ArrayList<MksCtcModule> moduleList = new ArrayList<>();
        
        moduleMap.forEach((modulePath, moduleCtcPath) -> {
            MksCtcModule module = new MksCtcModule(modulePath.toString(), moduleCtcPath.toString());
            moduleList.add(module);
        });
        */
        
        moduleList = mksManager.getMksCtcModuleListFromJson(moduleListJsonFile);

        MksCommand cmdConnect = mksManager.connect(server, port, user, pass);
        
        if (cmdConnect.hasFailed()) {
            System.exit(cmdConnect.getExitCode());
        }
        
        // drop sandboxes
        for( MksCtcModule module : moduleList ){
            mksManager.dropsandbox(sandboxOutputDirectory+module.getMksModuleName());
        }
        
        Thread.sleep(6000);
        
        // create sandboxes
        for( MksCtcModule module : moduleList ){
            MksCommand cmdCreateSandbox = mksManager.createsandbox(module.getMksModulePath(),sandboxOutputDirectory+module.getMksModuleName());
            if (cmdCreateSandbox.hasFailed()) {
                System.exit(cmdCreateSandbox.getExitCode());
            }
        }
        
        System.exit(0);
        
        mksManager.dropsandbox(sandboxOutputDirectory+sandboxProjectName);
        
        mksManager.showSandboxesRegistered();
        
        Thread.sleep(6000);
        
        mksManager.showSandboxesRegistered();
        
        mksManager.exitClient();
        
        //mksManager.sendCommand("si", "--usage");
        
        //mksManager.sendCommand("si exit --shutdown");
        
    }
    
}
