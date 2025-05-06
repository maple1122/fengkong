package apiDependent;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wangx
 * @Title: FileUtil
 * @Description: 文件工具类
 * @date 2021/3/15
 */
public class FileUtil {

    private static Logger logger= LoggerFactory.getLogger(FileUtil.class);

    /**
     * @author wx
     * @description 读取文件内容
     * @date 2025/1/7 11:37
     * @param path 文件路径
     * @return java.lang.String
     **/
    public static String readFile(String path) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            return StringUtils.join(lines, System.lineSeparator());
        } catch (IOException e) {
            logger.error("读取文件[{}]异常", path, e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * @author wx
     * @description 下载附件-nio
     * @date 2025/1/7 15:34
     * @param httpUrl     链接地址
     * @param targetPath  目标路径
     * @return boolean
     **/
    public static boolean downloadFile(String httpUrl, String targetPath) {
        logger.info("开始下载附件 httpUrl[{}] 目标路径[{}]", httpUrl, targetPath);
        StopWatch sw = new StopWatch();
        sw.start();

        boolean result = true;
        FileChannel outChannel = null;
        ReadableByteChannel inChannel = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            inChannel = Channels.newChannel(conn.getInputStream());
            outChannel = FileChannel.open(Paths.get(targetPath), StandardOpenOption.WRITE, StandardOpenOption.READ,
                    StandardOpenOption.CREATE);
            //分配缓冲区 可以根据需要调整缓冲区大小
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024*10);
            while ((inChannel.read(buffer)) != -1) {
                //将缓冲区由写模式切换到读模式
                buffer.flip();
                //将缓冲区中的数据通过输出通道写出来
                while (buffer.hasRemaining()) {
                    outChannel.write(buffer);
                }
                //清空缓冲区，准备下一次读取
                buffer.clear();
            }
        } catch (Exception e) {
            result = false;
            logger.error("文件下载失败", e);
        } finally {
            try {
                if (Objects.nonNull(inChannel)) {
                    inChannel.close();
                }
                if (Objects.nonNull(outChannel)) {
                    outChannel.close();
                }
            } catch (IOException e) {
                logger.error("通道关闭异常", e);
            }
        }
        sw.stop();
        logger.info("下载附件 httpUrl[{}] 目标路径[{}] result[{}] 总耗时(秒)[{}] 耗时统计 {}", httpUrl, targetPath, result,
                sw.getTotalTimeSeconds(),sw.prettyPrint());
        return result;
    }

    /**
     * @author wx
     * @description 下载附件
     * @date 2025/1/7 15:34
     * @param httpUrl     链接地址
     * @param targetPath  目标路径
     * @return boolean
     **/
    public static boolean downloadFile2(String httpUrl, String targetPath) {

        logger.info("====downLoadFile:  " + httpUrl + " --> " + targetPath);
        logger.info("开始下载附件 httpUrl[{}] 目标路径[{}]", httpUrl, targetPath);
        StopWatch sw = new StopWatch();
        sw.start();

        boolean result = true;
        OutputStream fos = null;
        InputStream is = null;
        try {
            File file = new File(targetPath);
            if (!file.exists()) {
                File targetDir = new File(file.getParent());
                targetDir.mkdirs();
            }
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            is = conn.getInputStream();// 通过输入流获取图片数据
            fos = new FileOutputStream(file);
            byte[] bys = new byte[1024 * 10];
            int len = 0;
            while ((len = is.read(bys)) != -1) {
                fos.write(bys, 0, len);
            }
        } catch (Exception e) {
            result = false;
            logger.error("下载文件失败,网络路径[{}] 下载目录[{}]", httpUrl, targetPath, e);
        } finally {
            try {
                if (Objects.nonNull(is)) {
                    is.close();
                }
                if (Objects.nonNull(fos)) {
                    fos.close();
                }
            } catch (IOException e) {
                logger.error("通道关闭异常", e);
            }
        }
        sw.stop();
        logger.info("下载附件 httpUrl[{}] 目标路径[{}] result[{}] 总耗时(秒)[{}] 耗时统计 {}", httpUrl, targetPath, result,
                sw.getTotalTimeSeconds(),sw.prettyPrint());
        return result;
    }

    /**
     * 上传文件
     * @param file        文件
     * @param targetPath 保存路径
     * @return boolean
     */
    public static boolean uploadFile(MultipartFile file, String targetPath) {
        boolean result = true;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            File f = new File(targetPath);
            if (!f.exists() && !f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            inChannel = ((FileInputStream) file.getInputStream()).getChannel();
            outChannel = FileChannel.open(Paths.get(targetPath), StandardOpenOption.WRITE, StandardOpenOption.READ,
                    StandardOpenOption.CREATE);
            outChannel.transferFrom(inChannel, 0, inChannel.size());
        } catch (IOException e) {
            result = false;
            logger.error("文件上传失败", e);
        } finally {
            try {
                if (Objects.nonNull(inChannel)) {
                    inChannel.close();
                }
                if (Objects.nonNull(outChannel)) {
                    outChannel.close();
                }
            } catch (IOException e) {
                logger.error("通道关闭异常", e);
            }
        }
        return result;
    }

    /**
     * copy目录
     * @param sourceDir 原目录
     * @param targetDir 目标目录
     * @return boolean
     */
    public static boolean copyDir(String sourceDir, String targetDir) {
        boolean result = true;
        if (StringUtils.isAnyBlank(sourceDir, targetDir)) {
            logger.error("原目录 [{}]或目标目录 [{}]不能为空", sourceDir, targetDir);
            return false;
        }
        long start = System.currentTimeMillis();
        logger.info("copy目录 原目录 [{}] 目标目录 [{}] 开始时间 [{}]", sourceDir, targetDir, start);

        List<File> files = new ArrayList<>();
        //获取所有文件
        getDirAllFiles(sourceDir, files);
        for (File file : files) {
            String filePath = file.getAbsolutePath();
            copyFileByCache(filePath, filePath.replace(sourceDir, targetDir));
        }
        long end = System.currentTimeMillis();
        logger.info("copy目录 原目录 [{}] 目标目录 [{}] 结束时间 [{}] 耗时 [{}]s", sourceDir, targetDir, end, (end - start) / 1000);
        return result;
    }

    /**
     * 获取目录下所有文件
     * @param sourceDir 原目录
     * @param fileList  文件
     */
    private static void getDirAllFiles(String sourceDir, List<File> fileList) {
        File dir = new File(sourceDir);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getDirAllFiles(file.getAbsolutePath(), fileList);
            } else {
                fileList.add(file);
            }
        }
    }

    /**
     * 通过本地IO获取通道  进行数据传输
     * @param sourcePath  原路径
     * @param targetPath  目标路径
     * @return boolean
     */
    public static boolean copyFile(String sourcePath, String targetPath) {
        boolean result = true;
        if (StringUtils.isAnyBlank(sourcePath, targetPath)) {
            logger.error("原路径 [{}]或目标路径 [{}]不能为空", sourcePath, targetPath);
            return false;
        }
        logger.info("copy 文件 原路径 [{}] 至目标路径 [{}] ", sourcePath, targetPath);
        File file = new File(targetPath);
        if (!file.exists() && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(sourcePath).getChannel();
            outChannel = new FileOutputStream(targetPath).getChannel();
            //分配缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while ((inChannel.read(buffer)) != -1) {
                //将缓冲区由写模式切换到读模式
                buffer.flip();
                //将缓冲区中的数据通过输出通道写出来
                outChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            result = false;
            logger.error("文件copy异常", e);
        } finally {
            try {
                if (Objects.nonNull(inChannel)) {
                    inChannel.close();
                }
                if (Objects.nonNull(outChannel)) {
                    outChannel.close();
                }
            } catch (IOException e) {
                result = false;
                logger.error("通道关闭异常", e);
            }
        }
        return result;
    }

    /**
     * 通道之间直接进行文件传输（直接缓冲区）
     * @param sourcePath  原路径
     * @param targetPath  目标路径
     * @return boolean
     */
    public static boolean copyFileByCache(String sourcePath, String targetPath) {
        boolean result = true;
        if (StringUtils.isAnyBlank(sourcePath, targetPath)) {
            logger.error("原路径 [{}]或目标路径 [{}]不能为空", sourcePath, targetPath);
            return false;
        }
        logger.info("copy 文件 原路径 [{}] 至目标路径 [{}] ", sourcePath, targetPath);
        File file = new File(targetPath);
        if (!file.exists() && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = FileChannel.open(Paths.get(sourcePath), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get(targetPath), StandardOpenOption.WRITE, StandardOpenOption.READ,
                    StandardOpenOption.CREATE);
            outChannel.transferFrom(inChannel, 0, inChannel.size());
        } catch (IOException e) {
            result = false;
            logger.error("文件copy异常", e);
        } finally {
            try {
                if (Objects.nonNull(inChannel)) {
                    inChannel.close();
                }
                if (Objects.nonNull(outChannel)) {
                    outChannel.close();
                }
            } catch (IOException e) {
                result = false;
                logger.error("通道关闭异常", e);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String source="D://xinhuating/member/headImage/2019/04/19/faee572158184fce859b7edbaef6c609.JPEG";
        String target="D://new/member/headImage/2019/04/19/faee572158184fce859b7edbaef6c609.JPEG";
        try {
//            System.out.println("result:"+copyFileByCache(source,target));
            System.out.println("result:"+copyDir("D:\\xinhuating","D:\\new"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
