public class Arm{
    Geometry base, upper, midjoint, lower;

    public Arm(){
        base = new Geometry(0, 0);

        upper = new Geometry(0, 0);
        upper.buildCube();
        base.add(upper);

        midjoint = new Geometry(0, 0);
        base.add(midjoint);

        lower = new Geometry(0, 0);
        lower.buildCube();
        midjoint.add(lower);
    }

    public Matrix getMatrix(){
        return base.getMatrix();
    }

    public Geometry getRoot(){
        return base;
    }

    public void transform(double ttime, Vec3 baseTranslate, Vec3 baseRotate, Vec3 lowerRotate){
        base.getMatrix().identity();
        //base.getMatrix().rotateZ(-Math.PI/2);
        base.getMatrix().rotateZ(baseRotate.z);
        base.getMatrix().rotateY(baseRotate.y);
        base.getMatrix().translate(baseTranslate.x, baseTranslate.y, baseTranslate.z);

        upper.getMatrix().identity();
        upper.getMatrix().translate(0, 1, 0);
        upper.getMatrix().scale(.2, 2, .2);

        midjoint.getMatrix().identity();
        midjoint.getMatrix().translate(0, 4, 0);

        lower.getMatrix().identity();
        lower.getMatrix().translate(0, 1, 0);
        lower.getMatrix().scale(.2, 1.5, .2);
        lower.getMatrix().rotateZ(lowerRotate.z);
    }
}
