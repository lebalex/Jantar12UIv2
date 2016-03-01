package helpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import paneljantar.PanelInterface;

/**
 *
 * @author ivc_LebedevAV
 */
public class ClearRowsActionAdapter implements ActionListener {
  PanelInterface adaptee;

  public ClearRowsActionAdapter(PanelInterface adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.clearRowsActionPerformed(e);
  }
}