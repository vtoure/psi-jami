package psidev.psi.mi.jami.xml.io.writer.elements.impl.xml30;

import psidev.psi.mi.jami.exception.MIIOException;
import psidev.psi.mi.jami.model.Confidence;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.ModelledConfidence;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlPublicationWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.XmlConfidenceWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Xml30 writer for modelled confidences
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>12/11/13</pre>
 */
public class XmlModelledConfidenceWriter extends XmlConfidenceWriter {
    private PsiXmlPublicationWriter publicationWriter;

    /**
     * <p>Constructor for XmlModelledConfidenceWriter.</p>
     *
     * @param writer a {@link javax.xml.stream.XMLStreamWriter} object.
     */
    public XmlModelledConfidenceWriter(XMLStreamWriter writer){
        super(writer);
    }

    /**
     * <p>Getter for the field <code>publicationWriter</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlPublicationWriter} object.
     */
    public PsiXmlPublicationWriter getPublicationWriter() {
        if (this.publicationWriter == null){
            initialisePublicationWriter();
        }
        return publicationWriter;
    }

    /**
     * <p>initialisePublicationWriter.</p>
     */
    protected void initialisePublicationWriter() {
        this.publicationWriter = new XmlPublicationWriter(getStreamWriter());
    }

    /**
     * <p>Setter for the field <code>publicationWriter</code>.</p>
     *
     * @param publicationWriter a {@link psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlPublicationWriter} object.
     */
    public void setPublicationWriter(PsiXmlPublicationWriter publicationWriter) {
        this.publicationWriter = publicationWriter;
    }

    /** {@inheritDoc} */
    @Override
    public void write(Confidence object) throws MIIOException {
        if (object != null){
            try {
                // write start
                this.getStreamWriter().writeStartElement("confidence");
                // write confidence type
                CvTerm type = object.getType();
                getTypeWriter().write(type, "type");
                // write value
                this.getStreamWriter().writeStartElement("value");
                this.getStreamWriter().writeCharacters(object.getValue());
                this.getStreamWriter().writeEndElement();

                // write other properties
                writeOtherProperties(object);

                // write end confidence
                this.getStreamWriter().writeEndElement();

            } catch (XMLStreamException e) {
                throw new MIIOException("Impossible to write the confidence : "+object.toString(), e);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void writeOtherProperties(Confidence object) {
        // write publication if not done yet
        ModelledConfidence modelledParam = (ModelledConfidence)object;

        // write bibref if necessary
        if (modelledParam.getPublication() != null){
            getPublicationWriter().write(modelledParam.getPublication());
        }
    }
}
