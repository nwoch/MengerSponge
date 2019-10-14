import java.util.*;

public class MengerSponge {

    private Queue<SpongeCube> cubes;

    public MengerSponge() {
        cubes = new LinkedList<>();
        cubes.add(new SpongeCube(new Point3D(-100.0, 100.0, -100.0), 200.0, 4));
    }

//    public List<SpongeCube> buildMengerSponge(List<SpongeCube> currentSpongeCubes, int n) {
//        if(n == 0) {
//            return currentSpongeCubes;
//        }
//        List<SpongeCube> newSpongeCubes = new ArrayList<>();
//        double edgeIncrement = currentSpongeCubes.get(0).getEdgeLength()/3.0;
//        for(SpongeCube cube : currentSpongeCubes) {
//            double upperLeftX = cube.getVertices()[0].getX();
//            double upperLeftY = cube.getVertices()[0].getY();
//            double upperLeftZ = cube.getVertices()[0].getZ();
//            for(int i = 0; i < 3; i++) {
//                double y = upperLeftY * -i;
//                for(int j = 0; j < 3; j++) {
//                    double z = upperLeftZ * j;
//                    for(int k = 0; k < 3; k++) {
//                        double x = upperLeftX * k;
//                        newSpongeCubes.add(new SpongeCube(new Point3D(x, y, z), edgeIncrement));
//                    }
//                }
//            }
//        }
//        return buildMengerSponge(newSpongeCubes, n - 1);
//    }

    public Intersection intersectWithSponge(Point3D startingPoint, Point3D ray) {
        //make increments generic for any direction
        List<SpongeCube> intersectedCubes = new ArrayList<>();
        while(!cubes.isEmpty()) {
            SpongeCube cube = cubes.remove();
            if(cube.intersectsSpongeCube(startingPoint, ray)) {
                if(cube.getLevel() == 0) {
                    intersectedCubes.add(cube);
                } else {
                    double edgeIncrement = cube.getEdgeLength() / 3.0;
                    double upperLeftX = cube.getUpperLeftVertex().getX();
                    double upperLeftY = cube.getUpperLeftVertex().getY();
                    double upperLeftZ = cube.getUpperLeftVertex().getZ();
                    int level = cube.getLevel();
                    for(int i = 0; i < 3; i++) {
                        double y = upperLeftY * -i;
                        for(int j = 0; j < 3; j++) {
                            double z = upperLeftZ * j;
                            for(int k = 0; k < 3; k++) {
                                double x = upperLeftX * k;
                                if((i == 2 || j != 1 || k != 1) && (i != 1 || (j + k) % 2 == 0)) {
                                    cubes.add(new SpongeCube(new Point3D(x, y, z), edgeIncrement, level - 1));
                                }
                            }
                        }
                    }
                }
            }
        }
        if(!intersectedCubes.isEmpty()) {
            double[] tValues = new double[intersectedCubes.size()];
            for(int i = 0; i < intersectedCubes.size(); i++) {
                tValues[i] = intersectedCubes.get(i).getT();
            }
            double minTValue = intersectedCubes.get(0).findMaxOrMin(tValues, 0);
            for(SpongeCube cube : intersectedCubes) {
                if(cube.getT() == minTValue) {
                    Point3D intersectionPoint = cube.findIntersectionPoint(startingPoint, ray);
                    return (new Intersection(intersectionPoint, cube.findIntersectedFaceNormal(intersectionPoint)));
                }
            }
        }
        return null;
    }



}