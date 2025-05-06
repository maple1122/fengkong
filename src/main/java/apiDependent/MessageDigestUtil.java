package apiDependent;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * @author wangx
 * @Title: MessageDigestUtil
 * @Description: 摘要(加密)算法(即散列哈希算法，不可逆) MD5、sha
 * @date 2022/3/21
 */
public class MessageDigestUtil {

    private static Logger logger= LoggerFactory.getLogger(MessageDigestUtil.class);


    /**
     * Md5加密(摘要算法)
     * @param value  明文
     * @return 密文
     */
    public static String getMd5String(String value) {
        //方式一：
        MessageDigest md5Digest = DigestUtils.getMd5Digest();
        byte[] digest = md5Digest.digest(value.getBytes());
        return Hex.encodeHexString(digest);
        //方式二：
        //return DigestUtils.md5Hex(value);
    }

    /**
     * 文件Md5加密(摘要算法)
     * @param file  明文
     * @return 密文
     */
    public static String getFileMd5String(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            //方式1：
            MessageDigest md5Digest = DigestUtils.getMd5Digest();
            MappedByteBuffer byteBuffer = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            md5Digest.update(byteBuffer);
            return Hex.encodeHexString(md5Digest.digest());
            //方式2：
//            return DigestUtils.md5Hex(fis);
        } catch (IOException e) {
            logger.error("获取文件MD5 失败",e);
            return null;
        }
    }

    /**
     * sha1加密(摘要算法)
     * @param value  明文
     * @return 密文
     */
    public static String getSHA1String(String value) {
        //方式一：
        MessageDigest sha1Digest = DigestUtils.getSha1Digest();
        byte[] digest = sha1Digest.digest(value.getBytes());
        return Hex.encodeHexString(digest);
        //方式二：
//        return DigestUtils.sha1Hex(value);
    }

    /**
     * sha256加密(摘要算法)
     * @param value  明文
     * @return 密文
     */
    public static String getSHA256String(String value) {
        //方式一：
        MessageDigest sha256Digest = DigestUtils.getSha256Digest();
        byte[] digest = sha256Digest.digest(value.getBytes());
        return Hex.encodeHexString(digest);
        //方式二：
//        return DigestUtils.sha256Hex(value);
    }

    /**
     * sha384加密(摘要算法)
     * @param value  明文
     * @return 密文
     */
    public static String getSHA384String(String value) {
        //方式一：
        MessageDigest sha384Digest = DigestUtils.getSha384Digest();
        byte[] digest = sha384Digest.digest(value.getBytes());
        return Hex.encodeHexString(digest);
        //方式二：
//        return DigestUtils.sha384Hex(value);
    }

    /**
     * sha512加密(摘要算法)
     * @param value  明文
     * @return 密文
     */
    public static String getSHA512String(String value) {
        //方式一：
        MessageDigest sha512Digest = DigestUtils.getSha512Digest();
        byte[] digest = sha512Digest.digest(value.getBytes());
        return Hex.encodeHexString(digest);
        //方式二：
//        return DigestUtils.sha512Hex(value);
    }


    public static void main(String[] args) {
        String value="111";
        System.out.println("md5:"+getMd5String(value));

        File file=new File("C:/Users/wangx/Pictures/2m26.jpg");
        System.out.println("md5 file:"+getFileMd5String(file));

        System.out.println("SHA-1:"+getSHA1String(value));

        System.out.println("SHA-256:"+getSHA256String(value));

        System.out.println("SHA-384:"+getSHA384String(value));

        System.out.println("SHA-512:"+getSHA512String(value));
    }



}
