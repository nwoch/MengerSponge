import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.*;

public class SpongeCube {

    private static final double EPSILON = 0.001;

    private Point3D upperLeftVertex;
    private double edgeLength;
    private int level;
    private Point3D[] vertices;
    private double t;

    public SpongeCube(Point3D upperLeftVertex, double edgeLength, int level) {
        this.upperLeftVertex = upperLeftVertex;
        this.edgeLength = edgeLength;
        this.level = level;
        this.vertices = new Point3D[8];
    }

    public boolean intersectsSpongeCube2(Point3D o, Point3D dir) {
        Point3D lb = this.upperLeftVertex;
        Point3D rt = new Point3D(lb.getX() + edgeLength, lb.getY() + edgeLength, lb.getZ() + edgeLength);

        // r.dir is unit direction vector of ray
        Point3D dirfrac = new Point3D();
        dirfrac.x = 1.0f / dir.getX();
        dirfrac.y = 1.0f / dir.getY();
        dirfrac.z = 1.0f / dir.getZ();
// lb is the corner of AABB with minimal coordinates - left bottom, rt is maximal corner
// r.org is origin of ray
        double t1 = (lb.x - o.x)*dirfrac.x;
        double t2 = (rt.x - o.x)*dirfrac.x;
        double t3 = (lb.y - o.y)*dirfrac.y;
        double t4 = (rt.y - o.y)*dirfrac.y;
        double t5 = (lb.z - o.z)*dirfrac.z;
        double t6 = (rt.z - o.z)*dirfrac.z;

        double tmin = max(max(min(t1, t2), min(t3, t4)), min(t5, t6));
        double tmax = min(min(max(t1, t2), max(t3, t4)), max(t5, t6));

// if tmax < 0, ray (line) is intersecting AABB, but the whole AABB is behind us
        if (tmax < 0)
        {
            t = tmax;
            return false;
        }

// if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax)
        {
            t = tmax;
            return false;
        }

        t = tmin;
        return true;
    }

    public boolean intersectsSpongeCube(Point3D startingPoint, Point3D ray) {
        double[] maxMinValues = new double[]{this.upperLeftVertex.getX(), this.upperLeftVertex.getX() + this.edgeLength,
                                             this.upperLeftVertex.getY(), this.upperLeftVertex.getY() + this.edgeLength,
                                             this.upperLeftVertex.getZ(), this.upperLeftVertex.getZ() + this.edgeLength};
        //solve for Ts using equation of line
        double[] startingPointCoords = new double[] {startingPoint.getX(), startingPoint.getY(), startingPoint.getZ()};
        double[] rayDirections = new double[] {ray.getX(), ray.getY(), ray.getZ()};
        double[][] tIntervals = new double[3][2];
        for(int i = 0; i < 6; i++) {
            double t = (maxMinValues[i] - startingPointCoords[i/2])/rayDirections[i/2];
            tIntervals[i/2][i%2] = t;
        }
        //find if/where intervals overlap
        double minT = this.findMax(extractRangeStart(tIntervals));
        double maxT = this.findMin(extractRangeEnd(tIntervals));
        if(maxT < 0 || maxT < minT) { return false; }
        if(minT < 0) { this.t = maxT; } // ray starts inside the sponge, so must use 2nd intersection point/maxT
        this.t = minT;
        return true;
    }

    private double[] extractRangeEnd(double[][] tIntervals) {
        return new double[] {Math.max(tIntervals[0][0],tIntervals[0][1]),Math.max(tIntervals[1][0],tIntervals[1][1]),Math.max(tIntervals[2][0],tIntervals[2][1])};
    }

    private double[] extractRangeStart(double[][] tIntervals) {
        return new double[] {Math.min(tIntervals[0][0],tIntervals[0][1]),Math.min(tIntervals[1][0],tIntervals[1][1]),Math.min(tIntervals[2][0],tIntervals[2][1])};
    }

