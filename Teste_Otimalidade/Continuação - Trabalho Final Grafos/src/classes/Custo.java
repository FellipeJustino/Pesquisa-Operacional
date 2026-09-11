package classes;

public class Custo {
    public static int calcularCustoTotal(int[][] distribuicaoMatriz, int[][] custos, int qtdLinhas, int qtdColunas) {
        int Z = 0;
        for (int i = 0; i < qtdLinhas; ++i) {
            for (int j = 0; j < qtdColunas; ++j) {
                if (distribuicaoMatriz[i][j] > 0) {
                    Z += distribuicaoMatriz[i][j] * custos[i][j];
                }
            }
        }
        return Z;
    }
}
