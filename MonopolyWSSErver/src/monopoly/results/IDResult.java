/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly.results;

/**
 *
 * @author blecherl
 */
public class IDResult extends MonopolyResult{
    private int result;

     public IDResult(int value) {
        super ();
        this.result = value;
    }

    public IDResult(String errorMessage) {
        super (errorMessage);
    }

    public IDResult(boolean hasError, String errorMessage, int value) {
        super (hasError, errorMessage);
        this.result = value;
    }

    public int getResult() {
        return result;
    }

     public static IDResult error(String message) {
        return new IDResult(message);
    }
}