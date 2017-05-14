package edu.illinois.cs.cogcomp.finer.distribuational_semantic;

/**
 * Created by haowu4 on 1/15/17.
 */
public class Feature {
    private String featureType;
    private String featureId;

    public Feature(String featureType, String featureId) {
        this.featureType = featureType;
        this.featureId = featureId;
    }

    public String getFeatureType() {
        return featureType;
    }

    public String getFeatureId() {
        return featureId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feature feature = (Feature) o;

        if (!featureType.equals(feature.featureType)) return false;
        return featureId.equals(feature.featureId);
    }

    @Override
    public int hashCode() {
        int result = featureType.hashCode();
        result = 31 * result + featureId.hashCode();
        return result;
    }
}
