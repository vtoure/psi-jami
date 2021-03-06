package psidev.psi.mi.jami.xml.io.writer.elements.impl.xml30;

import psidev.psi.mi.jami.exception.MIIOException;
import psidev.psi.mi.jami.model.CausalRelationship;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlCausalRelationshipWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlVariableNameWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.XmlOpenCvTermWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * XML 3.0 writer for causalRelationship
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>11/11/13</pre>
 */
public class XmlCausalRelationshipWriter implements PsiXmlCausalRelationshipWriter {
    private XMLStreamWriter streamWriter;
    private PsiXmlObjectCache objectIndex;

    private PsiXmlVariableNameWriter<CvTerm> causalStatementWriter;

    /**
     * <p>Constructor for XmlCausalRelationshipWriter.</p>
     *
     * @param writer a {@link javax.xml.stream.XMLStreamWriter} object.
     * @param objectIndex a {@link psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache} object.
     */
    public XmlCausalRelationshipWriter(XMLStreamWriter writer, PsiXmlObjectCache objectIndex){
        if (writer == null){
            throw new IllegalArgumentException("The XML stream writer is mandatory for the XmlVariableParameterWriter");
        }
        this.streamWriter = writer;
        if (objectIndex == null){
            throw new IllegalArgumentException("The PsiXml 2.5 object index is mandatory for the XmlVariableParameterWriter. It is necessary for generating " +
                    "an id to a variable parameter value");
        }
        this.objectIndex = objectIndex;
    }

    /**
     * <p>Getter for the field <code>causalStatementWriter</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlVariableNameWriter} object.
     */
    public PsiXmlVariableNameWriter<CvTerm> getCausalStatementWriter() {
        if (this.causalStatementWriter == null){
            initialiseCausalStatementWriter();
        }
        return causalStatementWriter;
    }

    /**
     * <p>initialiseCausalStatementWriter.</p>
     */
    protected void initialiseCausalStatementWriter() {
        this.causalStatementWriter = new XmlOpenCvTermWriter(this.streamWriter);
    }

    /**
     * <p>Setter for the field <code>causalStatementWriter</code>.</p>
     *
     * @param causalStatementWriter a {@link psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlVariableNameWriter} object.
     */
    public void setCausalStatementWriter(PsiXmlVariableNameWriter<CvTerm> causalStatementWriter) {
        this.causalStatementWriter = causalStatementWriter;
    }

    /** {@inheritDoc} */
    @Override
    public void write(CausalRelationship object, Participant source) throws MIIOException {
        if (object != null && source != null){
            try {
                // write start
                this.streamWriter.writeStartElement("causalRelationship");
                // write source
                writeSource(object, source);
                // write causal statement
                writeCausalStatement(object);
                // write target
                writeTarget(object);
                // write end causal relationship
                this.streamWriter.writeEndElement();

            } catch (XMLStreamException e) {
                throw new MIIOException("Impossible to write the variable parameter value : "+object.toString(), e);
            }
        }
    }

    /**
     * <p>writeTarget.</p>
     *
     * @param object a {@link psidev.psi.mi.jami.model.CausalRelationship} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    protected void writeTarget(CausalRelationship object) throws XMLStreamException {
        this.streamWriter.writeStartElement("targetParticipantRef");
        this.streamWriter.writeCharacters(Integer.toString(this.objectIndex.extractIdForParticipant(object.getTarget())));
        this.streamWriter.writeEndElement();
    }

    /**
     * <p>writeCausalStatement.</p>
     *
     * @param object a {@link psidev.psi.mi.jami.model.CausalRelationship} object.
     */
    protected void writeCausalStatement(CausalRelationship object) {
        getCausalStatementWriter().write(object.getRelationType(), "causalityStatement");
    }

    /**
     * <p>writeSource.</p>
     *
     * @param object a {@link psidev.psi.mi.jami.model.CausalRelationship} object.
     * @param source a {@link psidev.psi.mi.jami.model.Participant} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    protected void writeSource(CausalRelationship object, Participant source) throws XMLStreamException {
        this.streamWriter.writeStartElement("sourceParticipantRef");
        this.streamWriter.writeCharacters(Integer.toString(this.objectIndex.extractIdForParticipant(source)));
        this.streamWriter.writeEndElement();
    }

    /**
     * <p>Getter for the field <code>streamWriter</code>.</p>
     *
     * @return a {@link javax.xml.stream.XMLStreamWriter} object.
     */
    protected XMLStreamWriter getStreamWriter() {
        return streamWriter;
    }

    /**
     * <p>Getter for the field <code>objectIndex</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache} object.
     */
    protected PsiXmlObjectCache getObjectIndex() {
        return objectIndex;
    }
}
