package psidev.psi.mi.jami.listener.impl;

import psidev.psi.mi.jami.listener.ParticipantEvidencePoolChangeListener;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.model.ParticipantEvidencePool;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This listener will just entity pool change events
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>11/06/13</pre>
 */

public class ParticipantEvidencePoolChangeLogger extends ParticipantEvidenceChangeLogger<ParticipantEvidencePool> implements ParticipantEvidencePoolChangeListener {

    private static final Logger entityPoolChangeLogger = Logger.getLogger("ParticipantPoolChangeLogger");

    public void onTypeUpdate(ParticipantEvidencePool entity, CvTerm oldTerm) {
        entityPoolChangeLogger.log(Level.INFO, "The pool type " + oldTerm + " has been updated with " + entity.getType() + " in the experimental entity pool " + entity.toString());
    }

    public void onAddedEntity(ParticipantEvidencePool entity, Participant added) {
        entityPoolChangeLogger.log(Level.INFO, "The entity " + added.toString() + " has been added to the experimental entity pool " + entity.toString());
    }

    public void onRemovedEntity(ParticipantEvidencePool entity, Participant removed) {
        entityPoolChangeLogger.log(Level.INFO, "The entity " + removed.toString() + " has been removed from the experimental entity pool " + entity.toString());
    }
}