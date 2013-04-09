public class Material {
    private Vec3 ambient;
    private Vec3 diffuse;
    private Vec3 specular;
    private double specPower;

    public Material(){
        ambient = new Vec3();
        diffuse = new Vec3();
        specular = new Vec3();
        specPower = 1;
    }

    public void setAmbient(double r, double g, double b){
        this.ambient = new Vec3(r*255, g*255, b*255);
    }

    public void setDiffuse(double r, double g, double b){
        this.diffuse = new Vec3(r*255, g*255, b*255);
    }

    public void setSpecular(double r, double g, double b, double p){
        this.specular = new Vec3(r*255, g*255, b*255);
        this.specPower = p;
    }
}
