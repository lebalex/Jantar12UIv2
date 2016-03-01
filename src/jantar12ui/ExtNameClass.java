/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui;

import java.util.*;

/**
 *
 * @author LebAlex
 */
public class ExtNameClass {
    private List<String> list = Arrays.asList("SXM", "NAM", "HGN", "YZL", "NGR", "ENX");
    private List<String> listInterval = Arrays.asList("IDD", "KDD", "HNG", "AGR", "LIN", "AZR", "VYZ", "KCR", "VSV");
    
    private List<String> listAllFilesBase;
    private List<String> listAllFilesInterval;

    public ExtNameClass() {
        listAllFilesBase = new ArrayList<String>();
        String baseTitle = LoadData.getPropTitles("basetitles");
        if(baseTitle!=null)
        {
            StringTokenizer st = new StringTokenizer(baseTitle, ",");
            while(st.hasMoreTokens()){
                listAllFilesBase.add(st.nextToken()) ; 
            }
        }
        
        listAllFilesInterval = new ArrayList<String>();
        String intervaltitles = LoadData.getPropTitles("intervaltitles");
        if(intervaltitles!=null)
        {
            StringTokenizer st = new StringTokenizer(intervaltitles, ",");
            while(st.hasMoreTokens()){
                listAllFilesInterval.add(st.nextToken()) ; 
            }
        }
    }

    public List<String> getList() {
        return list;
    }

    public List<String> getListInterval() {
        return listInterval;
    }

    public List<String> getListAllFilesBase() {
        return listAllFilesBase;
    }

    public List<String> getListAllFilesInterval() {
        return listAllFilesInterval;
    }


    
}
