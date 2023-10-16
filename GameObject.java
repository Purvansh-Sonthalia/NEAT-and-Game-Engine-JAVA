import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;

public class GameObject
{
    Vector2 position;
    Vector2 forward;
    ArrayList<Vector2> shape = new ArrayList<>();
    boolean isCircle=false;
    double radius;
    GameObject parent;
    ArrayList<GameObject> children = new ArrayList<>();
    Color color;

    boolean fill = true;

    public void Update()
    {}

    void draw(Graphics g)
    {
        g.setColor(color);
        if(isCircle)
        {
            if(fill)
                g.fillOval((int)(position.x-radius), (int)(position.y-radius), (int)(2*radius), (int)(2*radius));
            else
                g.drawOval((int)(position.x-radius), (int)(position.y-radius), (int)(2*radius), (int)(2*radius));
        }
        else
        {
            int[] shapeX = new int[shape.size()];
            int[] shapeY = new int[shape.size()];
            for (int i=0; i<shape.size(); i++)
            {
                Vector2 temp = shape.get(i).add(position);
                
                shapeX[i] = (int)temp.x;
                shapeY[i] = (int)temp.y;
            }
            if(fill)
                g.fillPolygon(shapeX, shapeY, shape.size());
            else
                g.drawPolygon(shapeX, shapeY, shape.size());
        }
    }

    public void rotateClockwise(double radians)
    {
        forward=forward.rotateClockwise(radians).normalized();
        for (int i=0; i<shape.size(); i++)
        {
            double magnitude = shape.get(i).magnitude();
            shape.set(i, shape.get(i).rotateClockwise(radians).normalized().multiply(magnitude));
        }
    }

    public CollisionPoint collisionDetection(GameObject go)
    {
        CollisionPoint cp=null;
        
        if(go.isCircle)
        {
            if(this.isCircle)
            {
                if (this.position.subtract(go.position).sqMagnitude() <= (this.radius+go.radius)*(this.radius+go.radius))
                {
                    cp = new CollisionPoint();
                    cp.position = Vector2.Lerp(this.position, go.position, this.radius/(this.radius+go.radius));
                    cp.normal = this.position.subtract(go.position).normalized();
                    return cp;
                }
                return null;
            }
            //else
            boolean flag = true;
            for (int i = 0; i < this.shape.size(); i++)
            {
                Vector2 p1 = this.position.add(this.shape.get(i));
                Vector2 p2 = this.position.add(this.shape.get((i+1)%this.shape.size()));

                Line temp = new Line(p1, p2.subtract(p1).normalized());

                if(flag)
                {
                    flag = (temp.put(go.position)*temp.put(this.position))>0;
                }
                
                if(temp.distanceFromPoint(go.position) <= go.radius)
                {
                    Vector2 foot = temp.footOfNormal(go.position);
                    if(p1.subtract(foot).dotProduct(p2.subtract(foot))<0)
                    {
                        cp = new CollisionPoint();
                        cp.position = foot.clone();
                        cp.normal = go.position.subtract(foot).normalized();
                    }
                    if(p1.subtract(go.position).sqMagnitude() < go.radius*go.radius)
                    {
                        cp = new CollisionPoint();
                        cp.position = p1.clone();
                        cp.normal = go.position.subtract(p1).normalized();
                    }
                    if(p2.subtract(go.position).sqMagnitude() < go.radius*go.radius)
                    {
                        cp = new CollisionPoint();
                        cp.position = p2.clone();
                        cp.normal = go.position.subtract(p2).normalized();
                    }
                }
            }
            
            return cp;
        }

        //else
        if(this.isCircle)
        {
            return go.collisionDetection(this);
        }
        else
        {
            for (int i = 0; i < this.shape.size(); i++)
            {
                Vector2 p1 = this.position.add(this.shape.get(i));
                Vector2 p2 = this.position.add(this.shape.get((i+1)%this.shape.size()));
                Line temp = new Line(p1, p2.subtract(p1).normalized());

                for (int j = 0; j < go.shape.size(); j++)
                {
                    Vector2 p3 = go.position.add(go.shape.get(j));
                    Vector2 p4 = go.position.add(go.shape.get((j+1)%go.shape.size()));
                    Line line = new Line(p3, p4.subtract(p3).normalized());

                    Vector2 iPoint = temp.intersection(line);
                    if(iPoint != null)
                    {
                        if(p1.subtract(iPoint).dotProduct(p2.subtract(iPoint))<0)
                        {
                            if(p3.subtract(iPoint).dotProduct(p4.subtract(iPoint))<0)
                            {
                                cp = new CollisionPoint();
                                cp.position = iPoint;
                                cp.normal = go.position.subtract(temp.footOfNormal(go.position)).normalized();
                                return cp;
                            }
                        }
                    }
                }

            }
            
            return null;
        }
    }
}
