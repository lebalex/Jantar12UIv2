/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import paneljantar.SXMClass;

/**
 *
 * @author LebAlex
 */
public class TreeNode {
private List<ScenClass> listRoot = new ArrayList<ScenClass>();
private ExtNameClass extNameClass;
 

    public TreeNode(String pathJantar12, ExtNameClass extNameClass) {
        this.extNameClass = extNameClass;
        File file = new File(pathJantar12 + "BEGIN/PcxAll.dat");
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() != 0) {
                String a = dis.readLine();
                if (a.length() > 9) {
                    String b = (String) a.substring(0, 8);
                    //System.out.println(b);
                    String c = (String) a.substring(9, a.length());
                    String str = new String(c.getBytes("ISO-8859-1"), "CP1251");


                    ScenClass scenClass = new ScenClass(0, b.trim(), str.trim(), getVariantCount(b.trim()));
                    listRoot.add(scenClass);
                }
            }

            fis.close();
            bis.close();
            dis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    public DefaultMutableTreeNode ctreateTree() {
        DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("Все проекты");
        DefaultMutableTreeNode project = null;
        for (ScenClass temp : listRoot) {
            project = new DefaultMutableTreeNode(temp);
            treeRoot.add(project);
            createNode(temp,project);
        }

        return treeRoot;

    }
    private void createNode(ScenClass nameRoot,DefaultMutableTreeNode project) {

        for (String temp : extNameClass.getList()) {
            project.add(new DefaultMutableTreeNode(temp));
        }
        for(int i=0; i<nameRoot.getIntervalCount();i++)
                project.add(createNodeInterval(i));

    }
    private DefaultMutableTreeNode createNodeInterval(int nInterval) {
        DefaultMutableTreeNode interv = null;
        interv = new DefaultMutableTreeNode("Interv"+getNum(nInterval+1));
        for (String temp : extNameClass.getListInterval()) {
            interv.add(new DefaultMutableTreeNode(temp));
        }
        return interv;
    }
    private String getNum(int i)
    {
        if(i<10) return "0"+i;
        else
            return i+"";
    }

    private int getVariantCount(String projectName)
    {
        //KNXClass kNXClass = new KNXClass();
        //return kNXClass.getVariantCount(LoadData.getPathJantar12() + "Data/" + projectName+".KNX");
        //return 12;
        return new SXMClass(null).getKS(projectName+".SXM");
    }
        }
