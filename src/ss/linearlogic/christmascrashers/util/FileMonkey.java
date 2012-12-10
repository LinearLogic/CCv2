package ss.linearlogic.christmascrashers.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The FileMonkey provides methods for various file operations, such as recursive deleting of folders and files.
 * As with the RenderMonkey and TextureMonkey, the FileMonkey responds best to bananas.
 * 
 * @author LinearLogic
 * @since 0.2.4
 */
public class FileMonkey {

	/**
	 * Deletes the supplied File recursively, meaning that if the file is a directory, this method is excecuted on all
	 * subfiles. This method completely removes a directory and all its contents.
	 * 
	 * @param file The File object to delete
	 */
	public static void delete(File file) {
		if (file.isDirectory()) {
			for (File subfile : file.listFiles())
				delete(subfile);
		}
		if (!file.delete())
			System.err.println("Failed to delete file " + file.getPath() + ".");
	}

	/**
	 * Copies the provided file or directory to the supplied target file or directory, creating the target if it doesn't already exist.
	 * 
	 * @param source The file or directory to copy from
	 * @param target The file or directory to copy to
	 * 
	 * @throws IOException
	 */
    public static void copyDir(File source, File target) throws IOException {
 
    	if(source.isDirectory()) {
    		if(!target.exists()) {
    		   target.mkdir();
    		}
    		String files[] = source.list();
 
    		for (String file : files) {
    		   File srcFile = new File(source, file);
    		   File destFile = new File(target, file);
    		   copyDir(srcFile, destFile);
    		}
    	} else {
    		InputStream in = new FileInputStream(source);
    	    OutputStream out = new FileOutputStream(target); 
 
    	    byte[] buffer = new byte[1024];
 
	        int length;
	        //copy the file content in bytes 
	        while ((length = in.read(buffer)) > 0) {
	        	out.write(buffer, 0, length);
    	    }
 
	        in.close();
	        out.close();
    	}
    }
}
