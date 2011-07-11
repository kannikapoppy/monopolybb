package src.monopolyUI;

import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;

import src.client.Server;
import src.services.Utils;

/**
 * this class is the menu of the glorious game
 * @author Benda & Eizenman
 *
 */
public class MonopolyMenu extends JMenuBar 
	{
	
	private static final String FILE_STR = "File";
	private static final String NEW_GAME_STR = "Start/Join Game";
	private static final String EXIT_STR = "Exit";
	private static final String GAME_IN_PROGRESS_ERROR_MSG = "A game is in progress and the server supports only one conccurent game.";

    public MonopolyMenu() throws HeadlessException {
    	final JComponent parentComponenet = this;

        JMenu fileMenu = new JMenu(FILE_STR);
        this.add (fileMenu);

        final JMenuItem newGameMenu = new JMenuItem(NEW_GAME_STR);
        fileMenu.add(newGameMenu);
        newGameMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<String> waitingGames;
                try {
                    waitingGames = Server.getInstance().getWaitingGames();
                } catch (Exception ex) {
                    Utils.ShowError(parentComponenet, "An error occured while connecting to the server.");
                    return;
                }

                if (waitingGames.isEmpty())
                {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run()
                    {
                        boolean areThereRunningGames;
                        try {
                            areThereRunningGames = !Server.getInstance().getActiveGames().isEmpty();
                        } catch (Exception ex) {
                            Utils.ShowError(parentComponenet, "An error occured while connecting to the server.");
                            return;
                        }

                        if (areThereRunningGames)
                        {
                            Utils.ShowError(parentComponenet, GAME_IN_PROGRESS_ERROR_MSG);
                            return;
                        }

                        String gameName = JOptionPane.showInputDialog(null,
                            "Currenly No Game Exists. Please Provide A Name For The New Game",
                            "Create New Game",
                            JOptionPane.QUESTION_MESSAGE);

                        while (true)
                        {
                            if (gameName == null)
                                return;

                            if (gameName.trim().isEmpty())
                                gameName = JOptionPane.showInputDialog(null,
                                    "Empty Name Is Not Valid. Please Provide A Name For The New Game",
                                    "Create New Game",
                                    JOptionPane.QUESTION_MESSAGE);
                            else
                                break;
                        }
                        
                        String humanPlayersStr = JOptionPane.showInputDialog(null,
                            "Please Insert The Number Of Human Players",
                            "Human Players",
                            JOptionPane.QUESTION_MESSAGE);

                        if (humanPlayersStr == null)
                            return;

                        int humanPlayers = 0;

                        while (true)
                        {
                            try
                            {
                                humanPlayers = Integer.parseInt(humanPlayersStr);
                                if (humanPlayers > 0 && humanPlayers <= 6)
                                {
                                    break;
                                }
                            }
                            catch (NumberFormatException ex) {}

                            humanPlayersStr = JOptionPane.showInputDialog(null,
                            "Invalid Input. Try Again. Please Insert The Number Of Human Players",
                            "Human Players",
                            JOptionPane.QUESTION_MESSAGE);

                            if (humanPlayersStr == null)
                            return;
                        }

                        int machinePlayers = 0;

                        if (humanPlayers == 6)
                            machinePlayers = 0;
                        else
                        {
                            String machinePlayersStr = JOptionPane.showInputDialog(null,
                                "Please Insert The Number Of Machine Players",
                                "Machine Players",
                                JOptionPane.QUESTION_MESSAGE);

                            if (machinePlayersStr == null)
                                return;

                            while (true)
                            {
                                try
                                {
                                    machinePlayers = Integer.parseInt(machinePlayersStr);
                                    if ((machinePlayers + humanPlayers) >= 2 && (machinePlayers + humanPlayers) <= 6)
                                    {
                                        break;
                                    }
                                }
                                catch (NumberFormatException ex){}

                                machinePlayersStr = JOptionPane.showInputDialog(null,
                                "Invalid Input. Try Again. Please Insert The Number Of Machine Players",
                                "Machine Players",
                                JOptionPane.QUESTION_MESSAGE);

                                if (machinePlayersStr == null)
                                    return;
                            }
                        }
                        boolean automateRollDice = JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
                            "Would You Like To Automate Dice Roll ?",
                            "Automate Dice Roll", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                        boolean success;

                        try {
                             success = Server.getInstance().startNewGame(gameName, humanPlayers,
                                machinePlayers, automateRollDice);
                        } catch (Exception ex) {
                            Utils.ShowError(parentComponenet, "An error occured while connecting to the server.");
                            return;
                        }


                        if (!success)
                        {
                            // show error message and suggest to try again
                            Utils.ShowError(parentComponenet, "Something went wrong. It's possible that a game has already been created. Try Again.");
                        }
                        else
                        {
                            PromptForUserName(gameName, newGameMenu);
                            // show message that updates and states how many people are missing
                        }

                        }
                    });
                }
                else
                {
                    // show join game window
                    final String currentGame = waitingGames.get(0);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run()
                        {
                            PromptForUserName(currentGame, newGameMenu);
                        }
                    });
                }
            }
        });
				
        // add the exit menu item
        JMenuItem exitMenuItem = new JMenuItem(EXIT_STR);
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
    }

    private void PromptForUserName(String gameName, JMenuItem newGameMenu)
    {
        String playerName = JOptionPane.showInputDialog(null, "Please Provide User Name To Join The Game '" + gameName + "'", "Join Game",
                JOptionPane.QUESTION_MESSAGE);
        
        while (true)
        {
            if (playerName == null)
                return;
            
            if (playerName.trim().isEmpty())
                playerName = JOptionPane.showInputDialog(null, "Empty Name Is Not Valid. Please Provide User Name To Join The Game '" + gameName + "'", "Join Game", 
                        JOptionPane.QUESTION_MESSAGE);
            else
                break;
        }
        int playerId;

        try {
            playerId = Server.getInstance().joinPlayer(gameName, playerName);
        } catch (Exception ex) {
            Utils.ShowError(null, "An error occured while connecting to the server.");
            return;
        }

        if (playerId == -1)
        {
            // error occured, show message to try again with a different name
            Utils.ShowError(this, "Something went wrong. Try Again.");
        } 
        else
        {
            // timer to show how many players are missing
            Server.getInstance().setPlayerId(playerId);
            // create the new board
            Board board = null;
            board = new Board(gameName, playerName);

            // display the board
            JFrame rootFrame = (JFrame)getTopLevelAncestor(); 
            rootFrame.getContentPane().add(board, BorderLayout.CENTER);
            rootFrame.setVisible(true);

            newGameMenu.setEnabled(false);
        }
    }
}
