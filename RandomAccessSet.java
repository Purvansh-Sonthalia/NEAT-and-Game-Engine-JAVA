import java.util.ArrayList;
import java.util.HashSet;

public class RandomAccessSet<T>
{
    private HashSet<T> hash;
    private ArrayList<T> data;

    public RandomAccessSet()
    {
        hash = new HashSet<>();
        data = new ArrayList<>();
    }

    public boolean contains(T obj)
    {
        return hash.contains(obj);
    }

    public void add(T obj)
    {
        if(!this.contains(obj))
        {
            hash.add(obj);
            data.add(obj);
        }
    }

    public T getRandomElement()
    {
        if(data.size()>0)
            return data.get((int)(Math.random()*data.size()));
        return null;
    }

    public int size()
    {
        return data.size();
    }

    public void clear()
    {
        hash.clear();
        data.clear();
    }

    public T getDataAt(int i)
    {
        if(i>=0 && i<size())
            return data.get(i);
        return null;
    }

    public void removeDataAt(int i)
    {
        if(i>=0 && i<size())
        {
            hash.remove(this.getDataAt(i));
            data.remove(i);
        }
    }

    public void removeElement(T obj)
    {
        hash.remove(obj);
        data.remove(obj);
    }

    public ArrayList<T> getDataList()
    {
        return data;
    }

    public HashSet<T> getHashSet()
    {
        return hash;
    }

    public void addSorted(T obj)
    {
        Gene g = (Gene)obj;
        for (int i = 0; i < size(); i++)
        {
            int in = ((Gene)data.get(i)).getInnovationNumber();
            if(g.getInnovationNumber() < in)
            {
                data.add(i,obj);
                hash.add(obj);
                return;
            }
        }
        data.add(obj);
        hash.add(obj);
    }
}