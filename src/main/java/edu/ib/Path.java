package edu.ib;

import java.util.ArrayList;

public class Path {

    private ArrayList<Integer> x = new ArrayList<>();
    private ArrayList<Integer> y = new ArrayList<>();

    public Path() {
    }

    public Path(ArrayList<Integer> x, ArrayList<Integer> y) {
        this.x = new ArrayList<>(x);
        this.y = new ArrayList<>(y);
    }

    public Path(Path path) {
        this.x = new ArrayList<>(path.x);
        this.y = new ArrayList<>(path.y);
    }

    public void addPoint(int x, int y) {
        this.x.add(x);
        this.y.add(y);
    }

    public void deletePoint(int index) {
        this.x.remove(index);
        this.y.remove(index);
    }

    public int[] getPoint(int index) {
        return new int[]{x.get(index), y.get(index)};
    }

    public int size() {
        return x.size();
    }

    public ArrayList<Integer> getX() {
        return x;
    }

    public ArrayList<Integer> getY() {
        return y;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<x.size(); i++){
            stringBuilder.append("(");
            stringBuilder.append(x.get(i));
            stringBuilder.append(",");
            stringBuilder.append(y.get(i));
            stringBuilder.append(")");
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }
}
