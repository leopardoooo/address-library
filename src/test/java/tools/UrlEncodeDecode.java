package tools;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


public class UrlEncodeDecode {
	@Test
	public void testEncode() throws Exception {
		String queryString = "UserName=%E5%BC%A0%E5%88%9A%E4%BC%9F&Password=12&OA=1";
		String gbk = "GBK";
		String utf = "UTF-8";
		queryString = "UserName=收拾收拾";
		Map<String, String> params = getRequestParam(queryString, utf);
		
		
		System.err.println(params.get("UserName"));
		
	}
	
	/**
	 * 之所以使用这么变态的方法是因为老是出现乱码.只好手工处理参数.
	 * @param queryString
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private Map<String, String> getRequestParam(String queryString, String encoding)
			throws UnsupportedEncodingException {
		String decode = java.net.URLDecoder.decode(queryString, encoding);
		Map<String, String> params = new HashMap<String, String>();
		if(decode.indexOf("=")<0){
			return params;
		}
		String[] split = decode.split("&");
		for (String param : split) {
			String[] pair = param.split("=");
			params.put(pair[0], pair[1]);
		}
		return params;
	}
	
	@Test
	public void testDecode() throws Exception {
		String name = "伟";
		String gbk = "GBK";
		String utf = "UTF-8";
		
		String encode = java.net.URLEncoder.encode(name,utf);
		System.err.println("encode  from " + name + " to : "+ encode);
	}
}
