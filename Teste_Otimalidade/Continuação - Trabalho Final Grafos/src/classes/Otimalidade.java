package classes;

import java.util.List;

import static classes.Custo.calcularCustoTotal;
import static classes.Matriz.copiarMatriz;
import static classes.Matriz.imprimirMatriz;

// Teste de otimalidade do problema de transporte: metodo dos multiplicadores
public class Otimalidade {

    // Identifica quais celulas sao variaveis basicas 
    // (Canto Noroeste, Custo Minimo ou Vogel)
    public static boolean[][] identificarBasicas(int[][] distribuicaoMatriz, int qtdFornecedores, int qtdConsumidores) {
        boolean[][] basica = new boolean[qtdFornecedores][qtdConsumidores];
        int totalBasicas = 0;

        for (int i = 0; i < qtdFornecedores; ++i) {
            for (int j = 0; j < qtdConsumidores; ++j) {
                if (distribuicaoMatriz[i][j] > 0) {
                    basica[i][j] = true;
                    totalBasicas++;
                }
            }
        }

        int totalNecessario = qtdFornecedores + qtdConsumidores - 1;

        while (totalBasicas < totalNecessario) {
            boolean adicionou = false;

            for (int i = 0; i < qtdFornecedores && !adicionou; ++i) {
                for (int j = 0; j < qtdConsumidores && !adicionou; ++j) {
                    if (basica[i][j]) {
                        continue;
                    }

                    basica[i][j] = true;
                    if (formaCiclo(basica, qtdFornecedores, qtdConsumidores)) {
                        basica[i][j] = false;
                    } else {
                        totalBasicas++;
                        adicionou = true;
                    }
                }
            }

            if (!adicionou) {
                break;
            }
        }

        return basica;
    }

    private static boolean formaCiclo(boolean[][] basica, int qtdFornecedores, int qtdConsumidores) {
        int totalNos = qtdFornecedores + qtdConsumidores;
        int[] pai = new int[totalNos];
        for (int k = 0; k < totalNos; ++k) {
            pai[k] = k;
        }

        for (int i = 0; i < qtdFornecedores; ++i) {
            for (int j = 0; j < qtdConsumidores; ++j) {
                if (!basica[i][j]) {
                    continue;
                }

                int raizLinha = encontrarRaiz(pai, i);
                int raizColuna = encontrarRaiz(pai, qtdFornecedores + j);

                if (raizLinha == raizColuna) {
                    return true;
                }
                pai[raizLinha] = raizColuna;
            }
        }
        return false;
    }

    private static int encontrarRaiz(int[] pai, int no) {
        while (pai[no] != no) {
            no = pai[no];
        }
        return no;
    }

    // Calcula os multiplicadores u (fornecedores) e v (consumidores) a partir das variaveis
    // basicas, usando ui + vj = cij. Sempre fixa u[0] = 0 como referencia
    public static int[][] calcularMultiplicadores(boolean[][] basica, int[][] custos,
                                                   int qtdFornecedores, int qtdConsumidores) {
        int[] u = new int[qtdFornecedores];
        int[] v = new int[qtdConsumidores];
        boolean[] uDefinido = new boolean[qtdFornecedores];
        boolean[] vDefinido = new boolean[qtdConsumidores];

        uDefinido[0] = true;

        boolean mudou = true;
        while (mudou) {
            mudou = false;

            for (int i = 0; i < qtdFornecedores; ++i) {
                for (int j = 0; j < qtdConsumidores; ++j) {
                    if (!basica[i][j]) {
                        continue;
                    }

                    if (uDefinido[i] && !vDefinido[j]) {
                        v[j] = custos[i][j] - u[i];
                        vDefinido[j] = true;
                        mudou = true;
                    } else if (!uDefinido[i] && vDefinido[j]) {
                        u[i] = custos[i][j] - v[j];
                        uDefinido[i] = true;
                        mudou = true;
                    }
                }
            }
        }

        return new int[][]{u, v};
    }

