/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar;

import helpers.EditClass;
import helpers.NumberHelpers;
import helpers.VECTableRenderer;
import jantar12ui.Jantar12UI;
import jantar12ui.LoadData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import paneljantar.interval.NCRClass;
import paneljantar.interval.NRRClass;

/**
 *
 * @author LebAlex
 */
public class SXMClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(SXMClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();
    private Jantar12UI jantar12UI;
    private int ks;
    private String projectName;
    
    Object[][] data;
    private String fileName;
    public SXMClass(JTabbedPane tab) {
        tabPane = tab;
    }
    @Override
    public void setJantar12UI(Jantar12UI jantar12UI)
    {
        this.jantar12UI = jantar12UI;
    }
    @Override
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        jPanel1 = new javax.swing.JPanel();
        columnNames = new String[titleList.size()];
        columnNamesDescript = new String[titleList.size()];
        int i=0;
        for (String string : titleList)
        {
            columnNames[i]=string;
            columnNamesDescript[i]=LoadData.getDescript(string);
            i++;
        }
        data = new Object[1][valList.size()];
        i=0;
        for (String string : valList)
        {
            data[0][i]=NumberHelpers.addNol(string);
            if(i==2)
                ks=NumberHelpers.getInt(string.trim());
            i++;
        }
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new JTable(data, columnNames);
        jTable1.setDefaultRenderer(Object.class, new VECTableRenderer(5));
        setDescript();

        jTable1.getModel().addTableModelListener(new TableModelListener() {

            @Override
      public void tableChanged(TableModelEvent e) {
        updateTitle(true);
      }
    });
        
        jLabel = new JLabel(LoadData.getTitle(getSuff(this.getClass().getSimpleName())));
        jScrollPane1.setViewportView(jTable1);
        setArrange(projectName, getSuff(this.getClass().getSimpleName()));


        
        return jPanel1;
    }

    public int getM(String fName)
    {
        return (Integer)getValue(fName, 0);
    }
    public int getIB(String fName)
    {
        return (Integer)getValue(fName, 1);
    }
    public int getKS(String fName)
    {
        return (Integer)getValue(fName, 2);
    }
    public int getIA(String fName)
    {
        return (Integer)getValue(fName, 3);
    }
    public int getKSR(String fName)
    {
        return (Integer)getValue(fName, 4);
    }
    public float getPRK(String fName)
    {
        return (Float)getValue(fName, 5);
    }
    public float getKRIT(String fName)
    {
        return (Float)getValue(fName, 6);
    }
    public float getERN(String fName)
    {
        return (Float)getValue(fName, 7);
    }
    
    private Object getValue(String fName, int i)
    {
        Object result=null;
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        String str = (String)valList.get(i);
        try{
            if(i>=5)
                result = Float.parseFloat(str);
            else
                result = Integer.parseInt(str);
        }catch(Exception e){}
        return result;
        
    }

    private void getContextFile(String fn) {
        titleList.clear();
        valList.clear();
        fileName = fn;
        File file = new File(fn);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            
            String title = LoadData.getPropTitles(getSuff(this.getClass().getSimpleName()));
            title = new String(title.getBytes("ISO-8859-1"), "CP1251");
            int count=0;
            for (String string : title.split(",")) {
                if(string.trim().length()>0 && count<9){
                    titleList.add(string.trim());
                    count++;
                }
            }
         while (dis.available() != 0) {   
            String val = dis.readLine();
            
            if(val!=null)
            for (String string : val.split("[ ]")) {
                if(string.trim().length()>0){
                    valList.add(string.trim());
                    count--;
                }
            }
            
        }
            for(int i=0;i<count;i++)
                valList.add("");

            fis.close();
            bis.close();
            dis.close();
        } catch (FileNotFoundException e) {
            logger_job.log(Level.ERROR, e);
        } catch (IOException e) {logger_job.log(Level.ERROR, e);
        }
    }
    public void saveM(int countM)
    {
        valList.set(0, Integer.toString(countM));
        try {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"))) {
                for(int i=0; i<valList.size();i++){
                    if(valList.get(i).toString().trim().length()>0)
                        if(i==0)
                            pw.print(valList.get(i));
                        else
                            pw.print(" "+valList.get(i));
                }
            }
        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
    }
    @Override
    public void saveValue() {
        try {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"))) {
                for(int i=0; i<jTable1.getModel().getColumnCount();i++)
                    if(i==0)
                        pw.print(jTable1.getModel().getValueAt(0, i));
                    else
                        pw.print(" "+jTable1.getModel().getValueAt(0, i));
            }
            /*если кол-во интервалов поменяли, перезагрузим TreeNode*/
            if(NumberHelpers.getInt(jTable1.getModel().getValueAt(0, 2))!=ks)
                jantar12UI.refreshTree();
            /*сохранить по умолчанию файлы в интервалах NCR и NRR*/
            
            if(NumberHelpers.getInt(jTable1.getModel().getValueAt(0, 0))!=0)
            {
                for(int intrv=1;intrv<=NumberHelpers.getInt(jTable1.getModel().getValueAt(0, 2));intrv++)
                {
                    String intS = Integer.toString(intrv);
                    if(intrv<10)
                        intS="0"+intrv;
                    String fN = "Interv"+intS+"/"+projectName+".NRR";
                    NRRClass nRRClass = new NRRClass();
                    int countM = NumberHelpers.getInt(jTable1.getModel().getValueAt(0, 0));
                    if(nRRClass.getNRRAll(fN)==0)
                    {
                        String dataTemp="";
                        for(int i=0;i<countM;i++)
                            dataTemp=dataTemp+"0 ";
                        nRRClass.saveDataFromAZR(fN,dataTemp);
                    }else
                    {
                        String dataTemp="";
                        for(int i=0;i<countM;i++)
                            dataTemp=dataTemp+nRRClass.getNRR(fN, i+1)+" ";
                        
                        nRRClass.saveDataFromAZR(fN,dataTemp);
                        
                    }
                   
                }
            }
            if(NumberHelpers.getInt(jTable1.getModel().getValueAt(0, 1))!=0)
            {
                for(int intrv=1;intrv<=NumberHelpers.getInt(jTable1.getModel().getValueAt(0, 2));intrv++)
                {
                    String intS = Integer.toString(intrv);
                    if(intrv<10)
                        intS="0"+intrv;
                    String fN = "Interv"+intS+"/"+projectName+".NCR";
                    NCRClass nCRClass = new NCRClass();
                    int countIB = NumberHelpers.getInt(jTable1.getModel().getValueAt(0, 1));
                    if(nCRClass.getNCRAll(fN)==0)
                    {
                        String dataTemp="";
                        for(int i=0;i<countIB;i++)
                            dataTemp=dataTemp+"0 ";
                        nCRClass.saveDataFromKCR(fN,dataTemp);
                    }else
                    {
                        String dataTemp="";
                        for(int i=0;i<countIB;i++)
                            dataTemp=dataTemp+nCRClass.getNCR(fN, i+1)+" ";
                        
                        nCRClass.saveDataFromKCR(fN,dataTemp);
                        
                    }
                   
                }
            }
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }

}
