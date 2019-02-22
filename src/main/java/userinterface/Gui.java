

package userinterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.ComponentOrientation;
import java.awt.Desktop.Action;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextPane;
import java.awt.Rectangle;
import java.awt.FlowLayout;
import java.awt.Label;
import javax.swing.BoxLayout;
import javax.xml.ws.handler.MessageContext.Scope;

import java.awt.Button;
import java.awt.List;
import javax.swing.JList;
import javax.swing.AbstractListModel;

import org.apache.batik.ext.awt.image.URLImageCache;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.LinkActivationListener;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;


import net.miginfocom.swing.MigLayout;
import net.sourceforge.plantuml.Url;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager.*;
import Controller.Control;
import Svg.*;
import Controller.*;


public class Gui extends JFrame {

	public JPanel contentPane;
	public JSVGCanvas svgcanvas;
	public JScrollPane svgpane;
	public JMenuBar menuBar;
	public JMenu mnFile;
	public JMenuItem menuitem_save;
	public JMenuItem menuitem_load;
	public JMenu mnPreferences;
	public JCheckBoxMenuItem rdbtnmntmOpenInNew;
	public JPanel top_element_panel;
	public JPanel top_button_panel;
	public JButton reference_button;
	public JButton note_button;
	public JButton back_button;
	public JLabel validation_label;
	public JPanel left_list_panel;
	public JList list;
	public JScrollPane listpane;
	public RSyntaxTextArea code_text_area;
	public RTextScrollPane code_text_pane;
	public Svg svgjobs;
	public DefaultListModel<DrawerItem> model =null;
	public JFileChooser fc_save;
	public JFileChooser fc_load;
	public JFileChooser fc_export;
	public JFileChooser fc_reference;
	public JPopupMenu Pmenu;
	public JPopupMenu Pmenu1;
	public JMenuItem popupmenu_item_export;
	public JMenuItem popupmenu_item_ref;
	public SeperateDisplay linkwindow;
	public PreviewPane preview;
	public NoteGui note_screen;
	public JCheckBoxMenuItem chckbxmntmRemove;
	public RefGui ref_screen;
	public JButton save_button;
	public JLabel filename_label;
	public JCheckBoxMenuItem chckbxmntmIncrementalModel;
 

