package edu.ib.img;

import edu.ib.Path;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PathImage {

    double[][] values;
    private Type type;
    private AnchorPane anchorPane;
    private List<Path> path;

    public enum Type {
        Dot,
        Sequences
    }

    public PathImage(double[][] values, ArrayList<Path> path, AnchorPane anchorPane) {
        this.values = values;
        this.anchorPane = anchorPane;
        this.path = new ArrayList<>(path);
        type = Type.Sequences;
        anchorPane.getChildren().clear();
    }

    public PathImage(boolean[][] values, AnchorPane anchorPane) {
        this.values = new double[values.length][values[0].length];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                if (values[i][j]) {
                    this.values[i][j] = 1;
                }
            }
        }
        this.anchorPane = anchorPane;
        type = Type.Dot;
        anchorPane.getChildren().clear();
    }

    public void generate() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                min = Math.min(values[i][j], min);
                max = Math.max(values[i][j], max);
            }
        }

        int x2 = 0;
        int x3 = 0;
        int x4 = 0;
        int x5 = 0;
        int x6 = 0;
        int x7 = 0;
        int x8 = 0;
        int x9 = 0;

        int y2 = 0;
        int y3 = 0;
        int y4 = 0;
        int y5 = 0;
        int y6 = 0;
        int y7 = 0;
        int y8 = 0;
        int y9 = 0;
        double cellWidth = 0;
        double cellHeight = 0;
        ImageGenerator imageGenerator = new ImageGenerator(900, 600, Color.WHITE);
        cellWidth = (double) imageGenerator.getWidth() / values[0].length;
        cellHeight = (double) imageGenerator.getHeight() / values.length;
        if (type == Type.Dot) {
            for (int i = 0; i < values.length; i++) {
                for (int j = 0; j < values[i].length; j++) {
                    if (values[i][j] == 1)
                        imageGenerator.addDrawable(new Rectangle(j * cellWidth, i * cellHeight, cellWidth + 1, cellHeight + 1, Color.BLUE));
                }
                x9 = values[i].length-1;
                x8 = 7*x9/8;
                x7 = 3*x9/4;
                x6 = 5*x9/8;
                x5 = x9/2;
                x4 = 3*x9/8;
                x3 = x9/4;
                x2 = x9/8;

            }
            y9 = values.length-1;
            y8 = 7*y9/8;
            y7 = 3*y9/4;
            y6 = 5*y9/8;
            y5 = y9/2;
            y4 = 3*y9/8;
            y3 = y9/4;
            y2 = y9/8;
        } else {
            for (int i = 0; i < values.length; i++) {
                for (int j = 0; j < values[i].length; j++) {
                    int r = (int) ((values[i][j] - min) / (max - min) * 255);
                    int g = (int) ((values[i][j] - min) / (max - min) * 255);
                    int b = 255;
                    imageGenerator.addDrawable(new Rectangle(j * cellWidth, i * cellHeight, cellWidth + 1, cellHeight + 1, Color.rgb(r, g, b)));

                }
                x9 = values[i].length-1;
                x8 = (int) Math.ceil(7*x9/8);
                x7 = 3*x9/4;
                x6 = 5*x9/8;
                x5 = x9/2;
                x4 = 3*x9/8;
                x3 = x9/4;
                x2 = x9/8;

            }
            y9 = values.length-1;
            y8 = 7*y9/8;
            y7 = 3*y9/4;
            y6 = 5*y9/8;
            y5 = y9/2;
            y4 = 3*y9/8;
            y3 = y9/4;
            y2 = y9/8;
        }
        int temp = 550;
        if (type == Type.Dot) {
            temp = 100;
        }
        ImageGenerator legend = new ImageGenerator(100, temp, Color.WHITE);
        if (type == Type.Sequences) {
            legend.addDrawable(new Rectangle(0, 0, 100, 50, Color.WHITE));
        } else {
            legend.addDrawable(new Rectangle(0, 0, 100, 50, Color.BLUE));
        }
        Text maxValue = new Text(1250, 125, String.format("%.2f", max));
        if (type == Type.Sequences) {
            int r1 = (int) (((max - min) * 0.9) / (max - min) * 255);
            String s1 = String.format("%.2f", ((max - min) * 0.9 + min));
            legend.addDrawable(new Rectangle(0, 50, 100, 50, Color.rgb(r1, r1, 255)));
            Text secondValue = new Text(1250, 175, s1);
            anchorPane.getChildren().add(secondValue);
            int r2 = (int) (((max - min) * 0.8) / (max - min) * 255);
            String s2 = String.format("%.2f", ((max - min) * 0.8 + min));
            legend.addDrawable(new Rectangle(0, 100, 100, 50, Color.rgb(r2, r2, 255)));
            Text thirdValue = new Text(1250, 225, s2);
            anchorPane.getChildren().add(thirdValue);
            int r3 = (int) (((max - min) * 0.7) / (max - min) * 255);
            String s3 = String.format("%.2f", ((max - min) * 0.7 + min));
            legend.addDrawable(new Rectangle(0, 150, 100, 50, Color.rgb(r3, r3, 255)));
            Text fourthValue = new Text(1250, 275, s3);
            anchorPane.getChildren().add(fourthValue);
            int r4 = (int) (((max - min) * 0.6) / (max - min) * 255);
            String s4 = String.format("%.2f", ((max - min) * 0.6 + min));
            legend.addDrawable(new Rectangle(0, 200, 100, 50, Color.rgb(r4, r4, 255)));
            Text fifthValue = new Text(1250, 325, s4);
            anchorPane.getChildren().add(fifthValue);
            int r5 = (int) (((max - min) * 0.5) / (max - min) * 255);
            String s5 = String.format("%.2f", ((max - min) * 0.5 + min));
            legend.addDrawable(new Rectangle(0, 250, 100, 50, Color.rgb(r5, r5, 255)));
            Text sixthValue = new Text(1250, 375, s5);
            anchorPane.getChildren().add(sixthValue);
            int r6 = (int) (((max - min) * 0.4) / (max - min) * 255);
            String s6 = String.format("%.2f", ((max - min) * 0.4 + min));
            legend.addDrawable(new Rectangle(0, 300, 100, 50, Color.rgb(r6, r6, 255)));
            Text seventhValue = new Text(1250, 425, s6);
            anchorPane.getChildren().add(seventhValue);
            int r7 = (int) (((max - min) * 0.3) / (max - min) * 255);
            String s7 = String.format("%.2f", ((max - min) * 0.3 + min));
            legend.addDrawable(new Rectangle(0, 350, 100, 50, Color.rgb(r7, r7, 255)));
            Text eighthValue = new Text(1250, 475, s7);
            anchorPane.getChildren().add(eighthValue);
            int r8 = (int) (((max - min) * 0.2) / (max - min) * 255);
            String s8 = String.format("%.2f", ((max - min) * 0.2 + min));
            legend.addDrawable(new Rectangle(0, 400, 100, 50, Color.rgb(r8, r8, 255)));
            Text ninthValue = new Text(1250, 525, s8);
            anchorPane.getChildren().add(ninthValue);
            int r9 = (int) (((max - min) * 0.1) / (max - min) * 255);
            String s9 = String.format("%.2f", ((max - min) * 0.1 + min));
            legend.addDrawable(new Rectangle(0, 450, 100, 50, Color.rgb(r9, r9, 255)));
            Text tenthValue = new Text(1250, 575, s9);
            anchorPane.getChildren().add(tenthValue);
        }

        Text minValue;
        if (type == Type.Sequences) {
            minValue = new Text(1250, 625, String.format("%.2f", min));
            legend.addDrawable(new Rectangle(0, 500, 100, 50, Color.BLUE));
        } else {
            minValue = new Text(1250, 175, String.format("%.2f", min));
            legend.addDrawable(new Rectangle(0, 50, 100, 50, Color.WHITE));
        }
        anchorPane.getChildren().add(minValue);

        ImageView imageView = new ImageView();
        imageView.setX(100);
        imageView.setY(100);
        if (type == Type.Sequences) {
            for (int j = 0; j < path.size(); j++) {
                Random random = new Random();
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                for (int i = 1; i < path.get(j).size(); i++) {
                    int[] p1 = path.get(j).getPoint(i - 1);
                    int[] p2 = path.get(j).getPoint(i);
                    imageGenerator.addDrawable(new Line(0.5 * cellWidth + p1[1] * cellWidth, 0.5 * cellHeight + p1[0] * cellHeight, 0.5 * cellWidth + p2[1] * cellWidth, 0.5 * cellHeight + p2[0] * cellHeight, Color.rgb(r, g, b)));
                }
            }
        }

        imageView.setImage(imageGenerator.generateImage());
        ImageView legendView = new ImageView();
        legendView.setX(1100);
        legendView.setY(100);
        legendView.setImage(legend.generateImage());
        anchorPane.getChildren().add(new Text(500, 40, "Sequence 2"));
        anchorPane.getChildren().add(new Text(100 + cellWidth/2, 80, "0"));
        anchorPane.getChildren().add(new Text(100 + cellWidth/2 + (imageGenerator.getWidth()-cellWidth)/8.0 - cellWidth/5, 80, String.valueOf(x2)));
        anchorPane.getChildren().add(new Text(100 + cellWidth/2 + (imageGenerator.getWidth()-cellWidth)/4.0 - cellWidth/5, 80, String.valueOf(x3)));
        anchorPane.getChildren().add(new Text(100 + cellWidth/2 + 3*(imageGenerator.getWidth()-cellWidth)/8.0 - cellWidth/5, 80, String.valueOf(x4)));
        anchorPane.getChildren().add(new Text(100 + cellWidth/2 + (imageGenerator.getWidth()-cellWidth)/2.0 - cellWidth/5, 80, String.valueOf(x5)));
        anchorPane.getChildren().add(new Text(100 + cellWidth/2 + 5*(imageGenerator.getWidth()-cellWidth)/8.0 - cellWidth/5, 80, String.valueOf(x6)));
        anchorPane.getChildren().add(new Text(100 + cellWidth/2 + 3*(imageGenerator.getWidth()-cellWidth)/4.0 - cellWidth/5, 80, String.valueOf(x7)));
        anchorPane.getChildren().add(new Text(100 + cellWidth/2 + 7*(imageGenerator.getWidth()-cellWidth)/8.0 - cellWidth/5, 80, String.valueOf(x8)));
        anchorPane.getChildren().add(new Text(100 + imageGenerator.getWidth() - cellWidth/2.0, 80, String.valueOf(x9)));


        anchorPane.getChildren().add(new Text(70, 100 + cellHeight/2, "0"));
        anchorPane.getChildren().add(new Text(70, 100 + cellHeight/2 + (imageGenerator.getHeight()-cellHeight)/8.0, String.valueOf(y2)));
        anchorPane.getChildren().add(new Text(70, 100 + cellHeight/2 + (imageGenerator.getHeight()-cellHeight)/4.0, String.valueOf(y3)));
        anchorPane.getChildren().add(new Text(70, 100 + cellHeight/2 + 3*(imageGenerator.getHeight()-cellHeight)/8.0, String.valueOf(y4)));
        anchorPane.getChildren().add(new Text(70, 100 + cellHeight/2 + (imageGenerator.getHeight()-cellHeight)/2.0, String.valueOf(y5)));
        anchorPane.getChildren().add(new Text(70, 100 + cellHeight/2 + 5*(imageGenerator.getHeight()-cellHeight)/8.0, String.valueOf(y6)));
        anchorPane.getChildren().add(new Text(70, 100 + cellHeight/2 + 3*(imageGenerator.getHeight()-cellHeight)/4.0, String.valueOf(y7)));
        anchorPane.getChildren().add(new Text(70, 100 + cellHeight/2 + 7*(imageGenerator.getHeight()-cellHeight)/8.0, String.valueOf(y8)));
        anchorPane.getChildren().add(new Text(70, 100 + imageGenerator.getHeight() - cellHeight/2.0, String.valueOf(y9)));
        Text text2 = new Text(0, 400, "Sequence 1");
        text2.rotateProperty().setValue(270);
        anchorPane.getChildren().add(text2);
        anchorPane.getChildren().add(imageView);
        anchorPane.getChildren().add(maxValue);
        anchorPane.getChildren().add(legendView);

    }


}
