package cache;

import common.LogLevel;
import common.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProblemsCachManager {
	private boolean update = false;
	private BaseCache cache;
	private static ProblemsCachManager instance;
	private static Object lock = new Object();
        private static Logger logger=Logger.getInstance();
	private ProblemsCachManager() {
		// 这个根据配置文件来，初始BaseCache而已;
		Properties prop = new Properties();
		InputStream in = ProblemsCachManager.class
				.getResourceAsStream("cache.properties");
		try {
			prop.load(in);
			String validTime = prop.getProperty("problemsValidTime").trim();
			cache = new BaseCache("Problems", Integer.parseInt(validTime));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ProblemsCachManager getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ProblemsCachManager();
				}
			}
		}
		return instance;
	}

	public void putObject(String key, Object value) { // key以classId_examId
             logger.log("已放入缓存"+key, LogLevel.INFO);
		cache.put(key, value);
	}

	public void removeObject(String key) {
            logger.log("已移除缓存"+key, LogLevel.INFO);
		cache.remove(key);
	}

	public void removeAllObject() {
		cache.removeAll();
	}

	public Object getObject(String key) {
		try {
			// 从Cache中获得
			return cache.get(key);
		} catch (Exception e) {
                        logger.log("获取缓存出错:"+key+"e:"+e.getMessage(), LogLevel.ERROR);
			// Cache中没有则从DB库获取
			// 数据库中读取数据
			// 把获取的对象再次存入Cache中
			this.putObject(key, null);
			update = true;
			return null;
		} finally {
			if (!update) {
				// 如果Cache中的内容更新出现异常，则终止该方法
				cache.cancelUpdate(key); // 取消对id的更新
			}
		}
	}

}
