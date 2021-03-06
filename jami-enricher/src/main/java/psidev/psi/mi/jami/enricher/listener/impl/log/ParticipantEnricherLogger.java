package psidev.psi.mi.jami.enricher.listener.impl.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import psidev.psi.mi.jami.enricher.listener.EnrichmentStatus;
import psidev.psi.mi.jami.enricher.listener.ParticipantEnricherListener;
import psidev.psi.mi.jami.listener.impl.ParticipantChangeLogger;
import psidev.psi.mi.jami.model.Participant;

/**
 * A logging listener. It will display a message when each event if fired.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 22/07/13

 */
public class ParticipantEnricherLogger<P extends Participant>
        extends ParticipantChangeLogger<P> implements ParticipantEnricherListener<P> {

    private static final Logger log = LoggerFactory.getLogger(ParticipantEnricherLogger.class.getName());

    /** {@inheritDoc} */
    public void onEnrichmentComplete(P participant, EnrichmentStatus status, String message) {
        log.info(participant.toString()+" enrichment complete with status ["+status+"], message: "+message);
    }

    /** {@inheritDoc} */
    public void onEnrichmentError(P object, String message, Exception e) {
        log.error(object.toString()+" enrichment error, message: "+message, e);
    }
}
