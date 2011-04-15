package monopolyUI;

import javax.swing.*;

import java.util.List;

import objectmodel.Player;
import services.Utils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonopolyMenu extends JMenuBar 
	{
	
	private static final String NEW_GAME_STR = "New Game";
	private static final String EXIT_STR = "Exit";
	private static final String UNABLE_TO_INIT_GAME_PREFIX_ERROR_MSG = "Unable To Init Game! Error message: ";
	private static final String GAME_ENDED_UNEXPECTEDLY_PREFIX_ERROR_MSG = "The game has ended unexpectedly! Error message: ";

    public MonopolyMenu() throws HeadlessException {
        JMenu fileMenu = new JMenu("File");
        this.add (fileMenu);

        JMenuItem newGameMenu = new JMenuItem(NEW_GAME_STR);
        fileMenu.add(newGameMenu);
        newGameMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame rootFrame = (JFrame)getTopLevelAncestor(); 
				CreatePlayersDialog playersDialog = new CreatePlayersDialog(rootFrame);
				playersDialog.setVisible(true);
				List<Player> players = playersDialog.GetPlayers();
				if (playersDialog.GetPlayers() != null)
				{
					// user wants to start the game
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
        
        JMenuItem exitMenuItem = new JMenuItem(EXIT_STR);
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
    }
    
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
