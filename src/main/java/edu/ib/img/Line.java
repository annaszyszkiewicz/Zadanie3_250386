package edu.ib.img;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Line implements DrawableObject {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private Color color;

    public Line(double x1, double y1, double x2, double y2, Color color) {
        this.x1 = (int)x1;
        this.y1 = (int)y1;
        this.x2 = (int)x2;
        this.y2 = (int)y2;
        this.color = color;
    }

    @Override
    public void draw(WritableImage image) {
        PixelWriter writer=image.getPixelWriter();
        if(x1==x2){
            for(int i=Math.min(y1,y2);i<=Math.max(y1,y2);i++){
                if(i>=0 && i<image.getHeight()){
                    writer.setColor(x1,i,color);
                    writer.setColor(x1-1,i,color);
                    writer.setColor(x1+1,i,color);
                }
            }
        } else if(y1==y2){
            for(int i=Math.min(x1,x2);i<=Math.max(x1,x2);i++){
                if(i>=0 && i<image.getWidth() && y1-1>=0 && y1+1<image.getHeight()){
                    writer.setColor(i,y1,color);
                    writer.setColor(i,y1-1,color);
                    writer.setColor(i,y1+1,color);
                }
            }
        } else {
            for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
                int y=(int)((double)(y1-y2)/(x1-x2)*i+y1-(double)(y1-y2)/(x1-x2)*x1);
                if(i-1>0 && y-1>0 && i+1<image.getWidth() && y+1<image.getHeight()) {
                    writer.setColor(i, y, color);
                    writer.setColor(i, y + 1, color);
                    writer.setColor(i + 1, y, color);
                    writer.setColor(i, y - 1, color);
                    writer.setColor(i - 1, y, color);
                }
            }
        }
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
