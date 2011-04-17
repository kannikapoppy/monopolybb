package monopolyUI;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;
import java.awt.Insets;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.SwingConstants;

import main.MonopolyGame;
import objectmodel.Dice.DiceThrowResult;
import services.Utils;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.Box;
import java.awt.Component;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DiceController extends JPanel {

	private static final long serialVersionUID = 1L;
	private final JLabel lblfirstDice = new JLabel("");
	private final JLabel labelSecondDice = new JLabel("");
	private final JButton btnRollDice = new JButton("Roll Dice");
	private final int DELAY_TIME_MS = 300;

	/**
	 * This is the default constructor
	 */
	public DiceController(final MonopolyGame game) {
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
				game.NotifyRollDice();
			}
		});
		
		btnRollDice.setEnabled(false);
		GridBagConstraints gbc_btnRollDice = new GridBagConstraints();
		gbc_btnRollDice.gridwidth = 2;
		gbc_btnRollDice.gridx = 0;
		gbc_btnRollDice.gridy = 1;
		add(btnRollDice, gbc_btnRollDice);
		initialize();
	}
	
	public void SimulateThrow(final DiceThrowResult diceThrow)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() 
		    {
				btnRollDice.setEnabled(false);
				
				lblfirstDice.setIcon(Utils.getImageIcon("die" + diceThrow.getFirstDice() + ".gif"));
				labelSecondDice.setIcon(Utils.getImageIcon("die" + diceThrow.getSecondDice() + ".gif"));
				
				validate();
				repaint();
		    }
		});
	}

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
