package copy_in_order.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import copy_in_order.logic.exceptions.FileManagementException;

/**
 * Logic class for doing the file management operations.
 */
public class FileManager {

	private final File sourceDirectory;
	private final File destinationDirectory;
	private final FileOrderManager fileOrderManager;
	
	/**
	 * Creates a file management object.
	 * @param sourceDirectory The source directory.
	 * @param destinationDirectory The destination directory.
	 * @param selectedFiles The directories and files from the first level of the
	 * source directory's children that are selected for copy. If <code>null</code>,
	 * all directories and files will be copied. If empty, no directories or files
	 * will be copied.
	 * @param extensions Extensions of the files to copy.
	 * @throws FileManagementException If something goes wrong.
	 */
	public FileManager(
		File sourceDirectory, File destinationDirectory, Collection<String> extensions
	) throws FileManagementException {
		this.sourceDirectory = sourceDirectory;
		this.destinationDirectory = destinationDirectory;
		if (this.sourceDirectory == null) {
			throw new FileManagementException("You must select a source folder.");
		} else if (!this.sourceDirectory.isDirectory()) {
			throw new FileManagementException("The source folder is not valid.");
		}
		if (this.destinationDirectory == null) {
			throw new FileManagementException("You must select a destination folder.");
		} else if (!this.destinationDirectory.isDirectory()) {
			throw new FileManagementException("The destination folder is not valid.");
		}
		if (this.sourceDirectory.getAbsolutePath().equals(this.destinationDirectory.getAbsolutePath())) {
			throw new FileManagementException("You cannot copy files to the same directory.");
		}
		Collection<String> lowerCaseExtensions = new ArrayList<>();
		for (String extension : extensions) {
			lowerCaseExtensions.add(extension.toLowerCase());
		}
		this.fileOrderManager = new FileOrderManager(lowerCaseExtensions);
	}
	
	/**
	 * Gets the children already filtered by file extension and ordered
	 * lexicographically.
	 * @param directory A directory.
	 * @return The list of filtered and ordered children.
	 */
	public List<File> getChildren(File directory) {
		return this.fileOrderManager.getChildren(directory);
	}
	
	/**
	 * Copies the given file to the given folder.
	 * @param file A file.
	 * @param directory A directory.
	 * @throws IOException If something goes wrong.
	 */
	public static void copyFileToFolder(File file, File directory) throws IOException {
		Path source = file.toPath();
		Path destination = new File(directory, file.getName()).toPath();
		long sourceSize = getSize(source);
		long destinationSize = getSize(destination);
		if (sourceSize != destinationSize) {
			Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	/**
	 * Creates the directory if it doesn't exist.
	 * @param directory A directory.
	 */
	public static void createIfNotExists(File directory) {
		if (!directory.exists()) {
			directory.mkdir();
		}
	}
	
	/**
	 * Deletes the directory if it is empty.
	 * @param directory A directory.
	 */
	public static void deleteIfEmpty(File directory) {
		File[] files = directory.listFiles();
		if (files != null && files.length < 1) {
			directory.delete();
		}
	}
	
	private static long getSize(Path filePath) throws IOException {
		long result;
		if (Files.exists(filePath)) {
			result = Files.size(filePath);
		} else {
			result = 0;
		}
		return result;
	}
}
