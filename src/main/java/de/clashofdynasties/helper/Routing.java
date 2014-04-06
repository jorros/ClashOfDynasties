package de.clashofdynasties.helper;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Road;
import de.clashofdynasties.models.Route;
import de.clashofdynasties.repository.RoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Routing
{
    private PriorityQueue<Node> openList;
    private LinkedList<Node> closedList;
    private Node goal;

    @Autowired
    private RoadRepository roadRepository;

    private class Node implements Comparable
    {
        private Object object;

        public double f;
        public double g;
        public Node parent;

        public Node(Object obj)
        {
            object = obj;
            f = 0.0;
            g = 0.0;
        }

        public int getX()
        {
            if(object instanceof City)
                return ((City)object).getX();
            else if(object instanceof Formation)
                return ((Formation)object).getX();
            else
                return 0;
        }

        public int getY()
        {
            if(object instanceof City)
                return ((City)object).getY();
            else if(object instanceof Formation)
                return ((Formation)object).getY();
            else
                return 0;
        }

        // Heuristik: geschätzter Restweg (Luftlinie)
        public double getH()
        {
            return Math.sqrt(Math.pow(getX() - goal.getX(), 2) + Math.pow(getY() - goal.getY(), 2));
        }

        public void setObject(Object obj)
        {
            object = obj;
        }

        public Object getObject()
        {
            return object;
        }

        @Override
        public int compareTo(Object o)
        {
            Node n = (Node) o;
            return (int) (f - n.f);
        }

        public List<Node> getNeighbours()
        {
            List<Node> neighbours = new ArrayList<Node>();

            if(object instanceof City)
            {
                City n = (City)object;

                for(Road r : roadRepository.findByCity(n.getId()))
                {
                    if(!r.getPoint1().equals(n))
                        neighbours.add(new Node(r.getPoint1()));
                    else
                        neighbours.add(new Node(r.getPoint2()));
                }
            }
            else if(object instanceof Formation)
            {
                Formation n = (Formation)object;

                if(n.isDeployed())
                {
                    List<Road> roads = roadRepository.findByCity(n.getLastCity().getId());
                    for(Road r : roads)
                    {
                        if(!r.getPoint1().equals(n.getLastCity()))
                            neighbours.add(new Node(r.getPoint1()));
                        else
                            neighbours.add(new Node(r.getPoint2()));
                    }
                }
                else
                {
                    neighbours.add(new Node(n.getLastCity()));
                    neighbours.add(new Node(n.getRoute().getNext()));
                }
            }

            return neighbours;
        }
    }

    // Distanz zwischen a und b berechnen
    private double caluclateG(Node a, Node b)
    {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    private void expandNode(Node current)
    {
        List<Node> neighbours = current.getNeighbours();
        for(Node n : neighbours)
        {
            if(closedList.contains(n))
                continue;

            double tempG = n.g + caluclateG(current, n);

            if(openList.contains(n) && tempG >= n.g)
                continue;

            n.parent = current;
            n.g = tempG;
            n.f = tempG + n.getH(); // Gesamtweg geschätzt

            if(!openList.contains(n))
                openList.offer(n);
        }
    }

    private Route createRoute(Node endPoint)
    {
        Route route = new Route();
        List<Road> roads = new ArrayList<Road>();

        Node lastNode = endPoint;

        while(true)
        {
            if(lastNode.getObject() instanceof City)
            {
                // Wenn Aktueller und Vorgänger Knoten Städte sind, Road ermitteln
                if(lastNode.parent.getObject() instanceof City)
                {
                    City p1 = (City) lastNode.getObject();
                    City p2 = (City) lastNode.parent.getObject();

                    roads.add(roadRepository.findByCities(p1.getId(), p2.getId()));
                }
                else if(lastNode.parent.getObject() instanceof Formation)
                {
                    // Wenn parent Formation ist, currentNode als nächste Stadt setzen
                    route.setNext((City) lastNode.getObject());
                }

                lastNode = lastNode.parent;
            }
            else
                break;
        }

        route.setRoads(roads);

        return route;
    }

    public Route calculateRoute(Formation formation, City city)
    {
        openList = new PriorityQueue<Node>();
        closedList = new LinkedList<Node>();
        goal = null;

        Node from = new Node(formation);
        goal = new Node(city);

        openList.offer(from);

        while(!openList.isEmpty())
        {
            Node currentNode = openList.poll();

            if(currentNode.getObject() instanceof City)
            {
                City currC = (City)currentNode.getObject();
                if(currC.equals(city))
                    return createRoute(currentNode);

                closedList.add(currentNode);
                expandNode(currentNode);
            }
            else if(currentNode.getObject() instanceof Formation)
            {
                closedList.add(currentNode);
                expandNode(currentNode);
            }
        }

        return null;
    }
}
