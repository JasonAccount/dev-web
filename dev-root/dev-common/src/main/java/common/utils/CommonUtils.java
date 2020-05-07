package common.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共的工具类
 */
public class CommonUtils {

    /**
     * 输入的字符串是否正则匹配
     * @param pattern 正则
     * @param input 输入字符
     * @return
     */
    public static boolean isMatch(String pattern, String input){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        return m.matches();
    }

    /**
     * 生成随机图片
     * @param words
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage generateRandowImg(String words, int width, int height){
        Random random = new Random();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(new Color(200+random.nextInt(50),
                200+random.nextInt(50),
                200+random.nextInt(50)));
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.WHITE);
        graphics.drawRect(0, 0, width - 1, height - 1);

        Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setFont(new Font("宋体", Font.BOLD, 18));

        // 定义x坐标
        int x = 10;
        for (int i = 0; i < words.length(); i++) {
            // 随机颜色
            graphics2d.setColor(new Color(20 + random.nextInt(110),
                    20 + random.nextInt(110),
                    20 + random.nextInt(110)));
            // 旋转 -30 --- 30度
            int jiaodu = random.nextInt(60) - 30;
            // 换算弧度
            double theta = jiaodu * Math.PI / 180;
            // 获得字母数字
            char c = words.charAt(i);
            // 将c 输出到图片
            graphics2d.rotate(theta, x, 20);
            graphics2d.drawString(String.valueOf(c), x, 20);
            graphics2d.rotate(-theta, x, 20);
            x += 30;
        }

        // 绘制干扰线
        graphics.setColor(new Color(160+random.nextInt(40),
                160+random.nextInt(40),
                160+random.nextInt(40)));
        int x1;
        int x2;
        int y1;
        int y2;
        for (int i = 0; i < 30; i++) {
            x1 = random.nextInt(width);
            x2 = random.nextInt(12);
            y1 = random.nextInt(height);
            y2 = random.nextInt(12);
            graphics.drawLine(x1, y1, x1 + x2, x2 + y2);
        }

        graphics.dispose();// 释放资源

        return bufferedImage;
    }
}
