/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui.results;

import helpers.NumberHelpers;
import helpers.StringHelper;
import helpers.XLSHelper;
import jantar12ui.LoadData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import paneljantar.NAMClass;
import paneljantar.SXMClass;
import paneljantar.VECClass;

/**
 *
 * @author ivc_LebedevAV
 */
public class CommonDataGod {
    public static final Logger logger = Logger.getLogger(CommonDataGod.class);
    private String projectName;
    private CreateResultsDialog createResultsDialog;
    private List<String> valList = new ArrayList<>();
    private String pathResult = LoadData.getPathJantar12() + "Result/";
    private String prefFileName;
    private String lenghFile="";
    
    public CommonDataGod(String projectName, String prefFileName, CreateResultsDialog createResultsDialog, String resultDir){
        this.createResultsDialog = createResultsDialog;
        this.projectName = projectName;
        lenghFile = pathResult+projectName+".len";
        //lenghFile = pathResult+"Lengh.txt";
        try{
            Workbook w = new XSSFWorkbook(new FileInputStream(LoadData.getPathXlt()+"God.xlsx"));
            writeDate(w);
            FileOutputStream fileOut = new FileOutputStream(resultDir+prefFileName+"_God.xlsx");
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
            return null;
        } catch (IOException e) {logger.log(Level.ERROR, e);return null;
        }
    }
   
