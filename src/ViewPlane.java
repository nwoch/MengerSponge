/** Emma Blair and Nicole Woch Final Project - Computer Graphics 2019 **/

/**
 ** This class creates the view plane for ray tracing by creating a grid in front of the camera
 ** and maps every pixel in the BufferedImage to this grid, allowing for the generation of camera
 ** rays to the center of every pixel in the image. In order to do this, a new set of coordinates for
 ** the camera must be created for the current camera position.
 **/
public class ViewPlane {

  /** Center of the shape object being viewed by the camera **/
  private Point3D centerOfScene;
  private double viewPlaneWidth;
  private double viewPlaneHeight;
  private Point3D horizontalViewVector;
  private Point3D verticalViewVector;

  public ViewPlane(double fieldOfViewAngle, double widthHeightRatio, Point3D cameraPosition) {
    /** Set camera viewing object to the object center passed in from the Ray Tracer **/
    this.centerOfScene = new Point3D(0.0, 0.0, 0.0);
    calcViewPlaneDimensions(fieldOfViewAngle, widthHeightRatio, cameraPosition);
  }

  /** This method computes the height and the width of the view plane **/
  private void calcViewPlaneDimensions(double fieldOfViewAngle, double widthHeightRatio, Point3D cameraPosition) {
    /** Create a vector (new z axis) from the camera position to the center of the shape that is being viewed **/
    Point3D forwardVector = centerOfScene.subtractVector(cameraPosition);
    /**
     ** Create a vector (new y axis) from an arbitrary vector (we use (0,1,0) since that is the y axis). Project this vector on the
     ** plane of the forwardVector in order to get a new vector perpendicular to the forwardVector that corresponds the normal
     ** y vector in the normal coordinate system for this new coordinates system for the camera
     **/
    this.verticalViewVector = projectVectorOntoPlane(new Point3D(0.0, 1.0, 0.0), forwardVector);
    /** Create a vector (new x axis) that is perpendicular to both new y and z vectors by using the cross product **/
    this.horizontalViewVector = forwardVector.crossProduct(verticalViewVector).normalize();
    /** Find the viewPlaneWidth of the screen using the horizontal Field of View Angle (is set in the Canvas class) for the view plane and the length of the forwardVector to get the correct proportions **/
    this.viewPlaneWidth = 2 * Math.tan(fieldOfViewAngle/2.0) * forwardVector.magnitude();
    this.viewPlaneHeight = viewPlaneWidth/widthHeightRatio;
  }

  /** This method takes the new camera vectors created in the constructor and applies the pixel coordinates
   ** from the image in the RayTracer class to the view plane by scaling the vector x and y vectors
   ** by the center of the current pixel divided by the times the width of the view plane. Gives the correct coordinates
   ** for generating the rays from the camera through the pixels for the scene.
   **/
  public Point3D getNewCoordinates(double x, double y) {
    /** Get new pixel coordinates based on the new view plane for the camera **/
    Point3D newXPixel = horizontalViewVector.scale(viewPlaneWidth * (x - 0.5)); //0.5 to get to middle of pixel
    Point3D newYPixel = verticalViewVector.scale(viewPlaneHeight * (y - 0.5)); //0.5 to get to middle of pixel
    return centerOfScene.addVector(newXPixel).addVector(newYPixel);
  }

  /**
   ** This method is used to create the upVector (new y vector for the camera position) by using the passed
   ** in (0,1,0) vector and the vector from the camera to the center of the object we are looking at (new z vector)
   ** and get the new up vector for the camera by projecting this arbitrary vector onto the plane of the new z vector. Use
   ** the equation (u * v /(|v|^2)) * v where u and v are vectors.
   **/
  private Point3D projectVectorOntoPlane(Point3D startingVector, Point3D planeNormalVector) {
    double normalMagnitudeSquared = (planeNormalVector.getX() * planeNormalVector.getX()) + (planeNormalVector.getY() * planeNormalVector.getY()) + (planeNormalVector.getZ() * planeNormalVector.getZ());
    double startingDotNormal = startingVector.dotProduct(planeNormalVector);
    Point3D projectedVector = planeNormalVector.scale(startingDotNormal/normalMagnitudeSquared);
    return startingVector.subtractVector(projectedVector);
  }

}
