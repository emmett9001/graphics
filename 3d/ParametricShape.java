public abstract class ParametricShape{
    abstract double x(double u, double v);
    abstract double y(double u, double v);
    abstract double z(double u, double v);
}

class PSphere extends ParametricShape{
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
        return Math.sin(2*Math.PI*u);
    }
    double y(double u, double v){
        return Math.cos(2*Math.PI*u);
    }
    double z(double u, double v){
        return 2 * v - 1;
    }
}
