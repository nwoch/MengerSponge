/** Emma Blair and Nicole Woch Final Project - Computer Science 2019 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.lang.Math.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;

/**
 ** Canvas class creates a JPanel and then calls the rayTrace method in RayTracer to
 ** create the image and then draws in entire image onto the screen using paintComponent.
 ** This class also sets the light sources, camera position, and dimension of the image.
**/
public class Canvas extends JPanel {

  /** Instance of RayTracer class **/
  RayTracer rayTracer;

  /** Create scene points and image size **/
  Point3D camera = new Point3D(1000.0, -800.0, 1000.0); //set camera point
  Point3D lightBlue = new Point3D(1000.0, -700.0, 1000.0); //set light point
  Point3D lightRed = new Point3D(-1000.0, -1000.0, 1000.0); //set light point
  Dimension imageResolution = new Dimension(800, 1000); //image size
  ArrayList<LightSource> allLightSources = new ArrayList<LightSource>();

  /** Set size and background color of JPanel and instantiate variables **/
  public Canvas() {
    setPreferredSize(imageResolution);
    setBackground(Color.white);
    rayTracer = new RayTracer();
    allLightSources.add(new LightSource(lightBlue, 50)); //Add new light source to this list
    allLightSources.add(new LightSource(lightRed, 50)); //Add a new light source to this list
  }

  /** Method draws to the JPanel **/
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    /** Call rayTrace to create the image **/
    rayTracer.rayTrace(camera, 90, imageResolution, allLightSources);
    /** Draw the BufferedImage to the screen **/
    g2d.drawImage(rayTracer.image, 0, 0, this);
  }
}
