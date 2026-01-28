package copy_in_order.ui.j_components.subfolder_list;

import java.awt.Component;
import java.io.File;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Class that renders each cell in a {@link JList} with a checkbox that
 * has the name of a file.
 */
public class FileListCellRenderer implements ListCellRenderer<File> {

	/**
	 * The files that are selected, which determines
	 * which checkboxes are rendered as selected.
	 */
	private final Map<File, Boolean> selectedFiles;
	
	/**
	 * Whether the checkboxes will be rendered as enabled or not.
	 */
	private boolean enabled;
	
	/**
	 * Creates a new renderer for checkboxes.
	 * @param selectedFiles The files that are selected, which determines
	 * which checkboxes are rendered as selected.
	 */
	public FileListCellRenderer(Map<File, Boolean> selectedFiles) {
		this.selectedFiles = selectedFiles;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getListCellRendererComponent(JList<? extends File> list, File value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Boolean isCheckboxSelected = this.isSelected(value);
		String name = value.getName();
		JCheckBox checkBox = new JCheckBox(name);
		checkBox.setSelected(isCheckboxSelected);
		checkBox.setEnabled(this.enabled);
		return checkBox;
	}
	
	/**
	 * Makes the checkboxes in the list be renderer as enabled or disabled
	 * depending on the given value.
	 * @param enabled Whether to enable the checkboxes or not.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	private boolean isSelected(File file) {
		Boolean result = this.selectedFiles.get(file);
		return result != null && result;
	}
}
