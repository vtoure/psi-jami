package psidev.psi.mi.jami.xml.io.writer.elements.impl.extended;

import psidev.psi.mi.jami.model.Position;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.abstracts.AbstractXmlPositionWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * XML writer for the end position of a range
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>13/11/13</pre>
 */
public class XmlEndPositionWriter extends AbstractXmlPositionWriter {
    /**
     * <p>Constructor for XmlEndPositionWriter.</p>
     *
     * @param writer a {@link javax.xml.stream.XMLStreamWriter} object.
     */
    public XmlEndPositionWriter(XMLStreamWriter writer) {
        super(writer);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeStartPositionNode() throws XMLStreamException {
        getStreamWriter().writeStartElement("end");
    }

    /** {@inheritDoc} */
    @Override
    protected void writeStartIntervalNode() throws XMLStreamException {
        getStreamWriter().writeStartElement("endInterval");
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseStatusWriter() {
        super.setStatusWriter(new XmlCvTermWriter(getStreamWriter()));
    }

    /** {@inheritDoc} */
    @Override
    protected void writeStatus(Position object) {
        getStatusWriter().write(object.getStatus(),"endStatus");
    }
}
