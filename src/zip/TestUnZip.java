package zip;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
 * @author Administrator
 */
public class TestUnZip {

    public static void main(String[] args) {
        FileUtils fileUtils = FileUtils.newFileUtils();
        expandFile(fileUtils, new File("c:/test.zip"), new File("c:/temp"));
    }

    protected static void expandFile(FileUtils fileUtils, File srcF, File dir) {
        ZipFile zf = null;
        try {
            zf = new ZipFile(srcF, "big5");
            Enumeration e = zf.getEntries();
            while (e.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) e.nextElement();
                extractFile(fileUtils, srcF, dir, zf.getInputStream(ze), ze
                        .getName(), new Date(ze.getTime()), ze.isDirectory());
            }

        } catch (IOException ioe) {
            throw new BuildException("Error while expanding " + srcF.getPath(),
                    ioe);
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    protected static void extractFile(FileUtils fileUtils, File srcF, File dir,
            InputStream compressedInputStream, String entryName,
            Date entryDate, boolean isDirectory) throws IOException {

        boolean overwrite = true;
        File f = fileUtils.resolveFile(dir, entryName);
        try {
            if (!overwrite && f.exists()
                    && f.lastModified() >= entryDate.getTime()) {
                System.out.println("Skipping " + f + " as it is up-to-date");
                return;
            }

            System.out.println("expanding " + entryName + " to " + f);
            
            // create intermediary directories - sometimes zip don't add them
            File dirF = fileUtils.getParentFile(f);
            if (dirF != null) {
                dirF.mkdirs();
            }

            if (isDirectory) {
                f.mkdirs();
            } else {
                byte[] buffer = new byte[1024];
                int length = 0;
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);

                    while ((length = compressedInputStream.read(buffer)) >= 0) {
                        fos.write(buffer, 0, length);
                    }

                    fos.close();
                    fos = null;
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }

            fileUtils.setFileLastModified(f, entryDate.getTime());
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to expand to file " + f.getPath());
        }

    }
}
