package dev.nafplio;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public interface Unis {
    static  <T> T run(Supplier<? extends T> supplier) {
        try {
            return Uni.createFrom().item(supplier)
                    .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                    .subscribe().asCompletionStage().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
