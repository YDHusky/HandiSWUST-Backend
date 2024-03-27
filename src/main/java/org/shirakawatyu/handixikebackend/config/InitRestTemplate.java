package org.shirakawatyu.handixikebackend.config;


public class InitRestTemplate {

//    public static RestTemplate init(BasicCookieStore basicCookieStore) {
//
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectionRequestTimeout(Timeout.ofMilliseconds(3000))
//                .setResponseTimeout(Timeout.ofMilliseconds(3000))
//                .setCircularRedirectsAllowed(true)
//                .setCookieSpec(StandardCookieSpec.RELAXED)
//                .build();
//        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
//                .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                .register("https", new SSLConnectionSocketFactory(getSSLContext()))
//                .build();
//        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
//        // 最大线程数
//        connectionManager.setMaxTotal(20);
//        // 默认线程数
//        connectionManager.setDefaultMaxPerRoute(5);
//
//        HttpClient httpClient = HttpClientBuilder.create()
//                .setDefaultCookieStore(basicCookieStore)
//                .setDefaultRequestConfig(requestConfig)
//                .setConnectionManager(connectionManager)
//                .build();
//
//        factory.setHttpClient(httpClient);
//
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(factory);
//        return restTemplate;
//    }
//
//    private static SSLContext getSSLContext() {
//        try {
//            // 这里可以填两种值 TLS和LLS
//            SSLContext sc = SSLContext.getInstance("TLS");
//            // 构建新对象
//            X509TrustManager manager = new X509TrustManager() {
//                @Override
//                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
//                }
//
//                // 这里返回Null
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//            };
//            sc.init(null, new TrustManager[]{manager}, null);
//            return sc;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
