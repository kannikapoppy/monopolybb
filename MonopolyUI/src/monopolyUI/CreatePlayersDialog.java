package monopolyUI;

import javax.swing.JPanel;
import java.awt.Frame;

import javax.swing.AbstractCellEditor;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.SwingConstants;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JSeparator;

import services.*;
import objectmodel.AutomaticPlayer;
import objectmodel.Player;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class CreatePlayersDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTextField playerNameField;
	private JComboBox playerTypeComboBox;
	private JTable table;
	
	private List<Player> playerList = new ArrayList<Player>();
	private boolean exitedWell = false;
	
	private static final String USER_NAME_EMPTY_ERROR_MSG = "User Name Cannot Be Empty"; 
	
	
	
	/**
	 * @param owner
	 */
	public CreatePlayersDialog(Frame owner) {
		super(owner);
		initialize();
		setModal(true);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(423, 441);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			GridBagLayout gbl_jContentPane = new GridBagLayout();
			gbl_jContentPane.columnWidths = new int[]{67, 86, 64, 78, 81, 0};
			gbl_jContentPane.rowHeights = new int[]{57, 0, 0, 41, -7, 65, 14, 0};
			gbl_jContentPane.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_jContentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			jContentPane.setLayout(gbl_jContentPane);
			
			JLabel lblPlayername = new JLabel("Player Name :");
			lblPlayername.setVerticalAlignment(SwingConstants.TOP);
			GridBagConstraints gbc_lblPlayername = new GridBagConstraints();
			gbc_lblPlayername.insets = new Insets(0, 0, 5, 5);
			gbc_lblPlayername.gridx = 0;
			gbc_lblPlayername.gridy = 0;
			jContentPane.add(lblPlayername, gbc_lblPlayername);
			
			playerNameField = new JTextField();
			GridBagConstraints gbc_playerNameField = new GridBagConstraints();
			gbc_playerNameField.anchor = GridBagConstraints.WEST;
			gbc_playerNameField.insets = new Insets(0, 0, 5, 5);
			gbc_playerNameField.gridx = 1;
			gbc_playerNameField.gridy = 0;
			jContentPane.add(playerNameField, gbc_playerNameField);
			playerNameField.setColumns(10);
			
			playerTypeComboBox = new JComboBox();
			playerTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Human", "Machine"}));
			GridBagConstraints gbc_playerTypeComboBox = new GridBagConstraints();
			gbc_playerTypeComboBox.insets = new Insets(0, 0, 5, 5);
			gbc_playerTypeComboBox.gridx = 2;
			gbc_playerTypeComboBox.gridy = 0;
			jContentPane.add(playerTypeComboBox, gbc_playerTypeComboBox);
			
			JButton btnAdd = new JButton("");
			btnAdd.setVerticalAlignment(SwingConstants.TOP);
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					AddPlayer();
				}
			});
			btnAdd.setIcon(new ImageIcon(CreatePlayersDialog.class.getResource("/Images/add32.png")));
			GridBagConstraints gbc_btnAdd = new GridBagConstraints();
			gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
			gbc_btnAdd.gridx = 4;
			gbc_btnAdd.gridy = 0;
			jContentPane.add(btnAdd, gbc_btnAdd);
			
			JSeparator separator = new JSeparator();
			GridBagConstraints gbc_separator = new GridBagConstraints();
			gbc_separator.gridwidth = 5;
			gbc_separator.insets = new Insets(0, 0, 5, 0);
			gbc_separator.gridx = 0;
			gbc_separator.gridy = 1;
			jContentPane.add(separator, gbc_separator);
			
			JLabel lblPlayers = new JLabel("Players");
			lblPlayers.setFont(new Font("Tahoma", Font.BOLD, 18));
			GridBagConstraints gbc_lblPlayers = new GridBagConstraints();
			gbc_lblPlayers.insets = new Insets(0, 0, 5, 5);
			gbc_lblPlayers.gridx = 0;
			gbc_lblPlayers.gridy = 2;
			jContentPane.add(lblPlayers, gbc_lblPlayers);
			
			table = new JTable();
			table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Player Name", "Player Type", ""
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class, Object.class, Object.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				boolean[] columnEditables = new boolean[] {
					true, true, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			table.getColumnModel().getColumn(2).setResizable(false);
			GridBagConstraints gbc_table = new GridBagConstraints();
			gbc_table.gridheight = 6;
			gbc_table.gridwidth = 3;
			gbc_table.insets = new Insets(0, 0, 0, 5);
			gbc_table.fill = GridBagConstraints.BOTH;
			gbc_table.gridx = 0;
			gbc_table.gridy = 3;
			jContentPane.add(table, gbc_table);
			
			JButton btnStartTheGame = new JButton("Start The Game");
			btnStartTheGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					exitedWell = true;
					dispose();
				}
			});
			GridBagConstraints gbc_btnStartTheGame = new GridBagConstraints();
			gbc_btnStartTheGame.gridx = 4;
			gbc_btnStartTheGame.gridy = 8;
			jContentPane.add(btnStartTheGame, gbc_btnStartTheGame);
		}
		return jContentPane;
	}
	
	public List<Player> GetPlayers()
	{
		if (exitedWell == false)
			return null;
		else
			return playerList;
	}
	
	private void AddPlayer() 
	{
		if (playerTypeComboBox.getSelectedIndex() == 0)
		{
			// Human
			
			if (playerNameField.getText().trim() == "")
			{
				ShowUserNameEmptyError();
				return;
			}
			
			Player humanPlayer = new Player();
			humanPlayer.setName(playerNameField.getText().trim());
			humanPlayer.setInputObject(new UIPlayerInput(humanPlayer));
			playerList.add(humanPlayer);
			
			
		}
		else
		{
			// Machine
			Player autoPlayer = new AutomaticPlayer();
			
			UUID uuid = UUID.randomUUID();
			String randomUUIDString = uuid.toString();
			
			autoPlayer.setName("Computer" + randomUUIDString);
			playerList.add(autoPlayer);
		}
	}

	private void ShowUserNameEmptyError() 
	{
		ShowError(USER_NAME_EMPTY_ERROR_MSG);
	}

	private void ShowError(String msg) 
	{
		JOptionPane.showMessageDialog(this, msg);
	}
}
