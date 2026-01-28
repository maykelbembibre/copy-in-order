package copy_in_order.ui.runnables;

import java.io.File;

/**
 * Task that does something with a {@link File}.
 */
public interface FileRunnable {

	/**
	 * Does something with the given file.
	 * @param file A file. It must not be <code>null</code>.
	 */
	void run(File file);
}
