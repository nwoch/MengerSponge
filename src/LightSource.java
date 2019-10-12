import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicole Woch
 * Class which represents a point light source and contains methods
 * for calculating the intensity of the light source at different points on a surface.
 */
public class LightSource {

    private Point3D lightSourcePosition;
    private int ambientLight;

    /** Constructor which sets the class variables: the light source's position and the ambient light value. */
    public LightSource(Point3D lightSourcePosition, int ambientLight) {
        this.lightSourcePosition = lightSourcePosition;
        this.ambientLight = ambientLight;
    }

    /**
     * Calculates the intensity of the light source at each point on a surface
     * by summing ambient light, diffuse light, and specular reflection. Sum must fall between 0 and 255.
     */
    public List<Double> calcLightIntensity(List<Point3D> surfaceNormals) {
        List<Double> lightIntensities = new ArrayList<>();
        for(Point3D normal : surfaceNormals) {
            Point3D lightVector = lightSourcePosition.subtractVector(normal);
            double diffuseLight = this.calcDiffuseLight(normal, lightVector);
            double lightIntensity = ambientLight + diffuseLight + this.calcSpecularReflection(normal, lightVector, diffuseLight);
            lightIntensities.add(lightIntensity);
        }
        return lightIntensities;
    }

    /**
     * Calculates diffuse light from the light source at a point on a surface
     * using the normal vector at that point and the vector from the point to the light source.
     */
    private double calcDiffuseLight(Point3D normalVector, Point3D lightVector) {
        Point3D unitNormal = normalVector.normalize();
        Point3D unitLight = lightVector.normalize();
        double cos = unitNormal.dotProduct(unitLight);
        if(cos <= 0.0) { return 0.0; }
        return ((255.0 - ambientLight) * cos);
    }

    /**
     * Calculates specular reflection from the light source at a point on a surface
     * using the vector from the point to the eye and the reflected ray at that point.
     */
    private double calcSpecularReflection(Point3D normalVector, Point3D lightVector, double diffuseLight) {
        double projectionLength = lightVector.dotProduct(normalVector)/normalVector.magnitude();
        Point3D w = normalVector.normalize().scale(projectionLength);
        Point3D reflectedRay = w.scale(2).subtractVector(lightVector);
        Point3D eyeVector = new Point3D(0, 0, 1000).subtractVector(normalVector);
        double cos = reflectedRay.normalize().dotProduct(eyeVector.normalize());
        if(cos <= 0.0) { return 0.0; }
        return ((255.0 - ambientLight - diffuseLight) * Math.pow(cos, 1.5));
    }

    /** Getters & Setters */
    public void setLightSourcePosition(Point3D lightSourcePosition) { this.lightSourcePosition = lightSourcePosition; }

}
