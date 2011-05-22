package src.monopolyUI;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.Box;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;


import src.services.Utils;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;
import javax.swing.JButton;
import objectmodel.PlayerDetails;
import src.client.Server;

/**
 * this class represent the visual center board (without the cells that is)
 * @author Benda & Eizenman
 */
public class CenterBoard extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String CARD_DECK_IMG_PATH = "cardsdeck.jpg";
	/**
	 * instance of the dice controller
	 */
	private DiceController dice;
	/**
	 * mapping between player to player display
	 */
	private final Hashtable<PlayerDetails,SinglePlayerDisplay> playerToDisplay = new Hashtable<PlayerDetails,SinglePlayerDisplay>();

        /**
         * the name of the local player
         */
        private String localPlayerName;
        /**
         * resign button
         */
        private JButton resignButton = new JButton("Resign");
	/**
	 * This is the default constructor
	 */
	public CenterBoard(List<PlayerDetails> players, String localPlayerName) {
		super();

                this.localPlayerName = localPlayerName;
		// set layout
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{217, 0};
		gridBagLayout.rowHeights = new int[]{142, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		// create the players mapping display and add it
		Box playersBox = Box.createVerticalBox();
		GridBagConstraints gbc_playersBox = new GridBagConstraints();
		gbc_playersBox.insets = new Insets(0, 0, 5, 5);
		gbc_playersBox.gridx = 0;
		gbc_playersBox.gridy = 0;
		add(playersBox, gbc_playersBox);

		// add players to the box with the right values
		for (PlayerDetails player : players)
		{
			SinglePlayerDisplay userDisplay = new SinglePlayerDisplay(player);
			playerToDisplay.put(player, userDisplay);
			playersBox.add(userDisplay);
		}

                resignButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean res = Server.getInstance().Resign();
                                if (res)
                                    resignButton.setEnabled(false);
			}
		});

                playersBox.add(resignButton);

		// create the dice controller and it
		dice = new DiceController();
		GridBagConstraints gbc_dice = new GridBagConstraints();
		gbc_dice.insets = new Insets(0, 0, 5, 0);
		gbc_dice.gridx = 1;
		gbc_dice.gridy = 0;
		add(dice, gbc_dice);

		// create the image of the card decks and add it.
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(Utils.getImageIcon(CARD_DECK_IMG_PATH));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);

		initialize();
	}

	/**
	 * indicating who is the current player
	 * @param player - the current player
	 */
	public void SetPlayingUser(final PlayerDetails player)
	{
		for (PlayerDetails p : playerToDisplay.keySet())
		{
			// indicate to each player if he is playing now or not
			playerToDisplay.get(p).SetPlayingUser(p == player);
		}
	}

	public void UpdatePlayerDisplay(PlayerDetails player)
	{
		playerToDisplay.get(player).UpdatePlayerLabelText();
                if (player.getName().compareTo(localPlayerName) == 0 && !player.isInGame())
                    resignButton.setEnabled(false);
	}

	public void SimulateDiceThrow(int dice1, int dice2)
	{
		dice.SimulateThrow(dice1, dice2);
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
