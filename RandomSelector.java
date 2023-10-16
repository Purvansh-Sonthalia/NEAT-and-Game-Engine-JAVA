import java.util.ArrayList;

public class RandomSelector<T>
{
    private ArrayList<T> elements;
    private ArrayList<Double> scores;
    private double totalScore;

    public RandomSelector()
    {
        elements = new ArrayList<>();
        scores = new ArrayList<>();
        totalScore = 0;
    }

    public void add(T element, double score)
    {
        elements.add(element);
        scores.add(score);
        totalScore+=score;
    }

    public T getRandomElement()
    {
        if(elements.size() == 1)
            return elements.get(0);
        double temp = Math.random() * totalScore;
        double count = 0;

        for(int i=0; i<scores.size(); i++)
        {
            count+=scores.get(i);
            if(count>=temp)
                return elements.get(i);
        }
        return null;
    }

    public void clear()
    {
        elements.clear();
        scores.clear();
    }
}
