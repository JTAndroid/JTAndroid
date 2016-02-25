package com.utils.common;

import java.text.DecimalFormat;
import java.util.Random;

import com.utils.string.Base64;

public class TaskIDMaker {

	private static final int MAX_GENERATE_COUNT = 99999;
    private static int generateCount = 0;
    private static DecimalFormat df = new DecimalFormat("00000");

    /**创建唯一字符串
     * @return 18位唯一字符串  13位时间+5位流水号
     */
    public static synchronized String getPrimaryKey() {
        generateCount++;
        if (generateCount > MAX_GENERATE_COUNT)
            generateCount = 1;
        String uniqueNumber = Long.toString(System.currentTimeMillis()) + df.format(generateCount);
        return uniqueNumber;
    }
	public static String getTaskId(String userName) {
		String fileId = getPrimaryKey();
		fileId += userName;
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			String rand = String.valueOf(random.nextInt(10));
			fileId += rand;
		}
		byte[] bt = fileId.getBytes();
		return new String(Base64.encode(bt));
	}

	public static String getTaskId(long incId) {
		String fileId = getPrimaryKey();
		fileId += incId;
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			String rand = String.valueOf(random.nextInt(10));
			fileId += rand;
		}
		byte[] bt = fileId.getBytes();
		return new String(Base64.encode(bt));
	}
	
	public static String getTaskId() {
        String fileId = getPrimaryKey();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            String rand = String.valueOf(random.nextInt(10));
            fileId += rand;
        }
        byte[] bt = fileId.getBytes();
        return new String(Base64.encode(bt));
    }
}
