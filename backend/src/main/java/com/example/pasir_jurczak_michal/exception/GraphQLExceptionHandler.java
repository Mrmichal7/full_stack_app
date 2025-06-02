package com.example.pasir_jurczak_michal.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GraphQLExceptionHandler implements DataFetcherExceptionResolver {

    @Override
    public @NotNull Mono<List<GraphQLError>> resolveException(@NotNull Throwable ex, @NotNull DataFetchingEnvironment env) {
        if (ex instanceof ConstraintViolationException validationEx) {
            List<GraphQLError> errors = validationEx.getConstraintViolations().stream()
                    .map(violation -> GraphqlErrorBuilder.newError(env)
                            .message("Validation error: " + violation.getMessage())
                            .build())
                    .collect(Collectors.toList());
            return Mono.just(errors);
        }

        GraphQLError error = GraphqlErrorBuilder.newError(env)
                .message("Internal error: " + ex.getMessage())
                .build();

        return Mono.just(List.of(error));
    }
}