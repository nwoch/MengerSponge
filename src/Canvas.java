/** Emma Blair and Nicole Woch Final Project - Computer Science 2019 **/

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 ** Canvas class creates a JPanel and then calls the rayTrace method in RayTracer to
 ** create the image and then draws in entire image onto the screen using paintComponent.
 ** This class also sets the light sources, camera position, and dimension of the image.
**/
public class Canvas extends JPanel {

  private RayTracer rayTracer;
  private Point3D camera;
  private Point3D lightBlue;
  private Point3D lightRed;
  private Dimension imageResolution;
  private ArrayList<LightSource> lightSources;

  /** Set size and background color of JPanel and instantiate variables **/
  public Canvas() {
    this.rayTracer = new RayTracer();

    /** Create scene points and image size **/
    this.camera = new Point3D(1400.0, -1200.0, 1600.0); //set camera point - front: 0, 0, 1500 or angled: 1400, -1200, 1600
    this.lightBlue = new Point3D(1000.0, -700.0, 1000.0); //set light point - front: 0, 0, 1000 or angled: 1000, -900, 1000
    this.lightRed = new Point3D(-1000.0, -1000.0, 1000.0); //set light point - front: 0, 0, 1000 or angled: -1000, -900, 1000
    this.imageResolution = new Dimension(1400, 1000); //image size
    this.lightSources = new ArrayList<>();
    this.lightSources.add(new LightSource(lightBlue, 50)); //Add new light source to this list
    this.lightSources.add(new LightSource(lightRed, 50)); //Add a new light source to this list

    setPreferredSize(imageResolution);
    setBackground(Color.white);
  }

  /** Method draws to the JPanel **/
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    /** Call rayTrace to create the image **/
    rayTracer.rayTrace(camera, imageResolution, lightSources);
    /** Draw the BufferedImage to the screen **/
    g2d.drawImage(rayTracer.getImage(), 0, 0, this);
  }
}
