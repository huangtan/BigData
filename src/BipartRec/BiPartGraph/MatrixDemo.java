package BiPartGraph;


import java.util.Arrays;  

/** 
 * ����ϡ������ѹ���洢��������� 
 * ѹ���洢ֻ�洢����ķǿ�Ԫ�� 
 * �����������Ǵ�0��ʼ 
 */  
public class MatrixDemo<E> {  
      
  /*  public static void main(String[] args) {  
        MatrixDemo<Integer> md = new MatrixDemo<Integer>();  
        Integer[][] M = new Integer[6][7];  
        M[0][1] = 12; M[0][2] = 9;  
        M[2][0] = -3; M[2][5] = 14;  
        M[3][2] = 24; M[4][1] = 18;  
        M[5][0] = 15; M[5][3] = -7;  
  
        //md.compreMatrix(M);  
          
        Integer[][] m = new Integer[3][4];  
        m[0][0]=3;m[0][3]=5;  
        m[1][1]=-1;m[2][0]=2;  
        Integer[][] n = new Integer[4][2];  
        n[0][0]=5;n[0][1]=2;  n[1][0]=1;  
        n[2][0]=-2; n[2][1]=4;  
        md.multiMatrix(m, n);  
        System.out.println();  
        md.multiMatrix(md.compreMatrix(m), md.compreMatrix(n));  
    }  
    */  
      
    /** 
     * ��ϡ��������ѹ�� 
     */  
    public  Matrix compreMatrix(E[][] e){  
        Matrix matrix = new Matrix();  
        int mu = e.length;  
        int nu = e[0].length;  
        for(int i=0;i<mu;i++){  
            for(int j=0;j<nu;j++){  
                if(e[i][j] != null){    //ѹ���ǿ�Ԫ��  
                    Triple<E> triple = new Triple<E>(i,j,e[i][j]);  
                    matrix.add(triple);  
                }  
            }  
        }  
        matrix.mu = mu;  
        matrix.nu = nu;  
          
        //�����ǲ鿴Ԫ��  
        for(int i=0;i<matrix.tu;i++){  
            Triple<E> t = matrix.data[i];  
            System.out.print(t.i + " " + t.j + " " + t.e);  
            System.out.println();  
        }  
        System.out.println("----------");  
//      transMatrix(matrix);  
//      transMatrix2(matrix);  
        return matrix;  
    }  
      
    /** 
     * ѹ�������ת�ã�����һ 
     * ԭ��1.����������ж�Ӧ��ֵ�໥���� 
     *       2.������������±꽻�� 
     *       3.���������¾����˳��(�����ţ������зǿ�Ԫ�س��ֵĴ���) 
     */  
    public Matrix transMatrix(Matrix m){  
        Matrix t = new Matrix();    //ת�ú�ľ���  
        t.mu = m.nu;  
        t.nu = m.mu;  
  
        if(m.tu == 0) return null;  
        for(int col=0; col<m.nu;col++){  //����m���е�˳������ת�������˳��  
            for(int p=0;p<m.tu;p++){  
                if(m.data[p].j == col){  
                    Triple<E> triple = new Triple<E>(m.data[p].j, m.data[p].i, (E)m.data[p].e);  
                    t.add(triple);  
                }  
            }  
        }  
        System.out.println("�����û�����һ");  
        for(int i=0;i<t.tu;i++){  
            Triple<E> tr = t.data[i];  
            System.out.print(tr.i + " " + tr.j + " " + tr.e);  
            System.out.println();  
        }  
        return t;  
    }  
      
