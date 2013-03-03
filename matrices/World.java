public class World{
    Geometry root;
    Matrix matrix;

    public World(){
        root = new Geometry(0, 0);
        matrix = new Matrix();
        matrix.identity();
    }

    public Geometry getRoot(){
        return root;
    }

    public Matrix getMatrix(){
        return matrix;
    }
}
