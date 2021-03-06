package indi.cyken.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.commons.io.IOUtils;

/**
 * 音频格式转换工具类
 * @author Yong
 *
 */
public class AudioConverterUtil {

	/**
	 * MP3转换PCM文件方法
	 * @param mp3filepath 原始文件路径
	 * @param pcmfilepath 转换文件的保存路径
	 * @throws Exception 
	 */
	public static void mp3Convertpcm(String mp3filepath,String pcmfilepath) throws Exception{
		File mp3 = new File(mp3filepath);
		File pcm = new File(pcmfilepath);
		//原MP3文件转AudioInputStream
		AudioInputStream mp3audioStream = AudioSystem.getAudioInputStream(mp3);
		//将AudioInputStream MP3文件 转换为PCM AudioInputStream
		AudioInputStream pcmaudioStream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, mp3audioStream);
		//准备转换的流输出到OutputStream
		OutputStream os = new FileOutputStream(pcm);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead=pcmaudioStream.read(buffer, 0, 8192))!=-1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		pcmaudioStream.close();
	}
	
	
	/**
	 * 输入流转byte二进制数据
	 * @param fis
	 * @return
	 * @throws IOException
	 */
	private static byte[] InputStreamToByte(FileInputStream fis) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		long size = fis.getChannel().size();
		byte[] buffer = null;
		if (size <= Integer.MAX_VALUE) {
			buffer = new byte[(int) size];
		} else {
			buffer = new byte[8];
			for (int ix = 0; ix < 8; ++ix) {
				int offset = 64 - (ix + 1) * 8;
				buffer[ix] = (byte) ((size >> offset) & 0xff);
			}
		}
		int len;
		while ((len = fis.read(buffer)) != -1) {
			byteStream.write(buffer, 0, len);
		}
		byte[] data = byteStream.toByteArray();
		IOUtils.closeQuietly(byteStream);
		return data;

	}
	
	   /**
     * @Description MP3转换pcm
     * @param mp3Stream
     *            原始文件流
     * @return 转换后的二进制
     * @throws Exception
     * @author liuyang
     * @blog http://www.pqsky.me
     * @date 2018年1月30日
     */
    public byte[] mp3Convertpcm(InputStream mp3Stream) throws Exception {
        // 原MP3文件转AudioInputStream
        AudioInputStream mp3audioStream = AudioSystem.getAudioInputStream(mp3Stream);
        // 将AudioInputStream MP3文件 转换为PCM AudioInputStream
        AudioInputStream pcmaudioStream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED,
                mp3audioStream);
        byte[] pcmBytes = IOUtils.toByteArray(pcmaudioStream);
        pcmaudioStream.close();
        mp3audioStream.close();
        return pcmBytes;
    }
}
