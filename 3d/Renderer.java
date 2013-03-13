import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

public class Renderer{
    int w, h;
    Graphics g;
    World world;
    int a[], b[];
    double point0[], point1[];
    int[][][][] triangles;

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

        triangles = new int[5000][2][4][2];
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

    public void renderScanConvertedGeometry(Geometry geo, Matrix mat, int[][][] pixels){
        for (int e = 0 ; e < geo.numFaces(); e++) {
            int[] face = geo.getFace(e);
            for(int f = 0; f < face.length; f++){
                mat.transform(geo.getVertex(face[f]), point0);
                projectPoint(point0, a);

                // triangles[totalnumber][slicedpair][3vertices+D][2coordinates]
                // create two triangles (triangles[e][0] and triangles[e][1])
                // by splitting the face
                if(f == 0 || f == 2){
                    triangles[e][0][f] = a.clone();
                    triangles[e][1][f] = a.clone();
                } else if(f == 1){
                    triangles[e][0][1] = a.clone();
                } else if(f == 3){
                    triangles[e][1][1] = a.clone();
                }
            }

            // now that we have two triangles, sort the vertices of each by Y component
            for(int i = 0; i < 2; i++){
                Arrays.sort(triangles[e][i], new Comparator<int[]>(){
                    @Override
                    public int compare(final int[] entry1, final int[] entry2){
                        final int y1 = entry1[1];
                        final int y2 = entry2[1];
                        if(y1 > y2) return 1;
                        if(y1 < y2) return -1;
                        return 0;
                    }
                });
                // A -triangles[e][i][0]
                // B -triangles[e][i][1]
                // C -triangles[e][i][2]
                // D -triangles[e][i][3]
                // calculate the split point (D) for the two trapezoids
                triangles[e][i][3][1] = triangles[e][i][1][1];  // same scan line as B
                int t = (int)((triangles[e][i][1][1] - triangles[e][i][0][1]) / (double)(triangles[e][i][2][1] - triangles[e][i][0][1]));
                triangles[e][i][3][0] = (int)(triangles[e][i][0][0] + (double)t * (double)(triangles[e][i][2][0] - triangles[e][i][0][0]));
            }
        }

        for(int e = 0; e < triangles.length; e++){
            for(int tri = 0; tri < 2; tri++){
                int yT, yB, xLT, xRT, xLB, xRB;
                yT = triangles[e][tri][1][1];
                yB = triangles[e][tri][2][1];
                xLT = triangles[e][tri][1][0];
                xRT = triangles[e][tri][3][0];
                xLB = triangles[e][tri][2][0];
                xRB = triangles[e][tri][2][0];
                for(int scanline = yT; scanline < yB; scanline++){
                    int t = (int)((scanline - yT) / (double)(yB - yT));
                    int xL = xLT + t * (xLB - xLT);
                    int xR = xRT + t * (xRB - xRT);
                    for(int pix = xL; pix < xR; pix++){
                        pixels[scanline][pix][0] = 255;
                        pixels[scanline][pix][1] = 255;
                        pixels[scanline][pix][2] = 255;
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
