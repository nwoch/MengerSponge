/** Emma Blair and Nicole Woch Final Project - Compter Graphics 2019 **/

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.ArrayList;

/**
 ** The RayTracer class is responsible for creating a BufferedImage to display in the Canvas class,
 ** creating a ViewPlane instance that creates the scene based on the current location of the camera,
 ** and then generating camera rays to shoot into the scene where in the intersections methods determine
 ** how the ray intersects with the Menger Sponge. Then the RayTracer find the diffuse and specular relfections
 ** by following a vector from the intersection to the light source. If this vector intersects with another point
 ** on the cube before it hits the light, then this point is in shadow and is set to the ambient light.
**/
public class RayTracer {

  /** Main image - Color pixels on this image **/
  BufferedImage image;

  /** Create an instance of MengerSponge to create the cube shape to put in the scene **/
  MengerSponge mengerSponge;

  /** The calculated color values for each light source **/
  int blueValue, redValue;

  /** Values for the lighting variables to calculate each pixel color **/
  double diffuseLightBlue, diffuseLightRed, specularReflectionLightBlue, specularReflectionLightRed;

  /** Constructor instantiates a new MengerSponge **/
  public RayTracer() {
      mengerSponge = new MengerSponge(new Point3D(-500, -500, -500.0), 1000.0, 3);
  }

  /** Render method takes in the current image being used, an x and y location, and color values for the red and blue light **/
  public void render(BufferedImage scene, int x, int y, int redValue, int blueValue) {
    scene.setRGB(x, y, new Color(redValue, 0, blueValue).getRGB());
  }

