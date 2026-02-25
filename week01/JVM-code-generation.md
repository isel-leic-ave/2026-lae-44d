# JVM: Compilação e Geração de Código de Máquina

## Comparação com a Compilação Nativa

- Compilação Nativa: Exemplo de fluxo C/C++.
- Compilação + Interpretação com JIT: Exemplo de fluxo Java/Kotlin.

```mermaid
graph TD
subgraph "Compilação Nativa"
    direction TB
    A1[Código Fonte .c ou .cpp] --> B1(Compilador gcc/g++)
    B1 --> C1[Código de Máquina Binário]
end

subgraph "Compilação + Interpretação"
    direction TB
    A2[Código .java ou .kt] --> B2(Compilador javac/kotlinc)
    B2 --> C2[Bytecode .class]
    C2 --> D2(java - JVM)
    D2 --> E2[Código de Máquina Binário]
end

style C1 fill:#f96,stroke:#333,stroke-width:2px
style E2 fill:#f96,stroke:#333,stroke-width:2px
style C2 fill:#69f,stroke:#333,stroke-width:2px
```
- Por omissão, a JVM interpreta Bytecode usando um processo de compilação _Just-in-time_ (JIT).
  - Este processo foi introduzido para melhor o desempenho da JVM.

## Linguagem Java

- Compilação: `javac MeuPrograma.ajva`
- Execução: `java MeuPrograma`
  - O comando `java` é a JVM.

## Linguagem Kotlin

- Compilação: `kotlinc MeuPrograma.kt`
- Execução: `kotlin MeuProgramaKt`
  - Equivalente a: `java -cp "caminho/para/kotlin-stdlib.jar:." MeuProgramaKt`
  - O comando `kotlin` é um _wrapper_ da JVM java.
