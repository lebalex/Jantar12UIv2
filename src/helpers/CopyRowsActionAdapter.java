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
public class CopyRowsActionAdapter implements ActionListener {
  PanelInterface adaptee;

  public CopyRowsActionAdapter(PanelInterface adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.copyRowsActionPerformed(e);
  }
}