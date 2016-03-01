/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import jantar12ui.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import paneljantar.NAMClass;
import paneljantar.PanelInterface;
import paneljantar.interval.AGRClass;
import paneljantar.interval.AZRClass;
import paneljantar.interval.KCRClass;
import paneljantar.interval.LINClass;

/**
 *
 * @author ivc_LebedevAV
 */
public class EditClass {

    public JTable jTable1;
    public JPopupMenu popupMenu;
    public String INSERT_CMD = "Вставить строку";
    public String DELETE_CMD = "Удалить строку";
    public String CLEAR_CMD = "Очистить строку";
    //public String COPY_CMD = "Копировать дынные";
    //public String PASTE_CMD = "Вставить данные";
    PanelInterface adaptee;
    public Vector dateVector;
    private BufferClass bufferClass;
    public String[] columnNamesDescript;
    public String[] columnNames;
    public StringBuffer comment = new StringBuffer();
    public JLabel jLabel;
    public JPanel jPanel1;
    public JScrollPane jScrollPane1;
    public JTabbedPane jTabbedPane;
    public JTabbedPane tabPane;
    private char[] valuesKey = new char[] {'0','1','2','3','4','5','6','7','8','9','.'};
    public ExcelAdapter myAd;
    
    public void setJantar12UI(Jantar12UI jantar12UI)
    {
        //
    }
    public void createMenu(PanelInterface adaptee) {
        bufferClass = BufferClass.getInstance();
        this.adaptee = adaptee;
        popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(CLEAR_CMD);
        menuItem.addActionListener(new ClearRowsActionAdapter(adaptee));
        popupMenu.add(menuItem);
        if ((adaptee instanceof NAMClass) || (adaptee instanceof AGRClass) || (adaptee instanceof LINClass) || (adaptee instanceof AZRClass) || (adaptee instanceof KCRClass)) {
            menuItem = new JMenuItem(DELETE_CMD);
            menuItem.addActionListener(new DeleteRowsActionAdapter(adaptee));
            popupMenu.add(menuItem);
        }
        if ((adaptee instanceof NAMClass)) {
            menuItem = new JMenuItem(INSERT_CMD);
            menuItem.addActionListener(new InsertRowsActionAdapter(adaptee));
            popupMenu.add(menuItem);
        }
        //if((adaptee instanceof HGNClass))
        //{
        //menuItem = new JMenuItem(COPY_CMD);
        //menuItem.addActionListener(new CopyRowsActionAdapter(adaptee));
        //popupMenu.add(menuItem);
        //menuItem = new JMenuItem(PASTE_CMD);
        //menuItem.addActionListener(new PasteRowsActionAdapter(adaptee));
        //popupMenu.add(menuItem);
        //}
        MouseListener popupListener = new PopupListener();
        jTable1.addMouseListener(popupListener);
        setDescript();

    }

    public ExcelAdapter getMyAd() {
        return myAd;
    }
    
