public class Vec3{
    double x, y, z;

    public Vec3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 add(Vec3 b){
        return new Vec3(this.x + b.x, this.y + b.y, this.z + b.z);
    }

    public Vec3 sub(Vec3 b){
        return new Vec3(this.x - b.x, this.y - b.y, this.z - b.z);
    }

    public Vec3 mul(double multiplier){
        return new Vec3(this.x*multiplier, this.y*multiplier, this.z*multiplier);
    }

    public Vec3 div(double divisor){
        return new Vec3(this.x/divisor, this.y/divisor, this.z/divisor);
    }

    public double mag(){
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }

    public Vec3 reverse(){
        return new Vec3(this.x*-1, this.y*-1, this.z*-1);
    }

    public double dot(Vec3 multiplier){
        return this.x*multiplier.x + this.y*multiplier.y + this.z*multiplier.z;
    }

    public Vec3 normalize(){
        double magnitude = this.mag();
        return new Vec3(this.x/magnitude, this.y/magnitude, this.z/magnitude);
    }
}
