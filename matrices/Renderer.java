import java.awt.*;

public class Renderer{
    int w, h;
    Graphics g;
    int a[], b[];
    double point0[], point1[];

    public Renderer(){
        this(1280, 700, (Graphics)null);
    }

    public Renderer(int width, int height, Graphics g){
        this.w = width;
        this.h = height;
        this.g = g;

        a = new int[2];
        b = new int[2];

        point0 = new double[3];
        point1 = new double[3];
    }

    public void renderGeometry(Geometry geo, Matrix mat){
        renderGeometry(geo, mat, this.g, Color.BLACK);
    }

    public void renderGeometry(Geometry geo, Matrix mat, Color col){
        renderGeometry(geo, mat, this.g, col);
    }

    public void renderGeometry(Geometry geo, Matrix mat, Graphics g, Color col){
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

                g.setColor(col);
                g.drawLine(a[0], a[1], b[0], b[1]);
            }
        }
    }

    private void projectPoint(double[] xyz, int[] pxy) {
        double FL = 7.0;

        double x = xyz[0];
        double y = xyz[1];
        double z = xyz[2];

        pxy[0] = w / 2 + (int)(h * x / (FL - z));
        pxy[1] = h / 2 - (int)(h * y / (FL - z));
    }
}
