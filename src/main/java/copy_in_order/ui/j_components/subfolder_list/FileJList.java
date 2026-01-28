package copy_in_order.ui.j_components.subfolder_list;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

/**
 * An implementation of {@link JList} that contains a list of files together
 * with checkboxes for selecting them.
 */
public class FileJList extends JList<File> {

	private static final long serialVersionUID = -6283577688791254940L;

	private final DefaultListModel<File> defaultListModel = new DefaultListModel<>();
	private final FileListCellRenderer listCellRenderer;
	private final Map<File, Boolean> selectedFiles = new HashMap<>();
	
	/**
	 * Creates a new list of files.
	 */
	public FileJList() {
		this.setModel(this.defaultListModel);
		this.listCellRenderer = new FileListCellRenderer(this.selectedFiles);
		this.setCellRenderer(this.listCellRenderer);
		this.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	/*
		    	 * When the list is not enabled we do not want its checkboxes to respond
		    	 * to clicks.
		    	 */
		    	if (isEnabled()) {
			        int index = locationToIndex(e.getPoint());
			        if (index >= 0) {
			        	File file = getModel().getElementAt(index);
			        	selectedFiles.put(file, !selectedFiles.get(file));
			            repaint(getCellBounds(index, index));
			        }
		    	}
		    }
		});
	}

	/**
	 * Updates the model of this list to the given collection of files.
	 * @param files The collection of files that will be painted on this
	 * list.
	 */
	public void updateModel(Collection<File> files) {
		this.defaultListModel.clear();
		
		// Not resetting the selection model before repopulating the list model
		// would make it so slow it looks as if the program had crashed.
		ListSelectionModel listSelectionModel = this.getSelectionModel();
		listSelectionModel.clearSelection();
		listSelectionModel.setAnchorSelectionIndex(-1);
		listSelectionModel.setLeadSelectionIndex(-1);
		
		this.recreateSelectionModel(files);
		this.defaultListModel.addAll(files);
	}

	/**
	 * Enables or disables the list. When the list is disabled, all its checkboxes
	 * will be automatically selected. When the list is disabled, all its
	 * checkboxes will be automatically deselected.
	 * @param enabled Whether to enable this list or not.
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (File file : this.selectedFiles.keySet()) {
			this.selectedFiles.put(file, !enabled);
		}
		this.listCellRenderer.setEnabled(enabled);
		if (this.getModel().getSize() > 0) {
			/*
			 * In this case the list has to be repainted to reflect the new
			 * state of the checkboxes.
			 */
			repaint(getCellBounds(0, this.getModel().getSize() - 1));
		}
	}
	
	/**
	 * Returns the files that are selected on this list.
	 * @return The files that are selected on this list.
	 */
	public Set<File> getSelectedFiles() {
		Set<File> result = new HashSet<>();
		Boolean selected;
		for (File file : this.selectedFiles.keySet()) {
			selected = this.selectedFiles.get(file);
			if (selected != null && selected) {
				result.add(file);
			}
		}
		return result;
	}
	
	private void recreateSelectionModel(Iterable<File> files) {
		this.selectedFiles.clear();
		for (File file : files) {
			this.selectedFiles.put(file, false);
		}
	}
}
