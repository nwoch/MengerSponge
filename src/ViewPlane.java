

public class ViewPlane {

  /** Center of the shape object being viewed by the camera **/
  Point3D centerOfScene;
  Point3D horizontalViewVector;
  Point3D verticalViewVector;
  double viewPlaneWidth;
  double viewPlaneHeight;

  public ViewPlane(Point3D cameraPosition, Point3D toObject, Point3D upArbitrary, double horizontalFOVAngle, double aspectRatio) {
    /** Set camera viewing object to the object center passed in from the Ray Tracer **/
    centerOfScene = new Point3D(toObject.getX(), toObject.getY(), toObject.getZ());
    /** Create a vector (new z axis) from the camera position to the center of the shape that is being viewed **/
    Point3D forwardVector = toObject.subtractVector(cameraPosition);
    /**
    Create a vector (new y axis) from an arbitrary vector (we use (0,1,0) since that is the y axis). Scale the forwardVector
    from the camera to the shape by the dot product (angle) between the arbitrary vector and the forwardVector divided by the length
    squared of the forwardVector (to give the correct size). Then subtract the scaled forwardVector from the arbitrary vector to get
    the correct new y axis vector for the viewing plane.
    **/
    verticalViewVector = projectVectorOntoPlane(upArbitrary, forwardVector);
    /** Create a vector (new x axis) that is perpendicular to both new y and z vectors by using the cross product **/
    horizontalViewVector = forwardVector.crossProduct(verticalViewVector).normalize();
    /** Find the viewPlaneWidth of the screen using the horizontal Field of View Angle (is set in the Canvas class) for the view plane and the length of the forwardVector to get the correct proportions **/
    viewPlaneWidth = 2 * Math.tan(horizontalFOVAngle/2.0) * forwardVector.magnitude();
    viewPlaneHeight = viewPlaneWidth/aspectRatio;
  }

  public Point3D getNewCoorindates(double xCoord, double yCoord) {
    /** Get new pixel coordinates based on the new view plane for the camera **/
    Point3D newXPixel = horizontalViewVector.scale((xCoord - 0.5) * viewPlaneWidth);
    Point3D newYPixel = verticalViewVector.scale((yCoord - 0.5) * viewPlaneHeight);
    return centerOfScene.addVector(newXPixel).addVector(newYPixel);
  }

  public Point3D projectVectorOntoPlane(Point3D startingVector, Point3D planeNormalVector) {
    double normalMagnitudeSquared = (planeNormalVector.getX() * planeNormalVector.getX()) + (planeNormalVector.getY() * planeNormalVector.getY()) + (planeNormalVector.getZ() * planeNormalVector.getZ());
    double startingDotNormal = startingVector.dotProduct(planeNormalVector);
    Point3D projectedVector = planeNormalVector.scale(startingDotNormal/normalMagnitudeSquared);
    return startingVector.subtractVector(projectedVector);
  }

}
