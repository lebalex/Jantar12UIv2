/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ivc_LebedevAV
 */
public class VECTableRenderer extends DefaultTableCellRenderer  
{
    private int typeTable;
    private Color colorHead = new Color(214,217,223);
    private Color colorSelect = new Color(57,105,138);
    private Color colorNechet = new Color(242,242,242);

    public VECTableRenderer(int typeTable) {
        this.typeTable = typeTable;
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Font orig = c.getFont();
        
        //if(isSelected)
            //System.out.println(c.getBackground());
        if(typeTable==1)
        {
            if(column==1 || column==3)
                c.setFont(new Font(orig.getFamily(), Font.BOLD, orig.getSize()));
            else
                c.setFont(orig);
        }
        else if(typeTable==2)
        {
            if(column==0){
                c.setFont(new Font(orig.getFamily(), Font.BOLD, orig.getSize()));
                if(isSelected){
                        c.setBackground(colorSelect);
                        c.setForeground(Color.white); 
                   }
                   else{
                        c.setBackground(Color.white);            
                        c.setForeground(Color.black); 
                   }
        }else{
                c.setFont(orig);
                try{
                    if(((String)value).trim().length()>0)
                        Float.parseFloat((String)value);
                    c.setForeground(Color.black); 
                }catch(Exception er)
                {
                    c.setForeground(Color.red); 
                }
            }
                   if(isSelected){
                        c.setBackground(colorSelect);
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.white); 
                   }
                   else{
                        c.setBackground(Color.white);            
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.black); 
                   }
            
        }
        else if(typeTable==3)
        {
            if(column==1 || column==3){
                c.setFont(new Font(orig.getFamily(), Font.BOLD, orig.getSize()));
                   if(isSelected){
                        c.setBackground(colorSelect);
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.white); 
                   }
                   else{
                        c.setBackground(Color.white);            
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.black); 
                   }

            }
            else{
                c.setFont(orig);
                try{
                    if(((String)value).trim().length()>0)
                        Float.parseFloat((String)value);
                    c.setForeground(Color.black); 
                }catch(Exception er)
                {
                    c.setForeground(Color.red); 
                }
            }
                   if(isSelected){
                        c.setBackground(colorSelect);
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.white); 
                   }
                   else{
                        c.setBackground(Color.white);            
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.black); 
                   }
        }
        else if(typeTable==4)
        {
            float a = 0;
            if(value!=null && ((String)value).trim().length()>0){
                try{
                    a = Float.parseFloat((String)value);
                }catch(Exception er){}
            }
            //Color origBg = c.getBackground();
            //c.setBackground(origBg);
            if(column==25){
                c.setFont(new Font(orig.getFamily(), Font.BOLD, orig.getSize()));
                c.setBackground(Color.yellow);
                if(a>24)
                    c.setForeground(Color.red);
                else
                    c.setForeground(Color.black);
            }
            else{
                c.setFont(orig);
                //if(column>0){
                try{
                    if(column>0 && value!=null && ((String)value).trim().length()>0){
                        c.setForeground(Color.black); 
                        a = Float.parseFloat((String)value);
                    }else
                        c.setForeground(Color.black); 
                }catch(Exception er)
                {
                    c.setForeground(Color.red); 
                }
                //}
                   if(isSelected){
                        c.setBackground(colorSelect);
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.white); 
                   }
                   else{
                        c.setBackground(Color.white);            
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.black); 
                   }

                
            }
        }
        else if(typeTable==5)
        {
                c.setFont(orig);
                try{
                    if(((String)value).trim().length()>0)
                        Float.parseFloat((String)value);
                    c.setForeground(Color.black); 
                }catch(Exception er)
                {
                    c.setForeground(Color.red); 
                }
                   if(isSelected){
                        c.setBackground(colorSelect);
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.white); 
                   }
                   else{
                        c.setBackground(Color.white);            
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.black); 
                   }
        }
        else if(typeTable==6)
        {
            if(column==0){
                c.setFont(new Font(orig.getFamily(), Font.BOLD, orig.getSize()));
                if(isSelected){
                        c.setBackground(colorSelect);
                        c.setForeground(Color.white); 
                   }
                   else{
                        c.setBackground(Color.white);            
                        c.setForeground(Color.black); 
                   }
            }
                c.setFont(orig);
                try{
                    if(((String)value).trim().length()>0)
                        Float.parseFloat((String)value);
                    c.setForeground(Color.black); 
                }catch(Exception er)
                {
                    c.setForeground(Color.red); 
                }
                   if(isSelected){
                        c.setBackground(colorSelect);
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.white); 
                   }
                   else{
                        c.setBackground(Color.white);            
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.black); 
                   }
        }
        else if(typeTable==7)
        {
            if(column==0){
                c.setFont(new Font(orig.getFamily(), Font.BOLD, orig.getSize()));
                c.setBackground(colorHead);
                c.setForeground(Color.black);

            }else{
                c.setFont(orig);
                try{
                    if(((String)value).trim().length()>0 && value!=null){
                        Float.parseFloat((String)value);
                        c.setForeground(Color.black); 
                    }else
                        c.setForeground(Color.black); 
                }catch(Exception er)
                {
                        c.setForeground(Color.red); 
                }
            
                   if(isSelected){
                        c.setBackground(colorSelect);
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.white); 
                   }
                   else{
                        c.setBackground(Color.white);            
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.black); 
                   }
            }
        }
        if((row%2)==0)
        {
            if(c.getBackground()!=Color.yellow && c.getBackground()!=colorHead)
                c.setBackground(colorNechet);
        }
        if(isSelected){
                        c.setBackground(colorSelect);
                        if(c.getForeground()!=Color.red)
                            c.setForeground(Color.white); 
                   }
        return c;
    }
}