import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class CRC {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextArea  textArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CRC window = new CRC();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static String ToBin(char a) {
		String str = "";
		switch (a) {
			case '0':
				str = "0000"; break;
			case '1':
				str = "0001"; break;
			case '2':
				str = "0010"; break;
			case '3':
				str = "0011"; break;
			case '4':
				str = "0100"; break;
			case '5':
				str = "0101"; break;
			case '6':
				str = "0110"; break;
			case '7':
				str = "0111"; break;
			case '8':
				str = "1000"; break;
			case '9':
				str = "1001"; break;
			case 'a':
				str = "1010"; break;
			case 'b':
				str = "1011"; break;
			case 'c':
				str = "1100"; break;
			case 'd':
				str = "1101"; break;
			case 'e':
				str = "1110"; break;
			case 'f':
				str = "1111"; break;
		}
		return str;
	}
	
	
	public static String CRC8(String s) {
		//数组a里存放的是CRC8的多项式
		int[] a = {1,0,0,0,0,0,1,1,1};
		s = s + "00000000";    //进行CRC8之前要加8个0
		int len = 0;
		int temp = 0;
		String str = "";
		int[] A = new int[9999];	
		//提取二进制字符串里面的字符，按从前到后的顺序存入数组A中
		while (len < s.length()) {
			A[len] = s.charAt(len) - '0';
			len++;
		}
		int h = 0, t = 8;
		while (t < len && t - h == 8) {
			int n = 1;
			int hc = h;
			for (int i = h; i <= t; i++) {
				if (A[i] == a[i - h])
					temp = 0;
				else
					temp = 1;
				if (temp == 1 && n == 1) {
					n = 0;
					hc = i;
				}
				A[i] = temp;
			}
			h = hc;
			while (t - h < 8)
				t++;
		}
		
		//将进行完校验的A数组最后的32位数字转化为字符串添加入字符串s1中
		String s1 = "";
		for (int i = len - 8; i < len; i++)
			s1 = s1 + A[i];
		int start = 0;
		int end = 4;
		
		//将校验字段的二进制字符串转化为十六进制字符串
		while (start < 8) {
			str += Character.toUpperCase(Integer.toHexString(Integer.parseInt(s1.substring(start, end), 2)).charAt(0));
			start += 4;
			end += 4;
		}
		return str;
	}
	public static String CRC1(String s) {
		//数组a里存放的是CRC8的多项式
		int[] a = {1,0,0,0,0,0,1,1,1};
		s = s + "00000000";    //进行CRC8之前要加8个0
		int len = 0;
		int temp = 0;
		String str = "";
		int[] A = new int[9999];
		
		//提取二进制字符串里面的字符，按从前到后的顺序存入数组A中
		while (len < s.length()) {
			A[len] = s.charAt(len) - '0';
			len++;
		}
		int h = 0, t = 8;
		while (t < len && t - h == 8) {
			int n = 1;
			int hc = h;
			for (int i = h; i <= t; i++) {
				if (A[i] == a[i - h])
					temp = 0;
				else
					temp = 1;
				if (temp == 1 && n == 1) {
					n = 0;
					hc = i;
				}
				A[i] = temp;
			}
			h = hc;
			while (t - h < 8)
				t++;
		}
		
		//将进行完校验的A数组最后的32位数字转化为字符串添加入字符串s1中
		String s1 = "0b";
		for (int i = len - 8; i < len; i++)
			s1 = s1 + A[i];
		return s1;
	}
	public static String StrToHex(String buffer) {
		String str = "";
		str = str + buffer;
		return str;
	}
	
	//处理已经转化好为十六进制字符串的数据部分
	public  void Test(String buffer,int t) {
		buffer = StrToHex(buffer);
		int mark = 0;
		String s1 = "";
		String s2 = "8000ff5623dd8000fe553a53";//已经固定的目的地址源地址十六进制字符串
		String s3 = buffer;
		String str = "";
		String cd = "";//长度字段
		int n = buffer.length();//记录数据部分十六进制字符串的长度
		
		//数据部分不足，在后补0直到长度达标
		while (n < 92) {
			buffer = buffer + "0";
			n++;
		}
		if(n > 3000) {
			mark = 1;    //标记mark为1时代表需要分片
			s1 = buffer.substring(3000);
			buffer = buffer.substring(0,3000);
		}
		
		//如果数据部分超过1500字节，进行分片
		for (int i = 0; i < 4 - Integer.toHexString( buffer.length()/2+18).length(); i++) {
			cd = cd + "0";
		}
		
		//对长度字段长度不足前面补0，直到满足四位十六进制要求
		cd = cd+ Integer.toHexString(buffer.length() / 2 + 18);
		//将已经修改好的长度字段和数据字段的十六进制字符串添加到字符串s2中
		s2 += cd + buffer;
		
		//将十六进制字符串转化为二进制字符串
		char[] ch= s2.toCharArray();
		for (int i = 0; i < s2.length(); i++) {
			str += ToBin(ch[i]);
		}


		textField.setText("");
		textField_1.setText("0xAAAAAAAAAAAAAA");
		textField_2.setText("0xAB");
		textField_3.setText("80:00:ff:56:23:dd");
		textField_4.setText("80:00:fe:55:3a:53");
		textField_5.setText("0x" + cd);
		textField_6.setText("0b" + s3);
		textField_7.setText(CRC1(str));
		textArea.setText("AAAAAAAAAAAAAAAB8000FF5623DD8000FE553A53"+cd+buffer+"000000"+CRC8(str));
		if(mark == 1) {
			t++;
			Test(s1,t);
		}
	}
	
	/**
	 * Create the application.
	 */
	public CRC() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\u4EE5\u592A\u7F51\u5E27\u5C01\u88C5");
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setBounds(100, 100, 840, 642);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u8BF7\u8F93\u5165\u6570\u636E");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 23));
		lblNewLabel.setBounds(46, 47, 122, 36);
		frame.getContentPane().add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(203, 55, 241, 27);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel label = new JLabel("\u524D\u5BFC\u7801");
		label.setFont(new Font("宋体", Font.PLAIN, 23));
		label.setBounds(46, 110, 112, 27);
		frame.getContentPane().add(label);
		
		textField_1 = new JTextField();
		textField_1.setBounds(203, 109, 241, 27);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel label_1 = new JLabel("\u5E27\u524D\u5B9A\u754C\u7B26");
		label_1.setFont(new Font("宋体", Font.PLAIN, 23));
		label_1.setBounds(46, 170, 122, 36);
		frame.getContentPane().add(label_1);
		
		textField_2 = new JTextField();
		textField_2.setBounds(203, 174, 241, 27);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblmac = new JLabel("\u76EE\u7684MAC\u5730\u5740");
		lblmac.setFont(new Font("宋体", Font.PLAIN, 23));
		lblmac.setBounds(46, 260, 138, 36);
		frame.getContentPane().add(lblmac);
		
		textField_3 = new JTextField();
		textField_3.setBounds(203, 264, 241, 27);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblmac_1 = new JLabel("\u6E90MAC\u5730\u5740");
		lblmac_1.setFont(new Font("宋体", Font.PLAIN, 23));
		lblmac_1.setBounds(46, 327, 122, 27);
		frame.getContentPane().add(lblmac_1);
		
		textField_4 = new JTextField();
		textField_4.setBounds(203, 333, 241, 21);
		frame.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		
		JLabel label_2 = new JLabel("\u957F\u5EA6\u5B57\u6BB5");
		label_2.setFont(new Font("宋体", Font.PLAIN, 23));
		label_2.setBounds(46, 385, 102, 36);
		frame.getContentPane().add(label_2);
		
		textField_5 = new JTextField();
		textField_5.setBounds(205, 396, 239, 21);
		frame.getContentPane().add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\u6570\u636E\u5B57\u6BB5");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 23));
		lblNewLabel_1.setBounds(46, 447, 102, 36);
		frame.getContentPane().add(lblNewLabel_1);
		
		textField_6 = new JTextField();
		textField_6.setBounds(199, 458, 245, 21);
		frame.getContentPane().add(textField_6);
		textField_6.setColumns(10);
		
		JLabel label_3 = new JLabel("\u6821\u9A8C\u5B57\u6BB5");
		label_3.setFont(new Font("宋体", Font.PLAIN, 23));
		label_3.setBounds(46, 526, 102, 27);
		frame.getContentPane().add(label_3);
		
		textField_7 = new JTextField();
		textField_7.setBounds(198, 532, 246, 21);
		frame.getContentPane().add(textField_7);
		textField_7.setColumns(10);
		
		JButton button = new JButton("\u5C01\u88C5");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "输入的数据不能为空");
				else {
					String data = textField.getText();
					Test(data, 0);
					
				}
			}

		});
		button.setFont(new Font("宋体", Font.PLAIN, 23));
		button.setBounds(646, 517, 97, 36);
		frame.getContentPane().add(button);
		
		textArea = new JTextArea();
		textArea.setBounds(519, 56, 269, 283);
		frame.getContentPane().add(textArea);
		textArea.setLineWrap(true);        //激活自动换行功能 
        textArea.setWrapStyleWord(true);

	}
}
