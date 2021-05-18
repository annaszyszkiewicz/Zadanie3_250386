package edu.ib;

import java.util.ArrayList;

public class SmithWaterman {

    private double[][] score;
    private Memory[][] memory;
    public final int MAX_PATHS = 100;

    public ArrayList<Path> findPath(String seq1, String seq2, double compatibility, double incompatibility, double gap) {
        int n = seq1.length();
        int m = seq2.length();

        char[] s1 = seq1.toCharArray();
        char[] s2 = seq2.toCharArray();

        score = new double[n + 1][m + 1];

        double v1 = 0;
        double v2 = 0;
        double v3 = 0;

        double maxValue = Double.MIN_VALUE;

        memory = new Memory[n + 1][m + 1];

        for (int i = 0; i < memory[0].length; i++) {
            for (int j = 0; j < memory.length; j++) {
                memory[j][i] = new Memory();
            }
        }

        for (int i = 0; i < score[0].length; i++) {
            score[0][i] = 0;
            if (i != 0)
                memory[0][i].setMemory(2, true);
        }

        for (int i = 0; i < score.length; i++) {
            score[i][0] = 0;
            if (i != 0)
                memory[i][0].setMemory(1, true);
        }

        for (int i = 1; i < score[0].length; i++) {
            for (int j = 1; j < score.length; j++) {

                if (s1[j - 1] == s2[i - 1]) {
                    v1 = score[j - 1][i - 1] + compatibility;
                    if (v1 < 0)
                        v1 = 0;
                } else {
                    v1 = score[j - 1][i - 1] + incompatibility;
                    if (v1 < 0)
                        v1 = 0;
                }

                v2 = score[j - 1][i] + gap;
                if (v2 < 0)
                    v2 = 0;
                v3 = score[j][i - 1] + gap;
                if (v3 < 0)
                    v3 = 0;

                double max = Math.max(v3, (Math.max(v1, v2)));

                if (max == v1) {
                    memory[j][i].setMemory(0, true);
                }

                if (max == v2) {
                    memory[j][i].setMemory(1, true);
                }

                if (max == v3) {
                    memory[j][i].setMemory(2, true);
                }
                score[j][i] = max;

                maxValue = Math.max(max, maxValue);
            }

        }

        ArrayList<Path> path = new ArrayList<>();

        ArrayList<Integer> oldPathNumber = new ArrayList<>();
        ArrayList<Integer> newPathNumber = new ArrayList<>();

        ArrayList<Integer> xNew = new ArrayList<>();
        ArrayList<Integer> xOld = new ArrayList<>();
        ArrayList<Integer> yNew = new ArrayList<>();
        ArrayList<Integer> yOld = new ArrayList<>();

        for (int i = 0; i < score.length; i++) {
            for (int j = 0; j < score[i].length; j++) {
                if (score[i][j] == maxValue) {
                    path.add(new Path());
                    path.get(path.size() - 1).addPoint(i, j);
                    oldPathNumber.add(path.size() - 1);
                    xOld.add(i);
                    yOld.add(j);

                }
            }
        }

        do {
            for (int i = 0; i < xOld.size(); i++) {
                int x = xOld.get(i);
                int y = yOld.get(i);
                int p = oldPathNumber.get(i);

                Memory mem = memory[x][y];
                int count = 0;
                for (int j = 0; j < 3; j++) {
                    if (mem.getMemory(j)) {
                        count++;

                        if (count == 1) {
                            switch (j) {
                                case 0:
                                    path.get(p).addPoint(x - 1, y - 1);
                                    if (!(score[x - 1][y - 1] == 0)) {
                                        xNew.add(x - 1);
                                        yNew.add(y - 1);
                                        newPathNumber.add(p);
                                    }
                                    break;
                                case 1:
                                    path.get(p).addPoint(x - 1, y);
                                    if (!(score[x - 1][y] == 0)) {
                                        xNew.add(x - 1);
                                        yNew.add(y);
                                        newPathNumber.add(p);
                                    }
                                    break;
                                case 2:
                                    path.get(p).addPoint(x, y - 1);
                                    if (!(score[x][y - 1] == 0)) {
                                        xNew.add(x);
                                        yNew.add(y - 1);
                                        newPathNumber.add(p);
                                    }
                            }
                        } else if (path.size() < MAX_PATHS) {
                            path.add(new Path(path.get(p)));
                            path.get(path.size() - 1).deletePoint(path.get(path.size() - 1).size() - 1);
                            switch (j) {
                                case 0:
                                    path.get(path.size() - 1).addPoint(x - 1, y - 1);
                                    if (!(score[x - 1][y - 1] == 0)) {
                                        xNew.add(x - 1);
                                        yNew.add(y - 1);
                                        newPathNumber.add(path.size() - 1);
                                    }
                                    break;
                                case 1:
                                    path.get(path.size() - 1).addPoint(x - 1, y);
                                    if (!(score[x - 1][y] == 0)) {
                                        xNew.add(x - 1);
                                        yNew.add(y);
                                        newPathNumber.add(path.size() - 1);
                                    }
                                    break;
                                case 2:
                                    path.get(path.size() - 1).addPoint(x, y - 1);
                                    if (!(score[x][y - 1] == 0)) {
                                        xNew.add(x);
                                        yNew.add(y - 1);
                                        newPathNumber.add(path.size() - 1);
                                    }
                            }
                        }
                    }
                }
            }
            xOld = xNew;
            yOld = yNew;
            oldPathNumber = newPathNumber;
            xNew = new ArrayList<>();
            yNew = new ArrayList<>();
            newPathNumber = new ArrayList<>();

        } while (!xOld.isEmpty());

        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();

        for (int i = 0; i < path.size(); i++) {
            for (int j = 1; j < path.get(i).size(); j++) {
                int[] xy = path.get(i).getPoint(j);
                if (score[xy[0]][xy[1]] == maxValue) {
                    x.add(xy[0]);
                    y.add(xy[1]);
                }
            }
        }

        for (int i = path.size() - 1; i >= 0; i--) {
            int[] start = path.get(i).getPoint(0);
            for (int j = 0; j < x.size(); j++) {
                if (start[0] == x.get(j) && start[1] == y.get(j)) {
                    path.remove(i);
                    break;
                }
            }
        }
        return path;
    }

    public double getScoreValue() {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < score.length; i++) {
            for (int j = 0; j < score[i].length; j++) {
                max = Math.max(max, score[i][j]);
            }
        }
        return max;
    }

    public double[][] getScore() {
        return score;
    }

}

