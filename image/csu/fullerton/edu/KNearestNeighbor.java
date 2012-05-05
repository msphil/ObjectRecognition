package image.csu.fullerton.edu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class KNearestNeighbor {

	private List<Feature> features;

	private class Feature {
		double[] feature;
		int c;
		double distance;

		Feature(double[] feature, int c) {
			this.feature = feature;
			this.c = c;
		}
	}

	private class distCompare implements Comparator<Feature> {
		public int compare(Feature f1, Feature f2) {
			double dist = f1.distance - f2.distance;
			if (dist > 0.0)
				return 1;
			else if (dist < 0.0)
				return -1;
			else
				return 0;
		}
	}
	
	KNearestNeighbor() {
		features = new ArrayList<Feature>();
	}

	void addDesign(double[] featureVector, int c) {
		Feature f = new Feature(featureVector, c);
		features.add(f);
	}

	private void calculateDistances(double[] v) {
		Iterator<Feature> it = features.iterator();
		while (it.hasNext()) {
			Feature f = it.next();
			double d = 0.0;
			for (int i = 0; i < f.feature.length; i++) {
				double diff = v[i] - f.feature[i];
				d += diff * diff;
			}
			f.distance = Math.sqrt(d);

		}
	}

	int majorityVote(int k) {
		Iterator<Feature> it = features.subList(0, k).iterator();
		ArrayList<Integer> al = new ArrayList<Integer>();

		while (it.hasNext()) {
			Feature f = it.next();
			for (int i = al.size(); i <= f.c + 1; i++) {
				al.add(0);
			}
			al.set(f.c, al.get(f.c) + 1);
		}
		return al.indexOf(Collections.max(al));
	}

	int testVector(double[] v, int k) {
		calculateDistances(v);
		Collections.sort(features, new distCompare());
		return majorityVote(k);
	}

}
