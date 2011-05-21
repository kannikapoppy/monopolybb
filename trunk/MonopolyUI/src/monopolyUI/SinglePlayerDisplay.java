package monopolyUI;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import services.Utils;

import java.awt.BorderLayout;
import javax.swing.JButton;
import src.client.PlayerDetails;

/**
 * displays one player in the center board player mapping
 * @author Benda & Eizenman
 *
 */
public class SinglePlayerDisplay extends JPanel {

	private static final long serialVersionUID = 1L;
	/**
	 * display color, name and amount of money of player
	 */
	private final JLabel userLbl = new JLabel("", JLabel.LEFT);
	/**
	 * displays the arrow image to indicate the current player if its the player turn now
	 */
	private final JLabel currentPlayerLbl = new JLabel("", JLabel.RIGHT);
	/**
	 * the player object this class is displaying
	 */
	private PlayerDetails player;

	/**
	 * This is the default constructor
	 */
	public SinglePlayerDisplay(PlayerDetails player) {
		super();

		this.player = player;

		setLayout(new BorderLayout(0, 0));

		// set the player text display
		UpdatePlayerLabelText();
		String colorImageName = "Box" + player.getPlayerColor().toString() + "32.png";
		userLbl.setIcon(Utils.getImageIcon(colorImageName));

		add(userLbl, BorderLayout.WEST);

		// add the current player indication
		currentPlayerLbl.setIcon(Utils.getImageIcon("arrow-left.gif"));
		currentPlayerLbl.setVisible(false);

		add(currentPlayerLbl, BorderLayout.EAST);

		initialize();
	}

	/**
	 * updates the player text composed of color, name and money
	 */
	public void UpdatePlayerLabelText()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
		    {
				userLbl.setText(player.getName() + ", " + player.getAmount() + "$");
				if (!player.isInGame())
				{
					// if the player is not in game == lost then disable the mapping
					userLbl.setEnabled(false);
				}
		    }
		});
	}

	/**
	 * turn on or off the current player indication, depending on who's turn is it right now.
	 * @param isHisTurn
	 */
	public void SetPlayingUser(final boolean isHisTurn)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
		    {
				currentPlayerLbl.setVisible(isHisTurn);

				validate();
				repaint();
		    }
		});
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
	}

}
