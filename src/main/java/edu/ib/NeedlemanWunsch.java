package edu.ib;

import java.util.ArrayList;

public class NeedlemanWunsch {

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

        memory = new Memory[n + 1][m + 1];

        for (int i = 0; i < memory[0].length; i++) {
            for (int j = 0; j < memory.length; j++) {
                memory[j][i] = new Memory();
            }
        }

        for (int i = 0; i < score[0].length; i++) {
            score[0][i] = i * gap;
            if (i != 0)
                memory[0][i].setMemory(2, true);
        }

        for (int i = 0; i < score.length; i++) {
            score[i][0] = i * gap;
            if (i != 0)
                memory[i][0].setMemory(1, true);
        }

        for (int i = 1; i < score[0].length; i++) {
            for (int j = 1; j < score.length; j++) {

                if (s1[j - 1] == s2[i - 1]) {
                    v1 = score[j - 1][i - 1] + compatibility;
                } else {
                    v1 = score[j - 1][i - 1] + incompatibility;
                }

                v2 = score[j - 1][i] + gap;
                v3 = score[j][i - 1] + gap;

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
            }

        }

        ArrayList<Path> path = new ArrayList<>();

        ArrayList<Integer> oldPathNumber = new ArrayList<>();
        ArrayList<Integer> newPathNumber = new ArrayList<>();

        ArrayList<Integer> xNew = new ArrayList<>();
        ArrayList<Integer> xOld = new ArrayList<>();
        ArrayList<Integer> yNew = new ArrayList<>();
        ArrayList<Integer> yOld = new ArrayList<>();

        path.add(new Path());
        path.get(0).addPoint(n, m);
        oldPathNumber.add(0);
        xOld.add(n);
        yOld.add(m);

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
                                    if (!((x - 1) == 0 && (y - 1) == 0)) {
                                        xNew.add(x - 1);
                                        yNew.add(y - 1);
                                        newPathNumber.add(p);
                                    }
                                    break;
                                case 1:
                                    path.get(p).addPoint(x - 1, y);
                                    if (!((x - 1) == 0 && y == 0)) {
                                        xNew.add(x - 1);
                                        yNew.add(y);
                                        newPathNumber.add(p);
                                    }
                                    break;
                                case 2:
                                    path.get(p).addPoint(x, y - 1);
                                    if (!(x == 0 && (y - 1) == 0)) {
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
                                    if (!((x - 1) == 0 && (y - 1) == 0)) {
                                        xNew.add(x - 1);
                                        yNew.add(y - 1);
                                        newPathNumber.add(path.size() - 1);
                                    }
                                    break;
                                case 1:
                                    path.get(path.size() - 1).addPoint(x - 1, y);
                                    if (!((x - 1) == 0 && y == 0)) {
                                        xNew.add(x - 1);
                                        yNew.add(y);
                                        newPathNumber.add(path.size() - 1);
                                    }
                                    break;
                                case 2:
                                    path.get(path.size() - 1).addPoint(x, y - 1);
                                    if (!(x == 0 && (y - 1) == 0)) {
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

        return path;
    }

    public double getScoreValue() {
        return score[score.length - 1][score[score.length - 1].length - 1];
    }

    public double[][] getScore() {
        return score;
    }

}
