/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui.results;

import helpers.DateHelper;
import javax.swing.JTextArea;

/**
 *
 * @author ivc_LebedevAV
 */
public class UpdateProgressBarTask implements Runnable {
        private javax.swing.JProgressBar progressBar;
        private JTextArea jTextAreaLog;
        private int value;
        private String text;
        public UpdateProgressBarTask(javax.swing.JProgressBar progressBar, boolean progress, JTextArea jTextAreaLog, String text) {
            this.value = progressBar.getValue();
            if(progress)
                this.value++;
            
            
            this.progressBar = progressBar;
            this.jTextAreaLog = jTextAreaLog;
            this.text = text;
        }
        public UpdateProgressBarTask(javax.swing.JProgressBar progressBar, JTextArea jTextAreaLog, String text) {
            this.value = progressBar.getMaximum();
            this.progressBar = progressBar;
            this.jTextAreaLog = jTextAreaLog;
            this.text = text;
        }
        public void run() {
            if ( this.progressBar != null )
            this.progressBar.setValue(value);
            if ( this.jTextAreaLog != null )
            jTextAreaLog.append(DateHelper.CurrentDate("yyyy-MM-dd HH:mm:ss")+" - "+text+"\n");
        }
    }
    
