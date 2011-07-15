package src.monopolyUI;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.SwingConstants;

import src.services.Utils;
import java.awt.Component;

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
				// set the images describing the throw
				lblfirstDice.setIcon(Utils.getImageIcon("die" + firstDice + ".gif"));
				labelSecondDice.setIcon(Utils.getImageIcon("die" + secondDice + ".gif"));

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
		this.setSize(278, 178);
	}

}
