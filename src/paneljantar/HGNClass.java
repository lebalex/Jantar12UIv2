/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar;

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
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author LebAlex
 */
public class HGNClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(HGNClass.class);
    private List<String> valList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private List<String> listDescGR;
    Object[][] data;
    private String fileName;
    private String fileNameNGN;
    private String projectName;
    private NGNClass nGNClass;
    
    public HGNClass(JTabbedPane tab) {
        tabPane = tab;
    }
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        fileNameNGN = LoadData.getPathJantar12() + "Data/" + projectName+".NGN";
        int countGraph = new SXMClass(null).getIA(projectName+".SXM");
        nGNClass = new NGNClass();
        listDescGR = nGNClass.getNGN(projectName+".NGN");
        getContextFile(fileName);

        jPanel1 = new javax.swing.JPanel();
        columnNames = new String[26];
        columnNames[0]="XCH";
        columnNames[25]="сумма";
        for(int i=1; i<25; i++)
            columnNames[i]=""+i;
        columnNamesDescript = new String[26];
        for(int i=0; i<26; i++)
            columnNamesDescript[i]=LoadData.getDescript(columnNames[i]);
        
        //System.out.print(countGraph);
        data = new Object[countGraph][26];
        int i=0;
        for(String str: valList)
        {
            if(str.trim().length()>0){
            int j=0;
            if(countGraph>i){
                if(i<listDescGR.size())
                    data[i][j]=(String)listDescGR.get(i);
                j++;
                for (String string : str.split("[ ]"))
                {
                    data[i][j]=NumberHelpers.addNol(string.trim());
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
                if (colIndex == 25)
                    return false;
                else
                    return true;
            }
        };
        jTable1.setDefaultRenderer(Object.class, new VECTableRenderer(4));
        createMenu(this);
        jTable1.getModel().addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
          int row = e.getFirstRow();
          int column = e.getColumn();
          if(row<jTable1.getRowCount()){
          if(column!=25){
            float sum = 0;
            for(int k=1; k<25; k++){
                String val = (String)jTable1.getValueAt(row, k);
                float a=0;
                try{
                    a = Float.parseFloat(val);
                }catch(Exception er)
                {
                    sum=100;
                }
              sum+=a;
            }
            jTable1.setValueAt(sum+"", row, 25);
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
        File file = new File(fn);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            int count = 0;
            String title = LoadData.getPropTitles(getSuff(this.getClass().getSimpleName()));
            title = new String(title.getBytes("ISO-8859-1"), "CP1251");
            for (String string : title.split(",")) {
                if(string.trim().length()>0){
                    titleList.add(string.trim());
                    count++;
                }
            }
            while (dis.available() != 0) {
                String a = dis.readLine();
                a = new String(a.getBytes("ISO-8859-1"), "CP1251");
                valList.add(a.trim());
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
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"));
            PrintWriter pwNGN = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileNameNGN), "Cp1251"));
            for(int i=0; i<jTable1.getModel().getRowCount();i++)
            {
                if(jTable1.getModel().getValueAt(i,0)!=null && jTable1.getModel().getValueAt(i,0).toString().length()>0)
                    pwNGN.println(jTable1.getModel().getValueAt(i,0));
                for(int j=1; j<jTable1.getModel().getColumnCount();j++)
                {
                    if(jTable1.getModel().getValueAt(i,j)!=null && jTable1.getModel().getValueAt(i,j).toString().length()>0){
                        if(j==1)
                            pw.print(jTable1.getModel().getValueAt(i,j));
                        else
                            pw.print(" "+jTable1.getModel().getValueAt(i,j));
                    }else
                    {
                        if(j==1)
                            pw.print(0);
                        else
                            pw.print(" "+0);
                        
                    }
                    
                }
                pw.println("");
            }
            pw.close();
            pwNGN.close();
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }

}