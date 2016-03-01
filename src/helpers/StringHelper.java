/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package helpers;

/**
 *
 * @author scherb
 */
public class StringHelper {

    /**
     *
     * @param source
     * @param toReplace
     * @param replacement
     * @return
     */
    public static String replaceAll(String source, String toReplace, String replacement) {
        if (source!=null)
        {
        int idx = source.lastIndexOf(toReplace);
        if (idx != -1) {
            StringBuilder ret = new StringBuilder(source);
            ret.replace(idx, idx + toReplace.length(), replacement);
            while ((idx = source.lastIndexOf(toReplace, idx - 1)) != -1) {
                ret.replace(idx, idx + toReplace.length(), replacement);
            }
            source = ret.toString();
        }
        }
        return source;
    }

    /**
     * 
     * @param a
     * @return
     */
    public static String getStringFromNumber(int a) {
        if (a < 10 && a >= 0) {
            return "0" + a;
        } else {
            return a + "";
        }
    }

    /**
     * 
     * @param a
     * @return
     */
    public static String getStringFromNumber(long a) {
        if (a < 10 && a >= 0) {
            return "0" + a;
        } else {
            return a + "";
        }
    }

  
}
