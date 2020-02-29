package com.xxw.spider.httpclient;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * Created by xiexianwu on 20/2/29.
 */
public class DefaultHttpClientBuilder extends HttpClientBuilder {
    public boolean isSetPool=false;//记录是否设置了连接池

    private DefaultHttpClientBuilder(){}

    public static DefaultHttpClientBuilder custom(){
        return new DefaultHttpClientBuilder();
    }


    /**
     * 设置超时时间以及是否允许网页重定向（自动跳转 302）
     *
     * @param connectionRequestTimeout 连接池获取连接超时时间，单位-毫秒
     * @param connectTimeout		   请求连接超时时间，单位-毫秒
     * @param socketTimeout		       请求读取数据超时时间，单位-毫秒
     * @param redirectEnable		   自动跳转
     * @return	返回当前对象
     */
    public DefaultHttpClientBuilder timeout(int connectionRequestTimeout, int connectTimeout, int socketTimeout, boolean redirectEnable){
        // 配置请求的超时设置
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setRedirectsEnabled(redirectEnable)
                .build();
        return (DefaultHttpClientBuilder) this.setDefaultRequestConfig(config);
    }

    /**
     * 设置连接池（默认开启https）
     *
     * @param maxTotal					最大连接数
     * @param defaultMaxPerRoute	每个路由默认连接数
     * @return	返回当前对象
     *
     */
    public DefaultHttpClientBuilder pool(int maxTotal, int defaultMaxPerRoute) {
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        //设置连接池大小
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setMaxTotal(maxTotal);// Increase max total connection to $maxTotal
        connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);// Increase default max connection per route to $defaultMaxPerRoute
        //connManager.setMaxPerRoute(route, max);// Increase max connections for $route(eg：localhost:80) to 50
        isSetPool=true;
        return (DefaultHttpClientBuilder) this.setConnectionManager(connManager);
    }

    /**
     * 设置代理
     *
     * @param hostOrIP		代理host或者ip
     * @param port			代理端口
     * @return	返回当前对象
     */
    public DefaultHttpClientBuilder proxy(String hostOrIP, int port){
        // 依次是代理地址，代理端口号，协议类型
        HttpHost proxy = new HttpHost(hostOrIP, port, "http");
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        return (DefaultHttpClientBuilder) this.setRoutePlanner(routePlanner);
    }

    /**
     * 重试（如果请求是幂等的，就再次尝试）
     *
     * @param tryTimes		重试次数
     * @return	返回当前对象
     */
    public DefaultHttpClientBuilder retry(final int tryTimes){
        return retry(tryTimes, false);
    }

    /**
     * 重试（如果请求是幂等的，就再次尝试）
     *
     * @param tryTimes						重试次数
     * @param retryWhenInterruptedIO		连接拒绝时，是否重试
     * @return	返回当前对象
     */
    public DefaultHttpClientBuilder retry(final int tryTimes, final boolean retryWhenInterruptedIO){
        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= tryTimes) {// 如果已经重试了n次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    //return false;
                    return retryWhenInterruptedIO;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return true;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        this.setRetryHandler(httpRequestRetryHandler);
        return this;
    }
}
