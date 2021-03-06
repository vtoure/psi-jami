package psidev.psi.mi.jami.enricher.impl.minimal;

import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.bridges.fetcher.CvTermFetcher;
import psidev.psi.mi.jami.enricher.CvTermEnricher;
import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.impl.AbstractMIEnricher;
import psidev.psi.mi.jami.enricher.listener.CvTermEnricherListener;
import psidev.psi.mi.jami.enricher.listener.EnrichmentStatus;
import psidev.psi.mi.jami.enricher.util.EnricherUtils;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.CvTermUtils;

/**
 * Provides minimal enrichment of cv term.
 *
 * - enrich fullname of CvTerm if not set. It will not override any existing fullName with the one loaded from the fetched CvTerm
 * - enrich identifiers of CvTerm. It will only add missing identifiers and not remove any existing identifiers using DefaultXrefComparator
 *
 * It will ignore all other properties of a CvTerm
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 08/05/13

 */
public class    MinimalCvTermEnricher<C extends CvTerm> extends AbstractMIEnricher<C> implements CvTermEnricher<C>{

    private int retryCount = 5;

    private CvTermFetcher<C> fetcher = null;
    private CvTermEnricherListener<C> listener = null;

    /**
     * A constructor matching super.
     *
     * @param cvTermFetcher The fetcher to initiate the enricher with.
     *                      If null, an illegal state exception will be thrown at the next enrichment.
     */
    public MinimalCvTermEnricher(CvTermFetcher<C> cvTermFetcher) {
        if (cvTermFetcher == null){
            throw new IllegalArgumentException("The fetcher is required and cannot be null");
        }
        this.fetcher = cvTermFetcher;
    }

    /**
     * The fetcher to be used for used for fetcher.
     *
     * @return  The fetcher which is being used for fetching.
     */
    public CvTermFetcher<C> getCvTermFetcher() {
        return fetcher;
    }

    /**
     * {@inheritDoc}
     *
     * The cvTermEnricherListener to be used.
     * It will be fired at all points where a change is made to the cvTerm
     */
    public void setCvTermEnricherListener(CvTermEnricherListener<C> listener) {
        this.listener = listener;
    }
    /**
     * The current CvTermEnricherListener.
     *
     * @return  the current listener. May be null.
     */
    public CvTermEnricherListener<C> getCvTermEnricherListener() {
        return listener;
    }

    /**
     * <p>Getter for the field <code>retryCount</code>.</p>
     *
     * @return a int.
     */
    public int getRetryCount() {
        return retryCount;
    }

