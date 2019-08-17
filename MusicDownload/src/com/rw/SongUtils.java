package com.rw;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 	歌曲操作工具
 */
public class SongUtils {

	/**
	 * 	所有请求的地址都是这个
	 */
	public static final String URL = "http://www.gequdaquan.net/gqss/api.php";
	
	/**
	 * 	输入对应的参数，获取对应的json数据--get请求
	 * @param url 需要解析的地址
	 * @param params 参数地址
	 * @return 返回一个对应的json数据
	 */
	public static String httpGetCallbackData(String url, List<NameValuePair> params) {
		//获取连接客户端工具
		BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(connManager)
				.build();
		//网址返回的json数据
		String entityStr = null;
		//相应工具
	    CloseableHttpResponse response = null;
		try {
			//由于GET请求的参数都是拼装在URL地址后方，所以我们要构建一个URL，带参数
	        URIBuilder uriBuilder = new URIBuilder(url);
	        
	        //添加参数的形式
			uriBuilder.setParameters(params);
	        
			// 创建POST请求对象
			HttpGet httpGet = new HttpGet(uriBuilder.build());
			//设置请求参数
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(5000)
					.setConnectTimeout(50000)
					.setConnectionRequestTimeout(50000)
					.build();
			//加载请求参数
			httpGet.setConfig(requestConfig);
			
			//添加请求头信息
//			httpGet.setHeader("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
//			httpGet.setHeader("Accept-Encoding", "gzip, deflate");
//			httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
//			httpGet.setHeader("Connection", "keep-alive");
			httpGet.setHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
//			httpGet.setHeader("Host", "www.gequdaquan.net");
//			httpGet.setHeader("Origin", "http://www.gequdaquan.net");
//			httpGet.setHeader("Referer", "http://www.gequdaquan.net/gqss/");
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
//			httpGet.setHeader("cookie","UM_distinctid=16b81df4399df-091c2da2ea9418-e343166-1fa400-16b81df439d20f; Hm_lvt_e941dfc465779f2553a65876b7d920fe=1561252021,1561261570,1561262579,1561303696; CNZZDATA1275011118=1020311708-1561249086-%7C1561303709; Hm_lpvt_e941dfc465779f2553a65876b7d920fe=1561303723");
//			httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
			
			// 执行请求
			response = httpclient.execute(httpGet);
			// 获得响应的实体对象
			HttpEntity entity = response.getEntity();
	        // 使用Apache提供的工具类进行转换成字符串
			entityStr = EntityUtils.toString(entity, "UTF-8");
			// 打印响应内容
		    //System.out.println(entityStr);
		} catch (ClientProtocolException e) {
	        System.err.println("Http协议出现问题");
	        e.printStackTrace();
	    } catch (ParseException e) {
	        System.err.println("解析错误");
	        e.printStackTrace();
	    } catch (URISyntaxException e) {
	        System.err.println("URI解析异常");
	        e.printStackTrace();
	    } catch (IOException e) {
	        System.err.println("IO异常");
	        e.printStackTrace();
	    } finally {
	    	// 释放连接
	        if (null != response) {
	            try {
	                response.close();
	                httpclient.close();
	            } catch (IOException e) {
	                System.err.println("释放连接出错："+e.getMessage());
	            }
	        }
	    }
		return entityStr;
	}
	
	/**
	 * 	输入对应的参数，获取对应的json数据--post请求
	 * @param url 需要解析的地址
	 * @param params 参数地址
	 * @return 返回一个对应的json数据
	 */
	public static String httpPostCallbackData(String url, List<NameValuePair> params) {
		//获取连接客户端工具
		BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(connManager)
				.build();
		//网址返回的json数据
		String entityStr = null;
		//相应工具
	    CloseableHttpResponse response = null;
		try {
			// 创建POST请求对象
			HttpPost httpPost = new HttpPost(url);
			//设置请求参数
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(5000)
					.setConnectTimeout(50000)
					.setConnectionRequestTimeout(50000)
					.build();
			//加载请求参数
			httpPost.setConfig(requestConfig);
			
			//添加请求头信息
			
//			httpPost.setHeader("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
//			httpPost.setHeader("Accept-Encoding", "gzip, deflate");
//			httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
//			httpPost.setHeader("Connection", "keep-alive");
			httpPost.setHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
//			httpPost.setHeader("Host", "www.gequdaquan.net");
//			httpPost.setHeader("Origin", "http://www.gequdaquan.net");
//			httpPost.setHeader("Referer", "http://www.gequdaquan.net/gqss/");
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
//			httpPost.setHeader("cookie","UM_distinctid=16b81df4399df-091c2da2ea9418-e343166-1fa400-16b81df439d20f; Hm_lvt_e941dfc465779f2553a65876b7d920fe=1561252021,1561261570,1561262579,1561303696; CNZZDATA1275011118=1020311708-1561249086-%7C1561303709; Hm_lpvt_e941dfc465779f2553a65876b7d920fe=1561303723");
//			httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
			
			// 添加请求参数，使用URL实体转换工具
	        HttpEntity httpEntity = new UrlEncodedFormEntity(params,"UTF-8");
			httpPost.setEntity(httpEntity);
			
			// 执行请求
			response = httpclient.execute(httpPost);
			// 获得响应的实体对象
			HttpEntity entity = response.getEntity();
	        // 使用Apache提供的工具类进行转换成字符串
			entityStr = EntityUtils.toString(entity, "UTF-8");
			// 打印响应内容
		    //System.out.println(entityStr);
		} catch (ClientProtocolException e) {
	        System.err.println("Http协议出现问题："+e.getMessage());
	    } catch (ParseException e) {
	        System.err.println("解析错误："+e.getMessage());
	    } catch (IOException e) {
	        System.err.println("IO异常："+e.getMessage());
	    } finally {
	    	// 释放连接
	        if (null != response) {
	            try {
	                response.close();
	                httpclient.close();
	            } catch (IOException e) {
	                System.err.println("释放连接出错"+e.getMessage());
	            }
	        }
	    }
		return entityStr;
	}
	
	//单独查询酷我的数据
	public static String callbackKuWo(Map<String, String> paramsMap) {
		String url = "http://www.kuwo.cn/api/www/search/searchMusicBykeyWord";
		if (paramsMap == null || paramsMap.isEmpty()) {
			return null;
		}
		List<NameValuePair> list = new LinkedList<>();
		for (String key : paramsMap.keySet()) {
			list.add(new BasicNameValuePair(key, paramsMap.get(key)));
		}
		//访问接口
		return httpGetCallbackData(url, list);
	}
	
	/**
	 * 	传递参数，获取对应数据
	 * @return
	 */
	public static String callback(Map<String, String> paramsMap) {
		if (paramsMap == null || paramsMap.isEmpty()) {
			return null;
		}
		List<NameValuePair> list = new LinkedList<>();
		for (String key : paramsMap.keySet()) {
			list.add(new BasicNameValuePair(key, paramsMap.get(key)));
		}
		//访问接口
		return httpPostCallbackData(URL, list);
	}
	
	public static void main(String[] args) {
		String songJson = SongInfos.getKuWoSongs("周杰伦", "1", "10");
		JSONArray array = JSONObject.parseObject(songJson).getJSONObject("data").getJSONArray("list");
		System.out.println(array);
	}
}
