package tools;

import java.io.InputStream;

import org.mybatis.generator.logging.LogFactory;

import com.yaochen.builder.mybatis.Main;
import com.yaochen.builder.mybatis.YCMyBatisGenerator;
import com.yaochen.builder.mybatis.exception.GeneratorException;

/**
 * MyBatis 代码生成器
 * 
 * @author Killer
 */
public class Generator {
	
	static final String DEFAULT_CONFIGURATION_PATH = "generatorConfiguration.xml";
	static final String DEFAULT_CONFIGURATION_TABLES_PATH = "generatorConfiguration-tables.xml";

	static final String DEFAULT_CLASSPATH = "/";

	
	private static void runIt() throws GeneratorException {

		InputStream mybatisStream = Main.class
				.getResourceAsStream(DEFAULT_CLASSPATH
						+ DEFAULT_CONFIGURATION_PATH);
		InputStream ycStream = Main.class.getResourceAsStream(DEFAULT_CLASSPATH
				+ DEFAULT_CONFIGURATION_TABLES_PATH);

		try {
			new YCMyBatisGenerator().generator(mybatisStream, ycStream);
		} catch (GeneratorException e) {
			throw new RuntimeException("Generator Modules failure.", e);
		}
	}

	
	public static void main(String[] args) {
		try {
			runIt();
			
			LogFactory.getLog(Main.class).warn("completed.");
		} catch (GeneratorException e) {
			e.printStackTrace();
		}
	}
}
