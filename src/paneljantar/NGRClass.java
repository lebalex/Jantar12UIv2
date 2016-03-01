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
import java.util.Arrays;
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
public class NGRClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(NGRClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();
    private List<String> valListFull = new ArrayList<>();
    Object[][] data;
    private String fileName;
    private String projectName;
    List<String> listNAM;
    public NGRClass(JTabbedPane tab) {
        tabPane = tab;
    }
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        int countM = new SXMClass(null).getM(projectName+".SXM");
        int countKS = new SXMClass(null).getKS(projectName+".SXM");
        getContextFile(fileName);
        jPanel1 = new javax.swing.JPanel();
        columnNames = new String[countKS+1];
        columnNamesDescript = new String[countKS+1];
        int i=1;
        columnNames[0]="Name";
        columnNamesDescript[0]=LoadData.getDescript(columnNames[0]);
        for (String string : getTiitles(countKS))
        {
            columnNames[i]=string;
            columnNamesDescript[i]=LoadData.getDescript(string);
            i++;
        }
        listNAM = new NAMClass(null).getNAM(projectName+".NAM");
        data = new Object[countM][countKS+1];
        i=0;
        int k=0;
        while(i<valList.size())
        {
            if(k<countM){
                data[k][0]=listNAM.get(k);
                int j=1;
                while(j<countKS+1)
                {
                    if(i<valList.size())
                    {
                        data[k][j]=NumberHelpers.addNol(valList.get(i));
                        i++;
                    }
                    j++;
                }
                k++;
            }else i++;
        }
        jScrollPane1 = new javax.swing.JScrollPane();
        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        jTable1 = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                if (colIndex == 0)
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
          if(row<jTable1.getRowCount()){
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
        
        jLabel = new JLabel(LoadData.getTitle(getSuff(this.getClass().getSimpleName())+countKS)+" "+LoadData.getTitle(getSuff(this.getClass().getSimpleName())));
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
                valListFull.add(a.trim());
                for (String string : a.split("[ ]"))
                    valList.add(string.trim());
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
            for(int i=0; i<jTable1.getModel().getRowCount();i++)
            {
                for(int j=1; j<jTable1.getModel().getColumnCount();j++)
                {
                    if(jTable1.getModel().getValueAt(i,j)!=null)
                        if(j==1)
                            pw.print(jTable1.getModel().getValueAt(i,j));
                        else
                            pw.print(" "+jTable1.getModel().getValueAt(i,j));
                }
                pw.println("");
            }
            pw.close();
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }

    public List<String> getTiitles(int ks)
    {
        switch(ks){
            case 12: return Arrays.asList("jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec");
            case 4: return Arrays.asList("I","II","III","IV");
            case 2: return Arrays.asList("I","II");
            case 1: return Arrays.asList("I");
            default:return null;
        }
    
    }
    public void saveFromDelNGR(List<Integer> idM, String fn)
    {
        getContextFile(LoadData.getPathJantar12() + "Data/" + fn);
        for(int i:idM)
        {
            valListFull.set(i, "");
        }
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"));
            for(String string: valListFull)
            {
                if(string.trim().length()>0)
                pw.println(string);
            }
            pw.close();

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
    }
    public void saveFromInsNGR(List<Integer> idM, String fn)
    {
        getContextFile(LoadData.getPathJantar12() + "Data/" + fn);
        for(int i:idM)
        {
            valListFull.add(i, "0 0 0 0 0 0 0 0 0 0 0 0");
        }
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"));
            for(String string: valListFull)
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