package com.sunxn.news.webcrawler.utils;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * @description: 从HTML中提取纯文本（去掉标签）
 * @data: 2020/4/24 12:48
 * @author: xiaoNan
 */
public class HtmlUtil {

    public static String getText(String html, String id) {
        try {
            Parser parser = new Parser(html);
            CssSelectorNodeFilter filter = new CssSelectorNodeFilter("#" + id);
            NodeList nodeList = parser.extractAllNodesThatMatch(filter);
            return nodeList == null ||
                    nodeList.size() == 0 ? null : nodeList.elementAt(0).toPlainTextString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTextByClass(String html, String css_class) {
        try {
            Parser parser = new Parser(html);
            CssSelectorNodeFilter filter = new CssSelectorNodeFilter("." + css_class);
            NodeList nodeList = parser.extractAllNodesThatMatch(filter);
            return nodeList == null ||
                    nodeList.size() == 0 ? null : nodeList.elementAt(0).toPlainTextString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取网页中纯文本信息
     * @param html
     * @return
     */
    public static String getText(String html) throws ParserException {
        if (StringUtils.isBlank(html)) {
            return "";
        }

        StringBean bean = new StringBean();
        bean.setLinks(false);
        bean.setReplaceNonBreakingSpaces(true);
        bean.setCollapse(true);
        // 返回解析后的网页纯文本信息
        Parser parser = Parser.createParser(html, "utf-8");
        parser.visitAllNodesWith(bean);
        parser.reset();
        String text = bean.getStrings();
        String reg = "[^\u4e00-\u9fa5]";
        text = text.replaceAll(reg, " ");
        return text;
    }
}
