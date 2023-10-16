public class NodeGene extends Gene
{
    private double x,y;

    public NodeGene(int in)
    {super(in);}

    public double getX()
    {return x;}

    public void setX(double x)
    {this.x=x;}

    public double getY()
    {return y;}

    public void setY(double y)
    {this.y=y;}

    public boolean equals(Object obj)
    {
        if(obj instanceof NodeGene)
            return this.innovationNumber == ((NodeGene)obj).getInnovationNumber();
        return false;
    }

    public int hashCode()
    {
        return innovationNumber;
    }
}
