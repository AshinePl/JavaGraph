package graph;

import java.awt.Color;

class GraphConnection {
    int index;
    double distance;
    double time;
    int speed;
    Color color;
    
    public Color getColor()
    {
        return color;
    }
    
    public void setColor(Color c)
    {
        color = c;
    }
    
    public int getCity()
    {
        return index;
    }
    
    public double getDistance()
    {
        return distance;
    }
    
    public double getTime()
    {
        return time;
    }
    
    public int getSpeed()
    {
        return speed;
    }

    public GraphConnection(int city, double distance, int speed) {
        this.index = city;
        this.distance = distance;
        this.speed = speed;
        this.time = distance / speed;
        this.color = Color.black;
    }
    
    
    
}
