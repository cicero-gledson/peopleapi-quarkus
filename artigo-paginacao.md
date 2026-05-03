Guia Completo: Paginação com Quarkus e PanacheEntityBase
Este artigo apresenta de forma didática como implementar a Paginação (Pagination) utilizando o padrão Active Record (Registro Ativo) com a classe PanacheEntityBase.
1. Teoria Simplificada: O que é Paginação?
Imagine que você está em uma biblioteca imensa. Se você pedir ao bibliotecário "todos os livros de Java", ele não conseguirá carregar 5.000 livros de uma vez. Ele trará um carrinho com uma quantidade limitada para que você possa manusear.
Na computação, a paginação é a técnica de dividir um conjunto de dados grande em blocos menores (páginas). Isso evita o Overhead (Sobrecarga) de memória e rede, garantindo que sua aplicação não trave ao lidar com milhares de registros.
Termos Chave (Traduções):
* Page Index (Índice da Página): A posição da página que você quer ver. Começa sempre em 0 (Página 1 = Índice 0).
* Page Size (Tamanho da Página): Quantos registros você quer ver por vez (ex: 10 itens por página).
* Offset (Deslocamento): Quantos registros o banco de dados deve "pular" para chegar na página desejada.
* Limit (Limite): O número máximo de registros a serem retornados naquela chamada.
2. Quando usar?
Você deve utilizar a paginação sempre que:
1. Volume de Dados: A tabela possuir centenas ou milhares de registros.
2. Performance (Desempenho): Você deseja que sua API responda rapidamente, consumindo o mínimo de memória possível.
3. Experiência do Usuário (UX): Listas infinitas em telas de celular ou sistemas web podem travar o navegador ou consumir dados excessivos do usuário.
3. Implementação Prática em Java
Ao contrário do PanacheEntity (que já traz um ID automático), o PanacheEntityBase permite que você tenha controle total sobre sua chave primária (@Id). Isso é fundamental para sistemas que usam chaves customizadas ou legadas.
A Entidade (O Modelo)
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Produto extends PanacheEntityBase {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long id; // No EntityBase, nós definimos o ID explicitamente

   public String nome;
   public Double preco;
}

O Recurso (O Controller)
Aqui está o segredo para a implementação: o fluxo Page -> Query -> List.
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
public class ProdutoResource {

   @GET
   public List<Produto> listar(
           @QueryParam("page") @DefaultValue("0") int pageIndex, 
           @QueryParam("size") @DefaultValue("10") int pageSize) {
       
       // 1. Criamos a configuração da página (A "Lente")
       Page pagina = Page.of(pageIndex, pageSize);

       // 2. Criamos a Query (A consulta básica)
       PanacheQuery<Produto> query = Produto.findAll();

       // 3. Aplicamos a página e executamos a busca final
       return query.page(pagina).list();
   }
}

4. Dica de Memorização: A Técnica do "P.Q.L."
Para nunca mais esquecer a ordem no Quarkus Panache, lembre-se da sigla PQL:
1. P de Page: Defina as regras (qual página e qual o tamanho).
2. Q de Query: Chame o método de busca (ex: findAll() ou find()).
3. L de List: Aplique a página na query e transforme em lista com .list().
5. Indo Além: Metadados da Paginação (O DTO)
Em aplicações profissionais, o cliente da API (Frontend) precisa saber quantas páginas existem no total para desenhar os botões de navegação. Para isso, usamos um DTO (Data Transfer Object / Objeto de Transferência de Dados) para "envelopar" a resposta.
import java.util.List;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

public class RespostaPaginada<T> {
   public List<T> dados;
   public long totalRegistros;
   public int totalPaginas;

   public RespostaPaginada(PanacheQuery<T> query) {
       this.dados = query.list();              // Pega apenas os itens da página atual
       this.totalRegistros = query.count();    // Conta o total geral no banco de dados
       this.totalPaginas = query.pageCount(); // Calcula o total de páginas disponíveis
   }
}

Resumo das Assertivas (Revisão Técnica)
* PanacheEntityBase: Use quando precisar de controle total sobre o ID (ex: UUID ou Identity customizado).
* Page.of(0, 10): Comando que prepara a primeira página com 10 elementos.
* query.page(p): Não busca os dados ainda, apenas configura os limites do SQL (LIMIT e OFFSET).
* list(): É o comando que efetivamente dispara a consulta ao banco de dados.