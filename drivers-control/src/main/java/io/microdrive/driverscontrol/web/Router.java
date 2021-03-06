package io.microdrive.driverscontrol.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class Router {
    private final ApiHandler apiHandler;

    @Bean
    RouterFunction<ServerResponse> routes() {
        var routes = route().nest(
            accept(APPLICATION_JSON),
            builder -> builder
                .POST("/add-to-free", apiHandler::addToFree)
                .POST("/find-free", apiHandler::findFree)
        ).build();

        return route()
            .path("/api/drivers-control", () -> routes)
            .build();
    }
}
