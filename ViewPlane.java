

public class ViewPlane {

  /** Center of the shape object being viewed by the camera **/
  Point3D lookingAtObject;
  Point3D rightVector;
  Point3D upVector;
  double width;
  double height;

  public ViewPlane(Point3D cameraPosition, Point3D toObject, Point3D upArbitrary, double horizontalFOVAngle, double aspectRatio) {
    /** Set camera viewing object to the object center passed in from the Ray Tracer **/
    lookingAtObject = new Point3D(toObject.getX(), toObject.getY(), toObject.getZ());
    /** Create a vector (new z axis) from the camera position to the center of the shape that is being viewed **/
    Point3D forwardVector = toObject.subtractVector(cameraPosition);
    /**
    Create a vector (new y axis) from an arbitrary vector (we use (0,1,0) since that is the y axis). Scale the forwardVector
    from the camera to the shape by the dot product (angle) between the arbitrary vector and the forwardVector divided by the length
    squared of the forwardVector (to give the correct size). Then subtract the scaled forwardVector from the arbitrary vector to get
    the correct new y axis vector for the viewing plane.
    **/
    upVector = upArbitrary.subtractVector((forwardVector.scale((upArbitrary.dotProduct(forwardVector)) / Math.pow(forwardVector.magnitude(), 2.0)))).normalize();
    /** Create a vector (new x axis) that is perpendicular to both new y and z vectors by using the cross product **/
    rightVector = forwardVector.crossProduct(upVector).normalize();
    /** Find the width of the screen using the horizontal Field of View Angle (is set in the Canvas class) for the view plane and the length of the forwardVector to get the correct proportions **/
    width = 2 * Math.tan(horizontalFOVAngle/2.0) * forwardVector.magnitude();
    height = width/aspectRatio;
  }

  public Point3D getNewCoorindates(double xCoord, double yCoord) {
    /** Get new pixel coordinates based on the new view plane for the camera **/
    Point3D newXPixel = rightVector.scale((xCoord - 0.5) * width);
    Point3D newYPixel = upVector.scale((yCoord - 0.5) * height);
    return lookingAtObject.addVector(newXPixel).addVector(newYPixel);
  }
}
