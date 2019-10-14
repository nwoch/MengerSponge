import java.awt.*;
import javax.swing.*;

public class Draw {

  public static void main(String args[]) {
    /** Create two JFrames **/
    JFrame canvasFrame = new JFrame();
    canvasFrame.setTitle("Ray Tracer"); //Label the window
    canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Quits the program is window is closed

    /** Creates classes to be added to the JFrames **/
    Canvas canvas = new Canvas();

    /** Set the layout for the CubeCanvas frame **/
    canvasFrame.setLayout(new BorderLayout());
    canvasFrame.add(canvas, BorderLayout.CENTER);

    canvasFrame.pack();
    canvasFrame.setVisible(true); //Allows frame to be seen
    canvasFrame.setResizable(false);//Cannot resize the frame

  }
}