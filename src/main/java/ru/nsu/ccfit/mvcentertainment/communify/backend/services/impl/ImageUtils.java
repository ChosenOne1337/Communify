package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import lombok.experimental.UtilityClass;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.exceptions.ResourceException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@UtilityClass
public class ImageUtils {

    public void scaleImage(
            File imageFile,
            Integer width,
            Integer height,
            String outputImageFormat
    ) throws IOException {
        BufferedImage imageToScale = ImageIO.read(imageFile);
        BufferedImage scaledImage = new BufferedImage(width, height, imageToScale.getType());
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );
        graphics2D.setRenderingHint(
                RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY
        );
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );

        graphics2D.drawImage(imageToScale, 0, 0, width, height, null);
        graphics2D.dispose();
        ImageIO.write(scaledImage, outputImageFormat, imageFile);
    }

    public void validateImage(File imageFile) throws IOException {
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile)) {
            Iterator<ImageReader> imageReaderIterator = ImageIO.getImageReaders(imageInputStream);
            if (!imageReaderIterator.hasNext()) {
                throw new ResourceException("Image has an unknown format");
            }
        }
    }

}
