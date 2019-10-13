import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class RayTracer implements MengerSponge {

  Sphere sphere;

  public RayTracer() {
    sphere = new Sphere(50.0, new Point3D(0.0,0.0,0.0)); //change this to place the sphere
  }

  public void render(BufferedImage scene, double[][] pixelColors) {

  }

  public double[][] rayTrace(Point3D cameraPosition, double horizontalFOVAngle, double verticalFOVAngle, Dimension imgResolution, LightSource lightSource) {
    double[][] pixelColors = new double[(int)imgResolution.getWidth()][(int)imgResolution.getHeight()];
    for(int y = 0; y < imgResolution.getHeight(); y++) { //y pixel coordinate
      for(int x = 0; x < imgResolution.getWidth(); x++) { //x pixel coordiante
        System.out.println("Current Point: " + x + ", " + y);
        /** Create the View Plane and find coordinate in center of current pixel to create new 3D point **/
        Point3D fieldOfViewCoordinate = createViewPlane(x, y, cameraPosition, horizontalFOVAngle, verticalFOVAngle, imgResolution);
        /** Create a ray from the camera position to the view plane coordinates **/
        Point3D cameraRay = fieldOfViewCoordinate.subtractVector(cameraPosition);
        System.out.println("Camera Ray at Current Pixel: " + cameraRay.getX() + ", " + cameraRay.getY() + ", " + cameraRay.getZ());
        /** Check if this create ray intersects with the sphere **/
        Point3D cameraIntersection = intersectWithSphere(cameraPosition, cameraRay);
        if(cameraIntersection != null) { //If the ray does intersect with the sphere shape
          System.out.println("Intersects With Sphere!");
          System.out.println("Intersection Point: " + cameraIntersection.getX() + ", " + cameraIntersection.getY() + ", " + cameraIntersection.getZ());
          /** Create a ray from this point of intersection and the light source position **/
          Point3D lightRay = lightSource.getLightSourcePosition().subtractVector(cameraIntersection);
          /** Chech if this ray intersects with another shape **/
          Point3D lightIntersection = intersectWithSponge(cameraIntersection, lightRay);
          if(lightIntersection == null) { // If there is no shape blocking the light, calculate the light intensity at that point on the shape
            /** Calculate diffuse light **/
            double diffuseLight = lightSource.calcDiffuseLight(cameraIntersection, lightRay);
            System.out.println("Diffuse Light: " + diffuseLight);
            /** Calculate Speculate Reflection **/
            double specularReflectionLight = lightSource.calcSpecularReflection(cameraIntersection, lightRay, diffuseLight);
            System.out.println("Specular Reflection: " + specularReflectionLight);
            pixelColors[x][y] = lightSource.ambientLight + diffuseLight + specularReflectionLight;
          } else {
            /** Set pixel color to the Ambient Light **/
            pixelColors[x][y] = lightSource.ambientLight;
          }
        } else {
            /** Set pixel color to the Ambient Light **/
          pixelColors[x][y] = lightSource.ambientLight;
        }
      }
    }
    return pixelColors;
  }

  public Point3D intersectWithSphere(Point3D startingPoint, Point3D ray) {
    //returns intersection point if it exists, otherwise returns null
    sphere.intersectWithSphere(startingPoint, ray);
    return startingPoint;
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
    double pixelScreenY = (2 * pixelNDCy) - 1;
    /** Conver Screen Space to Camera **/
    double pixelCameraX = (2 * pixelScreenX - 1) * Math.tan(Math.toRadians(horizontalFOVAngle/2));
    double pixelCameraY = (1 - (2 * pixelScreenY)) * Math.tan(Math.toRadians(verticalFOVAngle/2));

    return new Point3D(pixelCameraX, pixelCameraY, -1.0);
  }

  public static void main(String args[]) {
    RayTracer ray = new RayTracer();
    Point3D camera = new Point3D(0.0, 0.0, 0.0); //set camera point
    Point3D light = new Point3D(300.0, 300.0, 300.0); //set light point
    Dimension imageResolution = new Dimension(100, 100); //image size
    LightSource lightSource = new LightSource(light, 60); //create light source
    ray.rayTrace(camera, 45, 45, imageResolution, lightSource);
  }

}
