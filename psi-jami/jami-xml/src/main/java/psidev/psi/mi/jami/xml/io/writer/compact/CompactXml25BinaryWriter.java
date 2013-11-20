package psidev.psi.mi.jami.xml.io.writer.compact;

import psidev.psi.mi.jami.binary.BinaryInteraction;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.binary.ModelledBinaryInteraction;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Compact PSI-XML 2.5 writer for a mix of binary interactions
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>19/11/13</pre>
 */

public class CompactXml25BinaryWriter extends AbstractCompactXml25MixWriter<BinaryInteraction, ModelledBinaryInteraction, BinaryInteractionEvidence>{

    public CompactXml25BinaryWriter() {
        super(BinaryInteraction.class);
    }

    public CompactXml25BinaryWriter(File file) throws IOException, XMLStreamException {
        super(BinaryInteraction.class, file);
    }

    public CompactXml25BinaryWriter(OutputStream output) throws XMLStreamException {
        super(BinaryInteraction.class, output);
    }

    public CompactXml25BinaryWriter(Writer writer) throws XMLStreamException {
        super(BinaryInteraction.class, writer);
    }

    @Override
    protected void initialiseDelegateWriters() {
        setModelledWriter(new CompactXml25ModelledBinaryWriter(getStreamWriter()));
        setEvidenceWriter(new CompactXml25BinaryEvidenceWriter(getStreamWriter()));
        setLightWriter(new LightCompactXml25BinaryWriter(getStreamWriter()));
    }
}