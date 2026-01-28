package copy_in_order.ui.j_components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import copy_in_order.logic.FileOrderManager;
import copy_in_order.ui.j_components.subfolder_list.FileJList;
import copy_in_order.ui.j_components.subfolder_list.NoSelectionModel;
import copy_in_order.ui.workers.FileCopyTask;

public class FileSelectionJPanel extends JPanel {

	private static final long serialVersionUID = 1503627171176547121L;

	private final JLabel noFilesJLabel;
	private final FileJList fileJList;
	private final JRadioButton allButton;
	private JComponent subfolderListScrollPane;
	
	public FileSelectionJPanel() {
		super(new BorderLayout());
		JPanel pageStartPanel = new JPanel();
		pageStartPanel.setLayout(new BoxLayout(pageStartPanel, BoxLayout.Y_AXIS));
		JLabel subfolderSelectionJLabel = new JLabel("Subfolder selection");
		subfolderSelectionJLabel.setAlignmentX(LEFT_ALIGNMENT);
		pageStartPanel.add(subfolderSelectionJLabel);
		
		this.allButton = new JRadioButton("Copy all subfolders");
		this.allButton.setSelected(true);
		this.allButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileJList.setEnabled(false);
			}
	    });
	    JRadioButton onlySelectedButton = new JRadioButton("Copy only selected subfolders");
	    onlySelectedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileJList.setEnabled(true);
			}
	    });
	    
	    //Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(this.allButton);
	    group.add(onlySelectedButton);
	    
	    //Put the radio buttons in a column in a panel.
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));
        radioPanel.setAlignmentX(LEFT_ALIGNMENT);
        radioPanel.add(this.allButton);
        radioPanel.add(onlySelectedButton);
        pageStartPanel.add(radioPanel);
        
        this.noFilesJLabel = new JLabel("No source folder has been selected.");
        pageStartPanel.add(this.noFilesJLabel);
        
        this.add(pageStartPanel, BorderLayout.PAGE_START);
        
        this.fileJList = new FileJList();
        this.fileJList.setSelectionModel(new NoSelectionModel());
        this.subfolderListScrollPane = new JScrollPane(this.fileJList);
        this.add(this.subfolderListScrollPane, BorderLayout.CENTER);
        this.reloadList(Collections.emptyList());
	}
	
	/**
	 * Draws on this panel the list of subfolders of the given folder.
	 * @param file A directory. It must not be <code>null</code>. If it is
	 * not a folder but a regular file, nothing will be done.
	 */
	public void drawSubFolders(File directory) {
		FileOrderManager fileOrderManager = new FileOrderManager(FileCopyTask.EXTENSIONS);
		List<File> subfolders;
		if (directory == null || !directory.isDirectory()) {
			subfolders = Collections.emptyList();
		} else {
			subfolders = fileOrderManager.getChildren(directory);
		}
		this.reloadList(subfolders);
		this.allButton.doClick();
	}
	
	/**
	 * Returns the files that are selected on the list that is on this panel.
	 * @return The files that are selected on the list that is on this panel.
	 */
	public Set<File> getSelectedFiles() {
		return this.fileJList.getSelectedFiles();
	}
	
	private void reloadList(List<File> files) {
		boolean empty = files.isEmpty();
		this.fileJList.updateModel(files);
		this.noFilesJLabel.setVisible(empty);
		this.subfolderListScrollPane.setVisible(!empty);
	}
}
