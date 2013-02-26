import java.awt.*;
import java.util.Random;

public class GeometryTest extends BufferedApplet{
    int w = 800;
    int h = 700;

    Geometry torus = new Geometry(30, 10);
    Geometry sphere = new Geometry(20, 20);
    Matrix ball = new Matrix();
    Matrix planet = new Matrix();
    Matrix ring = new Matrix();

    double[] point0 = new double[3];
    double[] point1 = new double[3];

    int[] a = new int[2];
    int[] b = new int[2];

    long startTime = System.currentTimeMillis();
    boolean setupDone = false;

    public void render(Graphics g){
        if(!setupDone){
            setupDone = true;
            sphere.buildSphere();
            torus.buildTorus(15, .5);
        }
        double ttime = (System.currentTimeMillis() - startTime) / 1000.0;

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);

        ball.identity();
        ball.rotateY(7*ttime);
        ball.rotateZ(5*ttime);
        ball.translate(2*Math.sin(.1*ttime), 0, 0);
        renderGeometry(sphere, ball, g);

        planet.identity();
        planet.scale(.1, .1, .1);
        planet.parent(ball);
        planet.translate(2*Math.sin(ttime), 0, 2*Math.cos(ttime));
        planet.rotateX(1.2);
        planet.rotateY(1);
        renderGeometry(sphere, planet, g);

        for(int i = 1; i <= 4; i++){
            ring.identity();
            ring.scale(.1+(.03*i), .1+(.03*i), .1+(.03*i));
            ring.rotateX((8 - i) * .01 * ttime);
            ring.rotateY((8 - i) * .05 * ttime);
            ring.rotateZ((8 - i) *.02 * ttime);
            ring.parent(ball);
            renderGeometry(torus, ring, g);
        }
    }

    public void renderGeometry(Geometry geo, Matrix mat, Graphics g){
        int i, j;
        for (int e = 0 ; e < geo.numFaces(); e++) {
            int[] face = geo.getFace(e);
            for(int f = 0; f < face.length; f++){
                i = face[f];
                if(f == face.length - 1){
                    j = face[0];
                } else {
                    j = face[f+1];
                }

                mat.transform(geo.getVertex(i), point0);
                mat.transform(geo.getVertex(j), point1);

                projectPoint(point0, a);
                projectPoint(point1, b);

                g.drawLine(a[0], a[1], b[0], b[1]);
            }
        }
    }

    public void projectPoint(double[] xyz, int[] pxy) {
        double FL = 7.0;

        double x = xyz[0];
        double y = xyz[1];
        double z = xyz[2];

        pxy[0] = w / 2 + (int)(h * x / (FL - z));
        pxy[1] = h / 2 - (int)(h * y / (FL - z));
    }
}
