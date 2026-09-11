package classes;

import java.util.Arrays;

// Metodo Hungaro para o problema de designacao (minimizacao), seguindo os passos do PDF:
// Passo 1 - reduzir cada linha pelo seu menor elemento.
// Passo 2 - reduzir cada coluna (da matriz do passo 1) pelo seu menor elemento.
// Passo 3 - cobrir todos os zeros com o menor numero possivel de linhas/colunas.
//           Se o numero de retas = tamanho da matriz, ha solucao otima entre os zeros cobertos.
// Passo 4 - senao, pegar o menor elemento nao coberto (k), subtrair k dos nao cobertos e somar k
//           aos cobertos duas vezes (linha e coluna), depois voltar ao passo 3.

public class Hungaro {

    // Caso especial: matriz nao quadrada (numero de tarefas != numero de maquinas).
    // Completa-se a matriz com linhas/colunas ficticias de custo zero ate ela ficar quadrada
    public static int[] resolver(int[][] custosOriginais, int qtdLinhasReal, int qtdColunasReal) {
        int tamanho = Math.max(qtdLinhasReal, qtdColunasReal);

        int[][] custos = new int[tamanho][tamanho];
        for (int i = 0; i < tamanho; ++i) {
            for (int j = 0; j < tamanho; ++j) {
                if (i < qtdLinhasReal && j < qtdColunasReal) {
                    custos[i][j] = custosOriginais[i][j];
                } else {
                    custos[i][j] = 0;
                }
            }
        }

        reduzirLinhas(custos, tamanho);
        reduzirColunas(custos, tamanho);

        int[] casamentoColuna;

        while (true) {
            casamentoColuna = encontrarCasamentoMaximo(custos, tamanho);

            boolean[] linhaCoberta = new boolean[tamanho];
            boolean[] colunaCoberta = new boolean[tamanho];
            marcarCobertura(custos, tamanho, casamentoColuna, linhaCoberta, colunaCoberta);

            int totalCobertura = 0;
            for (int i = 0; i < tamanho; ++i) {
                if (linhaCoberta[i]) {
                    totalCobertura++;
                }
            }
            for (int j = 0; j < tamanho; ++j) {
                if (colunaCoberta[j]) {
                    totalCobertura++;
                }
            }

            if (totalCobertura >= tamanho) {
                break;
            }

            ajustarMatriz(custos, tamanho, linhaCoberta, colunaCoberta);
        }

        int[] designacaoLinhaParaColuna = new int[tamanho];
        Arrays.fill(designacaoLinhaParaColuna, -1);
        for (int j = 0; j < tamanho; ++j) {
            if (casamentoColuna[j] != -1) {
                designacaoLinhaParaColuna[casamentoColuna[j]] = j;
            }
        }

        return designacaoLinhaParaColuna;
    }

    private static void reduzirLinhas(int[][] custos, int tamanho) {
        for (int i = 0; i < tamanho; ++i) {
            int menor = custos[i][0];
            for (int j = 1; j < tamanho; ++j) {
                if (custos[i][j] < menor) {
                    menor = custos[i][j];
                }
            }
            for (int j = 0; j < tamanho; ++j) {
                custos[i][j] -= menor;
            }
        }
    }

    private static void reduzirColunas(int[][] custos, int tamanho) {
        for (int j = 0; j < tamanho; ++j) {
            int menor = custos[0][j];
            for (int i = 1; i < tamanho; ++i) {
                if (custos[i][j] < menor) {
                    menor = custos[i][j];
                }
            }
            for (int i = 0; i < tamanho; ++i) {
                custos[i][j] -= menor;
            }
        }
    }

    // Casamento maximo entre linhas e colunas, usando apenas as arestas onde o custo ja e zero
    // (algoritmo de Kuhn, com caminhos aumentantes).
    private static int[] encontrarCasamentoMaximo(int[][] custos, int tamanho) {
        int[] casamentoColuna = new int[tamanho];
        Arrays.fill(casamentoColuna, -1);

        for (int i = 0; i < tamanho; ++i) {
            boolean[] visitada = new boolean[tamanho];
            tentarCasar(i, custos, tamanho, visitada, casamentoColuna);
        }

        return casamentoColuna;
    }

