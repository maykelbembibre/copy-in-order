package copy_in_order.ui.workers;

import java.awt.Component;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import copy_in_order.logic.FileManager;
import copy_in_order.logic.exceptions.FileManagementException;
import copy_in_order.ui.listeners.FileCopyPropertyChangeListener;

/**
 * An asynchronous task that copies files in order from one
 * directory to another and updates the progress in the GUI.
 */
public class FileCopyTask extends SwingWorker<Void, Void> {

	private static final Collection<String> EXTENSIONS = Arrays.asList("mp3");
	private final File sourceDirectory;
	private final File destinationDirectory;
	private final JTextArea statusNote;
	private final Collection<Component> sensitiveComponents;
	private final Component stopButton;
	private String error;
	private FileManager fileManagement;
	private volatile int totalFiles;
	private volatile int copiedFiles;
	
	/**
	 * Constructor.
	 * @param sourceDirectory The source directory.
	 * @param destinationDirectory The destination directory.
	 * @param statusNote The status note.
	 * @param sensitiveComponents The components that have to be enabled
	 * only when the task ends.
	 * @param stopButton The button that makes this task stop.
	 */
    public FileCopyTask(
    	File sourceDirectory, File destinationDirectory,
    	JTextArea statusNote, Collection<Component> sensitiveComponents,
    	Component stopButton
    ) {
    	this.sourceDirectory = sourceDirectory;
    	this.destinationDirectory = destinationDirectory;
		this.statusNote = statusNote;
		this.sensitiveComponents = sensitiveComponents;
		this.stopButton = stopButton;
	}

    /**
     * Main task. Executed in background thread. Note that this method is
     * executed only once.
     * @return <code>null</code>.
     */
    @Override
    public Void doInBackground() {
    	try {
    		this.setProgress(0);
    		this.statusNote.setText("Calculating data...");
			this.fileManagement = new FileManager(this.sourceDirectory, this.destinationDirectory, EXTENSIONS);
			this.totalFiles = this.countFilesRecursively(this.sourceDirectory);
			this.copiedFiles = 0;
			this.statusNote.setText(
				FileCopyPropertyChangeListener.createStatusNoteText(this.copiedFiles, this.totalFiles, this.getProgress())
			);
			this.copyRecursively(this.sourceDirectory, this.destinationDirectory, true);
		} catch (FileManagementException e) {
			this.error(e.getMessage());
		} catch (FileSystemException e) {
			this.error("It looks like an external storage device has been removed. The operation can't go on.");
		} catch (IOException e) {
			e.printStackTrace();
			this.error("OS file system error.");
		}
        return null;
    }
    
    /**
     * Returns the total number of files.
     * @return The total number of files.
     */
    public int getTotalFiles() {
		return totalFiles;
	}

    /**
     * Returns the number of files that have already been copied.
     * @return The copied files.
     */
	public int getCopiedFiles() {
		return copiedFiles;
	}

	/**
	 * Executed in event dispatching thread. This method is called when the task
	 * finishes.
	 */
    @Override
    public void done() {
        Toolkit.getDefaultToolkit().beep();
        for (Component component : this.sensitiveComponents) {
        	component.setEnabled(true);
        }
        
        // When a task ends, it can't be stopped anymore.
        this.stopButton.setEnabled(false);
        
        if (this.error != null) {
        	this.statusNote.setText("Error: " + this.error);
        } else if (this.isCancelled()) {
        	this.statusNote.setText("Task cancelled.");
        } else {
        	this.statusNote.setText("File copy has been completed.");
        }
    }
	
	private int countFilesRecursively(File fromFolder) {
		Iterator<File> children = this.fileManagement.getChildren(fromFolder).iterator();
		File child;
		int count = 0;
    	while (!this.isCancelled() && children.hasNext()) {
    		child = children.next();
    		if (child.isFile()) {
    			count++;
    		} else {
    			count = count + countFilesRecursively(child);
    		}
    	}
		return count;
	}

	private void copyRecursively(File fromFolder, File toFolder, boolean root) throws IOException {
		Iterator<File> fromFolderChildren = this.fileManagement.getChildren(fromFolder).iterator();
		File fromFolderChild;
		while (!this.isCancelled() && fromFolderChildren.hasNext()) {
    		fromFolderChild = fromFolderChildren.next();
    		if (fromFolderChild.isFile()) {
    			FileManager.copyFileToFolder(fromFolderChild, toFolder);
    			this.copiedFiles++;
    			this.setProgress(Math.min(this.copiedFiles * 100 / this.totalFiles, 100));
    		} else {
    			File toFolderChild = new File(toFolder, fromFolderChild.getName());
    			if (!toFolderChild.isFile()) {
    				FileManager.createIfNotExists(toFolderChild);
    				this.copyRecursively(fromFolderChild, toFolderChild, false);
    				FileManager.deleteIfEmpty(toFolderChild);
    			}
    		}
    	}
    }
    
    private void error(String error) {
    	this.cancel(true);
    	this.error = error;
    }
}