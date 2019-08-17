package com.rw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class MusicServlet
 */
@WebServlet("/musicServlet")
public class MusicServlet extends HttpServlet {
//

//zz:住址

	static int 整数 = 100;
	public static void main(String[] args) {
		System.out.println(整数);
	}
	
	//www.jlzhou.top/Music
	//刘盼民	90	29
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 * 
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String method = request.getParameter("method");
		if (method == null || "querySongs".equals(method)) {
			String count = request.getParameter("count");
			//String source = request.getParameter("source");
			String pages = request.getParameter("pages");
			String name = request.getParameter("name");
			if (count == null) {
				count = "5";//如果为空，默认是5个长度
			}
			if (pages == null || Integer.parseInt(pages) <= 1) {
				pages = "1";
			}
			
			//....
			String[] names = {ParamsConstants.NETEASE,ParamsConstants.TENCENT,ParamsConstants.KUGOU};
			JSONObject object = new JSONObject();
			JSONObject result = new JSONObject();
			for (int n = 0; n < names.length; n++) {
				//System.out.println(names[n]);
				String songs = SongInfos.search(count, names[n], pages, name);
				if (songs == null || songs.equals("")) {
					continue;
				}
				JSONArray jsonArray = JSONArray.parseArray(songs);
				
				List<Song> list = new ArrayList<Song>();
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					//System.out.println(jsonObject);
					Song song = new Song();
					song.setArtist(jsonObject.getString("artist"));//歌手名称
					song.setPic_id(jsonObject.getString("pic_id"));//图片编号
					song.setLyric_id(jsonObject.getString("lyric_id"));//歌词id
					song.setPic_info(SongInfos.replaceUrl(SongInfos.getPicture(jsonObject.getString("pic_id"), jsonObject.getString("source"))));//图片地址
					System.out.println("图片："+song.getPic_info());
					//song.setLyric_info(SongInfos.getLyric(jsonObject.getString("lyric_id"), jsonObject.getString("source")));//歌词具体信息
					//System.out.println(SongInfos.replaceUrl(SongInfos.getSong(jsonObject.getString("id"), jsonObject.getString("source"))));//获取歌曲的下载地址（这个是随时变动的，所以每次都要重新获取）
//					String songUrl = SongInfos.replaceUrl(SongInfos.getSong(jsonObject.getString("id"), jsonObject.getString("source")));
//					if (songUrl != null && !"".equals(songUrl)) {
//						songUrl = JSONObject.parseObject(songUrl).getString("url");
//						song.setSong_url(songUrl);
//					}
					song.setAlbum(jsonObject.getString("album"));//专辑名称
					song.setSong_id(jsonObject.getString("id"));//歌曲id
					song.setSource(jsonObject.getString("source"));//资源来源
					song.setName(jsonObject.getString("name"));//歌曲名称
					song.setUrl_id(jsonObject.getString("url_id"));//下载地址的编号
					song.setCreatetime(sdf.format(new Date()));//设置当前时间
					//System.out.println(song);
					list.add(song);
					//System.out.println("=====================================");
				}
				object.fluentPut(names[n], list);
			}
			
			//酷我的音乐资源
			String kuwoSongsJson = SongInfos.getKuWoSongs(name, pages, count);
			if (name != null && !"".equals(name) && kuwoSongsJson != null && !"".equals(kuwoSongsJson)) {
				JSONArray array = JSONObject.parseObject(kuwoSongsJson).getJSONObject("data").getJSONArray("list");
				List<Song> list = new ArrayList<Song>();
				for (Object object2 : array) {
					JSONObject kuwoSong = (JSONObject) object2;
					Song song = new Song();
					song.setSong_id(kuwoSong.getString("musicrid"));//获取音乐id
					//song.setSong_url(SongInfos.getKuWoUrl(kuwoSong.getString("musicrid")));//获取下载链接
					song.setArtist(kuwoSong.getString("artist"));
					song.setAlbum(kuwoSong.getString("album"));
					song.setName(kuwoSong.getString("name"));
					song.setPic_info(kuwoSong.getString("pic"));
					song.setSource("kuwo");
					list.add(song);
				}
				object.fluentPut("kuwo", list);
			}
			
