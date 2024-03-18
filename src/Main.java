import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.round;

public class Main {
    public static void main(String[] args) {
        String line;
        int counter=0,k,l=0;
        Scanner in = new Scanner(System.in);
        System.out.println("Denote number of nearest neighbors");
        k=in.nextInt();
        System.out.println("Insert path to the training file");
        String fi = in.nextLine();
        fi = in.nextLine();
        try (BufferedReader br = new BufferedReader(new FileReader(fi))) {
            while ((line = br.readLine()) != null) {
                if(counter==0){
                    String[] row = line.split(",");
                    l=row.length-1;
                }
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        double[][] values = new double[counter][l];
        String[] type = new String[counter];
        counter=0;
        try (BufferedReader br = new BufferedReader(new FileReader(fi))) {
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                for (int i=0;i<row.length;i++){
                    if(i==row.length-1){
                        type[counter]=row[i];
                    }else{
                        values[counter][i]=Double.parseDouble(row[i]);
                    }
                }
                counter++;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        l=0;
        while(l!=4){
            System.out.println("Choose option:");
            System.out.println("1. Classification of all observations from the test set given in a separate file");
            System.out.println("2. Classification of the observation given by the user in the console");
            System.out.println("3. Change k");
            System.out.println("4. exit");
            l=in.nextInt();
            while(l!=1&&l!=2&&l!=3&&l!=4){
                System.out.println("You did wrong input, try again");
                l=in.nextInt();
            }
            switch (l){
                case 1:
                    int cor=0, all=0;
                    String res;
                    System.out.println("Insert path to the file");
                    String file;
                    file =in.nextLine();
                    file =in.nextLine();
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        while ((line = br.readLine()) != null) {
                            all++;
                            String[] row = line.split(",");
                            String[] row1 = new String[row.length-1];
                            System.arraycopy(row,0,row1,0,row1.length);
                            System.out.print(Arrays.toString(row1)+"   ");
                            double[][] nearest;
                            nearest=getNearest(values,row1,k);
                            res = getAnswer(nearest,type);
                            System.out.println("kNN answer: "+res+"  ,correct answer: "+row[row.length-1]);
                            if(res.equals(row[row.length-1])){
                                cor++;
                            }
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    double resault = (double) cor /all;
                    System.out.println("Accuracy: "+(resault*100)+"%");
                    break;
                case 2:
                    System.out.println("Insert data, use \',\' between values");
                    line = in.nextLine();
                    line = in.nextLine();
                    String[] row = line.split(",");
                    while(row.length!=values[0].length){
                        System.out.println("Insert data cannot be compared with default data. Insert new data, use \',\' between values");
                        line = in.nextLine();
                        row = line.split(",");
                    }
                    double[][] nearest;
                    nearest=getNearest(values,row,k);
                    System.out.println();
                    System.out.println(getAnswer(nearest,type));
                    System.out.println();
                    break;
                case 3:
                    System.out.println("Insert new value of k");
                    k=in.nextInt();
                    while(k<1) {
                        System.out.println("k cannot be smaller than 1");
                        k = in.nextInt();
                    }
                    break;
            }
        }
    }
    public static String getAnswer(double[][] nearest,String[] type){
        Map<String,Integer> map = new HashMap<>();
        for(int i=0;i<nearest.length;i++){
            if(map.containsKey(type[(int)nearest[i][1]])) map.put(type[(int)nearest[i][1]],map.get(type[(int)nearest[i][1]])+1);
            else map.put(type[(int)nearest[i][1]],1);
        }
        return map.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
    }
    public static double[][] getNearest(double[][] values, String[] ins,int k){
        double[][] nearest = new double[k][2];
        double[] coor = new double[ins.length];
        for(int i=0;i<coor.length;i++){
            coor[i]=Double.parseDouble(ins[i]);
        }
        double S=0;
        for (int i=0;i<values.length;i++){
            for(int n=0;n<values[0].length;n++){
                S+=Math.pow((values[i][n]-coor[n]),2);
            }
            if(k>i+1){
                nearest[i][0]=S;
                nearest[i][1]=i;
            }else if(i+1==k){
                nearest[i][0]=S;
                nearest[i][1]=i;
                nearest=sortD(nearest);
            }else if(S<nearest[0][0]){
                nearest[0][0]=S;
                nearest[0][1]=i;
                nearest=sortD(nearest);
            }
            S=0;
        }
        return nearest;
    }
    public static double[][] sortD(double[][] a){
        int ind;
        double min;
        for(int i=0;i<a.length;i++){
            ind=0;
            min=a[0][0];
            for (int k=0;k< a.length-i;k++){
                if(min>a[k][0]){
                    ind=k;
                    min=a[k][0];
                }
            }
            min=a[a.length-1-i][0];
            a[a.length-1-i][0]=a[ind][0];
            a[ind][0]=min;
            min=a[a.length-1-i][1];
            a[a.length-1-i][1]=a[ind][1];
            a[ind][1]=min;
        }
        return a;
    }
}