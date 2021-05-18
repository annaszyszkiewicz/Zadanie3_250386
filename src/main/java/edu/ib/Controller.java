package edu.ib;

import edu.ib.img.PathImage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

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
    private AnchorPane anchorePane;


    private void loadingMethod() {
        switch (choiceMethod.getSelectionModel().getSelectedIndex()) {
            case 0, 2 -> {
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
            case 1, 2 -> {
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

            String s1 = "-" + sequence1;
            String s2 = "-" + sequence2;
            char[] charSequence1 = s1.toCharArray();
            char[] charSequence2 = s2.toCharArray();

            double score = 0;

            StringBuilder result = new StringBuilder();

            if (choiceAlgorithm.getSelectionModel().getSelectedIndex() == 0) {
                int win = Integer.parseInt(txtWindow.getText().toString());
                int thres = Integer.parseInt(txtThreshold.getText().toString());

                dotPlot.findPath(sequence1, sequence2, win, thres);

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

                txtParam.setText(result.toString());

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
                                seq1.append(charSequence1[x]);
                                seq2.append(charSequence2[y]);
                                if (charSequence1[x] == charSequence2[y]) {
                                    lines.append("|");
                                    identity++;
                                } else {
                                    lines.append(" ");
                                }
                            } else if (xLast + 1 == x && yLast == y) {
                                seq1.append(charSequence1[x]);
                                seq2.append("-");
                                lines.append(" ");
                                gaps++;
                            } else if (xLast == x && yLast + 1 == y) {
                                seq1.append("-");
                                seq2.append(charSequence2[y]);
                                lines.append(" ");
                                gaps++;
                            }
                        }

                        result.append("# Length: ");
                        result.append(pathList.get(i).size() - 1);
                        result.append("\n");

                        result.append("# Identity: ");
                        result.append(identity);
                        result.append("/");
                        result.append(pathList.get(i).size() - 1);
                        result.append(" (");
                        result.append(Math.round((double) identity / (pathList.get(i).size() - 1) * 100));
                        result.append(")");
                        result.append("\n");

                        result.append("# Gaps: ");
                        result.append(gaps);
                        result.append("/");
                        result.append(pathList.get(i).size() - 1);
                        result.append(" (");
                        result.append(Math.round((double) gaps / (pathList.get(i).size() - 1) * 100));
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

                    textFile = result.toString();

                } else {
                    pathList = smithWaterman.findPath(sequence1, sequence2, comp, incomp, g);
                    score = smithWaterman.getScoreValue();

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

                        int l = pathList.get(i).size();

                        ArrayList<Path> temp = new ArrayList<>();
                        for (int k = 0; k < pathList.size(); k++) {
                            temp.add(new Path(pathList.get(k)));
                        }

                        if (temp.get(i).getPoint(temp.get(i).size() - 1)[0] == 0 && temp.get(i).getPoint(temp.get(i).size() - 1)[1] == 0) {
                            l--;
                        } else {
                            temp.get(i).addPoint(0, 0);
                        }

                        for (int j = temp.get(i).size() - 2; j >= 0; j--) {
                            int xLast = temp.get(i).getPoint(j + 1)[0];
                            int yLast = temp.get(i).getPoint(j + 1)[1];
                            int x = temp.get(i).getPoint(j)[0];
                            int y = temp.get(i).getPoint(j)[1];

                            if (xLast + 1 == x && yLast == y) {
                                seq1.append(charSequence1[x]);
                                seq2.append("-");
                                lines.append(" ");
                                gaps++;
                            } else if (xLast == x && yLast + 1 == y) {
                                seq1.append("-");
                                seq2.append(charSequence2[y]);
                                lines.append(" ");
                                gaps++;
                            } else {
                                if (y >= 0)
                                    seq1.append(charSequence1[x]);
                                else
                                    seq1.append("-");

                                if (x >= 0)
                                    seq2.append(charSequence2[y]);
                                else
                                    seq2.append("-");

                                if (y - 1 >= 0 && x - 1 >= 0) {
                                    if (charSequence1[x] == charSequence2[y]) {
                                        identity++;
                                        lines.append("|");
                                    } else {
                                        lines.append(" ");
                                    }
                                } else {
                                    lines.append(" ");
                                }
                            }
                        }

                        result.append("# Length: ");
                        result.append(l);
                        result.append("\n");

                        result.append("# Identity: ");
                        result.append(identity);
                        result.append("/");
                        result.append(l);
                        result.append(" (");
                        result.append(Math.round((double) identity / l * 100));
                        result.append(")");
                        result.append("\n");

                        result.append("# Gaps: ");
                        result.append(gaps);
                        result.append("/");
                        result.append(l);
                        result.append(" (");
                        result.append(Math.round((double) gaps / l * 100));
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

                        textFile = result.toString();

                    }
                }
            }

            ArrayList<Path> pathTemp;
            if (pathList.size() < 8) {
                pathTemp = new ArrayList<>(pathList);
            } else {
                pathTemp = new ArrayList<>();
                pathTemp.add(pathList.get(0));
            }

            PathImage pathImage;
            if (choiceAlgorithm.getSelectionModel().getSelectedIndex() == 0) {
                pathImage = new PathImage(dotPlot.getFiltration(), anchorePane);
            } else if (choiceAlgorithm.getSelectionModel().getSelectedIndex() == 1) {
                pathImage = new PathImage(needlemanWunsch.getScore(), pathTemp, anchorePane);
            } else if (choiceAlgorithm.getSelectionModel().getSelectedIndex() == 2) {
                pathImage = new PathImage(smithWaterman.getScore(), pathTemp, anchorePane);
            } else {
                throw new IllegalArgumentException();
            }

            pathImage.generate();

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
        assert anchorePane != null : "fx:id=\"anchorePane\" was not injected: check your FXML file 'graph.fxml'.";

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


        choiceMethod.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadingMethod());

        choiceAlgorithm.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> alignmentAlgorithm());
    }
}

