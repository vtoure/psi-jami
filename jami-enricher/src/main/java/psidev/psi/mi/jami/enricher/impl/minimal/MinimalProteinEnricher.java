package psidev.psi.mi.jami.enricher.impl.minimal;

import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.bridges.fetcher.ProteinFetcher;
import psidev.psi.mi.jami.bridges.mapper.ProteinMapper;
import psidev.psi.mi.jami.enricher.ProteinEnricher;
import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.impl.AbstractInteractorEnricher;
import psidev.psi.mi.jami.enricher.listener.EnrichmentStatus;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.AnnotationUtils;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import psidev.psi.mi.jami.utils.comparator.organism.OrganismTaxIdComparator;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enriches a protein to the minimum level. As an enricher, no data will be overwritten in the protein being enriched.
 * See description of minimal enrichment in AbstractInteractorEnricher
 * If the protein remapper is not null and the enricher cannot find a uniprot identifier, it will remap to uniprot using the proteinMapper.
 * In case of dead uniprot entries, it will move the dead uniprot identifier to the xrefs and add a caution in the annotations.
 * If the remapping is successfull, it will remove any cautions left because of dead entry and set the uniprot identifier of the protein
 *
 * The protein fetcher is require to enrich proteins
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 14/05/13

 */
public class MinimalProteinEnricher extends AbstractInteractorEnricher<Protein> implements ProteinEnricher {

    private ProteinMapper proteinMapper = null;
    private static final Logger log = Logger.getLogger(MinimalProteinEnricher.class.getName());

    /** Constant <code>CAUTION_MESSAGE="This sequence has been withdrawn from U"{trunked}</code> */
    public static final String CAUTION_MESSAGE = "This sequence has been withdrawn from Uniprot.";
    /**
     * The only constructor, fulfilling the requirement of a protein fetcher.
     * If the protein fetcher is null, an illegal state exception will be thrown at the next enrichment.
     *
     * @param fetcher   The fetcher used to collect protein records.
     */
    public MinimalProteinEnricher(ProteinFetcher fetcher){
        super(fetcher);
        if (fetcher == null){
            throw new IllegalArgumentException("The protein enricher needs a non null fetcher");
        }
    }

    /**
     * The fetcher to be used for used to collect data.
     *
     * @return  The fetcher which is currently being used for fetching.
     */
    public ProteinFetcher getInteractorFetcher() {
        return (ProteinFetcher) super.getInteractorFetcher();
    }

    /**
     * {@inheritDoc}
     *
     * The protein mapper to be used when a protein doesn't have a uniprot id or the uniprotID is dead.
     */
    public void setProteinMapper(ProteinMapper proteinMapper){
        this.proteinMapper = proteinMapper;
    }
    /**
     * The protein remapper has no default and can be left null
     *
     * @return  The current remapper.
     */
    public ProteinMapper getProteinMapper(){
        return proteinMapper;
    }

