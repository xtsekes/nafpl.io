module nafplio.data {
    requires static lombok;

    requires io.smallrye.mutiny;
    requires jakarta.persistence;
    requires jakarta.transaction;
    requires quarkus.hibernate.orm.panache;
    requires quarkus.panache.common;

    exports dev.nafplio.data;
}