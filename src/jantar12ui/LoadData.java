/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author LebAlex
 */
public class LoadData {
    public static final Logger logger_job = Logger.getLogger(LoadData.class);
    public static final String GLOBALPRO = "./pro/main.properties";
    public static final String GLOBALLOC = "./pro/loc.properties";
    public static final String GLOBALLOCDESCRIPT = "./pro/locDescript.properties";
    public static final String GLOBALTITLES = "./pro/titles.properties";
    public static final String GLOBALLOC_R = "loc.properties";
    public static final String GLOBALLOCDESCRIPT_R = "locDescript.properties";
    public static final String GLOBALTITLES_R = "titles.properties";
    private static LoadData instance;
    private static Properties _propLoc;
    private static Properties _propLocDescript;
    private static Properties _propTitles;
    private static Properties _propPro;
    private static boolean error_GLOBALTITLES=false;
    private static boolean error_GLOBALLOCDESCRIPT=false;
    private static boolean error_GLOBALLOC=false;
    /*
     * private DataClass dataClass = new DataClass();
     */

    public static LoadData getInstance() {
        if(instance==null)
            instance=new LoadData();
        return instance;
    }

    public LoadData() {
        getProp();
        if(error_GLOBALTITLES)
        {
            try{
                InputStream is=this.getClass().getResourceAsStream(GLOBALTITLES_R);
                _propTitles.load(is);
                is.close();
                copeFromRezerv(_propTitles, GLOBALTITLES);
              } catch (Exception e) {
                logger_job.log(Level.ERROR, e);
                logger_job.log(Level.ERROR, "Ошибка загрузки файла конфигураций :" + GLOBALTITLES_R);
        }  
        }
        if(error_GLOBALLOCDESCRIPT)
        {
            try{
                InputStream is=this.getClass().getResourceAsStream(GLOBALLOCDESCRIPT_R);
                _propLocDescript.load(is);
                is.close();
                copeFromRezerv(_propLocDescript, GLOBALLOCDESCRIPT);
              } catch (Exception e) {
                logger_job.log(Level.ERROR, e);
                logger_job.log(Level.ERROR, "Ошибка загрузки файла конфигураций :" + GLOBALLOCDESCRIPT_R);
        }  
        }
        if(error_GLOBALLOC)
        {
            try{
                InputStream is=this.getClass().getResourceAsStream(GLOBALLOC_R);
                _propLoc.load(is);
                is.close();
                copeFromRezerv(_propLoc, GLOBALLOC);
              } catch (Exception e) {
                logger_job.log(Level.ERROR, e);
                logger_job.log(Level.ERROR, "Ошибка загрузки файла конфигураций :" + GLOBALLOC_R);
        }  
        }
        getProp();
    }
    public static void saveProp(String key, String value)
    {
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(GLOBALPRO), "Cp1251"));
            Enumeration enumProp = _propPro.keys();
            while(enumProp.hasMoreElements())
            {
                String k = (String)enumProp.nextElement();
                if(!k.equalsIgnoreCase(key))
                    pw.println(k+"="+_propPro.getProperty(k));
            }
            pw.println(key+"="+value);
            pw.close();
            getProp();
        }catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }        
    }
    public static String getMainProp(String key)
    {
        return _propPro.getProperty(key, null);
    }
    private static void copeFromRezerv(Properties _prop, String fn)
    {
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fn), "Cp1251"));
            Enumeration enumProp = _prop.keys();
            while(enumProp.hasMoreElements())
            {
                String k = (String)enumProp.nextElement();
                pw.println(k+"="+_prop.getProperty(k));
            }
            pw.close();
        }catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
        
    }
    public static String getJantarAppPath()
    {
           String path = System.getProperty("user.dir");
           int k = path.lastIndexOf("\\");
           return path.substring(0,k)+"/programm/";
    }
    public static String getAppPath()
    {
           return System.getProperty("user.dir");
    }
    private static void getProp()
    {
        
        try {
            InputStream is=null;
            _propPro = new Properties();
            try{
                is = new FileInputStream(GLOBALPRO);
                _propPro.load(is);
            }catch(IOException ee)
            {
                OutputStream uis = new FileOutputStream(GLOBALPRO);
                uis.close();
            }
            
            is.close();
            _propLoc = new Properties();
            try{
                is = new FileInputStream(GLOBALLOC);
                _propLoc.load(is);
            }catch(IOException ee)
            {
                /*OutputStream uis = new FileOutputStream(GLOBALLOC);
                uis.close();
                is = new FileInputStream(GLOBALLOC);*/
                error_GLOBALLOC=true;
            }
            
            is.close();
            
            _propLocDescript = new Properties();
            try{
                is = new FileInputStream(GLOBALLOCDESCRIPT);
                _propLocDescript.load(is);
            }catch(IOException ee)
            {
                /*OutputStream uis = new FileOutputStream(GLOBALLOCDESCRIPT);
                uis.close();
                is = new FileInputStream(GLOBALLOCDESCRIPT);*/
                error_GLOBALLOCDESCRIPT=true;
                
            }
            
            is.close();

            
            _propTitles = new Properties();
            //is = this.getClass().getResourceAsStream(GLOBALTITLES);
            try{
                is = new FileInputStream(GLOBALTITLES);
                _propTitles.load(is);
            }catch(IOException ee)
            {
/*                OutputStream uis = new FileOutputStream(GLOBALTITLES);
                uis.close();
                is = new FileInputStream(GLOBALTITLES);*/
                error_GLOBALTITLES=true;
                
            }
           
            is.close();
            
            } catch (Exception e) {
                logger_job.log(Level.ERROR, e);
                logger_job.log(Level.ERROR, "Ошибка загрузки файла конфигураций :" + getJantarAppPath());
        }
    }

    public static String getPathXlt() {
        return getAppPath()+"/xlt/";
    }
 
    public static String getTitle(String name)
    {
        String a = _propLoc.getProperty(name, null);
        try{
            return new String(a.getBytes("ISO-8859-1"), "CP1251");
        }catch(Exception e){return name;}
    }
    public static String getDescript(String name)
    {
        //return _propLocDescript.getProperty(name, null);
        String a = _propLocDescript.getProperty(name, null);
        try{
            return new String(a.getBytes("ISO-8859-1"), "CP1251");
        }catch(Exception e){return null;}
    }

    public static String getPropTitles(String name) {
        if(_propTitles == null)
            new LoadData();
        //return _propTitles.getProperty(name, null);
        String a = _propTitles.getProperty(name, null);
        try{
            return new String(a.getBytes("ISO-8859-1"), "CP1251");
        }catch(Exception e){return name;}
    }
    

    public static String getPathJantar12() {
        return getJantarAppPath();
    }


    public String getTextFile(String fn) {
        String result = "";
        File file = new File(getPathJantar12()+"Data/"+ fn);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() != 0) {
                String a = dis.readLine();
                result += new String(a.getBytes("ISO-8859-1"), "CP1251") + "\n";
            }

            fis.close();
            bis.close();
            dis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String getLocDescriptFileName()
    {
        return GLOBALLOCDESCRIPT;
    }
    public static String getLocFileName()
    {
        return GLOBALLOC;
    }
    public static String getTitlesFileName()
    {
        return GLOBALTITLES;
    }
     public static String getMainFileName()
    {
        return getJantarAppPath();
    }       
    public static String getFile(String fn)
    {
        StringBuffer buffer = new StringBuffer();
        File file = new File(fn);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            while (dis.available() != 0) {
                String a = dis.readLine();
                a = new String(a.getBytes("ISO-8859-1"), "CP1251")+"\n";
                buffer.append(a);

            }
            fis.close();
            bis.close();
            dis.close();
            return buffer.toString();
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static void setFile(String fn, String text)
    {
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fn), "Cp1251"));
            pw.println(text);
            pw.close();
            getProp();
        }catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
    }
}
