package monopolyUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonopolyMenu extends JMenuBar 
	{
	
	private static final String NEW_GAME_STR = "New Game";
	private static final String EXIT_STR = "Exit";

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
				if (playersDialog.GetPlayers() != null)
				{
					// user wants to start the game
					rootFrame.getContentPane().add(new Board(), BorderLayout.CENTER);
					rootFrame.setVisible(true);
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
}
