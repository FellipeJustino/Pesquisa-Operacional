import classes.LeitorArquivo;
import classes.LeitorArquivoDesignacao;

import java.util.Scanner;

import static classes.Balanceamento.adicionarColunaCustoZero;
import static classes.Balanceamento.adicionarElemento;
import static classes.Balanceamento.adicionarLinhaCustoZero;
import static classes.CantoNoroeste.cantoNoroeste;
import static classes.Custo.calcularCustoTotal;
import static classes.CustoMinimo.custoMinimo;
import static classes.Hungaro.calcularCustoDesignacao;
import static classes.Hungaro.resolver;
import static classes.Matriz.imprimirMatriz;
import static classes.Maximizacao.converterParaMinimizacao;
import static classes.Otimalidade.aplicarModi;
import static classes.Vetor.copiarVetor;
import static classes.Vogel_copia.vogel;


public class Main {
    static void main(String[] args) {

        LeitorArquivo leitor = new LeitorArquivo();
        leitor.lerArquivo();

        int qtdFornecedores = leitor.getQtdFornecedores();
        int qtdConsumidores = leitor.getQtdConsumidores();
        int[] capacidade = leitor.getCapacidade();
        int[] demanda = leitor.getDemanda();
        int[][] custosOriginais = leitor.getCustos();

        Scanner scanner = new Scanner(System.in);

        // Caso especial: problema desbalanceado (oferta total != demanda total).
        int totalCapacidade = 0;
        for (int i = 0; i < qtdFornecedores; ++i) {
            totalCapacidade += capacidade[i];
        }
        int totalDemanda = 0;
        for (int j = 0; j < qtdConsumidores; ++j) {
            totalDemanda += demanda[j];
        }

        if (totalCapacidade > totalDemanda) {
            System.out.println("\nProblema desbalanceado: oferta (" + totalCapacidade + ") > demanda (" + totalDemanda
                    + "). Criando consumidor ficticio com custo zero.");
            custosOriginais = adicionarColunaCustoZero(custosOriginais, qtdFornecedores, qtdConsumidores);
            demanda = adicionarElemento(demanda, totalCapacidade - totalDemanda);
            qtdConsumidores++;
        } else if (totalDemanda > totalCapacidade) {
            System.out.println("\nProblema desbalanceado: demanda (" + totalDemanda + ") > oferta (" + totalCapacidade
                    + "). Criando fornecedor ficticio com custo zero.");
            custosOriginais = adicionarLinhaCustoZero(custosOriginais, qtdConsumidores);
            capacidade = adicionarElemento(capacidade, totalDemanda - totalCapacidade);
            qtdFornecedores++;
        }

        // Caso especial: problema de maximizacao (lucro), em vez de minimizacao de custo.
        System.out.println("\n1 - Minimizar custo");
        System.out.println("2 - Maximizar (lucro/beneficio)");
        System.out.print("Escolha: ");
        int tipoProblema = scanner.nextInt();

        int[][] custosAlgoritmo = custosOriginais;
        if (tipoProblema == 2) {
            custosAlgoritmo = converterParaMinimizacao(custosOriginais, qtdFornecedores, qtdConsumidores);
        }

        int opcao = -1;

        while (opcao != 0) {

            System.out.println("\n==============================");
            System.out.println("      PROBLEMA DO TRANSPORTE    ");
            System.out.println("================================");
            System.out.println("1 - Canto Noroeste");
            System.out.println("2 - Custo Minimo");
            System.out.println("3 - Vogel");
            System.out.println("4 - Executar todos");
            System.out.println("5 - Teste de Otimalidade (MODI)");
            System.out.println("6 - Metodo Hungaro (Designacao)");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();

            if (opcao == 1) {
                int[] capCopia = copiarVetor(capacidade);
                int[] demCopia = copiarVetor(demanda);

                int[][] distribuicaoNoroeste = cantoNoroeste(qtdFornecedores, qtdConsumidores, capCopia, demCopia);
                System.out.println("\n------ CANTO NOROESTE ------");
                imprimirMatriz(distribuicaoNoroeste, qtdFornecedores, qtdConsumidores);
                System.out.println("Z = " + calcularCustoTotal(distribuicaoNoroeste, custosOriginais, qtdFornecedores, qtdConsumidores));

            } else if (opcao == 2) {
                int[] capCopia = copiarVetor(capacidade);
                int[] demCopia = copiarVetor(demanda);

                int[][] distribuicaoCustoMin = custoMinimo(qtdFornecedores, qtdConsumidores, capCopia, demCopia, custosAlgoritmo);
                System.out.println("\n------ CUSTO MINIMO ------");
                imprimirMatriz(distribuicaoCustoMin, qtdFornecedores, qtdConsumidores);
                System.out.println("Z = " + calcularCustoTotal(distribuicaoCustoMin, custosOriginais, qtdFornecedores, qtdConsumidores));

            } else if (opcao == 3) {
                int[] capCopia = copiarVetor(capacidade);
                int[] demCopia = copiarVetor(demanda);

                int[][] alocacaoVogel = vogel(qtdFornecedores, qtdConsumidores, capCopia, demCopia, custosAlgoritmo);
                System.out.println("\n------ VOGEL ------");
                imprimirMatriz(alocacaoVogel, qtdFornecedores, qtdConsumidores);
                System.out.println("Z = " + calcularCustoTotal(alocacaoVogel, custosOriginais, qtdFornecedores, qtdConsumidores));

            } else if (opcao == 4) {
                int[] capCopia1 = copiarVetor(capacidade);
                int[] demCopia1 = copiarVetor(demanda);

                int[][] distribuicaoNoroeste = cantoNoroeste(qtdFornecedores, qtdConsumidores, capCopia1, demCopia1);
                System.out.println("\n========= CANTO NOROESTE =========");
                imprimirMatriz(distribuicaoNoroeste, qtdFornecedores, qtdConsumidores);
                System.out.println("Z = " + calcularCustoTotal(distribuicaoNoroeste, custosOriginais, qtdFornecedores, qtdConsumidores));

                int[] capCopia2 = copiarVetor(capacidade);
                int[] demCopia2 = copiarVetor(demanda);
                int[][] distribuicaoCustoMin = custoMinimo(qtdFornecedores, qtdConsumidores, capCopia2, demCopia2, custosAlgoritmo);
                System.out.println("\n========= CUSTO MINIMO =========");
                imprimirMatriz(distribuicaoCustoMin, qtdFornecedores, qtdConsumidores);
                System.out.println("Z = " + calcularCustoTotal(distribuicaoCustoMin, custosOriginais, qtdFornecedores, qtdConsumidores));

                int[] capCopia3 = copiarVetor(capacidade);
                int[] demCopia3 = copiarVetor(demanda);
                int[][] distribuicaoVogel = vogel(qtdFornecedores, qtdConsumidores, capCopia3, demCopia3, custosAlgoritmo);
                System.out.println("\n======== VOGEL =========");
                imprimirMatriz(distribuicaoVogel, qtdFornecedores, qtdConsumidores);
                System.out.println("Z = " + calcularCustoTotal(distribuicaoVogel, custosOriginais, qtdFornecedores, qtdConsumidores));

            } else if (opcao == 5) {
                System.out.println("\nQual solucao inicial usar como ponto de partida?");
                System.out.println("1 - Canto Noroeste");
                System.out.println("2 - Custo Minimo");
                System.out.println("3 - Vogel");
                System.out.print("Escolha: ");
                int metodoInicial = scanner.nextInt();

                int[] capCopia = copiarVetor(capacidade);
                int[] demCopia = copiarVetor(demanda);
                int[][] solucaoInicial;

                if (metodoInicial == 2) {
                    solucaoInicial = custoMinimo(qtdFornecedores, qtdConsumidores, capCopia, demCopia, custosAlgoritmo);
                } else if (metodoInicial == 3) {
                    solucaoInicial = vogel(qtdFornecedores, qtdConsumidores, capCopia, demCopia, custosAlgoritmo);
                } else {
                    solucaoInicial = cantoNoroeste(qtdFornecedores, qtdConsumidores, capCopia, demCopia);
                }

                int[][] solucaoOtima = aplicarModi(solucaoInicial, custosAlgoritmo, qtdFornecedores, qtdConsumidores);

                System.out.println("\n------ SOLUCAO OTIMA (apos MODI) ------");
                imprimirMatriz(solucaoOtima, qtdFornecedores, qtdConsumidores);
                System.out.println("Z = " + calcularCustoTotal(solucaoOtima, custosOriginais, qtdFornecedores, qtdConsumidores));

            } else if (opcao == 6) {
                LeitorArquivoDesignacao leitorDesignacao = new LeitorArquivoDesignacao();
                leitorDesignacao.lerArquivo();

                int qtdLinhas = leitorDesignacao.getQtdLinhas();
                int qtdColunas = leitorDesignacao.getQtdColunas();
                int[][] custosDesignacao = leitorDesignacao.getCustos();

                if (custosDesignacao == null) {
                    continue;
                }

                int[] designacao = resolver(custosDesignacao, qtdLinhas, qtdColunas);

                System.out.println("\n------ METODO HUNGARO ------");
                for (int i = 0; i < qtdLinhas; ++i) {
                    int j = designacao[i];
                    if (j != -1 && j < qtdColunas) {
                        System.out.println("Linha " + (i + 1) + " -> Coluna " + (j + 1)
                                + " (custo " + custosDesignacao[i][j] + ")");
                    } else {
                        System.out.println("Linha " + (i + 1) + " -> nao designada (linha/coluna ficticia)");
                    }
                }
                System.out.println("Z = " + calcularCustoDesignacao(designacao, custosDesignacao, qtdLinhas, qtdColunas));

            } else if (opcao != 0) {
                System.out.println("Opcao invalida!");
            }
        }

        scanner.close();
    }
}
