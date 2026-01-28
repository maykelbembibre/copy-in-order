package copy_in_order.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import copy_in_order.logic.alphanumeric.AlphanumericComparator;
import copy_in_order.logic.models.ComparableFile;

/**
 * Class that manages how files should be ordered.
 */
public class FileOrderManager {

	private static final Comparator<String> ALPHANUMERIC_COMPARATOR = new AlphanumericComparator();
	private final Collection<String> extensions;
	
	/**
	 * Constructor.
	 * @param extensions A list of extensions so that only files with those extensions will be
	 * passed through. <code>null</code> or empty {@link Collection} means all extensions will
	 * be passed through.
	 */
	public FileOrderManager(Collection<String> extensions) {
		this.extensions = extensions;
	}
	
	/**
	 * Orders the given files.
	 * @param files Files.
	 * @return The ordered list of files.
	 */
	public List<ComparableFile> order(Iterable<File> files) {
		List<ComparableFile> result = this.filter(files);
		Collections.sort(result);
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
	
	private boolean extensionIsValid(String extension) {
		return this.extensions == null || this.extensions.isEmpty() || this.extensions.contains(extension);
	}
	
	private List<ComparableFile> filter(Iterable<File> files) {
		List<ComparableFile> result = new ArrayList<>();
		if (files != null) {
			for (File file : files) {
				if (file != null &&
					(file.isDirectory() || this.extensionIsValid(getExtension(file.getName()).toLowerCase()))
				) {
					result.add(new ComparableFile(ALPHANUMERIC_COMPARATOR, file));
				}
			}
		}
		return result;
	}
}
