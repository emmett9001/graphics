public class Vec2{
    double x, y;

    public Vec2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vec2 sub(Vec2 b){
        return new Vec2(this.x - b.x, this.y - b.y);
    }

    public Vec2 mul(double multiplier){
        return new Vec2(this.x*multiplier, this.y*multiplier);
    }

    public Vec2 div(double divisor){
        return new Vec2(this.x/divisor, this.y/divisor);
    }

    public double mag(){
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }

    public Vec2 reverse(){
        return new Vec2(this.x*-1, this.y*-1);
    }

    public double dot(Vec2 multiplier){
        return this.x*multiplier.x + this.y*multiplier.y;
    }

    public Vec2 normalize(){
        return new Vec2(this.x/this.mag(), this.y/this.mag());
    }
}
