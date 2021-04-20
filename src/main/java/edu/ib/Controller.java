package edu.ib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller {

    private String textFile;
    private String sequence1 = "";
    private String sequence2 = "";

    private DotPlot dotPlot;
    private NeedlemanWunsch needlemanWunsch;
    private SmithWaterman smithWaterman;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtComp;

    @FXML
    private TextArea txtInCom;

    @FXML
    private TextArea txtGap;

    @FXML
    private TextArea txtWindow;

    @FXML
    private TextArea txtThreshold;

    @FXML
    private Button btnGenerate;

    @FXML
    private Button btnSaveFile;

    @FXML
    private TextArea txtParam;

    @FXML
    private ChoiceBox<String> choiceAlgorithm;
    private ObservableList<String> algorithm = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<String> choiceMethod;
    private ObservableList<String> method = FXCollections.observableArrayList();

    @FXML
    private TextArea txtSeq1;

    @FXML
    private TextArea txtSeq2;

    @FXML
    private Button btnFile1;

    @FXML
    private Button btnFile2;

    @FXML
    private LineChart<Integer, Integer> chart;

    private void loadingMethod() {
        switch (choiceMethod.getSelectionModel().getSelectedIndex()) {
            case 0 -> {
                btnFile1.setDisable(true);
                btnFile2.setDisable(true);
                txtSeq1.setDisable(false);
                txtSeq2.setDisable(false);
            }
            case 1 -> {
                btnFile1.setDisable(false);
                btnFile2.setDisable(false);
                txtSeq1.setDisable(true);
                txtSeq2.setDisable(true);
            }
            case 2 -> {
                btnFile1.setDisable(true);
                btnFile2.setDisable(true);
                txtSeq1.setDisable(false);
                txtSeq2.setDisable(false);
            }
        }
    }

    private void alignmentAlgorithm() {
        switch (choiceAlgorithm.getSelectionModel().getSelectedIndex()) {
            case 0 -> {
                txtComp.setDisable(true);
                txtInCom.setDisable(true);
                txtGap.setDisable(true);
                txtWindow.setDisable(false);
                txtThreshold.setDisable(false);
            }
            case 1 -> {
                txtComp.setDisable(false);
                txtInCom.setDisable(false);
                txtGap.setDisable(false);
                txtWindow.setDisable(true);
                txtThreshold.setDisable(true);
            }
            case 2 -> {
                txtComp.setDisable(false);
                txtInCom.setDisable(false);
                txtGap.setDisable(false);
                txtWindow.setDisable(true);
                txtThreshold.setDisable(true);
            }
        }
    }

    @FXML
    void onClick(ActionEvent event) {
        dotPlot = new DotPlot();
        needlemanWunsch = new NeedlemanWunsch();
        smithWaterman = new SmithWaterman();

        try {

            switch (choiceMethod.getSelectionModel().getSelectedIndex()) {
                case 0 -> {
                    sequence1 = txtSeq1.getText().toString();
                    sequence2 = txtSeq2.getText().toString();
                }
                case 1 -> {
                    sequence1 = FASTA.read(new File(txtSeq1.getText().toString()));
                    sequence2 = FASTA.read(new File(txtSeq2.getText().toString()));
                }
                case 2 -> {
                    sequence1 = NCBI.getResponse(txtSeq1.getText().toString());
                    sequence2 = NCBI.getResponse(txtSeq2.getText().toString());
                }
            }

            ArrayList<Path> pathList = new ArrayList<>();

            char[] charSequence1 = sequence1.toCharArray();
            char[] charSequence2 = sequence2.toCharArray();

            double score = 0;

            StringBuilder result = new StringBuilder();

            if (choiceAlgorithm.getSelectionModel().getSelectedIndex() == 0) {
                int win = Integer.parseInt(txtWindow.getText().toString());
                int thres = Integer.parseInt(txtThreshold.getText().toString());

                result.append("# 1: ");
                result.append(sequence1);
                result.append("\n");
                result.append("# 2: ");
                result.append(sequence2);
                result.append("\n");
                result.append("# Window: ");
                result.append(Double.parseDouble((txtWindow.getText().toString())));
                result.append("\n");
                result.append("# Threshold: ");
                result.append(Double.parseDouble((txtThreshold.getText().toString())));
                result.append("\n");
                result.append("\n");


            } else {
                double comp = Double.parseDouble(txtComp.getText().toString());
                double incomp = Double.parseDouble(txtInCom.getText().toString());
                double g = Double.parseDouble(txtGap.getText().toString());

                double match = 0;
                match = Double.parseDouble(txtComp.getText().toString());
                double mismatch = 0;
                mismatch = Double.parseDouble(txtInCom.getText().toString());
                double gap = 0;
                gap = Double.parseDouble(txtGap.getText().toString());

                if (choiceAlgorithm.getSelectionModel().getSelectedIndex() == 1) {
                    pathList = needlemanWunsch.findPath(sequence1, sequence2, comp, incomp, g);
                     score = needlemanWunsch.getScoreValue();
                } else {
                    pathList = smithWaterman.findPath(sequence1, sequence2, comp, incomp, g);
                    score = smithWaterman.getScoreValue();
                }

                result.append("# 1: ");
                result.append(sequence1);
                result.append("\n");
                result.append("# 2: ");
                result.append(sequence2);
                result.append("\n");
                result.append("# Match: ");
                result.append(match);
                result.append("\n");
                result.append("# Mismatch: ");
                result.append(mismatch);
                result.append("\n");
                result.append("# Gap: ");
                result.append(gap);
                result.append("\n");
                result.append("# Score: ");
                result.append(score);
                result.append("\n");
                result.append("\n");

                for (int i = 0; i < pathList.size(); i++) {
                    int identity = 0;
                    int gaps = 0;

                    StringBuilder seq1 = new StringBuilder();
                    StringBuilder lines = new StringBuilder();
                    StringBuilder seq2 = new StringBuilder();

                    for (int j = pathList.get(i).size() - 2; j >= 0; j--) {
                        int xLast = pathList.get(i).getPoint(j + 1)[0];
                        int yLast = pathList.get(i).getPoint(j + 1)[1];
                        int x = pathList.get(i).getPoint(j)[0];
                        int y = pathList.get(i).getPoint(j)[1];

                        if (xLast + 1 == x && yLast + 1 == y) {
                            seq1.append(charSequence1[x - 1]);
                            seq2.append(charSequence2[y - 1]);
                            if (charSequence1[x - 1] == charSequence2[y - 1]) {
                                lines.append("|");
                                identity++;
                            } else {
                                lines.append(" ");
                            }
                        } else if (xLast + 1 == x && yLast == y) {
                            seq1.append(charSequence1[x - 1]);
                            seq2.append("-");
                            lines.append(" ");
                            gaps++;
                        } else if (xLast == x && yLast + 1 == y) {
                            seq1.append("-");
                            seq2.append(charSequence2[y - 1]);
                            lines.append(" ");
                            gaps++;
                        }
                    }

                    result.append("# Length: ");
                    result.append(pathList.get(i).size());
                    result.append("\n");

                    result.append("# Identity: ");
                    result.append(identity);
                    result.append("/");
                    result.append(pathList.get(i).size());
                    result.append(" (");
                    result.append(Math.round((double) identity / pathList.get(i).size() * 100));
                    result.append(")");
                    result.append("\n");

                    result.append("# Gaps: ");
                    result.append(gaps);
                    result.append("/");
                    result.append(pathList.get(i).size());
                    result.append(" (");
                    result.append(Math.round((double) gaps / pathList.get(i).size() * 100));
                    result.append(")");
                    result.append("\n");

                    result.append(seq1);
                    result.append("\n");

                    result.append(lines);
                    result.append("\n");

                    result.append(seq2);
                    result.append("\n");
                    result.append("\n");

                    if (i == 0) {
                        txtParam.setText(result.toString());
                    }
                }

                XYChart.Series<Integer, Integer> data = new XYChart.Series<>();
                for (int i = 0; i < pathList.get(0).size(); i++) {
                    data.getData().add(new XYChart.Data<>(pathList.get(0).getPoint(i)[0], -pathList.get(0).getPoint(i)[1]));
                }
                chart.getData().clear();
                chart.getData().add(data);
            }

            textFile = result.toString();




        } catch (NumberFormatException e) {
            txtParam.setText("Nalezy wpisac liczby");
            e.getStackTrace();
        } catch (NullPointerException e) {
            txtParam.setText("Blednie wpisana sekwencja");
            e.getStackTrace();
        } catch (FileNotFoundException e) {
            txtParam.setText("Nie znaleziono pliku");
            e.printStackTrace();
        } catch (IOException e) {
            txtParam.setText("Blad polaczenia z baza");
            e.printStackTrace();
        }
    }

    @FXML
    void onClickFile1(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik FASTA (fasta)", "*.fasta"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*"));
        File file = fileChooser.showOpenDialog(((Button) event.getSource()).getScene().getWindow());
        txtSeq1.setText(file.getAbsolutePath());
    }

    @FXML
    void onClickFile2(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik FASTA (fasta)", "*.fasta"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*"));
        File file = fileChooser.showOpenDialog(((Button) event.getSource()).getScene().getWindow());
        txtSeq2.setText(file.getAbsolutePath());
    }

    @FXML
    void onClickSave(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(((Button) event.getSource()).getScene().getWindow());
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(textFile);
            printWriter.close();
        } catch (IOException | NullPointerException e) {
            txtParam.setText("Błąd zapisu pliku");
        }
    }

    @FXML
    void onClickGraph(ActionEvent event) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        double[][] scores = needlemanWunsch.getScore();
        for (int i = 0; i < scores.length; i++) {
            for (int j = 0; j < scores[i].length; j++) {
                min = Math.min(scores[i][j], min);
                max = Math.max(scores[i][j], max);
            }
        }
        WritableImage image = new WritableImage(500, 500);
        PixelWriter pixelWriter = image.getPixelWriter();
        double r = max - min;
        double h = image.getHeight() / scores.length;
        double w = image.getWidth() / scores[0].length;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int y = (int) (i / h);
                int x = (int) (j / w);

                int color = (int) ((scores[y][x] - min) / r * 255);

                pixelWriter.setColor(j, i, Color.grayRgb(color));
            }
        }
        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.initOwner(((Button) event.getSource()).getParent().getScene().getWindow());
        AnchorPane anchorPane = new AnchorPane();
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        anchorPane.getChildren().add(imageView);
        Scene scene = new Scene(anchorPane, image.getWidth(), image.getHeight());
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void initialize() {
        assert txtComp != null : "fx:id=\"txtComp\" was not injected: check your FXML file 'graph.fxml'.";
        assert txtInCom != null : "fx:id=\"txtInCom\" was not injected: check your FXML file 'graph.fxml'.";
        assert txtGap != null : "fx:id=\"txtGap\" was not injected: check your FXML file 'graph.fxml'.";
        assert txtWindow != null : "fx:id=\"txtWindow\" was not injected: check your FXML file 'graph.fxml'.";
        assert txtThreshold != null : "fx:id=\"txtThreshold\" was not injected: check your FXML file 'graph.fxml'.";
        assert btnGenerate != null : "fx:id=\"btnGenerate\" was not injected: check your FXML file 'graph.fxml'.";
        assert choiceAlgorithm != null : "fx:id=\"choiceAlgorithm\" was not injected: check your FXML file 'graph.fxml'.";
        assert choiceMethod != null : "fx:id=\"choiceMethod\" was not injected: check your FXML file 'graph.fxml'.";
        assert txtSeq1 != null : "fx:id=\"txtSeq1\" was not injected: check your FXML file 'graph.fxml'.";
        assert btnFile1 != null : "fx:id=\"btnFile1\" was not injected: check your FXML file 'graph.fxml'.";
        assert txtSeq2 != null : "fx:id=\"txtSeq2\" was not injected: check your FXML file 'graph.fxml'.";
        assert btnFile2 != null : "fx:id=\"btnFile2\" was not injected: check your FXML file 'graph.fxml'.";
        assert chart != null : "fx:id=\"chart\" was not injected: check your FXML file 'graph.fxml'.";

        algorithm.add("kropkowy");
        algorithm.add("Needlemana-Wunscha");
        algorithm.add("Smitha-Watermana");
        choiceAlgorithm.setItems(algorithm);
        choiceAlgorithm.getSelectionModel().select(0);

        method.add("reczne");
        method.add("z pliku FASTA");
        method.add("z bazy NCBI");
        choiceMethod.setItems(method);
        choiceMethod.getSelectionModel().select(0);

        chart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        chart.setLegendVisible(false);

        choiceMethod.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadingMethod());

        choiceAlgorithm.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> alignmentAlgorithm());
    }
}