    /** 
     * ѹ�������ת�ã������� 
     * ԭ�������ת�ú�ľ���t��˳�򣬿��������m��ÿһ�еĵ�һ���ǿ�Ԫ���� 
     * t�е�����cpot[col]����ȷ��mÿһ���еķǿ�Ԫ�صĸ���num[col] 
     * ��ʽ�����׵õ���1.cpot[0] = 0; 
     *                2.cpot[col] = cpot[col - 1]+ num[col-1] (1<col<m.nu) 
     */  
    public Matrix transMatrix2(Matrix m){  
        Matrix t = new Matrix();    //ת�ú�ľ���  
        t.mu = m.nu;  
        t.nu = m.mu;  
          
        //num��cpot����ĳ�ʼ��  
        int[] num = new int[m.nu];  
        for(int ti=0;ti<m.tu;ti++){  //num��ʼ��  
            num[m.data[ti].j]++;  
        }  
        int[] cpot = new int[m.nu];  
        cpot[0] = 0;  
        //���col���е�һ���ǿ�Ԫ����t�е�λ��  
        for(int col=1;col<m.nu;col++){   //cpot��ʼ��  
            cpot[col] = cpot[col-1] + num[col - 1];  
        }  
        //����ת��  
        for(int p=0;p<m.tu;p++){  
            int col = m.data[p].j;  
            int q = cpot[col];  
            Triple<E> triple = new Triple<E>(m.data[p].j, m.data[p].i, (E)m.data[p].e);  
            t.add(q, triple);  
            cpot[col]++;    //���λ�õ���һ��λ�ô洢���е���һ��Ԫ��  
        }  
        //�鿴  
        System.out.println("�����û�������");  
        for(int i=0;i<t.tu;i++){  
            Triple<E> tr = t.data[i];  
            System.out.print(tr.i + " " + tr.j + " " + tr.e);  
            System.out.println();  
        }  
        return t;  
    }  
      
    /** 
     * ��ϡ�������ˡ� 
     * ǰ�᣺1.����Ԫ���ܹ����  
     *       2.һ��������е�����һ��������� 
     * ԭ������������M��N��ˣ�ǰ��M����M.colҪ����N����N.row(��֮���) 
     * �õ��������Q, Q.row=M.row, Q.col = N.col  
     * ����Q[i][j] += M[i][k] * N[k][j]  0<i<M.row,0<j<N.col,0<k<M.col��N.row   
     */  
    //public Integer[][] multiMatrix(Integer[][] m,Integer[][] n){  
    public static Double[][] multiMatrix(Double[][] m,int[][] n){
        Double[][] q = new Double[m.length][n[0].length];  
        for(int i=0;i<m.length;i++){  
            for(int j=0;j<n[0].length;j++){  
                double num = 0;  
                for(int k=0;k<n.length;k++){  
                    num += (m[i][k]==null?0:m[i][k]) * (n[k][j]==0?0:n[k][j]);  
                }  
                q[i][j] = num;  
            }  
        }  
        //��ӡ���  
        for(int i=0;i<q.length;i++){  
            for(int j=0;j<q[0].length;j++){  
                System.out.print(q[i][j]+" ");  
            }  
            System.out.println();  
        }  
        return q;  
    }  
      
