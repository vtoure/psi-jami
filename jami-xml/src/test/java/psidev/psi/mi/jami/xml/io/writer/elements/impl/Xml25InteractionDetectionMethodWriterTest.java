package psidev.psi.mi.jami.xml.io.writer.elements.impl;

import junit.framework.Assert;
import org.junit.Test;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.impl.DefaultAlias;
import psidev.psi.mi.jami.model.impl.DefaultCvTerm;
import psidev.psi.mi.jami.model.impl.DefaultXref;
import psidev.psi.mi.jami.xml.io.writer.AbstractXml25WriterTest;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * Unit tester for Xml25InteractionDetectionMethodWriter
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>21/11/13</pre>
 */

public class Xml25InteractionDetectionMethodWriterTest extends AbstractXml25WriterTest {

    private String detection = "<interactionDetectionMethod>\n" +
            "  <names>\n" +
            "    <shortLabel>inferred</shortLabel>\n"+
            "  </names>\n"+
            "  <xref>\n" +
            "    <primaryRef db=\"psi-mi\" id=\"MI:0362\" refType=\"identity\"/>\n"+
            "  </xref>\n"+
            "</interactionDetectionMethod>";
    private String detectionFullName = "<interactionDetectionMethod>\n" +
            "  <names>\n" +
            "    <shortLabel>inferred</shortLabel>\n"+
            "    <fullName>inference</fullName>\n"+
            "  </names>\n"+
            "  <xref>\n" +
            "    <primaryRef db=\"psi-mi\" id=\"MI:0362\" refType=\"identity\"/>\n"+
            "  </xref>\n"+
            "</interactionDetectionMethod>";
    private String detectionAliases = "<interactionDetectionMethod>\n" +
            "  <names>\n" +
            "    <shortLabel>inferred</shortLabel>\n"+
            "    <alias type=\"synonym\">unspecified</alias>\n"+
            "    <alias type=\"test\">test name</alias>\n"+
            "  </names>\n"+
            "  <xref>\n" +
            "    <primaryRef db=\"psi-mi\" id=\"MI:0362\" refType=\"identity\"/>\n"+
            "  </xref>\n"+
            "</interactionDetectionMethod>";
    private String detectionMod = "<interactionDetectionMethod>\n" +
            "  <names>\n" +
            "    <shortLabel>inferred</shortLabel>\n"+
            "  </names>\n"+
            "  <xref>\n" +
            "    <primaryRef db=\"psi-mod\" id=\"MI:0362\" refType=\"identity\"/>\n"+
            "  </xref>\n"+
            "</interactionDetectionMethod>";
    private String detectionPar = "<interactionDetectionMethod>\n" +
            "  <names>\n" +
            "    <shortLabel>inferred</shortLabel>\n"+
            "  </names>\n"+
            "  <xref>\n" +
            "    <primaryRef db=\"psi-par\" id=\"MI:0362\" refType=\"identity\"/>\n"+
            "  </xref>\n"+
            "</interactionDetectionMethod>";
    private String detectionFirstIdentifier = "<interactionDetectionMethod>\n" +
            "  <names>\n" +
            "    <shortLabel>inferred</shortLabel>\n"+
            "  </names>\n"+
            "  <xref>\n" +
            "    <primaryRef db=\"test\" id=\"MI:0362\" refType=\"identity\"/>\n"+
            "    <secondaryRef db=\"test2\" id=\"xxxxx2\"/>\n"+
            "    <secondaryRef db=\"test3\" id=\"xxxxx3\"/>\n"+
            "  </xref>\n"+
            "</interactionDetectionMethod>";
    private String detectionFirstXref = "<interactionDetectionMethod>\n" +
            "  <names>\n" +
            "    <shortLabel>inferred</shortLabel>\n"+
            "  </names>\n"+
            "  <xref>\n" +
            "    <primaryRef db=\"test2\" id=\"xxxxx2\"/>\n"+
            "    <secondaryRef db=\"test3\" id=\"xxxxx3\"/>\n"+
            "  </xref>\n"+
            "</interactionDetectionMethod>";

