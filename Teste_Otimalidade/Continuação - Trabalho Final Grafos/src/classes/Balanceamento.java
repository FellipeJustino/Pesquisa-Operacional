package classes;

public class Balanceamento {

    // Caso especial: problema desbalanceado (oferta total != demanda total)
    // Crie um fornecedor ou consumidor ficticio, com custo zero, para igualar oferta e demanda

    public static int[] adicionarElemento(int[] vetor, int valorNovo) {
        int[] novoVetor = new int[vetor.length + 1];
        System.arraycopy(vetor, 0, novoVetor, 0, vetor.length);
        novoVetor[vetor.length] = valorNovo;
        return novoVetor;
    }

    public static int[][] adicionarLinhaCustoZero(int[][] custos, int qtdConsumidores) {
        int[][] novosCustos = new int[custos.length + 1][qtdConsumidores];
        for (int i = 0; i < custos.length; ++i) {
            System.arraycopy(custos[i], 0, novosCustos[i], 0, qtdConsumidores);
        }
        // a ultima linha (fornecedor ficticio) fica com custo zero (valor padrao do int[])
        return novosCustos;
    }

    public static int[][] adicionarColunaCustoZero(int[][] custos, int qtdFornecedores, int qtdConsumidoresAtual) {
        int[][] novosCustos = new int[qtdFornecedores][qtdConsumidoresAtual + 1];
        for (int i = 0; i < qtdFornecedores; ++i) {
            System.arraycopy(custos[i], 0, novosCustos[i], 0, qtdConsumidoresAtual);
            // a ultima coluna (consumidor ficticio) fica com custo zero (valor padrao do int[])
        }
        return novosCustos;
    }
}
