/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar.interval;

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
import paneljantar.PanelInterface;
import paneljantar.SXMClass;

/**
 *
 * @author ivc_LebedevAV
 */
public class LINClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(LINClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();
    Object[][] data;
    private List<JTable> jTableList;
    private List<ExcelAdapter> excelAdapterList;
    private String fileName;
    private String projectName;
    private JList rowHeader;
    private int countIBIdx=0;
    private JScrollPane jScrollPane1;
    private List<JScrollPane> jScrollPane1List;
    
    public LINClass(JTabbedPane tab) {
        tabPane = tab;
    }
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        int countIB = new SXMClass(null).getIB(projectName.substring(projectName.indexOf("/")+1, projectName.length()) +".SXM");
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane = new JTabbedPane();
        jTableList = new ArrayList<JTable>();
        excelAdapterList = new ArrayList<ExcelAdapter>();
        jScrollPane1List = new ArrayList<JScrollPane>();
        
        /**/
        for(int idx=0; idx<countIB; idx++){
        countIBIdx =  new IZLClass().getValueIndex(projectName+".IZL",idx);
        int countIBIdxBegin=0;
        if(idx>0)
        {
            for(int k=0; k<idx;k++)
            countIBIdxBegin +=  new IZLClass().getValueIndex(projectName+".IZL",k);
        }
        columnNames = new String[5+1];
        columnNamesDescript = new String[5+1];
        columnNames[0]="IZ";
        columnNamesDescript[0]=LoadData.getDescript(columnNames[0]);
        int i=1;
        for (String string : titleList)
        {
                columnNames[i]=string;
                columnNamesDescript[i]=LoadData.getDescript(string);
                i++;
        }
        data = new Object[50][5+1];
        int j=0;
        int ri=0;
        for (String string : valList)
        {
            if(j>=countIBIdxBegin){
                if(j<countIBIdx+countIBIdxBegin){
                    data[ri][0]=ri+1;
                    i=1;
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
                jTable1 = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                if (colIndex != 0)
                    return true;
                else
                    return false;
            }
        };
        jTable1.setDefaultRenderer(Object.class, new VECTableRenderer(7));
        createMenu(this);

        jTable1.getModel().addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
          if(e.getColumn()!=0){
            int ii = jTabbedPane.getSelectedIndex();
                JTable jT = (JTable)jTableList.get(ii);
                for(int i=0; i< jT.getRowCount();i++)
                    if (jT.getValueAt(i, 1)!=null && jT.getValueAt(i, 1).toString().trim().length()>0)
                    {
                        jT.setValueAt(i+1, i, 0);
                    }else
                        jT.setValueAt("", i, 0);
          }
        updateTitle(true);
      }
    });
    jTableList.add(jTable1);
    excelAdapterList.add(myAd);
        
        
jScrollPane1.setViewportView(jTable1);
      
        
        jTabbedPane.add(""+(idx+1),jScrollPane1);//Связь 
        jScrollPane1List.add(jScrollPane1);
        /**/
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
            String a = LoadData.getPropTitles("LIN");
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
    @Override
    public void saveValue() {
        String dataIZL="";
        int countGroupAg=0;
        try {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"))) {
            for(int tabIdx=0; tabIdx<jTableList.size(); tabIdx++){
                JTable jT = jTableList.get(tabIdx);
                for(int j=0; j<jT.getModel().getRowCount();j++){
                    if(jT.getModel().getValueAt(j,1)!=null && jT.getModel().getValueAt(j,1).toString().trim().length()>0){
                        countGroupAg++;
                    for(int i=1; i<jT.getModel().getColumnCount();i++){
                        if(i==1)
                            pw.print(jT.getModel().getValueAt(j,i));
                        else
                            pw.print(" "+jT.getModel().getValueAt(j,i));
                    }
                    pw.println("");
                }
                }
                dataIZL+=countGroupAg+" ";
                countGroupAg=0;
            }
            }
            new IZLClass().saveDataFromAGR(projectName+".IZL",dataIZL.substring(0, dataIZL.length()-1));
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }
}

