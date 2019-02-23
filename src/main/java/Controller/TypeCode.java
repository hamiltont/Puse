
package Controller;

import Svg.Data;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/*
 * This class handles user input from keyboard. When user enters input into code area it class realted functions and triggers svg creataion
 * methods
 */
public class TypeCode implements DocumentListener {

    Control c;
    JTextArea codeArea;

    public TypeCode(Control temp) {
        c = temp;
        codeArea = c.gui.code_text_area;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        String code = codeArea.getText().trim();

        // Update the GUI to indicate the new code contents have not been saved
        Data.is_saved = false;
        c.gui.save_button.setEnabled(true);
        c.gui.menuitem_save.setEnabled(true);
        c.gui.rdbtnmntmOpenInNew.setEnabled(false);
        c.gui.chckbxmntmIncrementalModel.setEnabled(false);
        c.gui.filename_label.setText("New File");

        // Attempt to write SVG. If success, reload graphic
        // TODO stop running writeSvg on the UI thread!!
        String validationtext = c.svgutils.writeSvgToDefaultFile(code);
        if (validationtext.equalsIgnoreCase(" ") || validationtext.contains("@")) {
            c.gui.validation_label.setText(validationtext);
            c.gui.svgcanvas.setURI(null);
        } else {
            c.gui.svgcanvas = c.svgutils.reload(c.gui.svgcanvas, Data.temp_output_svgfile_name);
            c.gui.validation_label.setText(validationtext);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        // no-op
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // no-op
    }

}
