package threedee.application;

import threedee.pipeline.GeometryStage;
import threedee.pipeline.PipelineListener;
import threedee.pipeline.PixelStage;
import threedee.pipeline.Rasterization;
import threedee.pipeline.implementation.SimpleGeometryStage;
import threedee.pipeline.implementation.SimplePixelStage;
import threedee.pipeline.implementation.SimpleRasterization;
import threedee.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class ThreedeeView extends JPanel {
    private final Scene scene;
    private final GeometryStage geometryStage;
    private final Rasterization rasterization;
    private final PixelStage pixelStage;

    public ThreedeeView(Scene scene) {
        this.scene = scene;

        geometryStage = new SimpleGeometryStage(scene, 0, 100, 0, 100, 100, 100, 100);
        rasterization = new SimpleRasterization(geometryStage);
        pixelStage = new SimplePixelStage(rasterization);

        pixelStage.addPipelineListener(new PipelineListener() {
            public void pipelineChanged() {
                repaint();
            }
        });

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                Dimension d = getSize();
                geometryStage.setScreenSize(d.width, d.height);
            }
        });
    }

    protected void paintComponent(Graphics g) {
        BufferedImage screenBuffer = pixelStage.getPixels();
        g.drawImage(screenBuffer, 0, 0, null);
    }
}
