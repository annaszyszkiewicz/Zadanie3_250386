package edu.ib;

public class DotPlot {

    private boolean[][] matrix;
    private boolean[][] filtration;

    public boolean[][] findPath(String seq1, String seq2, int window, int threshold) {

        int n = seq1.length();
        int m = seq2.length();

        char[] s1 = seq1.toCharArray();
        char[] s2 = seq2.toCharArray();

        matrix = new boolean[n][m];

        for (int i = 0; i <= matrix[0].length; i++) {
            for (int j = 0; j <= matrix.length; j++) {
                if (s1[j] == s2[i]) {
                    matrix[j][i] = true;
                }
            }
        }

        for(int i = 0; i<=m-window; i++){
            for(int j = 0; j<=n-window; j++){
                if(matrix[i][j]){
                    int v = 0;
                    for(int k=0; k<window; k++)
                        if(matrix[i+k][j+k])
                            v ++;
                    if(v>=threshold)
                        filtration[i][j]=true;
                }
            }
        }
        return filtration;
    }
}
