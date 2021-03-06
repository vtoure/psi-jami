package psidev.psi.mi.jami.utils.comparator.feature;

import psidev.psi.mi.jami.model.*;

import java.util.Comparator;

/**
 * Generic feature comparator.
 * Entity features come first, then Biological features come first and then experimental features.
 * - It uses ModelledFeatureComparator to compare biological features
 * - It uses FeatureEvidenceComparator to compare experimental features
 * - It uses AbstractFeatureBaseComparator to compare basic feature properties
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>04/02/13</pre>
 */
public class FeatureComparator implements Comparator<Feature> {

    private ModelledFeatureComparator biologicalFeatureComparator;
    private FeatureEvidenceComparator experimentalFeatureComparator;
    private Comparator<Feature> featureBaseComparator;

    /**
     * <p>Constructor for FeatureComparator.</p>
     *
     * @param featureBaseComparator a {@link java.util.Comparator} object.
     * @param cvTermComparator a {@link java.util.Comparator} object.
     */
    public FeatureComparator(Comparator<Feature> featureBaseComparator, Comparator<CvTerm> cvTermComparator){
        if (featureBaseComparator == null){
            throw new IllegalArgumentException("The featureBaseComparator is required to create more specific feature comparators and compares basic feature properties. It cannot be null");
        }
        this.featureBaseComparator = featureBaseComparator;
        this.biologicalFeatureComparator = new ModelledFeatureComparator(this.featureBaseComparator);
        this.experimentalFeatureComparator = new FeatureEvidenceComparator(this.featureBaseComparator, cvTermComparator);

    }

    /**
     * <p>Getter for the field <code>biologicalFeatureComparator</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.utils.comparator.feature.ModelledFeatureComparator} object.
     */
    public ModelledFeatureComparator getBiologicalFeatureComparator() {
        return biologicalFeatureComparator;
    }

    /**
     * <p>Getter for the field <code>experimentalFeatureComparator</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.utils.comparator.feature.FeatureEvidenceComparator} object.
     */
    public FeatureEvidenceComparator getExperimentalFeatureComparator() {
        return experimentalFeatureComparator;
    }

    /**
     * <p>Getter for the field <code>featureBaseComparator</code>.</p>
     *
     * @return a {@link java.util.Comparator} object.
     */
    public Comparator<Feature> getFeatureBaseComparator() {
        return featureBaseComparator;
    }

    /**
     * Biological features come first and then experimental features.
     * - It uses ModelledFeatureComparator to compare biological features
     * - It uses FeatureEvidenceComparator to compare experimental features
     * - It uses AbstractFeatureBaseComparator to compare basic feature properties
     *
     * @param feature1 a {@link psidev.psi.mi.jami.model.Feature} object.
     * @param feature2 a {@link psidev.psi.mi.jami.model.Feature} object.
     * @return a int.
     */
    public int compare(Feature feature1, Feature feature2) {
        int EQUAL = 0;
        int BEFORE = -1;
        int AFTER = 1;

        if (feature1 == feature2){
            return 0;
        }
        else if (feature1 == null){
            return AFTER;
        }
        else if (feature2 == null){
            return BEFORE;
        }
        else {
            // first check if both features are from the same interface

            // both are biological features
            boolean isBiologicalFeature1 = feature1 instanceof ModelledFeature;
            boolean isBiologicalFeature2 = feature2 instanceof ModelledFeature;
            if (isBiologicalFeature1 && isBiologicalFeature2){
                return biologicalFeatureComparator.compare((ModelledFeature) feature1, (ModelledFeature) feature2);
            }
            // the biological feature is before
            else if (isBiologicalFeature1){
                return BEFORE;
            }
            else if (isBiologicalFeature2){
                return AFTER;
            }
            else {
                // both are experimental features
                boolean isExperimentalFeature1 = feature1 instanceof FeatureEvidence;
                boolean isExperimentalFeature2 = feature2 instanceof FeatureEvidence;
                if (isExperimentalFeature1 && isExperimentalFeature2){
                    return experimentalFeatureComparator.compare((FeatureEvidence) feature1, (FeatureEvidence) feature2);
                }
                // the experimental feature is before
                else if (isExperimentalFeature1){
                    return BEFORE;
                }
                else if (isExperimentalFeature2){
                    return AFTER;
                }
                else {
                    return featureBaseComparator.compare(feature1, feature2);
                }
            }
        }
    }
}
