package Controller;

import Svg.Data;
import Svg.SvgUtils;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;
import userinterface.Gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.MalformedURLException;

/**
 * The Class Control. Provides communication with model and view
 */
public class Control {

    SvgUtils svgutils;
    Gui gui;

    /**
     * Add listeners to view
     *
     * @param tempgui the tempgui
     * @param tempsvg the tempsvg
     */
    public Control(Gui tempgui, SvgUtils tempsvg) {
        svgutils = tempsvg;
        gui = tempgui;

        gui.addcodetextListener(new TypeCode(this));
        gui.addsaveListener(new SaveItem());
        gui.addloadListener(new LoadItemToText());
        gui.addpopupactionListener(new ExportDiagram());
        gui.addlinkactivationListener(new LinkActivation(this));
        gui.addreferenceListener(new ReferenceAction(this));
        gui.addbackListener(new BackButtonListener());
        gui.addnoteListener();
        gui.note_screen.addnoteListener2(new AddNoteaction(this));
        gui.addgeneratelinkListener(new GenerateLinks());
        gui.linkwindow.addSVgListener(new SeperateWindowHandling());
        gui.addClosingListener(new ClosingHangler());
        gui.adddynamicrefListener(new DynamicRef());
    }

    /**
     * This class creates ref operators. User can choose an area in main diagram and with right click on mouse user will activate this class
     * This object will take selected area and create svg file. Later referancing code will be placed in base  diagram's code.
     */
    public class DynamicRef implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String selected_code = gui.code_text_area.getSelectedText().trim();
            String svg = "";
            int returnVal = gui.fc_save.showSaveDialog(gui.code_text_area);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String removed_code = selected_code;
                selected_code = "@startuml \n " + selected_code + " \n @enduml";
                String filename = gui.fc_save.getSelectedFile().getAbsolutePath();
                svgutils.writeSvgToFile(selected_code, filename);
                String main_code = gui.code_text_area.getText();
                int insert_index = main_code.indexOf(removed_code);
                main_code = main_code.replaceFirst(removed_code, "");
                String object_name = JOptionPane.showInputDialog(null, "Write object name(s)?");
                if (object_name.equals("") || object_name == null) {
                    object_name = "Object name  here";
                }

