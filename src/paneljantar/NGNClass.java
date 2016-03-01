/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar;

import jantar12ui.ButtonTabComponent;
import jantar12ui.LoadData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author LebAlex
 */
public class NGNClass {
    public static final Logger logger_job = Logger.getLogger(NAMClass.class);
    private List<String> valList = new ArrayList<>();
    private String titleList;

    public NGNClass() {
    }
    
    private void getContextFile(String fn) {
        titleList="";
        valList.clear();
        File file = new File(fn);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            int count = 0;
            String title = LoadData.getPropTitles("NGN");
            title = new String(title.getBytes("ISO-8859-1"), "CP1251");

                if(title.trim().length()>0){
                    titleList=title.trim();
                    count++;
                }

            
            while (dis.available() != 0) {
                String a = dis.readLine();
                a = new String(a.getBytes("ISO-8859-1"), "CP1251");
                valList.add(a.trim());
                count++;

            }
            fis.close();
            bis.close();
            dis.close();
        } catch (FileNotFoundException e) {
            logger_job.log(Level.ERROR, e);
        } catch (IOException e) {logger_job.log(Level.ERROR, e);
        }
    }
    

    public List<String> getNGN(String fName)
    {
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        return valList;
    }
    public String getTitle()
    {
        return titleList;
    }
}
