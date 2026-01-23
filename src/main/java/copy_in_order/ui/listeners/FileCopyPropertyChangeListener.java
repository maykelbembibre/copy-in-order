package copy_in_order.ui.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import copy_in_order.ui.workers.FileCopyTask;

/**
 * An object of this class must listen for changes in a
 * {@link FileCopyTask}.
 */
public class FileCopyPropertyChangeListener implements PropertyChangeListener {

	private final JProgressBar progressBar;
	private final JTextArea statusNote;
	private final FileCopyTask task;
	
	/**
	 * Constructor.
	 * @param progressBar The progress bar.
	 * @param statusNote The status note.
	 * @param task The asynchronous task.
	 */
	public FileCopyPropertyChangeListener(
		JProgressBar progressBar, JTextArea statusNote, FileCopyTask task
	) {
		this.progressBar = progressBar;
		this.statusNote = statusNote;
		this.task = task;
	}

	/**
	 * Creates the status note text.
	 * @param copiedFiles Number of copied files.
	 * @param totalFiles Total number of files.
	 * @param progress Progress 0 - 100.
	 * @return The status note text.
	 */
	public static String createStatusNoteText(int copiedFiles, int totalFiles, int progress) {
		String result;
		if (totalFiles > 0) {
			result = "Copied " + copiedFiles + "/" + totalFiles + " files.\nCompleted " +
			progress + "% of task.\n";
		} else {
			result = "";
		}
		return result;
	}
	
	/**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            int totalFiles = task.getTotalFiles();
            if (totalFiles > 0) {
	            statusNote.setText(
            		createStatusNoteText(task.getCopiedFiles(), task.getTotalFiles(), task.getProgress())
	            );
            }
        } 
    }
}
