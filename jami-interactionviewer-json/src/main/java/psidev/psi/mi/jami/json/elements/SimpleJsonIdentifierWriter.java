package psidev.psi.mi.jami.json.elements;

import org.json.simple.JSONValue;
import psidev.psi.mi.jami.json.MIJsonUtils;
import psidev.psi.mi.jami.model.Xref;

import java.io.IOException;
import java.io.Writer;

/**
 * Json writer for identifiers
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>18/07/14</pre>
 */
public class SimpleJsonIdentifierWriter implements JsonElementWriter<Xref>{

    private Writer writer;

    /**
     * <p>Constructor for SimpleJsonIdentifierWriter.</p>
     *
     * @param writer a {@link java.io.Writer} object.
     */
    public SimpleJsonIdentifierWriter(Writer writer){
        if (writer == null){
            throw new IllegalArgumentException("The json identifier writer needs a non null Writer");
        }
        this.writer = writer;
    }

    /**
     * <p>write.</p>
     *
     * @param object a {@link psidev.psi.mi.jami.model.Xref} object.
     * @throws java.io.IOException if any.
     */
    public void write(Xref object) throws IOException {
        MIJsonUtils.writeStartObject(writer);
        MIJsonUtils.writeProperty("db", JSONValue.escape(object.getDatabase().getShortName()), writer);
        MIJsonUtils.writeSeparator(writer);
        MIJsonUtils.writeProperty("id", JSONValue.escape(object.getId()), writer);
        MIJsonUtils.writeEndObject(writer);
    }
}
