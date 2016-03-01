/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paneljantar;

import helpers.ExcelAdapter;
import jantar12ui.Jantar12UI;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author LebAlex
 */
public interface PanelInterface {
    public JPanel getPanel(String fName);
    public void saveValue();
    public void clearRowsActionPerformed(ActionEvent e);
    public void deleteRowsActionPerformed(ActionEvent e);
    public void insertRowsActionPerformed(ActionEvent e);
    public void copyRowsActionPerformed(ActionEvent e);
    public void pasteRowsActionPerformed(ActionEvent e);
    public JTable getTable();
    public void addToDelM(int idM);
    public void addToInsM(int idM);
    public ExcelAdapter getMyAd();
    public void setJantar12UI(Jantar12UI jantar12UI);
}
