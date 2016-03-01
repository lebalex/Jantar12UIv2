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
public class VYZClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(VYZClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();
    Object[][] data;
    private List<JTable> jTableList;
    private List<ExcelAdapter> excelAdapterList;
    private String fileName;
    private int countKRRIdx=0;
    private String projectName;
    private JList rowHeader;
    private int countNRRIdx=0;
    private JScrollPane jScrollPane1;
    private List<JScrollPane> jScrollPane1List;
    private KRRClass kRRClass;
    
    public VYZClass(JTabbedPane tab) {
        tabPane = tab;
    }
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        int countM = new SXMClass(null).getM(projectName.substring(projectName.indexOf("/")+1, projectName.length()) +".SXM");
        
        jPanel1 = new javax.swing.JPanel();
        
        
         jTabbedPane = new JTabbedPane();
        jTableList = new ArrayList<JTable>();
        excelAdapterList = new ArrayList<ExcelAdapter>();
        jScrollPane1List = new ArrayList<JScrollPane>();
        
        /**/
        kRRClass = new KRRClass();
        int idxKRR=0;
        for(int idx=0; idx<countM; idx++){
        countNRRIdx =  new NRRClass().getNRR(projectName+".NRR",idx+1);
        if(countNRRIdx>0){
            countKRRIdx = new KRRClass().getMaxKRR(projectName+".KRR",idxKRR);
            idxKRR++;
        }
        int countNRRIdxBegin=0;
        if(idx>0)
        {
            for(int k=0; k<idx;k++)
            countNRRIdxBegin +=  new NRRClass().getNRR(projectName+".NRR",k+1);
        }
        if(countKRRIdx==0)countKRRIdx=1;
        columnNames = new String[countKRRIdx+1];
        columnNamesDescript = new String[countKRRIdx+1];
        for(int i=0;i<countKRRIdx+1;i++)
            columnNames[i]="";
        columnNames[0]="NRR";
        columnNamesDescript[0]=LoadData.getDescript(columnNames[0]);
        int i=1;
        for (String string : titleList)
        {
                columnNames[i]=string;
                columnNamesDescript[i]=LoadData.getDescript(string);
                i++;
        }
        
        
        data = new Object[countNRRIdx][countKRRIdx+1];

        int j=0;
        int ri=0;
        for (String string : valList)
        {
            if(j>=countNRRIdxBegin){
                if(j<countNRRIdx+countNRRIdxBegin){
                    i=1;
                    data[ri][0]=ri+1;
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
        updateTitle(true);
      }
    });
    jTableList.add(jTable1);
    excelAdapterList.add(myAd);
        
        
jScrollPane1.setViewportView(jTable1);

        
        jTabbedPane.add("Узел "+(idx+1),jScrollPane1);
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
            String a = LoadData.getPropTitles("VYZ");
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
        try {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"))) {
            for(int tabIdx=0; tabIdx<jTableList.size(); tabIdx++){
                JTable jT = jTableList.get(tabIdx);
                for(int j=0; j<jT.getModel().getRowCount();j++){
                    for(int i=1; i<jT.getModel().getColumnCount();i++){
                        if(jT.getModel().getValueAt(j,i)!=null && jT.getModel().getValueAt(j,i).toString().trim().length()>0){
                            if(i==1)
                                pw.print(jT.getModel().getValueAt(j,i));
                            else
                                pw.print(" "+jT.getModel().getValueAt(j,i));
                        }
                    }
                    pw.println("");

                }
            }
            }
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
    }

}


