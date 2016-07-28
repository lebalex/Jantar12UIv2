/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar.interval;

import helpers.EditClass;
import helpers.ExcelAdapter;
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
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import paneljantar.PanelInterface;
import paneljantar.SXMClass;
import paneljantar.VECClass;

/**
 *
 * @author LebAlex
 */
public class HNGClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(HNGClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();
    Object[][] data;
    private String fileName;
    private String projectName;
    private List<JTable> jTableList;
    private List<ExcelAdapter> excelAdapterList;
    //private JList rowHeader;
    //private int countNRRIdx=0;
    private JScrollPane jScrollPane1;
    private List<JScrollPane> jScrollPane1List;
    //private int countVEC;
    private int countIA;
    public HNGClass(JTabbedPane tab) {
        tabPane = tab;
    }
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        int countM = new SXMClass(null).getM(projectName.substring(projectName.indexOf("/")+1, projectName.length()) +".SXM");
        countIA = new SXMClass(null).getIA(projectName.substring(projectName.indexOf("/")+1, projectName.length()) +".SXM");
        //countVEC = ((List<String>)(new VECClass(null).getVEC(projectName.substring(projectName.indexOf("/")+1, projectName.length()) +".VEC"))).size();
        int countID = new IDDClass(null).getID(projectName+".IDD");
        
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane = new JTabbedPane();
        jTableList = new ArrayList<JTable>();
        excelAdapterList = new ArrayList<ExcelAdapter>();
        jScrollPane1List = new ArrayList<JScrollPane>();
        
/**/
        for(int idx=0; idx<countM; idx++){
        int countNRRIdxBegin=0;
        if(idx>0)
        {
            for(int k=0; k<idx;k++)
            countNRRIdxBegin +=  countID;
        }
        columnNames = new String[2];
        columnNamesDescript = new String[2];
        int i=0;
        for (String string : titleList)
        {
                columnNames[i]=string;
                columnNamesDescript[i]=LoadData.getDescript(string);
                i++;
        }
        data = new Object[countID][2];
        int j=0;
        int ri=0;
        for (String string : valList)
        {
            if(j>=countNRRIdxBegin){
                if(j<countID+countNRRIdxBegin){
                    i=0;
                    for (String str : string.split("[ ]")){
                        if(str.trim().length()>0)
                            data[ri][i]=NumberHelpers.addNol(str.trim());
                        i++;
                    }
                    ri++;
                }
            }
            j++;
            
        }
        jScrollPane1 = new javax.swing.JScrollPane();
        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        jTable1 = new JTable(model);
        jTable1.setDefaultRenderer(Object.class, new VECTableRenderer(5));
        createMenu(this);

        jTable1.getModel().addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
          int row = e.getFirstRow();
          int column = e.getColumn();
          if(column==0){
            JTable jT = jTableList.get(jTabbedPane.getSelectedIndex());
            String val = (String)jT.getValueAt(row, column);
            int numVec=0;
            try{numVec=Integer.parseInt(val.trim());}
            catch(Exception er){}
            if(numVec>(countIA+1))
                jT.setValueAt("ошибка", row, column);
          }
          
        updateTitle(true);
      }
    });
    jTableList.add(jTable1);
    excelAdapterList.add(myAd);
        
        
jScrollPane1.setViewportView(jTable1);
        
        jTabbedPane.add("Узел "+(idx+1),jScrollPane1);
        jScrollPane1List.add(jScrollPane1);

        }        
        
        
        jLabel = new JLabel(LoadData.getTitle(getSuff(this.getClass().getSimpleName())));
        setArrangeTab(projectName, getSuff(this.getClass().getSimpleName()));
        
        return jPanel1;
    }

    @Override
    public JTable getTable()
    {
        return jTableList.get(jTabbedPane.getSelectedIndex());
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
            String a = LoadData.getPropTitles("HNG");
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
            for(int tabIdx=0; tabIdx<jTableList.size(); tabIdx++){
                JTable jT = jTableList.get(tabIdx);
                for(int j=0; j<jT.getModel().getRowCount();j++){
                    if(jT.getModel().getValueAt(j,0)!=null && jT.getModel().getValueAt(j,0).toString().trim().length()>0){
                    for(int i=0; i<jT.getModel().getColumnCount();i++){
                        if(i==0)
                            pw.print(jT.getModel().getValueAt(j,i));
                        else
                            pw.print(" "+jT.getModel().getValueAt(j,i));
                    }
                    pw.println("");
                }
                }
            }
            }
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }

}
