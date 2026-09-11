package classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LeitorArquivoDesignacao {

    private int qtdLinhas;
    private int qtdColunas;
    private int[][] custos;

    public void lerArquivo() {
        try {
            String[] candidatos = {"src/dados_designacao.txt", "dados_designacao.txt",
                    "../dados_designacao.txt", "../src/dados_designacao.txt"};
            String caminho = "src/dados_designacao.txt";
            for (String candidato : candidatos) {
                if (new File(candidato).exists()) {
                    caminho = candidato;
                    break;
                }
            }

            BufferedReader br = new BufferedReader(new FileReader(caminho));

            String linha = br.readLine();
            String[] partes = linha.split(" ");

            qtdLinhas = Integer.parseInt(partes[0]);
            qtdColunas = Integer.parseInt(partes[1]);

            custos = new int[qtdLinhas][qtdColunas];
            for (int i = 0; i < qtdLinhas; ++i) {
                linha = br.readLine();
                partes = linha.split(" ");
                for (int j = 0; j < qtdColunas; ++j) {
                    custos[i][j] = Integer.parseInt(partes[j]);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo dados_designacao.txt nao encontrado.");
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    public int getQtdLinhas() {
        return qtdLinhas;
    }

    public int getQtdColunas() {
        return qtdColunas;
    }

    public int[][] getCustos() {
        return custos;
    }
}
