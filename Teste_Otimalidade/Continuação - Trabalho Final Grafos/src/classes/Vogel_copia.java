package classes;

import static classes.Matriz.criarMatrizZerada;
import static classes.Menor.obterMenor;
import static classes.Vetor.criarVetorBoolFalso;

public class Vogel_copia {

    public static int calcularPenalidadeLinha(int i, int qtdConsumidores, int[][] custos, boolean[] colunaSatisfeita) {
        int menor1 = 999999;
        int menor2 = 999999;

        for (int j = 0; j < qtdConsumidores; ++j) {
            if (colunaSatisfeita[j] == true) {
                continue;
            }

            if (custos[i][j] < menor1) {
                menor2 = menor1;
                menor1 = custos[i][j];
            } else if (custos[i][j] < menor2) {
                menor2 = custos[i][j];
            }
        }

        return menor2 - menor1;
    }

    public static int calcularPenalidadeColuna(int j, int qtdFornecedores, int[][] custos, boolean[] linhaSatisfeita) {
        int menor1 = 999999;
        int menor2 = 999999;

        for (int i = 0; i < qtdFornecedores; ++i) {
            if (linhaSatisfeita[i] == true) {
                continue;
            }

            if (custos[i][j] < menor1) {
                menor2 = menor1;
                menor1 = custos[i][j];
            } else if (custos[i][j] < menor2) {
                menor2 = custos[i][j];
            }
        }

        return menor2 - menor1;
    }


    public static int[][] vogel(int qtdFornecedores, int qtdConsumidores, int[] capacidade, int[] demanda, int[][] custos) {
        int[][] distribuicaoMatriz = criarMatrizZerada(qtdFornecedores, qtdConsumidores);

        boolean[] linhaSatisfeita = criarVetorBoolFalso(qtdFornecedores);
        boolean[] colunaSatisfeita = criarVetorBoolFalso(qtdConsumidores);

        int linhasAtivas = qtdFornecedores;
        int colunasAtivas = qtdConsumidores;

        while (linhasAtivas > 0 && colunasAtivas > 0) {
            int maiorPenalidade = -1;
            int indiceEscolha = -1;
            boolean ehLinha = true;

            for (int i = 0; i < qtdFornecedores; ++i) {
                if (linhaSatisfeita[i] == true) {
                    continue;
                }

                int penalidade = calcularPenalidadeLinha(i, qtdConsumidores, custos, colunaSatisfeita);
                if (penalidade > maiorPenalidade) {
                    maiorPenalidade = penalidade;
                    indiceEscolha = i;
                    ehLinha = true;
                }
            }

            for (int j = 0; j < qtdConsumidores; ++j) {
                if (colunaSatisfeita[j] == true) {
                    continue;
                }

                int pen = calcularPenalidadeColuna(j, qtdFornecedores, custos, linhaSatisfeita);
                if (pen > maiorPenalidade) {
                    maiorPenalidade = pen;
                    indiceEscolha = j;
                    ehLinha = false;
                }
            }

            int linhaAlocar = -1;
            int colunaAlocar = -1;

            if (ehLinha) {
                linhaAlocar = indiceEscolha;
                int menorCusto = 999999;
                for (int j = 0; j < qtdConsumidores; ++j) {
                    if (!colunaSatisfeita[j] && custos[linhaAlocar][j] < menorCusto) {
                        menorCusto = custos[linhaAlocar][j];
                        colunaAlocar = j;
                    }
                }
            } else {
                colunaAlocar = indiceEscolha;
                int menorCusto = 999999;
                for (int i = 0; i < qtdFornecedores; ++i) {
                    if (!linhaSatisfeita[i] && custos[i][colunaAlocar] < menorCusto) {
                        menorCusto = custos[i][colunaAlocar];
                        linhaAlocar = i;
                    }
                }
            }

            int qtdAlocada = obterMenor(capacidade[linhaAlocar], demanda[colunaAlocar]);
            distribuicaoMatriz[linhaAlocar][colunaAlocar] = qtdAlocada;

            capacidade[linhaAlocar] = capacidade[linhaAlocar] - qtdAlocada;
            demanda[colunaAlocar] = demanda[colunaAlocar] - qtdAlocada;

            if (capacidade[linhaAlocar] == 0) {
                linhaSatisfeita[linhaAlocar] = true;
                linhasAtivas--;
            }
            if (demanda[colunaAlocar] == 0) {
                colunaSatisfeita[colunaAlocar] = true;
                colunasAtivas--;
            }
        }

        return distribuicaoMatriz;
    }
}
