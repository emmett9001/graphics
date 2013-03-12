import java.awt.*;

public class Renderer{
    int w, h;
    Graphics g;
    World world;
    int a[], b[];
    int[][] triangleVerts1, triangleVerts2;
    int[][] trapVerts1, trapVerts2;
    int[] swapVertex;
    double point0[], point1[];

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

        triangleVerts1 = new int[3][2];
        triangleVerts2 = new int[3][2];

        trapVerts1 = new int[3][2];
        trapVerts2 = new int[3][2];

        swapVertex = new int[2];
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

    public void renderScanConvertedGeometry(Geometry geo, Matrix mat){
        int i;
        for (int e = 0 ; e < geo.numFaces(); e++) {
            int[] face = geo.getFace(e);
            for(int f = 0; f < face.length; f++){
                i = face[f];
                mat.transform(geo.getVertex(i), point0);
                projectPoint(point0, a);

                if(f == 0 || f == 2){
                    triangleVerts1[f] = a;
                    triangleVerts2[f] = a;
                } else if(f == 1){
                    triangleVerts1[1] = a;
                } else if(f == 3){
                    triangleVerts2[1] = a;
                }
            }

            // sort triangle vertices
            if(triangleVerts1[0][1] > triangleVerts1[1][1]){
                swap(triangleVerts1[0], triangleVerts1[1]);
            }
            if(triangleVerts1[1][1] > triangleVerts1[2][1]){
                swap(triangleVerts1[2], triangleVerts1[1]);
            }
            // split first triangle into trapezoids

            System.out.println();
            for(int k = 0; k < 3; k++){
                trapVerts1[k] = triangleVerts1[k];
            }
            // render the two trapezoids

            // split second triangle into trapezoids
            // render the two trapezoids
        }
    }

    private void swap(int[] a, int[] b){
        swapVertex = a;
        a = b;
        b = swapVertex;
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
