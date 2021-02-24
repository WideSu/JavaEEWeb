package org.hqu.vibsignal_analysis.controller.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * created on 2016年9月6日 
 *
 * 自定义日期转换器
 *
 * @author  megagao
 * @version  0.0.1
 */
public class DateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		
		try {
			//进行日期转换
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(source);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
