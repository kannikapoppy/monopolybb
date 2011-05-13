package src.client.ui;

import src.client.ui.initgame.GamesPanel;
import src.client.ui.utils.ExamplesUtils;

/**
 *
 * @author blecherl
 */
public class JoinGameExample {

    public static void main (String[] args) {
        ExamplesUtils.setNativeLookAndFeel();
        ExamplesUtils.showExample("Join Game", new GamesPanel());
    }
}