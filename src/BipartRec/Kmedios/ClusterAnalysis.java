package Kmedios;


import java.util.ArrayList;
import java.util.List;

public class ClusterAnalysis {

    private Cluster[] clusters;// �������
    private int miter;// ��������
    private ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();// ����������
    private int dimNum;//ά��

    public ClusterAnalysis(int k, int iter, ArrayList<DataPoint> dataPoints,int dimNum) {
        clusters = new Cluster[k];// ���������
        for (int i = 0; i < k; i++) {
            clusters[i] = new Cluster("Cluster:" + i);
        }
        this.miter = iter;
        this.dataPoints = dataPoints;
        this.dimNum=dimNum;
    }

    public int getIterations() {
        return miter;
    }

    public ArrayList<DataPoint>[] getClusterOutput() {
        ArrayList<DataPoint> v[] = new ArrayList[clusters.length];
        for (int i = 0; i < clusters.length; i++) {
            v[i] = clusters[i].getDataPoints();
        }
        return v;
    }
    
    public ArrayList<Medoid> getMedios(){
    	ArrayList<Medoid> v = new ArrayList();
    	
    	 for (int i = 0; i < clusters.length; i++) {
            v.add(clusters[i].getMedoid());
         }
         return v;
    	
    }

   
    public void startAnalysis(double[][] medoids) {

        setInitialMedoids(medoids);

        double[][] newMedoids=medoids;
        double[][] oldMedoids=new double[medoids.length][this.dimNum];

        while(!isEqual(oldMedoids,newMedoids)){
            for(int m = 0; m < clusters.length; m++){//ÿ�ε�����ʼ�������صĵ�
                clusters[m].getDataPoints().clear();
            }
            for (int j = 0; j < dataPoints.size(); j++) {
                int clusterIndex=0;
                double minDistance=Double.MAX_VALUE;

                for (int k = 0; k < clusters.length; k++) {//�ж������������ĸ����
                    double eucDistance=dataPoints.get(j).testEuclideanDistance(clusters[k].getMedoid());
                    if(eucDistance<minDistance){
                        minDistance=eucDistance;
                        clusterIndex=k;
                    }
                }

               //������������ӵ������
                clusters[clusterIndex].addDataPoint(dataPoints.get(j));

            }

            for(int m = 0; m < clusters.length; m++){
                clusters[m].getMedoid().calcMedoid();//���¼������ص��ʵ�
            }

            for(int i=0;i<medoids.length;i++){
                for(int j=0;j<this.dimNum;j++){
                    oldMedoids[i][j]=newMedoids[i][j];
                }
            }


            for(int n=0;n<clusters.length;n++){
                newMedoids[n]=clusters[n].getMedoid().getDimensioin();
            }

            this.miter++;
        }


    }

    private void setInitialMedoids(double[][] medoids) {
        for (int n = 0; n < clusters.length; n++) {
            Medoid medoid = new Medoid(medoids[n]);
            clusters[n].setMedoid(medoid);
            medoid.setCluster(clusters[n]);
        }
    }

   
    private boolean isEqual(double[][] oldMedoids,double[][] newMedoids){
        boolean flag=false;
        for(int i=0;i<oldMedoids.length;i++){
            for(int j=0;j<oldMedoids[i].length;j++){
                if(oldMedoids[i][j]!=newMedoids[i][j]){
                    return flag;
                }
            }
        }
        flag=true;
        return flag;
    }
}