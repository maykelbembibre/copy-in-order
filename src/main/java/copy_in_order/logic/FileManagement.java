package copy_in_order.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import copy_in_order.logic.alphanumeric.AlphanumericComparator;
import copy_in_order.logic.exceptions.FileManagementException;
import copy_in_order.logic.models.ComparableFile;

/**
 * Logic class for doing the file management operations.
 */
public class FileManagement {

	private static final Comparator<String> ALPHANUMERIC_COMPARATOR = new AlphanumericComparator();
	private final File sourceDirectory;
	private final File destinationDirectory;
	private final Collection<String> extensions;
	
	/**
	 * Creates a file management object.
	 * @param sourceDirectory The source directory.
	 * @param destinationDirectory The destination directory.
	 * @param extension An extension of the files to copy.
	 * @param additionalExtensions Additional extensions of files to copy.
	 * @throws FileManagementException If something goes wrong.
	 */
	public FileManagement(
		File sourceDirectory, File destinationDirectory, String extension, String... additionalExtensions
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
		this.extensions = new ArrayList<>();
		this.extensions.add(extension.toLowerCase());
		for (String additionalExtension : additionalExtensions) {
			this.extensions.add(additionalExtension.toLowerCase());
		}
	}
	
	/**
	 * Gets the children already filtered by file extension and ordered
	 * lexicographically.
	 * @param folder A folder.
	 * @return The list of filtered and ordered children.
	 */
	public List<File> getChildren(File folder) {
		List<ComparableFile> children = this.order(folder.listFiles());
		List<File> result = new ArrayList<>();
		for (ComparableFile child : children) {
			result.add(child.getFile());
		}
		return result;
	}
	
	/**
	 * Copies the given file to the given folder.
	 * @param file A file.
	 * @param folder A folder.
	 * @throws IOException If something goes wrong.
	 */
	public static void copyFileToFolder(File file, File folder) throws IOException {
		Path source = file.toPath();
		Path destination = new File(folder, file.getName()).toPath();
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
	
	private static String getExtension(String fileName) {
		String extension = "";
		if (fileName != null && !fileName.isEmpty()) {
			int pos = fileName.length() - 1;
			while (pos > 0 && extension.isEmpty()) {
				if (fileName.charAt(pos) == '.') {
					extension = fileName.substring(pos + 1, fileName.length());
				}
				pos--;
			}
		}
		return extension;
	}
	
	private List<ComparableFile> filter(File[] files) {
		List<ComparableFile> result = new ArrayList<>();
		if (files != null) {
			for (File file : files) {
				if (file != null &&
					(file.isDirectory() || this.extensions.contains(getExtension(file.getName()).toLowerCase()))
				) {
					result.add(new ComparableFile(ALPHANUMERIC_COMPARATOR, file));
				}
			}
		}
		return result;
	}
	
	private List<ComparableFile> order(File[] files) {
		List<ComparableFile> result = this.filter(files);
		Collections.sort(result);
		return result;
	}
}
