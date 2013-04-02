import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

public class Renderer{
    int w, h;
    Graphics g;
    World world;
    int a[], b[];
    double aNormals[];
    double point0[], point1[];
    int[][][] tmpTriangles;
    int[][][] tmpTrapezoids;
    int[][] tmpProjectedFace;
    double[] D;
    double FL;

    public Renderer(){
        this(1280, 700, (Graphics)null);
    }

    public Renderer(int width, int height, Graphics g, World world){
        this(width, height, g);
        this.world = world;
    }

    public Renderer(int width, int height, World world){
        this(width, height, (Graphics)null);
        this.world = world;
    }

    public Renderer(int width, int height, Graphics g){
        this.w = width;
        this.h = height;
        this.g = g;

        a = new int[6];
        b = new int[6];

        point0 = new double[6];
        point1 = new double[6];

        tmpTriangles = new int[2][3][6];
        tmpTrapezoids = new int[4][4][6];
        tmpProjectedFace = new int[4][6];
        D = new double[6];

        FL = 7.0;
    }

    public double getFocalLength(){
        return FL;
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

        tmpTriangles[1][0] = tmpProjectedFace[0].clone();
        tmpTriangles[1][1] = tmpProjectedFace[2].clone();
        tmpTriangles[1][2] = tmpProjectedFace[3].clone();
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
            double t = (double)(B[1] - A[1]) / (double)(C[1] - A[1]);
            D[0] = (double)A[0] + (t * (double)(C[0] - A[0]));
            D[1] = A[1];
            D[2] = LERP(t, A[2], C[2]);
            D[3] = LERP(t, A[3], C[3]);
            D[4] = LERP(t, A[4], C[4]);
            D[5] = LERP(t, A[5], C[5]);

            tmpTrapezoids[2*i][0] = A.clone();
            tmpTrapezoids[2*i][1] = A.clone();
            tmpTrapezoids[2*i][2] = B.clone();
            tmpTrapezoids[2*i][3][0] = (int)D[0];
            tmpTrapezoids[2*i][3][1] = (int)D[1];
            tmpTrapezoids[2*i][3][2] = (int)D[2];
            tmpTrapezoids[2*i][3][3] = (int)D[3];
            tmpTrapezoids[2*i][3][4] = (int)D[4];
            tmpTrapezoids[2*i][3][5] = (int)D[5];

            tmpTrapezoids[(2*i)+1][0] = B.clone();
            tmpTrapezoids[(2*i)+1][1][0] = (int)D[0];
            tmpTrapezoids[(2*i)+1][1][1] = (int)D[1];
            tmpTrapezoids[(2*i)+1][1][2] = (int)D[2];
            tmpTrapezoids[(2*i)+1][1][3] = (int)D[3];
            tmpTrapezoids[(2*i)+1][1][4] = (int)D[4];
            tmpTrapezoids[(2*i)+1][1][5] = (int)D[5];
            tmpTrapezoids[(2*i)+1][2] = C.clone();
            tmpTrapezoids[(2*i)+1][3] = C.clone();
        }
    }

    private void scanconvertTrapezoid(int i, int[][][] pixels, double[][] zbuf){
        int[] lt, rt, lb, rb;
        double yT, yB, xLT, xRT, xLB, xRB;
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

            double pzXL = LERP(t, lt[2], lb[2]);
            double nxXL = LERP(t, lt[3], lb[3]);
            double nyXL = LERP(t, lt[4], lb[4]);
            double nzXL = LERP(t, lt[5], lb[5]);

            double pzXR = LERP(t, rt[2], rb[2]);
            double nxXR = LERP(t, rt[3], rb[3]);
            double nyXR = LERP(t, rt[4], rb[4]);
            double nzXR = LERP(t, rt[5], rb[5]);

            double xL = xLT + t * (xLB - xLT);
            double xR = xRT + t * (xRB - xRT);
            if(xL > xR){
                double tmp = xL;
                xL = xR;
                xR = tmp;
            }
            for(int pix = (int)xL; pix <= xR; pix++){
                double xPct = pix / (xR - xL);
                double pZ = LERP(t, pzXL, pzXR);
                try{
                    if(pZ > zbuf[scanline][pix]){
                        zbuf[scanline][pix] = pZ;
                        pixels[scanline][pix][0] = (int)LERP(t, nxXL, nxXR);
                        pixels[scanline][pix][1] = (int)LERP(t, nyXL, nyXR);
                        pixels[scanline][pix][2] = (int)LERP(t, nzXL, nzXR);
                    }
                } catch(ArrayIndexOutOfBoundsException f){}
            }
        }
    }

    private boolean frontFacingFace(){
        // return true if the triangles in tmpTriangles are facing front
        // false otherwise
        double sum = 0;
        for(int i = 0; i < 3; i++){
            int[] A = tmpTriangles[0][i].clone();
            int incremented = i+1;
            if(incremented > 2){
                incremented = 0;
            }
            int[] B = tmpTriangles[0][incremented].clone();

            sum += ((double)((double)(A[0] - B[0])*(double)(A[1] + B[1]))) / 2.0;
        }
        if(sum > 0){
            return true;
        }
        return false;
    }

    public void renderScanConvertedGeometry(Geometry geo, Matrix mat, int[][][] pixels, double[][] zbuf){
        for (int e = 0 ; e < geo.numFaces(); e++) {
            int[] face = geo.getFace(e);
            for(int f = 0; f < face.length; f++){
                mat.transform(geo.getVertex(face[f]), point0);
                projectPoint(point0, a);
                tmpProjectedFace[f] = a.clone();
            }

            trianglesFromFace();

            if(frontFacingFace()){
                sortTriangleVertices();
                trapezoidsFromTriangles();
                for(int i = 0; i < 4; i++){
                    scanconvertTrapezoid(i, pixels, zbuf);
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

    public void renderZ(int[][][] pix, double[][] zbuf){
        _renderZ(world.getRoot(), world.getRoot(), pix, zbuf);
    }

    private void _renderZ(Geometry node, Geometry parent, int[][][] pix, double[][] zbuf){
        node.getMatrix().rightMultiply(parent.getMatrix());
        if(node.numFaces() > 0 && node.numVertices() > 0){
            renderScanConvertedGeometry(node, node.getMatrix(), pix, zbuf);
        }
        if(node.getNumChildren() != 0){
            for(int i = 0; i < node.getNumChildren(); i++){
                _renderZ(node.getChild(i), node, pix, zbuf);
            }
        }
    }

    private void projectPoint(double[] xyz, int[] pxy) {
        double x = xyz[0];
        double y = xyz[1];
        double z = xyz[2];

        pxy[0] = w / 2 + (int)(h * x / (FL - z));
        pxy[1] = h / 2 - (int)(h * y / (FL - z));
        pxy[2] = (int)(FL * z) / (int)(FL - z);
        pxy[3] = (int)xyz[3];
        pxy[4] = (int)xyz[4];
        pxy[5] = (int)xyz[5];
    }

    private int map(double normal){
        return (int)((normal + 1.0) * 255 / 2.0);
    }

    private double LERP(double percent, double a, double b){
        return percent * (b - a) + a;
    }

}
