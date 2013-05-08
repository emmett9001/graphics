import java.util.*;

public class RaytraceRenderer{
    private ArrayList<LeanSphere> spheres;
    private double[] v_s = new double[3];
    private double[] v = new double[3];
    private double[] w = new double[3];
    private double[] t = new double[2];
    private double[] nn = new double[3];
    private int[] c = new int[3];
    private double f = 1000;
    private int W, H;
    private DirectionalLight light;
    private Vec3 vertColor, vertDiffuseColor, vertSpecColor, vertNormal, eye;

    public RaytraceRenderer(int W, int H){
        this.W = W;
        this.H = H;
        this.spheres = new ArrayList<LeanSphere>();

        vertColor = new Vec3();
        vertNormal = new Vec3();
        vertDiffuseColor = new Vec3();
        vertSpecColor = new Vec3();
        eye = new Vec3(0, 0, 1);
    }

    public void addLight(DirectionalLight l){
        this.light = l;
    }

    public void addSphere(LeanSphere s){
        this.spheres.add(s);
    }

    public void render(int[][][] pix, double[][] zbuf){
        for(LeanSphere s : this.spheres){
            this._render(s.getArray().clone(), s.getMaterial(), pix, zbuf);
        }
    }

    private void _render(double[] s, Material mat, int[][][] pix, double[][] zbuf){
        int thresh = 30, skip = 6;
        for (int i = 0 ; i < W; i+=skip)
        for (int j = 0 ; j < H; j++) {
            doRaytrace(s, mat, pix, zbuf, i, j);
            if(i >= skip && colorDiff(pix[j][i], pix[j][i-skip]) > thresh){
                for(int k = 1; k < skip; k++){
                    doRaytrace(s, mat, pix, zbuf, i-k, j);
                }
            } else {
                for(int k = 1; k < skip; k++){
                    lerpColor(pix, zbuf, i, j, k, skip);
                }
            }
        }
    }

    private void lerpColor(int[][][] pix, double[][] zbuf, int i, int j, int k, int skip){
        float pct = ((i-k)-(i-skip))/-skip;
        if(i == 0) return;
        pix[j][i-k][0] = (int)LERP(pct, pix[j][i-skip][0], pix[j][i][0]);
        pix[j][i-k][1] = (int)LERP(pct, pix[j][i-skip][1], pix[j][i][1]);
        pix[j][i-k][2] = (int)LERP(pct, pix[j][i-skip][2], pix[j][i][2]);
    }

    private int colorDiff(int[] c1, int[] c2){
        int dR = Math.abs(c1[0] - c2[0]);
        int dG = Math.abs(c1[1] - c2[1]);
        int dB = Math.abs(c1[2] - c2[2]);
        int total = dR + dG + dB;

        return total;
    }

    private void doRaytrace(double[] s, Material mat, int[][][] pix, double[][] zbuf, int i, int j){
        double x = 0.8 * ((i+0.5) / W - 0.5);
        double y = 0.8 * ((j+0.5) / W - 0.5 * H / W);

        set(v, 0, 0, f);
        set(w, x, y, -f*s[2]);
        normalize(w);

        if(raytrace(s, v, w, t)) {
            for (int k = 0 ; k < 3 ; k++)
                nn[k] = v[k] + t[0] * w[k] - s[k];
            normalize(nn);

            set(v, x, y, -f*s[2]);
            set(w, this.light.getDirection().x,
                this.light.getDirection().y,
                this.light.getDirection().z);
            normalize(w);
            boolean shadow = false;
            double[] t2 = new double[2];
            for(LeanSphere s2 : this.spheres){
                if(raytrace(s2.getArray(), v, w, t2)){
                    shadow = true;
                }
            }
            computeShading(mat, s, x, y, shadow);

            if(t[0] < zbuf[j][i]){
                pix[j][i][0] = c[0];
                pix[j][i][1] = c[1];
                pix[j][i][2] = c[2];
                zbuf[j][i] = t[0];
            }
        }
    }

    private void computeShading(Material mat, double[] sp, double x, double y, boolean shadow){
        vertNormal.init(nn[0], nn[1], nn[2]);

        double diffusePower = this.light.getDirection().dot(vertNormal.normalize());
        if(diffusePower < 0){
            diffusePower = 0;
        }
        vertDiffuseColor = mat.getDiffuse().mul(diffusePower);

        double specPower = this.light.getDirection().reflect(vertNormal).dot(eye);
        if(specPower < 0){
            specPower = 0;
        }
        specPower = Math.pow(specPower, mat.getSpecularFocus());
        vertSpecColor = mat.getSpecular().mul(specPower*mat.getSpecularPower());

        if(!shadow){
            vertColor = vertDiffuseColor.add(vertSpecColor);
            vertColor = vertColor.mul(this.light.getPower());
        }
        vertColor = mat.getAmbient().add(vertColor);

        c[0] = (int)(255 * Math.pow(vertColor.x, .45));
        c[1] = (int)(255 * Math.pow(vertColor.y, .45));
        c[2] = (int)(255 * Math.pow(vertColor.z, .45));
    }

    private boolean equals(double[] s1, double[] s2){
        return (s1[0] == s2[0] && s1[1] == s2[1] && s1[2] == s2[2] &&
                s1[3] == s2[3]);
    }

    private boolean raytrace(double[] s, double[] v, double[] w, double[] t) {
        diff(v, s, v_s);

        double A = 1.0;
        double B = 2 * dot(w, v_s);
        double C = dot(v_s, v_s) - s[3] * s[3];

        return solveQuadraticEquation(A, B, C, t);
    }

    private boolean solveQuadraticEquation(double A, double B, double C, double[] t) {
        double discriminant = B * B - 4 * A * C;
        if (discriminant < 0)
            return false;

        double d = Math.sqrt(discriminant);
        t[0] = (-B - d) / (2 * A);
        t[1] = (-B + d) / (2 * A);
        return true;
    }

    private double dot(double[] o, double[] t){
        return o[0]*t[0] + o[1]*t[1] + o[2]*t[2];
    }

    private void diff(double[] f, double[] s, double[] res){
        res[0] = f[0] - s[0];
        res[1] = f[1] - s[1];
        res[2] = f[2] - s[2];
    }

    private void set(double[] r, double x, double y, double z){
        r[0] = x;
        r[1] = y;
        r[2] = z;
    }

    private void normalize(double[] w){
        double length = Math.sqrt((w[0]*w[0]) + (w[1]*w[1]) + (w[2]*w[2]));
        w[0] = w[0] / length;
        w[1] = w[1] / length;
        w[2] = w[2] / length;
    }

    private double LERP(double percent, double a, double b){
        return percent * (b - a) + a;
    }
}
