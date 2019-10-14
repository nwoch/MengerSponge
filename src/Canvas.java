import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.lang.Math.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Canvas extends JPanel {

  RayTracer rayTracer;

  Point3D camera = new Point3D(0.0, 0.0, 0.0); //set camera point
  Point3D light = new Point3D(0.0, 0.0, 500.0); //set light point
  Dimension imageResolution = new Dimension(500, 500); //image size
  LightSource lightSource = new LightSource(light, 20); //create light source

  public Canvas() {
    setPreferredSize(new Dimension(600,600));
    setBackground(Color.white);
    rayTracer = new RayTracer();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    rayTracer.rayTrace(camera, 90, 90, imageResolution, lightSource);
    g2d.drawImage(rayTracer.image, 50, 50, this);
  }
}
