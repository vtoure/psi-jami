package psidev.psi.mi.jami.xml.io.writer.elements.impl.extended.xml30;

import psidev.psi.mi.jami.xml.io.writer.elements.impl.extended.XmlOpenCvTermWriter;

import javax.xml.stream.XMLStreamWriter;

/**
 * Xml30 writer for modelled confidences
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>12/11/13</pre>
 */
public class XmlModelledConfidenceWriter extends psidev.psi.mi.jami.xml.io.writer.elements.impl.xml30.XmlModelledConfidenceWriter {
    /**
     * <p>Constructor for XmlModelledConfidenceWriter.</p>
     *
     * @param writer a {@link javax.xml.stream.XMLStreamWriter} object.
     */
    public XmlModelledConfidenceWriter(XMLStreamWriter writer){
        super(writer);
    }

    /** {@inheritDoc} */
    @Override
    protected void initialisePublicationWriter() {
        super.setPublicationWriter(new XmlPublicationWriter(getStreamWriter()));
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseTypeWriter() {
        super.setTypeWriter(new XmlOpenCvTermWriter(getStreamWriter()));
    }
}
