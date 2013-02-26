import java.awt.*;

public class GeometryTest extends BufferedApplet{
    int w = 640;
    int h = 480;

    Geometry torus = new Geometry(20, 20);
    Geometry sphere = new Geometry(20, 20);
    Matrix balls = new Matrix();

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
            torus.buildTorus();
        }
        double ttime = (System.currentTimeMillis() - startTime) / 1000.0;

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);

        double[] anchor = {0.0, 0.0, 0.0};

        balls.identity();
        balls.rotateY(ttime);
        balls.translate(0, 0, -4-Math.sin(5*ttime));
        balls.scale(.5, .2*(2+Math.sin(ttime)), .5);
        renderGeometry(torus, balls, g);
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
