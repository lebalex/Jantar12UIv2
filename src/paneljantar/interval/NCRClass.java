/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar.interval;

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
import paneljantar.PanelInterface;
import paneljantar.SXMClass;

/**
 *
 * @author ivc_LebedevAV
 */
public class NCRClass /*implements PanelInterface*/ {
    public static final Logger logger_job = Logger.getLogger(NCRClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();
    String[] columnNames;
    Object[][] data;

    private String fileName;

    public NCRClass() {
    }
    /*public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        int countIB = new SXMClass(null).getIB(projectName.substring(projectName.indexOf("/")+1, projectName.length()) +".SXM");
        JPanel jPanel1 = new javax.swing.JPanel();
        columnNames = new String[countIB];
        for(int i=0;i<countIB;i++)
            columnNames[i]="";
        int i=0;
        for (String string : titleList)
        {
            if(i<countIB){
                columnNames[i]=string;
                i++;
            }
        }
        data = new Object[1][countIB];
        i=0;
        for (String string : valList)
        {
            for (String str : string.split("[ ]"))
            if(i<countIB){
                if(str.trim().length()>0){
                    data[0][i]=str.trim();
                }
            i++;
            }
        }
        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new JTable(data, columnNames);
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                updateTitle(true);
            }
        });
        jTable1.getModel().addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
        updateTitle(true);
      }
    });
        
        
jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        
        return jPanel1;
    }*/

    private void getContextFile(String fn) {
        titleList.clear();
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
            String a = LoadData.getPropTitles("NCR");
            a = new String(a.getBytes("ISO-8859-1"), "CP1251");
            for (String string : a.split(",")) {
                if(string.trim().length()>0){
                    titleList.add(string.trim());
                }
            }
            while (dis.available() != 0) {
                a = dis.readLine();
                a = new String(a.getBytes("ISO-8859-1"), "CP1251");
                    valList.add(a);
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
    /*@Override
    public void saveValue() {
        try {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"))) {
            for (String string : titleList)
                pw.print(string+" ");
            pw.println();
                for(int i=0; i<jTable1.getModel().getColumnCount();i++){
                    if(i==0)
                        pw.print(jTable1.getModel().getValueAt(0,i));
                    else
                        pw.print(" "+jTable1.getModel().getValueAt(0,i));
                }
            }
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }
    private void updateTitle(boolean bold)
    {
        if(tabPane.getTabComponentAt(tabPane.getSelectedIndex()) instanceof ButtonTabComponent)
        {
            ((ButtonTabComponent)tabPane.getTabComponentAt(tabPane.getSelectedIndex())).updateLabel(bold);
        }
    }*/
    public int getNCRAll(String fName)
    {
        return getValue(fName);
    }
    private int getValue(String fName)
    {
        int result=0;
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        for (String string : valList)
        {
            for (String str : string.split("[ ]"))
               if(str.trim().length()>0){
                   try{
                    result+=Integer.parseInt(str.trim());
                   }catch(Exception e){} 
                }
        }
        return result;
    }
    public int getNCR(String fName, int i)
    {
        return getValue(fName, i);
    }
    private int getValue(String fName, int i)
    {
        int result=0;
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        for (String string : valList)
        {
            int j=0;
            for (String str : string.split("[ ]")){
               if(str.trim().length()>0){
                   try{
                       if(j==(i-1))
                        result=Integer.parseInt(str.trim());
                   }catch(Exception e){} 
                }
               j++;
            }
        }
        return result;
        
    }
    public int getMaxNCR(String fName)
    {
        return getValue2(fName);
    }
    private int getValue2(String fName)
    {
        int result=0;
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        for (String string : valList)
        {
            for (String str : string.split("[ ]")){
               if(str.trim().length()>0){
                   try{
                       if(result<Integer.parseInt(str.trim()))
                        result=Integer.parseInt(str.trim());
                   }catch(Exception e){} 
                }
            }
        }
        return result;
     }
    public int getCountNCR(String fName)
    {
        return getValue3(fName);
    }
    private int getValue3(String fName)
    {
        int result=0;
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        for (String string : valList)
        {
            int j=0;
            for (String str : string.split("[ ]")){
               if(str.trim().length()>0){
                   try{
                       if(Integer.parseInt(str.trim())!=0)
                        result++;
                   }catch(Exception e){} 
                }
            }
        }
        return result;
        
    }
    
    public void saveDataFromKCR(String fName, String data)
    {
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        try {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"))) {
            pw.println(data);
            }

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
    }
}