import java.util.LinkedList;
import java.util.Queue;

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

    public Point3D intersectWithSponge(Point3D startingPoint, Point3D ray) {
        //make increments generic for any direction
        while(!cubes.isEmpty()) {
            SpongeCube cube = cubes.remove();
            if(cube.intersectsSpongeCube(startingPoint, ray)) {
                if(cube.getLevel() == 0) { return cube.findIntersectionPoint(startingPoint, ray); }
                else {
                    cubes.clear();
                    double edgeIncrement = cube.getEdgeLength()/3.0;
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
                                if((i != 0 || j != 1 || k != 1) && (i != 1 || (j + k) % 2 == 0)) {
                                    cubes.add(new SpongeCube(new Point3D(x, y, z), edgeIncrement, level - 1));
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }



}
