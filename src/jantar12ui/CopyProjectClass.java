/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar12ui;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author ivc_LebedevAV
 */
public class CopyProjectClass {

    public static final Logger logger_job = Logger.getLogger(CopyProjectClass.class);

    public CopyProjectClass() {
    }

    private static String[] getFileList(String dir) {

        File f = new File(dir);
        String list[] = f.list();
        return list;
    }

    private synchronized void copyFile(InputStream in, OutputStream out) throws IOException {
        int len = 0, b = 0;
        while ((b = in.read()) != -1) {
            out.write(b);
            len += b;
        }
        in.close();
        out.flush();
        out.close();
    }

    private synchronized void copyFile(String source, String destination) throws FileNotFoundException, IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
        copyFile(in, out);
    }

    private String getFileExtention(String filename) {
        int dotPos = filename.lastIndexOf(".") + 1;
        return filename.substring(dotPos);
    }

    private void delFile(String source) {
        try {
            File f = new File(source);
            f.delete();
        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
        }
    }

    private boolean checkName(String name, Jantar12UI jantar12UI) {
        if (name.length() > 8) {
            JOptionPane.showMessageDialog(jantar12UI, "Длина имени более 8 символов", "Сообщение", 1);
            return false;
        }

        return true;
    }

    public boolean copyProject(Jantar12UI jantar12UI, String name, String descript, ScenClass selectedScenClass, String path) {
        boolean result = true;
        if (checkName(name, jantar12UI)) {
            try {
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path + "BEGIN/PcxAll.dat", true), "Cp1251"));
                pw.print(String.format("%1$-9s", name));
                pw.println(descript);
                pw.close();
            } catch (Exception e) {
                logger_job.log(Level.ERROR, e);
            }
            try {
                String[] listFile = getFileList(path + "Data/");
                for (String s : listFile) {

                    File f = new File(path + "Data/" + s);
                    if (f.isFile()) {
                        if (s.indexOf(selectedScenClass.getName()) >= 0) {
                            copyFile(path + "Data/" + s, path + "Data/" + name + "." + getFileExtention(s));
                        }
                    } else if (f.isDirectory()) {
                        String[] listFileInterval = getFileList(path + "Data/" + s);
                        for (String sI : listFileInterval) {
                            if (sI.indexOf(selectedScenClass.getName()) >= 0) {
                                File fI = new File(path + "Data/" + s + "/" + sI);
                                if (fI.isFile()) {
                                    copyFile(path + "Data/" + s + "/" + sI, path + "Data/" + s + "/" + name + "." + getFileExtention(sI));
                                }
                            }
                        }
                    }

                }
            } catch (Exception e) {
                logger_job.log(Level.ERROR, e);
                result = false;
            }
            return result;
        } else {
            return false;
        }

    }

    public boolean deleteProject(ScenClass selectedScenClass, String path) {
        boolean result = true;
        try {
            List<String> listProjects = new ArrayList<String>();
            File file = new File(path + "BEGIN/PcxAll.dat");
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DataInputStream dis = null;
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() != 0) {
                String a = dis.readLine();
                if (a.indexOf(selectedScenClass.getName()) == -1) {
                    listProjects.add(new String(a.getBytes("ISO-8859-1"), "CP1251"));
                }
            }
            fis.close();
            bis.close();
            dis.close();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path + "BEGIN/PcxAll.dat"), "Cp1251"));
            for (String s : listProjects) {
                pw.println(s);
            }
            pw.close();

            String[] listFile = getFileList(path + "Data/");
            for (String s : listFile) {
                File f = new File(path + "Data/" + s);
                if (f.isFile()) {
                    if (s.indexOf(selectedScenClass.getName()) >= 0) {
                        delFile(path + "Data/" + s);
                    }
                } else if (f.isDirectory()) {
                    String[] listFileInterval = getFileList(path + "Data/" + s);
                    for (String sI : listFileInterval) {
                        if (sI.indexOf(selectedScenClass.getName()) >= 0) {
                            File fI = new File(path + "Data/" + s + "/" + sI);
                            if (fI.isFile()) {
                                delFile(path + "Data/" + s + "/" + sI);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger_job.log(Level.ERROR, e);
            result = false;
        }
        return result;
    }
}
