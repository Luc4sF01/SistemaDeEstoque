Sistema de Estoque 📦
Um projeto em Java desenvolvido para controle de estoque, cadastro de produtos, registro de vendas e geração de relatórios. O sistema inclui uma interface gráfica moderna e intuitiva com FlatLaf e suporte a banco de dados local SQLite.

🚀 Funcionalidades Principais
Cadastro de Produtos: Permite adicionar produtos com nome, preço e quantidade ao banco de dados.
Atualização e Exclusão de Produtos: Gerencia facilmente os produtos cadastrados.
Registro de Vendas: Registra vendas de produtos com atualização automática do estoque.
Relatório de Vendas: Gera relatórios com filtros por período específico.
Interface Gráfica (Look and Feel): Estilo moderno com FlatLaf e opção de alternar entre tema claro e escuro.
Banco de Dados SQLite: Armazena permanentemente os dados de produtos e vendas.


🛠️ Tecnologias Utilizadas
Linguagem: Java (JDK 15.0.2)
Banco de Dados: SQLite
Interface Gráfica: Swing (FlatLaf)
Ferramentas:
IntelliJ IDEA para desenvolvimento
Launch4j para gerar executável .exe
Git e GitHub para versionamento

💻 Pré-requisitos
JDK 15 ou superior instalado.
SQLite JDBC Driver (sqlite-jdbc-3.47.1.0.jar) para conexão com o banco de dados.
Ambiente de desenvolvimento (IntelliJ IDEA ou similar).


🚀 Como Executar o Projeto
Clone o repositório:

bash
Copiar código
git clone https://github.com/Luc4sF01/SistemaDeEstoque.git
cd SistemaDeEstoque
Importe o projeto no IntelliJ IDEA ou na IDE de sua preferência.

Adicione as dependências (bibliotecas externas):

Adicione o sqlite-jdbc-3.47.1.0.jar ao classpath.
Adicione o flatlaf-3.5.2.jar para a interface gráfica.
Execute o projeto:

Execute o Main.java na pasta controller para iniciar o sistema.
Gerar Executável:

Use Launch4j para criar um .exe que execute o arquivo SistemaDeEstoque.jar.
📝 Como Alternar o Tema
No menu superior da interface gráfica, selecione Arquivo > Alternar Tema para alternar entre tema claro e tema escuro.

⚙️ Banco de Dados SQLite
O sistema utiliza um banco de dados SQLite armazenado no arquivo estoque.db com as seguintes tabelas:

Produtos: Tabela que armazena os produtos.
Vendas: Tabela que armazena o registro das vendas realizadas.

📧 Contato
Desenvolvido por Lucas.

GitHub: @Luc4sF01
E-mail: lucashfilipe@gmail.com
