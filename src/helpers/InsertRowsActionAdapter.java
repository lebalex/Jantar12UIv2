/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import paneljantar.PanelInterface;

/**
 *
 * @author ivc_LebedevAV
 */
public class InsertRowsActionAdapter implements ActionListener {
  PanelInterface adaptee;

  public InsertRowsActionAdapter(PanelInterface adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.insertRowsActionPerformed(e);
  }
}