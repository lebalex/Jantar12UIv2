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
 * @author LebAlex
 */
public class PasteRowsActionAdapter implements ActionListener {
  PanelInterface adaptee;

  public PasteRowsActionAdapter(PanelInterface adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.pasteRowsActionPerformed(e);
  }
}