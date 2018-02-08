/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

/**
 *
 * @author ivc_lebedevav
 */
public class LogEditClass {
    private int type;
    private int id_list;
    private int id_row;
    private int id_col;
    private String val;

    public LogEditClass(int type, int id_list, int id_row, int id_col, String val) {
        this.type = type;
        this.id_list = id_list;
        this.id_row = id_row;
        this.id_col = id_col;
        this.val = val;
    }

    public int getType() {
        return type;
    }

    public int getId_list() {
        return id_list;
    }

    public int getId_row() {
        return id_row;
    }

    public int getId_col() {
        return id_col;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
    
    
    
}
