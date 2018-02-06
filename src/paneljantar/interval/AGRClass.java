/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar.interval;

import helpers.*;
import jantar12ui.ButtonTabComponent;
import jantar12ui.Jantar12UI;
import jantar12ui.LoadData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
public class AGRClass extends EditClass implements PanelInterface {

    public static final Logger logger_job = Logger.getLogger(AGRClass.class);
    private List<String> titleList = new ArrayList<>();
    private ArrayList<String> valList = new ArrayList<>();
    Object[][] data;
    private List<JTable> jTableList;
    private List<ExcelAdapter> excelAdapterList;
    private String projectName;
    private JList rowHeader;
    private int countJOIdx = 0;
    private JScrollPane jScrollPane1;
    private List<JScrollPane> jScrollPane1List;

    public AGRClass(JTabbedPane tab) {
        tabPane = tab;
    }

    public JPanel getPanel(String fName) {
        projectName = fName.substring(0, fName.indexOf("."));
        String fileName = LoadData.getPathJantar12() + "Data/" + fName;
        getContextFile(fileName);
        int countM = new SXMClass(null).getM(projectName.substring(projectName.indexOf("/") + 1, projectName.length()) + ".SXM");
        int countJO = new AJOClass().getJOAll(projectName + ".AJO");

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane = new JTabbedPane();
        jTableList = new ArrayList<JTable>();
        excelAdapterList = new ArrayList<ExcelAdapter>();
        jScrollPane1List = new ArrayList<JScrollPane>();

        /**/
        for (int idx = 0; idx < countM; idx++) {
            countJOIdx = new AJOClass().getValueIndex(projectName + ".AJO", idx);
            int countJOIdxBegin = 0;
            if (idx > 0) {
                for (int k = 0; k < idx; k++) {
                    countJOIdxBegin += new AJOClass().getValueIndex(projectName + ".AJO", k);
                }
            }
            columnNames = new String[5 + 1];
            columnNamesDescript = new String[5 + 1];
            columnNames[0] = "JO";
            columnNamesDescript[0] = LoadData.getDescript(columnNames[0]);
            int i = 1;
            for (String string : titleList) {
                columnNames[i] = string;
                columnNamesDescript[i] = LoadData.getDescript(string);
                i++;
            }
            data = new Object[50][5 + 1];
            int j = 0;
            int ri = 0;
            for (String string : valList) {
                if (j >= countJOIdxBegin) {
                    if (j < countJOIdx + countJOIdxBegin) {
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
            jScrollPane1 = new javax.swing.JScrollPane();
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
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

                public void tableChanged(TableModelEvent e) {
                    if (e.getColumn() != 0) {
                        int ii = jTabbedPane.getSelectedIndex();
                        JTable jT = (JTable) jTableList.get(ii);
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

            jTabbedPane.add("" + (idx + 1), jScrollPane1);//Узел 
            jScrollPane1List.add(jScrollPane1);
            /**/
        }

        jLabel = new JLabel(LoadData.getTitle(getSuff(this.getClass().getSimpleName())));
        setArrangeTab(projectName, getSuff(this.getClass().getSimpleName()));

        return jPanel1;
    }

    @Override
    public JTable getTable() {
        return jTableList.get(jTabbedPane.getSelectedIndex());
    }

    @Override
    public ExcelAdapter getMyAd() {
        return excelAdapterList.get(jTabbedPane.getSelectedIndex());
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
            String a = LoadData.getPropTitles("AGR");
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
        String dataAJO = "";
        int countGroupAg = 0;
        try {
            int countKS = new SXMClass(null).getKS(projectName.substring(projectName.indexOf("/") + 1, projectName.length()) + ".SXM");
            //выделим номер интервала
            Pattern p = Pattern.compile("Interv([0-9]*)");
            Matcher m = p.matcher(projectName);
            String numIntervalStart = "01";
            if (m.find()) {
                numIntervalStart = m.group(1);
            }else logger_job.log(Level.ERROR, "no matcher 'Interv([0-9]*)' in "+projectName);
            for (int i_interv = Integer.parseInt(numIntervalStart); i_interv <= countKS; i_interv++) {
                String newIndexInterv=i_interv+"";
                if(i_interv<10)
                    newIndexInterv="0"+i_interv;
                String projectName_i = projectName.replaceAll(numIntervalStart+"/", newIndexInterv+"/");
                String fileName_i= LoadData.getPathJantar12() + "Data/" + projectName_i+".AGR";
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
                        dataAJO += countGroupAg + " ";
                        countGroupAg = 0;
                    }
                }
                new AJOClass().saveDataFromAGR(projectName_i + ".AJO", dataAJO.substring(0, dataAJO.length() - 1));
            }
            updateTitle(false);

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }

    }

}
