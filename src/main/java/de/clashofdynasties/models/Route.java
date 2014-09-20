package de.clashofdynasties.models;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.repository.CityRepository;
import de.clashofdynasties.repository.RoadRepository;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Route {
    private ObjectId next;

    private List<ObjectId> roads;

    private ObjectId target;

    private ObjectId currentRoad;

    @Transient
    private int time;

    public Route() {
        roads = new ArrayList<>();
    }

    public City getNext() {
        return CityRepository.get().findById(next);
    }

    public void setNext(City next) {
        this.next = next.getId();
    }

    public List<Road> getRoads() {
        return roads.stream().map(r -> RoadRepository.get().findById(r)).collect(Collectors.toList());
    }

    public void removeRoad(int index) {
        roads.remove(index);
    }

    public void setRoads(List<Road> roads) {
        this.roads = roads.stream().map(r -> r.getId()).collect(Collectors.toList());
    }

    public Road getCurrentRoad() {
        return RoadRepository.get().findById(currentRoad);
    }

    public void setCurrentRoad(Road currentRoad) {
        this.currentRoad = currentRoad.getId();
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public City getTarget() {
        return CityRepository.get().findById(target);
    }

    public void setTarget(City target) {
        this.target = target.getId();
    }

    public ObjectNode toJSON() {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();

        node.put("next", getNext().getId().toHexString());
        node.put("time", getTime());

        List<Road> roads = getRoads();
        ArrayNode roadNodes = factory.arrayNode();

        for (Road road : roads) {
            roadNodes.add(road.getId().toHexString());
        }
        node.put("roads", roadNodes);

        return node;
    }
}