    /** {@inheritDoc} */
    @Override
    public Protein find(Protein proteinToEnrich) throws EnricherException {
        // If there is no uniprotID - try and remap.
        if(proteinToEnrich.getUniprotkb() == null) {
            if( log.isLoggable(Level.FINEST)) {
                log.finest("Remapping protein without uniprotkb");
            }
            if(!remapProtein(proteinToEnrich)){
                return null;
            }
        }

        // fetch proteins
        Collection<Protein> proteinsEnriched = fetchProteins(proteinToEnrich.getUniprotkb());
        // If the Protein is dead
        if(proteinsEnriched.isEmpty()){
            proteinsEnriched = processAndRemapDeadProtein(proteinToEnrich);
            if(proteinsEnriched.isEmpty()){
                return null;
            }
        }

        // 1 protein, use it
        if(proteinsEnriched.size() == 1) {
            return proteinsEnriched.iterator().next();
        }
        else{
            // Examples:
            // - one single entry : P12345
            // - uniprot demerge (different uniprot entries with different organisms) : P77681
            // - uniprot demerge (different uniprot entries with same organisms) : P11163
            // In your enricher, if you have several entries,
            // try to look for the one with the same organism as the protein you try to enrich.
            // If you don't find one or several entries have the same organism,
            // fire a specific event because we want to track these proteins.

            // Many proteins, try and choose
            if(proteinToEnrich.getOrganism() == null
                    || proteinToEnrich.getOrganism().getTaxId() == -3){
                if(getListener() != null)
                    getListener().onEnrichmentError(proteinToEnrich,  "The protein ["+ proteinToEnrich.getUniprotkb() +"] does not have a valid organism and could match "+proteinsEnriched.size()+" uniprot entries.", new EnricherException("Cannot enrich a demerged protein"));
                return null;
            }

            Protein proteinFetched = null;

            for(Protein protein : proteinsEnriched){
                if(OrganismTaxIdComparator.areEquals(protein.getOrganism(), proteinToEnrich.getOrganism())){
                    if(proteinFetched == null) proteinFetched = protein;
                    else{
                        // Multiple proteins share this organism - impossible to choose
                        if(getListener() != null)
                            getListener().onEnrichmentError(proteinToEnrich,  "The protein ["+ proteinToEnrich.getUniprotkb() +"] could match "+proteinsEnriched.size()+" uniprot entries and several have the same organism taxid.", new EnricherException("Cannot enrich a demerged protein"));
                        return null;
                    }
                }
            }

            if(proteinFetched == null){
                // No proteins share this organism - impossible to choose
                if(getListener() != null)
                    getListener().onEnrichmentError(proteinToEnrich,  "The protein ["+ proteinToEnrich.getUniprotkb() +"] could match "+proteinsEnriched.size()+" uniprot entries and non of them have the same organism taxid.", new EnricherException("Cannot enrich a demerged protein"));
                return null;
            }

            return proteinFetched;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void onEnrichedVersionNotFound(Protein objectToEnrich) throws EnricherException{
        if (getListener() != null){
            getListener().onEnrichmentComplete(
                    objectToEnrich , EnrichmentStatus.FAILED ,
                    "Could not fetch a protein ["+ objectToEnrich.getUniprotkb() +"] with the provided identifier/sequence.");
        }
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isFullEnrichment() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    protected void onCompletedEnrichment(Protein objectToEnrich) {
        if(getListener() != null)
            getListener().onEnrichmentComplete(
                    objectToEnrich , EnrichmentStatus.SUCCESS , "The protein ["+ objectToEnrich.getUniprotkb() +"] has been successfully enriched.");
    }

    /** {@inheritDoc} */
    @Override
    protected void onInteractorCheckFailure(Protein objectToEnrich, Protein fetchedObject) throws EnricherException{
        if(getListener() != null)
            getListener().onEnrichmentComplete(
                    objectToEnrich , EnrichmentStatus.FAILED , "Cannot enrich the protein ["+ objectToEnrich.getUniprotkb() +"] because the interactor type is not a protein/peptide type and/or there is a conflict with the organism.");
    }

    /** {@inheritDoc} */
    @Override
    protected boolean canEnrichInteractor(Protein entityToEnrich, Protein fetchedEntity) throws EnricherException{
        if (fetchedEntity == null){
            onEnrichedVersionNotFound(entityToEnrich);
            return false;
        }
        // if the interactor type is not a valid protein interactor type, we cannot enrich
        if (entityToEnrich.getInteractorType() != null &&
                !CvTermUtils.isCvTerm(entityToEnrich.getInteractorType(), Protein.PROTEIN_MI, Protein.PROTEIN)
                && !CvTermUtils.isCvTerm(entityToEnrich.getInteractorType(), Protein.PEPTIDE_MI, Protein.PEPTIDE)
                && !CvTermUtils.isCvTerm(entityToEnrich.getInteractorType(), Polymer.POLYMER_MI, Polymer.POLYMER)
                && !CvTermUtils.isCvTerm(entityToEnrich.getInteractorType(), Interactor.UNKNOWN_INTERACTOR_MI, Interactor.UNKNOWN_INTERACTOR)){
            return false;
        }

        // if the organism is different, we cannot enrich
        if (entityToEnrich.getOrganism() != null && fetchedEntity.getOrganism() != null &&
                !OrganismTaxIdComparator.areEquals(entityToEnrich.getOrganism(), fetchedEntity.getOrganism())){
            return false;
        }

        return true;
    }

    /**
     * <p>processDeadUniprotIdentity.</p>
     *
     * @param proteinToEnrich a {@link psidev.psi.mi.jami.model.Protein} object.
     * @param uniprotIdentity a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    protected void processDeadUniprotIdentity(Protein proteinToEnrich, Xref uniprotIdentity) {
        proteinToEnrich.getIdentifiers().remove(uniprotIdentity);
        if(getListener() != null){
            getListener().onRemovedIdentifier(proteinToEnrich, uniprotIdentity);
        }
        proteinToEnrich.getXrefs().add(uniprotIdentity);
        if(getListener() != null){
            getListener().onAddedXref(proteinToEnrich, uniprotIdentity);
        }
    }

    /**
     * Attempts to remap the protein using the provided proteinRemapper.
     * If one has not been included, this method returns false and reports a failure to the listener.
     * If after remapping, the protein still has no entry, this entry returns false.
     *
     * @param proteinToEnrich   The protein to find a remapping for.
     * @return                  Whether the remapping was successful.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected boolean remapProtein(Protein proteinToEnrich) throws EnricherException {

        // If there is no protein remapper, the enrichment fails
        if( getProteinMapper() == null ){
            return false;
        }

        // Attempt the remapping
        try {
            getProteinMapper().map(proteinToEnrich);
        } catch (BridgeFailedException e) {
            throw new EnricherException("Impossible to remap the protein to uniprot.", e);
        }

        if (proteinToEnrich.getUniprotkb() != null){
            Collection<Annotation> cautions = AnnotationUtils.collectAllAnnotationsHavingTopic(proteinToEnrich.getAnnotations(), Annotation.CAUTION_MI, Annotation.CAUTION);
            for (Annotation caution : cautions){
                if (caution.getValue() != null && CAUTION_MESSAGE.equalsIgnoreCase(caution.getValue())){
                    proteinToEnrich.getAnnotations().remove(caution);
                    if(getListener() != null){
                        getListener().onRemovedAnnotation(proteinToEnrich, caution);
                    }
                }
            }
            return true;
        }
        return false;
    }

    private Collection<Protein> processAndRemapDeadProtein(Protein proteinToEnrich) throws EnricherException {
        Collection<Protein> fetchedProteins = Collections.EMPTY_LIST;

        // move old uniprot identity to xrefs
        Collection<Xref> uniprotIdentities = XrefUtils.collectAllXrefsHavingDatabase(proteinToEnrich.getIdentifiers(), Xref.UNIPROTKB_MI, Xref.UNIPROTKB);
        for (Xref uniprotIdentity : uniprotIdentities){
            // we have the dead entry
            if (uniprotIdentity.getId().equals(proteinToEnrich.getUniprotkb())){
                processDeadUniprotIdentity(proteinToEnrich, uniprotIdentity);
            }
            else if(fetchedProteins.isEmpty()){
                fetchedProteins = fetchProteins(uniprotIdentity.getId());
                if (fetchedProteins.isEmpty()){
                    processDeadUniprotIdentity(proteinToEnrich, uniprotIdentity);
                }
            }
        }

        // remap successfull
        if (fetchedProteins.isEmpty() && remapProtein(proteinToEnrich)){
            fetchedProteins = fetchProteins(proteinToEnrich.getUniprotkb());
        }
        // remap unsuccessful, add caution
        else if (fetchedProteins.isEmpty()){
            boolean hasCaution = false;
            for (Annotation annot : proteinToEnrich.getAnnotations()){
                if (AnnotationUtils.doesAnnotationHaveTopic(annot, Annotation.CAUTION_MI, Annotation.CAUTION)){
                    if (annot.getValue() != null && CAUTION_MESSAGE.equalsIgnoreCase(annot.getValue())){
                        hasCaution = true;
                        break;
                    }
                }
            }
            if (!hasCaution){
                Annotation annot = AnnotationUtils.createCaution(CAUTION_MESSAGE);
                proteinToEnrich.getAnnotations().add(annot);
                if(getListener() != null){
                    getListener().onAddedAnnotation(proteinToEnrich, annot);
                }
            }

        }
        return fetchedProteins;
    }

    private Collection<Protein> fetchProteins(String uniprotkb) throws EnricherException {
        try {
            return getInteractorFetcher().fetchByIdentifier(uniprotkb);
        } catch (BridgeFailedException e) {
            int index = 0;
            while(index < getRetryCount()){
                try {
                    return getInteractorFetcher().fetchByIdentifier(uniprotkb);
                } catch (BridgeFailedException ee) {
                    ee.printStackTrace();
                }
                index++;
            }
            throw new EnricherException("Retried "+getRetryCount()+" times", e);
        }
    }
}