			result.fluentPut("songs", object);
			result.fluentPut("pages", pages);
			PrintWriter out = response.getWriter();
			if (name == null || "".equals(name)) {
				result.clear();
			}
			out.write(result.toString());
			out.flush();
			out.close();
		} else if ("downMusics".equals(method)) {
			//此方法失败，网络链接的输入流无法进行读取
			String song_id = request.getParameter("song_id");//歌曲编号
			String source = request.getParameter("source");//资源
			String name = request.getParameter("name");//歌名
			String artist = request.getParameter("artist");//歌手
			String songUrl = SongInfos.replaceUrl(SongInfos.getSong(song_id, source));
			if (songUrl != null && !"".equals(songUrl)) {
				InputStream in = null;
				OutputStream out = null;
				try {
					songUrl = JSONObject.parseObject(songUrl).getString("url");
					
					in = new URL(songUrl).openConnection().getInputStream();
					
					//in = new FileInputStream("C:\\Users\\我爱陈果果\\Downloads\\154812657.mp3");
					
					out = response.getOutputStream();
					// 3.设置让浏览器不进行缓存，不然会发现下载功能在opera和firefox里面好好的没问题，在IE下面就是不行，就是找不到文件
//		            response.setHeader("Pragma", "No-cache");
//		            response.setHeader("Cache-Control", "No-cache");
//		            response.setDateHeader("Expires", -1);
		            
		            //拼接文件名
		            String fileName = artist + "—" + name + songUrl.substring(songUrl.lastIndexOf("."));
		            
		            // 5.设置http响应头，告诉浏览器以下载的方式处理我们的响应信息
		            response.setHeader("content-disposition", "attachment;filename=" + fileName);
		            
		            // 6.开始写文件
		            byte[] buf = new byte[1024*4];
		            int len = 0;
		            while ((len = in.read(buf)) != -1) {
		                out.write(buf, 0, len);
		            }
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
					response.flushBuffer();
				}
			}
		} else if ("downMusic".equals(method)) {
			//下载单首音乐
			String song_id = request.getParameter("song_id");
			String source = request.getParameter("source");
			String songUrl = "";
			if ("kuwo".equals(source)) {
				songUrl = SongInfos.getKuWoUrl(song_id);
			} else {
				songUrl = SongInfos.replaceUrl(SongInfos.getSong(song_id, source));
				if (songUrl != null && !"".equals(songUrl)) {
					songUrl = JSONObject.parseObject(songUrl).getString("url");
				}
				if ("tencent".equals(source)) {
					songUrl = SongInfos.checkTencentUrl(songUrl);
				}
			}
			if (songUrl == null || "".equals(songUrl)) {
				PrintWriter write = response.getWriter();
				write.write("<h1>小哥哥小姐姐们，该歌曲资源不存在，暂时不支持下载哦~程序猿哥哥正在努力中....</h1>");
				return;
			}
			//直接进行
			response.sendRedirect(songUrl);
			//System.out.println(songUrl);
//			PrintWriter out = response.getWriter();
//			out.write(songUrl);
//			out.flush();
//			out.close();
		} else if ("downLrc".equals(method)) {
			//SongInfos.getLyric(jsonObject.getString("lyric_id"), jsonObject.getString("source"))
			String lyric_id = request.getParameter("lyric_id");//歌曲编号
			String source = request.getParameter("source");//资源
			String name = request.getParameter("name");//歌名
			String artist = request.getParameter("artist");//歌手
			String lrc = SongInfos.getLyric(lyric_id, source);
			if ("kuwo".equals(source)) {
				String song_id = request.getParameter("song_id");
				song_id = song_id.split("\\_")[1];//取后面的id
				lrc = SongInfos.getKuwoLrc(song_id);
			}
			//歌词不存在，直接提示
			if (lrc == null || "".equals(lrc)) {
				PrintWriter write = response.getWriter();
				write.write("<h1>小哥哥小姐姐们，该歌词暂时不支持下载哦~程序猿哥哥正在努力中....</h1>");
				return;
			} 
			InputStream in = null;
			OutputStream repout = null;
			try {
				String fileName = artist+"—"+name+".lrc";
				String tempPath = request.getServletContext().getRealPath("lrc")+"\\"+fileName;
				File file = new File(tempPath);
				if (file == null || !file.exists()) {
					//System.out.println("文件不存在，下载。");
					OutputStream out = new FileOutputStream(tempPath);
					out.write(lrc.getBytes());
					out.flush();
					out.close();
				} 
				//System.out.println("\"" + fileName+"\"");
				//获取文件的输入流
				in = new FileInputStream(file);
				//获取相应输出流
				repout = response.getOutputStream();
				// 5.设置http响应头，告诉浏览器以下载的方式处理我们的响应信息
	            response.setHeader("content-disposition", "attachment;filename=\"" + new String(fileName.getBytes("GBK"),"ISO8859_1") +"\"");
	            
	            // 6.开始写文件
	            byte[] buf = new byte[1024*4];
	            int len = 0;
	            while ((len = in.read(buf)) != -1) {
	            	repout.write(buf, 0, len);
	            }
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					in.close();
				}
				if (repout != null) {
					repout.close();
				}
				response.flushBuffer();
			}
		}
	}

}