    // Custo reduzido das variaveis nao basicas: c_ij = ui + vj - cij (formula do metodo dos
    // multiplicadores). Se algum for positivo, a solucao ainda pode melhorar
    public static int[][] calcularCustosReduzidos(boolean[][] basica, int[] u, int[] v, int[][] custos,
                                                   int qtdFornecedores, int qtdConsumidores) {
        int[][] reduzido = new int[qtdFornecedores][qtdConsumidores];

        for (int i = 0; i < qtdFornecedores; ++i) {
            for (int j = 0; j < qtdConsumidores; ++j) {
                if (!basica[i][j]) {
                    reduzido[i][j] = u[i] + v[j] - custos[i][j];
                }
            }
        }

        return reduzido;
    }

    // Aplica o teste de otimalidade (MODI) e, enquanto a solucao nao for otima,
    // Imprime cada iteracao, que nem o exemplo do PDF 
    public static int[][] aplicarModi(int[][] distribuicaoInicial, int[][] custos, int qtdFornecedores, int qtdConsumidores) {
        int[][] distribuicaoMatriz = copiarMatriz(distribuicaoInicial, qtdFornecedores, qtdConsumidores);
        boolean[][] basica = identificarBasicas(distribuicaoMatriz, qtdFornecedores, qtdConsumidores);

        int iteracao = 1;

        while (true) {
            int[][] multiplicadores = calcularMultiplicadores(basica, custos, qtdFornecedores, qtdConsumidores);
            int[] u = multiplicadores[0];
            int[] v = multiplicadores[1];

            int[][] reduzido = calcularCustosReduzidos(basica, u, v, custos, qtdFornecedores, qtdConsumidores);

            System.out.println("\n--- MODI - Iteracao " + iteracao + " ---");
            imprimirMatriz(distribuicaoMatriz, qtdFornecedores, qtdConsumidores);
            System.out.println("Z = " + calcularCustoTotal(distribuicaoMatriz, custos, qtdFornecedores, qtdConsumidores));

            int linhaEntrada = -1;
            int colunaEntrada = -1;
            int maiorReduzido = 0;

            for (int i = 0; i < qtdFornecedores; ++i) {
                for (int j = 0; j < qtdConsumidores; ++j) {
                    if (basica[i][j]) {
                        continue;
                    }
                    if (reduzido[i][j] > maiorReduzido) {
                        maiorReduzido = reduzido[i][j];
                        linhaEntrada = i;
                        colunaEntrada = j;
                    }
                }
            }

            if (linhaEntrada == -1) {
                System.out.println("Todos os custos reduzidos sao <= 0: solucao otima encontrada.");

                // Caso especial: multiplas solucoes otimas (custo reduzido igual a zero
                // em alguma variavel nao basica, na solucao otima final)
                for (int i = 0; i < qtdFornecedores; ++i) {
                    for (int j = 0; j < qtdConsumidores; ++j) {
                        if (!basica[i][j] && reduzido[i][j] == 0) {
                            System.out.println("Ha uma solucao otima alternativa (mesmo Z), trazendo x"
                                    + (i + 1) + (j + 1) + " para a base.");
                        }
                    }
                }
                break;
            }

            List<int[]> ciclo = CicloFechado.encontrarCiclo(linhaEntrada, colunaEntrada, basica,
                    qtdFornecedores, qtdConsumidores);

            if (ciclo == null) {
                System.out.println("Nao foi possivel montar o ciclo fechado (stepping stone). Encerrando.");
                break;
            }

            int theta = Integer.MAX_VALUE;
            for (int k = 1; k < ciclo.size() - 1; k += 2) {
                int[] celula = ciclo.get(k);
                theta = Math.min(theta, distribuicaoMatriz[celula[0]][celula[1]]);
            }

            for (int k = 0; k < ciclo.size() - 1; ++k) {
                int[] celula = ciclo.get(k);
                if (k % 2 == 0) {
                    distribuicaoMatriz[celula[0]][celula[1]] += theta;
                } else {
                    distribuicaoMatriz[celula[0]][celula[1]] -= theta;
                }
            }

            basica[linhaEntrada][colunaEntrada] = true;

            for (int k = 1; k < ciclo.size() - 1; k += 2) {
                int[] celula = ciclo.get(k);
                if (distribuicaoMatriz[celula[0]][celula[1]] == 0) {
                    basica[celula[0]][celula[1]] = false;
                    break;
                }
            }

            iteracao++;
        }

        return distribuicaoMatriz;
    }
}
