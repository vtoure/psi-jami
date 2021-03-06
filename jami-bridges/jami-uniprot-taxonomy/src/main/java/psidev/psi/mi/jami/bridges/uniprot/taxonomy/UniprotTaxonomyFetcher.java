package psidev.psi.mi.jami.bridges.uniprot.taxonomy;

import com.hp.hpl.jena.rdf.model.*;
import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.bridges.fetcher.OrganismFetcher;
import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.model.impl.DefaultOrganism;
import psidev.psi.mi.jami.utils.AliasUtils;
import psidev.psi.mi.jami.utils.OrganismUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 28/06/13

 */
public class UniprotTaxonomyFetcher implements OrganismFetcher {

    private final Logger log = Logger.getLogger(UniprotTaxonomyFetcher.class.getName());

    private static final String UNIPROT_NS = "http://purl.uniprot.org/core/";
    private static final String UNIPROT_TAXONOMY_NS = "http://purl.uniprot.org/taxonomy/";
    private static final String UNIPROT_TAXONOMY_URL = "https://www.uniprot.org/taxonomy/";

    /** {@inheritDoc} */
    public Organism fetchByTaxID(int taxID) throws BridgeFailedException {
        Organism organism = OrganismUtils.createSpecialistOrganism(taxID);
        if ( organism == null ) {
            organism = fetchOrganismFromStream( taxID );
        }
        return organism;
    }

    /** {@inheritDoc} */
    public Collection<Organism> fetchByTaxIDs(Collection<Integer> taxIDs) throws BridgeFailedException {

        if (taxIDs == null){
            throw new IllegalArgumentException("The collection of taxids cannot be null");
        }
        if (taxIDs.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        Collection<Organism> results = new ArrayList<Organism>(taxIDs.size());
        for(Integer taxID : taxIDs){
            Organism organism = fetchByTaxID(taxID);
            if (organism != null){
                results.add(organism);
            }
        }
        return results;
    }

    // NOTE: It takes the IntAct approach; the nemonic has precedence vs the common name.
    // By default returns nmemonic and adds the common name to alias
    private Organism fetchOrganismFromStream(int taxID) throws BridgeFailedException {
        InputStream stream = null;
        Organism organism;
        try {
            URL url = new URL(generateQueryUrl(taxID));
            URLConnection con = url.openConnection();
            con.setConnectTimeout(20000);
            con.setReadTimeout(20000);
            stream = con.getInputStream();

            Model model = ModelFactory.createDefaultModel();
            model.read(stream, UNIPROT_NS);
            // model.write(System.out);
            Resource taxonomyResource = model.getResource(UNIPROT_TAXONOMY_NS + taxID);

            if (log.isLoggable(Level.WARNING)) {
                // check first if it has been replaced by another record (would contain the replacedBy property)
                Property replacedByProperty = model.getProperty(UNIPROT_NS, "replacedBy");
                boolean isReplaced = model.contains(taxonomyResource, replacedByProperty);

                if (isReplaced) {
                    Statement replacedStatement = model.getProperty(taxonomyResource, replacedByProperty);
                    String replacedUri = replacedStatement.getObject().asResource().getURI();
                    String replacedByTaxidStr = replacedUri.replaceAll(UNIPROT_TAXONOMY_NS, "");
                    int replacedByTaxid = Integer.parseInt(replacedByTaxidStr);

                    log.info("WARNING - the taxid replacement for " + taxID + " is " + replacedByTaxid);
                }
            }

            organism = new DefaultOrganism(taxID);

            String mnemonic = getLiteral(model, taxonomyResource, "mnemonic");
            if (mnemonic != null && mnemonic.length() > 0) organism.setCommonName(mnemonic);

            String commonName = getLiteral(model, taxonomyResource, "commonName");
            if (commonName != null && commonName.length() > 0) {
                if (mnemonic == null) {
                    organism.setCommonName(commonName);
                } else {
                    organism.getAliases().add(AliasUtils.createAlias(Alias.SYNONYM, Alias.SYNONYM_MI, commonName));
                }
            }

            String scientificName = getLiteral(model, taxonomyResource, "scientificName");
            if (scientificName != null && scientificName.length() > 0) organism.setScientificName(scientificName);

            if (organism.getCommonName() == null) {
                organism.setCommonName(scientificName);
            }

            String synonym = getLiteral(model, taxonomyResource, "synonym");
            if (synonym != null && synonym.length() > 0)
                organism.getAliases().add(AliasUtils.createAlias(Alias.SYNONYM, Alias.SYNONYM_MI, synonym));


        } catch (IOException e) {
            throw new BridgeFailedException("Input stream failed to open", e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                log.severe("Cannot close stream: " + e);
            }
        }
        return organism;
    }

    private String getLiteral(Model model, Resource taxonomyResource, String propertyName) {
        Property property = model.getProperty(UNIPROT_NS, propertyName);
        if (model.contains(taxonomyResource, property)) {
            return model.getProperty(taxonomyResource, property).getLiteral().getString();
        }
        return null;
    }

    private String generateQueryUrl( int taxid ) throws IOException {
        return UNIPROT_TAXONOMY_URL + taxid + ".rdf";
    }
}
