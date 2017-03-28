package com.export.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.*;

/**
 * Freemarker 模版引擎类
 * Created by qiboo on 2017/1/17.
 * @version 1.0
 */
public class Freemarker {

	/**
	 * 打印到控制台(测试用)
	 *  @param ftlName
	 */
	public static void print(String ftlName, Map<String,Object> root, String ftlPath) throws Exception{
		try {
			Template temp = getTemplate(ftlName, ftlPath);		//通过Template可以将模板文件输出到相应的流
			temp.process(root, new PrintWriter(System.out));
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出到输出到文件
	 * @param ftlName   ftl文件名
	 * @param root		传入的map
	 * @param outFile	输出后的文件全部路径
	 * @param filePath	输出前的文件上部路径
	 */
	public static File printFile(String ftlName, Map<String,Object> root, String outFile,  String ftlPath) throws Exception{
		File file = new File( outFile);
		try {
			if(!file.getParentFile().exists()){			//判断有没有父路径，就是判断文件整个路径是否存在
				file.getParentFile().mkdirs();			//不存在就全部创建
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			Template template = getTemplate(ftlName, ftlPath);
			template.process(root, out);		//模版输出
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	/**
	 * 通过文件名加载模版
	 * @param ftlName
	 */
	public static Template getTemplate(String ftlName, String ftlPath) throws Exception{
		try {
			Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS); 	//通过Freemaker的Configuration读取相应的ftl
			cfg.setEncoding(Locale.CHINA, "utf-8");
			cfg.setDirectoryForTemplateLoading(new File(PathUtil.getClassResources()+"/ftl/"+ftlPath)); //设定去哪里读取相应的ftl模板文件
			Template temp = cfg.getTemplate(ftlName);		//在模板文件目录中找到名称为name的文件
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static void main(String[] args) {
		/*resume简历对象*/
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("name", "码农");
        dataMap.put("phone", "18793409302");
        dataMap.put("address", "上海市");
        dataMap.put("mail", "xxxxxx@xiaozao.org");
        dataMap.put("language", "普通话与闽南语（母语），英语");
        dataMap.put("skill", "MATLAB，MS Office，R，彭博终端");
        dataMap.put("related_info", "国际青年成就（JA）广州地区企业志愿者，连续五年担任课程讲师辅导中学生职业发展");
        dataMap.put("hobby", "马拉松、蹦极、足球");

        /*教育背景列表对象*/
        List<Map<String, Object>> eduExp = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 2; i++) {
            Map<String,Object> map = new HashMap();
            map.put("school", "复旦大学");
            map.put("education", "工学硕士");
            map.put("subject", "经理");
            List<String> edu_experience = new ArrayList<String>();
            for (int j = 0; j < 2; j ++) {
				edu_experience.add("综合绩点 3.62/4.00（10/107）；二等奖学金 金融协会副会长（负责活动策划与执行），优秀研究生（4/87）");
			}
            map.put("edu_experience",edu_experience);
            map.put("city", "上海");
            map.put("start_time", "2001");
            map.put("end_time", "2005");
			eduExp.add(map);
        }
        dataMap.put("eduExp", eduExp);

        /*工作经历课外实践*/
        List<Map<String, Object>> experience = new ArrayList<Map<String, Object>>();
        for (int k = 0; k < 2; k ++) {
        	Map<String, Object> exMap = new HashMap<String, Object>();
        	exMap.put("title", "工作经历");
        	if (k == 1) {
        		exMap.put("title", "课外实践");
			}
			/*工作经历列表对象*/
			List<Map<String, Object>> workExp = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < 2; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("company", "普华永道");
				map.put("department", "企业管理咨询业务");
				map.put("position", "经理");
				map.put("city", "上海");
				map.put("start_time", "1999");
				map.put("end_time", "至今");
				//工作经历描述列表对象
				List<String> workDes = new ArrayList<String>();
				for (int j = 0; j < 4; j ++) {
					workDes.add("领导7人团队，建设某领先互联网金融公司对合作金融机构的授信管理体系及相关系统，设 计与开发工作了财富管理事业线某两个项目");
				}
				map.put("workDes", workDes);
				workExp.add(map);
			}
			exMap.put("workExp", workExp);
			experience.add(exMap);
		}
		dataMap.put("experience", experience);

        try {
//			print("resume.xml",dataMap,"");
			printFile("resume.xml", dataMap, "resume1.doc", "");
		} catch (Exception e) {
        	e.printStackTrace();
		}


	}

}
