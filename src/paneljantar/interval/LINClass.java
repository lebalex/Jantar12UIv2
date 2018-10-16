/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar.interval;

import helpers.*;
import jantar12ui.LoadData;
import jantar12ui.SaveAGRJDialog;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    //Object[][] data;
    Vector<Vector> data;
    private List<JTable> jTableList;
    private List<ExcelAdapter> excelAdapterList;
    private String projectName;
    private JList rowHeader;
    private int countIBIdx = 0;
    private JScrollPane jScrollPane1;
    private List<JScrollPane> jScrollPane1List;
    //private List<LogEditClass> logEditClassList;

    public LINClass(JTabbedPane tab) {
        tabPane = tab;
    }

    public JPanel getPanel(String fName) {
        projectName = fName.substring(0, fName.indexOf("."));
        String fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        int countIB = new SXMClass(null).getIB(projectName.substring(projectName.indexOf("/") + 1, projectName.length()) + ".SXM");
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane = new JTabbedPane();
        jTableList = new ArrayList<JTable>();
        excelAdapterList = new ArrayList<ExcelAdapter>();
        jScrollPane1List = new ArrayList<JScrollPane>();
        //logEditClassList = new ArrayList<>();

        /**/
        for (int idx = 0; idx < countIB; idx++) {
            countIBIdx = new IZLClass().getValueIndex(projectName + ".IZL", idx);
            int countIBIdxBegin = 0;
            if (idx > 0) {
                for (int k = 0; k < idx; k++) {
                    countIBIdxBegin += new IZLClass().getValueIndex(projectName + ".IZL", k);
                }
            }
            columnNames = new String[5 + 1];
            columnNamesDescript = new String[5 + 1];
            columnNames[0] = "IZ";
            columnNamesDescript[0] = LoadData.getDescript(columnNames[0]);
            int i = 1;
            for (String string : titleList) {
                columnNames[i] = string;
                columnNamesDescript[i] = LoadData.getDescript(string);
                i++;
            }
            setData(countIBIdxBegin);

            jScrollPane1 = new javax.swing.JScrollPane();
            //DefaultTableModel model = new DefaultTableModel(data, columnNames);
            DefaultTableModel model = new DefaultTableModel(data, new Vector(Arrays.asList(columnNames)));
            jTable1 = new JTable(model) {
                public boolean isCellEditable(int rowIndex, int colIndex) {
                    if (colIndex != 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            jTable1.setDefaultRenderer(Object.class, new VECTableRenderer(7));
            createMenu(this);

            jTable1.getModel().addTableModelListener(new TableModelListener() {

                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getColumn() != 0) {
                        int ii = jTabbedPane.getSelectedIndex();
                        JTable jT = (JTable) jTableList.get(ii);
                        LogEditClass logEditClass = null;
                        Object newValue = null;
                        switch (e.getType()) {
                            case -1:
                                logEditClass = new LogEditClass(e.getType(), ii, e.getFirstRow(), e.getColumn(), "del");
                                break;
                            case 0:
                                if (e.getColumn() > 0) {
                                    newValue = jT.getValueAt(e.getFirstRow(), e.getColumn());
                                    if (newValue != null) {
                                        logEditClass = new LogEditClass(e.getType(), ii, e.getFirstRow(), e.getColumn(), (String) newValue);
                                    }
                                }
                                break;
                            case 1:
                                logEditClass = new LogEditClass(e.getType(), ii, e.getFirstRow(), 1, "add");
                                break;
                        }
                        /*if (logEditClass != null) {
                            addLogEditClassList(logEditClass);
                        }*/
                        for (int i = 0; i < jT.getRowCount(); i++) {
                            if (jT.getValueAt(i, 1) != null && jT.getValueAt(i, 1).toString().trim().length() > 0) {
                                jT.setValueAt(i + 1, i, 0);
                            } else {
                                jT.setValueAt("", i, 0);
                            }
                        }
                    }
                    updateTitle(true);
                }
            });
            jTableList.add(jTable1);
            excelAdapterList.add(myAd);

            jScrollPane1.setViewportView(jTable1);

            jTabbedPane.add("" + (idx + 1), jScrollPane1);//Связь 
            jScrollPane1List.add(jScrollPane1);
            /**/
        }

        jLabel = new JLabel(LoadData.getTitle(getSuff(this.getClass().getSimpleName())));
        setArrangeTab(projectName, getSuff(this.getClass().getSimpleName()));

        return jPanel1;
    }

    private void setData(int countIBIdxBegin) {
        data = new Vector();
        int j = 0;
        int ri = 0;
        for (String string : valList) {
            if (j >= countIBIdxBegin) {
                if (j < countIBIdx + countIBIdxBegin) {
                    Vector<String> dataRow = new Vector();
                    dataRow.addElement(Integer.toString(ri + 1));
                    for (String str : string.split("[ ]")) {
                        if (str.trim().length() > 0) {
                            dataRow.addElement(NumberHelpers.addNol(str.trim()));
                        }
                    }
                    ri++;
                    data.addElement(dataRow);
                }
            }
            j++;

        }
        if (data.isEmpty()) {
            data.addElement(new Vector(Arrays.asList(new String[columnNames.length])));
            data.get(0).set(0, 1);
        }
    }

    /*private void setData(int countIBIdxBegin)
    {
            data = new Object[50][5 + 1];
            int i = 0;
            int j = 0;
            int ri = 0;
            for (String string : valList) {
                if (j >= countIBIdxBegin) {
                    if (j < countIBIdx + countIBIdxBegin) {
                        if(ri>=data.length){
                            moreData(data);
                                    }
                        data[ri][0] = ri + 1;
                        i = 1;
                        for (String str : string.split("[ ]")) {
                            if (str.trim().length() > 0) {
                                data[ri][i] = NumberHelpers.addNol(str.trim());
                            }
                            i++;
                        }
                        ri++;
                    }
                }
                j++;

            }
    }
    private void moreData(Object[][] arr)
    {
        data = new Object[arr.length*2+1][6];
        for (int row = 0; row < arr.length; row++){
              for (int col = 0; col < arr[row].length; col++){
                  data[row][col] = arr[row][col];
              }
        }
    }*/
    /*private void addLogEditClassList(LogEditClass logEditClass) {
        boolean find = false;
        for (LogEditClass a : logEditClassList) {
            if (a.getId_list() == logEditClass.getId_list() && a.getId_row() == logEditClass.getId_row() && a.getId_col() == logEditClass.getId_col()) {
                a.setVal(logEditClass.getVal());
                find = true;
            }
        }
        if (!find) {
            logEditClassList.add(logEditClass);
        }
    }*/

    @Override
    public JTable getTable() {
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
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                logger_job.log(Level.ERROR, e);
                saveValue();
                JOptionPane.showMessageDialog(null, "Файл создан, необходимо его заполнить перед открытием других!", "Предупреждение", JOptionPane.WARNING_MESSAGE);
            }

            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            int count = 0;
            String a = LoadData.getPropTitles("LIN");
            a = new String(a.getBytes("ISO-8859-1"), "CP1251");
            for (String string : a.split(",")) {
                if (string.trim().length() > 0) {
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
        } catch (IOException e) {
            logger_job.log(Level.ERROR, e);
        }
    }

    @Override
    public void saveValue() {
        try {
            /*if (logEditClassList.isEmpty()) {
                return;
            }*/
            int countKS = new SXMClass(null).getKS(projectName.substring(projectName.indexOf("/") + 1, projectName.length()) + ".SXM");
            //выделим номер интервала
            Pattern p = Pattern.compile("interv([0-9]*)");
            Matcher m = p.matcher(projectName.toLowerCase());
            String numIntervalStart = "01";
            if (m.find()) {
                numIntervalStart = m.group(1);
            } else {
                logger_job.log(Level.ERROR, "no matcher 'interv([0-9]*)' in " + projectName.toLowerCase());
                JOptionPane.showMessageDialog(null, "no matcher 'interv([0-9]*)' in " + projectName.toLowerCase(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
            /*if (JOptionPane.showConfirmDialog(null, "Перезаписать данные в остальных интервалах?", "Перезаписать данные?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                countKS = Integer.parseInt(numIntervalStart);
            }*/
            //boolean doUpdate = false;
            SaveAGRJDialog saveAGRJDialog = new SaveAGRJDialog(getJantar12UI(), true);
            saveAGRJDialog.setVisible(true);
            int q = saveAGRJDialog.getReturnStatus();
            switch (q) {
                case -1:
                    return;
                case 0:
                    countKS = Integer.parseInt(numIntervalStart);
                    break;
                /*case 1:
                    doUpdate = false;
                    break;
                case 2:
                    doUpdate = true;
                    break;*/

            }
            for (int i_interv = Integer.parseInt(numIntervalStart); i_interv <= countKS; i_interv++) {
                String dataIZL = "";
                int countGroupAg = 0;

                String newIndexInterv = i_interv + "";
                if (i_interv < 10) {
                    newIndexInterv = "0" + i_interv;
                }
                String projectName_i = projectName.replace(numIntervalStart + "/", newIndexInterv + "/");
                String fileName_i = LoadData.getPathJantar12() + "Data/" + projectName_i + ".LIN";
                /*if (i_interv != Integer.parseInt(numIntervalStart) && doUpdate) {
                    rollforwatd(projectName_i + ".LIN", fileName_i);
                } else {*/
                    try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName_i), "Cp1251"))) {
                        for (int tabIdx = 0; tabIdx < jTableList.size(); tabIdx++) {
                            JTable jT = jTableList.get(tabIdx);
                            for (int j = 0; j < jT.getModel().getRowCount(); j++) {
                                if (jT.getModel().getValueAt(j, 1) != null && jT.getModel().getValueAt(j, 1).toString().trim().length() > 0) {
                                    countGroupAg++;
                                    for (int i = 1; i < jT.getModel().getColumnCount(); i++) {
                                        if (i == 1) {
                                            pw.print(jT.getModel().getValueAt(j, i));
                                        } else {
                                            pw.print(" " + jT.getModel().getValueAt(j, i));
                                        }
                                    }
                                    pw.println("");
                                }
                            }
                            dataIZL += countGroupAg + " ";
                            countGroupAg = 0;
                        }
                    }
                    new IZLClass().saveDataFromAGR(projectName_i + ".IZL", dataIZL.substring(0, dataIZL.length() - 1));
                /*}*/
            }

            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }

    }

    /*private void rollforwatd(String fName, String fileName_i) {
        try {
            String dataIZL = "";
            String projectName = fName.substring(0, fName.indexOf("."));
            String fileName = LoadData.getPathJantar12() + "Data/" + fName;
            getContextFile(fileName);
            int countIB = new SXMClass(null).getIB(projectName.substring(projectName.indexOf("/") + 1, projectName.length()) + ".SXM");

            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName_i), "Cp1251"))) {
                for (int idx = 0; idx < countIB; idx++) {
                    countIBIdx = new IZLClass().getValueIndex(projectName + ".IZL", idx);
                    int countIBIdxBegin = 0;
                    if (idx > 0) {
                        for (int k = 0; k < idx; k++) {
                            countIBIdxBegin += new IZLClass().getValueIndex(projectName + ".IZL", k);
                        }
                    }

                    setData(countIBIdxBegin);

                    /////////////////////
                    for (LogEditClass a : logEditClassList) {
                        if (idx == a.getId_list()) {
                            if ((a.getType() == -1) && (a.getId_row()<data.size())) {
                                Vector<String> dataRow = data.get(a.getId_row());
                                dataRow.set(0, null);
                            } else if (a.getType() == 1) {
                                Vector<String> dataRow = new Vector();
                                dataRow.addElement(Integer.toString(data.size() + 1));
                                dataRow.addElement(a.getVal());
                                for (int i = 2; i < columnNames.length; i++) {
                                    dataRow.addElement(null);
                                }
                                data.addElement(dataRow);
                            } else if ((a.getVal().length() > 0) && (a.getId_row()<data.size())){
                                Vector<String> dataRow = data.get(a.getId_row());
                                dataRow.set(a.getId_col(), a.getVal());
                            }

                        }
                    }
                    int countGroupAg = 0;
                    for (int row = 0; row < data.size(); row++) {
                        Vector<String> dataRow = data.get(row);
                        if (dataRow.get(0) != null) {
                            countGroupAg++;
                            for (int col = 1; col < dataRow.size(); col++) {
                                if (col == 1) {
                                    pw.print(dataRow.get(col));
                                } else {
                                    pw.print(" " + dataRow.get(col));
                                }
                            }
                            pw.println("");
                        }
                    }
                    dataIZL += countGroupAg + " ";
                }//конец листа

                new IZLClass().saveDataFromAGR(projectName + ".IZL", dataIZL.substring(0, dataIZL.length() - 1));
            }
        } catch (Exception e) {
        }

    }*/
}
