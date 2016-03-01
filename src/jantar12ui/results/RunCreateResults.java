/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui.results;

import jantar12ui.LoadData;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import paneljantar.SXMClass;

/**
 *
 * @author ivc_LebedevAV
 */
public class RunCreateResults extends Thread {
    public static final Logger logger = Logger.getLogger(RunCreateResults.class);
    private String projectName;
    private CreateResultsDialog createResultsDialog;
    private String prefFileName;
    private boolean stop=false;
    private String resultDir;
    
    public RunCreateResults(String projectName, String prefFileName, CreateResultsDialog createResultsDialog, String resultDir) {
        this.createResultsDialog = createResultsDialog;
        this.projectName = projectName;
        this.prefFileName = prefFileName;
        this.resultDir = resultDir;
    }
    public void setStop()
    {
        stop = true;
    }
    public void run() {
        this.createResultsDialog.getjButtonCancel().setEnabled(false);
        this.createResultsDialog.getjButtonStart().setText("Стоп");
        this.createResultsDialog.setStart(true);
        SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),false, this.createResultsDialog.getjTextAreaLog(),"старт заполнения CommonData"));
            CommonData commonData = new CommonData(this.projectName, this.prefFileName, createResultsDialog, resultDir);
        SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),true, this.createResultsDialog.getjTextAreaLog(),"CommonData заполнен"));

        if(!stop){
            SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),true, this.createResultsDialog.getjTextAreaLog(),"старт заполнения Годовых результатов"));
            CommonDataGod commonDataGod=null;
            commonDataGod = new CommonDataGod(this.projectName, this.prefFileName, createResultsDialog, resultDir);
            SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),true, this.createResultsDialog.getjTextAreaLog(),"Годовые результаты заполнен"));
        }
        int countKS = new SXMClass(null).getKS(projectName+".SXM");
        IntervalData intervalData = null;
        for(int i=1; i<=countKS; i++)
        {
            if(!stop){
                String idx=Integer.toString(i);
                if(i<10) idx="0"+i;
                SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),true, this.createResultsDialog.getjTextAreaLog(),"старт заполнения Interval"+idx));
                intervalData = new IntervalData(i, idx, this.projectName, this.prefFileName, createResultsDialog, resultDir);
                SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),true, this.createResultsDialog.getjTextAreaLog(),"Interval"+idx+" заполнен"));
            }

        }

        
        
        //all
        SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(), this.createResultsDialog.getjTextAreaLog(),"Результаты сохранены"));
        this.createResultsDialog.getjButtonCancel().setEnabled(true);
        this.createResultsDialog.getjButtonStart().setText("Старт");
        this.createResultsDialog.setStart(false);
        
    }
    @Override
    public void interrupt()
    {
        SwingUtilities.invokeLater(new UpdateProgressBarTask(this.createResultsDialog.getjProgressBar1(),this.createResultsDialog.getjTextAreaLog(),"Процесс остановлен!"));
        this.createResultsDialog.getjButtonCancel().setEnabled(true);
        this.createResultsDialog.getjButtonStart().setText("Старт");
        this.createResultsDialog.setStart(false);
        super.interrupt();
        
    }
    
    
}
