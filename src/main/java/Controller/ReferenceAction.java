package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import userinterface.RefGui;

import Controller.Control.LoadItemToText;
import Svg.Data;

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
		c = temp ;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * creates a referance code after user clicks on button 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		int returnVal= c.userinferface.fc_reference.showOpenDialog(c.userinferface.code_text_area);
		if(returnVal==JFileChooser.APPROVE_OPTION){
			c.userinferface.ref_screen.setVisible(true);
			c.userinferface.preview.svg_preview.setURI(null);
		}//approve
	}

}
