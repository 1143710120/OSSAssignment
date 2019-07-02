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
		this.setTitle("ͼƬ�鿴��");

		// ���ò���
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
	 * ��ȡBMP�ļ��ķ���(BMP24λ)
	 */

	public int[][] readFile(String path) {

		try {
			// ������ȡ�ļ����ֽ���
			FileInputStream fis = new FileInputStream(path);
			BufferedInputStream bis = new BufferedInputStream(fis);
			// ��ȡʱ����ǰ���18λ��
			// ��ȡͼƬ��18~21�Ŀ��
			bis.skip(18);
			byte[] b = new byte[4];
			bis.read(b);
			// ��ȡͼƬ�ĸ߶�22~25
			byte[] b2 = new byte[4];
			bis.read(b2);

			// �õ�ͼƬ�ĸ߶ȺͿ��
			int width = byte2Int(b);
			int heigth = byte2Int(b2);
			
			
			// ʹ�����鱣���ͼƬ�ĸ߶ȺͿ��
			int[][] date = new int[heigth][width];

			int skipnum = 0;
			if (width * 3 / 4 != 0) {
				skipnum = 4 - width * 3 % 4;
			}
			
			// ��ȡλͼ�е����ݣ�λͼ������ʱ��54λ��ʼ�ģ��ڶ�ȡ����ǰҪ����ǰ�������
			bis.skip(28);
			for (int i = 0; i < date.length; i++) {
				for (int j = 0; j < date[i].length; j++) {
					// bmp��ͼƬ��window������3��byteΪһ������
					int blue = bis.read();
					int green = bis.read();
					int red = bis.read();
					// ����һ��Color���󣬽�rgb��Ϊ������������
					Color c = new Color(red, green, blue);
					// Color c = new Color(blue,green,red);
					// ���õ������ر��浽date������
					date[i][j] = c.getRGB();
				}
				// �����0�ĸ�����Ϊ0������Ҫ������Щ���ϵ�0
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

	// ���ĸ�byteƴ�ӳ�һ��int
	public int byte2Int(byte[] by) {
		int t1 = by[3] & 0xff;
		int t2 = by[2] & 0xff;
		int t3 = by[1] & 0xff;
		int t4 = by[0] & 0xff;
		int num = t1 << 24 | t2 << 16 | t3 << 8 | t4;
		return num;

	}
	public  void getvalue(int hang,int lie) {
		int[][] date = readFile("E:\\�½��ļ���\\bk.bmp");
		System.out.println(Integer.toHexString(date[hang-1][lie-1]));
		}
	public int[][] spilt(int num,int hang,int lie){
		int[][] date = readFile("E:\\�½��ļ���\\bk.bmp");
		return date;
	}

	class myPanel extends JPanel {
		public void paint(Graphics g) {
			super.paint(g);
			// ��ȡ����
			int[][] date = readFile("E:\\�½��ļ���\\bk.bmp");
			// �ж��Ƿ����
			if (date != null) {
				// this.setPreferredSize(new
				// Dimension(date[0].length,date.length));
				this.setPreferredSize(new Dimension(date[0].length, date.length));
				// ����
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