    /** 
     * ѹ������ĳ˷����� 
     * ϡ�������г˷�����ʱ��ʹ����0Ԫ��Ҳ����ˣ� 
     * Ϊ����������������о���ѹ��������� 
     * ѹ���������ԭ�� 
     * 1.�Ȱ�ϡ��������ѹ�� 
     * 2.���m��n��ÿһ�еĵ�һ����0Ԫ����m��n�е�λ�� 
     * 3.����mÿ�еķ�0Ԫ�أ� 
     */  
    public  Matrix multiMatrix(Matrix m,Matrix n){  
        Matrix q = new Matrix();  
        q.mu = m.mu;  
        q.nu = n.nu;  
        //��ʼ�����е�һ����0Ԫ�ص�λ�ñ�  
        int[] mNum = new int[m.mu];  
        for(int len=0;len<m.tu;len++){   //ÿ���ж��ٸ���0Ԫ��  
            mNum[m.data[len].i]++;  
        }  
        m.rpos = new int[m.mu];  
        m.rpos[0] = 0;  
        for(int mRow=1;mRow<m.mu;mRow++){    //ÿ�е�һ����0Ԫ����m�е�λ��  
            m.rpos[mRow] = m.rpos[mRow-1] + mNum[mRow-1];  
        }  
        int[] nNum = new int[n.mu];  
        for(int len=0;len<n.tu;len++){   //ÿ���ж��ٸ���0Ԫ��  
            nNum[n.data[len].i]++;  
        }  
        n.rpos = new int[n.mu];  
        n.rpos[0] = 0;  
        for(int nRow=1;nRow<n.mu;nRow++){    //ÿ�е�һ����0Ԫ����n�е�λ��  
            n.rpos[nRow] = n.rpos[nRow-1] + nNum[nRow-1];  
        }  
  
        //��ʼ����ϣ���ʼ����  
        if(m.tu * n.tu !=0){  
            for(int arow=0;arow<m.mu;arow++){  //һ��һ�д���  
                int mlast=0;  
                if(arow < m.mu-1)  
                    mlast = m.rpos[arow+1];  
                else  
                    mlast = m.tu;  
                for(int p=m.rpos[arow];p<mlast;p++){ //����һ�е�һ����0���������һ����0����  
                    int brow = m.data[p].j; //����m.j=n.i���ɴ˿���������mԪ����˵�nԪ��  
                    int nlast=0;  
                    if(brow < n.mu-1)  
                        nlast = n.rpos[brow+1];  
                    else  
                        nlast = n.tu;  
                    for(int w=n.rpos[brow];w<nlast;w++){  
                        int ccol = n.data[w].j; //ͬһ�еķ�0Ԫ�ص���������Ȼ����ͬ  
                        int sum = (Integer)m.data[p].e * (Integer)n.data[w].e;    
                        if(sum!=0){     //��ȥ0Ԫ��  
                            Triple<Integer> triple = new Triple<Integer>(arow, ccol , sum);  
                            q.add(triple);  
                        }  
                    }  
                }  
            }  
        }  
        //��ӡ���  
        for(int i=0;i<q.tu;i++){  
            Triple<E> tr = q.data[i];  
            System.out.print(tr.i + " " + tr.j + " " + tr.e);  
            System.out.println();  
        }  
        return q;  
    }  
      
    /** 
     * �ǿ�Ԫ������Ԫ���ʾ 
     */  
    class Triple<E>{  
        int i;  //Ԫ�ص����±�  
        int j;  //Ԫ�ص����±�  
        E e;    //Ԫ��ֵ  
        Triple(int i,int j,E e) {  
            this.i = i;  
            this.j = j;  
            this.e = e;  
        }  
    }  
    /** 
     * ѹ������ 
     */  
    class Matrix{  
        Triple[] data;   //�洢��Ԫ�������  
        int mu;     //���������  
        int nu;     //���������  
        int tu;     //�ǿո���  
        int[] rpos;  //���е�һ����0Ԫ�ص�λ�ñ�  
        public Matrix(){  
            this(10);  
        }  
          
        public Matrix(int capacity) {  
            data = new Triple[capacity];  
        }  
  
        /** 
         * �Ƿ���Ҫ�������� 
         */  
        public void ensureCapacity(int minCapacity) {  
            int oldCapacity = data.length;  
            if (minCapacity > oldCapacity) { //ָ����С������ԭ�������������  
                int newCapacity = (oldCapacity * 3) / 2 + 1;    //����ԭ������1.5����1  
                if (newCapacity < minCapacity)   //�������С��Ҫ�����С����������������Ϊ��С����  
                    newCapacity = minCapacity;  
                data = Arrays.copyOf(data, newCapacity);  
            }  
        }  
        //���Ԫ�ص�ѹ������  
        public boolean add(Triple triple){  
            ensureCapacity(tu + 1);   
            data[tu++] = triple;    //size++  
            return true;  
        }  
          
        //��ָ��λ�����Ԫ�أ�����ӷǱ���ӣ�  
        public boolean add(int index,Triple triple){  
            if (index >= tu || index < 0) //����Ƿ�Խ��  
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: "+ tu);  
            ensureCapacity(tu + 1);   
            data[index] = triple;     
            tu++;  
            return true;  
        }  
    }  
      
      
}  
