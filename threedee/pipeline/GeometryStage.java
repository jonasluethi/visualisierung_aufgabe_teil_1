package threedee.pipeline;

import threedee.geometry.ScreenPolygon;

import java.util.ArrayList;

public interface GeometryStage {
    ArrayList<ScreenPolygon> getScreenObjects();

    void setScreenSize(int width, int height);
    int getScreenWidth();
    int getScreenHeight();

    void setFieldOfView(double xMin, double xMax, double yMin, double yMax);
    void setCameraPosition(double z);
    
    void addPipelineListener(PipelineListener listener);
}
