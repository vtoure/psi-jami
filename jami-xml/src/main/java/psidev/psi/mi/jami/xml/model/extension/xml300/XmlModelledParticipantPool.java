package psidev.psi.mi.jami.xml.model.extension.xml300;

import psidev.psi.mi.jami.model.ModelledFeature;
import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.model.ModelledParticipantCandidate;
import psidev.psi.mi.jami.model.ModelledParticipantPool;
import psidev.psi.mi.jami.xml.XmlEntryContext;
import psidev.psi.mi.jami.xml.model.extension.AbstractXmlParticipant;
import psidev.psi.mi.jami.xml.model.extension.XmlModelledParticipant;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Xml implementation of participant pool
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>02/09/14</pre>
 */
@XmlTransient
public class XmlModelledParticipantPool extends AbstractXmlParticipantPool<ModelledInteraction, ModelledFeature, ModelledParticipantCandidate>
implements ModelledParticipantPool{

    /**
     * <p>Constructor for XmlModelledParticipantPool.</p>
     */
    public XmlModelledParticipantPool() {
        super();
    }

    /**
     * <p>Constructor for XmlModelledParticipantPool.</p>
     *
     * @param delegate a {@link psidev.psi.mi.jami.xml.model.extension.AbstractXmlParticipant} object.
     */
    public XmlModelledParticipantPool(AbstractXmlParticipant<ModelledInteraction, ModelledFeature> delegate) {
        super(delegate);
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseDefaultDelegate() {
        super.setDelegate(new XmlModelledParticipant());
    }

    /** {@inheritDoc} */
    @Override
    public void setId(int id) {
        super.setId(id);
        // register participant as complex participant
        XmlEntryContext.getInstance().registerComplexParticipant(id, this);
    }
}
