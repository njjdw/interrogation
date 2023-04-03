package com.interrogation.utils;

import com.interrogation.exception.InterrogationException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

@Component
public class RandomImgCodeUtils {

    private static int IMG_WIDTH = 90;
    private static int IMG_HEIGHT = 20;
    private static int LINE_SIZE = 12;
    private static int CODE_NUMBER = 4;

    private static Random random = new Random();

    /**
     *
     */
    public static String getRandomImgCode(HttpServletResponse response,HttpServletRequest request) {

        //图片存放的文件
        String codeImg = request.getServletContext().getRealPath("/codeImg.jpg");
        System.out.println("realpath  "+codeImg);
//        String codeImg = "D:\\InstallDir\\interrogation\\serverSite\\src\\main\\resources\\static\\codeImg.jpg";
        File file = new File(codeImg);
        //生成图片缓冲区
        BufferedImage bufferedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        //创建画笔
        Graphics2D imageGraphics = bufferedImage.createGraphics();
        //图片前景色,设为随机生成
        imageGraphics.setBackground(Color.WHITE);
        //随机生成干扰线
        drawRandomLine(bufferedImage);
        //随机生成验证码字符串以及字符串颜色
        String codes = drawRandomString(bufferedImage);
        //将画成的图片存入文件
        ServletOutputStream outputStream = null;
        FileInputStream inputStream = null;
        try {
            ImageIO.write(bufferedImage,"jpg",file);
        //通过输出流将图片输出到页面
        outputStream = response.getOutputStream();
        inputStream = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int read = 0;
        do{
            read = inputStream.read(bytes, 0, bytes.length);
            //bytes缓存数组需要通过编码后通过输出流传回页面，否则会出现乱码
            Base64.encodeBase64(bytes);
            outputStream.write(bytes,0,bytes.length);
        }while (read >= 0);
        }catch (IOException e){
            throw new InterrogationException("图片验证码传输异常");
        }finally {
            try {
                if (inputStream != null){
                    inputStream.close();
                }
            if (outputStream != null){
                outputStream.close();
            }
            }catch (Exception e){
                throw new InterrogationException("数据流关闭异常");

            }
        }
        //以下方法传回图片验证码会出现因缓存不及时更新图片的问题
//        HashMap<String, Object> resultMap = new HashMap<>();
//        String codeImgSrc = request.getRequestURL()+"/codeImg.jpg";
//        resultMap.put("imgCodeSrc",codeImgSrc);
//        resultMap.put("codes",codes);
        return codes;
    }

    private static String  drawRandomString(BufferedImage bufferedImage){
        Graphics2D imageGraphics = bufferedImage.createGraphics();
        String codes = RandomUtils.getMixBits(4);
        imageGraphics.setBackground(getRandomColor());
        imageGraphics.setColor(getRandomColor());
        Font font = new Font("Arial", Font.CENTER_BASELINE, 15);
        imageGraphics.setFont(font);
        imageGraphics.drawString(codes,10+random.nextInt(50), 10+random.nextInt(10));
        return codes;
    }
    private static void drawRandomLine(BufferedImage bufferedImage){
        Graphics2D imageGraphics = bufferedImage.createGraphics();
        imageGraphics.setBackground(getRandomColor());
        imageGraphics.setColor(getRandomColor());
        for (int i = 0; i < LINE_SIZE; i++) {
            int x = random.nextInt(80);
            int y = random.nextInt(10);
            int x1 = x+random.nextInt(20);
            int y1 = y+random.nextInt(20);
            imageGraphics.drawLine(x,y,x1,y1);
        }
    }
    private static Color getRandomColor(){
        int R = random.nextInt(255);
        int G = random.nextInt(255);
        int B = random.nextInt(255);
        Color color = new Color(R, G, B);
        return color;
    }
}