    /**
     * <p>Setter for the field <code>retryCount</code>.</p>
     *
     * @param retryCount a int.
     */
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    /**
     * A method that can be overridden to add to or change the behaviour of enrichment without effecting fetching.
     *
     * @param cvTermToEnrich the CvTerm to enrich
     * @param cvTermFetched a C object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    public void processCvTerm(C cvTermToEnrich, C cvTermFetched) throws EnricherException {

        //ShortName not checked - never null

        // == FullName ================================================================
        processFullName(cvTermToEnrich, cvTermFetched);

        // == Identifiers =============================================================
        processIdentifiers(cvTermToEnrich, cvTermFetched);
    }

    /**
     * <p>processIdentifiers.</p>
     *
     * @param cvTermToEnrich a C object.
     * @param cvTermFetched a C object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processIdentifiers(C cvTermToEnrich, C cvTermFetched) throws EnricherException {
        EnricherUtils.mergeXrefs(cvTermToEnrich, cvTermToEnrich.getIdentifiers(), cvTermFetched.getIdentifiers(), false, true,
                getCvTermEnricherListener(), getCvTermEnricherListener());
    }

    /**
     * <p>processFullName.</p>
     *
     * @param cvTermToEnrich a C object.
     * @param cvTermFetched a C object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processFullName(C cvTermToEnrich, C cvTermFetched) throws EnricherException{
        if(cvTermToEnrich.getFullName() == null
                && cvTermFetched.getFullName() != null){

            cvTermToEnrich.setFullName(cvTermFetched.getFullName());
            if (getCvTermEnricherListener() != null)
                getCvTermEnricherListener().onFullNameUpdate(cvTermToEnrich, null);
        }
    }

    /** {@inheritDoc} */
    @Override
    public C find(C cvTermToEnrich) throws EnricherException {
        C cvTermFetched = null;

        if(cvTermToEnrich.getMIIdentifier() != null){
            cvTermFetched = fetchCvTerm(cvTermToEnrich.getMIIdentifier(), CvTermUtils.getPsimi());
            if(cvTermFetched != null) return cvTermFetched;
        }

        if(cvTermToEnrich.getMODIdentifier() != null){
            cvTermFetched = fetchCvTerm(cvTermToEnrich.getMODIdentifier(), CvTermUtils.getPsimod());
            if(cvTermFetched != null) return cvTermFetched;
        }

        if(cvTermToEnrich.getPARIdentifier() != null){
            cvTermFetched = fetchCvTerm(cvTermToEnrich.getPARIdentifier(), CvTermUtils.getPsipar());
            if(cvTermFetched != null) return cvTermFetched;
        }

        for(Xref identifierXref : cvTermToEnrich.getIdentifiers()){
            cvTermFetched = fetchCvTerm(identifierXref.getId(), identifierXref.getDatabase());
            if(cvTermFetched != null) return cvTermFetched;
        }

        String name = cvTermToEnrich.getFullName() != null ? cvTermToEnrich.getFullName() : cvTermToEnrich.getShortName();
        if(cvTermFetched == null && name != null){
            // first try in PSI-MI ontology
            cvTermFetched = fetchCvTerm(name, CvTerm.PSI_MI);
            if (cvTermFetched != null){
                return cvTermFetched;
            }

            // then try in PSI-MOD ontology
            cvTermFetched = fetchCvTerm(name, CvTerm.PSI_MOD);
            if (cvTermFetched != null){
                return cvTermFetched;
            }

            // then try in PSI-PAR ontology
            cvTermFetched = fetchCvTerm(name, CvTerm.PSI_PAR);
            if (cvTermFetched != null){
                return cvTermFetched;
            }

            // then try in all ontologies
            cvTermFetched = fetchCvTerm(name, (String)null);
            if (cvTermFetched != null){
                return cvTermFetched;
            }
        }

        return cvTermFetched;
    }

    /** {@inheritDoc} */
    @Override
    protected void onEnrichedVersionNotFound(C cvTermToEnrich) throws EnricherException{
        if(getCvTermEnricherListener() != null)
            getCvTermEnricherListener().onEnrichmentComplete(cvTermToEnrich, EnrichmentStatus.FAILED, "The cvTerm does not exist.");
    }

    /** {@inheritDoc} */
    @Override
    public void enrich(C cvTermToEnrich, C cvTermFetched) throws EnricherException {
        processCvTerm(cvTermToEnrich, cvTermFetched);
        if(listener != null) listener.onEnrichmentComplete(cvTermToEnrich, EnrichmentStatus.SUCCESS, "CvTerm enriched successfully.");
    }

    private C fetchCvTerm(String id, CvTerm db) throws EnricherException {
        try {
            return getCvTermFetcher().fetchByIdentifier(id, db);
        } catch (BridgeFailedException e) {
            int index = 1;
            while(index < retryCount){
                try {
                    return getCvTermFetcher().fetchByIdentifier(id, db);
                } catch (BridgeFailedException ee) {
                    ee.printStackTrace();
                }
                index++;
            }
            throw new EnricherException("Re-tried "+ retryCount +" times to fetch the CvTerm but cannot connect to the fetcher.", e);
        }
    }

    private C fetchCvTerm(String name, String db) throws EnricherException {
        try {
            return getCvTermFetcher().fetchByName(name, db);
        } catch (BridgeFailedException e) {
            int index = 1;
            while(index < retryCount){
                try {
                    return getCvTermFetcher().fetchByName(name, db);
                } catch (BridgeFailedException ee) {
                    ee.printStackTrace();
                }
                index++;
            }
            throw new EnricherException("Re-tried "+ retryCount +" times to fetch the CvTerm but cannot connect to the fetcher.", e);
        }
    }
}
