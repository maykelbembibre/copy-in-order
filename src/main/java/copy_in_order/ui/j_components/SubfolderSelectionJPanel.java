package copy_in_order.ui.j_components;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SubfolderSelectionJPanel extends JPanel {

	private static final long serialVersionUID = 1503627171176547121L;

	public SubfolderSelectionJPanel() {
		super(new BorderLayout());
		JPanel pageStartPanel = new JPanel();
		pageStartPanel.setLayout(new BoxLayout(pageStartPanel, BoxLayout.Y_AXIS));
		JLabel subfolderSelectionJLabel = new JLabel("Subfolder selection");
		subfolderSelectionJLabel.setAlignmentX(LEFT_ALIGNMENT);
		pageStartPanel.add(subfolderSelectionJLabel);
		
		JRadioButton allButton = new JRadioButton("Copy all subfolders");
	    allButton.setSelected(true);
	    JRadioButton onlySelectedButton = new JRadioButton("Copy only selected subfolders");

	    //Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(allButton);
	    group.add(onlySelectedButton);
	    
	    //Put the radio buttons in a column in a panel.
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));
        radioPanel.setAlignmentX(LEFT_ALIGNMENT);
        radioPanel.add(allButton);
        radioPanel.add(onlySelectedButton);
        pageStartPanel.add(radioPanel);
        
        this.add(pageStartPanel, BorderLayout.PAGE_START);
	}
	
	/**
	 * Draws on this panel the list of subfolders of the given folder.
	 * @param file A folder. It must not be <code>null</code>. If it is
	 * not a folder but a regular file, nothing will be done.
	 */
	public void drawSubFolders(File file) {
		if (file.isDirectory()) {
			
		}
	}
}