    public void setDescript() {
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setRowHeight(20);
        jTable1.setCellSelectionEnabled(true);
        myAd = new ExcelAdapter(jTable1);
        //jTable1.setShowHorizontalLines(true);
        //jTable1.setShowVerticalLines(true);
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                keyPressedTable(evt);
                
            }
        });
        jTable1.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1 && !evt.isConsumed()) {
                    evt.consume();
                    JTable table = ((JTableHeader) evt.getSource()).getTable();
                    TableColumnModel colModel = table.getColumnModel();

                    // The index of the column whose header was clicked
                    int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                    int mColIndex = table.convertColumnIndexToModel(vColIndex);
                    if(columnNamesDescript!=null)
                    if (columnNamesDescript[mColIndex] != null) {
                        DescriptDialog descriptDialog = new DescriptDialog(null, true);
                        
                        descriptDialog.setDescript(columnNames[mColIndex], columnNamesDescript[mColIndex], evt.getX()+jTable1.getParent().getParent().getParent().getParent().getX(), evt.getY()+100);
                        descriptDialog.setVisible(true);
                    }
                }

            }
        });

    }

    public JTable getTable() {
        return jTable1;
    }

    public void clearRowsActionPerformed(ActionEvent e) {
        JTable jT = adaptee.getTable();
        int row = jT.getSelectedRow();
        for (int i = 0; i < jT.getColumnCount(); i++) {
            if (jT.isCellEditable(row, i)) {
                jT.setValueAt("", row, i);
            }
        }
    }

    public void deleteRowsActionPerformed(ActionEvent e) {
        JTable jT = adaptee.getTable();
        int row = jT.getSelectedRow();
        if (adaptee instanceof NAMClass) {
            adaptee.addToDelM(row);
        }
        DefaultTableModel dtm = (DefaultTableModel) jT.getModel();
        dtm.removeRow(row);
    }

    public void insertRowsActionPerformed(ActionEvent e) {
        JTable jT = adaptee.getTable();
        int row = jT.getSelectedRow();
        if (adaptee instanceof NAMClass) {
            adaptee.addToInsM(row);
        }
        DefaultTableModel dtm = (DefaultTableModel) jT.getModel();
        dtm.insertRow(row, new Vector());
    }

    public void copyRowsActionPerformed(ActionEvent e) {
        JTable jT = adaptee.getTable();
        int row = jT.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) jT.getModel();
        dateVector = new Vector();
        for (int i = 0; i < dtm.getColumnCount(); i++) {
            dateVector.add(dtm.getValueAt(row, i));
        }
        bufferClass.setDateVector(dateVector);
    }

    public void pasteRowsActionPerformed(ActionEvent e) {
        JTable jT = adaptee.getTable();
        int row = jT.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) jT.getModel();
        dateVector = bufferClass.getDateVector();
        if (dateVector != null && dateVector.size() > 0) {
            for (int i = 0; i < dtm.getColumnCount(); i++) {
                if(jT.isCellEditable(row, i))
                    dtm.setValueAt(dateVector.get(i), row, i);
            }
        }
    }

    public void addToDelM(int idM) {
    }

    public void addToInsM(int idM) {
    }

    public class PopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        private void showPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    public String getSuff(String cn)
    {
        return cn.substring(0, cn.indexOf("Class"));
    }
    private void setLabelClick(final String projectName, final String className)
    {
        jLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CommentDialog сommentDialog = new CommentDialog(null, true);
                ReadWriteComm readWriteComm = new ReadWriteComm();
                        
                        сommentDialog.setDescript(projectName+"."+className, readWriteComm.getComm(projectName, className), evt.getX()+jTable1.getParent().getParent().getParent().getParent().getX(), evt.getY()+100);
                        сommentDialog.setVisible(true);
            }
        });
    }
    public void setArrange(String projectName, String className)
    {
        setLabelClick(projectName, className);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE))
        );
    }
    public void setArrangeTab(String projectName, String className)
    {
        setLabelClick(projectName, className);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE))
        );
        
    }
    private boolean contains(char data) {
    for (int i = 0; i < valuesKey.length; i++) {
      if (valuesKey[i] == data) {
        return true;
      }
    }
    return false;
  }
    public void keyPressedTable(java.awt.event.KeyEvent evt)
    {
        //System.out.println(evt.getKeyCode());
        //System.out.println(evt.getKeyChar());
        //evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER
        
        JTable jT=(JTable)evt.getSource();
        int col = jT.getSelectedColumn();
        int row = jT.getSelectedRow();
            int[] rowsselected = jT.getSelectedRows();
            int[] colsselected = jT.getSelectedColumns();
            int numcols = jT.getSelectedColumnCount();
            int numrows = jT.getSelectedRowCount();
        if(evt.getKeyCode()==java.awt.event.KeyEvent.VK_DELETE && (numcols>1 || numrows>1))
        {
            for (int i = 0; i < numrows; i++)
                for (int j = 0; j < numcols; j++)
                    if(jT.isCellEditable(rowsselected[i], colsselected[j]))
                        jT.setValueAt(null, rowsselected[i], colsselected[j]);
        }
        else if(contains(evt.getKeyChar())  || evt.getKeyCode()==java.awt.event.KeyEvent.VK_DELETE)
        {
            if(jT.isCellEditable(row, col))
                jT.setValueAt(null, row, col);
        }
        if(jT.isCellEditable(row, col))
            updateTitle(true);

    }
    public void updateTitle(boolean bold)
    {
        if(tabPane.getTabComponentAt(tabPane.getSelectedIndex()) instanceof ButtonTabComponent)
        {
            ((ButtonTabComponent)tabPane.getTabComponentAt(tabPane.getSelectedIndex())).updateLabel(bold);
        }
    }

}
