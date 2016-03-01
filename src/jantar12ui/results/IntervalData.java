/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui.results;

import helpers.NumberHelpers;
import helpers.XLSHelper;
import jantar12ui.LoadData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
//import jxl.Workbook;
//import jxl.write.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import paneljantar.NAMClass;
import paneljantar.SXMClass;
import paneljantar.VECClass;
import paneljantar.interval.AJOClass;
import paneljantar.interval.IDDClass;
import paneljantar.interval.IZLClass;

/**
 *
 * @author ivc_LebedevAV
 */
public class IntervalData {
    public static final Logger logger = Logger.getLogger(IntervalData.class);
    private String projectName;
    private CreateResultsDialog createResultsDialog;
    private String prefFileName;
    private List<String> valList = new ArrayList<>();
    private String pathResultMain = LoadData.getPathJantar12() + "Result/";
    private String pathResult = LoadData.getPathJantar12() + "Result/Interv";
    private String idxInterv;
    private int numInterv;
     private String lenghFile="";

    
    public IntervalData(int numInterv, String idxInterv, String projectName, String prefFileName, CreateResultsDialog createResultsDialog, String resultDir){
        this.createResultsDialog = createResultsDialog;
        this.projectName = projectName;
        pathResult = pathResult+idxInterv+"/";
        lenghFile = pathResultMain+projectName+".len";
        //lenghFile = pathResultMain+"Lengh.txt";
        this.idxInterv = idxInterv;
        this.numInterv = numInterv;
        try{
            Workbook w = new XSSFWorkbook(new FileInputStream(LoadData.getPathXlt()+"Interv.xlsx"));
            writeDate(w);
            FileOutputStream fileOut = new FileOutputStream(resultDir+prefFileName+"_Interv"+idxInterv+".xlsx");
            w.write(fileOut);
            fileOut.close();
        }catch(Exception we)
        {
            logger.log(Level.ERROR, we);
            SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),false, this.createResultsDialog.getjTextAreaLog(),we.getMessage()));
        }
                
        
    }
    private List<String> getContextFile(String fn) {
        List<String> localValList = new ArrayList<>();
        localValList.clear();
        File file = new File(fn);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() != 0) {
                String str = dis.readLine();
                str = new String(str.getBytes("ISO-8859-1"), "CP1251");
                localValList.add(str.trim());
            }
            fis.close();
            bis.close();
            dis.close();
            return localValList;
        } catch (FileNotFoundException e) {
            logger.log(Level.ERROR, e);
            SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),false, this.createResultsDialog.getjTextAreaLog(),e.getMessage()));
            return null;
        } catch (IOException e) {logger.log(Level.ERROR, e);return null;
        }
    }
    
    private void writeDate(Workbook w)
    {
        try{
            
            valList = getContextFile(pathResult+projectName+".ngr");
            SXMClass sXMClass = new SXMClass(null);
            int countIB = sXMClass.getIB(projectName+".SXM");
            int countM = sXMClass.getM(projectName+".SXM")+1;
            int countKS = sXMClass.getKS(projectName+".SXM");
            List<String> listVEC = new VECClass(null).getVEC(projectName+".VEC");
            List<String> listNAM = new NAMClass(null).getNAM(projectName+".NAM");
            Sheet sheet;
            int rowNumber=0;

                String oneString = valList.get(rowNumber);
                sheet = w.getSheetAt(0);
                    int n=0;
                    int row=2;
                    int idd = new IDDClass(null).getID("Interv"+idxInterv+"/"+projectName+".IDD");
                    for(int id=1;id<=idd;id++)
                    {
                    for(int i=0;i<countM-1;i++)
                    {

                        oneString = valList.get(rowNumber);
                        for (String str : oneString.split("[ ]"))
                        {
                            if(str.trim().length()>0)
                            {
                                XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                n++;
                            }
                        }
                        row++;
                        n=0;
                        rowNumber++;
                    }
                    XLSHelper.setMerge(sheet, 0, row, 3, row);
                    XLSHelper.createCell(w, sheet, 0, row, 0, 0, "Система", true);
                    XLSHelper.setStyleRange(w, sheet, row,row,1,3);
                    n=4;
                    oneString = valList.get(rowNumber);
                    for (String str : oneString.split("[ ]"))
                    {
                        if(str.trim().length()>0)
                        {
                           XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                           n++;
                        }
                    }
                    rowNumber++;rowNumber++;
                    row++;row++;
                    n=0;
                    }
                    
                    
                    //REM
                    rowNumber=0;
                    valList = getContextFile(pathResult+projectName+".rem");
                    sheet = w.getSheetAt(1);
                    int maxAJO = new AJOClass().getMaxAJO("Interv"+idxInterv+"/"+projectName+".AJO");
                    n=4;
                    for(int i=1;i<=maxAJO;i++)
                    {
                        XLSHelper.createCell(w, sheet, n, 2, 2, 0, i, true);
                        n++;n++;
                    }
                    row=3;
                    n=1;
                    for(int i=0;i<countM-1;i++)
                    {
                        XLSHelper.createCell(w, sheet, 0, row, 0, 0, (i+1), true);
                        oneString = valList.get(rowNumber);
                        for (String str : oneString.split("[ ]"))
                        {
                            if(str.trim().length()>0)
                            {
                                XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                n++;
                            }
                        }
                        row++;
                        n=1;
                        rowNumber++;
                    }
                    XLSHelper.createCell(w, sheet, 0, row, 0, 0, "Система", true);
                    n=1;
                    oneString = valList.get(rowNumber);
                    for (String str : oneString.split("[ ]"))
                    {
                        if(str.trim().length()>0)
                        {
                           XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                           n++;
                        }
                    }
                    n=4;
                    for(int i=1;i<=maxAJO;i++){
                        XLSHelper.setMerge(sheet, n, 2, n+1, 2);
                        XLSHelper.setStyleRange(w, sheet, 2,2,n,n+1);
                        
                        n++;n++;
                    }
                    XLSHelper.setStyleRange(w, sheet, 3,row-1,4,maxAJO*2+3);
                    
                    //ehn
            int sheetNum=2;
            rowNumber=0;
            valList = getContextFile(pathResult+projectName+".rez");
            while(rowNumber<valList.size())
            {
                sheet = w.getSheetAt(sheetNum);
                if(sheetNum==2)
                {
                    List<String> valListLenght = new ArrayList<>();
                    valListLenght = getContextFile(lenghFile);
                    String exn = valListLenght.get((numInterv-1)*2);
                    int countIBcurrent=1;
                    int rr=2;
                    for (String str : exn.split("[ ]"))
                    {
                        if(str.trim().length()>0 && !str.trim().equals("0"))
                        {
                            int countN = NumberHelpers.getInt(str.trim());
                            int k = 0;
                            int rM = rr;
                            while(k<countN)
                            {
                                
                                oneString = valList.get(rowNumber);
                                n=1;
                                for (String str2 : oneString.split("[ ]"))
                                {
                                    if(str2.trim().length()>0)
                                    {
                                        XLSHelper.createCell(w, sheet, n, rr, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2.trim())), true);
                                        n++;
                                    }
                                }
                                rr++;
                                rowNumber++;
                                k++;
                            }
                            XLSHelper.setMerge( sheet, 0, rM, 0, rr-1);
                            XLSHelper.createCell(w, sheet, 0, rM, 0, 0, countIBcurrent, true);
                            XLSHelper.setStyleRange(w, sheet, rM,rr-1,0,0);
                            countIBcurrent++;
                        }
                        if(str.trim().equals("0")) 
                        {
                            XLSHelper.createCell(w, sheet, 0, rr, 0, 0, countIBcurrent, true);
                            XLSHelper.createCell(w, sheet, 1, rr, 0, 0, "Вероятности загрузки связей меньше заданного ограничения 0,0000001, т.е., ЭНХ не выводятся", true);
                            rowNumber++;
                            rr++;
                            countIBcurrent++;
                        }
                    }
                    sheetNum++;
                }else if(sheetNum==3)
                {
                    List<String> valListLenght = new ArrayList<>();
                    valListLenght = getContextFile(lenghFile);
                    String exn = valListLenght.get((numInterv-1)*2+1);
                    int countMcurrent=1;
                    int rr=2;
                    for (String str : exn.split("[ ]"))
                    {
                        if(str.trim().length()>0 && !str.trim().equals("0"))
                        {
                            int countN = NumberHelpers.getInt(str.trim());
                            int k = 0;
                            int rM = rr;
                            while(k<countN)
                            {
                                
                                oneString = valList.get(rowNumber);
                                n=1;
                                for (String str2 : oneString.split("[ ]"))
                                {
                                    if(str2.trim().length()>0)
                                    {
                                        XLSHelper.createCell(w, sheet, n, rr, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str2.trim())), true);
                                        n++;
                                    }
                                }
                                rr++;
                                rowNumber++;
                                k++;
                            }
                            XLSHelper.setMerge(sheet, 0, rM, 0, rr-1);
                            if(countMcurrent<countM)
                                XLSHelper.createCell(w, sheet, 0, rM, 0, 0, countMcurrent, true);
                            else
                                XLSHelper.createCell(w, sheet, 0, rM, 0, 0, "Система", true);
                            XLSHelper.setStyleRange(w, sheet, rM,rr-1,0,0);
                            countMcurrent++;
                        }
                    }
                    sheetNum++;
                }else if(sheetNum==4)
                {
                    n=2;
                    row=2;
                    for(int i=0;i<countM;i++)
                    {
                        XLSHelper.createCell(w, sheet, 0, row, 0, 0, (i+1), true);
                        if(i<countM-1)
                            XLSHelper.createCell(w, sheet, 1, row, 0, 0, listNAM.get(i), true);
                        else
                            XLSHelper.createCell(w, sheet, 1, row, 0, 0, "Система", true);
                        oneString = valList.get(rowNumber);
                        for (String str : oneString.split("[ ]"))
                        {
                            if(str.trim().length()>0)
                            {
                                XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                n++;
                            }
                        }
                        row++;
                        n=2;
                        rowNumber++;
                    }
                    sheetNum++;
                }else if(sheetNum==5)
                {
                    n=1;
                    row=2;
                    for(int i=0;i<countIB;i++)
                    {
                        XLSHelper.createCell(w, sheet, 0, row, 0, 0, (i+1), true);
                        for (String str : listVEC.get(i).split("[ ]"))
                        {
                            XLSHelper.createCell(w, sheet, n, row, 0, 0, listNAM.get(NumberHelpers.getInt(str.trim())-1), true);
                            n++;
                        }

                               
                        oneString = valList.get(rowNumber);
                        for (String str : oneString.split("[ ]"))
                        {
                            if(str.trim().length()>0)
                            {
                                XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                n++;
                            }
                        }
                        row++;
                        n=1;
                        rowNumber++;
                    }
                    sheetNum++;
                    rowNumber++;
                }
                rowNumber++;
                
            }
