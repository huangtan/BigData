package Kmedios;

/*K���ĵ��㷨��K-medoids��

ǰ�������k-means�㷨�����о��˸��㷨��ȱ�㡣��K���ĵ��㷨��K-medoids�������ܽ��k-means�㷨�е� ������������������⡣

��ν�����أ�

���ȣ����ǵý�����k-means�㷨Ϊʲô��ԡ����������С����ǵ�K-meansѰ���ʵ�Ĺ����𣿶�ĳ��������е�������ά����ƽ��ֵ������ø�����ʵ��ά�ȡ�����������������С�����������Ⱥ�㣩ʱ���ڼ�������ʵ�Ĺ����л��ܵ������쳣ά�ȵĸ��ţ���������ʵ��ʵ���ʵ�λ��ƫ����󣬴Ӷ�ʹ��ط��������䡱��

Eg: ���C1���Ѿ�������A(1,1)��B(2,2)�� C(1,2)�� D(2,1)�� ����N(100,100)Ϊ�쳣�㣬�����������C1ʱ�������ʵ�Centroid((1+2+1+2+100)/5,(1+2+2+1+100)/5)=centroid(21,21),��ʱ������������C1�ʵ��ƫ�ƣ�����һ�ֵ������»����������ʱ�򣬽��������������C1�����������룬��˵õ���׼ȷ�ľ�������

Ϊ�˽�������⣬K���ĵ��㷨��K-medoids��������µ��ʵ�ѡȡ��ʽ�������Ǽ���k-means�㷨���þ�ֵ���㷨����K���ĵ��㷨�У�ÿ�ε�������ʵ㶼�ǴӾ������������ѡȡ����ѡȡ�ı�׼���ǵ����������Ϊ�µ��ʵ���������صľ���������ʹ����ظ����ա����㷨ʹ�þ�������׼������һ����صĽ��ճ̶ȡ�

������������� <wbr>K���ĵ��㷨��k-mediods��  (p�ǿռ��е������㣬Oj�����Cj���ʵ�)

���ĳ�������Ϊ�ʵ�󣬾��������С��ԭ�ʵ�����ɵľ�������ôK���ĵ��㷨��Ϊ���������ǿ���ȡ��ԭ�ʵ�ģ���һ�ε����ؼ�������ʵ��ʱ������ѡ����������С���Ǹ��������Ϊ�µ��ʵ㡣

Eg��������A �C>E1=10

������B �C>E2=11

������C �C>E3=12

ԭ�ʵ�O�C>E4=13��������ѡ��A��Ϊ��ص����ʵ㡣

��K-means�㷨һ����K-medoidsҲ�ǲ���ŷ����þ���������ĳ�������㵽���������ĸ���ء���ֹ�����ǣ������е���ص��ʵ㶼���ڷ����仯ʱ������Ϊ���������

���㷨���˸���K-means�ġ������������Ժ�����ȱ���K-meansһ�£��������ڲ����µ��ʵ�������Ҳʹ���㷨��ʱ�临�Ӷ�������O��k(n-k)2��
*/

import java.util.ArrayList;
public class Cluster {
    private String clusterName; // �����
    private Medoid medoid; // ��ص��ʵ�
    private ArrayList<DataPoint> dataPoints; // ����и�������

    public Cluster(String clusterName) {
        this.clusterName = clusterName;
        this.medoid = null; // will be set by calling setCentroid()
        dataPoints = new ArrayList<DataPoint>();
    }

    public void setMedoid(Medoid c) {
        medoid = c;
    }

    public Medoid getMedoid() {
        return medoid;
    }

   
    public void addDataPoint(DataPoint dp) { // called from CAInstance
        dp.setCluster(this);// ��ע���������ĳ��,����ŷʽ����
        this.dataPoints.add(dp);
    }

    public void removeDataPoint(DataPoint dp) {
        this.dataPoints.remove(dp);
    }

    public int getNumDataPoints() {
        return this.dataPoints.size();
    }

    public DataPoint getDataPoint(int pos) {
        return (DataPoint) this.dataPoints.get(pos);
    }


    public String getName() {
        return this.clusterName;
    }

    public ArrayList<DataPoint> getDataPoints() {
        return this.dataPoints;
    }
}








