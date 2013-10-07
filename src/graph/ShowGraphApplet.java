package graph;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Vector;

/*
    傳入的參數有:
        textList     : 每筆資料之名稱 逗號分隔(包括結束時)
        widthList    : 每筆資料之數值 逗號分隔(包括結束時)
        orderStyle   : ASC or DESC  (Default: ASC)
        title        : 圖表說明
    例如：
        textList=松壽廣場 停車場,市民大道建復段 停車場,
        widthList=455,170,
        orderStyle=DESC
        title=停車位數量
        
    <applet code="ShowGraphApplet.class" width="400" height="500" codebase="./">
        <param name = "textList" value = "松壽廣場 停車場,市民大道建復段 停車場,">
        <param name = "widthList" value = "455,170,">
        <param name = "orderStyle" value = "DESC">
        <param name = "title" value = "空位數量">
    </applet>
*/
public class ShowGraphApplet extends Applet {
    Vector showTexts = new Vector();
    Vector showWidths = new Vector();
    Vector showColors = new Vector();
    String title = "";          //圖表說明
    Font font;
    FontMetrics fm;
    int total_width = 400;      //Applet 的總寬度
    int top_marge = 20;         //上方的空隙
    int top_over = 5;           //最上方開始畫圖的座標與 y軸最上端的距離
    int botton_marge = 50;      //下方的空隙
    int left_marge = 20;        //左邊的空隙
    int right_marge = 20;       //右邊的空隙
    int right_over = 5;         //最後一個刻度到 x軸末端的距離 (會佔用 left_marge 的空間)
    int body_height = 35;       //每一筆資料所佔的高度(長條圖及下方的文字說明)
    int body_info_height = 25;  //長條圖下方文字所佔的高度(會佔用 body_height 的空間)
    int scale_width = 10;       //刻度所佔的像素長度
    int scale_size = 10;        //刻度的數量
    int scale_unit = 10;        //每個刻度的單位
    int scale_start;            //第一個刻度顯示的值
    int scale_end;              //最後一個刻度顯示的值
    int graph_width;            //長條圖最大寬度

    public ShowGraphApplet() { }
    
    //排序資料
    private void sortData() {
        String textList = getParameter("textList");
        String widthList = getParameter("widthList");
        String orderStyle = getParameter("orderStyle");
        if(orderStyle == null)
            orderStyle = "ASC";
        while(textList.length() != 0) {
            int i = textList.indexOf(",");
            int j = widthList.indexOf(",");
            String text = textList.substring(0, i);
            String width = widthList.substring(0, j);
            boolean flag = false;
            boolean flag1 = false;
            int addPos = showWidths.size();
            for(int k = 0; k < showWidths.size(); k++) {
                int w = Integer.parseInt((String)showWidths.elementAt(k));
                if(orderStyle.equals("ASC")) {
                    addPos = Integer.parseInt(width) <= w ? k : k + 1;
                } else if(orderStyle.equals("DESC")) {
                    addPos = Integer.parseInt(width) >= w ? k : k + 1;
                }
                if(addPos == k) {
                    break;
                }
            }

            showTexts.insertElementAt(text, addPos);
            showWidths.insertElementAt(width, addPos);
            textList = textList.substring(i + 1);
            widthList = widthList.substring(j + 1);
        }
    }