//rrm
            sheetNum=6;
            rowNumber=0;
            valList = getContextFile(pathResult+projectName+".rrm");
            while(rowNumber<valList.size())
            {
                sheet = w.getSheetAt(sheetNum);
                if(sheetNum==6)
                {
                    int maxN=0;
                    n=1;
                    row=2;
                    for(int i=0;i<countM-1;i++)
                    {
                        XLSHelper.createCell(w, sheet, 0, row, 0, 0, (i+1), true);
                        oneString = valList.get(rowNumber);
                        int maxNC=0;
                        for (String str : oneString.split("[ ]"))
                        {
                            if(str.trim().length()>0)
                            {
                                XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                n++;
                                maxNC++;
                            }
                        }
                        if(maxN<maxNC) maxN=maxNC;
                        row++;
                        n=1;
                        rowNumber++;
                    }
                    XLSHelper.setStyleRange(w, sheet, 2,row-1,3,maxN);
                    row=1;
                    n=3;
                    for(int j=1;j<=maxN-2;j++)
                    {
                        XLSHelper.createCell(w, sheet, n, row, 0, 0, j, true);
                        n++;
                    }
                    
                    sheetNum++;
                }else if(sheetNum==7)
                {
                    int maxN=0;
                    n=1;
                    row=2;
                    for(int i=0;i<countIB;i++)
                    {
                        XLSHelper.createCell(w, sheet, 0, row, 0, 0, (i+1), true);
                        oneString = valList.get(rowNumber);
                        int maxNC=0;
                        for (String str : oneString.split("[ ]"))
                        {
                            if(str.trim().length()>0)
                            {
                                XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                n++;
                                maxNC++;
                            }
                        }
                        if(maxN<maxNC) maxN=maxNC;
                        row++;
                        n=1;
                        rowNumber++;
                    }
                    XLSHelper.setStyleRange(w, sheet, 2,row-1,2,maxN);
                    row=1;
                    n=2;
                    for(int j=1;j<=maxN-1;j++)
                    {
                        XLSHelper.createCell(w, sheet, n, row, 0, 0, j, true);
                        n++;
                    }
                    sheetNum++;
                }
                rowNumber++;
                
            }
