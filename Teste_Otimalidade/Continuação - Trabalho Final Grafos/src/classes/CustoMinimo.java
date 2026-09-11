package classes;

import static classes.Matriz.criarMatrizZerada;
import static classes.Menor.obterMenor;
import static classes.Vetor.criarVetorBoolFalso;

public class CustoMinimo {
    public static int[][] custoMinimo(int qtdFornecedores, int qtdConsumidores, int[] capacidade, int[] demanda, int[][] custos) {
        int[][] distribuicaoMatriz = criarMatrizZerada(qtdFornecedores, qtdConsumidores);

        boolean[] linhaSatisfeita = criarVetorBoolFalso(qtdFornecedores);
        boolean[] colunaSatisfeita = criarVetorBoolFalso(qtdConsumidores);

        int totalAtendido = 0;
        int totalElementos = qtdFornecedores * qtdConsumidores;

        while (totalAtendido < totalElementos) {
            int menorCusto = 999999;
            int linhaMenor = -1;
            int colunaMenor = -1;

            for (int i = 0; i < qtdFornecedores; ++i) {
                if (linhaSatisfeita[i] == true) {
                    continue;
                }

                for (int j = 0; j < qtdConsumidores; ++j) {
                    if (colunaSatisfeita[j] == true) {
                        continue;
                    }

                    if (custos[i][j] < menorCusto) {
                        menorCusto = custos[i][j];
                        linhaMenor = i;
                        colunaMenor = j;
                    }
                }
            }

            if (linhaMenor == -1 || colunaMenor == -1) {
                break;
            }

            int qtdAlocada = obterMenor(capacidade[linhaMenor], demanda[colunaMenor]);
            distribuicaoMatriz[linhaMenor][colunaMenor] = qtdAlocada;

            capacidade[linhaMenor] = capacidade[linhaMenor] - qtdAlocada;
            demanda[colunaMenor] = demanda[colunaMenor] - qtdAlocada;

            if (capacidade[linhaMenor] == 0) {
                linhaSatisfeita[linhaMenor] = true;
            }
            if (demanda[colunaMenor] == 0) {
                colunaSatisfeita[colunaMenor] = true;
            }

            totalAtendido++;
        }

        return distribuicaoMatriz;
    }

}
