import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Supplier;
public class Benchmark {
    private static volatile Object consumir;
    private static long medir(Supplier<Object> acao) {
        long ini = System.nanoTime();
        consumir = acao.get();
        return System.nanoTime() - ini;
    }
    public static void main(String[] args) throws IOException {
        Path destino = Paths.get(args.length == 0 ? "resultados/medicoes.csv" : args[0]);
        Files.createDirectories(destino.toAbsolutePath().getParent());
        Random rng = new Random(2026);
        int[] tamanhosSoma = {100, 200, 400, 800, 1600, 3200, 6400};
        int[] tamanhosPontos = {100, 200, 400, 800, 1600, 3200, 6400};
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(destino))) {
            out.println("problema,metodo,n,execucao,tempo_ns");
            for (String problema : new String[]{"soma", "pontos"}) {
                for (int n : problema.equals("soma") ? tamanhosSoma : tamanhosPontos) {
                    int[] v = new int[n];
                    Main.Ponto[] pontos = new Main.Ponto[n];
                    for (int i = 0; i < n; i++) {
                        v[i] = rng.nextInt(2001) - 1000;
                        pontos[i] = new Main.Ponto(rng.nextDouble()*10000, rng.nextDouble()*10000, i);
                    }
                    for (String metodo : (problema.equals("soma") ? new String[]{"bruta", "divisao", "linear"} : new String[]{"bruta", "divisao"})) {
                        Supplier<Object> trabalho = problema.equals("soma")
                            ? (metodo.equals("bruta") ? () -> Main.somaBruta(v) : metodo.equals("linear") ? () -> Main.somaLinear(v) : () -> Main.somaDivisao(v))
                            : (metodo.equals("bruta") ? () -> Main.pontosBruta(pontos) : () -> Main.pontosDivisao(pontos));
                        for (int k = 0; k < 8; k++) medir(trabalho); // aquecimento do JIT
                        for (int repeticao = 1; repeticao <= 7; repeticao++) {
                            long ns = medir(trabalho);
                            out.printf(Locale.US, "%s,%s,%d,%d,%d%n", problema, metodo, n, repeticao, ns);
                        }
                    }
                    out.flush();
                    System.out.println("Concluído: " + problema + " n=" + n);
                }
            }
        }
        System.out.println("Medições registradas em " + destino);
    }
}
