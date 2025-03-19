package dev.nafplio.ai.response;

import io.quarkiverse.langchain4j.response.AiResponseAugmenter;
import io.quarkiverse.langchain4j.response.ResponseAugmenterParams;
import io.quarkus.runtime.util.StringUtil;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;

import java.util.List;

@Dependent
public final class DefaultResponseAugmenter implements AiResponseAugmenter<String> {
    private final List<AIResponseMapper> mappers;

    public DefaultResponseAugmenter(Instance<AIResponseMapper> mappers) {
        this.mappers = mappers.stream().toList();
    }

    @Override
    public Multi<String> augment(Multi<String> stream, ResponseAugmenterParams params) {
        return stream
                .map(response -> {
                    for (var map : mappers) {
                        if (map.allowed()) {
                            response = map.map(response);
                        }
                    }

                    return response;
                })
                .filter(response -> !StringUtil.isNullOrEmpty(response))
                ;
    }
}
