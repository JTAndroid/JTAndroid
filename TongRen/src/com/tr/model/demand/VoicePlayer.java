package com.tr.model.demand; 

import java.io.File;
import java.io.Serializable;


/**
 * @ClassName:     VoicePlayer.java
 * @author         fxtx
 * @Date           2015年3月19日 下午4:16:21 
 * @Description:   音频播放对象
 */
public class VoicePlayer implements Serializable{

	/**
	 * 
	 */
	public static final long serialVersionUID = 8449802624313863357L;
	public long id;
	public String name;
	public File file;//录音文件
	public long time;

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoicePlayer other = (VoicePlayer) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (time != other.time)
			return false;
		return true;
	}
	
	
}
 
