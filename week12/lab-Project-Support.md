# Apoio de Laboratório ao Projeto: Terceira Entrega

- Apoio de laboratório dedicado à terceira parte do projeto.

## Baselines para `mapResultSetToEntity`

- É importante gerar baselines para classes de exemplo, como:
  - Classes simples: tipos primitivos, String, java.sql.Date;
  - Classe com Enum;
  - Classe com tipo referência.
- As classes de Baseline devem implementar a interface ou herdar da classe que implementa `mapResultSetToEntity`.
  - Isto vai depender como o projeto está estruturado.
  - O objetivo é sobrescrever (_override_) o método `mapResultSetToEntity` para um mapeamento específico de uma classe do domínio.
    - _e.g._,  retornar `new User(rs.getLong("id"), rs.getString("name"), /*...*/);` para a entidade `User`.
  - Pode ser necessária uma reestruturação do código da reflexão.
- Testar os Baseline:
  - Incluir testes unitários para cada baseline.

## Geração de Código

- Preparar classe objeto _RepositoryDynamic.kt_ (sugestão de nome) similar a `DynamicMapper` do exemplo de aula `Mapper`.
  - Com métodos como:
    - _loadDynamicRepo_: para carregar um novo repositório, e
    - _buildRepositoryByteArray_: para construir uma classe de repositório utilizando a API Class-File.
- Construção da classe (bytecode):
  - Imitar o bytecode do baseline.
  - Para a necessidade de carregar um novo repositório, usar _loadDynamicRepo_ da classe _RepositoryDynamic.kt_.
  - Testar o getById, por exemplo, para verificar o funcionamento correto de `mapResultSetToEntity` sobrescrito dinamicamente.
    - Assegurar que o `mapResultSetToEntity` utilizado é aquele gerado dinamicamente.
- Recomendação de desenvolvimento e teste:
  - Começar pelas classes simples,
  - depois classes com Enum,
  - depois classes com tipo referência.