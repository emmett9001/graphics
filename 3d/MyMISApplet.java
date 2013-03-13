public class MyMISApplet extends MISApplet {
    double t = 0;
    Geometry cube = new Geometry(0, 0);
    Renderer rnd = new Renderer();
    int[][][] pixels;

    public void initialize(){
        pixels = new int[H][W][3];
        cube.buildCube();
    }

    public void initFrame(double time) {
        t = 5 * time;

        cube.getMatrix().identity();
        cube.getMatrix().rotateY(time);

        for(int i = 0; i < H; i++){
            for(int j = 0; j < W; j++){
                pixels[i][j][0] = 0;
                pixels[i][j][1] = 0;
                pixels[i][j][2] = 0;
            }
        }

        rnd.renderScanConvertedGeometry(cube, cube.getMatrix(), pixels);
    }

    public void setPixel(int x, int y, int rgb[]) {
        for (int j = 0 ; j < 3 ; j++){
            rgb[j] = pixels[y][x][j];
        }
    }
}
