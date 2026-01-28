package copy_in_order.ui.j_components.subfolder_list;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class SubfolderJList extends JList<File> {

	private static final long serialVersionUID = -6283577688791254940L;

	private final DefaultListModel<File> defaultListModel = new DefaultListModel<>();
	private final FileListCellRenderer listCellRenderer;
	private final Map<File, Boolean> selectedFiles = new HashMap<>();
	
	public SubfolderJList() {
		this.setModel(this.defaultListModel);
		this.listCellRenderer = new FileListCellRenderer(this.selectedFiles);
		this.setCellRenderer(this.listCellRenderer);
		this.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	if (listCellRenderer.isEnabled()) {
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
		repaint(getCellBounds(0, this.getModel().getSize() - 1));
	}
	
	private void recreateSelectionModel(Iterable<File> files) {
		this.selectedFiles.clear();
		for (File file : files) {
			this.selectedFiles.put(file, false);
		}
	}
}
