package edu.ib;

public class Memory {

    boolean[] temp;

    public Memory() {
        temp = new boolean[3];
    }

    public boolean getMemory(int index) {
        return temp[index];
    }

    public void setMemory(int index, boolean value) {
        temp[index] = value;
    }
}
