public class Sphere {

    private double radius;
    private Point3D center;
    private double distanceToIntersection;

    public Sphere(double radius, Point3D center) {
        this.radius = radius;
        this.center = center;
    }

    public Point3D intersectWithSphere(Point3D startingPoint, Point3D ray) {
        // ray passed in must be normalized (unit vector)
        Point3D co = startingPoint.subtractVector(this.center);
        double b = 2 * (co.dotProduct(ray));
        double c = co.dotProduct(co) - radius*radius;
        double delta = b*b - 4*c;
        if (delta < 0) { return null; }
        double sqrtDelta = Math.sqrt(delta);
        double negT = (-b - sqrtDelta) / 2;
        double posT = (-b + sqrtDelta) / 2;
        if(negT <= 0 && posT <= 0) { return null; }
        if(negT <= 0 && posT > 0) {
            distanceToIntersection = posT;
        } else {
            distanceToIntersection = negT;
        }
        return startingPoint.addVector(ray.scale(distanceToIntersection));
    }

    public double getDistanceToIntersection() {
        return distanceToIntersection;
    }
}
