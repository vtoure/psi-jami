package psidev.psi.mi.jami.enricher.impl.full;

import psidev.psi.mi.jami.enricher.CvTermEnricher;
import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.impl.minimal.MinimalFeatureUpdater;
import psidev.psi.mi.jami.enricher.listener.EnrichmentStatus;
import psidev.psi.mi.jami.enricher.listener.FeatureEnricherListener;
import psidev.psi.mi.jami.enricher.util.EnricherUtils;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.comparator.cv.DefaultCvTermComparator;

/**
 * Provides full updater of feature.
 *
 * - update minimal properties of feature (see MinimalFeatureUpdater)
 * - update interaction dependency
 * - update interaction effect
 * - update xrefs
 * - update aliases
 * - update annotations
 * - update linked features
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 13/08/13

 */
public class FullFeatureUpdater<F extends Feature> extends FullFeatureEnricher<F> {

    private MinimalFeatureUpdater<F> minimalUpdater;

    /**
     * <p>Constructor for FullFeatureUpdater.</p>
     */
    public FullFeatureUpdater(){
        super();
        this.minimalUpdater = new MinimalFeatureUpdater<F>();
    }

    /**
     * <p>Constructor for FullFeatureUpdater.</p>
     *
     * @param minimalUpdater a {@link psidev.psi.mi.jami.enricher.impl.minimal.MinimalFeatureUpdater} object.
     */
    protected FullFeatureUpdater(MinimalFeatureUpdater<F> minimalUpdater){
        super();
        this.minimalUpdater = minimalUpdater != null ? minimalUpdater : new MinimalFeatureUpdater<F>();
    }

    /** {@inheritDoc} */
    @Override
    protected void processLinkedFeatures(F featureToEnrich, F objectSource) throws EnricherException {
        mergeLinkedFeatures(featureToEnrich, featureToEnrich.getLinkedFeatures(), objectSource.getLinkedFeatures(), true);
    }

    /** {@inheritDoc} */
    @Override
    protected void processRole(F featureToEnrich, F objectSource) throws EnricherException {
        if (!DefaultCvTermComparator.areEquals(objectSource.getRole(), featureToEnrich.getRole())){
            CvTerm old = featureToEnrich.getRole();
            featureToEnrich.setRole(objectSource.getRole());
            if(getFeatureEnricherListener() != null) {
                getFeatureEnricherListener().onRoleUpdate(featureToEnrich, old);
            }
        }
        else if (getCvTermEnricher() != null
                && featureToEnrich.getRole() != objectSource.getRole()){
            getCvTermEnricher().enrich(featureToEnrich.getRole(), objectSource.getRole());
        }
        processRole(featureToEnrich);
    }

    /** {@inheritDoc} */
    @Override
    protected void processXrefs(F objectToEnrich, F cvTermFetched) {
        EnricherUtils.mergeXrefs(objectToEnrich, objectToEnrich.getXrefs(), cvTermFetched.getXrefs(), true, false,
                getFeatureEnricherListener(), getFeatureEnricherListener());
    }

    /** {@inheritDoc} */
    @Override
    protected void processAnnotations(F objectToEnrich, F fetchedObject){
        EnricherUtils.mergeAnnotations(objectToEnrich, objectToEnrich.getAnnotations(), fetchedObject.getAnnotations(), true,
                getFeatureEnricherListener());
    }

    /** {@inheritDoc} */
    @Override
    protected void processAliases(F objectToEnrich, F termFetched) {
        EnricherUtils.mergeAliases(objectToEnrich, objectToEnrich.getAliases(), termFetched.getAliases(), true, getFeatureEnricherListener());
    }

    /** {@inheritDoc} */
    @Override
    public void setFeatureEnricherListener(FeatureEnricherListener<F> featureEnricherListener) {
        this.minimalUpdater.setFeatureEnricherListener(featureEnricherListener);
    }

    /** {@inheritDoc} */
    @Override
    public FeatureEnricherListener<F> getFeatureEnricherListener() {
        return this.minimalUpdater.getFeatureEnricherListener();
    }

