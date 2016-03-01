/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author ivc_LebedevAV
 */
public class RunOptimus extends Thread {
    public static final Logger logger_job = Logger.getLogger(RunOptimus.class);
    private ScenClass selectedScenClass;
    private Jantar12UI jantar12UI;

    public RunOptimus(Jantar12UI jantar12UI, ScenClass selectedScenClass) {
        this.selectedScenClass = selectedScenClass;
        this.jantar12UI = jantar12UI;
    }
    
    
@Override
    public void run() {
            try{
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(LoadData.getPathJantar12() + "BEGIN/Pcx.dat"), "Cp1251"));
                pw.print(selectedScenClass.getName());
                pw.close();
            }catch(Exception e)
            {
                logger_job.log(Level.ERROR, e);
            }
            try{
                Map<String, String> env = new HashMap<String, String>(System.getenv());
                env.put("Path", env.get("Path") + ";"+LoadData.getPathJantar12()+"exe/");
                String[] strings=new String[1];
                strings[0]="Path="+env.get("Path");
                Process process = Runtime.getRuntime().exec("cmd /C start Jantar12.exe",strings, new File(LoadData.getPathJantar12()+"exe/"));

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s;
                while((s = bufferedReader.readLine()) != null) {
                    logger_job.log(Level.INFO,"[Output]"+new String(s.getBytes("ISO-8859-1"), "CP1251"));
                }
                try {
                    if (process.waitFor() != 0) {
                        logger_job.log(Level.ERROR, "exit value = " + process.exitValue());
                    }
                } catch (InterruptedException e) {
                    logger_job.log(Level.ERROR, e);
                }
                jantar12UI.getProgressRun().setVisible(false);
                jantar12UI.stopRunOptimus();

            } catch (Exception e) {

                logger_job.log(Level.ERROR, e);

            }
    }
    
}
