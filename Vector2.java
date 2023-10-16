public class Vector2
{
    double x,y;

    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double sqMagnitude()
    {
        return x*x+y*y;
    }

    public double magnitude()
    {
        return Math.sqrt(x*x+y*y);
    }

    public Vector2 zero()
    {
        return new Vector2(0, 0);
    }

    public Vector2 one()
    {
        return new Vector2(1, 1);
    }

    public Vector2 normalized()
    {
        double m = magnitude();
        return new Vector2(x/m, y/m);
    }

    public Vector2 add(Vector2 v)
    {
        return new Vector2(this.x+v.x, this.y+v.y);
    }

    public Vector2 subtract(Vector2 v)
    {
        return new Vector2(this.x-v.x, this.y-v.y);
    }

    public Vector2 multiply(double k)
    {
        return new Vector2(x*k, y*k);
    }

    public double dotProduct(Vector2 v)
    {
        return (this.x*v.x+this.y*v.y);
    }

    public Vector2 rotateClockwise(double radians)
    {
        double m = magnitude();
        double nx = x*Math.cos(radians) - y*Math.sin(radians);
        double ny = y*Math.cos(radians) + x*Math.sin(radians);
        return (new Vector2(nx, ny)).normalized().multiply(m);
    }

    public void rotateClockwiseSave(double radians)
    {
        double m = magnitude();
        
        x = x*Math.cos(radians) - y*Math.sin(radians);
        y = y*Math.cos(radians) + x*Math.sin(radians);

        Vector2 n=normalized();
        x = n.x * m;
        y = n.y * m;
    }

    public double distanceFrom(Vector2 v)
    {
        return this.subtract(v).magnitude();
    }

    public double sqDistanceFrom(Vector2 v)
    {
        return this.subtract(v).sqMagnitude();
    }

    public static Vector2 Lerp(Vector2 v1, Vector2 v2, double k)
    {
        return v1.add(v2.subtract(v1).multiply(k));
    }

    public Vector2 clone()
    {
        return new Vector2(x, y);
    }
}