package image.csu.fullerton.edu;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import Jama.Matrix;

public class GaussianClassifier {

	private List<Feature> features;
	private List<Likelihood> likelihoods;

	private class Feature {
		double[] feature;
		int c;
		
		Feature(double[] feature, int c) {
			this.feature = feature;
			this.c = c;
		}
	}
	private class Likelihood {
		int c;
		double likelihood;
		
		Likelihood(int classType, double likelihood){
			this.c = classType;
			this.likelihood = likelihood;
		}
	}
	private class sortByClasstype implements Comparator<Feature> {
		public int compare(Feature f1, Feature f2) {
			double c = f1.c - f2.c;
			if (c > 0.0)
				return 1;
			else if (c < 0.0)
				return -1;
			else
				return 0;
		}
	}
	private class sortByLikelihood implements Comparator<Likelihood> {
		public int compare(Likelihood l1, Likelihood l2) {
			double diff = l1.likelihood - l2.likelihood;
			if (diff > 0.0)
				return 1;
			else if (diff < 0.0)
				return -1;
			else
				return 0;
		}
	}
	
	GaussianClassifier() {
		features = new ArrayList<Feature>();
		likelihoods = new ArrayList<Likelihood>();
	}

	public void addDesign(double[] featureVector, int c) {
		Feature f = new Feature(featureVector, c);
		features.add(f);
	}

	/**
	 * calculate priori probability p(ci)
	 * @param subSize
	 * @param totalSize
	 * @return
	 */
	private double calculatePriori(double subSize, double totalSize){
		double p_ci = 0.0;
		if(totalSize==0){
			return Math.pow(-1, 0.5);
		}else{			
			p_ci = subSize/totalSize;
		}
		
		return p_ci;
	}
	
	/**
	 * calculate density probability p(x|ci)
	 * @param classType
	 * @param v
	 * @param density
	 * @return
	 */
	private double calculateDensity(int classType,double[] v,List<Feature> density){
		List<Feature> subFeatures= density;
		Iterator<Feature> it = subFeatures.iterator();

		int width = subFeatures.get(0).feature.length;
		double n = (double)subFeatures.size();
		double preDensity = 0.0, powerDensity =0.0 , p = 0.0;
		//double [][] powDensity = new double[1][1];
		NumberFormat formatter = new DecimalFormat("#0.000000");// formate the matrix numbers
		
		Matrix m = new Matrix(width,1);  // mean matrix width*1
		Matrix c = new Matrix(width,width);//covariance matrix width*width
		Matrix vv = new Matrix(v,width); // matrix with width rows, to be classified
		Matrix powerMatirxDensity = new Matrix(1,1);		
		//Mean vector
		while(it.hasNext()){
			Feature f = it.next();
			Matrix matrix = new Matrix(f.feature,width);
			m.plusEquals(matrix);
		}
		m = m.times(1/n);
		System.out.println("M"+classType+" Mean vector:");
		m.print(formatter, 1);
		
		//Covariance matrix
		it = subFeatures.iterator();
		while(it.hasNext()){
			Feature f = it.next();
			Matrix matrix = new Matrix(f.feature,width);
			Matrix xxt = matrix.times(matrix.transpose());
			c.plusEquals(xxt);
		}
		Matrix mmt = m.times(m.transpose());
		c = c.times(1/n).minusEquals(mmt);
		System.out.println("C"+classType+" Covariance matrix:");
		c.print(formatter, 1);
		
		// Covanriance matrix inverse
		Matrix x_mt = vv.minus(m).transpose();
		Matrix c_inverse;
		Matrix x_m = vv.minus(m);
		if(!c.lu().isNonsingular()){
			System.out.println("Does not exist covariance matrix inverse(Ci)." +
					"Can not get this density probability for C"+classType);// This classType would not be added to likelihoods list
			return Math.pow(-1, 0.5); //return NaN
		}else{
			c_inverse = c.inverse();
			powerMatirxDensity = x_mt.times(c_inverse).times(x_m).times((-0.5));
			powerDensity = powerMatirxDensity.getArray()[0][0];
		}
		
		preDensity = 1/(Math.pow(2*Math.PI,width/2)*Math.pow(c.det(),0.5));
		if(Double.isNaN(preDensity)){
			System.out.println("Covariance determinant value is negative. " +
					"Can not get this density probability for C"+classType);// This classType would not be added to likelihoods list
			return Math.pow(-1, 0.5);
		}else{
			p = preDensity*Math.exp(powerDensity);
		}	
		return p;
	}
	/**
	 *  precalculate for basic bayes probality (p(ci)*p(x|ci)),   
	 * @param v
	 */
	private void preCalculate(double[] v){
		Iterator<Feature> it = features.iterator();
		ArrayList<Integer> al = new ArrayList<Integer>();
		double totalDataSize = (double)features.size();
		int j = 0, index = 0;
		double priori = 0.0, density = 0.0;
				
		while (it.hasNext()) {
			Feature f = it.next();
			for (int i = al.size(); i <= f.c + 1; i++) {
				al.add(0);	
			}
			al.set(f.c, al.get(f.c) + 1);// System.out.println("set("+f.c+","+al.get(f.c)+")");
		}
		
		for(Integer classSize:al){
			if(classSize.intValue()!=0){
//			System.out.println("class"+j+" total size is "+classSize.intValue());			
			priori = calculatePriori((double)classSize.intValue(),(double)totalDataSize);
			density = calculateDensity(j,v,features.subList(index,index+classSize.intValue()));
				if(!Double.isNaN(density)&&!Double.isNaN(priori)){
					Likelihood ld = new Likelihood(j,priori*density);//j here means class type
					likelihoods.add(ld);  //(p(ci)*p(x|ci)) add to likelihood
				}				
			index +=classSize.intValue();
			}
			j++;
		}
	}

	int testVector(double[] v) {
		Collections.sort(features, new sortByClasstype());		
		preCalculate(v);
		Collections.sort(likelihoods, new sortByLikelihood());
		for(Likelihood l:likelihoods){
			System.out.println("New sorted likelihoods:"+l.likelihood +"The classified class is C"+l.c);
		}
		System.out.println("This object is classified as C"+likelihoods.get(0).c+" by Gaussian");
		return likelihoods.get(0).c;
	}
}
