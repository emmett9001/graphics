import java.awt.*;

public class MatrixTest extends BufferedApplet{
    int w = 640;
    int h = 480;
    double[][] vertices = {
        { -1,-1,-1}, { 1,-1,-1}, {-1, -1,1}, {1, -1,1},
        { 0,1,0 },
    };
    int[][] edges = {
        {0,1},{2,3},{2,0},{3,1},
        {0,4},{1,4},{2,4},{3,4},
    };

    Matrix matrix = new Matrix();

    double[] point0 = new double[3];
    double[] point1 = new double[3];

    int[] a = new int[2];
    int[] b = new int[2];

    long startTime = System.currentTimeMillis();

    public void render(Graphics g){
        double ttime = (System.currentTimeMillis() - startTime) / 1000.0;

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);

        double[] anchor = {0.0, 0.0, 0.0};

        matrix.identity();
        matrix.translate(5*Math.sin(ttime), 0.0, -5.0);
        matrix.rotateY(Math.sin(ttime));
        matrix.scale(1+(Math.sin(ttime)), 1+Math.sin(ttime), 1.0);

        for (int e = 0 ; e < edges.length ; e++) {
            int i = edges[e][0];
            int j = edges[e][1];

            matrix.transform(vertices[i], point0);
            matrix.transform(vertices[j], point1);

            projectPoint(point0, a);
            projectPoint(point1, b);

            g.drawLine(a[0], a[1], b[0], b[1]);
        }
    }

    public void projectPoint(double[] xyz, int[] pxy) {
        double FL = 20.0;

        double x = xyz[0];
        double y = xyz[1];
        double z = xyz[2];

        pxy[0] = w / 2 + (int)(h * x / (FL - z));
        pxy[1] = h / 2 - (int)(h * y / (FL - z));
    }
}
