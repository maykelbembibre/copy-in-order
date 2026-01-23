package copy_in_order.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import copy_in_order.ui.j_components.DirectoryChooser;
import copy_in_order.ui.j_components.HorizontalSeparator;
import copy_in_order.ui.j_components.JFileTextField;
import copy_in_order.ui.j_components.ReadOnlyJTextArea;
import copy_in_order.ui.listeners.FileCopyPropertyChangeListener;
import copy_in_order.ui.workers.FileCopyTask;

/**
 * The window containing all the GUI for this application.
 */
public class AppWindow extends JFrame {
	
	private static final long serialVersionUID = 2516782550252442192L;

	private final static int GAP = 10;
	
	private final JPanel contentPane;
	private final JFileChooser directoryChooser = new DirectoryChooser();
	private JFileTextField sourceFileTextField = createJFileTextField();
	private JFileTextField destinationFileTextField = createJFileTextField();
	private JButton sourceFileChooseButton;
	private JButton destinationFileChooseButton;
	private FileCopyTask task;
	
	/**
	 * Creates the window.
	 */
	public AppWindow() {
		//Set up the window.
	    this.setTitle("Copy in order");
	    this.setMinimumSize(new Dimension(450, 350));
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	    //Create the menu bar.
	    JMenuBar menuBar = new JMenuBar();
	    menuBar.setOpaque(true);
	
	    //Create a panel.
	    this.contentPane = new JPanel(new BorderLayout());
	    this.setContentPane(contentPane);
	    
	    //Set the menu bar.
	    this.setJMenuBar(menuBar);
	    
	    this.drawContentPane();
	
	    //Display the window.
	    this.setVisible(true);
	}
	
	private static void adjustTextField(JTextField jTextField) {
		jTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, jTextField.getPreferredSize().height));
	}
	
	private static JFileTextField createJFileTextField() {
		JFileTextField jFileTextField = new JFileTextField();
		adjustTextField(jFileTextField);
		return jFileTextField;
	}
	
	private JButton addFolderSelectionComponents(Container verticalPanel, JFileTextField fileTextField, String label) {
		JPanel verticalPanelContent = new JPanel();
		verticalPanelContent.setLayout(new BoxLayout(verticalPanelContent, BoxLayout.Y_AXIS));
		verticalPanelContent.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
		verticalPanelContent.add(new JLabel(label));
		verticalPanelContent.add(Box.createVerticalStrut(GAP));
		JPanel horizontalPanel = new JPanel();
	    BoxLayout horizontalLayout = new BoxLayout(horizontalPanel, BoxLayout.X_AXIS);
		horizontalPanel.setLayout(horizontalLayout);
	    
	    /*
	     * Necessary so the horizontal panel and the labels are aligned
	     * the same way horizontally.
	     */
	    horizontalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
		horizontalPanel.add(fileTextField);
		horizontalPanel.add(Box.createRigidArea(new Dimension(GAP, 0)));
		JButton selectDirectoryButton = new JButton("Select folder");
		selectDirectoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = AppWindow.this.directoryChooser.showOpenDialog(verticalPanel);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = AppWindow.this.directoryChooser.getSelectedFile();
		            fileTextField.setSelectedFile(file);
		        } else {
		        	// Open command cancelled by user.
		        }
			}});
		horizontalPanel.add(selectDirectoryButton);
		verticalPanelContent.add(horizontalPanel);
		verticalPanel.add(verticalPanelContent);
		verticalPanel.add(new HorizontalSeparator());
		return selectDirectoryButton;
	}
	
	private JPanel drawFileCopyPanel() {
		JPanel fileCopyPanel = new JPanel(new BorderLayout());

		JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
		
        JTextArea taskOutput = new ReadOnlyJTextArea();
        
		JButton startButton = new JButton("Copy files");
		JButton stopButton = new JButton("Stop");
		Collection<Component> sensitiveComponents = Arrays.asList(
			startButton, this.sourceFileTextField, this.destinationFileTextField,
			this.sourceFileChooseButton, this.destinationFileChooseButton
		);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File sourceDirectory = AppWindow.this.sourceFileTextField.getSelectedFile();
				File destinationDirectory = AppWindow.this.destinationFileTextField.getSelectedFile();
				
				progressBar.setValue(0);
		    	taskOutput.setText("");
		    	for (Component component : sensitiveComponents) {
		    		component.setEnabled(false);
		    	}
		        
		        //Instances of javax.swing.SwingWorker are not reusuable, so
		        //we create new instances as needed.
		        task = new FileCopyTask(
		        	sourceDirectory, destinationDirectory, taskOutput, sensitiveComponents, stopButton
		        );
		        
		        PropertyChangeListener propertyChangeListener = new FileCopyPropertyChangeListener(
	            	progressBar, taskOutput, task
	            );
		        task.addPropertyChangeListener(propertyChangeListener);
		        task.execute();
		        
		        // After a task starts, the user has the possibility of stopping it.
		        stopButton.setEnabled(true);
			}
		});
		stopButton.setEnabled(false);
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// It's better that as soon as the stop button is clicked, it can't get clicked again.
				stopButton.setEnabled(false);
				
				if (task != null) {
	                task.cancel(true);
				}
			}
		});
		
        JPanel panel = new JPanel();
        BoxLayout horizontalLayout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(horizontalLayout);
        panel.add(startButton);
        panel.add(Box.createRigidArea(new Dimension(GAP, 0)));
        panel.add(stopButton);
        panel.add(Box.createRigidArea(new Dimension(GAP, 0)));
        panel.add(progressBar);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, GAP, 0));
 
        fileCopyPanel.add(panel, BorderLayout.PAGE_START);
        fileCopyPanel.add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        fileCopyPanel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        
        return fileCopyPanel;
	}
	
	private void drawContentPane() {
		JPanel verticalPanel = new JPanel();
		verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
		this.contentPane.add(verticalPanel, BorderLayout.CENTER);
		this.sourceFileChooseButton = addFolderSelectionComponents(
			verticalPanel, this.sourceFileTextField, "Source folder"
		);
		this.destinationFileChooseButton = addFolderSelectionComponents(
			verticalPanel, this.destinationFileTextField, "Destination folder"
		);
		JPanel fileCopyPanel = drawFileCopyPanel();
		
		/*
	     * Necessary so the horizontal panel and the labels are aligned
	     * the same way horizontally.
	     */
		fileCopyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		verticalPanel.add(fileCopyPanel);
	}
}
