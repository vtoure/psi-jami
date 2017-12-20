package psidev.psi.mi.jami.enricher.listener.impl.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import psidev.psi.mi.jami.enricher.listener.EnrichmentStatus;
import psidev.psi.mi.jami.enricher.listener.ModelledInteractionEnricherListener;
import psidev.psi.mi.jami.listener.impl.ModelledInteractionChangeLogger;
import psidev.psi.mi.jami.model.ModelledInteraction;

/**
 * A logging listener. It will display a message when each event if fired.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 22/07/13

 */
public class ModelledInteractionEnricherLogger<I extends ModelledInteraction>
        extends ModelledInteractionChangeLogger<I> implements ModelledInteractionEnricherListener<I> {

    private static final Logger log = LoggerFactory.getLogger(ModelledInteractionEnricherLogger.class.getName());

    /** {@inheritDoc} */
    public void onEnrichmentComplete(I interaction, EnrichmentStatus status, String message) {
        log.info(interaction.toString()+" enrichment complete with status ["+status+"], message: "+message);
    }

    /** {@inheritDoc} */
    public void onEnrichmentError(I object, String message, Exception e) {
        log.error(object.toString() + " enrichment error, message: " + message, e);
    }
}
