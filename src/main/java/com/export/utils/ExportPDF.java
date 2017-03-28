package com.export.utils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created by qiboo on 2017/2/7.
 */
public class ExportPDF {


    public static File exportPdf(String ftlName, Map<String, Object> root, String outFile, String ftlPath) throws Exception {
        File file = new File(outFile);
        File htmlFile = null;
        try {
            if (!file.getParentFile().exists()) {            //判断有没有父路径，就是判断文件整个路径是否存在
                file.getParentFile().mkdirs();            //不存在就全部创建
            }
            Template template = Freemarker.getTemplate(ftlName, ftlPath);
            String htmlId = UUID.randomUUID().toString().replace("-", "");
            htmlFile = new File("pdf/" + htmlId + ".html");
            if (!htmlFile.getParentFile().exists()) {
                htmlFile.getParentFile().mkdirs();
            }
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "utf-8"));
            template.process(root, out);        //模版输出
            out.flush();

            String url = htmlFile.toURI().toURL().toString();
            OutputStream os = new FileOutputStream(outFile);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(url);

            // 解决中文问题
            ITextFontResolver fontResolver = renderer.getFontResolver();
            URL resource = Thread.currentThread().getContextClassLoader().getResource("simsun.ttc");
            assert resource != null;
            fontResolver.addFont(resource.toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.layout();
            renderer.createPDF(os);
            os.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (htmlFile != null) {
                htmlFile.delete();
            }
        }
        return file;
    }





    public static void main(String args[]) {

        List<String> related = new ArrayList<String>();
        related.add("cover global tech companies, post 30+ articles on influential media.");

        /*resume简历对象*/
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("name", "MARC (ZHENZHONG) YAN");
        dataMap.put("phone", "+86 186 8877-7925");
        dataMap.put("address", "2408 Zhaofeng Plaza, 1027 Changning Rd, Shanghai 200050");
        dataMap.put("mail", "marc.yan@duke.edu");
        dataMap.put("language", "Cantonese and Mandarin (native), English (fluent).");
        dataMap.put("skill", "Google Analytics, Google AdWords, and Udemy SEO Certificate.");
        dataMap.put("related_info", related);
        dataMap.put("hobby", "bungee jumping, soccer and marathon.");

        /*教育背景列表对象*/
        List<Map<String, Object>> eduExp = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 2; i++) {
            Map<String,Object> map = new HashMap();
            map.put("school", "Duke University");
            map.put("education", "The Fuqua School of Business");
            map.put("subject", "Master of Business");
            List<String> edu_experience = new ArrayList<String>();
            for (int j = 0; j < 2; j ++) {
                edu_experience.add("Cabinet Member of VC Club. Active member of Consulting Club. INNOVATEChina 2016 Business Plan Competition global finalist (6/30+). Adobe Analytics Competition national semi-finalist (#1 at Duke).");
            }
            map.put("edu_experience",edu_experience);
            map.put("city", "Durham, NC");
            map.put("start_time", "2000");
            map.put("end_time", "2017");
            eduExp.add(map);
        }
        dataMap.put("eduExp", eduExp);

        /*公司描述*/
        List<String> companyDes = new ArrayList<String>();
        companyDes.add("China-based global technology company, focusing on consumer electronics including smartphones");

        /*工作经历课外实践*/
        List<Map<String, Object>> experience = new ArrayList<Map<String, Object>>();
        for (int k = 0; k < 2; k ++) {
            Map<String, Object> exMap = new HashMap<String, Object>();
            exMap.put("title", "WORK EXPERIENCE");
            if (k == 1) {
                exMap.put("title", "EXTRACURRICULAR PRACTICE");
            }
			/*工作经历列表对象*/
            List<Map<String, Object>> workExp = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 2; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("company", "OPPO TECHNOLOGY");
                map.put("company_des", companyDes);
                map.put("department", "Senior Associate");
                map.put("position", "Investment Planning (pre-MBA summer intern)");
                map.put("city", "Shenzhen, China");
                map.put("start_time", "2000");
                map.put("end_time", "2015");
                //工作经历描述列表对象
                List<String> workDes = new ArrayList<String>();
                for (int j = 0; j < 2; j ++) {
                    workDes.add("Conducted benchmark study for the development plan of new-founded corporate venture capital.ssokcke lkdskaife");
                }
                map.put("workDes", workDes);
                workExp.add(map);
            }
            exMap.put("workExp", workExp);
            experience.add(exMap);
        }
        dataMap.put("experience", experience);
        try {
            File file = ExportPDF.exportPdf("en_resume.html", dataMap, "pdf/resume.pdf", "");
            pdfAddImg(file.getPath(), "pdf/avatar.jpeg", "pdf/avatar_resume.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pdfAddImg(String fm, String avatarPath, String outFile) throws Exception {
        PdfReader reader = new PdfReader(fm);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outFile));
        Image img = Image.getInstance(avatarPath);
        img.setAlignment(Image.LEFT | Image.TEXTWRAP);
        img.setBorderWidth(10);
        img.setAbsolutePosition(509, 700);
        img.scaleToFit(80, 80);
        PdfContentByte over = stamper.getUnderContent(1);
        over.addImage(img);
        stamper.close();
        reader.close();
    }


}
