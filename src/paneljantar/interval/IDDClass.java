/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar.interval;

import helpers.EditClass;
import helpers.NumberHelpers;
import helpers.VECTableRenderer;
import jantar12ui.ButtonTabComponent;
import jantar12ui.LoadData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import paneljantar.ENXClass;
import paneljantar.PanelInterface;
import paneljantar.SXMClass;

/**
 *
 * @author LebAlex
 */
public class IDDClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(IDDClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();
    Object[][] data;
    private String fileName;
    private String projectName;
    public IDDClass(JTabbedPane tab) {
        tabPane = tab;
    }
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        jPanel1 = new javax.swing.JPanel();
        int i=0;
        columnNames = new String[titleList.size()];
        columnNamesDescript = new String[titleList.size()];
        for (String string : titleList)
        {
            columnNames[i]=string;
            columnNamesDescript[i]=LoadData.getDescript(string);
            i++;
        }
        data = new Object[1][1];
        i=0;
        for (String string : valList)
        {
            if(string.trim().length()>0){
              data[0][0]=NumberHelpers.addNol(string);
            }
        }
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new JTable(data, columnNames);
        jTable1.setDefaultRenderer(Object.class, new VECTableRenderer(5));

        setDescript();
        jTable1.getModel().addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
        updateTitle(true);
      }
    });
        
        
        jLabel = new JLabel(LoadData.getTitle(getSuff(this.getClass().getSimpleName())));
        jScrollPane1.setViewportView(jTable1);
        setArrange(projectName, getSuff(this.getClass().getSimpleName()));

        
        return jPanel1;
    }

    private void getContextFile(String fn) {
        titleList.clear();
        valList.clear();
        File file = new File(fn);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            try{
                fis = new FileInputStream(file);
            }catch (FileNotFoundException e) {
                logger_job.log(Level.ERROR, e);
                saveValue();
                JOptionPane.showMessageDialog(null, "Файл создан, необходимо его заполнить перед открытием других!","Предупреждение", JOptionPane.WARNING_MESSAGE);
            }
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            int count = 0;
            String a = LoadData.getPropTitles("IDD");
            a = new String(a.getBytes("ISO-8859-1"), "CP1251");
            for (String string : a.split(",")) {
                if(string.trim().length()>0){
                    titleList.add(string.trim());
                }
            }
            while (dis.available() != 0) {
                a = dis.readLine();
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
    public void saveValue() {
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"));
            pw.println(jTable1.getModel().getValueAt(0,0));
            pw.close();
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }
    public int getID(String fName)
    {
        return getValue(fName);
    }
    private int getValue(String fName)
    {
        int result=0;
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        String str = (String)valList.get(0);
        try{
            result = Integer.parseInt(str);
        }catch(Exception e){}
        return result;
        
    }
}
