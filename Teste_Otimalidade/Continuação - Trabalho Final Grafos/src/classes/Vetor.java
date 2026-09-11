    package classes;

    public class Vetor {
        public static int[] copiarVetor(int[] vetor) {
            int[] copia = new int[vetor.length];
            System.arraycopy(vetor, 0, copia, 0, vetor.length);
            return copia;
        }

        public static boolean[] criarVetorBoolFalso(int tamanho) {
            boolean[] vetor = new boolean[tamanho];
            for (int i = 0; i < tamanho; ++i) {
                vetor[i] = false;
            }
            return vetor;
        }
    }
