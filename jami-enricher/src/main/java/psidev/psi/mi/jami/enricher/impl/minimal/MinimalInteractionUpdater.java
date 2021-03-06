package psidev.psi.mi.jami.enricher.impl.minimal;

import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.util.EnricherUtils;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.utils.comparator.cv.DefaultCvTermComparator;

import java.util.Date;

/**
 * The enricher for Interactions which can enrich a single interaction or a collection.
 * The interaction enricher has subEnrichers for participants and cvTerms.
 * It has no fetcher.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 28/06/13

 */
public class MinimalInteractionUpdater<I extends Interaction>
        extends MinimalInteractionEnricher<I> {

    /**
     * <p>processCreatedDate.</p>
     *
     * @param objectToEnrich a I object.
     * @param objectSource a I object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processCreatedDate(I objectToEnrich, I objectSource) throws EnricherException{
        if ((objectSource.getCreatedDate() != null && !objectSource.getCreatedDate().equals(objectToEnrich.getCreatedDate()))
                || (objectSource.getCreatedDate() == null && objectToEnrich.getCreatedDate() != null)){
            Date oldDate = objectToEnrich.getCreatedDate();
            objectToEnrich.setCreatedDate(objectSource.getCreatedDate());
            if (getInteractionEnricherListener() != null){
                getInteractionEnricherListener().onCreatedDateUpdate(objectToEnrich, oldDate);
            }
        }
    }

    /**
     * <p>processUpdateDate.</p>
     *
     * @param objectToEnrich a I object.
     * @param objectSource a I object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processUpdateDate(I objectToEnrich, I objectSource) throws EnricherException{
        if ((objectSource.getUpdatedDate() != null && !objectSource.getUpdatedDate().equals(objectToEnrich.getUpdatedDate()))
                || (objectSource.getUpdatedDate() == null && objectToEnrich.getUpdatedDate() != null)){
            Date oldDate = objectToEnrich.getUpdatedDate();
            objectToEnrich.setUpdatedDate(objectSource.getUpdatedDate());
            if (getInteractionEnricherListener() != null){
                getInteractionEnricherListener().onUpdatedDateUpdate(objectToEnrich, oldDate);
            }
        }
    }

    /**
     * <p>processShortName.</p>
     *
     * @param objectToEnrich a I object.
     * @param objectSource a I object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processShortName(I objectToEnrich, I objectSource) throws EnricherException{
        if ((objectSource.getShortName() != null && !objectSource.getShortName().equals(objectToEnrich.getShortName()))
                || (objectSource.getShortName() == null && objectToEnrich.getShortName() != null)){
            String oldName = objectToEnrich.getShortName();
            objectToEnrich.setShortName(objectSource.getShortName());
            if (getInteractionEnricherListener() != null){
                getInteractionEnricherListener().onShortNameUpdate(objectToEnrich, oldName);
            }
        }
    }

    /**
     * <p>processOtherProperties.</p>
     *
     * @param objectToEnrich a I object.
     * @param objectSource a I object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processOtherProperties(I objectToEnrich, I objectSource) throws EnricherException{
        // do nothing
    }

    /**
     * <p>processIdentifiers.</p>
     *
     * @param objectToEnrich a I object.
     * @param objectSource a I object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processIdentifiers(I objectToEnrich, I objectSource) throws EnricherException{
        EnricherUtils.mergeXrefs(objectToEnrich, objectToEnrich.getIdentifiers(), objectSource.getIdentifiers(),true, true,
                getInteractionEnricherListener(), getInteractionEnricherListener());
    }

    /**
     * <p>processParticipants.</p>
     *
     * @param objectToEnrich a I object.
     * @param objectSource a I object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processParticipants(I objectToEnrich, I objectSource) throws EnricherException{
        EnricherUtils.mergeParticipants(objectToEnrich, objectToEnrich.getParticipants(), objectSource.getParticipants(),
                true, getInteractionEnricherListener(), getParticipantEnricher());

        processParticipants(objectToEnrich);
    }

    /**
     * <p>processInteractionType.</p>
     *
     * @param objectToEnrich a I object.
     * @param objectSource a I object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processInteractionType(I objectToEnrich, I objectSource) throws EnricherException {

        if (!DefaultCvTermComparator.areEquals(objectToEnrich.getInteractionType(), objectSource.getInteractionType())){
            CvTerm oldType = objectToEnrich.getInteractionType();
            objectToEnrich.setInteractionType(objectSource.getInteractionType());
            if (getInteractionEnricherListener() != null){
                getInteractionEnricherListener().onInteractionTypeUpdate(objectToEnrich, oldType);
            }
        }
        else if (getCvTermEnricher() != null
                && objectToEnrich.getInteractionType() != objectSource.getInteractionType()){
            getCvTermEnricher().enrich(objectToEnrich.getInteractionType(), objectSource.getInteractionType());
        }

        processInteractionType(objectToEnrich);
    }
}
