package com.xxw.spider.httpclient;

import com.alibaba.fastjson.JSON;
import com.xxw.spider.httpclient.common.HttpMethods;
import com.xxw.spider.httpclient.common.HttpResult;
import com.xxw.spider.httpclient.exception.HttpProcessException;
import com.xxw.spider.httpclient.utils.Utils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.http.client.protocol.HttpClientContext.COOKIE_STORE;

/**
 * Created by xiexianwu on 20/2/29.
 */
@Service
public class HttpRequestTemplate {

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private HttpClientBuilder httpClientBuilder;
    /**
     * 以Get方式，请求资源或服务
     *
     * @param url					资源地址
     * @param headers			请求头信息
     * @param context			http上下文，用于cookie操作
     * @param encoding		编码
     * @return						返回处理结果
     */
    public  String get(String url, Header[] headers, HttpContext context, String encoding) throws HttpProcessException {
        return get(HttpRequestConfig.custom().url(url).headers(headers).context(context).encoding(encoding));
    }
    /**
     * 以Get方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return	返回结果
     */
    public  String get(HttpRequestConfig config) throws HttpProcessException {
        return send(config.method(HttpMethods.GET));
    }

    /**
     * 以Post方式，请求资源或服务
     *
     * @param url			资源地址
     * @param headers		请求头信息
     * @param parasMap		请求参数
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public  String post(String url, Header[] headers, Map<String,Object> parasMap, HttpContext context, String encoding) throws HttpProcessException {
        return post(HttpRequestConfig.custom().url(url).headers(headers).map(parasMap).context(context).encoding(encoding));
    }
    /**
     * 以Post方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public  String post(HttpRequestConfig config) throws HttpProcessException {
        return send(config.method(HttpMethods.POST));
    }

    /**
     * 以Put方式，请求资源或服务
     *

     * @param url			资源地址
     * @param parasMap		请求参数
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public  String put(String url, Map<String,Object>parasMap,Header[] headers, HttpContext context,String encoding) throws HttpProcessException {
        return put(HttpRequestConfig.custom().url(url).headers(headers).map(parasMap).context(context).encoding(encoding));
    }
    /**
     * 以Put方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public  String put(HttpRequestConfig config) throws HttpProcessException {
        return send(config.method(HttpMethods.PUT));
    }

    /**
     * 以Delete方式，请求资源或服务
     *

     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     */
    public  String delete(String url, Header[] headers, HttpContext context,String encoding) throws HttpProcessException {
        return delete(HttpRequestConfig.custom().url(url).headers(headers).context(context).encoding(encoding));
    }
    /**
     * 以Delete方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public  String delete(HttpRequestConfig config) throws HttpProcessException {
        return send(config.method(HttpMethods.DELETE));
    }

    /**
     * 以Patch方式，请求资源或服务
     *

     * @param url			资源地址
     * @param parasMap		请求参数
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public  String patch(String url, Map<String,Object>parasMap, Header[] headers, HttpContext context,String encoding) throws HttpProcessException {
        return patch(HttpRequestConfig.custom().url(url).headers(headers).map(parasMap).context(context).encoding(encoding));
    }
    /**
     * 以Patch方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public  String patch(HttpRequestConfig config) throws HttpProcessException {
        return send(config.method(HttpMethods.PATCH));
    }

    /**
     * 以Head方式，请求资源或服务
     *

     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public  String head(String url, Header[] headers, HttpContext context,String encoding) throws HttpProcessException {
        return head(HttpRequestConfig.custom().url(url).headers(headers).context(context).encoding(encoding));
    }
    /**
     * 以Head方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public  String head(HttpRequestConfig config) throws HttpProcessException {
        return send(config.method(HttpMethods.HEAD));
    }

    /**
     * 以Options方式，请求资源或服务
     *

     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public  String options(String url, Header[] headers, HttpContext context,String encoding) throws HttpProcessException {
        return options(HttpRequestConfig.custom().url(url).headers(headers).context(context).encoding(encoding));
    }
    /**
     * 以Options方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public  String options(HttpRequestConfig config) throws HttpProcessException {
        return send(config.method(HttpMethods.OPTIONS));
    }

    /**
     * 以Trace方式，请求资源或服务
     *

     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public  String trace(String url, Header[] headers, HttpContext context, String encoding) throws HttpProcessException {
        return trace(HttpRequestConfig.custom().url(url).headers(headers).context(context).encoding(encoding));
    }
    /**
     * 以Trace方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public  String trace(HttpRequestConfig config) throws HttpProcessException {
        return send(config.method(HttpMethods.TRACE));
    }

    /**
     * 下载文件
     *

     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作Hh
     * @param out			输出流
     * @return				返回处理结果
     */
    public  OutputStream down(String url, Header[] headers, HttpContext context, OutputStream out) throws HttpProcessException {
        return down(HttpRequestConfig.custom().url(url).headers(headers).context(context).out(out));
    }

