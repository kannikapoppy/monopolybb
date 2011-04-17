package monopolyUI;

import javax.swing.*;

import java.util.List;

import objectmodel.Player;
import services.Utils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * this class is the menu of the glorious game
 * @author Benda & Eizenman
 *
 */
public class MonopolyMenu extends JMenuBar 
	{
	
	private static final String FILE_STR = "File";
	private static final String NEW_GAME_STR = "New Game";
	private static final String EXIT_STR = "Exit";
	private static final String UNABLE_TO_INIT_GAME_PREFIX_ERROR_MSG = "Unable To Init Game! Error message: ";
	private static final String GAME_ENDED_UNEXPECTEDLY_PREFIX_ERROR_MSG = "The game has ended unexpectedly! Error message: ";
	private static final String CANT_START_NEW_GAME_UNTIL_NOT_FINISH_CURRENT_ERROR_MSG = "The crrent game didn't finish. First finish the current game and only than you may start a new one.";

    public MonopolyMenu() throws HeadlessException {
    	JMenu fileMenu = new JMenu(FILE_STR);
        this.add (fileMenu);

        JMenuItem newGameMenu = new JMenuItem(NEW_GAME_STR);
        fileMenu.add(newGameMenu);
        newGameMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// new game was clicked
				// first see if the current game ended
				JFrame rootFrame = (JFrame)getTopLevelAncestor(); 
				
				for(Component comp : rootFrame.getContentPane().getComponents())
				{
					if (comp instanceof Board)
					{
						Board previousGame = (Board)comp;
						if (previousGame.getMonopolyGame().isPlaying())
						{
							// we don't allow new game to start until previous one ends
							ShowError(CANT_START_NEW_GAME_UNTIL_NOT_FINISH_CURRENT_ERROR_MSG);
							return;
						}
						
						// clear the board and remove all relations to old game
						previousGame.FinishGame();
						rootFrame.getContentPane().remove(comp);
						break;
					}
				}
				
				CreatePlayersDialog playersDialog = new CreatePlayersDialog(rootFrame);
				playersDialog.setVisible(true);
				List<Player> players = playersDialog.GetPlayers();
				if (playersDialog.GetPlayers() != null)
				{
					// user wants to start the game
					
					// create the new board 
					Board board = null;
					try
					{
						board = new Board(players);
					}
					catch (final Exception ex)
					{
						SwingUtilities.invokeLater(new Runnable() {
							public void run() 
						    { 
								ShowError(UNABLE_TO_INIT_GAME_PREFIX_ERROR_MSG + ex.getMessage());
						    }
						});
						return;
					}
					
					// display the board
					rootFrame.getContentPane().add(board, BorderLayout.CENTER);
					rootFrame.setVisible(true);
					try
					{
						board.StartGame();
					}
					catch (final Exception ex)
					{
						SwingUtilities.invokeLater(new Runnable() {
							public void run() 
						    { 
								ShowError(GAME_ENDED_UNEXPECTEDLY_PREFIX_ERROR_MSG + ex.getMessage());
						    }
						});	
					}
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
    
    /**
     * shows error to the screen
     * @param msg
     */
    private void ShowError(final String msg)
    {
    	final JComponent parentComponenet = this;
    	SwingUtilities.invokeLater(new Runnable() {
			public void run() 
		    { 
				Utils.ShowError(parentComponenet, msg);
		    }
		});	
    }
}
