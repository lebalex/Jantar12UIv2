/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar;

import helpers.*;
import jantar12ui.ButtonTabComponent;
import jantar12ui.LoadData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author LebAlex
 */
public class YZLClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(YZLClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();
    Object[][] data;
    private String fileName;
    private String projectName;
    List<String> listNAM;
    
    
    public YZLClass(JTabbedPane tab) {
        tabPane = tab;
    }
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        int countM = new SXMClass(null).getM(projectName+".SXM");
        jPanel1 = new javax.swing.JPanel();
        columnNames = new String[titleList.size()+1];
        columnNamesDescript = new String[titleList.size()+1];
        columnNames[0]="Name";
        columnNamesDescript[0]=LoadData.getDescript(columnNames[0]);
        int i=1;
        for (String string : titleList)
        {
            columnNames[i]=string;
            columnNamesDescript[i]=LoadData.getDescript(string);
            i++;
        }
        listNAM = new NAMClass(null).getNAM(projectName+".NAM");
        data = new Object[countM+1][titleList.size()+1];
        i=0;
        for (String string : valList)
        {
            if(i<(countM)){
            int j=1;
            String[] strSplit = string.trim().split("[ ]");
            if(strSplit.length>1)
            {
                for (String str2 : strSplit) {
                    if(str2.trim().length()>0){
                        if(j==1){
                            int namIdx=i;
                            //try{namIdx=Integer.parseInt(str2.trim());}catch(Exception e){}
                            if(namIdx>=0 && namIdx<listNAM.size())
                                data[i][0]=listNAM.get(namIdx);
                            else
                                data[i][0]="ошибка";
                        }
                        data[i][j]=NumberHelpers.addNol(str2);
                        j++;
                    }
                }
            i++;
            }
            }else
            {
                data[i][4]=NumberHelpers.addNol(string.trim());
            }
        }
        jScrollPane1 = new javax.swing.JScrollPane();
        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        jTable1 = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                if (colIndex == 0 || (colIndex!=4 && rowIndex==jTable1.getRowCount()-1))
                    return false;
                else
                    return true;
            }
        };
        jTable1.setDefaultRenderer(Object.class, new VECTableRenderer(2));
        createMenu(this);
        jTable1.getModel().addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
          int row = e.getFirstRow();
          int column = e.getColumn();
          if(row<jTable1.getRowCount()-1){
          if(column>0 && jTable1.getValueAt(row, 0)==null && row<listNAM.size()){
            if(row<listNAM.size() && row>=0)
                jTable1.setValueAt(listNAM.get(row), row, 0);
            else
                jTable1.setValueAt("ошибка", row, 0);
          }
          }

          
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
        fileName=fn;
        File file = new File(fn);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            int count = 0;
            String a = LoadData.getPropTitles(getSuff(this.getClass().getSimpleName()));
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
            //for(int i=count;i<countY;i++)
                //valList.add("");
            fis.close();
            bis.close();
            dis.close();
        } catch (FileNotFoundException e) {
            logger_job.log(Level.ERROR, e);
        } catch (IOException e) {logger_job.log(Level.ERROR, e);
        }
    }
    public List<String> getValues(String fName)
    {
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        return valList;
    }
    public void saveValue() {
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"));
            for(int i=0; i<jTable1.getModel().getRowCount()-1;i++)
            {
                for(int j=1; j<jTable1.getModel().getColumnCount();j++)
                {
                    if(jTable1.getModel().getValueAt(i,j)!=null)
                    {
                        if(j==1)
                            pw.print(jTable1.getModel().getValueAt(i,j));
                        else
                            pw.print(" "+jTable1.getModel().getValueAt(i,j));
                    }
                }
                pw.println("");
            }
                for(int j=0; j<jTable1.getModel().getColumnCount();j++)
                {
                    if(jTable1.getModel().getValueAt(jTable1.getModel().getRowCount()-1,j)!=null)
                    {
                            pw.print(jTable1.getModel().getValueAt(jTable1.getModel().getRowCount()-1,j));
                    }
                }
            pw.close();
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }
    public void saveFromDelYZL(List<Integer> idM, String fn)
    {
        getContextFile(LoadData.getPathJantar12() + "Data/" + fn);
        for(int i:idM)
        {
            valList.set(i, "");
        }
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"));
            for(String string: valList)
            {
                if(string.trim().length()>0)
                pw.println(string);
            }
            pw.close();

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
    }
    public void saveFromInsYZL(List<Integer> idM, String fn)
    {
        getContextFile(LoadData.getPathJantar12() + "Data/" + fn);
        for(int i:idM)
        {
            valList.add(i, "0 0 0 0 0 0 0");
        }
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"));
            for(String string: valList)
            {
                if(string.trim().length()>0)
                pw.println(string);
            }
            pw.close();

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
    }
}
