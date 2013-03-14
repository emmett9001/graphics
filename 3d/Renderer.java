import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

public class Renderer{
    int w, h;
    Graphics g;
    World world;
    int a[], b[];
    double point0[], point1[];
    int[][][] tmpTriangles;
    int[][][] tmpTrapezoids;
    int[][] tmpProjectedFace;
    int[] D;

    public Renderer(){
        this(1280, 700, (Graphics)null);
    }

    public Renderer(int width, int height, Graphics g, World world){
        this(width, height, g);
        this.world = world;
    }

    public Renderer(int width, int height, Graphics g){
        this.w = width;
        this.h = height;
        this.g = g;

        a = new int[2];
        b = new int[2];

        point0 = new double[3];
        point1 = new double[3];

        tmpTriangles = new int[2][3][2];
        tmpTrapezoids = new int[4][4][2];
        tmpProjectedFace = new int[4][2];
        D = new int[2];
    }

    public void renderGeometry(Geometry geo, Matrix mat){
        renderGeometry(geo, mat, this.g, Color.BLACK);
    }

    public void renderGeometry(Geometry geo, Matrix mat, Color col){
        renderGeometry(geo, mat, this.g, col);
    }

    public void renderGeometry(Geometry geo){
        renderGeometry(geo, geo.getMatrix(), this.g, Color.black);
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

    private void trianglesFromFace(){
        tmpTriangles[0][0] = tmpProjectedFace[0].clone();
        tmpTriangles[0][1] = tmpProjectedFace[1].clone();
        tmpTriangles[0][2] = tmpProjectedFace[2].clone();

        tmpTriangles[1][0] = tmpProjectedFace[2].clone();
        tmpTriangles[1][1] = tmpProjectedFace[3].clone();
        tmpTriangles[1][2] = tmpProjectedFace[1].clone();
    }

    private void sortTriangleVertices(){
        for(int i = 0; i < 2; i++){
            Arrays.sort(tmpTriangles[i], new Comparator<int[]>(){
                @Override
                public int compare(final int[] entry1, final int[] entry2){
                    final int y1 = entry1[1];
                    final int y2 = entry2[1];
                    if(y1 > y2) return 1;
                    if(y1 < y2) return -1;
                    return 0;
                }
            });
        }
    }

    private void trapezoidsFromTriangles(){
        for(int i = 0; i < 2; i++){
            int[] A = tmpTriangles[i][0].clone();
            int[] B = tmpTriangles[i][1].clone();
            int[] C = tmpTriangles[i][2].clone();
            D[1] = A[1];
            double t = (double)(B[1] - A[1]) / (double)(C[1] - A[1]);
            D[0] = (int)((double)A[0] + (t * (C[0] - A[0])));

            tmpTrapezoids[2*i][0] = A.clone();
            tmpTrapezoids[2*i][1] = A.clone();
            tmpTrapezoids[2*i][2] = B.clone();
            tmpTrapezoids[2*i][3] = D.clone();

            tmpTrapezoids[(2*i+1)][0] = B.clone();
            tmpTrapezoids[(2*i+1)][1] = D.clone();
            tmpTrapezoids[(2*i+1)][2] = C.clone();
            tmpTrapezoids[(2*i+1)][3] = C.clone();
        }
    }

    public void renderScanConvertedGeometry(Geometry geo, Matrix mat, int[][][] pixels){
        for (int e = 0 ; e < geo.numFaces(); e++) {
            int[] face = geo.getFace(e);
            for(int f = 0; f < face.length; f++){
                mat.transform(geo.getVertex(face[f]), point0);
                projectPoint(point0, a);
                tmpProjectedFace[f] = a.clone();
            }

            trianglesFromFace();
            sortTriangleVertices();
            trapezoidsFromTriangles();

            for(int i = 0; i < 4; i++){
                int[] lt, rt, lb, rb;
                double yT, yB, xLT, xRT, xLB, xRB;
                /*
                for(int j = 0; j < 4; j++){
                    for(int k = 0; k < 2; k++){
                        System.out.println("trapezoids " + i + "- " + j + ": " + tmpTrapezoids[i][j][k]);
                    }
                }
                */
                lt = tmpTrapezoids[i][0];
                rt = tmpTrapezoids[i][1];
                lb = tmpTrapezoids[i][2];
                rb = tmpTrapezoids[i][3];

                yT = lt[1];
                yB = lb[1];
                xLT = lt[0];
                xRT = rt[0];
                xLB = lb[0];
                xRB = rb[0];
                for(int scanline = (int)yT; scanline <= yB; scanline++){
                    double t = (scanline - yT) / (yB - yT);
                    double xL = xLT + t * (xLB - xLT);
                    double xR = xRT + t * (xRB - xRT);
                    if(xL > xR){
                        double tmp = xL;
                        xL = xR;
                        xR = tmp;
                    }
                    for(int pix = (int)xL; pix <= xR; pix++){
                        try{
                            pixels[scanline][pix][0] = 255;
                            pixels[scanline][pix][1] = 255;
                            pixels[scanline][pix][2] = 255;
                        } catch(ArrayIndexOutOfBoundsException f){}
                    }
                }
            }
        }
    }

    public void render(){
        _render(world.getRoot(), world.getRoot());
    }

    private void _render(Geometry node, Geometry parent){
        node.getMatrix().rightMultiply(parent.getMatrix());
        if(node.numFaces() > 0 && node.numVertices() > 0){
            renderGeometry(node);
        }
        if(node.getNumChildren() != 0){
            for(int i = 0; i < node.getNumChildren(); i++){
                _render(node.getChild(i), node);
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
