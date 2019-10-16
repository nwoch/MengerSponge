
/**
 * Class which represents the intersection of a ray with a cube. Contains the t-value - the scalar that can be used to compute the intersection point,
 * and methods to calculate the intersection point and the normal vector at the intersection point.
 */
public class CubeIntersection {

    private double tValue;
    private Point3D intersectionPoint;
    private Point3D normalVector;

    /** Constructor that sets the t-value at which this intersection occurs. */
    public CubeIntersection(double tValue) {
        this.tValue = tValue;
    }

    /**
     * Finds the intersection point using the t-value, the ray, and the starting point of the ray
     * by substituting the parameters back into the equation for a ray: P = P0 + tv,
     * where P is the intersection point, P0 is the ray start point, and v is the ray's direction vector.
     */
    public void findIntersectionPoint(Point3D rayStartPoint, Point3D ray) {
        this.intersectionPoint = rayStartPoint.addVector(ray.scale(this.tValue));
    }

    /**
     * Finds the normal at the point of intersection by determining which face of the cube the point is on and finding the normal of the face.
     * This works because the cube is always aligned with the coordinate axes (because the sponge is), so at any face of the cube,
     * one of either the x, y, or z-coordinates is held constant. The method checks the coordinates of the intersection point against the coordinate
     * which is held constant at each face of the cube to determine which face contains the intersection point.
     * The orientation of each face is known and is always the same since the sponge and subsequently the cube are axis-aligned,
     * so the normal vector at each face is simply the unit vector pointing out at that face.
     */
    public void findIntersectedFaceNormal(Point3D backBottomLeftVertex, Point3D frontUpperRightVertex) {
        // Left face
        if(this.isAlmostEqual(this.intersectionPoint.getX(), backBottomLeftVertex.getX())) { this.normalVector = new Point3D(-1, 0, 0); }

        // Right face
        if(this.isAlmostEqual(this.intersectionPoint.getX(), frontUpperRightVertex.getX())) { this.normalVector = new Point3D(1, 0, 0); }

        // Top face
        if(this.isAlmostEqual(this.intersectionPoint.getY(), backBottomLeftVertex.getY())) { this.normalVector = new Point3D(0, -1, 0); }

        // Bottom face
        if(this.isAlmostEqual(this.intersectionPoint.getY(), frontUpperRightVertex.getY())) { this.normalVector = new Point3D(0, 1, 0); }

        // Front face
        if(this.isAlmostEqual(this.intersectionPoint.getZ(), backBottomLeftVertex.getZ())) { this.normalVector = new Point3D(0, 0, -1); }

        // Back face
        if(this.isAlmostEqual(this.intersectionPoint.getZ(), frontUpperRightVertex.getZ())) { this.normalVector = new Point3D(0, 0, 1); }
    }

    /** Checks whether one value is equal to another within a margin of error of 0.001. */
    private boolean isAlmostEqual(double a, double b) {
        return (Math.abs(a - b) < 0.001);
    }

    public double getTValue() { return tValue; }
    public Point3D getIntersectionPoint() {
        return intersectionPoint;
    }
    public Point3D getNormalVector() {
        return normalVector;
    }
    
}