                String ref_message = "";
                String link = "";
                try {
                    link = "[[" + gui.fc_save.getSelectedFile().toURL() + ".svg " + gui.fc_save.getSelectedFile().getName() + "]]";
                } catch (MalformedURLException e2) {
                    JOptionPane.showMessageDialog(gui.code_text_area, "Can not generate reference link");
                }
                ref_message = "ref over " + object_name.trim() + " : init" + link;
                StringBuffer sb = new StringBuffer(main_code);
                sb.insert(insert_index, ref_message);
                main_code = sb.toString();
                gui.code_text_area.setText(main_code);
                File file1 = new File(filename + ".txt");
                FileWriter fw = null;
                BufferedWriter out = null;
                try {

                    fw = new FileWriter(file1);
                    out = new BufferedWriter(fw);

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try {
                    out.write(selected_code);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                svgutils.writeSvgToDefaultFile(main_code);
                svgutils.reload(gui.svgcanvas, Data.temp_output_svgfile_name);
                gui.save_button.setVisible(true);
                gui.menuitem_save.setEnabled(true);
                gui.rdbtnmntmOpenInNew.setEnabled(false);
                gui.chckbxmntmIncrementalModel.setEnabled(false);

            } //APPROVE_OPTION
        }// Action
    }//DynamicRef

    /**
     * This class handles event for closing main program basically it ask user to whether they want to save the work or not
     */
    public class ClosingHangler extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            if (Data.is_saved == false && gui.code_text_area.getText().equals("") == false) {
                int ref = JOptionPane.showConfirmDialog(gui.code_text_area, "Do you want save changes?");

                if (ref == 0) {
                    int returnVal = gui.fc_save.showSaveDialog(gui.code_text_area);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        String filename = gui.fc_save.getSelectedFile().getAbsolutePath();
                        svgutils.exp.Convert(Data.temp_output_svgfile_name, filename + ".svg", ".svg");
                        if (!filename.endsWith(".txt")) {
                            filename = filename + ".txt";
                        }
                        File file1 = new File(filename);
                        if (file1.exists()) {
                            try {
                                FileWriter fw = new FileWriter(file1);
                                fw.write(gui.code_text_area.getText());
                                fw.close();
                            } catch (IOException ioe) {
                                System.err.println("IOException: " + ioe.getMessage());
                            }
                        }//if
                        else {
                            FileWriter fw = null;
                            try {
                                fw = new FileWriter(file1.getAbsoluteFile(), true);
                                gui.code_text_area.write(fw);
                            } catch (IOException en) {
                                System.out.println("could not save the file");
                            }
                        }//else
                    }
                }
            }
        }
    }//end of class

    /**
     * This class handles event for svg canvas when user chooses "open link in seperate window"
     * option.
     */
    public class SeperateWindowHandling implements SVGDocumentLoaderListener {

        @Override
        public void documentLoadingCancelled(SVGDocumentLoaderEvent arg0) {

        }

        @Override
        public void documentLoadingCompleted(SVGDocumentLoaderEvent arg0) {
            System.out.println("document loaded***************");
            String filename = gui.linkwindow.seperate_svg_display.getSVGDocument().getDocumentURI().toString();
            File f = new File(filename);
            gui.linkwindow.filename_label.setText(f.getName().replaceFirst(".svg", ""));
        }

        @Override
        public void documentLoadingFailed(SVGDocumentLoaderEvent arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void documentLoadingStarted(SVGDocumentLoaderEvent arg0) {
            if (Data.open_ref_new_window == true) {
                svgutils.reload(gui.svgcanvas, Data.temp_output_svgfile_name);
                System.out.println("loading********");
            }
        }
    }

    /**
     * The Class GenerateLinks for referencing function.
     */
    public class GenerateLinks implements ActionListener {


        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            String Object_name = gui.ref_screen.ref_object_name.getText().trim();
            String ref_message = "";
            if (Object_name == null || Object_name.equals("")) {
                Object_name = "Object 1";
            }

            if (gui.ref_screen.rdbtnNewRadioButton.isSelected()) {// Ref On Object is selected
                String link = "";
                try {

                    link = "[[" + gui.fc_reference.getSelectedFile().toURL() + " " + gui.fc_reference.getSelectedFile().getName() + "]]";
                } catch (MalformedURLException e1) {
                    System.out.println("problem creating link");
                }
                ref_message = "participant " + " \" " + Object_name.trim() + " &<u>" + link + "\" as " + Object_name.trim();

                String code = gui.code_text_area.getText();
                if (code.contains("@startuml")) {
                    code = code.replaceFirst("@startuml", "@startuml\n" + ref_message);
                    code = code.replace("&", "\\n");
                }
                ref_message = "";

                gui.code_text_area.setText(code);
            } else {
                String link = "";
                try {
                    link = "[[" + gui.fc_reference.getSelectedFile().toURL() + " " + gui.fc_reference.getSelectedFile().getName() + "]]";
                } catch (MalformedURLException e1) {
                    System.out.println("problem creating link");
                }
                ref_message = "ref over " + Object_name.trim() + " : init" + link;
                int get_position = gui.code_text_area.getCaretPosition();

                if (get_position > 0) {
                    gui.code_text_area.insert(ref_message, gui.code_text_area.getCaretPosition());
                } else {
                    gui.code_text_area.append(ref_message);
                }
            }//else
            svgutils.writeSvgToDefaultFile(gui.code_text_area.getText());
            svgutils.reload(gui.svgcanvas, Data.temp_output_svgfile_name);
            gui.ref_screen.dispose();
            gui.ref_screen.ref_object_name.setText("");
            gui.ref_screen.group.clearSelection();
            gui.ref_screen.rdbtnNewRadioButton.setSelected(true);
            gui.fc_reference.setSelectedFile(new File(""));
        }// end of action

    }//generate links

    /**
     * Saves current file
     */
    public class SaveItem implements ActionListener {

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            //In response to a button click:
            int returnVal = gui.fc_save.showSaveDialog(gui.code_text_area);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filename = gui.fc_save.getSelectedFile().getAbsolutePath();
                svgutils.exp.Convert(Data.temp_output_svgfile_name, filename + ".svg", ".svg");
                if (!filename.endsWith(".txt")) {
                    filename = filename + ".txt";
                }
                File file1 = new File(filename);

                if (file1.exists()) {
                    try {
                        FileWriter fw = new FileWriter(file1);
                        fw.write(gui.code_text_area.getText());
                        fw.close();
                    } catch (IOException ioe) {
                        System.err.println("IOException: " + ioe.getMessage());
                    }
                }//if

                else {
                    FileWriter fw = null;
                    try {
                        fw = new FileWriter(file1.getAbsoluteFile(), true);
                        gui.code_text_area.write(fw);
                    } catch (IOException en) {
                        System.out.println("could not save the file");
                    }
                }//else
                Data.is_saved = true;
                Data.currentfile = gui.fc_save.getSelectedFile().getAbsolutePath();
                gui.save_button.setEnabled(false);
                gui.menuitem_save.setEnabled(false);
                gui.rdbtnmntmOpenInNew.setEnabled(true);
                gui.chckbxmntmIncrementalModel.setEnabled(true);
                gui.filename_label.setText(gui.fc_save.getSelectedFile().getName().replaceFirst(".txt", "").toString());
            }
        }
    }// end of saveitem class

    /**
     * Loads item from text
     */
    public class LoadItemToText implements ActionListener {

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            int return_val = gui.fc_load.showOpenDialog(gui.code_text_area);
            if (return_val == JFileChooser.APPROVE_OPTION) {
                try {
                    gui.code_text_area.read(new BufferedReader(new FileReader(gui.fc_load.getSelectedFile().getAbsolutePath())), null);
                    gui.validation_label.setText(svgutils.writeSvgToDefaultFile(gui.code_text_area.getText()));
                    svgutils.reload(gui.svgcanvas, Data.temp_output_svgfile_name);
                    Data.currentfile = gui.fc_load.getSelectedFile().getAbsolutePath();
                    gui.filename_label.setText(gui.fc_load.getSelectedFile().getName().replaceFirst(".txt", "").toString());
                    Data.is_saved = true;
                    gui.save_button.setEnabled(false);
                    gui.menuitem_save.setEnabled(false);
                    gui.rdbtnmntmOpenInNew.setEnabled(true);
                    gui.chckbxmntmIncrementalModel.setEnabled(true);
                } catch (FileNotFoundException en) {
                    System.out.println("could not find svg file");
                } catch (IOException en) {
                    System.out.println("could not load the file");
                }
                gui.save_button.setEnabled(false);
                gui.menuitem_save.setEnabled(false);
            }//approve option
        }
    }//end of LoadItemToText

    /**
     * Exports sequence diagrams in different formats
     */
    public class ExportDiagram implements ActionListener {

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent arg0) {
            int return_val = gui.fc_export.showSaveDialog(gui.svgcanvas);
            if (return_val == JFileChooser.APPROVE_OPTION) {

                String input_file_format = Data.pruneFilePath(gui.svgcanvas.getSVGDocument().getDocumentURI());
                String output_file_format = gui.fc_export.getSelectedFile().getAbsolutePath();
                String file_format_option = gui.fc_export.getFileFilter().getDescription();
                if (!output_file_format.endsWith(".png") && file_format_option.equalsIgnoreCase(".png")) {
                    output_file_format = output_file_format + ".png";
                } else if (!output_file_format.endsWith(".jpg") && file_format_option.equalsIgnoreCase(".jpg")) {
                    output_file_format = output_file_format + ".jpg";
                } else if (!output_file_format.endsWith(".svg") && file_format_option.equalsIgnoreCase(".svg")) {
                    output_file_format = output_file_format + ".svg";
                }
                svgutils.exp.Convert(input_file_format, output_file_format, gui.fc_export.getFileFilter().getDescription());
                System.out.println(gui.svgcanvas.getURI());
            }
        }

    }//end of export diagram

    /**
     * The listener interface for receiving backButton events.
     * The class that is interested in processing a backButton
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addBackButtonListener<code> method. When
     * the backButton event occurs, that object's appropriate
     * method is invoked.
     *
     * @see BackButtonEvent
     */
    public class BackButtonListener implements ActionListener {

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            if (Data.browse_file_names.size() > 0) {
                String load_path = Data.browse_file_names.pop();
                File f = new File(load_path);
                gui.code_text_area.setText("");
                try {
                    gui.code_text_area.read(new BufferedReader(new FileReader(f)), null);
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(gui.code_text_area, "Could not find the file");
                    svgutils.reload(gui.svgcanvas, Data.temp_output_svgfile_name);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                svgutils.writeSvgToDefaultFile(gui.code_text_area.getText());
                svgutils.reload(gui.svgcanvas, Data.temp_output_svgfile_name);
                Data.currentfile = load_path;
                gui.filename_label.setText(f.getName().replaceFirst(".txt", ""));
            } else {
                gui.back_button.setEnabled(false);
            }
            if (Data.browse_file_names.size() == 0) {
                gui.back_button.setEnabled(false);
            }
        }// Data.browse_file_names.size()>0
    }//end of bakclistener class

}//end of control class
