package psidev.psi.mi.jami.xml.io.writer.expanded;

import psidev.psi.mi.jami.model.ComplexType;
import psidev.psi.mi.jami.model.InteractionCategory;
import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.model.Source;
import psidev.psi.mi.jami.xml.PsiXmlType;
import psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Expanded PSI-XML writer for named abstract interactions (no experimental evidences).
 * Participants, features, experiments also have expanded names
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>19/11/13</pre>
 */
public class ExpandedXmlNamedModelledWriter extends AbstractExpandedXmlWriter<ModelledInteraction> {

    /**
     * <p>Constructor for ExpandedXmlNamedModelledWriter.</p>
     */
    public ExpandedXmlNamedModelledWriter() {
        super(ModelledInteraction.class);
    }

    /**
     * <p>Constructor for ExpandedXmlNamedModelledWriter.</p>
     *
     * @param file a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    public ExpandedXmlNamedModelledWriter(File file) throws IOException, XMLStreamException {
        super(ModelledInteraction.class, file);
    }

    /**
     * <p>Constructor for ExpandedXmlNamedModelledWriter.</p>
     *
     * @param output a {@link java.io.OutputStream} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    public ExpandedXmlNamedModelledWriter(OutputStream output) throws XMLStreamException {
        super(ModelledInteraction.class, output);
    }

    /**
     * <p>Constructor for ExpandedXmlNamedModelledWriter.</p>
     *
     * @param writer a {@link java.io.Writer} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    public ExpandedXmlNamedModelledWriter(Writer writer) throws XMLStreamException {
        super(ModelledInteraction.class, writer);
    }

    /**
     * <p>Constructor for ExpandedXmlNamedModelledWriter.</p>
     *
     * @param streamWriter a {@link javax.xml.stream.XMLStreamWriter} object.
     * @param cache a {@link psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache} object.
     */
    public ExpandedXmlNamedModelledWriter(XMLStreamWriter streamWriter, PsiXmlObjectCache cache) {
        super(ModelledInteraction.class, streamWriter, cache);
    }


    /** {@inheritDoc} */
    @Override
    protected Source extractSourceFromInteraction() {
        return getCurrentInteraction().getSource() != null ? getCurrentInteraction().getSource() : super.extractSourceFromInteraction();
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseSubWriters() {
        super.initialiseSubWriters(false, true, PsiXmlType.expanded, InteractionCategory.modelled, ComplexType.n_ary);
    }
}
