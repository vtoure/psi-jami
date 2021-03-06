package psidev.psi.mi.jami.enricher.impl.full;

import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.util.EnricherUtils;
import psidev.psi.mi.jami.listener.AliasesChangeListener;
import psidev.psi.mi.jami.listener.AnnotationsChangeListener;
import psidev.psi.mi.jami.listener.ParticipantChangeListener;
import psidev.psi.mi.jami.listener.XrefsChangeListener;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Feature;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.utils.comparator.cv.DefaultCvTermComparator;
import psidev.psi.mi.jami.utils.comparator.interactor.DefaultExactInteractorComparator;

/**
 * The participant enricher is an enricher which can enrich either single participant or a collection.
 * The participant enricher has subEnrichers and no fetchers.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 19/06/13

 */
public class FullParticipantUpdater<P extends Participant, F extends Feature>
        extends FullParticipantEnricher<P,F>  {

    /** {@inheritDoc} */
    @Override
    protected void processCausalRelationships(P objectToEnrich, P objectSource) throws EnricherException {
        EnricherUtils.mergeCausalRelationships(objectToEnrich, objectToEnrich.getCausalRelationships(), objectSource.getCausalRelationships(), true, getParticipantEnricherListener());
    }

    /** {@inheritDoc} */
    @Override
    protected void processXrefs(P objectToEnrich, P objectSource) throws EnricherException{
        EnricherUtils.mergeXrefs(objectToEnrich, objectToEnrich.getXrefs(), objectSource.getXrefs(), true, false,
                getParticipantEnricherListener() instanceof XrefsChangeListener ? (XrefsChangeListener)getParticipantEnricherListener():null,
                null);
    }

    /** {@inheritDoc} */
    @Override
    protected void processAnnotations(P objectToEnrich, P objectSource) throws EnricherException{
        EnricherUtils.mergeAnnotations(objectToEnrich, objectToEnrich.getAnnotations(), objectSource.getAnnotations(),
                true,
                getParticipantEnricherListener() instanceof AnnotationsChangeListener ? (AnnotationsChangeListener)getParticipantEnricherListener():null);
    }

    /** {@inheritDoc} */
    @Override
    public void processInteractor(P objectToEnrich, P objectSource) throws EnricherException {
        if (!DefaultExactInteractorComparator.areEquals(objectToEnrich.getInteractor(), objectSource.getInteractor())){
            Interactor old = objectToEnrich.getInteractor();
            objectToEnrich.setInteractor(objectSource.getInteractor());
            if (getParticipantEnricherListener() != null){
                getParticipantEnricherListener().onInteractorUpdate(objectToEnrich, old);
            }
        }
        else if (getInteractorEnricher() != null
                && objectToEnrich.getInteractor() != objectSource.getInteractor()){
            getInteractorEnricher().enrich(objectToEnrich.getInteractor(), objectSource.getInteractor());
        }
        // nothing to do here
        processInteractor(objectToEnrich);
    }

    /** {@inheritDoc} */
    @Override
    public void processFeatures(P objectToEnrich, P objectSource) throws EnricherException {
        EnricherUtils.mergeFeatures(objectToEnrich, objectToEnrich.getFeatures(), objectSource.getFeatures(), true, getParticipantEnricherListener(),
                getFeatureEnricher());
        processFeatures(objectToEnrich);
    }

    /** {@inheritDoc} */
    @Override
    public void processBiologicalRole(P objectToEnrich, P objectSource) throws EnricherException {
        if (!DefaultCvTermComparator.areEquals(objectToEnrich.getBiologicalRole(), objectSource.getBiologicalRole())){
            CvTerm old = objectToEnrich.getBiologicalRole();
            objectToEnrich.setBiologicalRole(objectSource.getBiologicalRole());
            if (getParticipantEnricherListener() instanceof ParticipantChangeListener){
                ((ParticipantChangeListener)getParticipantEnricherListener()).onBiologicalRoleUpdate(objectToEnrich, old);
            }
        }
        else if (getCvTermEnricher() != null
                && objectToEnrich.getBiologicalRole() != objectSource.getBiologicalRole()){
            getCvTermEnricher().enrich(objectToEnrich.getBiologicalRole(), objectSource.getBiologicalRole());
        }
        // nothing to do here
        processBiologicalRole(objectToEnrich);
    }

    /** {@inheritDoc} */
    @Override
    protected void processAliases(P objectToEnrich, P objectSource) throws EnricherException{
        EnricherUtils.mergeAliases(objectToEnrich, objectToEnrich.getAliases(), objectSource.getAliases(), true,
                getParticipantEnricherListener() instanceof AliasesChangeListener ? (AliasesChangeListener)getParticipantEnricherListener() : null);
    }
}
