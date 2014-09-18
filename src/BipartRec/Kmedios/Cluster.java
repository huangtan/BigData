package Kmedios;

/*K中心点算法（K-medoids）

前面介绍了k-means算法，并列举了该算法的缺点。而K中心点算法（K-medoids）正好能解决k-means算法中的 “噪声”敏感这个问题。

如何解决的呢？

首先，我们得介绍下k-means算法为什么会对“噪声”敏感。还记得K-means寻找质点的过程吗？对某类簇中所有的样本点维度求平均值，即获得该类簇质点的维度。当聚类的样本点中有“噪声”（离群点）时，在计算类簇质点的过程中会受到噪声异常维度的干扰，造成所得质点和实际质点位置偏差过大，从而使类簇发生“畸变”。

Eg: 类簇C1中已经包含点A(1,1)、B(2,2)、 C(1,2)、 D(2,1)， 假设N(100,100)为异常点，当它纳入类簇C1时，计算质点Centroid((1+2+1+2+100)/5,(1+2+2+1+100)/5)=centroid(21,21),此时可能造成了类簇C1质点的偏移，在下一轮迭代重新划分样本点的时候，将大量不属于类簇C1的样本点纳入，因此得到不准确的聚类结果。

为了解决该问题，K中心点算法（K-medoids）提出了新的质点选取方式，而不是简单像k-means算法采用均值计算法。在K中心点算法中，每次迭代后的质点都是从聚类的样本点中选取，而选取的标准就是当该样本点成为新的质点后能提高类簇的聚类质量，使得类簇更紧凑。该算法使用绝对误差标准来定义一个类簇的紧凑程度。

聚类分析（三） <wbr>K中心点算法（k-mediods）  (p是空间中的样本点，Oj是类簇Cj的质点)

如果某样本点成为质点后，绝对误差能小于原质点所造成的绝对误差，那么K中心点算法认为该样本点是可以取代原质点的，在一次迭代重计算类簇质点的时候，我们选择绝对误差最小的那个样本点成为新的质点。

Eg：样本点A –>E1=10

样本点B –>E2=11

样本点C –>E3=12

原质点O–>E4=13，那我们选举A作为类簇的新质点。

与K-means算法一样，K-medoids也是采用欧几里得距离来衡量某个样本点到底是属于哪个类簇。终止条件是，当所有的类簇的质点都不在发生变化时，即认为聚类结束。

该算法除了改善K-means的“噪声”敏感以后，其他缺点和K-means一致，并且由于采用新的质点计算规则，也使得算法的时间复杂度上升：O（k(n-k)2）
*/

import java.util.ArrayList;
public class Cluster {
    private String clusterName; // 类簇名
    private Medoid medoid; // 类簇的质点
    private ArrayList<DataPoint> dataPoints; // 类簇中各样本点

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
        dp.setCluster(this);// 标注该类簇属于某点,计算欧式距离
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