    public void init() {
        sortData();
        title = getParameter("unit");
        if (title == null) {
            title = "統計圖";
        }
        font = new Font("新細明體", 0, 12);
        fm = getFontMetrics(font);

        //找出最大的值
        int maxValue = 0;
        for(int i = 0; i < showWidths.size(); i++) {
            int value = Integer.parseInt((String)showWidths.elementAt(i));
            maxValue = (maxValue >= value) ? maxValue : value;
        }

        //如果最大值為 88  ->  求出 90
        //如果最大值為 455  ->  求出 460
        //如果最大值為 3955 ->  求出 3960
        int len = String.valueOf(maxValue).length();
        int maxValue2 = 10;
        if (len > 1) {
            maxValue2 = Integer.parseInt(String.valueOf(maxValue).substring(0, (len-1))) + 1;
            for(int i = 0; i < len - 2; i++)
                maxValue2 *= 10;
        }
        scale_end = maxValue2;
        scale_unit = maxValue2 / scale_size;
        scale_start = maxValue2 - scale_unit * (scale_size - 1);

        //設定顏色
        showColors.addElement(new Color(0, 51, 153));
        showColors.addElement(new Color(128, 0, 0));
        showColors.addElement(new Color(204, 204, 0));
        showColors.addElement(new Color(0, 0, 128));
        showColors.addElement(new Color(51, 102, 0));
        showColors.addElement(new Color(204, 102, 0));
        showColors.addElement(new Color(51, 102, 153));
        showColors.addElement(new Color(0, 153, 51));
        setBackground(Color.white);
    }

    public void paint(Graphics g) {
        int str_width = 0;
        int str_height = 0;
        g.setFont(font);
        int scaleX = left_marge;    //刻度之 x 座標
        int xY = top_marge + body_height * showWidths.size();   //x軸之y座標
        int lastScaleX = total_width - right_marge;             //最後一個刻度的 X座標

        //畫出橫軸
        g.drawLine(left_marge, xY, lastScaleX + right_over, xY);
        graph_width = lastScaleX - left_marge;

        //算出刻度所佔的 y軸 範圍
        int scaleY1 = xY - scale_width / 2;
        int scaleY2 = xY + scale_width / 2;

        //畫出刻度 及 顯示之值
        for(int i = 0; i < scale_size; i++) {
            scaleX += graph_width / scale_size;
            //畫出刻度
            g.drawLine(scaleX, scaleY1, scaleX, scaleY2);
            //算出要顯示的數值，及其字串高度和長度
            String numStr = String.valueOf(scale_start + i * scale_unit);
            str_width = fm.stringWidth(numStr);
            str_height = fm.getHeight();
            int strX = scaleX - str_width / 2;
            //顯示數值
            g.drawString(numStr, strX, scaleY2 + str_height);
        }

        //圖表的說明 (刻度下方)
        int titleY = scaleY2 + str_height;  //title 的 y 座標
        str_width = fm.stringWidth(title);
        str_height = fm.getHeight();
        g.drawString(title, ((left_marge + graph_width + right_marge) - str_width) / 2, titleY + str_height);

        //畫出每一個長條圖
        for(int i = 0; i < showWidths.size(); i++) {
            //長條圖寬度
            int boxWidth = (int)(((float)Integer.parseInt((String)showWidths.elementAt(i)) / (float)scale_end) * (float)graph_width);
            //長條圖的左上 y 座標
            int boxY1 = top_marge + i * body_height;
            //設定顏色
            int colorIdx = i % showColors.size();
            g.setColor((Color)showColors.elementAt(colorIdx));
            //畫出長條圖
            g.fill3DRect(left_marge, boxY1, boxWidth, body_height - body_info_height, true);
            //處理長條圖下方之文字
            String text = (String)showTexts.elementAt(i) + " " + (String)showWidths.elementAt(i);
            str_width = fm.stringWidth(text);
            str_height = fm.getHeight();
            int strY = ((boxY1 + body_height) - body_info_height) + str_height;
            int strX = left_marge + boxWidth - str_width;
            if(strX < left_marge)
                strX = left_marge + 5;
            //show 出文字
            g.setColor(Color.black);
            g.drawString((String)showTexts.elementAt(i) + " ", strX, strY);
            //show 出數量
            g.setColor(Color.red);
            str_width = fm.stringWidth((String)showTexts.elementAt(i) + " ");
            g.drawString((String)showWidths.elementAt(i), strX + str_width, strY);
        }

        //畫出縱軸
        g.setColor(Color.black);
        g.drawLine(left_marge, top_marge - top_over, top_marge, top_marge + body_height * showWidths.size());
    }
}
