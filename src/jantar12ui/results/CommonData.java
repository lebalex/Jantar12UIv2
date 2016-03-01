/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui.results;

import helpers.NumberHelpers;
import helpers.XLSHelper;
import jantar12ui.LoadData;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.SwingUtilities;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import paneljantar.*;

/**
 *
 * @author ivc_LebedevAV
 */
public class CommonData{
    public static final Logger logger = Logger.getLogger(CommonData.class);
    private String projectName;
    private CreateResultsDialog createResultsDialog;
    private String prefFileName;

    public CommonData(String projectName, String prefFileName, CreateResultsDialog createResultsDialog, String resultDir){
        this.createResultsDialog = createResultsDialog;
        this.projectName = projectName;
        try{
            Workbook w = new XSSFWorkbook(new FileInputStream(LoadData.getPathXlt()+"Sistem.xlsx"));
            writeDate(w);
            FileOutputStream fileOut = new FileOutputStream(resultDir+prefFileName+"_Sistem.xlsx");
            w.write(fileOut);
            fileOut.close();
        }catch(Exception we)
        {
            logger.log(Level.ERROR, we);
            SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),false, this.createResultsDialog.getjTextAreaLog(),we.getMessage()));
        }
    }
          
    private void writeDate(Workbook w)
    {
        try{
            
            
            Sheet sheet = w.getSheetAt(0);
            //jxl.write.Number(col - от 0, row - от 0
            SXMClass sXMClass = new SXMClass(null);
            int countM = sXMClass.getM(projectName+".SXM");
            int countGraph = sXMClass.getIA(projectName+".SXM");
            int countKS = sXMClass.getKS(projectName+".SXM");
            int countIB = sXMClass.getIB(projectName+".SXM");
            XLSHelper.createCell(w, sheet, 1, 0, 0, 0, countM, true);
            XLSHelper.createCell(w, sheet, 1, 1, 0, 0, countIB, true);
            XLSHelper.createCell(w, sheet, 1, 2, 0, 0, countKS, true);
            XLSHelper.createCell(w, sheet, 1, 3, 0, 0, sXMClass.getIA(projectName+".SXM"), true);
            XLSHelper.createCell(w, sheet, 1, 4, 0, 0, sXMClass.getKSR(projectName+".SXM"), true);
            XLSHelper.createCell(w, sheet, 1, 5, 0, 0, sXMClass.getPRK(projectName+".SXM"), true);
            XLSHelper.createCell(w, sheet, 1, 6, 0, 0, sXMClass.getKRIT(projectName+".SXM"), true);
            XLSHelper.createCell(w, sheet, 1, 7, 0, 0, sXMClass.getERN(projectName+".SXM"), true);

            sheet = w.getSheetAt(1);
            List<String> listNAM = new NAMClass(null).getNAM(projectName+".NAM");
            int i=1;
            for (String string : listNAM)
            {
                XLSHelper.createCell(w, sheet, 0, i+1, 0, 0, i, true);
                XLSHelper.createCell(w, sheet, 1, i+1, 0, 0, string, true);
                i++;
            }
            
            sheet = w.getSheetAt(2);
            List<String> valuesString = new YZLClass(null).getValues(projectName+".HGN");
            
            i=0;
            for(String str: valuesString)
            {
                if(str.trim().length()>0){
                int j=0;
                if(countGraph>i){
                    XLSHelper.createCell(w, sheet, 0, (i+3), 0, 0, (i + 1));
                    for (String string : str.split("[ ]"))
                    {
                        XLSHelper.createCell(w, sheet, (j+1), (i+3), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(string.trim())), true);
                        j++;
                    }
                }
                i++;
                }
            }
            //YZL
            sheet = w.getSheetAt(3);
            valuesString = new YZLClass(null).getValues(projectName+".YZL");
            listNAM = new NAMClass(null).getNAM(projectName+".NAM");
            i=0;
            float sum=0;
            for (String string : valuesString) {
                if (i < (countM)) {
                    int j = 1;
                    String[] strSplit = string.trim().split("[ ]");
                    if (strSplit.length > 1) {
                        for (String str2 : strSplit) {
                            if (str2.trim().length() > 0) {
                                if (j == 1) {
                                    int namIdx = i;
                                    if (namIdx >= 0 && namIdx < listNAM.size()) {
                                        XLSHelper.createCell(w, sheet, 0, (i+3), 0, 0, (i + 1));
                                        XLSHelper.createCell(w, sheet, 1, (i+3), 0, 0, listNAM.get(namIdx));
                                    } else {
                                        XLSHelper.createCell(w, sheet, 1, (i+3), 0, 0, "ошибка");
                                    }
                                }
                                if(j==2)
                                {
                                    sum+=NumberHelpers.getDouble(NumberHelpers.addNol(str2));
                                    XLSHelper.createCell(w, sheet, (j + 1), (i+3), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2)));
                                }else if(j==3){
                                    XLSHelper.createCell(w, sheet, (j + 1), (i+3), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2)));
                                }else if(j==4)
                                {
                                    XLSHelper.createCell(w, sheet, 5, (i+3), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2)));
                                    //XLSHelper.createCell(w, sheet, 7, (i+3), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2)));
                                }else if(j==5)
                                {
                                    XLSHelper.createCell(w, sheet, 6, (i+3), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2)));
                                    //XLSHelper.createCell(w, sheet, 5, (i+3), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2)));
                                    
                                }else if(j==6)
                                {
                                    XLSHelper.createCell(w, sheet, 7, (i+3), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2)));
                                    //XLSHelper.createCell(w, sheet, 6, (i+3), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2)));
                                    
                                }else
                                {
                                    XLSHelper.createCell(w, sheet, (j + 1), (i+3), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2)));
                                    
                                }
                                j++;
                            }
                        }
                        i++;
                    }
                } else {
                    XLSHelper.createCell(w, sheet, 0, (i+3), 0, 0, "");
                    XLSHelper.createCell(w, sheet, 1, (i+3), 0, 0, "Система");
                    XLSHelper.createCell(w, sheet, 2, (i+3), 0, 0, "");
                    XLSHelper.createCell(w, sheet, 3, (i+3), 0, 0, sum);
                    XLSHelper.createCell(w, sheet, 4, (i+3), 0, 0, "");
                    XLSHelper.createCell(w, sheet, 5, (i+3), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(string.trim())));
                    XLSHelper.createCell(w, sheet, 6, (i+3), 0, 0, "");
                    XLSHelper.createCell(w, sheet, 7, (i+3), 0, 0, "");
                    
                }
            }
            
            
            //NGR
            sheet = w.getSheetAt(4);
            NGRClass nGRClass = new NGRClass(null);
            valuesString = nGRClass.getValues(projectName+".NGR");
            i=2;
            for (String string : nGRClass.getTiitles(countKS))
            {
                XLSHelper.createCell(w, sheet, i, 1, 0, 0, LoadData.getDescript(string));
                i++;
            }

            i=0;
            int k=0;
            while(i<valuesString.size())
            {
                if(k<countM){
                    XLSHelper.createCell(w, sheet, 0, (k+2), 0, 0, (k + 1));
                    XLSHelper.createCell(w, sheet, 1, (k+2), 0, 0, listNAM.get(k));
                    int j=1;
                    while(j<countKS+1)
                    {
                        if(i<valuesString.size())
                        {
                            XLSHelper.createCell(w, sheet, (j+1), (k+2), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(valuesString.get(i))));
                            i++;
                        }
                        j++;
                    }
                    k++;
                }else i++;
            }
  
            //ENX
            sheet = w.getSheetAt(5);
            ENXClass eNXClass = new ENXClass(null);
            valuesString = eNXClass.getValues(projectName+".ENX");
            List<String> listVEC = new VECClass(null).getVEC(projectName+".VEC");
            i=0;
            for(k=0; k<listVEC.size();k++)
            {
                if(((String)listVEC.get(k)).trim().length()>0){
                    int j=0;
                    XLSHelper.createCell(w, sheet, j, (k+2), 0, 0, (k + 1));
                for (String str2 : listVEC.get(k).split("[ ]"))
                {
                    XLSHelper.createCell(w, sheet, (j+1), (k+2), 0, 0, NumberHelpers.getInt(str2));
                    int namIdx=0;
                        try{namIdx=Integer.parseInt(str2.trim());}catch(Exception e){}
                        if(namIdx>0 && namIdx<=listNAM.size()){
                            XLSHelper.createCell(w, sheet, (j+2), (k+2), 0, 0, listNAM.get(namIdx-1));
                            }
                        else{
                            XLSHelper.createCell(w, sheet, (j+2), (k+2), 0, 0, "ошибка");

                        }
                    j++;j++;
                }
            }
            }



            i=0;
            for (String string : valuesString)
            {
                if(i<countIB){
                int j=5;
                String[] strSplit = string.trim().split("[ ]");
                if(strSplit.length>1)
                {
                    for (String str2 : strSplit) {
                        if(str2.trim().length()>0){
                            XLSHelper.createCell(w, sheet, j, (i+2), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2)));
                            j++;
                        }
                    }
                i++;
                }
                }
            }

        }catch(Exception we)
        {
            logger.log(Level.ERROR, we);
            SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),false, this.createResultsDialog.getjTextAreaLog(),we.getMessage()));
        }
    }
    
}