//wgs
            sheetNum=8;
            rowNumber=0;
            valList = getContextFile(pathResult+projectName+".wgs");
            while(rowNumber<valList.size())
            {
                sheet = w.getSheetAt(sheetNum);
                if(sheetNum==8)
                {
                    row=2;
                    for(int i=0;i<countM-1;i++)
                    {
                        /*XLSHelper.createCell(w, sheet, 0, row, 0, 0, (i+1), true);
                        XLSHelper.createCell(w, sheet, 1, row, 0, 0, listNAM.get(i), true);*/
                        int rM = row;
                        
                        int countAJO = new AJOClass().getValueIndex("Interv"+idxInterv+"/"+projectName+".AJO", i);
                        for(int j=0; j<countAJO;j++)
                        {
                            n=3;
                            XLSHelper.createCell(w, sheet, 2, row, 0, 0, (j+1), true);
                            oneString = valList.get(rowNumber);
                            for (String str : oneString.split("[ ]"))
                            {
                                if(str.trim().length()>0)
                                {
                                    XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                    n++;
                                }
                            }
                            row++;
                            rowNumber++;
                        }
                        XLSHelper.setMerge(sheet, 0, rM, 0, row-1);
                        XLSHelper.setMerge(sheet, 1, rM, 1, row-1);
                        XLSHelper.createCell(w, sheet, 0, rM, 0, 0, (i+1), true);
                        XLSHelper.createCell(w, sheet, 1, rM, 0, 0, listNAM.get(i), true);
                        XLSHelper.setStyleRange(w, sheet, rM,row-1,0,0);
                        XLSHelper.setStyleRange(w, sheet, rM,row-1,1,1);

                        XLSHelper.setMerge(sheet, 0, row, 1, row);
                        XLSHelper.createCell(w, sheet, 0, row, 0, 0, "Суммарное количество и мощность по узлу", true);
                        XLSHelper.setStyleRange(w, sheet, row,row,0,1);
                        oneString = valList.get(rowNumber);
                        n=4;
                        for (String str : oneString.split("[ ]"))
                            {
                                if(str.trim().length()>0)
                                {
                                    XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                    n++;
                                }
                            }
                            row++;
                            rowNumber++;
                    }
                        XLSHelper.setMerge(sheet, 0, row, 1, row);
                        XLSHelper.createCell(w, sheet, 0, row, 0, 0, "Суммарная мощность по системе", true);
                        XLSHelper.setStyleRange(w, sheet, row,row,0,1);
                        
                    
                        oneString = valList.get(rowNumber);
                        n=4;
                        for (String str : oneString.split("[ ]"))
                            {
                                if(str.trim().length()>0)
                                {
                                    XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                    n++;
                                }
                            }
                            row++;
                            rowNumber++;    
                    sheetNum++;
                }else if(sheetNum==9)
                {
                    row=3;
                    for(int i=0;i<countIB;i++)
                    {


                        int rM = row;
                        
                        int countIZL = new IZLClass().getValueIndex("Interv"+idxInterv+"/"+projectName+".IZL", i);
                        for(int j=0; j<countIZL;j++)
                        {
                            n=5;
                            //XLSHelper.createCell(w, sheet, 1, row, 0, 0, (j+1), true);
                            oneString = valList.get(rowNumber);
                            for (String str : oneString.split("[ ]"))
                            {
                                if(str.trim().length()>0)
                                {
                                    XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                    n++;
                                }
                            }
                            row++;
                            rowNumber++;
                        }
                        
                        XLSHelper.setMerge(sheet, 0, rM, 0, row-1);
                        XLSHelper.createCell(w, sheet, 0, rM, 0, 0, (i+1), true);
                        
                        n=1;
                        for (String str : listVEC.get(i).split("[ ]"))
                        {
                            XLSHelper.setMerge(sheet, n, rM, n, row-1);
                            XLSHelper.createCell(w, sheet, n, rM, 0, 0, str.trim(), true);
                            n++;
                            XLSHelper.setMerge(sheet, n, rM, n, row-1);
                            XLSHelper.createCell(w, sheet, n, rM, 0, 0, listNAM.get(NumberHelpers.getInt(str.trim())-1), true);
                            n++;
                        }                        
                        XLSHelper.setStyleRange(w, sheet, rM,row-1,0,4);
                        
                        
                        XLSHelper.setMerge(sheet, 0, row, 4, row);
                        XLSHelper.createCell(w, sheet, 0, row, 0, 0, "Суммарная пропускная способность", true);
                        XLSHelper.setStyleRange(w, sheet, row,row,0,4);
                        oneString = valList.get(rowNumber);
                        n=5;
                        for (String str : oneString.split("[ ]"))
                            {
                                if(str.trim().length()>0)
                                {
                                    XLSHelper.createCell(w, sheet, n, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                    n++;
                                }
                            }
                            row++;
                            rowNumber++;
                    }
  
                    sheetNum++;
                }
                rowNumber++;
                
            }
            
            
            /*Sheet sheet = w.getSheet("NGR");
            XLSHelper.copyPasteRange(w, sheet, 0, 0, 2, 2, 0, 15);
            
            
            
            int[] idx = {3,6,10};
            int rown=0;
            int ii=0;

   XLSHelper.insertCell(w, sheet, 0, (rown+idx[0]), 0, 0, 11);
   XLSHelper.createCell(w, sheet, 1, (rown+idx[0]), 0, 0, "Название 11");
   rown++;ii++;
   XLSHelper.insertCell(w, sheet, 0, (rown+idx[0]), 0, 0, 22);
   XLSHelper.createCell(w, sheet, 1, (rown+idx[0]), 0, 0, "Название 21");
   ii++;

   idx[1]=idx[1]+ii;
   rown=0;
   XLSHelper.insertCell(w, sheet, 0, (rown+idx[1]), 0, 0, 1);
   XLSHelper.createCell(w, sheet, 1, (rown+idx[1]), 0, 0, "Название 1");
   ii++;
   rown++;
   XLSHelper.insertCell(w, sheet, 0, (rown+idx[1]), 0, 0, 2);
   XLSHelper.createCell(w, sheet, 1, (rown+idx[1]), 0, 0, "Название 2");
   ii++;
   idx[2]=idx[2]+ii;
   rown=0;
   XLSHelper.insertCell(w, sheet, 0, (rown+idx[2]), 0, 0, 111);
   XLSHelper.createCell(w, sheet, 1, (rown+idx[2]), 0, 0, "Название 11111");
   ii++;
   rown++;
   XLSHelper.insertCell(w, sheet, 0, (rown+idx[2]), 0, 0, 2222);
   XLSHelper.createCell(w, sheet, 1, (rown+idx[2]), 0, 0, "Название 22222");
   ii++;

*/
            
            
            
            
            }catch(Exception we)
        {
            logger.log(Level.ERROR, we);
            SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),false, this.createResultsDialog.getjTextAreaLog(),we.getMessage()));
        }
    }
}
