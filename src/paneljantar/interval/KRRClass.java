/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar.interval;

import helpers.ListNumRow;
import helpers.RowHeaderRenderer;
import jantar12ui.ButtonTabComponent;
import jantar12ui.LoadData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import paneljantar.PanelInterface;
import paneljantar.SXMClass;

/**
 *
 * @author ivc_LebedevAV
 */
public class KRRClass /*implements PanelInterface*/ {
    public static final Logger logger_job = Logger.getLogger(KRRClass.class);
    private List<String> titleList = new ArrayList<>();
    private List<String> valList = new ArrayList<>();

    String[] columnNames;
    Object[][] data;

    
    public KRRClass() {
    }
    /*public JPanel getPanel(String fName)
    {
        projectName=fName.substring(0,fName.indexOf("."));
        fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);

        int countM = new SXMClass(null).getM(projectName.substring(projectName.indexOf("/")+1, projectName.length()) +".SXM");
        JPanel jPanel1 = new javax.swing.JPanel();
        jTabbedPane = new JTabbedPane();
        jTableList = new ArrayList<JTable>();
        jScrollPane1List = new ArrayList<JScrollPane>();
        
   
        int idxRow=0;
        for(int idx=0; idx<countM; idx++){
        countNRRIdx =  new NRRClass(null).getNRR(projectName+".NRR",idx+1);

        columnNames = new String[1];
        int i=0;
        for (String string : titleList)
        {
                columnNames[i]=string;
                i++;
        }
        data = new Object[50][1];
        if(countNRRIdx>0 && valList.size()>idxRow){
        String string = (String)valList.get(idxRow);
        i=0;
        for (String str : string.split("[ ]"))
        {
            //if(i<countNRRIdx)
                        if(str.trim().length()>0)
                            data[i][0]=str.trim();
                    i++;
        }
        if(i<countNRRIdx)
        {
            for(int k=i; k<countNRRIdx;k++)
            {
                data[k][0]="вставить значение!!!";
            }
        }
        
        idxRow++;
        }

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new JTable(data, columnNames);
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                updateTitle(true);
            }
        });
        jTable1.getModel().addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
        updateTitle(true);
      }
    });
    jTableList.add(jTable1);
        
        
jScrollPane1.setViewportView(jTable1);

        ListModel lm = new ListNumRow(countNRRIdx,50);
        rowHeader = new JList(lm);    
        rowHeader.setFixedCellWidth(50);
        rowHeader.setFixedCellHeight(jTable1.getRowHeight()
        + jTable1.getRowMargin()+ jTable1.getIntercellSpacing().height);
        rowHeader.setCellRenderer(new RowHeaderRenderer(jTable1));
        jScrollPane1.setRowHeaderView(rowHeader);


        javax.swing.GroupLayout jPanel1LayoutT = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1LayoutT);
        jPanel1LayoutT.setHorizontalGroup(
            jPanel1LayoutT.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel1LayoutT.setVerticalGroup(
            jPanel1LayoutT.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        
        jTabbedPane.add("Узел "+(idx+1),jScrollPane1);
        jScrollPane1List.add(jScrollPane1);

        }
        
        
        
javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );        

        
        return jPanel1;
    }*/

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
            String a = LoadData.getPropTitles("KRR");
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
    /*@Override
    public void saveValue() {
        String dataNRR="";
        int countGroupAg=0;
        try {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "Cp1251"))) {
            for (String string : titleList)
                pw.print(string+" ");
            pw.println();
            for(int tabIdx=0; tabIdx<jTableList.size(); tabIdx++){
                JTable jT = jTableList.get(tabIdx);
                for(int j=0; j<jT.getModel().getRowCount();j++){
                    if(jT.getModel().getValueAt(j,0)!=null && jT.getModel().getValueAt(j,0).toString().trim().length()>0){
                        countGroupAg++;
                    for(int i=0; i<jT.getModel().getColumnCount();i++){
                        if(j==0)
                            pw.print(jT.getModel().getValueAt(j,i));
                        else
                            pw.print(" "+jT.getModel().getValueAt(j,i));
                    }
                    
                }
                }
                
                if(countGroupAg>0) pw.println("");
                dataNRR+=countGroupAg+" ";
                countGroupAg=0;
            }
            }
            new NRRClass(null).saveDataFromKRR(projectName+".NRR",dataNRR.substring(0, dataNRR.length()-1));
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
        
    }*/
    /*private void updateTitle(boolean bold)
    {
        int ii = jTabbedPane.getSelectedIndex();
        JTable jT = (JTable)jTableList.get(ii);
        int i=1;
        while (jT.getValueAt(i, 0)!=null && jT.getValueAt(i, 0).toString().trim().length()>0)
            i++;
        ListModel lm = new ListNumRow(i,50);
        rowHeader = new JList(lm);    
        rowHeader.setFixedCellWidth(50);
        rowHeader.setCellRenderer(new RowHeaderRenderer(jT));
        JScrollPane jScroll = jScrollPane1List.get(ii);
        jScroll.setRowHeaderView(rowHeader);
        
        if(tabPane.getTabComponentAt(tabPane.getSelectedIndex()) instanceof ButtonTabComponent)
        {
            ((ButtonTabComponent)tabPane.getTabComponentAt(tabPane.getSelectedIndex())).updateLabel(bold);
        }
    }*/
    public int getMaxKRR(String fName, int idx)
    {
        return getValue2(fName, idx);
    }
    private int getValue2(String fName, int idx)
    {
        int result=0;
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        String string = (String)valList.get(idx);

            for (String str : string.split("[ ]")){
               if(str.trim().length()>0){
                   try{
                       if(result<Integer.parseInt(str.trim()))
                        result=Integer.parseInt(str.trim());
                   }catch(Exception e){} 
                }
            }

        return result;
        
    }
    public List<String> getListKRR(String fName)
    {
        getContextFile(LoadData.getPathJantar12() + "Data/" + fName);
        return valList;
    }
    public String getTitles()
    {
        String result="";
        for (String string : titleList)
            result+=string+" ";
        return result;
    }
}

