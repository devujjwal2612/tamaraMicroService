package com.axisrooms.tamara.configuration;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Value("${max.total.connections}")
    private Integer maxTotalConnections;

    @Value("${max.connections.per.route}")
    private Integer maxConnectionsPerRoute;

    @Value("${connection.timeout.milliseconds}")
    private Integer connectionTimeoutMilliSeconds;

    @Value("${socket.timeout.milliseconds}")
    private Integer socketTimeoutMilliSeconds;

    @Bean
    public RestTemplate restTemplate() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(connectionTimeoutMilliSeconds)
                .setConnectionRequestTimeout(connectionTimeoutMilliSeconds)
                .setSocketTimeout(socketTimeoutMilliSeconds)
                .build();
        CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }
}
