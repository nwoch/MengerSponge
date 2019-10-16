
public class CubeIntersection {

    private double tValue;
    private Point3D intersectionPoint;
    private Point3D normalVector;

    public CubeIntersection(double tValue) {
        this.tValue = tValue;
    }

    public void findIntersectionPoint(Point3D rayStartPoint, Point3D ray) {
        this.intersectionPoint = rayStartPoint.addVector(ray.scale(this.tValue));
    }

    //this works bc cube is always aligned with axes - can only be translated, not rotated
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
