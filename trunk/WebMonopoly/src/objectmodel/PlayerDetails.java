/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package objectmodel;

/**
 *
 * @author blecherl
 */
public class PlayerDetails {
    private String name;
    private boolean isHuman;
    private boolean isActive;
    private int amount;
    private PlayerColor playerColor;
    private boolean isInGame;
    private boolean isInJail;

    public PlayerDetails() {
        
    }

    public PlayerDetails(String name, boolean isHuman, boolean isActive, int amount, PlayerColor playerColor) {
        this.name = name;
        this.isHuman = isHuman;
        this.isActive = isActive;
        this.amount = amount;
        this.playerColor = playerColor;
        this.isInGame = true;
        this.isInJail = false;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public boolean isInJail() {
        return isInJail;
    }

    public boolean isIsHuman() {
        return isHuman;
    }

    public String getName() {
        return name;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public void setInJail(boolean val) {
        isInJail = val;
    }

    public void AddAmount(int amountPaid) {
        amount += amountPaid;
    }

    public void SubtractAmount(int amountPaid) {
        amount -= amountPaid;
    }

    public void setLost() {
        amount = 0;
        isInGame = false;
        isInJail = false;
    }
}
