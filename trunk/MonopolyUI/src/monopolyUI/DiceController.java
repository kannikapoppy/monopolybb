package monopolyUI;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.SwingConstants;

import services.Utils;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;
import src.client.Server;

/**
 * this class displays the Dice
 * @author Benda & Eizenman
 *
 */
public class DiceController extends JPanel {

	private static final long serialVersionUID = 1L;
	/**
	 * the first dice image
	 */
	private final JLabel lblfirstDice = new JLabel("");
	/**
	 * the second dice image
	 */
	private final JLabel labelSecondDice = new JLabel("");
	/**
	 * button to roll the dice
	 */
	private final JButton btnRollDice = new JButton("Roll Dice");

	/**
	 * This is the default constructor
	 */
	public DiceController() {
		super();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{106, 106, 0};
		gridBagLayout.rowHeights = new int[]{107, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		lblfirstDice.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_lblfirstDice = new GridBagConstraints();
		gbc_lblfirstDice.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblfirstDice.insets = new Insets(0, 0, 5, 5);
		gbc_lblfirstDice.gridx = 0;
		gbc_lblfirstDice.gridy = 0;
		add(lblfirstDice, gbc_lblfirstDice);

		lblfirstDice.setHorizontalAlignment(SwingConstants.CENTER);
		lblfirstDice.setIcon(Utils.getImageIcon("die4.gif"));
		labelSecondDice.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_labelSecondDice = new GridBagConstraints();
		gbc_labelSecondDice.anchor = GridBagConstraints.NORTHWEST;
		gbc_labelSecondDice.insets = new Insets(0, 0, 5, 0);
		gbc_labelSecondDice.gridx = 1;
		gbc_labelSecondDice.gridy = 0;
		add(labelSecondDice, gbc_labelSecondDice);

		labelSecondDice.setHorizontalAlignment(SwingConstants.CENTER);
		labelSecondDice.setIcon(Utils.getImageIcon("die6.gif"));

		btnRollDice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// we just notify the game that it can proceeds now after the user decided
				// he wants to roll the dice. this is of course only relevant to human players
                                Random random = new Random();
                                int dice1 = random.nextInt(6) + 1;
                                int dice2 = random.nextInt(6) + 1;
                                // TODO: fix this
                                //Server.getInstance().setDiceRollResults(, dice1, dice)
			}
		});

		// at first the dice button is disabled, until we get an event from the game
		btnRollDice.setEnabled(false);
		GridBagConstraints gbc_btnRollDice = new GridBagConstraints();
		gbc_btnRollDice.gridwidth = 2;
		gbc_btnRollDice.gridx = 0;
		gbc_btnRollDice.gridy = 1;
		add(btnRollDice, gbc_btnRollDice);
		initialize();
	}

	/**
	 * simulate dice throw
	 * @param diceThrow
	 */
	public void SimulateThrow(final int firstDice, final int secondDice)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
		    {
				btnRollDice.setEnabled(false);

				// set the images describing the throw
				lblfirstDice.setIcon(Utils.getImageIcon("die" + firstDice + ".gif"));
				labelSecondDice.setIcon(Utils.getImageIcon("die" + secondDice + ".gif"));

				validate();
				repaint();
		    }
		});
	}

	/**
	 * enable the dice throw button so the user can indicate he wants to throw the dice
	 */
	public void EnableDiceThrow()
    {
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
		    {
				btnRollDice.setEnabled(true);
		    }
		});
    }

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(278, 178);
	}

}
