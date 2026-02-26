# 🏭 Controle de Produtos (Product Control & Simulation)

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Quarkus](https://img.shields.io/badge/Quarkus-Supersonic%20Subatomic-blue?style=for-the-badge&logo=quarkus)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Render-Deploy-black?style=for-the-badge&logo=render)

O **Controle de Produtos** é uma API RESTful robusta desenvolvida em Java com o framework **Quarkus**. Este sistema gerencia o cadastro de produtos, as matérias-primas e a relação de necessidade entre eles. Além do CRUD básico, o grande diferencial deste projeto é a rota de **Simulação de Produção**, que realiza o cálculo do melhor cenário de fabricação possível com base no estoque atual de matérias-primas, priorizando produtos de maior valor para maximizar os lucros!

---

## 🚀 Tecnologias Utilizadas

A aplicação foi construída utilizando modernas práticas de desenvolvimento e arquitetura:

- **Java 21**: Linguagem de programação.
- **Quarkus 3.x**: Framework Java (Supersonic Subatomic Java) voltado para cloud-native e alta performance.
- **Hibernate ORM com Panache**: Para persistência e mapeamento objeto-relacional simplificado, utilizando o padrão Active Record / Repository.
- **Jakarta REST (JAX-RS)**: Para criação das rotas da API REST.
- **SmallRye OpenAPI & Swagger UI**: Documentação e interface da API interativa.
- **MySQL**: Banco de dados relacional.
- **Docker**: Containerização com Dockerfile multi-stage para facilitar a implementação e garantir isolamento.
- **Maven**: Gerenciador de dependências e automação de build.

---

## 📊 Modelagem do Banco de Dados

O banco de dados foi projetado através de um modelo coeso e normalizado.

### Modelo Entidade-Relacionamento (MER)

- **Produto (Product)**: Representa o item final que será vendido ou fabricado. Possui informações como nome e preço.
- **Matéria-Prima (RawMaterial)**: Representa os insumos necessários para a fabricação. Controla o nome do insumo e a quantidade atual em estoque.
- **Relacionamento (ProductRawMaterial)**: Como um produto usa várias matérias-primas e uma matéria-prima faz parte de vários produtos, nós temos um relacionamento **Muitos-para-Muitos (N:M)**. Esta entidade associativa faz a ponte entre o Produto e a Matéria-Prima, especificando a **quantidade necessária (quantity_needed)** daquele insumo para fabricar *uma* unidade do produto.

### Diagrama Entidade-Relacionamento (DER)

Abaixo, a representação das tabelas geradas no banco de dados e suas relações lógicas:

| Tabela | Colunas (Tipos) | Chaves e Regras |
|--------|-----------------|-----------------|
| `tb_products` | `id` (BIGINT)<br>`name` (VARCHAR(255))<br>`price` (DECIMAL(10,2)) | **PK:** `id` |
| `tb_raw_materials` | `id` (BIGINT)<br>`name` (VARCHAR(255))<br>`stock_quantity` (INT) | **PK:** `id` |
| `tb_product_raw_materials` | `id` (BIGINT)<br>`product_id` (BIGINT)<br>`raw_material_id` (BIGINT)<br>`quantity_needed` (INT) | **PK:** `id`<br>**FK:** `product_id` -> `tb_products(id)`<br>**FK:** `raw_material_id` -> `tb_raw_materials(id)` |

---

## 🛠️ Como Executar o Projeto Localmente

Siga as instruções para rodar o projeto na sua máquina de forma rápida.

### Pré-requisitos
- **JDK 21+** instalado (ex: Eclipse Temurin).
- **Maven** instalado (ou use o wrapper `./mvnw` incluso no diretório).
- Instância do **MySQL** rodando localmente ou via container.

### Passo a Passo

1. **Clone o repositório:**
   ```bash
   git clone <url-do-seu-repositorio>
   cd controle-de-produtos
   ```

2. **Configure o Banco de Dados:**
   Crie um banco/schema no seu MySQL (por exemplo, `controle_produtos`).
   Configure as seguintes variáveis de ambiente. Você pode exportá-las no seu terminal ou criar um arquivo `.env` na raiz do projeto (cujas variáveis o Quarkus tentará carregar):
   ```env
   DB_USER=seu_usuario
   DB_PASS=sua_senha
   DB_URL=jdbc:mysql://localhost:3306/controle_produtos?useSSL=false
   ```

3. **Inicie a Aplicação em Modo Dev (Quarkus):**
   No terminal, execute:
   ```bash
   ./mvnw compile quarkus:dev
   ```
   > O Quarkus iniciará a aplicação na porta `8080` utilizando **Live Reload** e gerando as tabelas automaticamente (`update`).

4. **Acesse a API e as ferramentas:**
   - **Swagger UI** (para testar as rotas): [http://localhost:8080/q/swagger-ui/](http://localhost:8080/q/swagger-ui/)
   - **Quarkus Dev UI** (dashboard dev): [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/)

---

## ☁️ Deploy na Nuvem (Render)

Esta aplicação foi empacotada em uma imagem Docker para produção e hospedada na plataforma **Render**, garantindo disponibilidade e praticidade.

### Entendendo a Arquitetura de Deploy
- A aplicação utiliza o arquivo `Dockerfile` na raiz do projeto para realizar um **build multistage**.
- **Stage 1**: Levanta um container com o Maven embutido para fazer o _clean package_ (eliminando a necessidade do ambiente destino ter Maven/Java de build instalados).
- **Stage 2**: Pega o app jar otimizado da pasta `target` e coloca numa imagem enxuta baseada em `openjdk-21`, habilitando as flags de hosts e logs nativas do Quarkus.

### Variáveis de Ambiente em Produção
Para que tudo funcione perfeitamente no Render (ou em outras plataformas PaaS), as seguintes _Environment Variables_ foram inseridas no painel de administração da plataforma (elas correspondem ao bloco `%prod` do `application.properties`):
- `DATABASE_URL`: A connection string JDBC apontando pro Host do Banco de Dados hospedado.
- `DATABASE_USER`: Usuário de produção.
- `DATABASE_PASS`: Senha do BD de produção.

Ao inicializar o serviço Web, a aplicação conecta ao banco recém provisionado e automaticamente reconstrói ou atualiza o _schema_ graças a propriedade `hibernate-orm.database.generation=update`.

---

*Projeto desenvolvido para otimização do controle e fabricação de produtos industriais e comerciais.*
