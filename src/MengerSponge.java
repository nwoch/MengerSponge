import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class MengerSponge {

    private final SpongeCube startingCube;

    public MengerSponge(Point3D backBottomLeftVertex, double startingEdgeLength, int spongeLevel) {
        this.startingCube = new SpongeCube(backBottomLeftVertex, startingEdgeLength, spongeLevel);
    }

    public CubeIntersection intersectWithRay(Point3D rayStartPoint, Point3D ray) {
        List<SpongeCube> intersectedCubes = this.findIntersectedCubes(rayStartPoint, ray);
        if(!intersectedCubes.isEmpty()) {
            SpongeCube intersectedCube = this.findNearestIntersectedCube(intersectedCubes);
            intersectedCube.findIntersectionPointAndNormal(rayStartPoint, ray);
            return intersectedCube.getIntersectionWithRay();
        }
        return null;
    }

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
                    for(int yIndex = 0; yIndex < 3; yIndex++) {
                        double y = backBottomLeftY + (yIndex * edgeIncrement);
                        for(int zIndex = 0; zIndex < 3; zIndex++) {
                            double z = backBottomLeftZ + (zIndex * edgeIncrement);
                            for(int xIndex = 0; xIndex < 3; xIndex++) {
                                double x = backBottomLeftX + (xIndex * edgeIncrement);
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
