package Kmedios;


import java.util.ArrayList;
import java.util.List;

public class ClusterAnalysis {

    private Cluster[] clusters;// 所有类簇
    private int miter;// 迭代次数
    private ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();// 所有样本点
    private int dimNum;//维度

    public ClusterAnalysis(int k, int iter, ArrayList<DataPoint> dataPoints,int dimNum) {
        clusters = new Cluster[k];// 类簇种类数
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
            for(int m = 0; m < clusters.length; m++){//每次迭代开始情况各类簇的点
                clusters[m].getDataPoints().clear();
            }
            for (int j = 0; j < dataPoints.size(); j++) {
                int clusterIndex=0;
                double minDistance=Double.MAX_VALUE;

                for (int k = 0; k < clusters.length; k++) {//判断样本点属于哪个类簇
                    double eucDistance=dataPoints.get(j).testEuclideanDistance(clusters[k].getMedoid());
                    if(eucDistance<minDistance){
                        minDistance=eucDistance;
                        clusterIndex=k;
                    }
                }

               //将该样本点添加到该类簇
                clusters[clusterIndex].addDataPoint(dataPoints.get(j));

            }

            for(int m = 0; m < clusters.length; m++){
                clusters[m].getMedoid().calcMedoid();//重新计算各类簇的质点
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