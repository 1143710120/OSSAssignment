package UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BmpLoading extends JFrame {
	public static void main(String[] args) {
		BmpLoading ui = new BmpLoading();
		//ui.initUI();
		ui.getvalue(1,1);
	}

	public void initUI() {

		this.setSize(600, 500);
		this.setTitle("图片查看器");

		// 设置布局
		FlowLayout layout = new FlowLayout();
		this.setLayout(layout);

		JPanel center = new myPanel();
		center.setPreferredSize(new Dimension(400, 300));
		center.setBackground(Color.WHITE);
		this.add(center);

		this.setDefaultCloseOperation(3);
		this.setVisible(true);
	}

	/**
	 * 读取BMP文件的方法(BMP24位)
	 */

	public int[][] readFile(String path) {

		try {
			// 创建读取文件的字节流
			FileInputStream fis = new FileInputStream(path);
			BufferedInputStream bis = new BufferedInputStream(fis);
			// 读取时丢掉前面的18位，
			// 读取图片的18~21的宽度
			bis.skip(18);
			byte[] b = new byte[4];
			bis.read(b);
			// 读取图片的高度22~25
			byte[] b2 = new byte[4];
			bis.read(b2);

			// 得到图片的高度和宽度
			int width = byte2Int(b);
			int heigth = byte2Int(b2);
			
			
			// 使用数组保存得图片的高度和宽度
			int[][] date = new int[heigth][width];

			int skipnum = 0;
			if (width * 3 / 4 != 0) {
				skipnum = 4 - width * 3 % 4;
			}
			
			// 读取位图中的数据，位图中数据时从54位开始的，在读取数据前要丢掉前面的数据
			bis.skip(28);
			for (int i = 0; i < date.length; i++) {
				for (int j = 0; j < date[i].length; j++) {
					// bmp的图片在window里面世3个byte为一个像素
					int blue = bis.read();
					int green = bis.read();
					int red = bis.read();
					// 创建一个Color对象，将rgb作为参数传入其中
					Color c = new Color(red, green, blue);
					// Color c = new Color(blue,green,red);
					// 将得到的像素保存到date数组中
					date[i][j] = c.getRGB();
				}
				// 如果补0的个数不为0，则需要跳过这些补上的0
				if (skipnum != 0) {
					bis.skip(skipnum);
				}
			}
			return date;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	// 将四个byte拼接成一个int
	public int byte2Int(byte[] by) {
		int t1 = by[3] & 0xff;
		int t2 = by[2] & 0xff;
		int t3 = by[1] & 0xff;
		int t4 = by[0] & 0xff;
		int num = t1 << 24 | t2 << 16 | t3 << 8 | t4;
		return num;

	}
	public  void getvalue(int hang,int lie) {
		int[][] date = readFile("E:\\新建文件夹\\bk.bmp");
		System.out.println(Integer.toHexString(date[hang-1][lie-1]));
		}
	public int[][] spilt(int num,int hang,int lie){
		int[][] date = readFile("E:\\新建文件夹\\bk.bmp");
		return date;
	}

	class myPanel extends JPanel {
		public void paint(Graphics g) {
			super.paint(g);
			// 读取数据
			int[][] date = readFile("E:\\新建文件夹\\bk.bmp");
			// 判断是否存在
			if (date != null) {
				// this.setPreferredSize(new
				// Dimension(date[0].length,date.length));
				this.setPreferredSize(new Dimension(date[0].length, date.length));
				// 遍历
				for (int i = 0; i < date.length; i++) {
					for (int j = 0; j < date[i].length; j++) {
						Color c = new Color(date[i][j]);
						g.setColor(c);
						g.drawLine(j, date.length - i, j, date.length - i);
					}
				}
			}
		}
	}
}
