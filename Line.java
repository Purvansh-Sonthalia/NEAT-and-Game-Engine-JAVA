public class Line
{
    double a,b,c;//ax+by+c

    public Line(Vector2 p1, Vector2 dir)
    {
        a = -dir.y;
        b = dir.x;
        c = dir.y*p1.x - dir.x*p1.y;
    }

    public Vector2 intersection(Line l2)
    {
        double k = this.a*l2.b - l2.a*this.b;
        if(k>-0.001 && k<0.001)
            return null;
        return new Vector2((this.b*l2.c - l2.b*this.c)/k, (l2.a*this.c - this.a*l2.c)/k);
    }

    public double distanceFromPoint(Vector2 point)
    {
        return Math.abs(a*point.x + b*point.y + c)/Math.sqrt(a*a+b*b);
    }

    public Vector2 footOfNormal(Vector2 point)
    {
        double k = -(a*point.x+b*point.y+c)/(a*a+b*b);
        return new Vector2(point.x + k*a, point.y + k*b);
    }

    public Vector2 reflectionOf(Vector2 point)
    {
        double k = -2*(a*point.x+b*point.y+c)/(a*a+b*b);
        return new Vector2(point.x + k*a, point.y + k*b);
    }

    public double put(Vector2 point)
    {return (a*point.x+b*point.y+c);}
}
