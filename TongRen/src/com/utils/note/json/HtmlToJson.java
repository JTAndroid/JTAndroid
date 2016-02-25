package com.utils.note.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

/**
 * Created by zhangchangwei on 2015/8/18.
 */
public class HtmlToJson {
	private String _bodyText;
	private int _attachmentCount;

	public HtmlToJson(String text) {
		_bodyText = text;
	}

	public JsonText convertHtmlToJson(String html) {
		if (html == null) {
			return null;
		} else {
			System.out.println("html:" + html);
			JsonText jsonText = new JsonText();
			Document doc = Jsoup.parse(html);
			Element body = doc.body();
			System.out.println("bodytext:" + _bodyText.replaceAll("\n", "H"));
			jsonText.setString(_bodyText);
			List<Runs> runss = new ArrayList<Runs>();
			int textLength = _bodyText.length();
			if (textLength > 0) {
				Map<Integer, TextStyle> textStyleMap = new HashMap<Integer, TextStyle>();
				List<Integer> indexs = new ArrayList<Integer>();
				List<TextStyle> textStyleList = new ArrayList<TextStyle>();
				Elements alignElements = body.getElementsByTag("div");
				for (Element e : alignElements) {
					org.jsoup.nodes.Attributes attributes = e.attributes();
					int alignment = ParagraphStyle.ALIGNMENT_NORMAL;
					String alignmentStr = attributes.get("align");
					if("left".equals(alignmentStr)){
						alignment = ParagraphStyle.ALIGNMENT_LEFT;
					}else if("right".equals(alignmentStr)){
						alignment = ParagraphStyle.ALIGNMENT_RIGHT;
					}else if("center".equals(alignmentStr)){
						alignment = ParagraphStyle.ALIGNMENT_CENTER;
					}
					int start = Integer.parseInt(attributes.get("start"));
					int length = Integer.parseInt(attributes.get("length"));
					for (int j = start; j < start + length; j++) {
						TextStyle ts;
						if (textStyleMap.containsKey(j)) {
							ts = textStyleMap.get(j);
						} else {
							ts = new TextStyle();
							ts.index = j;
						}
						ts.index = j;
						ts.alignment = alignment;
						textStyleMap.put(j, ts);
						indexs.add(j);
					}
				}
				Elements bElements = body.getElementsByTag("b");
				for (Element e : bElements) {
					org.jsoup.nodes.Attributes attributes = e.attributes();
					int start = Integer.parseInt(attributes.get("start"));
					int length = Integer.parseInt(attributes.get("length"));
					for (int j = start; j < start + length; j++) {
						TextStyle ts;
						if (textStyleMap.containsKey(j)) {
							ts = textStyleMap.get(j);
						} else {
							ts = new TextStyle();
							ts.index = j;
						}
						ts.isBold = 1;
						textStyleMap.put(j, ts);
						indexs.add(j);
					}
				}
				Elements uElements = body.getElementsByTag("u");
				for (Element e : uElements) {
					org.jsoup.nodes.Attributes attributes = e.attributes();
					int start = Integer.parseInt(attributes.get("start"));
					int length = Integer.parseInt(attributes.get("length"));
					for (int j = start; j < start + length; j++) {
						TextStyle ts;
						if (textStyleMap.containsKey(j)) {
							ts = textStyleMap.get(j);
						} else {
							ts = new TextStyle();
							ts.index = j;
						}
						ts.isUnderline = 1;
						textStyleMap.put(j, ts);
						indexs.add(j);
					}
				}
				Elements strikeElements = body.getElementsByTag("strike");
				for (Element e : strikeElements) {
					org.jsoup.nodes.Attributes attributes = e.attributes();
					int start = Integer.parseInt(attributes.get("start"));
					int length = Integer.parseInt(attributes.get("length"));
					for (int j = start; j < start + length; j++) {
						TextStyle ts;
						if (textStyleMap.containsKey(j)) {
							ts = textStyleMap.get(j);
						} else {
							ts = new TextStyle();
							ts.index = j;
						}
						ts.isStrikeThrough = 1;
						textStyleMap.put(j, ts);
						indexs.add(j);
					}
				}
				Elements imgElements = body.getElementsByTag("img");
				for (Element e : imgElements) {
					org.jsoup.nodes.Attributes attributes = e.attributes();
					int start = Integer.parseInt(attributes.get("start"));
					String src = attributes.get("src");
					Attachment attachment = new Attachment();
					attachment.setSuffix(src.substring(src.lastIndexOf(".")));
					attachment.setType(Attachment.TYPE_IMAGE);
					attachment.setUrl(src.substring(0, src.lastIndexOf(".")));
					TextStyle ts;
					if (textStyleMap.containsKey(start)) {
						ts = textStyleMap.get(start);
					} else {
						ts = new TextStyle();
						ts.index = start;
					}
					ts.isImage = 1;
					ts.attachment = attachment;
					textStyleMap.put(start, ts);
					indexs.add(start);
				}
				Elements embedElements = body.getElementsByTag("embed");
				for (Element e : embedElements) {
					org.jsoup.nodes.Attributes attributes = e.attributes();
					int start = Integer.parseInt(attributes.get("start"));
					String src = attributes.get("src");
					Attachment attachment = new Attachment();
					attachment.setSuffix(src.substring(src.lastIndexOf(".")));
					attachment.setType(Attachment.TYPE_AUDIO);
					attachment.setUrl(src.substring(0, src.lastIndexOf(".")));
					TextStyle ts;
					if (textStyleMap.containsKey(start)) {
						ts = textStyleMap.get(start);
					} else {
						ts = new TextStyle();
						ts.index = start;
					}
					ts.isAudio = 1;
					ts.attachment = attachment;
					textStyleMap.put(start, ts);
					indexs.add(start);
				}
				for (int i = 0; i < textLength; i++) {
					if (!indexs.contains(i)) {
						TextStyle ts = new TextStyle();
						ts.index = i;
						textStyleList.add(ts);
					} else {
						TextStyle ts = textStyleMap.get(i);
						textStyleList.add(ts);
					}
				}
				if (textStyleList.size() == 1) {
					Runs runs = getRuns(textStyleList.get(0), 0);
					runss.add(runs);
				} else {
					int rangeIndex = 0;
					for (int i = 1; i < textStyleList.size(); i++) {
						TextStyle preTs = textStyleList.get(i - 1);
						TextStyle ts = textStyleList.get(i);
						if (ts.isDiffrent(preTs)) {
							if (i == textStyleList.size() - 1) {
								Runs runs = getRuns(preTs, rangeIndex);
								Runs runs2 = getRuns(ts, i);
								runss.add(runs);
								runss.add(runs2);
							} else {
								Runs runs = getRuns(preTs, rangeIndex);
								runss.add(runs);
								rangeIndex = i;
							}
						} else {
							if (i == textStyleList.size() - 1) {
								Runs runs = getRuns(ts, rangeIndex);
								runss.add(runs);
							}
						}
					}
				}
			} else {
				TextStyle ts = new TextStyle();
				ts.index = _bodyText.length() - 1;
				Runs runs = getRuns(ts, 0);
				runss.add(runs);
			}
			jsonText.setRuns(runss);
			return jsonText;
		}
	}

