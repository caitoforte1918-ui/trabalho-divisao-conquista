# trabalho-divisao-conquista
Trabalho Prático — Divisão e Conquista

Disciplina: Construção de Algoritmos
Integrantes: Cairo e Valter
Linguagem: Java 21

1. Sobre o trabalho

Neste trabalho, implementamos e comparamos algoritmos utilizando força bruta e divisão e conquista.

O objetivo foi entender como essas duas abordagens funcionam e comparar seus tempos de execução conforme o tamanho das entradas aumenta.

Foram trabalhados dois problemas:

* Subarranjo de soma máxima.
* Par de pontos mais próximos.

Também implementamos uma solução linear para o primeiro problema.

2. Algoritmos implementados

Subarranjo de soma máxima

O objetivo é encontrar a sequência contínua de números que apresenta a maior soma possível.

Foram utilizadas três soluções:

* Força bruta: O(n²).
* Divisão e conquista: O(n log n).
* Algoritmo de Kadane: O(n).

Par de pontos mais próximos

O objetivo é encontrar os dois pontos com a menor distância entre si.

Foram utilizadas duas soluções:

* Força bruta: O(n²).
* Divisão e conquista: O(n log n).

Na solução de divisão e conquista, utilizamos um limite de 32 pontos para resolver subproblemas menores por força bruta.

3. Arquivos do projeto

Arquivo	Descrição
Main.java	Implementação dos algoritmos
Benchmark.java	Medição dos tempos de execução
Testes.java	Testes de funcionamento dos algoritmos
casos_de_teste.csv	Casos de teste e resultados esperados
medicoes.csv	Tempos registrados nos experimentos
Trabalho Av2.pdf	Relatório completo do trabalho

4. Como executar

É necessário ter o JDK 21 ou superior instalado no computador.

Abra o terminal na pasta onde os arquivos Java estão salvos.

Compilar o código:

javac Main.java Benchmark.java Testes.java

Executar os testes:

java Testes

Executar as medições de desempenho:

java Benchmark medicoes.csv

Executar os algoritmos individualmente:

java Main soma-bruta
java Main soma-divisao
java Main soma-linear
java Main pontos-bruta
java Main pontos-divisao

Para os algoritmos de soma, a entrada deve conter a quantidade de elementos e os números do vetor.

Para os algoritmos de pontos, a entrada deve conter a quantidade de pontos e suas coordenadas.

5. Testes e resultados

Foram realizados testes envolvendo diferentes situações, incluindo números negativos, vetores pequenos, empates e pontos com coordenadas repetidas.

Também foram feitas medições para diferentes tamanhos de entrada, utilizando sete execuções medidas por configuração.

Os resultados foram registrados no arquivo medicoes.csv e analisados no relatório.

6. Conclusão

Com este trabalho, conseguimos observar na prática as diferenças entre força bruta e divisão e conquista.

A força bruta é mais simples de implementar, mas pode apresentar um crescimento maior no tempo de execução quando a entrada aumenta.

Já a divisão e conquista permite resolver os problemas de forma mais eficiente para entradas maiores, embora tenha custos adicionais de recursão e combinação.

O desenvolvimento ajudou a compreender melhor a análise de algoritmos, as recorrências e a importância de escolher uma solução adequada para cada problema.