    public Point3D findIntersectionPoint(Point3D startingPoint, Point3D ray) {
        return startingPoint.addVector(ray.scale(this.t));
    }

    public double findMin(double[] values) {
        double currentMin = values[0];
        for(int i = 1; i < values.length; i++) {
            currentMin = Math.min(currentMin, values[i]);
        }
        return currentMin;
    }

    public double findMax(double[] values) {
        double currentMax = values[0];
        for(int i = 1; i < values.length; i++) {
            currentMax = Math.max(currentMax, values[i]);
        }
        return currentMax;
    }

    //this works bc cube is always aligned with axes - can only be translated, not rotated
    public Point3D findIntersectedFaceNormal(Point3D intersectionPoint) {
        if (almostEqual(intersectionPoint.getX(), upperLeftVertex.getX())) {
            // Left face
            return new Point3D(-1, 0, 0);
        }
        if (almostEqual(intersectionPoint.getX(), upperLeftVertex.getX() + edgeLength)) {
            // Right face
            return new Point3D(1, 0, 0);
        }
        if (almostEqual(intersectionPoint.getY(), upperLeftVertex.getY())) {
            // Top face
            return new Point3D(0, -1, 0);
        }
        if (almostEqual(intersectionPoint.getY(), upperLeftVertex.getY() + edgeLength)) {
            // Bottom face
            return new Point3D(0, 1, 0);
        }
        if (almostEqual(intersectionPoint.getZ(), upperLeftVertex.getZ())) {
            // Close face
            return new Point3D(0, 0, -1);
        }
        if (almostEqual(intersectionPoint.getZ(), upperLeftVertex.getZ() + edgeLength)) {
            // Far face
            return new Point3D(0, 0, 1);
        }
        // TODO: make this actually useful
        throw new IllegalStateException("Oops. That's an issue");
//        //combine this with min/max values in intersection method
//        //corner points?
//        //check top and bottom faces
//        Point3D[] faceVertices;
//        faceVertices = new Point3D[]{this.vertices[0], this.vertices[1], this.vertices[2], this.vertices[3]};
//        double[] xValues = new double[8];
//        double[] yValues = new double[8];
//        double[] zValues = new double[8];
//        for(int j = 0; j < faceVertices.length; j++) {
//            xValues[j] = faceVertices[j].getX();
//            yValues[j] = faceVertices[j].getY();
//            zValues[j] = faceVertices[j].getZ();
//        }
//        if(this.findMaxOrMin(xValues, 0) <= intersectionPoint.getX() && this.findMaxOrMin(xValues, 1) >= intersectionPoint.getX()) {
//            if(this.findMaxOrMin(yValues, 0) <= intersectionPoint.getY() && this.findMaxOrMin(yValues, 1) >= intersectionPoint.getY()) {
//                if(this.findMaxOrMin(zValues, 0) <= intersectionPoint.getZ() && this.findMaxOrMin(zValues, 1) >= intersectionPoint.getZ()) {
//                    //calc normal
//                    Point3D faceVector1 = faceVertices[1].subtractVector(faceVertices[0]);
//                    Point3D faceVector2 = faceVertices[2].subtractVector(faceVertices[1]);
//                    return faceVector1.crossProduct(faceVector2);
//                }
//            }
//        }
//        faceVertices = new Point3D[]{this.vertices[7], this.vertices[6], this.vertices[5], this.vertices[4]};
//        xValues = new double[8];
//        yValues = new double[8];
//        zValues = new double[8];
//        for(int j = 0; j < faceVertices.length; j++) {
//            xValues[j] = faceVertices[j].getX();
//            yValues[j] = faceVertices[j].getY();
//            zValues[j] = faceVertices[j].getZ();
//        }
//        if(this.findMaxOrMin(xValues, 0) <= intersectionPoint.getX() && this.findMaxOrMin(xValues, 1) >= intersectionPoint.getX()) {
//            if(this.findMaxOrMin(yValues, 0) <= intersectionPoint.getY() && this.findMaxOrMin(yValues, 1) >= intersectionPoint.getY()) {
//                if(this.findMaxOrMin(zValues, 0) <= intersectionPoint.getZ() && this.findMaxOrMin(zValues, 1) >= intersectionPoint.getZ()) {
//                    //calc normal
//                    Point3D faceVector1 = faceVertices[1].subtractVector(faceVertices[0]);
//                    Point3D faceVector2 = faceVertices[2].subtractVector(faceVertices[1]);
//                    return faceVector1.crossProduct(faceVector2);
//                }
//            }
//        }
//        //check side faces
//        for(int i = 0; i < 3; i++) {
//            faceVertices = new Point3D[]{this.vertices[i], this.vertices[i + 4], this.vertices[i + 5], this.vertices[i + 1]};
//            xValues = new double[8];
//            yValues = new double[8];
//            zValues = new double[8];
//            for(int j = 0; j < faceVertices.length; j++) {
//                xValues[j] = faceVertices[j].getX();
//                yValues[j] = faceVertices[j].getY();
//                zValues[j] = faceVertices[j].getZ();
//            }
//            if(this.findMaxOrMin(xValues, 0) <= intersectionPoint.getX() && this.findMaxOrMin(xValues, 1) >= intersectionPoint.getX()) {
//                if(this.findMaxOrMin(yValues, 0) <= intersectionPoint.getY() && this.findMaxOrMin(yValues, 1) >= intersectionPoint.getY()) {
//                    if(this.findMaxOrMin(zValues, 0) <= intersectionPoint.getZ() && this.findMaxOrMin(zValues, 1) >= intersectionPoint.getZ()) {
//                        //calc normal
//                        Point3D faceVector1 = faceVertices[1].subtractVector(faceVertices[0]);
//                        Point3D faceVector2 = faceVertices[2].subtractVector(faceVertices[1]);
//                        return faceVector1.crossProduct(faceVector2);
//                    }
//                }
//            }
//        }
//        //check back face
//        faceVertices = new Point3D[]{this.vertices[0], this.vertices[3], this.vertices[4], this.vertices[7]};
//        xValues = new double[8];
//        yValues = new double[8];
//        zValues = new double[8];
//        for(int j = 0; j < faceVertices.length; j++) {
//            xValues[j] = faceVertices[j].getX();
//            yValues[j] = faceVertices[j].getY();
//            zValues[j] = faceVertices[j].getZ();
//        }
//        if(this.findMaxOrMin(xValues, 0) <= intersectionPoint.getX() && this.findMaxOrMin(xValues, 1) >= intersectionPoint.getX()) {
//            if(this.findMaxOrMin(yValues, 0) <= intersectionPoint.getY() && this.findMaxOrMin(yValues, 1) >= intersectionPoint.getY()) {
//                if(this.findMaxOrMin(zValues, 0) <= intersectionPoint.getZ() && this.findMaxOrMin(zValues, 1) >= intersectionPoint.getZ()) {
//                    //calc normal
//                    Point3D faceVector1 = faceVertices[1].subtractVector(faceVertices[0]);
//                    Point3D faceVector2 = faceVertices[2].subtractVector(faceVertices[1]);
//                    return faceVector1.crossProduct(faceVector2);
//                }
//            }
//        }
//        System.out.println(intersectionPoint.getX() + ", " + intersectionPoint.getY() + ", " + intersectionPoint.getZ());
//        for(Point3D vertex : this.vertices) {
//            System.out.println(vertex.getX() + ", " + vertex.getY() + ", " + vertex.getZ());
//        }
//        return null;
    }

    private boolean almostEqual(double a, double b) {
        return Math.abs(a-b) < EPSILON;
    }

    //methods: group coordinates by XYZ, findNormal, checkIfPointInsideFace

    public Point3D getUpperLeftVertex() {
        return upperLeftVertex;
    }

    public double getEdgeLength() {
        return edgeLength;
    }

    public int getLevel() {
        return level;
    }

    public double getT() {
        return t;
    }
}