	public String getText() {
		return _bodyText;
	}

	public int getAttachmentCount() {
		return _attachmentCount;
	}

	public Runs getRuns(TextStyle ts, int rangeIndex) {
		Runs runs = new Runs();
		runs.setRange(new int[] { rangeIndex, ts.index - rangeIndex + 1 });
		Attributes attr = new Attributes();
		if(ts.alignment != ParagraphStyle.ALIGNMENT_NORMAL && ts.alignment != ParagraphStyle.ALIGNMENT_LEFT){
			ParagraphStyle paragraphStyle = new ParagraphStyle();
			paragraphStyle.setAlignment(ts.alignment);
			attr.setParagraphStyle(paragraphStyle);
		}
		if (ts.isBold == 1) {
			Font font = new Font();
			font.setFontWeight("bold");
			attr.setFont(font);
		}
		if (ts.isUnderline == 1) {
			attr.setUnderline(1);
		}
		if (ts.isStrikeThrough == 1) {
			attr.setStrikethrough(1);
		}
		if (ts.isImage == 1 || ts.isAudio == 1) {
			Attachment attachment = ts.attachment;
			attr.setAttachment(attachment);
			_attachmentCount++;
		}
		runs.setAttributes(attr);
		return runs;
	}

	class TextStyle {
		public int index;
		public int isBold;
		public int isUnderline;
		public int isStrikeThrough;
		public int isImage;
		public int isAudio;
		public int alignment;
		public Attachment attachment;

		public boolean isDiffrent(TextStyle ts) {
			return isAudio != ts.isAudio
					|| isImage != ts.isImage
					|| isStrikeThrough != ts.isStrikeThrough
					|| isUnderline != ts.isUnderline
					|| isBold != ts.isBold
					|| alignment != ts.alignment
					|| (attachment != null && ts.attachment != null && attachment != ts.attachment);
		}
	}
}
