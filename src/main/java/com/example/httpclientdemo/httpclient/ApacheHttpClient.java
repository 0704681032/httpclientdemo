package com.example.httpclientdemo.httpclient;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;


public class ApacheHttpClient {

    private static PoolingHttpClientConnectionManager initConnectionManager(HttpClientConfig clientConfig)  {
        // http
        //RegistryBuilder<ConnectionSocketFactory> socketFactoryRegistryBuilder = RegistryBuilder.create();
        //socketFactoryRegistryBuilder.register("http", new PlainConnectionSocketFactory());

        // https
        //SSLConnectionSocketFactory sslConnectionSocketFactory = createSSLConnectionSocketFactory();
        //socketFactoryRegistryBuilder.register("https", sslConnectionSocketFactory);

        // connPool socketFactoryRegistryBuilder.build()
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(clientConfig.getMaxRequests());
        connectionManager.setDefaultMaxPerRoute(clientConfig.getMaxRequestsPerHost());
        return connectionManager;
    }

    public static CloseableHttpClient getHttpClient(HttpClientConfig config) {
        HttpClientBuilder builder = HttpClientBuilder.create();;
//        CredentialsProvider credentialsProvider = this.clientConfig.getCredentialsProvider();
//        if (null != credentialsProvider) {
//            builder.setDefaultCredentialsProvider(credentialsProvider);
//        }
        // default request config
        RequestConfig defaultConfig = RequestConfig.custom().setConnectTimeout((int) config
                        .getConnectionTimeoutMillis()).setSocketTimeout((int) config.getReadTimeoutMillis())
                .setConnectionRequestTimeout((int) config.getWriteTimeoutMillis()).build();
        builder.setDefaultRequestConfig(defaultConfig);

        PoolingHttpClientConnectionManager connectionManager = initConnectionManager(config);
        builder.setConnectionManager(connectionManager);
        ApacheIdleConnectionCleaner.registerConnectionManager(connectionManager, config.getMaxIdleTimeMillis());



        builder.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));

        // keepAlive
        if (config.getKeepAliveDurationMillis() > 0) {
            builder.setKeepAliveStrategy((response, context) -> {
                long duration = DefaultConnectionKeepAliveStrategy.INSTANCE.getKeepAliveDuration(response, context);

                if (duration > 0 && duration < config.getKeepAliveDurationMillis()) {
                    return duration;
                } else {
                    return config.getKeepAliveDurationMillis();
                }
            });
        }

        return builder.build();
    }
}
