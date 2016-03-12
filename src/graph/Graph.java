/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.awt.Dimension;
import java.util.ArrayList;


public class Graph {
    String name;    
    double x;
    double y;
    int pX;
    int pY;
    ArrayList<GraphConnection> connection;
    
    Graph(String name, double x, double y)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        connection = new ArrayList<>();
    }
    
    public ArrayList<GraphConnection> getConnections()
    {
        return connection;
    }
    public GraphConnection getConnection(int city)
    {
        for(GraphConnection x : connection)
        {
            if(x.getCity() == city) return x;
        }
        return null;
    }
    
    public void AddConnection(int city, double dist, int speed)
    {
        connection.add(new GraphConnection(city, dist, speed));
    }
    
    public String getName()
    {
        return name;
    }
    
    public Dimension getXY()
    {
        return new Dimension(pX, pY);
    }
    
    public int getPX()
    {
        return pX;
    }
    
    public int getPY()
    {
        return pY;
    }
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    public void setPos(double aX, double aY, double minX, double minY, int panelMinX, int panelMinY)
    {
        pX = (int)(aX*(x - minX)) + panelMinX;
        pY = (int)(aY*(y - minY)) + panelMinY;
    }
}
