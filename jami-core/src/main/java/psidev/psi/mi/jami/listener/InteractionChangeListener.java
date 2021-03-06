package psidev.psi.mi.jami.listener;

import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Participant;

import java.util.Date;

/**
 * Listener for changes in interaction
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>01/10/13</pre>
 */
public interface InteractionChangeListener<I extends Interaction> extends AnnotationsChangeListener<I>, IdentifiersChangeListener<I>,
                       XrefsChangeListener<I>, ChecksumsChangeListener<I>{

    /**
     * <p>onShortNameUpdate</p>
     *
     * @param interaction : the updated interaction
     * @param oldName : old name
     */
    public void onShortNameUpdate(I interaction, String oldName);

    /**
     * <p>onUpdatedDateUpdate</p>
     *
     * @param interaction : the updated interaction
     * @param oldUpdate : old updated date
     */
    public void onUpdatedDateUpdate(I interaction, Date oldUpdate);

    /**
     * <p>onCreatedDateUpdate</p>
     *
     * @param interaction  : the updated interaction
     * @param oldCreated : old created date
     */
    public void onCreatedDateUpdate(I interaction, Date oldCreated);

    /**
     * <p>onInteractionTypeUpdate</p>
     *
     * @param interaction  : the updated interaction
     * @param oldType : old type
     */
    public void onInteractionTypeUpdate(I interaction, CvTerm oldType);

    /**
     * <p>onAddedParticipant</p>
     *
     * @param interaction  : the updated interaction
     * @param addedParticipant : added participant
     */
    public void onAddedParticipant(I interaction, Participant addedParticipant);

    /**
     * <p>onRemovedParticipant</p>
     *
     * @param interaction : the updated interaction
     * @param removedParticipant : removed participant
     */
    public void onRemovedParticipant(I interaction, Participant removedParticipant);
}
