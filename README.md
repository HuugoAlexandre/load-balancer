# Load Balancer

Projeto criado como nota na disciplina de Fundamentos de Redes no curso de Bacharelado em Sistemas de Informação.

## Integrantes

* Eike Fabrício
* Hugo Alexandre
* João Henrique

## Informações do Projeto

* JDK utilizada: `17`
* Como compilar:
  * Na pasta base, rodar `./gradlew build`
* Agora com os arquivos compilados, podemos acessar os arquivos gerados em:
  * `build/libs/load-balancer-1.0-SNAPSHOT.jar`
    * Iniciamos simplesmente rodando `java -jar load-balancer-1.0-SNAPSHOT.jar`
  * Como módulos dos servidores temos de ir na pasta `server-nodes/build/libs/server-nodes-1.0-SNAPSHOT.jar`
    * Iniciamos especificando a porta no primeiro argumento, como: `java -jar server-nodes-1.0-SNAPSHOT.jar <porta>`

## Funcionamento

O algoritmo de balanceamento de rede implementado foi o `round-robin`.

Ao iniciar o servidor nó, ele criará um socket enviando as informações de porta e endereço ao load-balancer, que por sua 
vez, armazena os dados numa lista implementada de maneira circular.

Ao cliente requisitar algo na porta 80, podemos testar utilizando o comando `curl localhost`, caso não hajam servidores nós,
o load balancer responderá `Hello from Load Balancer`.

Caso haja algum servidor nó na lista, o load balancer fica à par de criar um socket intermediando a conexão, passando os dados
de entrada do socket de origem e, ao receber os dados, copiando os dados de saída para o socket original.