    @Test
    public void test_write_cv_no_fullName() throws XMLStreamException, IOException {
        CvTerm method = new DefaultCvTerm("inferred", "MI:0362");

        Xml25InteractionDetectionMethodWriter writer = new Xml25InteractionDetectionMethodWriter(createStreamWriter());
        writer.write(method);
        streamWriter.flush();

        Assert.assertEquals(this.detection, output.toString());
    }

    @Test
    public void test_write_cv_fullName() throws XMLStreamException, IOException {
        CvTerm method = new DefaultCvTerm("inferred", "MI:0362");
        method.setFullName("inference");

        Xml25InteractionDetectionMethodWriter writer = new Xml25InteractionDetectionMethodWriter(createStreamWriter());
        writer.write(method);
        streamWriter.flush();

        Assert.assertEquals(this.detectionFullName, output.toString());
    }

    @Test
    public void test_write_cv_aliases() throws XMLStreamException, IOException {
        CvTerm method = new DefaultCvTerm("inferred", "MI:0362");
        method.getSynonyms().add(new DefaultAlias(new DefaultCvTerm("synonym"), "unspecified"));
        method.getSynonyms().add(new DefaultAlias(new DefaultCvTerm("test"), "test name"));

        Xml25InteractionDetectionMethodWriter writer = new Xml25InteractionDetectionMethodWriter(createStreamWriter());
        writer.write(method);
        streamWriter.flush();

        Assert.assertEquals(this.detectionAliases, output.toString());
    }

    @Test
    public void test_write_cv_mod() throws XMLStreamException, IOException {
        CvTerm method = new DefaultCvTerm("inferred", "MI:0362");
        method.setMODIdentifier(method.getMIIdentifier());
        method.setMIIdentifier(null);

        Xml25InteractionDetectionMethodWriter writer = new Xml25InteractionDetectionMethodWriter(createStreamWriter());
        writer.write(method);
        streamWriter.flush();

        Assert.assertEquals(this.detectionMod, output.toString());
    }

    @Test
    public void test_write_cv_par() throws XMLStreamException, IOException {
        CvTerm method = new DefaultCvTerm("inferred", "MI:0362");
        method.setPARIdentifier(method.getMIIdentifier());
        method.setMIIdentifier(null);
        Xml25InteractionDetectionMethodWriter writer = new Xml25InteractionDetectionMethodWriter(createStreamWriter());
        writer.write(method);
        streamWriter.flush();

        Assert.assertEquals(this.detectionPar, output.toString());
    }

    @Test
    public void test_write_cv_first_identifier() throws XMLStreamException, IOException {
        CvTerm method = new DefaultCvTerm("inferred", "MI:0362");
        method.getIdentifiers().iterator().next().getDatabase().setShortName("test");
        method.getIdentifiers().iterator().next().getDatabase().setMIIdentifier(null);
        method.getXrefs().add(new DefaultXref(new DefaultCvTerm("test2"), "xxxxx2"));
        method.getXrefs().add(new DefaultXref(new DefaultCvTerm("test3"), "xxxxx3"));

        Xml25InteractionDetectionMethodWriter writer = new Xml25InteractionDetectionMethodWriter(createStreamWriter());
        writer.write(method);
        streamWriter.flush();

        Assert.assertEquals(this.detectionFirstIdentifier, output.toString());
    }

    @Test
    public void test_write_cv_first_xref() throws XMLStreamException, IOException {
        CvTerm method = new DefaultCvTerm("inferred");
        method.getXrefs().add(new DefaultXref(new DefaultCvTerm("test2"), "xxxxx2"));
        method.getXrefs().add(new DefaultXref(new DefaultCvTerm("test3"), "xxxxx3"));

        Xml25InteractionDetectionMethodWriter writer = new Xml25InteractionDetectionMethodWriter(createStreamWriter());
        writer.write(method);
        streamWriter.flush();

        Assert.assertEquals(this.detectionFirstXref, output.toString());
    }
}
