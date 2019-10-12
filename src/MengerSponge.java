import java.awt.Dimension;
import java.awt.image.BufferedImage;

public interface MengerSponge {

    public void render(BufferedImage scene, double[][] pixelColors);
    //   Set color of each pixel in the scene

    public double[][] rayTrace(Point3D cameraPosition, double horizontalFOVAngle, double verticalFOVAngle, Dimension imgResolution, LightSource lightSource);
    //generate a 3D pixel grid in which each square in the grid maps to a pixel in the imgResolution (using fov angles for scaling)
    //do the following for each pixel/square in the grid:
    //cameraRay = create a ray from cameraPosition through the center of the pixel
    //cameraIntersection = intersectWithShape(cameraPosition, cameraRay) -> involves extending the ray to see if/where it intersects the scene
    //if cameraIntersection != null:
    //lightRay = create a ray from cameraIntersection to the light source
    //lightIntersection = intersectWithShape(cameraIntersection, lightRay)-> involves extending the ray to see if/where it intersects the scene
    //if lightIntersection == null:
    //calculate light intensity and set as pixelColors[i][j]

    public Point3D intersectWithSphere(Point3D startingPoint, Point3D ray);
    //returns intersection point if it exists, otherwise returns null

    public Point3D intersectWithSponge(Point3D startingPoint, Point3D ray);
    //returns intersection point if it exists, otherwise returns null

}