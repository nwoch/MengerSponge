
public class SpongeCube {

    private Point3D backBottomLeftVertex;
    private Point3D frontUpperRightVertex;
    private double edgeLength;
    private int levelInSponge;
    private CubeIntersection intersectionWithRay;

    public SpongeCube(Point3D backBottomLeftVertex, double edgeLength, int levelInSponge) {
        this.backBottomLeftVertex = backBottomLeftVertex;
        this.edgeLength = edgeLength;
        this.levelInSponge = levelInSponge;
    }

    public boolean intersectWithRay(Point3D rayStartPoint, Point3D ray) {
        // Find t intervals
        double[][] tIntervals = this.solveForTValues(rayStartPoint, ray);

        // Find if/where intervals overlap
        double[] overlappingTInterval = this.findOverlappingTInterval(tIntervals);
        double minOverlappingT = overlappingTInterval[0];
        double maxOverlappingT = overlappingTInterval[1];
        if(maxOverlappingT < 0 || maxOverlappingT < minOverlappingT) { return false; } // Intervals don't overlap or ray does not start before the sponge - no intersection
        if(minOverlappingT < 0) { this.intersectionWithRay = new CubeIntersection(maxOverlappingT); } // Ray starts inside the sponge, so must use maxOverlappingT for the 2nd intersection point
        this.intersectionWithRay = new CubeIntersection(minOverlappingT); // Use t of first intersection point
        return true;
    }

    public void findIntersectionPointAndNormal(Point3D rayStartPoint, Point3D ray) {
        this.intersectionWithRay.findIntersectionPoint(rayStartPoint, ray);
        this.intersectionWithRay.findIntersectedFaceNormal(this.backBottomLeftVertex, this.frontUpperRightVertex);
    }

    private double[][] solveForTValues(Point3D rayStartPoint, Point3D ray) {
        double[][] tIntervals = new double[3][2];
        this.frontUpperRightVertex = new Point3D(this.backBottomLeftVertex.getX() + this.edgeLength,
                                                 this.backBottomLeftVertex.getY() + this.edgeLength,
                                                 this.backBottomLeftVertex.getZ() + this.edgeLength);
        // back bottom left will always be all min coordinates, while front upper right will always be all max coordinates
        tIntervals[0][0] = (this.backBottomLeftVertex.getX() - rayStartPoint.getX()) / ray.getX(); // x min t
        tIntervals[0][1] = (frontUpperRightVertex.getX() - rayStartPoint.getX()) / ray.getX(); // x max t
        tIntervals[1][0] = (this.backBottomLeftVertex.getY() - rayStartPoint.getY()) / ray.getY(); // y min t
        tIntervals[1][1] = (frontUpperRightVertex.getY() - rayStartPoint.getY()) / ray.getY(); // y max t
        tIntervals[2][0] = (this.backBottomLeftVertex.getZ() - rayStartPoint.getZ()) / ray.getZ(); // z min t
        tIntervals[2][1] = (frontUpperRightVertex.getZ() - rayStartPoint.getZ()) / ray.getZ(); // z max t
        return tIntervals;
    }

    private double[] findOverlappingTInterval(double[][] tIntervals) {
        double[] minTValues = new double[] {Math.min(tIntervals[0][0], tIntervals[0][1]),
                                            Math.min(tIntervals[1][0], tIntervals[1][1]),
                                            Math.min(tIntervals[2][0], tIntervals[2][1])};

        double[] maxTValues = new double[] {Math.max(tIntervals[0][0], tIntervals[0][1]),
                                            Math.max(tIntervals[1][0], tIntervals[1][1]),
                                            Math.max(tIntervals[2][0], tIntervals[2][1])};

        double minOverlappingT = Math.max(Math.max(minTValues[0], minTValues[1]), minTValues[2]);
        double maxOverlappingT = Math.min(Math.min(maxTValues[0], maxTValues[1]), maxTValues[2]);
        return new double[] {minOverlappingT, maxOverlappingT};
    }

    public Point3D getBackBottomLeftVertex() { return backBottomLeftVertex; }
    public double getEdgeLength() { return edgeLength; }
    public int getLevelInSponge() { return levelInSponge; }
    public CubeIntersection getIntersectionWithRay() { return intersectionWithRay; }

}
