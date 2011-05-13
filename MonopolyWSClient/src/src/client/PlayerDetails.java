/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package src.client;

/**
 *
 * @author blecherl
 */
public class PlayerDetails {
    private String name;
    private boolean isHuman;
    private boolean isActive;
    private int amount;

    public PlayerDetails(String name, boolean isHuman, boolean isActive, int amount) {
        this.name = name;
        this.isHuman = isHuman;
        this.isActive = isActive;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public boolean isIsHuman() {
        return isHuman;
    }

    public String getName() {
        return name;
    }
}
