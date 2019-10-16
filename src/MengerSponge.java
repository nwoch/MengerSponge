import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which represents the Menger sponge, a 3D fractal.Contains a starting cube which is then broken down into smaller cubes.
 * Contains methods for determining whether a ray intersects with any of the sponge's cubes at its deepest level,
 * and if so, which one it intersects with first.
 */
public class MengerSponge {

    private final SpongeCube startingCube;

    /**
     * Constructor which creates the initial cube which represents the first level of the Menger sponge
     * using the back bottom left vertex of the cube and the edge length. Also allows for the specification of how many levels the sponge will have.
     * This cube is then broken down into smaller cubes at each subsequent level.
     */
    public MengerSponge(Point3D backBottomLeftVertex, double startingEdgeLength, int spongeLevel) {
        this.startingCube = new SpongeCube(backBottomLeftVertex, startingEdgeLength, spongeLevel);
    }

    /**
     * Finds the first point of intersection of a ray with the sponge. First finds all cubes that are intersected at the deepest level of the sponge,
     * then finds which of those cubes is intersected first, then finds the point of intersection and the normal vector at that point.
     * Returns a CubeIntersection object if an intersection is found, and null if not.
     */
    public CubeIntersection intersectWithRay(Point3D rayStartPoint, Point3D ray) {
        List<SpongeCube> intersectedCubes = this.findIntersectedCubes(rayStartPoint, ray);
        if(!intersectedCubes.isEmpty()) {
            SpongeCube intersectedCube = this.findNearestIntersectedCube(intersectedCubes);
            intersectedCube.findIntersectionPointAndNormal(rayStartPoint, ray);
            return intersectedCube.getIntersectionWithRay();
        }
        return null;
    }

    /**
     * Finds which, if any, cubes are intersected by the specified ray at each level of the sponge.
     * If a cube is found to be intersected, it is broken down into 27 smaller cubes,
     * 7 of which are excluded from further checks to create the holes in the resulting sponge.
     * The remaining 20 cubes are added to a queue to be checked for intersections.
     * Performs intersection checks only for the smaller cubes that are already known to potentially intersect
     * with the ray based on the ray's intersection at the previous level with the larger cube which contained them.
     * Returns only the cubes which are intersected at the deepest level of the sponge.
     */
    private List<SpongeCube> findIntersectedCubes(Point3D rayStartPoint, Point3D ray) {
        Queue<SpongeCube> cubesToCheck = new LinkedList<>();
        List<SpongeCube> intersectedCubes = new ArrayList<>();
        cubesToCheck.add(this.startingCube);

        while(!cubesToCheck.isEmpty()) {
            SpongeCube cube = cubesToCheck.remove();
            if(cube.intersectWithRay(rayStartPoint, ray)) {
                if(cube.getLevelInSponge() == 0) {
                    intersectedCubes.add(cube);
                } else {
                    double edgeIncrement = cube.getEdgeLength() / 3.0;
                    double backBottomLeftX = cube.getBackBottomLeftVertex().getX();
                    double backBottomLeftY = cube.getBackBottomLeftVertex().getY();
                    double backBottomLeftZ = cube.getBackBottomLeftVertex().getZ();
                    int nextLevel = cube.getLevelInSponge() - 1;
                    // Split the current cube into a top, middle, and bottom layer
                    // and iterate through the rows and columns of each of them one at a time
                    // to break the current cube down into 27 smaller cubes
                    for(int yIndex = 0; yIndex < 3; yIndex++) {
                        double y = backBottomLeftY + (yIndex * edgeIncrement);
                        for(int zIndex = 0; zIndex < 3; zIndex++) {
                            double z = backBottomLeftZ + (zIndex * edgeIncrement);
                            for(int xIndex = 0; xIndex < 3; xIndex++) {
                                double x = backBottomLeftX + (xIndex * edgeIncrement);
                                // If the cube is in the middle of the top or bottom layer or in the middle row or column of the middle layer,
                                // don't add it to the queue of cubes to be checked because this is where a hole is meant to be in the sponge
                                if((yIndex == 1 && (zIndex == 1 || xIndex == 1)) || (zIndex == 1 && xIndex == 1)) {
                                    continue;
                                }
                                cubesToCheck.add(new SpongeCube(new Point3D(x, y, z), edgeIncrement, nextLevel));
                            }
                        }
                    }
                }
            }
        }
        return intersectedCubes;
    }

    /**
     * Of the cubes intersected by the ray at the deepest level of the sponge,
     * returns the one which gets intersected first (nearest cube to the starting point of the ray)
     * by finding the cube with the minimum t-value. The t-value is the scalar by which
     * the ray's direction vector is multiplied to get a particular point the ray goes through (used in the eqn. of the ray: P = P0 + tv).
     */
    private SpongeCube findNearestIntersectedCube(List<SpongeCube> intersectedCubes) {
        SpongeCube minTValueCube = intersectedCubes.get(0);
        for(int i = 1; i < intersectedCubes.size(); i++) {
            double currentCubeTValue = intersectedCubes.get(i).getIntersectionWithRay().getTValue();
            if(currentCubeTValue == Math.min(minTValueCube.getIntersectionWithRay().getTValue(), currentCubeTValue)) {
                minTValueCube = intersectedCubes.get(i);
            }
        }
        return minTValueCube;
    }

}
