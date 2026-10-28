import java.util.*;

/** Trabalho prático de Divisão e Conquista. Compilar: javac src/*.java */
public class Main {
    public record Intervalo(long soma, int inicio, int fim) {}
    public record Ponto(double x, double y, int id) {}
    public record Par(Ponto a, Ponto b, double dist2) {
        public double distancia() { return Math.sqrt(dist2); }
    }

    // Critério de desempate: maior soma; depois menor início; depois menor fim.
    private static Intervalo melhor(Intervalo a, Intervalo b) {
        if (a.soma != b.soma) return a.soma > b.soma ? a : b;
        if (a.inicio != b.inicio) return a.inicio < b.inicio ? a : b;
        return a.fim <= b.fim ? a : b;
    }
    public static Intervalo somaBruta(int[] v) {
        if (v.length == 0) throw new IllegalArgumentException("Vetor vazio");
        Intervalo res = new Intervalo(Long.MIN_VALUE, 0, 0);
        for (int i = 0; i < v.length; i++) {
            long soma = 0;
            for (int j = i; j < v.length; j++) {
                soma += v[j];
                res = melhor(res, new Intervalo(soma, i, j));
            }
        }
        return res;
    }
    public static Intervalo somaDivisao(int[] v) {
        if (v.length == 0) throw new IllegalArgumentException("Vetor vazio");
        return somaRec(v, 0, v.length - 1);
    }
    private static Intervalo somaRec(int[] v, int ini, int fim) {
        if (ini == fim) return new Intervalo(v[ini], ini, fim);
        int meio = ini + (fim - ini) / 2;
        Intervalo esq = somaRec(v, ini, meio);
        Intervalo dir = somaRec(v, meio + 1, fim);
        long soma = 0, melhorE = Long.MIN_VALUE;
        int inicio = meio;
        for (int i = meio; i >= ini; i--) {
            soma += v[i];
            if (soma > melhorE || (soma == melhorE && i < inicio)) {
                melhorE = soma;
                inicio = i;
            }
        }
        soma = 0;
        long melhorD = Long.MIN_VALUE;
        int finalDireito = meio + 1;
        for (int j = meio + 1; j <= fim; j++) {
            soma += v[j];
            if (soma > melhorD) {
                melhorD = soma;
                finalDireito = j;
            }
        }
        Intervalo cruzado = new Intervalo(melhorE + melhorD, inicio, finalDireito);
        return melhor(melhor(esq, dir), cruzado);
    }
    private static double quadrado(Ponto a, Ponto b) {
        double dx = a.x - b.x, dy = a.y - b.y;
        return dx * dx + dy * dy;
    }
    private static Par escolher(Par p, Par q) {
        if (p == null) return q;
        if (q == null) return p;
        if (p.dist2 < q.dist2) return p;
        if (q.dist2 < p.dist2) return q;
        // Em caso de distância igual, escolher os IDs em ordem lexicográfica.
        int p0 = Math.min(p.a.id, p.b.id), p1 = Math.max(p.a.id, p.b.id);
        int q0 = Math.min(q.a.id, q.b.id), q1 = Math.max(q.a.id, q.b.id);
        if (p0 != q0) return p0 < q0 ? p : q;
        return p1 <= q1 ? p : q;
    }
    private static Par comparar(Par melhor, Ponto a, Ponto b) {
        return escolher(melhor, new Par(a, b, quadrado(a, b)));
    }
    public static Par pontosBruta(Ponto[] pontos) {
        if (pontos.length < 2) throw new IllegalArgumentException("É preciso pelo menos 2 pontos");
        Par melhor = null;
        for (int i = 0; i < pontos.length; i++)
            for (int j = i + 1; j < pontos.length; j++)
                melhor = comparar(melhor, pontos[i], pontos[j]);
        return melhor;
    }
    // Versão extra O(n) do subarranjo (Kadane), citada como desafio na avaliação.
    public static Intervalo somaLinear(int[] v) {
        if (v.length == 0) throw new IllegalArgumentException("Vetor vazio");
        Intervalo melhorGlobal = new Intervalo(v[0], 0, 0);
        long atual = v[0];
        int inicioAtual = 0;
        for (int i = 1; i < v.length; i++) {
            if (atual < 0) { atual = v[i]; inicioAtual = i; }
            else atual += v[i];
            melhorGlobal = melhor(melhorGlobal, new Intervalo(atual, inicioAtual, i));
        }
        return melhorGlobal;
    }
    private static final Comparator<Ponto> POR_X = Comparator.comparingDouble(Ponto::x)
            .thenComparingDouble(Ponto::y).thenComparingInt(Ponto::id);
    private static final Comparator<Ponto> POR_Y = Comparator.comparingDouble(Ponto::y)
            .thenComparingDouble(Ponto::x).thenComparingInt(Ponto::id);
    private static final int LIMIAR = 32;

