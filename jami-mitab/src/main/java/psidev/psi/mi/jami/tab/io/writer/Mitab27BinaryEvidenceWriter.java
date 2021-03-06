package psidev.psi.mi.jami.tab.io.writer;

import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.ParticipantEvidence;
import psidev.psi.mi.jami.tab.io.writer.feeder.MitabInteractionEvidenceFeeder;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Mitab 2.7 writer for binary interaction evidences
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>20/06/13</pre>
 */
public class Mitab27BinaryEvidenceWriter extends AbstractMitab27BinaryWriter<BinaryInteractionEvidence, ParticipantEvidence> {

    /**
     * <p>Constructor for Mitab27BinaryEvidenceWriter.</p>
     */
    public Mitab27BinaryEvidenceWriter() {
        super();
    }

    /**
     * <p>Constructor for Mitab27BinaryEvidenceWriter.</p>
     *
     * @param file a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public Mitab27BinaryEvidenceWriter(File file) throws IOException {
        super(file);
    }

    /**
     * <p>Constructor for Mitab27BinaryEvidenceWriter.</p>
     *
     * @param output a {@link java.io.OutputStream} object.
     */
    public Mitab27BinaryEvidenceWriter(OutputStream output){
        super(output);
    }

    /**
     * <p>Constructor for Mitab27BinaryEvidenceWriter.</p>
     *
     * @param writer a {@link java.io.Writer} object.
     */
    public Mitab27BinaryEvidenceWriter(Writer writer){
        super(writer);
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseColumnFeeder() {
        setColumnFeeder(new MitabInteractionEvidenceFeeder(getWriter()));
    }
}
