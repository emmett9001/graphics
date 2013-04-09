public class Material {
    private double[] ambient;
    private double[] diffuse;
    private double[] specular;
    private double specPower;

    public Material(){
        ambient = new double[3];
        diffuse = new double[3];
        specular = new double[3];
        specPower = 1;
    }

    public void setAmbient(double r, double g, double b){
        this.ambient[0] = r*255;
        this.ambient[1] = g*255;
        this.ambient[2] = b*255;
    }

    public void setDiffuse(double r, double g, double b){
        this.diffuse[0] = r*255;
        this.diffuse[1] = g*255;
        this.diffuse[2] = b*255;
    }

    public void setSpecular(double r, double g, double b){
        this.specular[0] = r*255;
        this.specular[1] = g*255;
        this.specular[2] = b*255;
    }

    public void setSpecPower(double p){
        this.specPower = p;
    }
}
