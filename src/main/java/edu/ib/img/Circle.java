package edu.ib.img;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Circle implements DrawableObject {
    private Color color;
    private int x;
    private int y;
    private int r;

    public Circle(int x, int y, int r, Color color) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public void draw(WritableImage image) {
        PixelWriter writer=image.getPixelWriter();
        for(int i=x-r;i<x+r;i++){
            for(int j=y-r;j<y+r;j++){
                if(i>=0 && i<image.getWidth() && j>=0 && j<image.getHeight()){
                    if((x-i)*(x-i)+(y-j)*(y-j)<=r*r){
                        writer.setColor(i,j,color);
                    }
                }
            }
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }


}
