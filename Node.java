import java.util.ArrayList;

public class Node implements Comparable<Node>
{
    private double x;
    private double output;
    private ArrayList<Connection> connections = new ArrayList<>();

    public Node(double x)
    {
        this.x=x;
    }

    public double getX()
    {return x;}

    public void setX(double x)
    {this.x = x;}

    public void setOutput(double o)
    {this.output = o;}

    public double getOutput()
    {return output;}

    public void setConnections(ArrayList<Connection> c)
    {this.connections = c;}

    public ArrayList<Connection> getConnections()
    {return connections;}

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.x, o.x);
    }
    
    public void calculate()
    {
        double s=0;
        for(Connection c:connections)
        {
            if(c.isEnabled())
                s+= c.getWeight() * c.getStart().getOutput();
        }
        output = tanh(s);
    }

    public static double tanh(double a)
    {return (1-2*sigmoid(-2*a));}

    public static double sigmoid(double a)
    {
        return 1/(1+Math.exp(-a));
    }
}
