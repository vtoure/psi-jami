package psidev.psi.mi.jami.enricher.impl.interaction;

import psidev.psi.mi.jami.enricher.CvTermEnricher;
import psidev.psi.mi.jami.enricher.impl.cvterm.MaximumCvTermEnricher;
import psidev.psi.mi.jami.model.Feature;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Participant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 09/07/13
 */
public class MaximumInteractionEnricher <I extends Interaction, P extends Participant, F extends Feature>
    extends MinimumInteractionEnricher <I , P , F>{


    public CvTermEnricher getCvTermEnricher(){
        if(cvTermEnricher == null) cvTermEnricher = new MaximumCvTermEnricher();
        return cvTermEnricher;
    }
}
