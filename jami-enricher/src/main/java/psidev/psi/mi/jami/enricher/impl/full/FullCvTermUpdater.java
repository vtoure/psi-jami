package psidev.psi.mi.jami.enricher.impl.full;


import psidev.psi.mi.jami.bridges.fetcher.CvTermFetcher;
import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.impl.minimal.MinimalCvTermUpdater;
import psidev.psi.mi.jami.enricher.listener.CvTermEnricherListener;
import psidev.psi.mi.jami.enricher.util.EnricherUtils;
import psidev.psi.mi.jami.model.CvTerm;



/**
 * Provides full update of cv term.
 *
 * - update minimal properties of CvTerm. See MinimalCvTermUpdater for more details
 * - update xrefs of CvTerm. It will add missing xrefs and remove all existing xrefs that are not in the fetched CvTerm using DefaultXrefComparator
 * - update synonyms of CvTerm. It will add missing synonyms and remove all existing synonyms that are not in the fetched CvTerm using DefaultAliasComparator
 * - update annotations of CvTerm. It will add missing annotations and remove all existing annotations that are not in the fetched CvTerm using DefaultAnnotationComparator
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since  13/05/13

 */
public class FullCvTermUpdater<C extends CvTerm> extends FullCvTermEnricher<C>{

    private MinimalCvTermUpdater<C> minimalCvTermUpdater;
    /**
     * A constructor matching super.
     *
     * @param cvTermFetcher The fetcher to initiate the enricher with.
     *                      If null, an illegal state exception will be thrown at the next enrichment.
     */
    public FullCvTermUpdater(CvTermFetcher<C> cvTermFetcher) {
        super(cvTermFetcher);
        this.minimalCvTermUpdater = new MinimalCvTermUpdater<C>(cvTermFetcher);
    }

    /**
     * <p>Constructor for FullCvTermUpdater.</p>
     *
     * @param minimalCvTermUpdater a {@link psidev.psi.mi.jami.enricher.impl.minimal.MinimalCvTermUpdater} object.
     */
    protected FullCvTermUpdater(MinimalCvTermUpdater<C> minimalCvTermUpdater) {
        super(minimalCvTermUpdater != null ? minimalCvTermUpdater.getCvTermFetcher() : null);
        this.minimalCvTermUpdater = minimalCvTermUpdater;
    }

    /** {@inheritDoc} */
    @Override
    protected void processMinimalUpdates(C cvTermToEnrich, C termFetched) throws EnricherException {
        this.minimalCvTermUpdater.processCvTerm(cvTermToEnrich, termFetched);
    }

    /** {@inheritDoc} */
    @Override
    protected void processAnnotations(C cvTermToEnrich, C termFetched) throws EnricherException{
        EnricherUtils.mergeAnnotations(cvTermToEnrich, cvTermToEnrich.getAnnotations(), termFetched.getAnnotations(), true, getCvTermEnricherListener());
    }

    /** {@inheritDoc} */
    @Override
    protected void processSynonyms(C cvTermToEnrich, C termFetched) throws EnricherException{
        EnricherUtils.mergeAliases(cvTermToEnrich, cvTermToEnrich.getSynonyms(), termFetched.getSynonyms(), true, getCvTermEnricherListener());
    }

    /** {@inheritDoc} */
    @Override
    protected void processXrefs(C cvTermToEnrich, C cvTermFetched) throws EnricherException{
        EnricherUtils.mergeXrefs(cvTermToEnrich, cvTermToEnrich.getXrefs(), cvTermFetched.getXrefs(), true, false,
                getCvTermEnricherListener(), getCvTermEnricherListener());
    }

    /** {@inheritDoc} */
    @Override
    public void setCvTermEnricherListener(CvTermEnricherListener<C> listener) {
        this.minimalCvTermUpdater.setCvTermEnricherListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public CvTermFetcher<C> getCvTermFetcher() {
        return this.minimalCvTermUpdater.getCvTermFetcher();
    }

    /** {@inheritDoc} */
    @Override
    public CvTermEnricherListener<C> getCvTermEnricherListener() {
        return this.minimalCvTermUpdater.getCvTermEnricherListener();
    }

    /** {@inheritDoc} */
    @Override
    public int getRetryCount() {
        return this.minimalCvTermUpdater.getRetryCount();
    }

    /** {@inheritDoc} */
    @Override
    public void setRetryCount(int retryCount) {
        this.minimalCvTermUpdater.setRetryCount(retryCount);
    }

    /** {@inheritDoc} */
    @Override
    public C find(C cvTermToEnrich) throws EnricherException {
        return this.minimalCvTermUpdater.find(cvTermToEnrich);
    }

    /**
     * <p>Getter for the field <code>minimalCvTermUpdater</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.enricher.impl.minimal.MinimalCvTermUpdater} object.
     */
    protected MinimalCvTermUpdater<C> getMinimalCvTermUpdater() {
        return minimalCvTermUpdater;
    }
}
