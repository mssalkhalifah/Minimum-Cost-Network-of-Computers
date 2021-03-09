package com.project.model;

public class Edge implements Comparable<Edge>{
    private final int weight;
    private final int source;
    private final int destination;

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.weight = weight;
        this.destination = destination;
    }

    public int getWeight() {
        return weight;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    @Override
    public int compareTo(Edge compareEdge) {
        return this.weight - compareEdge.weight;
    }

    @Override
    public String toString() {
        return String.format("%d<-%d->%d", source, weight, destination);
    }
}
