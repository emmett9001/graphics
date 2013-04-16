public class MyMISApplet extends MISApplet {
    double t = 0;
    RaytraceRenderer rnd = new RaytraceRenderer();
    int[][][] pixels;

    public void initialize(){
        rnd.addSphere(0, 0, 0, 8);
    }

    public void initFrame(double time) {
        t = 5 * time;

        for(int i = 0; i < H; i++){
            for(int j = 0; j < W; j++){
                pixels[i][j][0] = 0;
                pixels[i][j][1] = 0;
                pixels[i][j][2] = 0;
            }
        }

        rnd.render(pixels);
    }

    public void setPixel(int x, int y, int rgb[]) {
        for (int j = 0 ; j < 3 ; j++){
            rgb[j] = pixels[y][x][j];
        }
    }
}
