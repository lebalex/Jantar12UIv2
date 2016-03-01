/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import jantar12ui.LoadData;
import java.io.*;
import java.util.Vector;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author ivc_LebedevAV
 */
public class ReadWriteComm {
    public static final Logger logger_job = Logger.getLogger(ReadWriteComm.class);
    public Vector commSuff;
    public Vector commText;

    private void readAll(String projectName)
    {
        commSuff = new Vector();
        commText = new Vector();
        if(projectName.indexOf("/")>=0)
        {
            projectName = projectName.substring(projectName.indexOf("/")+1, projectName.length());
        }
        String fileName = LoadData.getPathJantar12() + "Data/" + projectName+".CMM";
        File file = new File(fileName);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            String str=null;
            while (dis.available() != 0) {
                String val = dis.readLine();
                if(val.trim().length()>0 && val.substring(0, 1).equals("#"))
                {
                    if(str!=null && str.trim().length()>0)
                        commText.add(str.substring(0, str.length()-1));
                    commSuff.add(val);
                    str=null;
                }
                else if(val.trim().length()>0)
                {
                    if(str==null)
                        str = new String(val.trim().getBytes("ISO-8859-1"), "CP1251")+"\n";
                    else
                        str += new String(val.trim().getBytes("ISO-8859-1"), "CP1251")+"\n";
                }
            }
            if(str!=null && str.trim().length()>0)
                        commText.add(str.substring(0, str.length()-1));
            fis.close();
            bis.close();
            dis.close();
        } catch (IOException e) {
            
            logger_job.log(Level.ERROR, e);
        }
    }
    
    
    public String getComm(String projectName, String suff)
    {
        String result = null;
        readAll(projectName);
        if(projectName.indexOf("/")>=0)
            suff = projectName+suff;
        for(int i=0; i<commSuff.size();i++)
        {
            if(((String)commSuff.get(i)).equalsIgnoreCase("#"+suff))
                result = (String)commText.get(i);
        }
        return result;
    }
    
    
    public void setComm(String projectName, String suff, String text)
    {
        readAll(projectName);
        if(projectName.indexOf("/")>=0)
        {
            suff = projectName+suff;
            projectName = projectName.substring(projectName.indexOf("/")+1, projectName.length());
        }
        boolean newRec = true;

        String fileName = LoadData.getPathJantar12() + "Data/" + projectName+".CMM";
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"));
            for(int i=0; i<commSuff.size();i++)
            {
                if(((String)commSuff.get(i)).equalsIgnoreCase("#"+suff))
                {
                    pw.println("#"+suff);
                    pw.println(text);
                    newRec = false;
                }else
                {
                    pw.println((String)commSuff.get(i));
                    pw.println((String)commText.get(i));
                }
            }
            if(newRec)
                {
                    pw.println("#"+suff);
                    pw.println(text);
                }
            pw.close();
        }catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
    }
    
}
