package BiPartGraph;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import java.io.FileWriter;

public class Test {
	static int U = 1017;
	static int I = 469;
	static int degree_Users[] = new int[U];// �û���
	static int degree_Items[] = new int[I];// ��Ʒ��
	static int IUMatrix[][] = new int[I][U];// ѵ������Ʒ�û�����
	static Double Weight[][] = new Double[I][I];// Ȩ�ط������
	static Double Final[][] = new Double[I][U];// ������Դ�������
	static int RL[][] = new int[U][I];// �Ƽ��б�
	static int RLlength = 10;// �Ƽ��б���
	static int Userid = 0;
	static int Itemid = 0;

	public static void main(String argv[]) throws Exception {

		// Map<Integer,String>User=new HashMap<Integer,String>();
		// Map<Integer,String>Item=new HashMap<Integer,String>();
		// Map<Integer,Map<Integer,Integer>> useritem=new HashMap<Integer,
		// Map<Integer,Integer>>();
		String Item[] = new String[I];
		String User[] = new String[U];
		String Itemnum[] = new String[I];
		String Usernum[] = new String[U];

		File file = new File("C:/Users/Administrator/Desktop/cf/rating.xls");

		String[][] result = ReadCsv.getData(file, 1);
	

	int rowLength = result.length;

		for (int i = 0; i < rowLength; i++) {
			String a = result[i][0];
			String b = result[i][1];
			int flag = 0;
			int flag1 = 0;

			for (int m = 0; m < i; m++) {

				// (int)Integer.parseInt(a)==(int)Integer.parseInt(result[m][0])
				if (a.equals((result[m][0]))) {
					// System.out.println(m +""+ Usernum[15]+i);
					for (int p = 0; p <= m; p++) {
						if (Usernum[p].equals((result[m][0]))) {
							flag = p;
							break;
						}

					}
					break;
				}
				// break;
			}
			for (int m = 0; m < i; m++) {
				// Integer.parseInt(b)==Integer.parseInt(result[m][1]))
				if (b.equals(result[m][1])) {

					for (int p = 0; p <= m; p++) {
						// System.out.println(i+Itemnum[17]+m);
						if (Itemnum[p].equals(result[m][1])) {
							flag1 = p;
							break;
						}

					}
					// User[m]=(int)Integer.parseInt(b);
					break;
				}
				// break;
			}

			if (flag == 0) {

				Usernum[Userid] = result[i][0];
				Userid++;
			}

			// User.put(Userid,result[i][0]);}
			// System.out.println(Userid);
			if (flag1 == 0) {

				Itemnum[Itemid] = result[i][1];
				Itemid++;
			}

			// System.out.println(Itemid);

			// IUMatrix[Itemid-1][Userid-1]=(int)Integer.parseInt(result[i][2]);

			if ((flag != 0) && (flag1 != 0)) {
				IUMatrix[flag1][flag] = (int) Integer.parseInt(result[i][2]);
			}
			if ((flag1 != 0) && (flag == 0)) {
				IUMatrix[flag1][Userid] = (int) Integer.parseInt(result[i][2]);

			}

			if ((flag != 0) && (flag1 == 0)) {
				IUMatrix[Itemid][flag] = (int) Integer.parseInt(result[i][2]);
			}

			if ((flag == 0) && (flag1 == 0)) {
				// System.out.println(Itemid+"   "+Userid+"   "+i+"   "+IUMatrix[Itemid-1][Userid-1]);
				IUMatrix[Itemid - 1][Userid - 1] = (int) Integer
						.parseInt(result[i][2]);
			}
			// IUMatrix[Itemid][Userid]=(int)Integer.parseInt(result[i][2]);

		}

		System.out.println(Itemid + "   " + Userid);

	

		loadDegree();

		System.out.println("���ؾ���");
		calcWeight();
		System.out.println("����Ȩ��");
		calcFinal();
		System.out.println("�������÷�");
		calcRL();

		System.out.println("��������");
		int l, j;
		appendFile("F:\\ItemRE.txt", "�û�\t\t�Ƽ��б�\r\n");
		for (l = 0; l < 17; l++) {
			appendFile("F:\\ItemRE.txt", Usernum[l] + "\t\t");// �û�ID��1��ʼ
			for (j = 0; j < RLlength; j++)// �Ƽ��б��ӡ���ȿ����޸�
			{

				// System.out.println(RL[l][j]);
				appendFile("F:\\ItemRE.txt", Itemnum[RL[l][j]] + "  ");// ��ƷID��1��ʼ
			}
			appendFile("F:\\ItemRE.txt", "\r\n");

		}

		System.out.println("5");

	}

	public static void loadDegree() throws Exception {
		int i, j;
		for (i = 0; i < I; i++) {
			for (j = 0; j < U; j++) {
				if (IUMatrix[i][j] != 0) {
					degree_Users[j]++;
					degree_Items[i]++;
				}
			}
		}

	}

	public static void calcWeight() {
		try {
			int i, j, k;
			for (i = 0; i < I; i++) {
				for (j = 0; j < I; j++) {

					for (k = 0; k < U; k++) {
						Weight[i][j] = 0.0;
						if (IUMatrix[i][k] * IUMatrix[j][k] == 0) {
							Weight[i][j] = 0.0;
						} else {
							// System.out.println(k+" "+j+" "+degree_Users[k]+" "+degree_Items[j]+"\n");
							// System.out.println(Weight[1][1]);
							Weight[i][j] += IUMatrix[i][k] * IUMatrix[j][k]
									/ (degree_Users[k] * degree_Items[j]); // ����ط�Ҫ��

						System.out.println(i+" "+j+" "+Weight[i][j]+"\n");
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void calcFinal() throws Exception {
		// Final=MatrixDemo.multiMatrix(Weight,IUMatrix);
		int i, j, k;
		for (i = 0; i < I; i++) {
			for (j = 0; j < U; j++) {
				// System.out.println(i+" "+j+" "+IUMatrix[i][j]);
				for (k = 0; k < I; k++) {
					// Final[i][j]=0.0;

					Final[i][j] += Weight[i][k] * IUMatrix[k][j];
					// System.out.println(Final[i][j]);//������������ ���ǱȽ�ϡ��
					// System.out.println(Weight[i][j]);

					// System.out.println(i+" "+j+" "+ Final[i][j]);
				}
			}
		}
		System.out.println(Final[152][10]);
	}

	public static int[][] calcRL() {
		int i, j, k, l, m, maxid = 0;// �洢�����û����ǰ��Ʒid
		Double temp[] = new Double[I];// �洢�û��Ĳ�Ʒ�������������Ƚ�
		Double maxtemp;
		for (i = 0; i < U; i++) {
			m = 0;
			k = 0;
			for (j = 0; j < I; j++) {
				if (IUMatrix[j][i] == 0) {
					temp[k++] = Final[j][i];// ���û�i��������Դ�������temp
					// System.out.println(Final[j][i]);
				}
			}

			for (l = 0; l < k; l++) {// ��Ҫ���λ��k�����ֵ��λ��
				maxtemp = 0.0;
				for (m = 0; m < k; m++) {
					if (maxtemp < temp[m]) {
						maxid = m;
						maxtemp = temp[m];
					}
				}// �õ�maxid
				RL[i][l] = maxid;// �Ƽ��б�ĳλ������ֵ
				temp[maxid] = 0.0;
				// System.out.println(RL[i][l]);
			}
		}
		return RL;

	}

	public static void appendFile(String fileName, String content) {
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
