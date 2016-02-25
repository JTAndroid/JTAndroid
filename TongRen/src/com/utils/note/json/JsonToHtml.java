package com.utils.note.json;

import java.util.List;

import com.google.gson.Gson;

/**
 * Created by zhangchangwei on 2015/8/19.
 */
public class JsonToHtml {

	public String convertJsonToHtml(String json){
		Gson gson = new Gson();
        JsonText jsonText = gson.fromJson(json, JsonText.class);
        return convertJsonTextToHtml(jsonText);
	}
	
    public String convertJsonTextToHtml(JsonText jsonText){
        StringBuilder html = new StringBuilder();
        String text = jsonText.getString();
        List<Runs> runs = jsonText.getRuns();
        int preAlignment = ParagraphStyle.ALIGNMENT_NORMAL;
        for (Runs r : runs){
            Attributes attributes = r.getAttributes();
            Attachment attachment = attributes.getAttachment();
            StringBuilder sb = null;
            if(attachment != null){
                String type = attachment.getType();
                if(Attachment.TYPE_IMAGE.equals(type)){
                    String suffix = attachment.getSuffix();
                    if (!suffix.startsWith(".")){
                        suffix = "." + suffix;
                    }
                    String src = attachment.getUrl() + suffix;
                    String tagString = "<img src = \"" + src + "\"></img>";
                    sb = new StringBuilder(tagString);
                }else if(Attachment.TYPE_AUDIO.equals(type)){
                    String suffix = attachment.getSuffix();
                    if (!suffix.startsWith(".")){
                        suffix = "." + suffix;
                    }
                    String src = attachment.getUrl() + suffix;
                    String tagString = "<embed src = \"" + src + "\"></embed>";
                    sb = new StringBuilder(tagString);
                }
            }else{
                int[] range = r.getRange();
                int start = range[0];
                int length = range[1];
                String rangText = text.substring(start, start + length);
                sb = new StringBuilder(rangText);
            }

            int underline = attributes.getUnderline();
            if(underline == 1){
                String startTag = "<u>";
                String endTag = "</u>";
                sb.insert(0,startTag).append(endTag);
            }
            int strikthrough = attributes.getStrikethrough();
            if(strikthrough == 1){
                String startTag = "<strike>";
                String endTag = "</strike>";
                sb.insert(0,startTag).append(endTag);
            }

            String bold = attributes.getFont().getFontWeight();
            if("bold".equals(bold)){
                String startTag = "<b>";
                String endTag = "</b>";
                sb.insert(0,startTag).append(endTag);
            }
            int alignment = attributes.getParagraphStyle().getAlignment();
            if(!isAlignmentSame(preAlignment, alignment)){
            	preAlignment = alignment;
            	int[] range = r.getRange();
                int start = range[0];
                int length = range[1];
            	if(alignment == ParagraphStyle.ALIGNMENT_RIGHT){
        			String tagStr = "<div align = right>";
            		sb.insert(0,tagStr);
            	}else if(alignment == ParagraphStyle.ALIGNMENT_CENTER || alignment == ParagraphStyle.ALIGNMENT_FULL){
            		String tagStr = "<div align = center>";
            		sb.insert(0,tagStr);
            	}
            	if(start != 0){
                	sb.insert(0,"</div>");
                }
                if(start + length == text.length()){
                	sb.append("</div>");
                }
            }
            html.append(sb);
        }
        System.out.println("load html:" + html);
//        html.append(alignment + "");
        return html.toString().replaceAll("\n","<br/>");
    }
    
    private boolean isAlignmentSame(int preAlignment,int alignment){
    	if(preAlignment == ParagraphStyle.ALIGNMENT_LEFT || preAlignment == ParagraphStyle.ALIGNMENT_NORMAL){
        	return alignment == ParagraphStyle.ALIGNMENT_LEFT || alignment == ParagraphStyle.ALIGNMENT_NORMAL;
        }else if(preAlignment == ParagraphStyle.ALIGNMENT_FULL || preAlignment == ParagraphStyle.ALIGNMENT_CENTER){
        	return alignment == ParagraphStyle.ALIGNMENT_CENTER || alignment == ParagraphStyle.ALIGNMENT_FULL;
        }else{
        	return alignment == preAlignment;
        }
    }
}
