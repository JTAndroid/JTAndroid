package com.tr.model.demand;

import java.io.Serializable;

import com.lidroid.xutils.util.PreferencesCookieStore.SerializableCookie;

public class ImageItem implements Serializable{
	
		//资源的路径
		public String path;
		//改资源是否是视频资源
		public boolean isVideo;
		public String filename;
		public String fileid ;
		public boolean isVisable ;
		//这个图片是否需要加阴影
		public boolean isShadow = false ;
		public String thumbnailspath;//缩略图地址
		public int type ;

		/**
		 * 
		 * @param path 文件路径
		 * @param isVideo 是否是视频
		 * @param fileName 文件名字
		 * @param fileId 文件id
		 */
		public ImageItem(String path,boolean isVideo,String fileName,String fileId) {
			this.path = path;
			this.isVideo = isVideo;
			this.filename = fileName;
			this.fileid = fileId;
		}
		public ImageItem(String path,boolean isVideo){
			this.path = path;
			this.isVideo = isVideo;
		}
		
	}