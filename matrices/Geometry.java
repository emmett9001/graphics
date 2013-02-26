public class Geometry{
    private double vertices[][];
    private int faces[][];
    private int M, N;
    Matrix matrix = new Matrix();

    public Geometry(int M, int N){
        this.M = M;
        this.N = N;
    }

    abstract class ParametricShape{
        abstract double x(double u, double v);
        abstract double y(double u, double v);
        abstract double z(double u, double v);
    }

    class Sphere extends ParametricShape{
        double x(double u, double v){
            return Math.cos(2*Math.PI*u) * Math.cos(Math.PI*(v-.5));
        }
        double y(double u, double v){
            return Math.sin(2*Math.PI*u) * Math.cos(Math.PI*(v-.5));
        }
        double z(double u, double v){
            return Math.sin(Math.PI*(v-.5));
        }
    }

    class Torus extends ParametricShape{
        double R, r;

        public Torus(){
            this.R = 3;
            this.r = 1;
        }
        public Torus(double R, double r){
            this.R = R;
            this.r = r;
        }
        double x(double u, double v){
            return Math.cos(2*Math.PI*u) * (R + Math.cos(2*Math.PI*v));
        }
        double y(double u, double v){
            return Math.sin(2*Math.PI*u) * (R + Math.cos(2*Math.PI*v));
        }
        double z(double u, double v){
            return Math.sin(2*Math.PI*v);
        }
    }

    class Cylinder extends ParametricShape{
        double x(double u, double v){
            return Math.cos(2*Math.PI*u);
        }
        double y(double u, double v){
            return Math.sin(2*Math.PI*u);
        }
        double z(double u, double v){
            return 2 * v - 1;
        }
    }

    private int index(int M, int m, int n){
        return m + (M + 1) * n;
    }

    public int[] getFace(int i){
        return faces[i];
    }

    public double[] getVertex(int i){
        return vertices[i];
    }

    public int numFaces(){
        return faces.length;
    }

    public int numVertices(){
        return vertices.length;
    }

    public void buildSphere(){
        buildParametricMesh(new Sphere());
    }

    public void buildTorus(){
        buildParametricMesh(new Torus());
    }

    public void buildTorus(double R, double r){
        buildParametricMesh(new Torus(R,r));
    }

    public void buildCylinder(){
        buildParametricMesh(new Cylinder());
    }

    public void buildCube(){
        vertices = new double[24][3];
        faces = new int[6][4];
        double[][] v = {
            {-1, -1, -1}, {1, -1, -1}, {1, 1, -1}, {-1, 1, -1},  // front
            {-1, 1, -1},  {1, 1, -1},  {1, 1, 1},  {-1, 1, 1},  // top
            {-1, -1, -1}, {1, -1, -1}, {1, -1, 1}, {-1, -1, 1},  // bottom
            {1, -1, -1},  {1, -1, 1},  {1, 1, 1},  {1, 1, -1},  // right
            {-1, -1, -1}, {-1, -1, 1}, {-1, 1, 1}, {-1, 1, -1},  // left
            {-1, -1, 1},  {1, -1, 1},  {1, 1, 1},  {-1, 1, 1},  // back
        };
        int[][] f = {
            {0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11},
            {12, 13, 14, 15}, {16, 17, 18, 19}, {20, 21, 22, 23}
        };
        copyShape(v, f);
    }

    private void buildParametricMesh(ParametricShape shape){
        vertices = new double[(M+2)*(N+2)][3];
        faces = new int[(M+2)*(N+2)][4];

        double u = 0, v = 0;
        for(int m = 0; m <= M; m++){
            for(int n = 0; n <= N; n++){
                vertices[index(M, m, n)][0] = shape.x(u,v);
                vertices[index(M, m, n)][1] = shape.y(u,v);
                vertices[index(M, m, n)][2] = shape.z(u,v);
                v += 1.0/N;

                if(m != M && n != N){
                    faces[m+M*n][0] = index(M, m, n);
                    faces[m+M*n][1] = index(M, m+1, n);
                    faces[m+M*n][2] = index(M, m+1, n+1);
                    faces[m+M*n][3] = index(M, m, n+1);
                }
            }
            u += 1.0/M;
            v = 0;
        }
    }

    private void copyShape(double[][] v, int[][] f){
        for(int i = 0; i < v.length; i++){
            vertices[i] = v[i].clone();
        }
        for(int i = 0; i < f.length; i++){
            faces[i] = f[i].clone();
        }
    }
}
