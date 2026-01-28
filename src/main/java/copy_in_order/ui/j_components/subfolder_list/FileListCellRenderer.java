package copy_in_order.ui.j_components.subfolder_list;

import java.awt.Component;
import java.io.File;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class FileListCellRenderer implements ListCellRenderer<File> {

	private final Map<File, Boolean> selectedFiles;
	private boolean enabled;
	
	public FileListCellRenderer(Map<File, Boolean> selectedFiles) {
		this.selectedFiles = selectedFiles;
	}

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
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	private boolean isSelected(File file) {
		Boolean result = this.selectedFiles.get(file);
		return result != null && result;
	}
}
