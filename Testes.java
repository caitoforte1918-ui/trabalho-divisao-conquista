import java.util.*;
public class Testes {
    private static int total = 0;
    private static void checar(boolean ok, String msg) {
        total++;
        if (!ok) throw new AssertionError("Falhou: " + msg);
    }
    private static void testarSoma(int[] v, long soma, int inicio, int fim) {
        Main.Intervalo b = Main.somaBruta(v), d = Main.somaDivisao(v);
        checar(b.equals(Main.somaLinear(v)), "versão linear diverge");
        checar(b.equals(d), "métodos divergem: " + Arrays.toString(v));
        checar(b.soma() == soma && b.inicio() == inicio && b.fim() == fim,
                "resultado inesperado: " + Arrays.toString(v) + " => " + b);
    }
    private static void testarPontos(double[][] xy, double distancia2) {
        Main.Ponto[] p = new Main.Ponto[xy.length];
        for (int i = 0; i < xy.length; i++) p[i] = new Main.Ponto(xy[i][0], xy[i][1], i);
        Main.Par b = Main.pontosBruta(p), d = Main.pontosDivisao(p);
        checar(Math.abs(b.dist2() - d.dist2()) < 1e-9, "distâncias divergentes");
        checar(Math.abs(b.dist2() - distancia2) < 1e-9, "distância esperada incorreta");
    }
    public static void main(String[] args) {
        testarSoma(new int[]{-4, -2, -9}, -2, 1, 1);
        testarSoma(new int[]{7}, 7, 0, 0);
        testarSoma(new int[]{0, 0, 0}, 0, 0, 0);
        testarSoma(new int[]{2, -2, 2}, 2, 0, 0);
        testarSoma(new int[]{-2, 5, -1, 6, -9, 2}, 10, 1, 3);
        testarSoma(new int[]{-3}, -3, 0, 0);
        testarPontos(new double[][]{{0, 0}, {3, 4}}, 25);
        testarPontos(new double[][]{{1, 1}, {1, 1}, {5, 5}}, 0);
        testarPontos(new double[][]{{0, 0}, {10, 0}, {0, 0.1}, {10, 10}}, 0.01);
        testarPontos(new double[][]{{-3, -4}, {-3, -5}, {10, 10}}, 1);
        Random rng = new Random(2026);
        for (int t = 0; t < 400; t++) {
            int n = 1 + rng.nextInt(80);
            int[] v = new int[n];
            for (int i = 0; i < n; i++) v[i] = rng.nextInt(31) - 15;
            checar(Main.somaBruta(v).equals(Main.somaDivisao(v)), "soma aleatória " + t);
            checar(Main.somaBruta(v).equals(Main.somaLinear(v)), "soma linear " + t);
        }
        for (int t = 0; t < 250; t++) {
            int n = 2 + rng.nextInt(120);
            Main.Ponto[] p = new Main.Ponto[n];
            for (int i = 0; i < n; i++) p[i] = new Main.Ponto(rng.nextInt(61)-30, rng.nextInt(61)-30, i);
            checar(Math.abs(Main.pontosBruta(p).dist2() - Main.pontosDivisao(p).dist2()) < 1e-9,
                    "pontos aleatórios " + t);
        }
        System.out.println("OK: " + total + " verificações passaram.");
    }
}
