package psidev.psi.mi.jami.xml.io.writer.elements.impl.extended;

import javax.xml.stream.XMLStreamWriter;

/**
 * Xml writer for open cv terms
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>12/11/13</pre>
 */
public class XmlOpenCvTermWriter extends psidev.psi.mi.jami.xml.io.writer.elements.impl.XmlOpenCvTermWriter {

    /**
     * <p>Constructor for XmlOpenCvTermWriter.</p>
     *
     * @param writer a {@link javax.xml.stream.XMLStreamWriter} object.
     */
    public XmlOpenCvTermWriter(XMLStreamWriter writer) {
        super(writer);
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseXrefWriter() {
        super.setXrefWriter(new XmlDbXrefWriter(getStreamWriter()));
    }
}