    private static boolean tentarCasar(int linha, int[][] custos, int tamanho, boolean[] visitada, int[] casamentoColuna) {
        for (int j = 0; j < tamanho; ++j) {
            if (custos[linha][j] != 0 || visitada[j]) {
                continue;
            }

            visitada[j] = true;

            if (casamentoColuna[j] == -1 || tentarCasar(casamentoColuna[j], custos, tamanho, visitada, casamentoColuna)) {
                casamentoColuna[j] = linha;
                return true;
            }
        }
        return false;
    }

    // A partir do casamento maximo, encontra a cobertura minima de zeros (teorema de Konig)
    // marca com alternancia as linhas nao casadas e tudo que e alcancavel a partir delas; a
    // cobertura minima e formada pelas linhas NAO marcadas e pelas colunas marcadas.
    private static void marcarCobertura(int[][] custos, int tamanho, int[] casamentoColuna, boolean[] linhaCoberta, boolean[] colunaCoberta) {

        boolean[] linhaCasada = new boolean[tamanho];
        for (int j = 0; j < tamanho; ++j) {
            if (casamentoColuna[j] != -1) {
                linhaCasada[casamentoColuna[j]] = true;
            }
        }

        boolean[] linhaMarcada = new boolean[tamanho];
        boolean[] colunaMarcada = new boolean[tamanho];

        for (int i = 0; i < tamanho; ++i) {
            if (!linhaCasada[i]) {
                marcarAlternado(i, custos, tamanho, casamentoColuna, linhaMarcada, colunaMarcada);
            }
        }

        for (int i = 0; i < tamanho; ++i) {
            linhaCoberta[i] = !linhaMarcada[i];
        }
        for (int j = 0; j < tamanho; ++j) {
            colunaCoberta[j] = colunaMarcada[j];
        }
    }

    private static void marcarAlternado(int linha, int[][] custos, int tamanho, int[] casamentoColuna,
                                         boolean[] linhaMarcada, boolean[] colunaMarcada) {
        if (linhaMarcada[linha]) {
            return;
        }
        linhaMarcada[linha] = true;

        for (int j = 0; j < tamanho; ++j) {
            if (custos[linha][j] == 0 && !colunaMarcada[j]) {
                colunaMarcada[j] = true;

                if (casamentoColuna[j] != -1) {
                    marcarAlternado(casamentoColuna[j], custos, tamanho, casamentoColuna, linhaMarcada, colunaMarcada);
                }
            }
        }
    }

    private static void ajustarMatriz(int[][] custos, int tamanho, boolean[] linhaCoberta, boolean[] colunaCoberta) {
        int menorNaoCoberto = Integer.MAX_VALUE;

        for (int i = 0; i < tamanho; ++i) {
            if (linhaCoberta[i]) {
                continue;
            }
            for (int j = 0; j < tamanho; ++j) {
                if (colunaCoberta[j]) {
                    continue;
                }
                if (custos[i][j] < menorNaoCoberto) {
                    menorNaoCoberto = custos[i][j];
                }
            }
        }

        for (int i = 0; i < tamanho; ++i) {
            for (int j = 0; j < tamanho; ++j) {
                if (!linhaCoberta[i] && !colunaCoberta[j]) {
                    custos[i][j] -= menorNaoCoberto;
                } else if (linhaCoberta[i] && colunaCoberta[j]) {
                    custos[i][j] += menorNaoCoberto;
                }
            }
        }
    }

    // Custo total da designacao, calculado com os custos originais,
    // ignorando designacoes que caem em linha/coluna ficticia 
    public static int calcularCustoDesignacao(int[] designacaoLinhaParaColuna, int[][] custosOriginais,
                                               int qtdLinhasReal, int qtdColunasReal) {
        int total = 0;
        for (int i = 0; i < qtdLinhasReal; ++i) {
            int j = designacaoLinhaParaColuna[i];
            if (j != -1 && j < qtdColunasReal) {
                total += custosOriginais[i][j];
            }
        }
        return total;
    }
}
