
public class SpongeCube {

    private Point3D upperLeftVertex;
    private double edgeLength;
    private int level;
    private double t;

    public SpongeCube(Point3D upperLeftVertex, double edgeLength, int level) {
        this.upperLeftVertex = upperLeftVertex;
        this.edgeLength = edgeLength;
        this.level = level;
    }

    public boolean intersectsSpongeCube(Point3D startingPoint, Point3D ray) {
        Point3D[] vertices = this.computeVertices();
        double[] xValues = new double[8];
        double[] yValues = new double[8];
        double[] zValues = new double[8];
        for(int i = 0; i < vertices.length; i++) {
            xValues[i] = vertices[i].getX();
            yValues[i] = vertices[i].getY();
            zValues[i] = vertices[i].getZ();
        }
        double[] maxMinValues = new double[]{this.findMaxOrMin(xValues, 0), this.findMaxOrMin(xValues, 1),
                                             this.findMaxOrMin(yValues, 0), this.findMaxOrMin(yValues, 1),
                                             this.findMaxOrMin(zValues, 0), this.findMaxOrMin(zValues, 1)};
        //solve for T's using equation of line
        double[] startingPointCoords = new double[] {startingPoint.getX(), startingPoint.getY(), startingPoint.getZ()};
        double[] rayDirections = new double[] {ray.getX(), ray.getY(), ray.getZ()};
        double[][] tIntervals = new double[3][2];
        for(int i = 0; i < 6; i++) {
            // integer division?
            double t = (maxMinValues[i] - startingPointCoords[i/2])/rayDirections[i/2];
            tIntervals[i/2][i % 2] = t;
        }
        //find if/where intervals overlap
        double maxT = this.findMaxOrMin(new double[] {tIntervals[0][0], tIntervals[1][0], tIntervals[2][0]}, 1);
        double minT = this.findMaxOrMin(new double[] {tIntervals[0][1], tIntervals[1][1], tIntervals[2][2]}, 0);
        if(maxT < minT) { return false; }
        this.t = minT;
        return true;
    }

    public Point3D findIntersectionPoint(Point3D startingPoint, Point3D ray) {
        return startingPoint.addVector(ray.scale(this.t));
    }

    private Point3D[] computeVertices() {
        Point3D[] vertices = new Point3D[8];
        vertices[0] = this.upperLeftVertex;
        vertices[3] = new Point3D(this.upperLeftVertex.getX(), this.upperLeftVertex.getY() - this.edgeLength, this.upperLeftVertex.getZ());
        for(int i = 0; i < 5; i += 4) {
            vertices[i + 1] = new Point3D(vertices[i].getX(), vertices[i].getY(), vertices[i].getZ() + this.edgeLength);
            vertices[i + 2] = new Point3D(vertices[i].getX() + this.edgeLength, vertices[i].getY(), vertices[i].getZ() + this.edgeLength);
            vertices[i + 3] = new Point3D(vertices[i].getX() + this.edgeLength, vertices[i].getY(), vertices[i].getZ());
        }
        return vertices;
    }

    private double findMaxOrMin(double[] values, int maxOrMin) {
        double currentMaxOrMin = values[0];
        if(maxOrMin == 0) {
            for(int i = 1; i < values.length; i++) {
                currentMaxOrMin = Math.min(currentMaxOrMin, values[i]);
            }
        }
        else if (maxOrMin == 1) {
            for(int i = 1; i < values.length; i++) {
                currentMaxOrMin = Math.min(currentMaxOrMin, values[i]);
            }
        }
        return currentMaxOrMin;
    }

    public Point3D getUpperLeftVertex() {
        return upperLeftVertex;
    }

    public double getEdgeLength() {
        return edgeLength;
    }

    public int getLevel() {
        return level;
    }
}
