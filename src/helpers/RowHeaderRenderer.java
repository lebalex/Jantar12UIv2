/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.JTableHeader;


/**
 *
 * @author ivc_LebedevAV
 */
public class RowHeaderRenderer extends JLabel implements ListCellRenderer 
{
    public RowHeaderRenderer(JTable table) 
    {
        JTableHeader header = table.getTableHeader();
        
        setOpaque(true);
        //setBorder(UIManager.getBorder(table.getTableHeader().getBorder()));
        //setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        this.setBorder(BorderFactory.createMatteBorder(0,0,1,2,Color.GRAY));
        setHorizontalAlignment(CENTER);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
    }
  
    public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
    {
        setText((value == null) ? "" : value.toString());    
        return this;
    }
}