public class Intersection {

    private Point3D intersectionPoint;
    private Point3D normalVector;

    public Intersection(Point3D intersectionPoint, Point3D normalVector) {
        this.intersectionPoint = intersectionPoint;
        this.normalVector = normalVector;
    }

    public Point3D getIntersectionPoint() {
        return intersectionPoint;
    }

    public Point3D getNormalVector() {
        return normalVector;
    }
}
