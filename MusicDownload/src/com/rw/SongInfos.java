package com.rw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

//所有歌曲
public class SongInfos {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static StringBuffer sb = new StringBuffer();
	public static DecimalFormat df = new DecimalFormat("00.00");
	/**
	 * 	获取播放列表
	 * @id 对应排行榜的编号
	 * @return
	 */
	public static String playList(String id) {
		//播放列表
		//参数：
		//types：播放列表类型
		//id：对应列表的编号
		
		//返回参数：
		//所有跟对应排行榜相关的json数据
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("types", ParamsConstants.PLAYLIST);
		paramsMap.put("id", id);
		try {
			//访问接口
			return SongUtils.callback(paramsMap);
		} catch (Exception e) {
			System.err.println("歌曲列表搜索错误："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 	搜索
	 * @count 每页显示数量
	 * @source 哪个网站资源
	 * @pages 页码 1开始
	 * @name 歌手、歌名、专辑
	 */
	public static String search(String count, String source, String pages, String name) {
		//播放列表
		//参数：
		//types：播放列表类型
		//count 每页显示数量
		//source 哪个网站资源
		//pages 页码 1开始
		//name 歌手、歌名、专辑
		
		//返回参数：
		//整个歌曲的列表
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("types", ParamsConstants.SEARCH);
		paramsMap.put("count", count);
		paramsMap.put("source", source);
		paramsMap.put("pages", pages);
		paramsMap.put("name", name);
		//访问接口
		return SongUtils.callback(paramsMap);
	}
	
	/**
	 * 	获取歌曲
	 * @id 对应歌曲的编号
	 * @source 对应网站资源
	 */
	public static String getSong(String id, String source) {
		//播放列表
		//参数：
		//types：歌曲类型
		//id：对应歌曲的编号
		//source：对应网站资源
		
		//返回参数：
		//歌曲的试听（下载）地址
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("types", ParamsConstants.URL);
		paramsMap.put("id", id);
		paramsMap.put("source", source);
		
		//访问接口
		String songinfo = SongUtils.callback(paramsMap);
		//将qq音乐地址更改一下，否则会出现403拒绝访问
		if ("tencent".equals(source)) {
			if (songinfo != null && !songinfo.equals("")) {
				if (songinfo.contains("/M800")) {
					return songinfo.replaceAll("/M800", "/M500");
				} else {
					return songinfo.replaceAll("/M500", "/M800");
				}
			} else {
				return null;
			}
		} else {
			return songinfo;
		}
	}
	
	/**
	 * 	获取歌词
	 * @id 对应歌曲的编号
	 * @source 对应网站资源
	 */
	public static String getLyric(String id, String source) {
		//播放列表
		//参数：
		//types：歌词类型
		//id：歌曲的编号
		//source：对应网站资源
		
		//返回参数：
		//歌词
		Map<String, String> paramsMap = new HashMap<String, String>();
		try {
			paramsMap.put("types", ParamsConstants.LYRIC);
			paramsMap.put("id", id);
			paramsMap.put("source", source);
			//访问接口
			return JSONObject.parseObject(SongUtils.callback(paramsMap)).getString("lyric");
		} catch (Exception e) {
			System.err.println("获取歌词错误："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 	获取图片
	 * @id 对应图片的编号
	 * @source 对应网站资源90
	 */
	public static String getPicture(String id, String source) {
		//播放列表
		//参数：
		//types：图片类型
		//id：对应图片的编号
		//source：具体是哪个网站的资源
		
		//返回参数：
		//对应歌曲的封面图片
		Map<String, String> paramsMap = new HashMap<String, String>();
		try {
			paramsMap.put("types", ParamsConstants.PICTURE);
			paramsMap.put("id", id);
			paramsMap.put("source", source);
			//访问接口
			return JSONObject.parseObject(SongUtils.callback(paramsMap)).getString("url");
		} catch (Exception e) {
			System.err.println("获取图片错误："+e.getMessage());
		}
		return null;
	}
	
	//去除\
	public static String replaceUrl(String url) {
		if (url == null) {
			return null;
		}
		return url.replaceAll("\\\\", "");
	}
	
	//单独查询酷我的数据
	public static String getKuWoSongs(String key, String pn, String rn) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		//key="+key+"&pn="+pn+"&rn="+rn
		paramsMap.put("key", key);
		paramsMap.put("pn", pn);
		paramsMap.put("rn", rn);
		return SongUtils.callbackKuWo(paramsMap);
	}
	
	//获取酷我的下载地址
	public static String getKuWoUrl(String mid) {
		String aac_url="";
		  try {  
	            String url = "http://antiserver.kuwo.cn/anti.s?format=aac|mp3&rid="+mid+"&type=convert_url&response=res";  
	            
	            URL serverUrl = new URL(url);  
	            HttpURLConnection conn = (HttpURLConnection) serverUrl  
	                    .openConnection();  
	            conn.setRequestMethod("GET");  
	            conn.setInstanceFollowRedirects(false);  
	  
	            conn.addRequestProperty("Accept-Charset", "UTF-8;");  
	            conn.addRequestProperty("User-Agent",  
	                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");  
	            conn.addRequestProperty("Referer", "http://zuidaima.com/");  
	            conn.connect();  
	            String location = conn.getHeaderField("Location");  
	  
	            serverUrl = new URL(location);  
	            conn = (HttpURLConnection) serverUrl.openConnection();  
	            conn.setRequestMethod("GET");  
	  
	            conn.addRequestProperty("Accept-Charset", "UTF-8;");  
	            conn.addRequestProperty("User-Agent",  
	                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");  
	            conn.addRequestProperty("Referer", "http://zuidaima.com/");  
	            conn.connect();  
	            aac_url= location;  
	  
	        } catch (Exception e) {  
	        	System.out.println("酷我歌曲链接获取错误："+e.getMessage());
	        }  
		  return aac_url;
	}
	
	//将时间进行解析
	private static void parseTime(JSONObject jsonObject, StringBuffer sb) {
		sb.append("[");
		double time = Double.parseDouble(jsonObject.getString("time"));
		df.applyPattern("00");
		sb.append(df.format(time/60));
		sb.append(":");
		df.applyPattern("00.00");
		sb.append(df.format(time%60));
		sb.append("]");
	}
	
	//单独获取酷我的歌词
	public static String getKuwoLrc(String musicId) {
		sb.setLength(0);
		String url = "http://m.kuwo.cn/newh5/singles/songinfoandlrc";
		List<NameValuePair> paramsMap = new LinkedList<>();
		paramsMap.add(new BasicNameValuePair("musicId", musicId));
		try {
			JSONArray jsonArray = JSONObject.parseObject(SongUtils.httpGetCallbackData(url, paramsMap))
			.getJSONObject("data").getJSONArray("lrclist");
			for (Object object : jsonArray) {
				JSONObject jsonObject = (JSONObject) object;
				//增加时间部分
				parseTime(jsonObject, sb);
				//增加歌词部分
				sb.append(jsonObject.getString("lineLyric"));
				sb.append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			System.out.println("kuwo歌词获取错误："+e.getMessage());
		}
		return null;
	}
	
	public static void main(String[] args) {
		//获取对应的
		//String playList = playList(ParamsConstants.CHINESE_TOP_HKTAIWANLIST);
		//System.out.println("playList="+playList);
        //"周杰伦","林俊杰","邓紫棋","林宥嘉","薛之谦","陈奕迅",
		//"李荣浩","华晨宇","王力宏","张学友","陈雪凝","张惠妹",
		//"赵雷", "孙燕姿", "王菲"
		//"孙露","庄心妍","BEYOND","Beyond",
		//"刀郎","降央卓玛","刘德华",
		//"许嵩","毛不易","张杰",
		//"凤凰传奇","许巍","张靓颖",
		//"杨宗纬","周华健","王菲",
		//"王琪","魏新雨","张靓颖","张信哲",
		//"张国荣", "张敬轩","周笔畅",
		//"许飞","刘若英","朴树","韩红",
		//"李宗盛","罗大佑","梁静茹","陈瑞","莫文蔚","阿杜","蔡依林",
		//"S.H.E","五月天","李健","Richard Claydeman","Bandari",
		//"那英","筷子兄弟",
		//"萧敬腾","张惠妹","齐秦","杨坤","李圣杰","张雨生",
		//"飞儿乐团","陈小春","伍佰",
		//"宋东野","水木年华","郭富城","黎明","范玮琪",
		//"容祖儿","Westlife","宋祖英","徐佳莹","陶喆","陈慧琳","BIGBANG","韩磊","王心凌","迪克牛仔","郑秀文",
		//,"Richard Clayderman","弦子","后弦","彭佳慧",
		String[] names = {"The One"};
		//String rootPath = "F:\\AllMusic\\";
		//下载
		int n = 0;//页数
		for (int i = 0; i < names.length; i++) {
			for (int k = 0; k < 5; k++) {
				OutputStream out = null;
				InputStream in = null;
				n++;
				try {
					String songs = search("60", ParamsConstants.TENCENT, n + "", names[i]);
					if (songs == null || songs.equals("")) {
						continue;
					}
					JSONArray jsonArray = JSONArray.parseArray(songs);
					for (int j = 0; j < jsonArray.size(); j++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(j);
						//String lyric_id = jsonObject.getString("lyric_id");//歌词编号
						String source = jsonObject.getString("source");//资源
						String song_id = jsonObject.getString("id");//歌曲编号
						String artist = jsonObject.getString("artist");//歌手
						
						if (artist != null) {
							//处理[]和"
							artist = artist.replaceAll("[\\[\\]\"]", "");
							//处理/和\\
							artist = artist.replaceAll("[\\s\\\\/:\\*\\?\\\"<>\\|]", ",");
						} else {
							continue;
						}
						 
						String song_name = jsonObject.getString("name");//歌曲名称
						
						if (song_name != null) {
							song_name = song_name.replaceAll("[\\s\\\\/:\\*\\?\\\"<>\\|]", "");
						}
						
						//System.out.println("artist="+artist+",song_name="+song_name);
						
						//歌词全名
						//String filelrc_name = artist+"—"+song_name+".lrc";
						
						//歌曲全名
						String filesong_name = artist+"—"+song_name+".mp3";
						
						String savePath = "F:\\AllMusic\\"+names[i];
						
						//判断存储路径是否存在，不存在创建
						File saveFile = new File(savePath);
						if (!saveFile.exists()) {
							saveFile.mkdirs();
						}
						
						//歌词文件
						//File file_lrc = new File(savePath + "\\" + filelrc_name);
						
//						//歌词文件不存在，可以下载
//						if (file_lrc == null || file_lrc.length() == 0 || !file_lrc.exists()) {
//							//歌词内容
//							String lrc = SongInfos.getLyric(lyric_id, source);
//							out = new FileOutputStream(file_lrc);
//							out.write(lrc.getBytes());//将歌词直接写入到文件中
//							System.out.println("歌词【"+filelrc_name+"】下载完成。下载路径是："+file_lrc);
//						} else {
//							System.out.println("=================歌词【"+filelrc_name+"】已存在！！！！！！！=================");
//						}
						
						//歌曲文件
						File file_song = new File(savePath + "\\" + filesong_name);
						//歌曲不存在，可以下载
						if ((file_song == null || !file_song.exists())) {
							System.out.println(filesong_name);
							//歌曲地址
							String song_url = SongInfos.replaceUrl(SongInfos.getSong(song_id, source));
							
							//歌曲路径不存在，不下载歌曲
							if (song_url == null || "".equals(song_url)) {
								continue;
							}
							song_url = JSONObject.parseObject(song_url).getString("url");
							//获取歌曲的输入流
							in = getSongInputStream(song_url);
							//获取歌曲的输出流
							out = new FileOutputStream(file_song);
							//创建4kb缓冲区
							byte[] b = new byte[1024*4];
							//创建获取每次读取的真实字节大小
							int len = 0;
							while ((len = in.read(b)) != -1) {
								//将读取到的字节写入到磁盘中
								out.write(b, 0, len);
							}
							out.close();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
//		String[] names = {ParamsConstants.NETEASE,ParamsConstants.TENCENT,ParamsConstants.KUGOU
//				, ParamsConstants.BAIDU, ParamsConstants.XIAMI};
//		JSONObject object = new JSONObject();
//		for (int n = 0; n < names.length; n++) {
//			System.out.println(names[n]);
//			String songs = search("10", names[n], "1", "周杰伦");
//			if (songs == null || songs.equals("")) {
//				continue;
//			}
//			JSONArray jsonArray = JSONArray.parseArray(songs);
//			
//			List<Song> list = new ArrayList<Song>();
//			for (int i = 0; i < jsonArray.size(); i++) {
//				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//				//System.out.println(jsonObject);
//				Song song = new Song();
//				song.setArtist(jsonObject.getString("artist"));//歌手名称
//				song.setPic_id(jsonObject.getString("pic_id"));//图片编号
//				song.setLyric_id(jsonObject.getString("lyric_id"));//歌词id
//				song.setPic_info(replaceUrl(getPicture(jsonObject.getString("pic_id"), jsonObject.getString("source"))));//图片地址
//				song.setLyric_info(getLyric(jsonObject.getString("lyric_id"), jsonObject.getString("source")));//歌词具体信息
//				System.out.println(replaceUrl(getSong(jsonObject.getString("id"), jsonObject.getString("source"))));//获取歌曲的下载地址（这个是随时变动的，所以每次都要重新获取）
//				song.setAlbum(jsonObject.getString("album"));//专辑名称
//				song.setSong_id(jsonObject.getString("id"));//歌曲id
//				song.setSource(jsonObject.getString("source"));//资源来源
//				song.setName(jsonObject.getString("name"));//歌曲名称
//				song.setUrl_id(jsonObject.getString("url_id"));//下载地址的编号
//				song.setCreatetime(sdf.format(new Date()));//设置当前时间
//				//System.out.println(song);
//				list.add(song);
//				//System.out.println("=====================================");
//			}
//			object.fluentPut(names[n], list);
//		}
//		System.out.println(object);
	}
	
	//获取歌曲的输入流
	public static InputStream getSongInputStream(String song_url) {
		//歌曲的流
		InputStream in = null;
		try {
			in = new URL(song_url).openConnection().getInputStream();
		} catch (Exception e) {
			System.out.println("网址解析错误："+e.getMessage());
			try {
				if (song_url.contains("/M800")) {
					return new URL(song_url.replaceAll("/M800", "/M500")).openConnection().getInputStream();
				} else {
					return new URL(song_url.replaceAll("/M500", "/M800")).openConnection().getInputStream();
				}
			} catch (Exception e2) {
				System.out.println("网址解析错误第2次："+e.getMessage());
			}
		}
		return in;
	}
	
	public static String checkTencentUrl(String song_url) {
		//歌曲的流
		try {
			new URL(song_url).openConnection().getInputStream();
		} catch (Exception e) {
			System.out.println("网址解析错误："+e.getMessage());
			if (song_url.contains("/M800")) {
				return song_url.replaceAll("/M800", "/M500");
			} else {
				return song_url.replaceAll("/M500", "/M800");
			}
		}
		return song_url;
	}
}