  /**
   ** rayTrace conducts the majority of the work that following that follows the Ray Tracing algorithm:
   ** Point3D cameraPoint - the current position (point) of the camera
   ** double horizontalFOVAngle - the field of view angle for creating the view plane (included in calculation of the width of the plane)
   ** Dimension imgResolution - the width and height of the BufferedImage (allows for iterating pixel by pixel)
   ** ArrayList<LightSource> lightsource - an ArrayList of the lights sources (points) in the scene
   ** This method loops through every pixel on the BufferedImage, generates camera rays, shoots them into the scene, checks for intersection
   ** with the sponge, from each intersection calculates vector to the light sources and if this ray intersects. If ths pixel in not in shadow,
   ** then calculates the light using the elementary light model with the ray tracing algorithm. Then it calls render so actually set the pixel color.
  **/
  public void rayTrace(Point3D cameraPosition, double horizontalFOVAngle, Dimension imgResolution, ArrayList<LightSource> lightSource) {
    /** Create a new view plane with the cameraPosition, the center of the object (0,0,0), a vector corresponding to normal y axis (0,1,0),
     ** the horizontal field of view angle, and the dimensions of the BufferedImage
    **/
    ViewPlane plane = new ViewPlane(cameraPosition,
                                    new Point3D(0.0, 0.0, 0.0),
                                    new Point3D(0.0, 1.0, 0.0),
                                    Math.toRadians(horizontalFOVAngle),
                                    imgResolution.getWidth()/imgResolution.getHeight());
    /** Create BufferedImage set at the dimensions passed into the rayTrace method **/
    image = new BufferedImage((int)imgResolution.getWidth(), (int)imgResolution.getHeight(),  BufferedImage.TYPE_INT_RGB);
    /** Loop through each y and x coordinates, going row by row **/
    for(int y = 0; y < imgResolution.getHeight(); y++) {
      for(int x = 0; x < imgResolution.getWidth(); x++) {
        /** Get the correct coordinates of the center of current x,y pixel using the ViewPlane **/
        Point3D fieldOfViewCoordinate = plane.getNewCoorindates(x/imgResolution.getWidth(), y/imgResolution.getHeight());
        /** Generate a ray from the camera position to the pixel coordinates generated from the ViewPlane **/
        Point3D cameraRay = fieldOfViewCoordinate.subtractVector(cameraPosition).normalize();
        /** Check if this create ray intersects with the MengerSponge **/
        Intersection cameraIntersection = mengerSponge.intersectWithSponge(cameraPosition, cameraRay);
        /** Check is the ray hits the shape **/
        if(cameraIntersection != null) { //If the ray does intersect with the shape, calculate light rays
          /** Create a ray from current point of intersection and the light source positions **/
          Point3D lightRayBlue = lightSource.get(0).getLightSourcePosition().subtractVector(cameraIntersection.getIntersectionPoint());
          Point3D lightRayRed = lightSource.get(1).getLightSourcePosition().subtractVector(cameraIntersection.getIntersectionPoint());
          /** Move slightly away from the surface of the face to avoid shadow acne **/
          double EPSILON = 0.0001;
          /** Chech if each light vector intersects with anywhere else on the sponge **/
          Intersection lightIntersectionBlue = mengerSponge.intersectWithSponge(cameraIntersection.getIntersectionPoint().addVector(cameraIntersection.getNormalVector().scale(EPSILON)), lightRayBlue);
          Intersection lightIntersectionRed = mengerSponge.intersectWithSponge(cameraIntersection.getIntersectionPoint().addVector(cameraIntersection.getNormalVector().scale(EPSILON)), lightRayRed);
          if(lightIntersectionBlue == null && lightIntersectionRed == null) { // If there is no shape blocking the light vector, calculate the light intensity at that point on the shape
            /** Calculate diffuse light using methods in LightSource class **/
            diffuseLightBlue = lightSource.get(0).calcDiffuseLight(cameraIntersection.getNormalVector(), lightRayBlue);
            diffuseLightRed = lightSource.get(1).calcDiffuseLight(cameraIntersection.getNormalVector(), lightRayRed);
            /** Calculate Speculate Reflection using methods in LightSource class **/
            specularReflectionLightBlue = lightSource.get(0).calcSpecularReflection(cameraIntersection.getNormalVector(), lightRayBlue, diffuseLightBlue);
            specularReflectionLightRed = lightSource.get(1).calcSpecularReflection(cameraIntersection.getNormalVector(), lightRayRed, diffuseLightRed);
            /** Get the Red and Blue color values **/
            blueValue = (int)(lightSource.get(0).getAmbientLight() + diffuseLightBlue + specularReflectionLightBlue);
            redValue = (int)(lightSource.get(1).getAmbientLight() + diffuseLightRed + specularReflectionLightRed);
            /** Call render method to set the pixel color in the BufferedImage **/
            render(image, x, y, redValue, blueValue);
          } else if (lightIntersectionBlue == null && lightIntersectionRed != null){
            /** Calculate diffuse light using methods in LightSource class **/
            diffuseLightBlue = lightSource.get(0).calcDiffuseLight(cameraIntersection.getNormalVector(), lightRayBlue);
            /** Calculate Speculate Reflection using methods in LightSource class **/
            specularReflectionLightBlue = lightSource.get(0).calcSpecularReflection(cameraIntersection.getNormalVector(), lightRayBlue, diffuseLightBlue);
            /** Calculate Blue light value **/
            blueValue = (int)(lightSource.get(0).getAmbientLight() + diffuseLightBlue + specularReflectionLightBlue);
            /** Call render method to set the pixel color in the BufferedImage **/
            render(image, x, y, 0, blueValue);
          } else if (lightIntersectionBlue != null && lightIntersectionRed == null) {
            /** Calculate diffuse light using methods in LightSource class **/
            diffuseLightRed = lightSource.get(1).calcDiffuseLight(cameraIntersection.getNormalVector(), lightRayRed);
            /** Calculate Speculate Reflection using methods in LightSource class **/
            specularReflectionLightRed = lightSource.get(1).calcSpecularReflection(cameraIntersection.getNormalVector(), lightRayRed, diffuseLightRed);
            /** Calculate Red light value **/
            redValue = (int)(lightSource.get(1).getAmbientLight() + diffuseLightRed + specularReflectionLightRed);
            /** Call render method to set the pixel color in the BufferedImage **/
            render(image, x, y, redValue, 0);
          } else {
            /** Set pixel color to the Ambient Light of Red and Blue colors **/
            blueValue = lightSource.get(0).getAmbientLight();
            redValue = lightSource.get(1).getAmbientLight();
            /** Call render method to set the pixel color in the BufferedImage **/
            render(image, x, y, redValue, blueValue);
          }
        } else { //If the camera ray does not intersect with the sponge
          /** Set pixel color to background color (black) **/
          /** Call render method to set the pixel color in the BufferedImage **/
          render(image, x, y, 0, 0);
        }

      }
    }
  }

}
