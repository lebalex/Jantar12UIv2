/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui;

/**
 *
 * @author LebAlex
 */
public class ScenClass {
    private int id;
    private String name;
    private String descript;
    private int intervalCount;

    public ScenClass(int id, String name, String descript, int intervalCount) {
        this.id = id;
        this.name = name;
        this.descript = descript;
        this.intervalCount = intervalCount;
    }

    public String getDescript() {
        return descript;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return this.descript;
    }

    public int getIntervalCount() {
        return intervalCount;
    }
    
}
