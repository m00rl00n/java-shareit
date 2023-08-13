package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class BaseClient {

    protected final WebClient webClient;

    @Autowired
    public BaseClient(WebClient webClient) {
        this.webClient = webClient;
    }

    protected Mono<ResponseEntity<Object>> get(String path) {
        return get(path, null, null);
    }

    protected Mono<ResponseEntity<Object>> get(String path, int userId) {
        return get(path, userId, null);
    }

    protected Mono<ResponseEntity<Object>> get(String path, Integer userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    protected <T> Mono<ResponseEntity<Object>> post(String path, T body) {
        return post(path, null, null, body);
    }

    protected <T> Mono<ResponseEntity<Object>> post(String path, int userId, T body) {
        return post(path, userId, null, body);
    }

    protected <T> Mono<ResponseEntity<Object>> post(String path, Integer userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    protected <T> Mono<ResponseEntity<Object>> put(String path, int userId, T body) {
        return put(path, userId, null, body);
    }

    protected <T> Mono<ResponseEntity<Object>> put(String path, int userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PUT, path, userId, parameters, body);
    }

    protected <T> Mono<ResponseEntity<Object>> patch(String path, T body) {
        return patch(path, null, null, body);
    }

    protected <T> Mono<ResponseEntity<Object>> patch(String path, int userId) {
        return patch(path, userId, null, null);
    }

    protected <T> Mono<ResponseEntity<Object>> patch(String path, int userId, T body) {
        return patch(path, userId, null, body);
    }

    protected <T> Mono<ResponseEntity<Object>> patch(String path, Integer userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
    }

    protected Mono<ResponseEntity<Object>> delete(String path) {
        return delete(path, null, null);
    }

    protected Mono<ResponseEntity<Object>> delete(String path, int userId) {
        return delete(path, userId, null);
    }

    protected Mono<ResponseEntity<Object>> delete(String path, Integer userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, parameters, null);
    }

    private <T> Mono<ResponseEntity<Object>> makeAndSendRequest(HttpMethod method, String path, Integer userId, @Nullable Map<String, Object> parameters, @Nullable T body) {
        WebClient.RequestHeadersSpec<?> requestSpec = webClient.method(method)
                .uri(path, parameters)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .headers(headers -> {
                    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                    if (userId != null) {
                        headers.set("X-Sharer-User-Id", String.valueOf(userId));
                    }
                });

        if (body != null) {
            return ((WebClient.RequestBodySpec) requestSpec).bodyValue(body).exchangeToMono(response -> handleResponse(response));
        } else {
            return requestSpec.exchangeToMono(response -> handleResponse(response));
        }
    }

    private Mono<ResponseEntity<Object>> handleResponse(ClientResponse response) {
        if (response.statusCode().is2xxSuccessful()) {
            return response.toEntity(Object.class);
        } else {
            return response.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                        HttpStatus httpStatus = HttpStatus.valueOf(response.statusCode().value());
                        return Mono.just(ResponseEntity.status(httpStatus).body(errorBody));
                    });
        }
    }
}