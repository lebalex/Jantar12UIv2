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
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import paneljantar.ENXClass;
import paneljantar.PanelInterface;
import paneljantar.SXMClass;

/**
 *
 * @author LebAlex
 */
public class KDDClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(KDDClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();
    Object[][] data;
    private String fileName;
    private String projectName;
    public KDDClass(JTabbedPane tab) {
        tabPane = tab;
    }
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        int countID = new IDDClass(null).getID(projectName+".IDD");
        jPanel1 = new javax.swing.JPanel();
        columnNames = new String[countID];
        columnNamesDescript = new String[countID];
        int i=0;
        for (String string : titleList)
        {
            if(i<countID){
                columnNames[i]=string+"("+(i+1)+")";
                columnNamesDescript[i]=LoadData.getDescript(string);
                i++;
            }
        }
        for(int j=i;j<countID;j++){
            columnNames[j]=titleList.get(0)+"("+(j+1)+")";
            columnNamesDescript[i]=LoadData.getDescript(titleList.get(0));
        }
        data = new Object[1][countID];
        i=0;
        for (String string : valList)
        {
            int j=0;
            String[] strSplit = string.trim().split("[ ]");

                for (String str2 : strSplit) {
                    if(j<countID){
                        if(str2.trim().length()>0){
                            data[i][j]=NumberHelpers.addNol(str2);
                            j++;
                    }
                }
                }
            i++;
        }
        jScrollPane1 = new javax.swing.JScrollPane();
        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        jTable1 = new JTable(model);
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
            String a = LoadData.getPropTitles("KDD");
            for (String string : a.split(",")) {
                if(string.trim().length()>0){
                    titleList.add(string.trim());
                }
            }
                a = dis.readLine();
                if(a!=null){
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
    public void saveValue() {
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"));
                for(int j=0; j<jTable1.getModel().getColumnCount();j++)
                {
                    String a = (String)jTable1.getModel().getValueAt(0,j);
                    if(a!=null)
                    {
                        if(j==0)
                            pw.print(jTable1.getModel().getValueAt(0,j));
                        else
                            pw.print(" "+jTable1.getModel().getValueAt(0,j));
                    }
                }
                pw.println("");
            pw.close();
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }
}