    /** {@inheritDoc} */
    @Override
    public void setCvTermEnricher(CvTermEnricher cvTermEnricher) {
        this.minimalUpdater.setCvTermEnricher(cvTermEnricher);
    }

    /** {@inheritDoc} */
    @Override
    public CvTermEnricher getCvTermEnricher() {
        return this.minimalUpdater.getCvTermEnricher();
    }

    /** {@inheritDoc} */
    @Override
    public void onSequenceUpdate(Protein protein, String oldSequence) {
        this.minimalUpdater.onSequenceUpdate(protein, oldSequence);
    }

    /** {@inheritDoc} */
    @Override
    public void onShortNameUpdate(Protein interactor, String oldShortName) {
        this.minimalUpdater.onShortNameUpdate(interactor, oldShortName);
    }

    /** {@inheritDoc} */
    @Override
    public void onFullNameUpdate(Protein interactor, String oldFullName) {
        this.minimalUpdater.onFullNameUpdate(interactor, oldFullName);
    }

    /** {@inheritDoc} */
    @Override
    public void onOrganismUpdate(Protein interactor, Organism o) {
        this.minimalUpdater.onOrganismUpdate(interactor, o);
    }

    /** {@inheritDoc} */
    @Override
    public void onInteractorTypeUpdate(Protein interactor, CvTerm old) {
        this.minimalUpdater.onInteractorTypeUpdate(interactor, old);
    }

    /** {@inheritDoc} */
    @Override
    public void onAddedAlias(Protein o, Alias added) {
        this.minimalUpdater.onAddedAlias(o, added);
    }

    /** {@inheritDoc} */
    @Override
    public void onRemovedAlias(Protein o, Alias removed) {
        this.minimalUpdater.onRemovedAlias(o, removed);
    }

    /** {@inheritDoc} */
    @Override
    public void onAddedAnnotation(Protein o, Annotation added) {
        this.minimalUpdater.onAddedAnnotation(o, added);
    }

    /** {@inheritDoc} */
    @Override
    public void onRemovedAnnotation(Protein o, Annotation removed) {
        this.minimalUpdater.onRemovedAnnotation(o, removed);
    }

    /** {@inheritDoc} */
    @Override
    public void onAddedChecksum(Protein interactor, Checksum added) {
        this.minimalUpdater.onAddedChecksum(interactor, added);
    }

    /** {@inheritDoc} */
    @Override
    public void onRemovedChecksum(Protein interactor, Checksum removed) {
        this.minimalUpdater.onRemovedChecksum(interactor, removed);
    }

    /** {@inheritDoc} */
    @Override
    public void onAddedIdentifier(Protein o, Xref added) {
        this.minimalUpdater.onAddedIdentifier(o, added);
    }

    /** {@inheritDoc} */
    @Override
    public void onRemovedIdentifier(Protein o, Xref removed) {
        this.minimalUpdater.onRemovedIdentifier(o, removed);
    }

    /** {@inheritDoc} */
    @Override
    public void onAddedXref(Protein o, Xref added) {
        this.minimalUpdater.onAddedXref(o, added);
    }

    /** {@inheritDoc} */
    @Override
    public void onRemovedXref(Protein o, Xref removed) {
        this.minimalUpdater.onRemovedXref(o, removed);
    }

    /** {@inheritDoc} */
    @Override
    public void onEnrichmentComplete(Protein object, EnrichmentStatus status, String message) {
        this.minimalUpdater.onEnrichmentComplete(object, status, message);
    }

    /** {@inheritDoc} */
    @Override
    public void onEnrichmentError(Protein object, String message, Exception e) {
        this.minimalUpdater.onEnrichmentError(object, message, e);
    }

    /**
     * <p>Getter for the field <code>minimalUpdater</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.enricher.impl.minimal.MinimalFeatureUpdater} object.
     */
    protected MinimalFeatureUpdater<F> getMinimalUpdater() {
        return minimalUpdater;
    }
}
