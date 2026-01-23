package copy_in_order.ui.j_components;

import java.io.File;

import javax.swing.JTextField;

/**
 * A {@link JTextField} whose content is the absolute path of a file.
 */
public class JFileTextField extends JTextField {

	private static final long serialVersionUID = -7579550663906486422L;
	
	private File selectedFile;

	/**
	 * Creates a text field whose content is the absolute path of a file.
	 */
	public JFileTextField() {
		this.addCaretListener(e -> {
		    String currentVal = this.getText();
		    if (currentVal == null || currentVal.isEmpty()) {
		    	this.selectedFile = null;
		    } else {
		    	this.selectedFile = new File(currentVal);
		    }
		});
	}
	
	/**
	 * Returns the selected file.
	 * @return The selected File.
	 */
	public File getSelectedFile() {
		return selectedFile;
	}

	/**
	 * Sets the selected file.
	 * @param selectedFile The selected file.
	 */
	public void setSelectedFile(File selectedFile) {
		this.setText(selectedFile.getAbsolutePath());
	}
}
