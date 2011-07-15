/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly;

import java.io.*;
/**
 *
 * @author Benda
 */
public class Utils
{
    public static final String ERROR_PLACEHOLDER = "%ERROR_PLACEHOLDER%";

    public static final String ACTION_CREATE = "create";
    public static final String ACTION_JOIN = "join";
    public static final String ACTION_BOARD = "board";
    public static final String ACTION_DICES = "dices";
    public static final String ACTION_BUY = "buy";

    public static String convertStreamToString(InputStream is)
            throws IOException {
        /*
         * To convert the InputStream to String we use the
         * Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to
         * read. We use the StringWriter class to produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }
}
