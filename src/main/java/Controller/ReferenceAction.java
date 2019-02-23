package Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Class ReferenceAction.
 */
public class ReferenceAction implements ActionListener {

    Control c;  /**<control object*/

    /**
     * Instantiates a new reference action.
     *
     * @param temp the temp
     */
    public ReferenceAction(Control temp) {
        c = temp;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     * creates a referance code after user clicks on button
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        int returnVal = c.gui.fc_reference.showOpenDialog(c.gui.code_text_area);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            c.gui.ref_screen.setVisible(true);
            c.gui.preview.svg_preview.setURI(null);
        }//approve
    }

}
