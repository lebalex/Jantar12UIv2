/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar;

import helpers.ClearRowsActionAdapter;
import helpers.EditClass;
import helpers.NumberHelpers;
import helpers.VECTableRenderer;
import jantar12ui.ButtonTabComponent;
import jantar12ui.LoadData;
import java.awt.event.MouseListener;
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
public class ENXClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(ENXClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();
    Object[][] data;
    private String fileName;
    private String fileNameVEC;
    private String projectName;
    List<String> listNAM;
    List<String> listVEC;
  
    public ENXClass(JTabbedPane tab) {
        tabPane = tab;
    }
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        fileNameVEC = LoadData.getPathJantar12() + "Data/" + projectName+".VEC";
        getContextFile(fileName);
        int countIB = new SXMClass(null).getIB(projectName+".SXM");
        listNAM = new NAMClass(null).getNAM(projectName+".NAM");
        listVEC = new VECClass(null).getVEC(projectName+".VEC");
        
        jPanel1 = new javax.swing.JPanel();
        columnNames = new String[titleList.size()+4];
        columnNamesDescript = new String[titleList.size()+4];
        columnNames[0]="Beg";
        columnNames[1]="Name";
        columnNames[2]="End";
        columnNames[3]="Name";
        columnNamesDescript[0] = LoadData.getDescript("Beg");
        columnNamesDescript[1] = LoadData.getDescript("Name");
        columnNamesDescript[2] = LoadData.getDescript("End");
        columnNamesDescript[3] = LoadData.getDescript("Name");
        int i=4;
        for (String string : titleList)
        {
            columnNames[i]=string;
            columnNamesDescript[i]=LoadData.getDescript(string);
            i++;
        }
        data = new Object[countIB][valList.size()+4];
        i=0;
        for(int n=0; n<listVEC.size();n++)
        {
            if(((String)listVEC.get(n)).trim().length()>0){
            int j=0;
            for (String str2 : listVEC.get(n).split("[ ]"))
            {
                try{
                    data[n][j]=str2;
                    int namIdx=0;
                        try{namIdx=Integer.parseInt(str2.trim());}catch(Exception e){}
                        if(namIdx>0 && namIdx<=listNAM.size())
                            data[n][j+1]=listNAM.get(namIdx-1);
                        else
                            data[n][j+1]="ошибка";
                }catch(Exception t){}
                j++;j++;
            }
        }
        }
        
        
        
        i=0;
        for (String string : valList)
        {
            if(i<countIB){
            int j=4;
            String[] strSplit = string.trim().split("[ ]");
            if(strSplit.length>1)
            {
                for (String str2 : strSplit) {
                    if(str2.trim().length()>0){
                        try{
                            data[i][j]=NumberHelpers.addNol(str2);
                        }catch(Exception t){}
                        j++;
                    }
                }
            i++;
            }
            }
        }
        jScrollPane1 = new javax.swing.JScrollPane();
        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        jTable1 = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                if (colIndex == 1  || colIndex == 3)
                    return false;
                else
                    return true;
            }
        };
        jTable1.setDefaultRenderer(Object.class, new VECTableRenderer(3));
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
    public List<String> getValues(String fName)
    {
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        return valList;
    }
    @Override
    public void saveValue() {
        try {
            PrintWriter pwVEC = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileNameVEC), "Cp1251"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"));
            for(int i=0; i<jTable1.getModel().getRowCount();i++)
            {
                pwVEC.println(jTable1.getModel().getValueAt(i,0)+" "+jTable1.getModel().getValueAt(i,2));
                for(int j=4; j<jTable1.getModel().getColumnCount();j++)
                {
                    if(jTable1.getModel().getValueAt(i,j)!=null)
                    {
                        if(j==4)
                            pw.print(jTable1.getModel().getValueAt(i,j));
                        else
                            pw.print(" "+jTable1.getModel().getValueAt(i,j));
                    }
                }
                pw.println("");
            }
            pw.close();
            pwVEC.close();
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }

}
