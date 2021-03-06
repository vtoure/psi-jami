package psidev.psi.mi.jami.enricher.impl.minimal;

import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.bridges.fetcher.BioactiveEntityFetcher;
import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.impl.AbstractInteractorEnricher;
import psidev.psi.mi.jami.enricher.listener.EnrichmentStatus;
import psidev.psi.mi.jami.model.BioactiveEntity;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.utils.CvTermUtils;

import java.util.Collection;

/**
 * A basic minimal enricher for bioactive entities.
 *
 * See description of minimal enrichment in AbstractInteractorEnricher.
 *
 * The bioactive entities fetcher is required for enriching bioactive entities.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 07/08/13

 */
public class MinimalBioactiveEntityEnricher extends AbstractInteractorEnricher<BioactiveEntity> {

    /**
     * The only constructor, fulfilling the requirement of a bioactiveEntity fetcher.
     * If the bioactiveEntity fetcher is null, an illegal state exception will be thrown at the next enrichment.
     *
     * @param fetcher   The fetcher used to collect bioactiveEntity records.
     */
    public MinimalBioactiveEntityEnricher(BioactiveEntityFetcher fetcher){
        super(fetcher);
        if (fetcher == null){
            throw new IllegalArgumentException("The bioactive enricher needs a non null fetcher");
        }
    }

    /**
     * Returns the current fetcher which is being used to collect information about entities for enrichment.
     *
     * @return  The current fetcher.
     */
    public BioactiveEntityFetcher getInteractorFetcher() {
        return (BioactiveEntityFetcher)super.getInteractorFetcher();
    }

    /** {@inheritDoc} */
    @Override
    public BioactiveEntity find(BioactiveEntity objectToEnrich) throws EnricherException {
        BioactiveEntity fetchedBioactiveEntity = null;

        if(objectToEnrich.getChebi() != null){
            fetchedBioactiveEntity = fetchEntity(objectToEnrich.getChebi());
        }
        return fetchedBioactiveEntity;
    }

    /** {@inheritDoc} */
    @Override
    protected void onEnrichedVersionNotFound(BioactiveEntity objectToEnrich) throws EnricherException{
        getListener().onEnrichmentComplete(
                objectToEnrich, EnrichmentStatus.FAILED,
                "Could not fetch a bioactive entity with the provided CHEBI identifier.");
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isFullEnrichment() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    protected void onCompletedEnrichment(BioactiveEntity objectToEnrich) {
        if(getListener() != null)
            getListener().onEnrichmentComplete(
                    objectToEnrich , EnrichmentStatus.SUCCESS , "The bioactive entity has been successfully enriched.");
    }

    /** {@inheritDoc} */
    @Override
    protected void onInteractorCheckFailure(BioactiveEntity objectToEnrich, BioactiveEntity fetchedObject) throws EnricherException{
        if(getListener() != null)
            getListener().onEnrichmentComplete(
                    objectToEnrich , EnrichmentStatus.FAILED , "Cannot enrich the bioactive entity because the interactor type is not a bioactive entity type.");
    }

    /** {@inheritDoc} */
    @Override
    protected boolean canEnrichInteractor(BioactiveEntity entityToEnrich, BioactiveEntity fetchedEntity) throws EnricherException{
        if (fetchedEntity == null){
            onEnrichedVersionNotFound(entityToEnrich);
            return false;
        }
        // if the interactor type is not a valid bioactive entity interactor type, we cannot enrich
        if (entityToEnrich.getInteractorType() != null &&
                !CvTermUtils.isCvTerm(entityToEnrich.getInteractorType(), BioactiveEntity.POLYSACCHARIDE_MI, BioactiveEntity.POLYSACCHARIDE)
                && !CvTermUtils.isCvTerm(entityToEnrich.getInteractorType(), BioactiveEntity.BIOACTIVE_ENTITY_MI, BioactiveEntity.BIOACTIVE_ENTITY)
                && !CvTermUtils.isCvTerm(entityToEnrich.getInteractorType(), BioactiveEntity.SMALL_MOLECULE_MI, BioactiveEntity.SMALL_MOLECULE)
                && !CvTermUtils.isCvTerm(entityToEnrich.getInteractorType(), Interactor.UNKNOWN_INTERACTOR_MI, Interactor.UNKNOWN_INTERACTOR)){
            return false;
        }
        return true;
    }

    private BioactiveEntity fetchEntity(String id) throws EnricherException {
        try {
            Collection<BioactiveEntity> entities = getInteractorFetcher().fetchByIdentifier(id);
            return entities != null && !entities.isEmpty() ? entities.iterator().next() : null;
        } catch (BridgeFailedException e) {
            int index = 1;
            while(index < getRetryCount()){
                try {
                    Collection<BioactiveEntity> entities = getInteractorFetcher().fetchByIdentifier(id);
                    return !entities.isEmpty() ? entities.iterator().next() : null;
                } catch (BridgeFailedException ee) {
                    ee.printStackTrace();
                }
                index++;
            }
            throw new EnricherException("Re-tried "+ getRetryCount() +" times to fetch the Bioactive entity but cannot connect to the fetcher.", e);
        }
    }
}
