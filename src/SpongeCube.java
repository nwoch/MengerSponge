
/**
 * Class which represents a cube in the Menger sponge. Such a cube can be represented initially only with its back bottom left vertex,
 * its edge length, and its level in the sponge (Levels decrease as you go deeper into the sponge/break the cubes down into smaller and smaller cubes.
 * Note: this is the opposite of how the sponges are actually classified in reality; the first sponge cube is level 0, the next 20 are level 1, etc.
 * But for the purposes of generalizing the code, it was easier to count down levels.) Contains methods for determining whether a ray intersects
 * the cube at any point, and for actually finding the point of intersection and the normal vector at that point if necessary.
 */
public class SpongeCube {

    private Point3D backBottomLeftVertex;
    private Point3D frontUpperRightVertex;
    private double edgeLength;
    private int levelInSponge;
    private CubeIntersection intersectionWithRay;

    /** Constructor which sets up the class variables that define the cube. */
    public SpongeCube(Point3D backBottomLeftVertex, double edgeLength, int levelInSponge) {
        this.backBottomLeftVertex = backBottomLeftVertex;
        this.edgeLength = edgeLength;
        this.levelInSponge = levelInSponge;
    }

    /**
     * Determines whether a ray intersects the cube using an AABB bounding box algorithm, which works because
     * the sponge will always be set up to be aligned with the coordinates axes (cannot be rotated), making it so that every cube in the sponge becomes a bounding box.
     * The first step in the algorithm is finding the t-intervals at which the ray intersects with the bounding planes of the cube
     * by solving for t for each min. and max. x, y, and z of the cube (using the ray eqn. P = P0 + tv).
     * The next step is determining if/where the intervals overlap: if they do overlap, the ray actually intersected within the bounds of the cube.
     * The minimum of the overlapping t-values, if such values existed, is used as it is the t-value for the point at which the ray first intersected
     */
    public boolean intersectWithRay(Point3D rayStartPoint, Point3D ray) {
        // Find the t-intervals
        double[][] tIntervals = this.solveForTValues(rayStartPoint, ray);

        // Find if/where intervals overlap
        double[] overlappingTInterval = this.findOverlappingTInterval(tIntervals);
        double minOverlappingT = overlappingTInterval[0];
        double maxOverlappingT = overlappingTInterval[1];
        if(maxOverlappingT < 0 || maxOverlappingT < minOverlappingT) { return false; } // Intervals don't overlap or ray does not start before the sponge - no intersection
        if(minOverlappingT < 0) { this.intersectionWithRay = new CubeIntersection(maxOverlappingT); } // Ray starts inside the sponge, so must use maxOverlappingT for the 2nd intersection point as the t-value
        this.intersectionWithRay = new CubeIntersection(minOverlappingT); // Use t of first intersection point
        return true;
    }

    /**
     * Calls methods to find the intersection point and the normal vector of the intersected face using the previously computed t-value.
     * Only called once it is determined that this cube is the cube that is first intersected by the specified ray.
     */
    public void findIntersectionPointAndNormal(Point3D rayStartPoint, Point3D ray) {
        this.intersectionWithRay.findIntersectionPoint(rayStartPoint, ray);
        this.intersectionWithRay.findIntersectedFaceNormal(this.backBottomLeftVertex, this.frontUpperRightVertex);
    }

    /**
     * Uses the minimum and maximum x, y, and z values among the vertices of the cube
     * (back bottom left vertex consists of all the min. values and front upper right vertex consists of all the max. values)
     * to solve for the t-values of each min. and max. using the equation of the ray (but for a single x, y, or z-coordinate at a time): P = P0 + tv.
     * These t-values give the intersection intervals for x, y, and z.
     */
    private double[][] solveForTValues(Point3D rayStartPoint, Point3D ray) {
        double[][] tIntervals = new double[3][2];
        this.frontUpperRightVertex = new Point3D(this.backBottomLeftVertex.getX() + this.edgeLength,
                                                 this.backBottomLeftVertex.getY() + this.edgeLength,
                                                 this.backBottomLeftVertex.getZ() + this.edgeLength);
        // back bottom left will always be all min coordinates, while front upper right will always be all max coordinates
        // use equation of ray to solve for one coordinate at a time
        tIntervals[0][0] = (this.backBottomLeftVertex.getX() - rayStartPoint.getX()) / ray.getX(); // x min t
        tIntervals[0][1] = (frontUpperRightVertex.getX() - rayStartPoint.getX()) / ray.getX(); // x max t
        tIntervals[1][0] = (this.backBottomLeftVertex.getY() - rayStartPoint.getY()) / ray.getY(); // y min t
        tIntervals[1][1] = (frontUpperRightVertex.getY() - rayStartPoint.getY()) / ray.getY(); // y max t
        tIntervals[2][0] = (this.backBottomLeftVertex.getZ() - rayStartPoint.getZ()) / ray.getZ(); // z min t
        tIntervals[2][1] = (frontUpperRightVertex.getZ() - rayStartPoint.getZ()) / ray.getZ(); // z max t
        return tIntervals;
    }

    /**
     * Find the interval at which the computed t-intervals for x, y, and z overlap.
     * This is the interval at which the ray intersects within the bounds of the cube.
     * This is first done by finding the min. and max. of each of the x, y, and z t-intervals,
     * and the finding the max of the minimums and the min of the maximums to get the interval that overlaps all 3.
     */
    private double[] findOverlappingTInterval(double[][] tIntervals) {
        // Find mins and maxes of each t-interval
        double[] minTValues = new double[] {Math.min(tIntervals[0][0], tIntervals[0][1]),
                                            Math.min(tIntervals[1][0], tIntervals[1][1]),
                                            Math.min(tIntervals[2][0], tIntervals[2][1])};

        double[] maxTValues = new double[] {Math.max(tIntervals[0][0], tIntervals[0][1]),
                                            Math.max(tIntervals[1][0], tIntervals[1][1]),
                                            Math.max(tIntervals[2][0], tIntervals[2][1])};

        // Find max of mins to find min overlapping t
        double minOverlappingT = Math.max(Math.max(minTValues[0], minTValues[1]), minTValues[2]);
        //Find min on maxes to find max overlapping t
        double maxOverlappingT = Math.min(Math.min(maxTValues[0], maxTValues[1]), maxTValues[2]);
        return new double[] {minOverlappingT, maxOverlappingT};
    }

    public Point3D getBackBottomLeftVertex() { return backBottomLeftVertex; }
    public double getEdgeLength() { return edgeLength; }
    public int getLevelInSponge() { return levelInSponge; }
    public CubeIntersection getIntersectionWithRay() { return intersectionWithRay; }

}
