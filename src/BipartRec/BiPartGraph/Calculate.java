package BiPartGraph;
import java.io.FileWriter;
import java.io.IOException;

//int static i=6300;
public class Calculate {
	static int U = 1344;
	static int I = 1300;
	static int IUMatrix[][] = new int[I][U];// ѵ������Ʒ�û�����
	static Double Weight[][] = new Double[I][I];// Ȩ�ط������
	static Double Final[][] = new Double[I][U];// ������Դ�������
	static int RL[][] = new int[U][I];// �Ƽ��б�
	static int RLlength = 10;// �Ƽ��б���
	static int Userid = 0;
	static int Itemid = 0;
	static int[] degree_Users;
	static int[] degree_Items;

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
						if (IUMatrix[i][k] * IUMatrix[j][k] == 0) {
							Weight[i][j] = 0.0;
						} else {
							Weight[i][j] += IUMatrix[i][k] * IUMatrix[j][k]
									/ degree_Users[k] / degree_Items[j]; // ����ط�Ҫ��
							// System.out.println(i+" "+j+" "+Weight[i][j]);
							// System.out.println(i+" "+k+" "+IUMatrix[i][k]);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void calcFinal() throws Exception {
		int i, j, k;
		for (i = 0; i < I; i++) {
			for (j = 0; j < U; j++) {
				// System.out.println(i+" "+j+" "+IUMatrix[i][j]);
				for (k = 0; k < I; k++) {
					Final[i][j] = (double) 0;
					Final[i][j] += Weight[i][k] * IUMatrix[k][j];
					// System.out.println(IUMatrix[i][j]);//������������ ���ǱȽ�ϡ��
					// System.out.println(Weight[i][j]);

					// System.out.println(i+" "+j+" "+ Final[i][j]);
				}
			}
		}
		// System.out.println(Final[152][10]);
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
