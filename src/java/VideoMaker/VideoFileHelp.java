package VideoMaker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import org.jcodec.common.SeekableByteChannel;

import javax.imageio.ImageIO;
import org.jcodec.api.FrameGrab.MediaInfo;
import org.jcodec.api.awt.FrameGrab;
import org.jcodec.common.FileChannelWrapper;
import org.jcodec.common.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.jcodec.common.RunLength;
import static org.jcodec.containers.mkv.MKVType.Video;

/**
 *
 * @author MSK
 */
public class VideoFileHelp {

    public void SaveFileToDisk(Part filePart) throws IOException, Exception {
        String fileName = filePart.getSubmittedFileName();
        String folderNameVideo = "D:\\test\\" + fileName + "\\video\\";
        String folderNameImage = "D:\\test\\" + fileName + "\\image\\";
        File folderVideo = new File(folderNameVideo);
        if (!folderVideo.exists()) {
            folderVideo.mkdirs();
        }
        File folderImage = new File(folderNameImage);
        if (!folderImage.exists()) {
            folderImage.mkdirs();
        }
        InputStream fileContent = filePart.getInputStream();
        byte[] buffer = new byte[fileContent.available()];
        fileContent.read(buffer);
        File targetFile = new File(folderNameVideo + fileName);
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        fileContent.close();
        outStream.flush();
        outStream.close();
        FileToScreenShots(targetFile);

    }

    public void FileToScreenShots(File file) {
        try {
            FileChannelWrapper ch = NIOUtils.readableFileChannel(file);
            FrameGrab fg = new FrameGrab(ch);
            for (int i = 0; i < 12; i++) {
                Picture pic = fg.seekToSecondPrecise(i).getNativeFrame();
                BufferedImage bi = AWTUtil.toBufferedImage(pic);
                dumpImageToFile(bi, i, file.getName());
            }
            ch.close();
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
    }

    public String dumpImageToFile(BufferedImage image, int i, String fileName) {
        try {
            String outputFilename = fileName + "\\image\\" + fileName + "_" + i + "_" + ".png";
            ImageIO.write(image, "png", new File("D:\\test\\" + outputFilename));
            return outputFilename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
