import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.*;

public class RayTracer {

  BufferedImage image;

  Sphere sphere;

  MengerSponge mengerSponge;

  public RayTracer() {
    mengerSponge = new MengerSponge();
  }

  public void render(BufferedImage scene, double[][] pixelColors) {
    image = scene;
    for(int i = 0; i < image.getHeight(); i++) {
      for(int j = 0; j < image.getWidth(); j++) {
        int currentColorValue = (int)pixelColors[j][i];
        image.setRGB(j, i, new Color(currentColorValue, currentColorValue, currentColorValue).getRGB()); //currently in gray scale
      }
    }
  }

  public double[][] rayTrace(Point3D cameraPosition, double horizontalFOVAngle, double verticalFOVAngle, Dimension imgResolution, LightSource lightSource) {
    ViewPlane plane = new ViewPlane(cameraPosition, new Point3D(0.0, 0.0, 0.0), new Point3D(0.0, 0.1, 0.0), Math.toRadians(horizontalFOVAngle), imgResolution.width/imgResolution.getHeight());
    double[][] pixelColors = new double[(int)imgResolution.getWidth()][(int)imgResolution.getHeight()];
    for(int y = 0; y < imgResolution.getHeight(); y++) { //y pixel coordinate
      for(int x = 0; x < imgResolution.getWidth(); x++) { //x pixel coordiante
        /** Create the View Plane and find coordinate in center of current pixel to create new 3D point **/
        Point3D fieldOfViewCoordinate = plane.getNewCoorindates(x/imgResolution.getWidth(), y/imgResolution.getHeight());
        /** Create a ray from the camera position to the view plane coordinates **/
        Point3D cameraRay = fieldOfViewCoordinate.subtractVector(cameraPosition).normalize();
        /** Check if this create ray intersects with the sphere **/
        Intersection cameraIntersection = mengerSponge.intersectWithSponge(cameraPosition, cameraRay);
        if(cameraIntersection != null) { //If the ray does intersect with the sphere shape
          /** Create a ray from this point of intersection and the light source position **/
          Point3D lightRay = lightSource.getLightSourcePosition().subtractVector(cameraIntersection.getIntersectionPoint());
          // TODO: Move slightly away from the surface of the face to avoid shadow acne
          double EPSILON = 0.0001;
          /** Chech if this ray intersects with another shape **/
          Intersection lightIntersection = mengerSponge.intersectWithSponge(cameraIntersection.getIntersectionPoint().addVector(cameraIntersection.getNormalVector().scale(EPSILON)), lightRay);
          if(lightIntersection == null) { // If there is no shape blocking the light, calculate the light intensity at that point on the shape
            /** Calculate diffuse light **/
            //Point3D normalToIntersection = cameraIntersection.subtractVector(sphere.getSphereCenter());
            double diffuseLight = lightSource.calcDiffuseLight(cameraIntersection.getNormalVector(), lightRay);
            /** Calculate Speculate Reflection **/
            double specularReflectionLight = lightSource.calcSpecularReflection(cameraIntersection.getNormalVector(), lightRay, diffuseLight);
            pixelColors[x][y] = lightSource.getAmbientLight() + diffuseLight + specularReflectionLight;
          } else {
            /** Set pixel color to the Ambient Light **/
            pixelColors[x][y] = lightSource.getAmbientLight(); //if the point is in shadow from another shape, set the pixel to the ambient value
          }
        } else {
          /** Set pixel color to the Ambient Light **/
          pixelColors[x][y] = 0.0; //if the ray does not intersect with a shape, set that pixel to black (background color)
        }
      }
    }
    render(new BufferedImage((int)imgResolution.getWidth(), (int)imgResolution.getHeight(),  BufferedImage.TYPE_INT_RGB), pixelColors);
    return pixelColors;
  }

  public Point3D intersectWithSphere(Point3D startingPoint, Point3D ray) {
    //returns intersection point if it exists, otherwise returns null
    return sphere.intersectWithSphere(startingPoint, ray);
  }

  public Point3D intersectWithSponge(Point3D startingPoint, Point3D ray) {
    //returns intersection point if it exists, otherwise returns null
    return null;
  }

}
