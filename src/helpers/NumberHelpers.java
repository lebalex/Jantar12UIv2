/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *fgdfg
 * @author LebAlex
 * test svn
 */
public class NumberHelpers {
    public static final Logger logger_job = Logger.getLogger(NumberHelpers.class);
    
    public static List<String> getNumbers(String source, int count)
    {
       count++;
       List<String> results = new ArrayList();
       while(source.trim().length()>0)
        {
            int pos = source.indexOf(".");
            if(pos>=0)
            {
                String res = source.substring(0,pos+count);
                if((res.substring(0,1)).equals("."))
                    res = "0"+res;
                results.add(res);
                if(source.length()>(count+1))
                    source = source.substring(pos+count, source.length());
                else source=" ";
            }
        }
        return results;
        
    }
    public static String addNol(String a)
    {
        if(a.length()>0){
        if((a.substring(0,1)).equals("."))
                    a = "0"+a;
        if((a.substring(0,1)).equals("+"))
            a=a.substring(1,a.length());
        if((a.substring(a.length()-1,a.length())).equals("."))
            a = a+"0";
        }
        return a;
    }
    public static List<String> getNumbersInt(String source, int count)
    {
       List<String> results = new ArrayList();
       while(source.trim().length()>0)
        {
            String res = source.substring(0,count);
            source = source.substring(count, source.length());
            results.add(res);
        }
        return results;
    }
    public static boolean isInteger(String val)
    {
        try{
            int i = Integer.parseInt(val.trim());
            return true;
        }catch(Exception er){return false;}
    }
    public static boolean isFloat(String val)
    {
        try{
            float f = Float.parseFloat(val.trim());
            return true;
        }catch(Exception er){return false;}
    }
    public static int getInt(Object a)
    {
        try{
            return Integer.parseInt(a.toString());
        }catch(Exception e)
        {
            logger_job.log(Level.ERROR, e);
            return 0;
        }
    }
    public static float getFloat(Object a)
    {
        try{
            return Float.parseFloat(a.toString());
        }catch(Exception e)
        {
            logger_job.log(Level.ERROR, e);
            return 0;
        }
    }    
    public static double getDouble(Object a)
    {
        try{
            return Double.parseDouble(a.toString());
        }catch(Exception e)
        {
            logger_job.log(Level.ERROR, e);
            return 0;
        }
    }    
}
