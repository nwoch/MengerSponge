import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.*;

public class RayTracer implements MengerSponge {

  BufferedImage image;

  Sphere sphere;

  double counter;

  public RayTracer() {
    sphere = new Sphere(200.0, new Point3D(0.0,0.0,-300.0)); //change this to place the sphere
    counter = 0;
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
    double[][] pixelColors = new double[(int)imgResolution.getWidth()][(int)imgResolution.getHeight()];
    for(int y = 0; y < imgResolution.getHeight(); y++) { //y pixel coordinate
      for(int x = 0; x < imgResolution.getWidth(); x++) { //x pixel coordiante
        /** Create the View Plane and find coordinate in center of current pixel to create new 3D point **/
        Point3D fieldOfViewCoordinate = createViewPlane(x, y, cameraPosition, horizontalFOVAngle, verticalFOVAngle, imgResolution);
        /** Create a ray from the camera position to the view plane coordinates **/
        Point3D cameraRay = fieldOfViewCoordinate.subtractVector(cameraPosition);
        cameraRay = cameraRay.normalize();
        /** Check if this create ray intersects with the sphere **/
        Point3D cameraIntersection = intersectWithSphere(cameraPosition, cameraRay);
        if(cameraIntersection != null) { //If the ray does intersect with the sphere shape
          /** Create a ray from this point of intersection and the light source position **/
          Point3D lightRay = lightSource.getLightSourcePosition().subtractVector(cameraIntersection);
          /** Chech if this ray intersects with another shape **/
          Point3D lightIntersection = intersectWithSponge(cameraIntersection, lightRay);
          if(lightIntersection == null) { // If there is no shape blocking the light, calculate the light intensity at that point on the shape
            /** Calculate diffuse light **/
            Point3D normalToIntersection = cameraIntersection.subtractVector(sphere.getSphereCenter());
            double diffuseLight = lightSource.calcDiffuseLight(cameraIntersection, lightRay);
            /** Calculate Speculate Reflection **/
            double specularReflectionLight = lightSource.calcSpecularReflection(cameraIntersection, lightRay, diffuseLight);
            pixelColors[x][y] = lightSource.ambientLight + diffuseLight + specularReflectionLight;
          } else {
            /** Set pixel color to the Ambient Light **/
            pixelColors[x][y] = lightSource.ambientLight; //if the point is in shadow from another shape, set the pixel to the ambient value
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

  public Point3D createViewPlane(int x, int y, Point3D cameraPosition, double horizontalFOVAngle, double verticalFOVAngle, Dimension imgResolution) {
    /** Raster space to NDC (Normalized Device Coordinates) Space **/
    double pixelNDCx = (double)(x + 0.5) / imgResolution.getWidth();
    double pixelNDCy = (double)(y + 0.5) / imgResolution.getHeight();
    double aspectRatio = imgResolution.getWidth()/imgResolution.getHeight();
    /** Convert NDC Space to Screen Space **/
    double pixelScreenX = (2 * pixelNDCx) - 1;
    double pixelScreenY = 1 - (2 * pixelNDCy);
    double pixelCameraX = 0.0;
    double pixelCameraY = 0.0;
    if(imgResolution.getWidth() > imgResolution.getHeight()) {
      /** Conver Screen Space to Camera **/
      pixelCameraX = (2 * pixelScreenX - 1) * aspectRatio * Math.tan(Math.toRadians(horizontalFOVAngle/2));
      pixelCameraY = (1 - (2 * pixelScreenY)) * Math.tan(Math.toRadians(verticalFOVAngle/2));
    } else if (imgResolution.getWidth() < imgResolution.getHeight()) {
      /** Conver Screen Space to Camera **/
      pixelCameraX = (2 * pixelScreenX - 1) * Math.tan(Math.toRadians(horizontalFOVAngle/2));
      pixelCameraY = (1 - (2 * pixelScreenY)) * aspectRatio * Math.tan(Math.toRadians(verticalFOVAngle/2));
    } else if (imgResolution.getWidth() == imgResolution.getHeight()) {
      /** Conver Screen Space to Camera **/
      pixelCameraX = (2 * pixelScreenX - 1) * Math.tan(Math.toRadians(horizontalFOVAngle/2));
      pixelCameraY = (1 - (2 * pixelScreenY)) * Math.tan(Math.toRadians(verticalFOVAngle/2));
    }

    return new Point3D(pixelCameraX, pixelCameraY, -1.0);
  }

}
