package org.pachou;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;

public class TestMain {
	
	//存放目录
	static String savePath = "G:\\douban_Picuture\\";
	//抓取深度
	static int deep = 30;
	
	public static void main(String[] args) {
		for (int i = 1; i <= deep; i++) {
            List<String> list = get(i);
            if (list == null || list.size() == 0) {
                break;
            } else {
                for (String s : list) {
                	System.out.println(s);
                    String type = s.substring(s.lastIndexOf("."));
                    if (s.indexOf("http://") == -1) {
                        s = "http://www.dbmeinv.com/" + s;
                    }
                    //保存的目录名称，可以修改
                    FileHelper.downloadWebFile(s, UUID.randomUUID() + type, savePath);
                }
            }
            System.out.println(i);
        }
	}
	
	private static List<String> get(int i) {
        InputStreamReader reader = null;
        BufferedReader in = null;
        try {
            URL url = new URL("http://www.dbmeinv.com/?pager_offset=" + i);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "MSIE");
            connection.setConnectTimeout(10000);
            reader = new InputStreamReader(connection.getInputStream(), "utf-8");
            in = new BufferedReader(reader);
            String line = null; // 每行内容
            int lineFlag = 0; // 标记: 判断有没有数据
            StringBuilder content = new StringBuilder();
            while ((line = in.readLine()) != null) {
                content.append(line);
                lineFlag++;
            }
            if (lineFlag >= 1) {
                return TextHelper.getTextImageSrc(content.toString());
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
