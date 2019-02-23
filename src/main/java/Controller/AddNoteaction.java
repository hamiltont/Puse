package Controller;

import Svg.Data;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

/*
 * Action listener controller for adding note functionality
 */
public class AddNoteaction implements ActionListener {

    Control c;

    /**
     * < controller object
     */


    public AddNoteaction(Control temp) {
        c = temp;

    } //end of constroctor

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        String note = c.gui.note_screen.note_text.getText().trim();
        String alignment = (getSelectedButtonText(c.gui.note_screen.group)).toLowerCase();
        String list_name = " #" + c.gui.note_screen.list.getSelectedValue().toString().trim();
        String code = "note " + alignment + " " + list_name + ": " + note + " end note";

        if (alignment.equalsIgnoreCase("over")) {
            String actor_names_field = c.gui.note_screen.text_field_actor_names.getText().trim();
            System.out.println(actor_names_field);
            code = "note " + alignment + " of " + actor_names_field + list_name + ": " + note + " end note";
        }// over

        if (c.gui.code_text_area.getCaretPosition() <= 0) {
            c.gui.code_text_area.append(code);
        } else {
            c.gui.code_text_area.insert(code, c.gui.code_text_area.getCaretPosition());
        }

        c.gui.note_screen.note_text.setText("");
        String temp = c.svgutils.writeSvgToDefaultFile(c.gui.code_text_area.getText());
        c.gui.validation_label.setText(temp);
        c.svgutils.reload(c.gui.svgcanvas, Data.temp_output_svgfile_name);
        c.gui.note_screen.dispose();

    }// end of action

    /**
     * Gets the selected button text.
     *
     * @param buttonGroup the button group
     * @return the selected button text
     */
    public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }
}//end of class