//package monopolyUI;
//
//import javax.swing.JPanel;
//import java.awt.Frame;
//
//import javax.swing.JDialog;
//import javax.swing.JLabel;
//import javax.swing.JTextField;
//import javax.swing.JComboBox;
//import javax.swing.DefaultComboBoxModel;
//import javax.swing.JButton;
//import javax.swing.SwingUtilities;
//
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
//
//import javax.swing.SwingConstants;
//
//import java.awt.Component;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.JSeparator;
//
//import services.*;
//
//import java.awt.Font;
//import javax.swing.Box;
//
///**
// * this class is a dialog for creating the players
// * @author Benda & Eizenman
// *
// */
//public class CreatePlayersDialog extends JDialog {
//
//	private static final long serialVersionUID = 1L;
//	private JPanel jContentPane = null;
//	private JTextField playerNameField;
//	private JComboBox playerTypeComboBox;
//	private Box playersBox;
//	final private JComboBox playerColorComboBox = new JComboBox();;
//	final private JButton btnAddPlayer = new JButton("Add Player");;
//
//	private List<Player> playerList = new ArrayList<Player>();
//	private boolean exitedWell = false;
//
//	private static final String USER_NAME_EMPTY_ERROR_MSG = "User Name Cannot Be Empty";
//	private static final String USER_NAME_ALREADY_EXISTS_ERROR_MSG = "User Name Already Exists";
//	private static final String NOT_ENOUGH_PLAYERS_ERROR_MSG = "Number Of Players Must Be At Least 2. Please Add Players And Try Again To Start.";
//
//
//
//	/**
//	 * @param owner
//	 */
//	public CreatePlayersDialog(Frame owner) {
//		super(owner);
//		initialize();
//		setModal(true);
//	}
//
//	/**
//	 * This method initializes this
//	 *
//	 * @return void
//	 */
//	private void initialize() {
//		this.setSize(423, 400);
//		this.setContentPane(getJContentPane());
//		this.setResizable(false);
//	}
//
//	/**
//	 * This method initializes jContentPane
//	 *
//	 * @return javax.swing.JPanel
//	 */
//	private JPanel getJContentPane() {
//		if (jContentPane == null) {
//			jContentPane = new JPanel();
//			jContentPane.setLayout(null);
//
//			// create and add the new player controls (labels, combox boxes, etc...)
//
//			JLabel lblPlayername = new JLabel("Player Name :");
//			lblPlayername.setBounds(4, 11, 122, 14);
//			lblPlayername.setVerticalAlignment(SwingConstants.TOP);
//			jContentPane.add(lblPlayername);
//
//			playerNameField = new JTextField();
//			playerNameField.setBounds(150, 8, 130, 20);
//			jContentPane.add(playerNameField);
//			playerNameField.setColumns(10);
//
//			playerTypeComboBox = new JComboBox();
//			playerTypeComboBox.setBounds(150, 39, 130, 18);
//			playerTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Human", "Machine"}));
//			jContentPane.add(playerTypeComboBox);
//
//			JSeparator separator = new JSeparator();
//			separator.setBounds(207, 57, 1, 1);
//			jContentPane.add(separator);
//
//			JLabel lblPlayers = new JLabel("Players");
//			lblPlayers.setBounds(4, 135, 66, 22);
//			lblPlayers.setFont(new Font("Tahoma", Font.BOLD, 18));
//			jContentPane.add(lblPlayers);
//
//			// add start game button
//			JButton btnStartTheGame = new JButton("Start The Game");
//			btnStartTheGame.setBounds(141, 334, 139, 23);
//			btnStartTheGame.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent arg0) {
//					if (playerList.size() < 2)
//					{
//						// if number of players is not enough we cannot start the game
//						SwingUtilities.invokeLater(new Runnable() {
//							public void run()
//						    {
//								ShowNotEnoughPlayersError();
//						    }
//						});
//						return;
//					}
//					// great - we can start the game. joy joy
//					// we mark exitedWell = true to understand from outside that we can start.
//					exitedWell = true;
//					dispose();
//				}
//			});
//			jContentPane.add(btnStartTheGame);
//
//			JLabel lblType = new JLabel("Type :");
//			lblType.setBounds(4, 41, 122, 14);
//			jContentPane.add(lblType);
//
//			JLabel lblColor = new JLabel("Color :");
//			lblColor.setBounds(4, 68, 122, 14);
//			jContentPane.add(lblColor);
//
//			playerColorComboBox.setModel(new DefaultComboBoxModel(PlayerColor.values()));
//			playerColorComboBox.setBounds(150, 65, 130, 20);
//			jContentPane.add(playerColorComboBox );
//
//			JSeparator separator_1 = new JSeparator();
//			separator_1.setBounds(4, 127, 401, 14);
//			jContentPane.add(separator_1);
//
//			btnAddPlayer.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent arg0) {
//					SwingUtilities.invokeLater(new Runnable() {
//					    public void run()
//					    {
//					    	// we need to validate that the user is valid
//
//					    	if (playerNameField.getText().trim().compareTo("") == 0)
//							{
//								ShowUserNameEmptyError();
//								return;
//							}
//
//					    	if (IsNameAlreadyExists(playerNameField.getText().trim()))
//					    	{
//					    		ShowUserNameAlreadyExistsError();
//								return;
//					    	}
//
//							// ok, valid user can be created. let's do it.
//
//					    	Player newPlayer;
//
//							// the creator selected if the player is human or machine
//					    	if (playerTypeComboBox.getSelectedIndex() == 0)
//							{
//								// human player
//								newPlayer = new Player();
//								newPlayer.setInputObject(new UIPlayerInput(newPlayer));
//							}
//							else
//							{
//								// machine
//								newPlayer = new AutomaticPlayer();
//							}
//
//							//set the player name
//					    	newPlayer.setName(playerNameField.getText().trim());
//							// set the player color
//					    	newPlayer.setPlayerColor((PlayerColor)playerColorComboBox.getSelectedItem());
//							// add him to the list of players
//					    	playerList.add(newPlayer);
//							// add him visually.
//					    	AddPlayerToVisualList(newPlayer);
//
//							// init,update and disable controls according to rules and
//					    	// Convenience of creator
//					    	playerNameField.setText("");
//							playerColorComboBox.removeItem(playerColorComboBox.getSelectedItem());
//
//							if (playerList.size() == 6)
//							{
//								//  no more players can be created
//								playerNameField.setEnabled(false);
//								btnAddPlayer.setEnabled(false);
//								playerColorComboBox.setEnabled(false);
//								playerTypeComboBox.setEnabled(false);
//							}
//							else
//							{
//								playerTypeComboBox.setSelectedIndex(0);
//								playerColorComboBox.setSelectedIndex(0);
//							}
//					    }
//					});
//				}
//			});
//			btnAddPlayer.setBounds(4, 93, 122, 23);
//			jContentPane.add(btnAddPlayer);
//
//			playersBox = Box.createVerticalBox();
//			playersBox.setBounds(4, 162, 401, 161);
//			jContentPane.add(playersBox);
//
//			JSeparator separator_2 = new JSeparator();
//			separator_2.setBounds(4, 323, 401, 22);
//			jContentPane.add(separator_2);
//		}
//		return jContentPane;
//	}
//
//	/**
//	 * validation method that checks if the player name already exists
//	 * @param newPlayerName
//	 * @return true if the user name already exists, otherwise false
//	 */
//	private boolean IsNameAlreadyExists(String newPlayerName)
//	{
//		for (Player player : playerList)
//		{
//			if (player.getName().compareTo(newPlayerName) == 0)
//			{
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//	public List<Player> GetPlayers()
//	{
//		if (exitedWell == false)
//			return null;
//		else
//			return playerList;
//	}
//
//	private void ShowNotEnoughPlayersError()
//	{
//		Utils.ShowError(this, NOT_ENOUGH_PLAYERS_ERROR_MSG);
//	}
//
//	private void ShowUserNameEmptyError()
//	{
//		Utils.ShowError(this, USER_NAME_EMPTY_ERROR_MSG);
//	}
//
//	private void ShowUserNameAlreadyExistsError()
//	{
//		Utils.ShowError(this, USER_NAME_ALREADY_EXISTS_ERROR_MSG);
//	}
//
//	/**
//	 * Adds the player visually to the list of players.
//	 * @param player
//	 */
//	private void AddPlayerToVisualList(Player player)
//	{
//		final Player newPlayer = player;
//		final Box horizontalBox = Box.createHorizontalBox();
//
//		// Adds a row which describes the new player and allow to remove him.
//		playersBox.add(horizontalBox);
//
//		JLabel lblNewLabel_1 = new JLabel(newPlayer.getName());
//		horizontalBox.add(lblNewLabel_1);
//
//		Component horizontalGlue = Box.createHorizontalGlue();
//		horizontalBox.add(horizontalGlue);
//
//		JLabel lblNewLabel = new JLabel(newPlayer.getPlayerColor().toString());
//		horizontalBox.add(lblNewLabel);
//
//		Component horizontalGlue_1 = Box.createHorizontalGlue();
//		horizontalBox.add(horizontalGlue_1);
//
//		String playerType = "Human";
//		if (newPlayer instanceof AutomaticPlayer)
//		{
//			playerType = "Machine";
//		}
//
//		JLabel lblType_1 = new JLabel(playerType);
//		horizontalBox.add(lblType_1);
//
//		Component horizontalGlue_2 = Box.createHorizontalGlue();
//		horizontalBox.add(horizontalGlue_2);
//
//		JButton btnRemovePlayer = new JButton("Remove Player");
//		btnRemovePlayer.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				// remove the player from the logical list of players
//				playerList.remove(newPlayer);
//				SwingUtilities.invokeLater(new Runnable() {
//					public void run()
//				    {
//						// remove the player from the visual list
//						playerColorComboBox.addItem(newPlayer.getPlayerColor());
//						playersBox.remove(horizontalBox);
//						playersBox.validate();
//						playersBox.repaint();
//
//						// enable to add more players now that the number of players is less then
//						// the maximum
//						btnAddPlayer.setEnabled(true);
//						playerNameField.setEnabled(true);
//						playerColorComboBox.setEnabled(true);
//						playerTypeComboBox.setEnabled(true);
//				    }
//				});
//			}
//		});
//
//		horizontalBox.add(btnRemovePlayer);
//
//		playersBox.validate();
//		playersBox.repaint();
//	}
//}
