
/**
 * @author Nicole Woch
 * Class which represents a point/vector in 3-dimensional space and provides operations that can be performed on/with it.
 */
public class Point3D {

    private double x;
    private double y;
    private double z;

    /** Constructor which sets the x, y, and z coordinates/directions of the point/vector. */
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /** Assuming this Point3D is a vector, calculates the dot product of this vector with another vector. */
    public double dotProduct(Point3D vector) {
        return ((this.x * vector.x) + (this.y * vector.y) + (this.z * vector.z));
    }

    /** Assuming this Point3D is a vector, calculates the cross product of this vector with another vector. */
    public Point3D crossProduct(Point3D vector) {
        double xCross = (this.y * vector.z) - (this.z * vector.y);
        double yCross = (this.z * vector.x) - (this.x * vector.z);
        double zCross = (this.x * vector.y) - (this.y * vector.x);
        return (new Point3D(xCross, yCross, zCross));
    }

    /** Adds a vector to this point/vector and returns the new vector/point. */
    public Point3D addVector(Point3D vector) {
        return (new Point3D(this.x + vector.x, this.y + vector.y, this.z + vector.z));
    }

    /** Subtracts a vector from this point/vector and returns the new vector/point. */
    public Point3D subtractVector(Point3D vector) {
        return (new Point3D(this.x - vector.x, this.y - vector.y, this.z - vector.z));
    }

    /** Assuming this Point3D is a vector, scales it by the specified factor. */
    public Point3D scale(double scaleFactor) {
        return (new Point3D(this.x * scaleFactor, this.y * scaleFactor, this.z * scaleFactor));
    }

    /** Assuming this Point3D is a vector, normalizes it. */
    public Point3D normalize() {
        double magnitude = this.magnitude();
        return (new Point3D(this.x/magnitude, this.y/magnitude, this.z/magnitude));
    }

    /** Assuming this Point3D is a vector, calculates its magnitude. */
    public double magnitude() {
        double sum = Math.pow(this.x, 2.0) + Math.pow(this.y, 2.0) + Math.pow(this.z, 2.0);
        return Math.sqrt(sum);
    }

    /** Assuming this Point3D is a vector, calculates angle between it and another vector. */
    public double calcAngle(Point3D vector) {
        double cos = (this.dotProduct(vector))/(this.magnitude() * vector.magnitude());
        return Math.acos(cos);
    }

    /** Getters & Setters */
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getZ() { return z; }
    public void setZ(double z) { this.z = z; }

}
