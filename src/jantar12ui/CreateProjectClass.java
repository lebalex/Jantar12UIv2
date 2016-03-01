/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author ivc_LebedevAV
 */
public class CreateProjectClass {
    public static final Logger logger_job = Logger.getLogger(CreateProjectClass.class);
    private ExtNameClass extNameClass = new ExtNameClass();
    
    public boolean createProject(String name, String descript, String path) {
        boolean result=true;
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path + "BEGIN/PcxAll.dat", true), "Cp1251"));
            pw.print(String.format("%1$-9s",name));
            pw.println(descript);
            pw.close();
        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        try{
            List<String> ext = extNameClass.getListAllFilesBase();
            for(int i=0; i<ext.size(); i++)
            {
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path + "Data/"+name+"."+ext.get(i), true), "Cp1251"));
                if(ext.get(i).equalsIgnoreCase("SXM"))
                {
                    pw.println("0 0 0 0 5 0.95 1000 0.05");
                }
                pw.close();
            }
            List<String> extInterval = extNameClass.getListAllFilesInterval();
            for(int interval=1; interval<=12; interval++)
            {
                String idxInterval = interval+"";
                if(interval<10) idxInterval="0"+interval;
                for(int i=0; i<extInterval.size(); i++)
                {
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path + "Data/INTERV"+idxInterval+"/"+name+"."+extInterval.get(i), true), "Cp1251"));
                    pw.close();
                }
            }
            
        }catch(Exception e)
        {
            logger_job.log(Level.ERROR, e);
        }
        return result;
    }
    
}
