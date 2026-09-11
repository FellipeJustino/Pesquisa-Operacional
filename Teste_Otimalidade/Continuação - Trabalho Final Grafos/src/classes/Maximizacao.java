package classes;

public class Maximizacao {

    // Caso especial: problema de maximizacao.
    // Os metodos (Canto Noroeste, Custo Minimo, Vogel, MODI) so sabem minimizar custo.

    public static int[][] converterParaMinimizacao(int[][] custos, int qtdFornecedores, int qtdConsumidores) {
        int maiorValor = custos[0][0];

        for (int i = 0; i < qtdFornecedores; ++i) {
            for (int j = 0; j < qtdConsumidores; ++j) {
                if (custos[i][j] > maiorValor) {
                    maiorValor = custos[i][j];
                }
            }
        }

        int[][] convertido = new int[qtdFornecedores][qtdConsumidores];
        for (int i = 0; i < qtdFornecedores; ++i) {
            for (int j = 0; j < qtdConsumidores; ++j) {
                convertido[i][j] = maiorValor - custos[i][j];
            }
        }

        return convertido;
    }
}
