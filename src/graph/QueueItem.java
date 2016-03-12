/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

/**
 *
 * @author Andrzej
 */
public class QueueItem implements Comparable<QueueItem>{
    Double prior;
    Integer city;

    @Override
    public int compareTo(QueueItem o) {
        if(prior > o.getPrior()) return 1;
        if(prior < o.getPrior()) return -1;
        return 0;
    }
    public Double getPrior()
    {
        return prior;
    }
    public Integer getCity()
    {
        return city;
    }
    QueueItem(Integer c, Double p)
    {
        city = c;
        prior = p;
    }
}
