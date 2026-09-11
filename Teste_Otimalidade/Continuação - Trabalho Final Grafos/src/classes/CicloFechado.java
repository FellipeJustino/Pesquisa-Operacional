package classes;

import java.util.ArrayList;
import java.util.List;

// Usado pelo metodo dos multiplicadores (MODI)
// a partir da variavel que vai entrar na base, monta um unico ciclo fechado possivel,
// alternando movimentos na vertical e na horizontal, passando somente por variaveis basicas.
public class CicloFechado {

    public static List<int[]> encontrarCiclo(int linhaEntrada, int colunaEntrada, boolean[][] basica,
                                              int qtdFornecedores, int qtdConsumidores) {

        List<int[]> caminho = new ArrayList<>();
        caminho.add(new int[]{linhaEntrada, colunaEntrada});

        if (buscarVertical(linhaEntrada, colunaEntrada, linhaEntrada, colunaEntrada, basica,
                qtdFornecedores, qtdConsumidores, caminho)) {
            return caminho;
        }

        caminho.clear();
        caminho.add(new int[]{linhaEntrada, colunaEntrada});

        if (buscarHorizontal(linhaEntrada, colunaEntrada, linhaEntrada, colunaEntrada, basica,
                qtdFornecedores, qtdConsumidores, caminho)) {
            return caminho;
        }

        return null;
    }

    private static boolean buscarVertical(int linhaAtual, int colunaAtual, int linhaEntrada, int colunaEntrada,boolean[][] basica, int qtdFornecedores, int qtdConsumidores,List<int[]> caminho) {

        for (int i = 0; i < qtdFornecedores; ++i) {
            if (i == linhaAtual) {
                continue;
            }

            boolean fechaCiclo = (i == linhaEntrada) && (colunaAtual == colunaEntrada) && caminho.size() >= 3;

            if (!basica[i][colunaAtual] && !fechaCiclo) {
                continue;
            }

            caminho.add(new int[]{i, colunaAtual});

            if (fechaCiclo) {
                return true;
            }

            if (buscarHorizontal(i, colunaAtual, linhaEntrada, colunaEntrada, basica,
                    qtdFornecedores, qtdConsumidores, caminho)) {
                return true;
            }

            caminho.remove(caminho.size() - 1);
        }
        return false;
    }

    private static boolean buscarHorizontal(int linhaAtual, int colunaAtual, int linhaEntrada, int colunaEntrada,boolean[][] basica, int qtdFornecedores, int qtdConsumidores, List<int[]> caminho) {

        for (int j = 0; j < qtdConsumidores; ++j) {
            if (j == colunaAtual) {
                continue;
            }

            boolean fechaCiclo = (j == colunaEntrada) && (linhaAtual == linhaEntrada) && caminho.size() >= 3;

            if (!basica[linhaAtual][j] && !fechaCiclo) {
                continue;
            }

            caminho.add(new int[]{linhaAtual, j});

            if (fechaCiclo) {
                return true;
            }

            if (buscarVertical(linhaAtual, j, linhaEntrada, colunaEntrada, basica,
                    qtdFornecedores, qtdConsumidores, caminho)) {
                return true;
            }

            caminho.remove(caminho.size() - 1);
        }
        return false;
    }
}
