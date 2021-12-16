package de.herberlin.server.common;

import de.herberlin.server.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Class for creation and deletion of temporary files.
 * The File#deleteOnExit() seems to be not a good solution
 * for lots of temporary files.
 * 
 * @author hans joachim herbertz
 * created 19.02.2004
 */
public abstract class TempFileHandler {

	/**
	 * Creates a new temporary file and returns it.
	 */
	public static File getTempFile() throws IOException {
		return File.createTempFile("BsCache",".tmp");
	}
	public static void deleteTempFiles() throws IOException {
		File[] tempFiles=getTempFile().getParentFile().listFiles(new TempFileFilter());
		for (int i=0;i<tempFiles.length;i++){
			try {
				tempFiles[i].delete();
			} catch (Exception e) {
                Logger.getLogger(TempFileHandler.class.getName()).info("Tempfile not deleted: " + e);
            }
		}
	}
	
	
	private static class TempFileFilter implements FilenameFilter {

		/**
		 * @see FilenameFilter#accept(File, String)
		 */
		public boolean accept(File dir, String name) {
			return name != null
					&& name.startsWith("BsServer")
					&& name.endsWith(".tmp");
		}
		
	}
}
