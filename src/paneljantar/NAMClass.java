/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar;

import helpers.EditClass;
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
public class NAMClass extends EditClass implements PanelInterface {
    public static final Logger logger_job = Logger.getLogger(NAMClass.class);
    private List<String> valList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    Object[][] data;
    private String fileName;
    private String projectName;
    private SXMClass sXMClass;
    List<Integer> listIdToDelM = new ArrayList<Integer>();
    List<Integer> listIdToInsM = new ArrayList<Integer>();
    public NAMClass(JTabbedPane tab) {
        tabPane = tab;
    }
    public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        sXMClass = new SXMClass(null);
        int countM = sXMClass.getM(projectName+".SXM");
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
        data = new Object[countM][1];
        i=0;
        for (String string : valList)
        {
            if(i<countM)
                data[i][0]=string;
            i++;
        }
        jScrollPane1 = new javax.swing.JScrollPane();
        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        jTable1 = new JTable(model);
        createMenu(this);
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
                count++;

            }
            fis.close();
            bis.close();
            dis.close();
        } catch (FileNotFoundException e) {
            logger_job.log(Level.ERROR, e);
        } catch (IOException e) {logger_job.log(Level.ERROR, e);
        }
        listIdToDelM.clear();
        listIdToInsM.clear();
    }
    @Override
    public void saveValue() {
        
        int countM=0;
        try {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"))) {
                for(int i=0; i<jTable1.getModel().getRowCount();i++){
                    if(jTable1.getModel().getValueAt(i,0).toString().trim().length()>0){
                        pw.println(jTable1.getModel().getValueAt(i,0));
                        countM++;
                    }
                }
                sXMClass.saveM(countM);
                if(listIdToDelM.size()>0){
                    new YZLClass(null).saveFromDelYZL(listIdToDelM, projectName+".YZL");
                    new NGRClass(null).saveFromDelNGR(listIdToDelM, projectName+".NGR");
                }
                if(listIdToInsM.size()>0){
                    new YZLClass(null).saveFromInsYZL(listIdToInsM, projectName+".YZL");
                    new NGRClass(null).saveFromInsNGR(listIdToInsM, projectName+".NGR");
                }
            }
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }
    @Override
    public void addToDelM(int idM)
    {
        listIdToDelM.add(idM);
    }
    @Override
    public void addToInsM(int idM)
    {
        listIdToInsM.add(idM);
    }

    public List<String> getNAM(String fName)
    {
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        return valList;
    }
}
