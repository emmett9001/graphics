public class Matrix implements IMatrix {
    public double[][] mat;
    private double[][] temp;
    private double[][] invert;
    private double[] transformedNormal;
    private double[] srcNormal;
    private Matrix inherit;

    // keeping things straight in my head...
    /* MY OTHER ONE (row major)
     *  | xx | xy | xz | 0  |
     *  | yx | yy | yz | 0  |
     *  | zx | zy | zz | 0  |
     *  | tx | ty | tz | 1  |
     *
     * THIS ONE (column major)
     *  | xx | yx | zx | tx |
     *  | xy | yy | zy | ty |
     *  | xz | yz | zz | tz |
     *  | 0  | 0  | 0  | 1  |
     * 0 means direction, 1 means point
     *
     * vector multiplication (dot product)
     * x1*x2 + y1*y2 + z1*z2 + t1*t2 = scalar value
     *
     * multiply the rows of the first by the columns of the second
     * eg result[0][0] = A.row0 * B.col0 ...dot product
     * eg result[1][0] = A.row1 * B.col0 ...dot product
     * */

    public Matrix(){
        mat = new double[4][4];
        temp = new double[4][4];
        invert = new double[4][4];
        transformedNormal = new double[3];
        srcNormal = new double[3];
        //inherit = new Matrix();
    }

    public Matrix(double[][] mat){
        this.mat = mat;
        this.temp = new double[4][4];
        //inherit = new Matrix();
    }

    public void identity(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(i == j){
                    mat[i][j] = 1.0f;
                } else {
                    mat[i][j] = 0.0f;
                }
            }
        }
    }

    public void set(int col, int row, double value){
        mat[col][row] = value;
    }

    public double get(int col, int row){
        return mat[col][row];
    }

    public void translate(double x, double y, double z){
        double[][] mul = {
            {1.0, 0.0, 0.0, 0.0},
            {0.0, 1.0, 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0},
            {x, y, z, 1.0},
        };
        rightMultiply(new Matrix(mul));
    }

    public void translate(Vec3 trans){
        translate(trans.x, trans.y, trans.z);
    }

    public void parent(Matrix par){
        translate(par.get(3,0), par.get(3,1), par.get(3,2));
    }

    public void rotate(Vec3 rot){
        rotateX(rot.x);
        rotateY(rot.y);
        rotateZ(rot.z);
    }

    public void rotateX(double radians){
        double[][] mul = {
            {1.0, 0.0,               0.0,               0.0},
            {0.0, Math.cos(radians), Math.sin(radians), 0.0},
            {0.0, -Math.sin(radians),Math.cos(radians), 0.0},
            {0.0, 0.0,               0.0,               1.0},
        };
        rightMultiply(new Matrix(mul));
    }

    public void rotateZ(double radians){
        double[][] mul = {
            {Math.cos(radians), Math.sin(radians), 0.0, 0.0},
            {-Math.sin(radians), Math.cos(radians), 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0},
            {0.0, 0.0, 0.0, 1.0},
        };
        rightMultiply(new Matrix(mul));
    }

    public void rotateY(double radians){
        double[][] mul = {
            {Math.cos(radians), 0.0, -Math.sin(radians), 0.0},
            {0.0, 1.0, 0.0, 0.0},
            {Math.sin(radians),0.0, Math.cos(radians), 0.0},
            {0.0, 0.0,               0.0,               1.0},
        };
        rightMultiply(new Matrix(mul));
    }

    public void scale(Vec3 sc){
        scale(sc.x, sc.y, sc.z);
    }

    public void scale(double x, double y, double z){
        double[][] mul = {
            {x, 0.0, 0.0, 0.0},
            {0.0, y, 0.0, 0.0},
            {0.0, 0.0, z, 0.0},
            {0.0, 0.0, 0.0, 1.0},
        };
        rightMultiply(new Matrix(mul));
    }

    public void leftMultiply(Matrix other){
        zeroTempMatrix();
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                for(int k = 0; k < 4; k++){
                    temp[j][i] += this.get(i,k) * other.get(k,j);
                }
            }
        }
        for(int i = 0; i < 4; i++){
            mat[i] = temp[i].clone();
        }
    }

    public void rightMultiply(Matrix other){
        zeroTempMatrix();
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                for(int k = 0; k < 4; k++){
                    temp[j][i] += this.get(j,k) * other.get(k,i);
                }
            }
        }
        for(int i = 0; i < 4; i++){
            mat[i] = temp[i].clone();
        }
    }

    private void zeroTempMatrix(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                temp[i][j] = 0.0;
            }
        }
    }

    public void transform(double[] src, double[] dst){
        double mul;
        for(int i = 0; i < 3; i++){
            dst[i] = 0;
            for(int j = 0; j < 4; j++){
                if(j == 3){
                    mul = 1;
                } else {
                    mul = src[j];
                }
                dst[i] += mul * mat[j][i];
            }
        }
        // ((M.invert).transpose) DOT N
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                temp[j][i] = mat[i][j];
            }
        }

        srcNormal[0] = src[3];
        srcNormal[1] = src[4];
        srcNormal[2] = src[5];

        Invert.invert(temp, invert);
        transpose(invert, temp);
        vecMatDot(srcNormal, temp, transformedNormal);

        dst[3] = ((transformedNormal[0] + 1) * 255) / 2;
        dst[4] = ((transformedNormal[1] + 1) * 255) / 2;
        dst[5] = ((transformedNormal[2] + 1) * 255) / 2;
    }

    private void transpose(double[][] src, double[][] dst){
        for(int i = 0; i < src.length; i++){
            for(int j = 0; j < src[0].length; j++){
                dst[j][i] = src[i][j];
            }
        }
    }

    private void vecMatDot(double[] vec, double[][]mat, double[] dst){
        for(int i = 0; i < 3; i++){
            dst[i] = dot(vec, mat[i].clone());
        }
    }

    private double dot(double[] vec1, double[] vec2){
        double product = vec1[0]*vec2[0] + vec1[1]*vec2[1] + vec1[2]*vec2[2];
        return product;
    }

    public void pp(){
        // prettyprint, for debugging
        for(int i = 0; i < 4; i++){
            System.out.print("| ");
            for(int j = 0; j < 4; j++){
                System.out.format("%.2f | ", mat[j][i]);
            }
            System.out.println();
        }
    }

    public void pp(double [][]arr){
        // prettyprint, for debugging
        for(int i = 0; i < 4; i++){
            System.out.print("| ");
            for(int j = 0; j < 4; j++){
                System.out.format("%.2f | ", arr[j][i]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        Matrix m = new Matrix();
        m.identity();
        m.translate(1.0, 2.0, 3.0);
        m.rotateX(Math.PI/2);
        m.scale(1.0, 2.0, 3.0);
        m.pp();
    }
}
