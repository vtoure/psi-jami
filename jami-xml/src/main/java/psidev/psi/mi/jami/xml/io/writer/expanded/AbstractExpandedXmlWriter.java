package psidev.psi.mi.jami.xml.io.writer.expanded;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.xml.cache.InMemoryLightIdentityObjectCache;
import psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache;
import psidev.psi.mi.jami.xml.io.writer.AbstractXmlWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlElementWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlExperimentWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Abstract class for expanded PSI-xml 2.5 writers
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>18/11/13</pre>
 */
public abstract class AbstractExpandedXmlWriter<T extends Interaction> extends AbstractXmlWriter<T> {

    private Class<T> type;

    /**
     * <p>Constructor for AbstractExpandedXmlWriter.</p>
     *
     * @param type a {@link java.lang.Class} object.
     */
    public AbstractExpandedXmlWriter(Class<T> type) {
        super();
        this.type = type;
    }

    /**
     * <p>Constructor for AbstractExpandedXmlWriter.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param file a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    public AbstractExpandedXmlWriter(Class<T> type, File file) throws IOException, XMLStreamException {
        super(file);
        this.type = type;
    }

    /**
     * <p>Constructor for AbstractExpandedXmlWriter.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param output a {@link java.io.OutputStream} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    public AbstractExpandedXmlWriter(Class<T> type, OutputStream output) throws XMLStreamException {
        super(output);
        this.type = type;
    }

    /**
     * <p>Constructor for AbstractExpandedXmlWriter.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param writer a {@link java.io.Writer} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    public AbstractExpandedXmlWriter(Class<T> type, Writer writer) throws XMLStreamException {
        super(writer);
        this.type = type;
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseOptionalWriters(PsiXmlExperimentWriter experimentWriter, PsiXmlElementWriter<String> availabilityWriter,
                                             PsiXmlElementWriter<Interactor> interactorWriter) {
        // no optional writers for experiments, interactors and availability
    }

    /**
     * <p>Constructor for AbstractExpandedXmlWriter.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param streamWriter a {@link javax.xml.stream.XMLStreamWriter} object.
     * @param elementCache a {@link psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache} object.
     */
    protected AbstractExpandedXmlWriter(Class<T> type, XMLStreamWriter streamWriter, PsiXmlObjectCache elementCache) {
        super(streamWriter, elementCache);
        this.type = type;
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseDefaultElementCache() {
        setElementCache(new InMemoryLightIdentityObjectCache());
    }

    /** {@inheritDoc} */
    @Override
    protected void writeStartEntryContent() throws XMLStreamException {
        // write start entry
        writeStartEntry();
        // write source
        writeSource();
        // write start interactionList
        writeStartInteractionList();
    }

    /**
     * <p>getInteractionType.</p>
     *
     * @return a {@link java.lang.Class} object.
     */
    protected Class<T> getInteractionType() {
        return type;
    }

    /**
     * <p>writeInteraction.</p>
     *
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    protected void writeInteraction() throws XMLStreamException {
        // write interaction
        super.writeInteraction();
        // remove experiments
        getElementCache().removeObject(getInteractionWriter().extractDefaultExperimentFrom(getCurrentInteraction()));
    }

    /** {@inheritDoc} */
    protected void writeComplex(ModelledInteraction modelled) {
        super.writeComplex(modelled);
        // remove experiments
        getElementCache().removeObject(getComplexWriter().extractDefaultExperimentFrom(modelled));
    }
}
