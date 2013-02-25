public class Matrix implements IMatrix {
    public double[][] mat;
    private double[][] temp;

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
    }

    public Matrix(double[][] mat){
        this.mat = mat;
        this.temp = new double[4][4];
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
        mat[3][0] += x;
        mat[3][1] += y;
        mat[3][2] += z;
    }

    public void rotateX(double radians){
        double[][] mul = {
            {1.0, 0.0,               0.0,               0.0},
            {0.0, Math.cos(radians), Math.sin(radians), 0.0},
            {0.0, -Math.sin(radians),Math.cos(radians), 0.0},
            {0.0, 0.0,               0.0,               1.0},
        };
        leftMultiply(new Matrix(mul));
    }

    public void rotateZ(double radians){
        double[][] mul = {
            {Math.cos(radians), Math.sin(radians), 0.0, 0.0},
            {-Math.sin(radians), Math.cos(radians), 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0},
            {0.0, 0.0, 0.0, 1.0},
        };
        leftMultiply(new Matrix(mul));
    }

    public void rotateY(double radians){
        double[][] mul = {
            {Math.cos(radians), 0.0, -Math.sin(radians), 0.0},
            {0.0, 1.0, 0.0, 0.0},
            {Math.sin(radians),0.0, Math.cos(radians), 0.0},
            {0.0, 0.0,               0.0,               1.0},
        };
        leftMultiply(new Matrix(mul));
    }

    public void scale(double x, double y, double z){
        mat[0][0] *= x;
        mat[1][1] *= y;
        mat[2][2] *= z;
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

    public static void main(String[] args){
        Matrix m = new Matrix();
        m.identity();
        m.translate(1.0, 2.0, 3.0);
        m.rotateX(Math.PI/2);
        m.scale(1.0, 2.0, 3.0);
        m.pp();
    }
}
