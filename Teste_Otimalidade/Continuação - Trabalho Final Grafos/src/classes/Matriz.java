package classes;

public class Matriz {

    public static int[][] criarMatrizZerada(int linhas, int colunas) {
        int[][] matriz = new int[linhas][colunas];
        for (int i = 0; i < linhas; ++i) {
            for (int j = 0; j < colunas; ++j) {
                matriz[i][j] = 0;
            }
        }
        return matriz;
    }

    public static int[][] copiarMatriz(int[][] matriz, int linhas, int colunas) {
        int[][] copia = new int[linhas][colunas];
        for (int i = 0; i < linhas; ++i) {
            System.arraycopy(matriz[i], 0, copia[i], 0, colunas);
        }
        return copia;
    }

    public static void imprimirMatriz(int[][] matriz, int qtdLinhas, int qtdColunas) {
        System.out.println("Matriz de Alocacao:");
        for (int i = 0; i < qtdLinhas; ++i) {
            for (int j = 0; j < qtdColunas; ++j) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }

}
