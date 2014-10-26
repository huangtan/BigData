package Kmedios;

import java.util.ArrayList;

public class DataPoint {
    private double dimension[]; //����ά��
    private String pointName; //�������
    private Cluster cluster; //���
    private double euDt;//��㵽�ʵ�ľ���

    public DataPoint(double dimension[], String pointName) {
        this.dimension = dimension;
        this.pointName = pointName;
        this.cluster = null;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }


   
    public double calEuclideanDistanceSum() {
        double sum=0.0;
        Cluster cluster=this.getCluster();
        ArrayList<DataPoint> dataPoints=cluster.getDataPoints();

        for(int i=0;i<dataPoints.size();i++){
            double[] dims=dataPoints.get(i).getDimensioin();
            for(int j=0;j<dims.length;j++){
                 double temp=Math.pow((dims[j]-this.dimension[j]),2);
                 sum=sum+temp;
            }
        }

        return Math.sqrt(sum);
    }

   
    public double testEuclideanDistance(Medoid c) {
        double sum=0.0;
        double[] cDim=c.getDimensioin();

        for(int i=0;i<dimension.length;i++){
           double temp=Math.pow((dimension[i]-cDim[i]),2);
           sum=sum+temp;
        }

        return Math.sqrt(sum);
    }

    public double[] getDimensioin() {
        return this.dimension;
    }

    public Cluster getCluster() {
        return this.cluster;
    }

    public double getCurrentEuDt() {
        return this.euDt;
    }

    public String getPointName() {
        return this.pointName;
    }
    //ͨ��ά�Ȼ�ȡ��ݵ�
    public  DataPoint getDatapointBydimen(double dimension[]){
    	DataPoint point =new DataPoint(this.dimension,this.pointName);
    	if(dimension==this.dimension){
    		return point;
    	}
		return null;
  
    }
    
   
	public double getEuDt() {
		return euDt;
	}

	public void setEuDt(double euDt) {
		this.euDt = euDt;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
}