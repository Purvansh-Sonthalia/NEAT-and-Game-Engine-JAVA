public class Connection
{
    private Node start;
    private Node end;

    private double weight;
    private boolean enabled=true;

    public Connection(Node from, Node to)
    {
        this.start = from;
        this.end = to;

        this.weight = 4*Math.random()-2;
    }

    public Node getStart()
    {return start;}

    public void setStart(Node from)
    {this.start = from;}

    public Node getEnd()
    {return end;}

    public void setEnd(Node to)
    {this.end = to;}

    public double getWeight()
    {return weight;}

    public void setWeight(double w)
    {this.weight = w;}

    public boolean isEnabled()
    {return enabled;}

    public void setEnabled(boolean e)
    {this.enabled = e;}
}
