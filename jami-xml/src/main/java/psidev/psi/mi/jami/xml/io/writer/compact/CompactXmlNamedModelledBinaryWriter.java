package psidev.psi.mi.jami.xml.io.writer.compact;

import psidev.psi.mi.jami.binary.ModelledBinaryInteraction;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.xml.PsiXmlType;
import psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Compact PSI-XML writer for named modelled binary interactions (no experimental evidences).
 * Participants, features, experiments also have expanded names
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>19/11/13</pre>
 */
public class CompactXmlNamedModelledBinaryWriter extends AbstractCompactXmlWriter<ModelledBinaryInteraction> {

    /**
     * <p>Constructor for CompactXmlNamedModelledBinaryWriter.</p>
     */
    public CompactXmlNamedModelledBinaryWriter() {
        super(ModelledBinaryInteraction.class);
    }

    /**
     * <p>Constructor for CompactXmlNamedModelledBinaryWriter.</p>
     *
     * @param file a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    public CompactXmlNamedModelledBinaryWriter(File file) throws IOException, XMLStreamException {
        super(ModelledBinaryInteraction.class, file);
    }

    /**
     * <p>Constructor for CompactXmlNamedModelledBinaryWriter.</p>
     *
     * @param output a {@link java.io.OutputStream} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    public CompactXmlNamedModelledBinaryWriter(OutputStream output) throws XMLStreamException {
        super(ModelledBinaryInteraction.class, output);
    }

    /**
     * <p>Constructor for CompactXmlNamedModelledBinaryWriter.</p>
     *
     * @param writer a {@link java.io.Writer} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    public CompactXmlNamedModelledBinaryWriter(Writer writer) throws XMLStreamException {
        super(ModelledBinaryInteraction.class, writer);
    }

    /**
     * <p>Constructor for CompactXmlNamedModelledBinaryWriter.</p>
     *
     * @param streamWriter a {@link javax.xml.stream.XMLStreamWriter} object.
     * @param cache a {@link psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache} object.
     */
    public CompactXmlNamedModelledBinaryWriter(XMLStreamWriter streamWriter, PsiXmlObjectCache cache) {
        super(ModelledBinaryInteraction.class, streamWriter, cache);
    }

    /** {@inheritDoc} */
    @Override
    protected void registerAvailabilities(ModelledBinaryInteraction interaction) {
        // nothing to do
    }

    /** {@inheritDoc} */
    @Override
    protected void registerInteractionProperties() {
        super.registerInteractionProperties();
        for (CooperativeEffect effect : getCurrentInteraction().getCooperativeEffects()){
            for (ModelledInteraction interaction : effect.getAffectedInteractions()){
                registerAllInteractorsAndExperimentsFrom(interaction);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void registerExperiment(ModelledBinaryInteraction interaction) {
        Experiment exp = getInteractionWriter().extractDefaultExperimentFrom(interaction);
        if (exp != null){
            getExperiments().add(exp);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected Source extractSourceFromInteraction() {
        return getCurrentInteraction().getSource();
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseSubWriters() {
        super.initialiseSubWriters(false, true, PsiXmlType.compact, InteractionCategory.modelled, ComplexType.binary);
        // initialise same default experiment
        getComplexWriter().setDefaultExperiment(getInteractionWriter().getDefaultExperiment());
    }
}
