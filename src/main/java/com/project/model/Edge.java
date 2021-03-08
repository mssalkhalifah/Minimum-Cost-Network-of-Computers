package com.project.model;

public class Edge implements Comparable<Edge>{
    int weight;
    int source;
    int destination;

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.weight = weight;
        this.destination = destination;
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
