public class Agent
{
    private Genome genome;
    private Species species;
    private double score;

    private Calculator calc;

    public void createCalculator()
    {
        calc = new Calculator(genome);
    }

    public double[] calculate(double... args)
    {
        if(calc!=null)
            return calc.calculate(args);
        return null;
    }

    public Calculator getCalculator()
    {return calc;}

    public double getScore()
    {return score;}

    public Genome getGenome()
    {return genome;}

    public Species getSpecies()
    {return species;}

    public void setScore(double s)
    {this.score = s;}

    public void setGenome(Genome g)
    {this.genome = g;}

    public void setSpecies(Species s)
    {this.species = s;}

    public double distance(Agent b)
    {
        return this.genome.distance(b.getGenome());
    }

    public void mutate()
    {genome.mutate();}
}
