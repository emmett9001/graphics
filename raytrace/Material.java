public class Material {
    private Vec3 ambient;
    private Vec3 diffuse;
    private Vec3 specular;
    private double specPower, specFoc;

    public Material(){
        ambient = new Vec3();
        diffuse = new Vec3();
        specular = new Vec3();
        specPower = 1;
        specFoc = 1;
    }

    public void setAmbient(double r, double g, double b){
        this.ambient = new Vec3(r, g, b);
    }

    public void setDiffuse(double r, double g, double b){
        this.diffuse = new Vec3(r, g, b);
    }

    public void setSpecular(double r, double g, double b, double p){
        this.specular = new Vec3(r, g, b);
        this.specPower = p;
    }

    public void setSpecularFocus(double f){
        this.specFoc = f;
    }

    public Vec3 getAmbient(){
        return this.ambient;
    }

    public Vec3 getDiffuse(){
        return this.diffuse;
    }

    public Vec3 getSpecular(){
        return this.specular;
    }

    public double getSpecularPower(){
        return this.specPower;
    }

    public double getSpecularFocus(){
        return this.specFoc;
    }
}
