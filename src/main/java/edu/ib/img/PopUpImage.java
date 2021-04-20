package edu.ib.img;

import edu.ib.Path;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PopUpImage {

    double[][] values;
    private final int width;
    private final int height;
    private Type type;
    private Window window;
    private Path path;

    public enum Type {
        Dot,
        Sequences
    }

    public PopUpImage(double[][] values, Path path, int width, int height, Window window) {
        this.values = values;
        this.width = width;
        this.height = height;
        this.window = window;
        this.path = path;
        type = Type.Sequences;
    }

    public PopUpImage(boolean[][] values, int width, int height, Window window) {
        this.values = new double[values.length][values[0].length];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                if (values[i][j]) {
                    this.values[i][j] = 1;
                }
            }
        }
        this.width = width;
        this.height = height;
        this.window = window;
        type = Type.Dot;
    }

    public void generate(String seq1, String seq2) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.setFullScreen(true);
        dialog.initOwner(window);
        AnchorPane dialogVbox = new AnchorPane();
        Text s1=new Text();
        s1.setText("1: "+seq1);
        s1.setX(5);
        s1.setY(50);
        Text s2=new Text();
        s2.setText("2: "+seq2);
        s2.setX(5);
        s2.setY(75);
        dialogVbox.getChildren().add(s1);
        dialogVbox.getChildren().add(s2);
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                min = Math.min(values[i][j], min);
                max = Math.max(values[i][j], max);
            }
        }
        ImageGenerator imageGenerator = new ImageGenerator(1000, 700, Color.WHITE);
        double cellWidth = (double) imageGenerator.getWidth() / values[0].length;
        double cellHeight = (double) imageGenerator.getHeight() / values.length;
        if (type == Type.Dot) {
            for (int i = 0; i < values.length; i++) {
                for (int j = 0; j < values[i].length; j++) {
                    if (values[i][j] == 1)
                        imageGenerator.addDrawable(new Rectangle(j * cellWidth, i * cellHeight, cellWidth + 1, cellHeight + 1, Color.BLACK));
                }
            }
        } else {
            for (int i = 0; i < values.length; i++) {
                for (int j = 0; j < values[i].length; j++) {
                    int color = (int) ((values[i][j] - min) / (max - min) * 255);
                    imageGenerator.addDrawable(new Rectangle(j * cellWidth, i * cellHeight, cellWidth + 1, cellHeight + 1, Color.grayRgb(color)));
                }
            }
        }
        int temp = 250;
        if (type == Type.Dot) {
            temp = 100;
        }
        ImageGenerator legend = new ImageGenerator(100, temp, Color.WHITE);
        if (type == Type.Sequences) {
            legend.addDrawable(new Rectangle(0, 0, 100, 50, Color.WHITE));
        } else {
            legend.addDrawable(new Rectangle(0, 0, 100, 50, Color.BLACK));
        }
        Text maxValue = new Text(1325, 125, String.valueOf(max));
        if (type == Type.Sequences) {
            int c1 = (int) (((max - min) * 0.75) / (max - min) * 255);
            legend.addDrawable(new Rectangle(0, 50, 100, 50, Color.grayRgb(c1)));
            Text secondValue = new Text(1325, 175, String.valueOf(((max - min) * 0.75 + min)));
            dialogVbox.getChildren().add(secondValue);
            int c2 = (int) (((max - min) * 0.5) / (max - min) * 255);
            legend.addDrawable(new Rectangle(0, 100, 100, 50, Color.grayRgb(c2)));
            Text thirdValue = new Text(1325, 225, String.valueOf(((max - min) * 0.5 + min)));
            dialogVbox.getChildren().add(thirdValue);
            int c3 = (int) (((max - min) * 0.25) / (max - min) * 255);
            legend.addDrawable(new Rectangle(0, 150, 100, 50, Color.grayRgb(c3)));
            Text fourthValue = new Text(1325, 275, String.valueOf(((max - min) * 0.25 + min)));
            dialogVbox.getChildren().add(fourthValue);
        }

        Text minValue;
        if (type == Type.Sequences) {
            minValue = new Text(1325, 325, String.valueOf(min));
            legend.addDrawable(new Rectangle(0, 200, 100, 50, Color.BLACK));
        } else {
            minValue = new Text(1325, 175, String.valueOf(min));
            legend.addDrawable(new Rectangle(0, 50, 100, 50, Color.WHITE));
        }
        dialogVbox.getChildren().add(minValue);

        ImageView imageView = new ImageView();
        imageView.setX(100);
        imageView.setY(100);
        if (type == Type.Sequences) {
            for (int i = 1; i < path.size(); i++) {
                int[] p1 = path.getPoint(i - 1);
                int[] p2 = path.getPoint(i);
                imageGenerator.addDrawable(new Line(0.5 * cellWidth + p1[0] * cellWidth, 0.5 * cellHeight + p1[1] * cellHeight, 0.5 * cellWidth + p2[0] * cellWidth, 0.5 * cellHeight + p2[1] * cellHeight, Color.DARKGREEN));
            }
        }

        imageView.setImage(imageGenerator.generateImage());
        ImageView legendView = new ImageView();
        legendView.setX(1200);
        legendView.setY(100);
        legendView.setImage(legend.generateImage());
        dialogVbox.getChildren().add(new Text(600, 90, "Sequence 2"));
        Text text2 = new Text(0, 600, "Sequence 1");
        text2.rotateProperty().setValue(270);
        dialogVbox.getChildren().add(text2);
        dialogVbox.getChildren().add(imageView);
        dialogVbox.getChildren().add(maxValue);


        dialogVbox.getChildren().add(legendView);
        Scene dialogScene = new Scene(dialogVbox, width, height);
        dialog.setScene(dialogScene);
        dialog.show();
    }


}