    public static Par pontosDivisao(Ponto[] pontos) {
        if (pontos.length < 2) throw new IllegalArgumentException("É preciso pelo menos 2 pontos");
        Ponto[] x = pontos.clone(), y = pontos.clone();
        Arrays.sort(x, POR_X);
        Arrays.sort(y, POR_Y);
        return pontosRec(x, y, 0, x.length);
    }
    private static Par pontosRec(Ponto[] x, Ponto[] y, int ini, int fim) {
        int n = fim - ini;
        if (n <= LIMIAR) {
            Par melhor = null;
            for (int i = ini; i < fim; i++)
                for (int j = i + 1; j < fim; j++)
                    melhor = comparar(melhor, x[i], x[j]);
            return melhor;
        }
        int meio = (ini + fim) >>> 1;
        double linha = x[meio].x;
        // Usa identidade por ID para manter cada metade com n/2 pontos,
        // inclusive quando há coordenadas repetidas.
        Set<Integer> idsEsquerda = new HashSet<>();
        for (int i = ini; i < meio; i++) idsEsquerda.add(x[i].id);
        Ponto[] yE = new Ponto[meio - ini], yD = new Ponto[fim - meio];
        int e = 0, d = 0;
        for (Ponto p : y) {
            if (idsEsquerda.contains(p.id)) yE[e++] = p;
            else yD[d++] = p;
        }
        Par melhor = escolher(pontosRec(x, yE, ini, meio), pontosRec(x, yD, meio, fim));
        double delta2 = melhor.dist2;
        ArrayList<Ponto> faixa = new ArrayList<>();
        for (Ponto p : y) {
            double dx = p.x - linha;
            if (dx * dx <= delta2) faixa.add(p);
        }
        for (int i = 0; i < faixa.size(); i++) {
            for (int j = i + 1; j < Math.min(i + 8, faixa.size()); j++) {
                double dy = faixa.get(j).y - faixa.get(i).y;
                if (dy * dy > delta2) break;
                melhor = comparar(melhor, faixa.get(i), faixa.get(j));
                delta2 = melhor.dist2;
            }
        }
        return melhor;
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java -cp src Main soma-bruta|soma-divisao|soma-linear|pontos-bruta|pontos-divisao");
            return;
        }
        Scanner sc = new Scanner(System.in).useLocale(Locale.US);
        if (args[0].startsWith("soma-")) {
            int n = sc.nextInt();
            int[] v = new int[n];
            for (int i = 0; i < n; i++) v[i] = sc.nextInt();
            Intervalo r = switch (args[0]) {
                case "soma-bruta" -> somaBruta(v);
                case "soma-divisao" -> somaDivisao(v);
                case "soma-linear" -> somaLinear(v);
                default -> throw new IllegalArgumentException("Modo desconhecido");
            };
            System.out.printf("Soma máxima: %d | Início: %d | Fim: %d%n", r.soma, r.inicio, r.fim);
        } else if (args[0].startsWith("pontos-")) {
            int n = sc.nextInt();
            Ponto[] p = new Ponto[n];
            for (int i = 0; i < n; i++) p[i] = new Ponto(sc.nextDouble(), sc.nextDouble(), i);
            Par r = args[0].equals("pontos-bruta") ? pontosBruta(p) : pontosDivisao(p);
            System.out.printf(Locale.US, "Pontos: %d e %d | Distância: %.8f%n", r.a.id, r.b.id, r.distancia());
        } else throw new IllegalArgumentException("Modo desconhecido");
    }
}

// update
