package classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LeitorArquivo {

    private int qtdFornecedores;
    private int qtdConsumidores;
    private int[] capacidade;
    private int[] demanda;
    private int[][] custos;

    public void lerArquivo() {
        try {
            // Tenta achar dados.txt independente de onde o programa foi executado
            // (raiz do projeto, dentro de src/, etc.).
            String[] candidatos = {"src/dados.txt", "dados.txt", "../dados.txt", "../src/dados.txt"};
            String caminho = "src/dados.txt";
            for (String candidato : candidatos) {
                if (new File(candidato).exists()) {
                    caminho = candidato;
                    break;
                }
            }

            BufferedReader br = new BufferedReader(new FileReader(caminho));

            String linha = br.readLine();
            String[] partes = linha.split(" ");

            qtdFornecedores = Integer.parseInt(partes[0]);
            qtdConsumidores = Integer.parseInt(partes[1]);

            capacidade = new int[qtdFornecedores];
            linha = br.readLine();
            partes = linha.split(" ");
            for (int i = 0; i < qtdFornecedores; ++i) {
                capacidade[i] = Integer.parseInt(partes[i]);
            }

            demanda = new int[qtdConsumidores];
            linha = br.readLine();
            partes = linha.split(" ");
            for (int j = 0; j < qtdConsumidores; ++j) {
                demanda[j] = Integer.parseInt(partes[j]);
            }

            custos = new int[qtdFornecedores][qtdConsumidores];
            for (int i = 0; i < qtdFornecedores; ++i) {
                linha = br.readLine();
                partes = linha.split(" ");
                for (int j = 0; j < qtdConsumidores; ++j) {
                    custos[i][j] = Integer.parseInt(partes[j]);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo dados.txt nao encontrado.");
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    public int getQtdFornecedores() {
        return qtdFornecedores;
    }

    public int getQtdConsumidores() {
        return qtdConsumidores;
    }

    public int[] getCapacidade() {
        return capacidade;
    }

    public int[] getDemanda() {
        return demanda;
    }

    public int[][] getCustos() {
        return custos;
    }

}
