package com.lvnluttngws;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetSocketAddress;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.lvnluttngws.document.repository")
public class EsConfig {

    @Value("${elasticsearch.host}")
    private String EsHost;

    @Value("${elasticsearch.port}")
    private int EsPort;

    @Value("${elasticsearch.clustername}")
    private String EsClusterName;

    @Bean
    public Client client() throws Exception {
        System.out.println("### client(): START ###");
        Settings esSettings = Settings.builder()
                .put("client.transport.ignore_cluster_name", true)
                .put("client.type", "transport")
                .put("cluster.name", EsClusterName)
                .put("http.type.default", "netty4")
                .put("network.server", false)
                .put("node.name", "_client_")
                .put("transport.ping_schedule", "5s")
                .put("transport.type.default", "netty4")
                .build();
        Client client = new PreBuiltTransportClient(esSettings)
                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(EsHost, EsPort)));
        return client;
    }
}