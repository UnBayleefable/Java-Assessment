/**
 * --------------------------------------------------------
 * Class: LibraryComponents
 *
 * @author Mark O'Reilly
 * Developed: 2017
 *
 * Purpose: To contain a library of utility methods that can be accessed from other Java applications
 *
 * Currently:
 *  - LocateAJLabel - for positioning a JLabel using the layout manager: SpringLayout
 *  - LocateAJTextField - for positioning a JTextField using SpringLayout
 *  - LocateAJButton - for positioning a JButton using SpringLayout
 *  - LocateAJTextArea - for positioning a JTextArea using SpringLayout
 *
 * ----------------------------------------------------------
 */
package trafficmonitoringapplication;

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


public class LibraryComponents
{

    /**
     * -------------------------------------------------------- Purpose: Locate
     * a single JLabel within the JFrame.
     *
     * @param Target_JFrame, Layout_manager, JLabel_Caption, Width, X_position, Y_Position;
     * @returns The JLabel.
     * ----------------------------------------------------------
     */
    public static JLabel LocateAJLabel(JFrame myJFrame, SpringLayout myJLabelLayout, String JLabelCaption, int x, int y)
    {
        // Instantiate the JLabel
        JLabel myJLabel = new JLabel(JLabelCaption);
        // Add the JLabel to the screen
        myJFrame.add(myJLabel);
        // Set the position of the JLabel (From left hand side of the JFrame (West), and from top of JFrame (North))
        myJLabelLayout.putConstraint(SpringLayout.WEST, myJLabel, x, SpringLayout.WEST, myJFrame);
        myJLabelLayout.putConstraint(SpringLayout.NORTH, myJLabel, y, SpringLayout.NORTH, myJFrame);
        // Return the label to the calling method
        return myJLabel;
    }

    /**
     * -------------------------------------------------------- Purpose: Locate
     * a single JTextField within the JFrame.
     *
     * @param Target_JFrame, Layout_manager, Width, X_position, Y_Position
     * @returns The JTextField.
     * ----------------------------------------------------------
     */
    public static JTextField LocateAJTextField(JFrame myJFrame, KeyListener myKeyLstnr, SpringLayout myJTextFieldLayout, int width, int x, int y)
    {
        JTextField myJTextField = new JTextField(width);
        myJFrame.add(myJTextField);
        myJTextField.addKeyListener(myKeyLstnr);
        myJTextFieldLayout.putConstraint(SpringLayout.WEST, myJTextField, x, SpringLayout.WEST, myJFrame);
        myJTextFieldLayout.putConstraint(SpringLayout.NORTH, myJTextField, y, SpringLayout.NORTH, myJFrame);
        return myJTextField;
    }

    /**
     * -------------------------------------------------------- Purpose: Locate
     * a single JButton within the JFrame.
     *
     * @param Target_JFrame, Layout_manager, JButton_name, JButton_caption, X_position,
     * Y_Position, Width, Height
     * @returns The JButton.
     * ----------------------------------------------------------
     */
    public static JButton LocateAJButton(JFrame myJFrame, ActionListener myActnLstnr, SpringLayout myJButtonLayout, String JButtonCaption, int x, int y, int w, int h)
    {
        JButton myJButton = new JButton(JButtonCaption);
        myJFrame.add(myJButton);
        myJButton.addActionListener(myActnLstnr);
        myJButtonLayout.putConstraint(SpringLayout.WEST, myJButton, x, SpringLayout.WEST, myJFrame);
        myJButtonLayout.putConstraint(SpringLayout.NORTH, myJButton, y, SpringLayout.NORTH, myJFrame);
        myJButton.setPreferredSize(new Dimension(w, h));
        
        return myJButton;
    }

    /**
     * -------------------------------------------------------- Purpose: Locate
     * a single JTextArea within the JFrame.
     *
     * @param Target_JFrame, Layout_manager, JTextArea_name, X_position, Y_Position, Width,
     * Height
     * @returns The JTextArea.
     * ----------------------------------------------------------
     */
    public static JTextArea LocateAJTextArea(JFrame myJFrame, SpringLayout myLayout, JTextArea myJTextArea, int x, int y, int w, int h)
    {
        
        myJTextArea = new JTextArea(w, h);
        JScrollPane areaScrollPane = new JScrollPane(myJTextArea);
        myJFrame.add(myJTextArea);
        myLayout.putConstraint(SpringLayout.WEST, myJTextArea, x, SpringLayout.WEST, myJFrame);
        myLayout.putConstraint(SpringLayout.NORTH, myJTextArea, y, SpringLayout.NORTH, myJFrame);
        
        return myJTextArea;
    }
    
    /**
     * -------------------------------------------------------- Purpose: Locate
     * a single JTable within the JFrame.
     *
     * @param Target_JFrame, Layout_manager, JTextTable_name,Data_ArrayList<Object[]>, Column_Names, Xpos, Ypos, Width, Height
     * @returns The JTable.
     * ----------------------------------------------------------
     */
    public static JTable LocateAJTable(JFrame myJFrame, TableModel myModel, SpringLayout myLayout, JTable myJTable, ArrayList<Object[]> tableData, String[] columnNames, int x, int y, int Width, int Height)
    {
        JPanel myJPanel = new JPanel();
        myJPanel.setLayout(new BorderLayout());          
        myJFrame.add(myJPanel);               
        
        myJTable = new JTable(myModel);        
        myJPanel.add(myJTable); 
        
        JScrollPane myScrollPane = new JScrollPane(myJTable);
        myJPanel.add(myScrollPane);
        myJPanel.setPreferredSize(new Dimension(Width, Height));
        
        myLayout.putConstraint(SpringLayout.WEST, myJPanel, x, SpringLayout.WEST, myJFrame);
        myLayout.putConstraint(SpringLayout.NORTH, myJPanel, y, SpringLayout.NORTH, myJFrame);    

        return myJTable;
    }

}


