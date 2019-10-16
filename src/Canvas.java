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

  Point3D camera = new Point3D(1400, -1200, 1600); //set camera point - front: 0, 0, 1500 or angled: 1400, -1200, 1600
  Point3D light = new Point3D(1000, -900, 1000); //set light point - front: 0, 0, 1000 or angled: 1000, -900, 1000
  Dimension imageResolution = new Dimension(1400, 1000); //image size
  LightSource lightSource = new LightSource(light, 20); //create light source

  public Canvas() {
    setPreferredSize(imageResolution);
    setBackground(Color.white);
    rayTracer = new RayTracer();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    rayTracer.rayTrace(camera, 70, 90, imageResolution, lightSource);
    g2d.drawImage(rayTracer.image, 0, 0, this);
  }
}
