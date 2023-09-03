package com.promotion.handwriting.util;

import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtil {

    /**
     * compress image and resize "jpg", "png" image.
     * max height and width depend on config. but default is 400px
     * compress quality also depend on config.
     * targetPath must contain absolute path.
     * compressImagePath must contain absolute path.
     *
     * @param targetPath        absolute path of original file
     * @param compressImagePath absolute path of compress file
     * @return if process is working good, return true. if not, return false.
     */
    public static boolean compress(String targetPath, String compressImagePath) {
        String format = format(targetPath);
        try {
            if (format.equals("jpg")) {
                resizeImage(targetPath, compressImagePath);
                compressImage(compressImagePath, compressImagePath, 1f);
            } else if (format.equals("png")) {
                resizeImage(targetPath, compressImagePath);
            } else {
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static final int maxWidth = 400, maxHeight = 400;

    private static void resizeImage(String inputImagePath, String outputImagePath) throws IOException {
        // Read the input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        //set image width, height
        int scaledWidth = inputImage.getWidth(null);
        int scaledHeight = inputImage.getHeight(null);
        float radio = scaledHeight / (float) scaledWidth;
        if (scaledWidth > maxWidth) {
            scaledWidth = maxWidth;
            scaledHeight = (int) (radio * scaledWidth);
        }
        if (scaledHeight > maxHeight) {
            scaledHeight = maxHeight;
            scaledWidth = (int) (scaledWidth / radio);
        }
        System.out.println("w, h: " + "(" + scaledWidth + "," + scaledHeight + ")");

        // Create a new BufferedImage with the specified dimensions
        Image outputImage = inputImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);

        // Draw the resized image onto the new BufferedImage
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(outputImage, 0, 0, null);
        g2d.dispose();

        // Write the resized image to the output file
        File outputFile = new File(outputImagePath);
        ImageIO.write(resizedImage, format(outputImagePath), outputFile);
    }

    private static void compressImage(String inputFilePath, String outputFilePath, float compressionQuality) {
        if (format(inputFilePath).equals("png")) {
            compressPNG(inputFilePath, outputFilePath, (int) (compressionQuality * 9));
        } else {
            compressJPG(inputFilePath, outputFilePath, compressionQuality);
        }
    }

    public static String compressImageName(String originalImageName) {
        return "compress-" + originalImageName;
    }

    private static void compressPNG(String inputFilePath, String outputFilePath, int compressionLevel) {
        try {
            PngReader pngr = new PngReader(new File(inputFilePath));
            PngWriter pngw = new PngWriter(new File(outputFilePath), pngr.imgInfo, true);
            pngw.setCompLevel(compressionLevel);

            for (int row = 0; row < pngr.imgInfo.rows; row++) {
                pngw.writeRow(pngr.readRow());
            }

            pngr.end();
            pngw.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void compressJPG(String inputFilePath, String outputFilePath, float compressionQuality) {
        try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(new File(outputFilePath))) {
            // Read the image file
            BufferedImage originalImage = ImageIO.read(new File(inputFilePath));

            // Get the image writer
            ImageWriter writer = ImageIO.getImageWritersByFormatName(format(inputFilePath)).next();

            // Create a write param that will compress the image
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(compressionQuality);

            // Write the compressed image to a file
            writer.setOutput(imageOutputStream);
            writer.write(null, new IIOImage(originalImage, null, null), writeParam);
            writer.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String format(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }
}
