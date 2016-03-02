/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui;

import java.util.Vector;

public class BufferClass {
    private static BufferClass instance;
    private Vector dateVector;

    public BufferClass() {
        instance=this;
    }

    public Vector getDateVector() {
        return dateVector;
    }

    public void setDateVector(Vector dateVector) {
        this.dateVector = dateVector;
    }

    public static BufferClass getInstance() {
        if(instance==null)
            new BufferClass();
        return instance;
    }

    
}
