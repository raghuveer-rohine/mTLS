package com.example.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;


@Configuration
public class Config {

    @Bean
    public RestTemplate restTemplate() throws Exception {
        char[] password = "password".toCharArray();

        // Load client.p12 from classpath
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(getClass().getClassLoader().getResourceAsStream("client.p12"), password);

        // Load ca.p12 from classpath
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        trustStore.load(getClass().getClassLoader().getResourceAsStream("ca.p12"), password);

        // Build SSLContext with both keystore and truststore
        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, password)
                .loadTrustMaterial(trustStore, null)
                .build();

        PoolingHttpClientConnectionManager connectionManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(
                                SSLConnectionSocketFactoryBuilder.create()
                                        .setSslContext(sslContext)
                                        .build()
                        )
                        .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(factory);
    }

}