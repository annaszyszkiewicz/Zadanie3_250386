package edu.ib.img;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ImageGenerator {

    private final int width;
    private final int height;
    private Color background;

    private final ArrayList<DrawableObject> drawableObjectArrayList=new ArrayList<>();

    public ImageGenerator(int width, int height, Color background){
        this.width=width;
        this.height=height;
        this.background=background;
    }

    public WritableImage generateImage(){
        WritableImage image=new WritableImage(width, height);
        PixelWriter writer=image.getPixelWriter();
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                writer.setColor(i,j,background);
            }
        }
        for(int i=0;i<drawableObjectArrayList.size();i++){
            drawableObjectArrayList.get(i).draw(image);
        }
        return image;
    }

    public void addDrawable(DrawableObject drawableObject){
        drawableObjectArrayList.add(drawableObject);
    }

    public void addDrawable(int index,DrawableObject drawableObject){
        drawableObjectArrayList.add(index,drawableObject);
    }

    public DrawableObject getDrawable(int index){
        return drawableObjectArrayList.get(index);
    }

    public int indexOfDrawable(DrawableObject drawableObject){
        return drawableObjectArrayList.indexOf(drawableObject);
    }

    public void removeDrawable(int index){
        drawableObjectArrayList.remove(index);
    }

    public void removeDrawable(DrawableObject drawableObject){
        drawableObjectArrayList.remove(drawableObject);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }
}
