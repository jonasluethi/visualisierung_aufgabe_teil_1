package threedee.pipeline.implementation;

import threedee.geometry.Fragment;
import threedee.geometry.Pixel;
import threedee.pipeline.PipelineListener;
import threedee.pipeline.PixelStage;
import threedee.pipeline.Rasterization;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SimplePixelStage implements PixelStage {
    private final Rasterization rasterization;
    private BufferedImage screenBuffer;
    private Color backgroundColor;

    private final ArrayList<PipelineListener> listeners;

    public SimplePixelStage(Rasterization rasterization) {
        this.rasterization = rasterization;
        rasterization.addPipelineListener(new PipelineListener() {
            public void pipelineChanged() {
                createScreenBuffer();
            }
        });

        backgroundColor = Color.black;

        listeners = new ArrayList<PipelineListener>();
    }

    public BufferedImage getPixels() {
        return screenBuffer;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        createScreenBuffer();
    }

    public void setDepthCueingEnabled(boolean enabled) {
    }

    public void setTransparencyEnabled(boolean enabled) {
    }

    public void addPipelineListener(PipelineListener listener) {
        listeners.add(listener);
    }

    private void firePipelineChanged() {
        for (PipelineListener listener : listeners) {
            listener.pipelineChanged();
        }
    }

    private void createScreenBuffer() {
        int screenWidth = rasterization.getScreenWidth();
        int screenHeight = rasterization.getScreenHeight();
        screenBuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);

        paintBackground();
        paintFragments();

        firePipelineChanged();
    }

    private void paintBackground() {
        for (int i=0; i<screenBuffer.getHeight(); i++) {
            for (int j=0; j<screenBuffer.getWidth(); j++) {
                screenBuffer.setRGB(j, i, backgroundColor.getRGB());
            }
        }
    }

    private void paintFragments() {
        int screenHeight = rasterization.getScreenHeight();
        ArrayList<Fragment> fragments = rasterization.getFragments();
        for (Fragment fragment : fragments) {
            for (int i=0; i<fragment.getPixelCount(); i++) {
                Pixel pixel = fragment.getPixel(i);
                screenBuffer.setRGB(pixel.x, (screenHeight-1) - pixel.y, pixel.color.getRGB());
            }
        }
    }

}

/*
    First of all, initialize the depth of each pixel.
        i.e,  d(i, j) = infinite (max length)
        Initialize the color value for each pixel
        as c(i, j) = background color
        for each polygon, do the following steps :

        for (each pixel in polygon's projection)
        {
        find depth i.e, z of polygon
        at (x, y) corresponding to pixel (i, j)

        if (z < d(i, j))
        {
        d(i, j) = z;
        c(i, j) = color;
        }
        }


 */