	public static void main(String[] args) {
		 SplashScreen splash = new SplashScreen(1000);
		 splash.showSplashAndExit();		 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Svg svg_model= new Svg();
					Gui frame = new Gui(svg_model);
					frame.setVisible(true);
					Control control = new Control(frame,svg_model);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"could not load the user interface ");
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,"could not load system theme");
					}   
				}
			}

		});
		
	}

	/**
	 * Create the frame.
	 */
	public Gui(Svg temp) {
	 
	
		svgjobs=temp;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 611);

		note_screen=new NoteGui();
		Pmenu = new JPopupMenu();
		ref_screen = new RefGui();
		popupmenu_item_export = new JMenuItem("Export");
		Pmenu.add(popupmenu_item_export);
		setTitle("Puse");

		Pmenu1 = new JPopupMenu();
		popupmenu_item_ref= new JMenuItem("Ref This");
		Pmenu1.add(popupmenu_item_ref);

		fc_save = new JFileChooser();
		fc_load = new JFileChooser();


		fc_export = new JFileChooser();
		fc_reference = new JFileChooser();

		preview = new PreviewPane();
		fc_reference .setAccessory(preview);
		fc_reference .addPropertyChangeListener(preview);

		FileNameExtensionFilter save_filter = new FileNameExtensionFilter(".txt","txt","*.txt");
		fc_save.setFileFilter(save_filter);

		FileNameExtensionFilter export_filter_png = new FileNameExtensionFilter(".png","png",".png");
		FileNameExtensionFilter export_filter_jpg = new FileNameExtensionFilter(".jpg","jpg",".jpg");
		FileNameExtensionFilter export_filter_svg = new FileNameExtensionFilter(".svg","svg",".svg");

		fc_export.setFileFilter(export_filter_svg);
		fc_export.setFileFilter(export_filter_jpg);
		fc_export.setFileFilter(export_filter_png);

		fc_load.setFileFilter(new javax.swing.filechooser.FileFilter() {

			@Override
			public String getDescription() {
				return "txt";
			}
			@Override
			public boolean accept(File arg0) {
				if(arg0.isDirectory())
					return arg0.isDirectory();
				else
					return arg0.getName().endsWith(".txt");
			}
		});

		svgcanvas = new JSVGCanvas();
		linkwindow = new SeperateDisplay(svgjobs,svgcanvas);
		svgpane = new JScrollPane(svgcanvas);
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		linkwindow = new SeperateDisplay(svgjobs,svgcanvas);
		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		menuitem_save = new JMenuItem("Save");
		menuitem_save.setIcon(new ImageIcon(Gui.class.getResource("/icons/1362784520_Save.png")));
		mnFile.add(menuitem_save);

		menuitem_load = new JMenuItem("Load");
		menuitem_load.setIcon(new ImageIcon(Gui.class.getResource("/icons/1362784627_folderopen1.png")));
		mnFile.add(menuitem_load);

		mnPreferences = new JMenu("Preferences");
		menuBar.add(mnPreferences);

		rdbtnmntmOpenInNew = new JCheckBoxMenuItem("Open references in new window");
		rdbtnmntmOpenInNew.setEnabled(false);
		rdbtnmntmOpenInNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnmntmOpenInNew.isSelected()==true){
					Data.open_ref_new_window=true;
				}
				else{
					Data.open_ref_new_window=false;
				}
			}
		});

		mnPreferences.add(rdbtnmntmOpenInNew);
		chckbxmntmRemove = new JCheckBoxMenuItem("Remove Footer");
		mnPreferences.add(chckbxmntmRemove);

		chckbxmntmIncrementalModel = new JCheckBoxMenuItem("Incremental Model");
		chckbxmntmIncrementalModel.setEnabled(false);
		chckbxmntmIncrementalModel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if(chckbxmntmIncrementalModel.isSelected()==true){
					rdbtnmntmOpenInNew.setEnabled(false);
					Data.open_ref_new_window=false;
					Data.incremental_model=true;
				}
				else{
					rdbtnmntmOpenInNew.setEnabled(true);
					Data.open_ref_new_window=true;
					Data.incremental_model=false;
				}

			}
		});
		mnPreferences.add(chckbxmntmIncrementalModel);
		chckbxmntmRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(chckbxmntmRemove.isSelected()){
					Data.remove_footer=true;
				}
				else{
					Data.remove_footer=false;
				}
			}
		}); 

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(5, 0));

		top_element_panel = new JPanel();
		contentPane.add(top_element_panel, BorderLayout.NORTH);
		top_element_panel.setLayout(new BoxLayout(top_element_panel, BoxLayout.Y_AXIS));

		top_button_panel = new JPanel();
		top_element_panel.add(top_button_panel);

		save_button = new JButton(" ");
		save_button.setIcon(new ImageIcon(Gui.class.getResource("/icons/1364664775_stock_save.png")));
		top_button_panel.add(save_button);

		reference_button = new JButton("");
		reference_button.setIcon(new ImageIcon(Gui.class.getResource("/icons/refico.png")));
		reference_button.setToolTipText("refer to other diagram ");
		top_button_panel.add(reference_button);

		note_button = new JButton("");
		note_button.setToolTipText("add note to diagram ");
		note_button.setIcon(new ImageIcon(Gui.class.getResource("/icons/1361839538_stock_insert-note.png")));
		top_button_panel.add(note_button);

		back_button = new JButton("");
		back_button.setEnabled(false);
		back_button.setToolTipText("back to previous diagram");
		back_button.setIcon(new ImageIcon(Gui.class.getResource("/icons/1363206701_import.png")));
		top_button_panel.add(back_button);


		validation_label = new JLabel(" ");
		top_element_panel.add(validation_label);

		filename_label = new JLabel(" ");
		top_element_panel.add(filename_label);

		left_list_panel = new JPanel();
		contentPane.add(left_list_panel, BorderLayout.WEST);
		left_list_panel.setLayout(new GridLayout(0, 1, 0, 0));


		GuideList fill_list = new GuideList();

		model= fill_list.loadItems();
		list = new JList();

		list.setModel(model);

		listpane=new JScrollPane(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setDropMode(DropMode.USE_SELECTION);
		list.setDragEnabled(true);
		list.setTransferHandler(new ListHandler());
		left_list_panel.add(listpane);
		contentPane.add(svgpane,BorderLayout.SOUTH);
		code_text_area = new RSyntaxTextArea(20,60);
		code_text_pane = new RTextScrollPane(code_text_area);
		code_text_area.setCodeFoldingEnabled(true);
		code_text_area.setAntiAliasingEnabled(true);
		AutoComplete complete = new AutoComplete(code_text_area);
		complete.installGrammar("/autocompletewords.txt");
		contentPane.add(code_text_pane, BorderLayout.CENTER);
		svgcanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderListener() {

			@Override
			public void documentLoadingStarted(SVGDocumentLoaderEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void documentLoadingFailed(SVGDocumentLoaderEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void documentLoadingCompleted(SVGDocumentLoaderEvent arg0) {
				if(Data.incremental_model_link==true){

					File f = new  File(Data.temp_output_svgfile_name);
					try {
						svgcanvas.setURI(f.toURL().toString());
						System.out.println("reload is complete");
					} catch (MalformedURLException e) {
						System.out.println("could not load the svg");
					}
					Data.incremental_model_link=false;
				}//if
			}

			@Override
			public void documentLoadingCancelled(SVGDocumentLoaderEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		 
		
	}//end of  constructor

	public void addrightmenutocodeListener(MouseListener e){
		code_text_area.addMouseListener(e);

	}// end of listener

	public void addClosingListener(WindowAdapter w){
		addWindowListener(w);
	}


	public void addcodetextListener(KeyListener type){
		code_text_area.addKeyListener(type);
	}

	public void addsaveListener(ActionListener e){
		menuitem_save.addActionListener(e);
		save_button.addActionListener(e);
	}

	public void addloadListener(ActionListener e){
		menuitem_load.addActionListener(e);
	}

	public void addsvgcanvasListener(MouseListener m){
		svgcanvas.addMouseListener(m);
	}

	public void addpopupactionListener(ActionListener e){
		popupmenu_item_export.addActionListener(e);
	}

	public void adddynamicrefListener(ActionListener e){
		popupmenu_item_ref.addActionListener(e);
	}

	public void addlinkactivationListener(LinkActivationListener l){
		svgcanvas.addLinkActivationListener(l);
	}

	public void addreferenceListener(ActionListener e){
		reference_button.addActionListener(e);
	}

	public void addbackListener(ActionListener e){
		back_button.addActionListener(e);
	}

	public void addgeneratelinkListener(ActionListener e){
		ref_screen.btnAddReference.addActionListener(e);
	}

	public void addnoteListener(){
		note_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				note_screen.setVisible(true);
			}
		});

	}

}//end of class 