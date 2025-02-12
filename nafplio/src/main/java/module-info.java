module nafplio {
    requires static lombok;

    requires arc;
    requires jakarta.ws.rs;
    requires langchain4j;
    requires langchain4j.core;
    requires langchain4j.pgvector;
    requires quarkus.core;
    requires quarkus.langchain4j.core;
    requires quarkus.narayana.jta;
    requires quarkus.websockets.next;
    requires io.vertx.core;

    requires nafplio.data;
    requires project.scanner;
    requires io.smallrye.mutiny;
    requires jakarta.cdi;
    requires org.slf4j;
}