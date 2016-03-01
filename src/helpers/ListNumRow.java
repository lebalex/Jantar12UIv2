/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author ivc_LebedevAV
 */
public class ListNumRow extends AbstractListModel {
  String[] strings;

  public ListNumRow(int count, int countAll) {
    strings = new String[countAll];
    for (int i = 0; i < count; i++) {
      strings[i] = (i+1)+"";
    }
    for (int i = count; i < countAll; i++) {
      strings[i] = " ";
    }

  }
  public ListNumRow(int count, List<String> listTitles) {
    strings = new String[count];
    int i=0;
    for(String str:listTitles)
    {
        strings[i] = str;
        i++;
    }
  }

  public int getSize() {
    return strings.length;
  }

  public Object getElementAt(int index) {
    return strings[index];
  }
}