/*<pre>
   This is a simple example to show how to use the MISApplet
   to make your own pixel-by-pixel framebuffer.

   Two methods you can override from the MISApplet base
   class are initFrame and setPixel.
*/

public class MyMISApplet extends MISApplet {

//----- THESE TWO METHODS OVERRIDE METHODS IN THE BASE CLASS

    double t = 0;

    public void initFrame(double time) { // INITIALIZE ONE ANIMATION FRAME
/*
       REPLACE THIS CODE WITH YOUR OWN TIME-DEPENDENT COMPUTATIONS, IF ANY.

       t = 3 * time;
*/
    }
    public void setPixel(int x, int y, int rgb[]) { // SET ONE PIXEL'S COLOR
/*
       REPLACE THIS CODE WITH WHATEVER YOU'D LIKE, TO MAKE YOUR OWN COOL IMAGE.

       double fx = ((double)x - W/2) / W;
       double fy = ((double)y - H/2) / H;
       for (int j = 0 ; j < 3 ; j++)
          rgb[j] = (int)(128 + 128 *
             Math.sin(30 * fx + (3 - j) * ImprovedNoise.noise(8 * fx, 8 * fy, t)) *
	     Math.sin(30 * fy + (2 + j) * ImprovedNoise.noise(4 * fx, 4 * fy, t)));
*/
    }
}
