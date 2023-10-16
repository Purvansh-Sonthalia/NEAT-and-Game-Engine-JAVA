public class Ray
{
    Vector2 startPoint;
    Vector2 direction;
    double length;

    Line line;

    public Ray(Vector2 s, Vector2 dir, double l)
    {
        startPoint = s;
        direction = dir.normalized();
        length = l;

        line = new Line(s, dir);
    }

    public void rotateClockwise(double radians)
    {
        direction.rotateClockwiseSave(radians);
        line = new Line(startPoint, direction);
    }

    public Vector2 checkCollision(double w, double h)
    {
        Vector2 temp = startPoint.add(direction.multiply(length));
        double tmpX=-1,tmpY=-1;
        if(temp.x<=0)
        {
            tmpX = 0;
        }
        if(temp.x>=w)
        {
            tmpX = w;
        }
        if(temp.y<=0)
        {
            tmpY = 0;
        }
        if(temp.y>=h)
        {
            tmpY = h;
        }

        if(tmpX<0 && tmpY<0)
            return null;
        if(tmpX>=-0.0001 && tmpY>=-0.0001)
            return new Vector2(tmpX, tmpY);
        if(tmpX >= -0.0001)
            return new Vector2(tmpX, temp.y);
        return new Vector2(temp.x, tmpY);
    }

    public Vector2 checkCollision(GameObject g)
    {      
        Vector2 min=null;
        double minDist = -1;
        
        if(g.isCircle)
        {
            if(line.distanceFromPoint(g.position)<g.radius)
            {
                Vector2 current = startPoint.clone();
                for (int i = 0; i < 10; i++)
                {
                    double step = g.position.subtract(current).magnitude() - g.radius;
                    current = current.add(direction.multiply(step));
                    if(current.subtract(startPoint).sqMagnitude()>length*length)
                        return null;
                }
                return current;
            }
        }
        else
        {
            for (int i = 0; i < g.shape.size(); i++)
            {
                Vector2 p1 = g.position.add(g.shape.get(i));
                Vector2 p2 = g.position.add(g.shape.get((i+1)%g.shape.size()));

                Line temp = new Line(p1, p2.subtract(p1).normalized());
                Vector2 iPoint = line.intersection(temp);
                if(iPoint != null)
                {
                    if(p1.subtract(iPoint).magnitude()+p2.subtract(iPoint).magnitude()<=p1.subtract(p2).magnitude()+0.001)
                    {
                        if(iPoint.subtract(startPoint).dotProduct(direction)>0)
                        {
                            double sqdist = iPoint.subtract(startPoint).sqMagnitude();
                            if(sqdist>minDist && sqdist<=length*length)
                            {
                                min = iPoint;
                                minDist = iPoint.subtract(startPoint).sqMagnitude();
                            }
                        }
                    }
                }
            }
        }

        
        return min;
    }
}