    /**
     * 下载文件
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public  OutputStream down(HttpRequestConfig config) throws HttpProcessException {
        if(config.method() == null) {
            config.method(HttpMethods.GET);
        }
        return fmt2Stream(execute(config), config.out());
    }

    /**
     * 转化为流
     *
     * @param resp			响应对象
     * @param out			输出流
     * @return				返回输出流
     * @throws HttpProcessException	http处理异常
     */
    public  OutputStream fmt2Stream(HttpResponse resp, OutputStream out) throws HttpProcessException {
        try {
            resp.getEntity().writeTo(out);
            EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {
            throw new HttpProcessException(e);
        }finally{
            close(resp);
        }
        return out;
    }

    /**
     * 上传文件
     *

     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @return				返回处理结果
     */
    public  String upload(String url, Header[] headers, HttpContext context) throws HttpProcessException {
        return upload(HttpRequestConfig.custom().url(url).headers(headers).context(context));
    }

    /**
     * 上传文件
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public  String upload(HttpRequestConfig config) throws HttpProcessException {
        if(config.method() != HttpMethods.POST  && config.method() != HttpMethods.PUT){
            config.method(HttpMethods.POST);
        }
        return send(config);
    }

    /**
     * 查看资源链接情况，返回状态码
     *

     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @return				返回处理结果
     * @throws HttpProcessException	http处理异常
     */
    public  int status(String url, Header[] headers, HttpContext context, HttpMethods method) throws HttpProcessException {
        return status(HttpRequestConfig.custom().url(url).headers(headers).context(context).method(method));
    }

    /**
     * 查看资源链接情况，返回状态码
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     * @throws HttpProcessException	http处理异常
     */
    public  int status(HttpRequestConfig config) throws HttpProcessException {
        return fmt2Int(execute(config));
    }

    /**
     * 转化为数字
     *
     * @param resp			响应对象
     * @return				返回处理结果
     * @throws HttpProcessException	http处理异常
     */
    private  int fmt2Int(HttpResponse resp) throws HttpProcessException {
        int statusCode;
        try {
            statusCode = resp.getStatusLine().getStatusCode();
            EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {
            throw new HttpProcessException(e);
        }finally{
            close(resp);
        }
        return statusCode;
    }

    /**
     * 请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public  String send(HttpRequestConfig config) throws HttpProcessException {
        return fmt2String(execute(config), config.outenc());
    }

    /**
     * 请求资源或服务，返回HttpResult对象
     *
     * @param config		请求参数配置
     * @return				返回HttpResult处理结果
     * @throws HttpProcessException	http处理异常
     */
    public  HttpResult sendAndGetResp(HttpRequestConfig config) throws HttpProcessException {
        Header[] reqHeaders = config.headers();
        //执行结果
        HttpResponse resp =  execute(config);

        HttpResult result = new HttpResult(resp);
        result.setResult(fmt2String(resp, config.outenc()));
        result.setReqHeaders(reqHeaders);

        return result;
    }

    /**
     * 请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回HttpResponse对象
     * @throws HttpProcessException	http处理异常
     */
    private HttpResponse execute(HttpRequestConfig config) throws HttpProcessException {
        HttpResponse resp = null;

        try {
            //创建请求对象
            HttpRequestBase request = getRequest(config.url(), config.method());

            //设置超时
            request.setConfig(config.getRequestConfig());

            //设置header信息
            request.setHeaders(config.headers());

            //判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
            if(HttpEntityEnclosingRequestBase.class.isAssignableFrom(request.getClass())){
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();

                if(request.getClass()==HttpGet.class) {
                    //检测url中是否存在参数
                    //注：只有get请求，才自动截取url中的参数，post等其他方式，不再截取
                    config.url(Utils.checkHasParas(config.url(), nvps, config.inenc()));
                }

                //装填参数
                HttpEntity entity = Utils.map2HttpEntity(nvps, config.map(), config.inenc());

                //设置参数到请求对象中
                ((HttpEntityEnclosingRequestBase)request).setEntity(entity);

                Utils.info("请求地址："+config.url());
                if(nvps.size()>0){
                    Utils.info("请求参数："+nvps.toString());
                }
                if(config.json()!=null){
                    Utils.info("请求参数："+config.json());
                }
            }else{
                int idx = config.url().indexOf("?");
                Utils.info("请求地址："+config.url().substring(0, (idx>0 ? idx : config.url().length())));
                if(idx>0){
                    Utils.info("请求参数："+config.url().substring(idx+1));
                }
            }
            //执行请求操作，并拿到结果（同步阻塞）
            resp = (config.context()==null)? httpClientBuilder.build().execute(request) : httpClientBuilder.build().execute(request, config.context()) ;

            if(config.isReturnRespHeaders()){
                //获取所有response的header信息
                config.headers(resp.getAllHeaders());
            }

            //获取结果实体
            return resp;

        } catch (IOException e) {
            throw new HttpProcessException(e);
        }
    }

    /**
     * 转化为字符串
     *
     * @param resp			响应对象
     * @param encoding		编码
     * @return				返回处理结果
     * @throws HttpProcessException	http处理异常
     */
    private  String fmt2String(HttpResponse resp, String encoding) throws HttpProcessException {
        String body = "";
        try {
            if (resp.getEntity() != null) {
                // 按指定编码转换结果实体为String类型
                body = EntityUtils.toString(resp.getEntity(), encoding);
                Utils.info(body);
            }else{//有可能是head请求
                body =resp.getStatusLine().toString();
            }
            EntityUtils.consume(resp.getEntity());
            return body;
        } catch (IOException e) {
            throw new HttpProcessException(e);
        }finally{
            close(resp);
        }
    }


    /**
     * 根据请求方法名，获取request对象
     *
     * @param url			资源地址
     * @param method		请求方式
     * @return				返回Http处理request基类
     */
    private  HttpRequestBase getRequest(String url, HttpMethods method) {
        HttpRequestBase request = null;
        switch (method.getCode()) {
            case 0:// HttpGet
                request = new HttpGet(url);
                break;
            case 1:// HttpPost
                request = new HttpPost(url);
                break;
            case 2:// HttpHead
                request = new HttpHead(url);
                break;
            case 3:// HttpPut
                request = new HttpPut(url);
                break;
            case 4:// HttpDelete
                request = new HttpDelete(url);
                break;
            case 5:// HttpTrace
                request = new HttpTrace(url);
                break;
            case 6:// HttpPatch
                request = new HttpPatch(url);
                break;
            case 7:// HttpOptions
                request = new HttpOptions(url);
                break;
            default:
                request = new HttpPost(url);
                break;
        }
        return request;
    }

    /**
     * 尝试关闭response
     *
     * @param resp				HttpResponse对象
     */
    private  void close(HttpResponse resp) {
        try {
            if(resp == null) return;
            //如果CloseableHttpResponse 是resp的父类，则支持关闭
            if(CloseableHttpResponse.class.isAssignableFrom(resp.getClass())){
                ((CloseableHttpResponse)resp).close();
            }
        } catch (IOException e) {
            Utils.exception(e);
        }
    }
}
