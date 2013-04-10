public class MyMISApplet extends MISApplet {
    double t = 0;
    Geometry cube = new Geometry(20, 20);
    Renderer rnd = new Renderer();
    int[][][] pixels;
    double[][] zbuffer;

    public void initialize(){
        rnd.colorNormals(true);
        pixels = new int[H][W][3];
        zbuffer = new double[H][W];
        cube.buildTorus();
    }

    public void initFrame(double time) {
        t = 5 * time;

        cube.getMatrix().identity();
        cube.getMatrix().rotateY(time);
        cube.getMatrix().rotateX(time);
        cube.getMatrix().translate(0, 0, -3);

        for(int i = 0; i < H; i++){
            for(int j = 0; j < W; j++){
                pixels[i][j][0] = 0;
                pixels[i][j][1] = 0;
                pixels[i][j][2] = 0;
                zbuffer[i][j] = -1*rnd.getFocalLength();
            }
        }

        rnd.renderScanConvertedGeometry(cube, cube.getMatrix(), pixels, zbuffer);
    }

    public void setPixel(int x, int y, int rgb[]) {
        for (int j = 0 ; j < 3 ; j++){
            rgb[j] = pixels[y][x][j];
        }
    }
}
