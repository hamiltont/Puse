package userinterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * This class provides a view for reference interface.
 */
public class RefGui extends JFrame {

    public JPanel contentPane;
    public JTextField ref_object_name;
    public ButtonGroup group;
    public JButton btnAddReference;
    public JRadioButton rdbtnNewRadioButton;
    public JRadioButton rdbtnNewRadioButton_1;

    /**
     * Instantiates a new ref gui.
     */
    public RefGui() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 439, 165);
        setResizable(false);
        setTitle("pusq");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        rdbtnNewRadioButton = new JRadioButton("Ref On Object");
        rdbtnNewRadioButton.setBounds(10, 27, 109, 23);
        contentPane.add(rdbtnNewRadioButton);

        rdbtnNewRadioButton_1 = new JRadioButton("Ref Under Object");
        rdbtnNewRadioButton_1.setSelected(true);
        rdbtnNewRadioButton_1.setBounds(10, 53, 136, 23);
        contentPane.add(rdbtnNewRadioButton_1);

        group = new ButtonGroup();
        group.add(rdbtnNewRadioButton);
        group.add(rdbtnNewRadioButton_1);


        JLabel lblObjectName = new JLabel("Object Name");
        lblObjectName.setBounds(162, 31, 73, 14);
        contentPane.add(lblObjectName);

        ref_object_name = new JTextField();
        ref_object_name.setToolTipText("Write an object name please");
        ref_object_name.setBounds(165, 54, 224, 20);
        contentPane.add(ref_object_name);
        ref_object_name.setColumns(10);

        btnAddReference = new JButton("Add Reference");
        btnAddReference.setBounds(105, 91, 130, 23);
        contentPane.add(btnAddReference);
    }
}//end of class
