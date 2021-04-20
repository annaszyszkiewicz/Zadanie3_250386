package edu.ib.img;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Rectangle implements DrawableObject {

    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;

    public Rectangle(double x, double y, double width, double height, Color color){
        if(width<=0) throw new IllegalArgumentException("width="+width+" can't be lower or equal 0");
        if(height<=0) throw new IllegalArgumentException("height="+height+" can't be lower or equal 0");
        this.x=(int)x;
        this.y=(int)y;
        this.width=(int)width;
        this.height=(int)height;
        this.color=color;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(WritableImage image) {
        PixelWriter pixelWriter=image.getPixelWriter();
        for(int i=x;i<x+width;i++){
            for(int j=y;j<y+height;j++){
                if(i>=0 && j>=0 && i<image.getWidth() && j<image.getHeight()) pixelWriter.setColor(i,j,color);
            }
        }
    }

}
