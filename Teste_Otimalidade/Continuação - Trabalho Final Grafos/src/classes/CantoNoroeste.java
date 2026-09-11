package classes;

import static classes.Matriz.criarMatrizZerada;
import static classes.Menor.obterMenor;

public class CantoNoroeste {
    public static int[][] cantoNoroeste(int qtdFornecedores, int qtdConsumidores, int[] capacidade, int[] demanda) {
        int i = 0;
        int j = 0;

        int[][] distribuicaoMatriz = criarMatrizZerada(qtdFornecedores, qtdConsumidores);

        while (i < qtdFornecedores && j < qtdConsumidores) {
            int quantidade = obterMenor(capacidade[i], demanda[j]);
            distribuicaoMatriz[i][j] = quantidade;

            capacidade[i] = capacidade[i] - quantidade;
            demanda[j] = demanda[j] - quantidade;

            if (capacidade[i] == 0) {
                i++;
            }

            if (demanda[j] == 0) {
                j++;
            }
        }
        return distribuicaoMatriz;
    }
}