    private void writeDate(Workbook w)
    {
        try{
            valList = getContextFile(pathResult+projectName+".god");
            //Sheet sheet = w.getSheet("NGR");
            SXMClass sXMClass = new SXMClass(null);
            int countIB = sXMClass.getIB(projectName+".SXM");
            int countM = sXMClass.getM(projectName+".SXM")+1;
            int countKS = sXMClass.getKS(projectName+".SXM");
            List<String> listVEC = new VECClass(null).getVEC(projectName+".VEC");
            List<String> listNAM = new NAMClass(null).getNAM(projectName+".NAM");
            Sheet sheet;
            int sheetNum=0;
            int rowNumber=0;
            //for(String oneString: valList)
            while(rowNumber<valList.size())
            {
                String oneString = valList.get(rowNumber);
                sheet = w.getSheetAt(sheetNum);
                if(sheetNum==0)
                {
                    int n=0;
                    for (String str : oneString.split("[ ]"))
                    {
                        if(str.trim().length()>0)
                        {
                            if(n<countM-1)
                                XLSHelper.createCell(w, sheet, (n+2), 0, 0, 0, (n+1), true);
                            else
                                XLSHelper.createCell(w, sheet, (n+2), 0, 0, 0, "Система", true);
                            XLSHelper.createCell(w, sheet, (n+2), 1, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                            n++;
                        }
                    }
                    rowNumber++;rowNumber++;
                    int countKScurrent=0;
                    float[][] valYear = new float[countKS][countM+1];
                    if(countKS==12)
                    {
                        while(countKS>countKScurrent)
                        {
                            oneString = valList.get(rowNumber);
                            n=0;
                            for (String str : oneString.split("[ ]"))
                            {
                                if(str.trim().length()>0)
                                {
                                    XLSHelper.createCell(w, sheet, (n+2), (countKScurrent+2), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                    valYear[countKScurrent][n]=NumberHelpers.getFloat(NumberHelpers.addNol(str.trim()));
                                    n++;
                                }
                            }
                            countKScurrent++;
                            rowNumber++;
                        }
                    }
                    if(countKS==4)
                    {
                        int countRcurrent=0;
                        while(countKS>countKScurrent)
                        {
                            oneString = valList.get(rowNumber);
                            n=0;
                            for (String str : oneString.split("[ ]"))
                            {
                                if(str.trim().length()>0)
                                {
                                    XLSHelper.createCell(w, sheet, (n+2), (countRcurrent+2), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                    valYear[countKScurrent][n]=NumberHelpers.getFloat(NumberHelpers.addNol(str.trim()));
                                    n++;
                                }
                            }
                            countRcurrent++;countRcurrent++;countRcurrent++;
                            countKScurrent++;
                            rowNumber++;
                        }
                    }
                    if(countKS==2)
                    {
                        int countRcurrent=0;
                        while(countKS>countKScurrent)
                        {
                            oneString = valList.get(rowNumber);
                            n=0;
                            for (String str : oneString.split("[ ]"))
                            {
                                if(str.trim().length()>0)
                                {
                                    XLSHelper.createCell(w, sheet, (n+2), (countRcurrent+2), 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                    valYear[countKScurrent][n]=NumberHelpers.getFloat(NumberHelpers.addNol(str.trim()));
                                    n++;
                                }
                            }
                            countRcurrent++;countRcurrent++;countRcurrent++;countRcurrent++;countRcurrent++;countRcurrent++;
                            countKScurrent++;
                            rowNumber++;
                        }
                    }
                    if(countKS==1)
                    {
                            oneString = valList.get(rowNumber);
                            n=0;
                            for (String str : oneString.split("[ ]"))
                            {
                                if(str.trim().length()>0)
                                {
                                    XLSHelper.createCell(w, sheet, (n+2), 14, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                    n++;
                                }
                            }
  
                            rowNumber++;

                    }
                    if(countKS!=1)
                    {
                        for(int m=0; m<countM; m++)
                        {
                            float a=0;
                            for(int j=0; j<countKS; j++)
                            {
                                a=a+valYear[j][m];
                            }
                            XLSHelper.createCell(w, sheet, (m+2), 14, 0, 0, a, true);
                        }
                    }
                    sheetNum++;
                    rowNumber++;
                }else if(sheetNum==1)
                {
                    List<String> valListLenght = new ArrayList<>();
                    valListLenght = getContextFile(lenghFile);
                    String exn = valListLenght.get(countKS*2);
                    int countIBcurrent=1;
                    int rr=2;
                    int rM=0;
                    for (String str : exn.split("[ ]"))
                    {
                        if(str.trim().length()>0 && !str.trim().equals("0"))
                        {
                            int countN = NumberHelpers.getInt(str.trim());
                            int k = 0;

                                rM = rr;
 
                            while(k<countN)
                            {
                                
                                oneString = valList.get(rowNumber);
                                int n=1;
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
                }else if(sheetNum==2)
                {
                    List<String> valListLenght = new ArrayList<>();
                    valListLenght = getContextFile(lenghFile);
                    String exn = valListLenght.get(countKS*2+1);
                    int countMcurrent=1;
                    int rr=2;
                    int rM=0;
                    for (String str : exn.split("[ ]"))
                    {
                        if(str.trim().length()>0 && !str.trim().equals("0"))
                        {
                            int countN = NumberHelpers.getInt(str.trim());
                            int k = 0;
                            rM=rr;
                            while(k<countN)
                            {
                                
                                oneString = valList.get(rowNumber);
                                int n=1;
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
                    //rowNumber++;
                }else if(sheetNum==3)
                {
                    int n=0;
                    oneString = valList.get(rowNumber);
                    for (String str : oneString.split("[ ]"))
                    {
                        
                        if(str.trim().length()>0)
                        {
                            XLSHelper.createCell(w, sheet, n, 1, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                            n++;
                        }
                    }
                    sheetNum++;
                    rowNumber++;
                }else if(sheetNum==4)
                {
                    int n=2;
                    int row=4;
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
                    int n=2;
                    int row=2;
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
                }else if(sheetNum==6)
                {
                    int n=1;
                    int row=1;
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
                    XLSHelper.setMerge(sheet, 1, row, 6, row);
                    XLSHelper.createCell(w, sheet, 1, row, 0, 0, "Суммарные потери мощности в системе", true);
                    XLSHelper.setStyleRange(w, sheet, row,row,1,6);
                    oneString = valList.get(rowNumber);
                        for (String str : oneString.split("[ ]"))
                        {
                            if(str.trim().length()>0)
                            {
                                XLSHelper.createCell(w, sheet, 7, row, 0, 0, NumberHelpers.getDouble(NumberHelpers.addNol(str.trim())), true);
                                n++;
                            }
                        }
                    sheetNum++;
                    rowNumber++;
                }else if(sheetNum==7)
                {
                    int n=2;
                    int row=2;
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
                }
                rowNumber++;
                
            }
                
            
            
            
            
            
            }catch(Exception we)
        {
            logger.log(Level.ERROR, we);
            SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),false, this.createResultsDialog.getjTextAreaLog(),we.getMessage()));
        }
    }
    
}
