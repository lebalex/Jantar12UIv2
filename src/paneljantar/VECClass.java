/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar;

import helpers.EditClass;
import helpers.VECTableRenderer;
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
public class VECClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(VECClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();
    Object[][] data;
    private String fileName;
    private String projectName;
    List<String> listNAM;
            
    public VECClass(JTabbedPane tab) {
        tabPane = tab;
    }
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        int countIB = new SXMClass(null).getIB(projectName+".SXM");
        jPanel1 = new javax.swing.JPanel();
        columnNames = new String[4];
        columnNamesDescript = new String[4];
        columnNames[0] = titleList.get(0);
        columnNames[2] = titleList.get(1);
        columnNames[1] = "";
        columnNames[3] = "";
 
        columnNamesDescript[0] = LoadData.getDescript(columnNames[0]);
        columnNamesDescript[2] = LoadData.getDescript(columnNames[2]);
        columnNamesDescript[1] = LoadData.getDescript(columnNames[1]);
        columnNamesDescript[3] = LoadData.getDescript(columnNames[3]);
 
        
        listNAM = new NAMClass(null).getNAM(projectName+".NAM");
        
        data = new Object[countIB][4];
        int i=0;
        for (String string : valList)
        {
            if(i<countIB){
                int j=0;
            for (String str2 : string.split("[ ]")) {
                if(str2.trim().length()>0){
                    data[i][j]=str2.trim();
                    j++;
                    int namIdx=0;
                    try{namIdx=Integer.parseInt(str2.trim());}catch(Exception e){}
                    if(namIdx>0 && namIdx<=listNAM.size())
                        data[i][j]=listNAM.get(namIdx-1);
                    else
                        data[i][j]="ошибка";
                    j++;
                }
            }
            i++;
            }
        }
        jScrollPane1 = new javax.swing.JScrollPane();

        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        jTable1 = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                if (colIndex == 0 || colIndex == 2)
                    return true;
                else
                    return false;
            }
        };
        jTable1.setDefaultRenderer(Object.class, new VECTableRenderer(1));
        createMenu(this);
        
        jTable1.getModel().addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
          int row = e.getFirstRow();
          int column = e.getColumn();
          if(column==0 || column==2){
            String val = (String)jTable1.getValueAt(row, column);
            int namIdx=1;
            try{
                namIdx=Integer.parseInt(val.trim());
                if(namIdx>0 && namIdx<=listNAM.size())
                    jTable1.setValueAt(listNAM.get(namIdx-1), row, column+1);
                else
                    jTable1.setValueAt("ошибка", row, column+1);
            }catch(Exception er){jTable1.setValueAt("ошибка", row, column+1);}
            
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
    @Override
    public void saveValue() {
        try {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"))) {
                for(int i=0; i<jTable1.getModel().getRowCount();i++){
                        pw.println(jTable1.getModel().getValueAt(i,0)+" "+jTable1.getModel().getValueAt(i,2));
                }
            }
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }
    public List<String> getVEC(String fName)
    {
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        return valList;
    }
    
}
