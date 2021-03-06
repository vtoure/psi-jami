package psidev.psi.mi.jami.tab.io.parser;

import psidev.psi.mi.jami.datasource.DefaultFileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.factory.InteractorFactory;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.tab.extension.*;
import psidev.psi.mi.jami.tab.extension.factory.MitabInteractorFactory;
import psidev.psi.mi.jami.tab.listener.MitabParserListener;
import psidev.psi.mi.jami.tab.utils.MitabUtils;
import psidev.psi.mi.jami.utils.AliasUtils;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.XrefUtils;

import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Abstract mitab line parser
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>20/06/13</pre>
 */
public abstract class AbstractInteractionLineParser<T extends Interaction, P extends Participant, F extends Feature> extends MitabLineParser<T,P,F> {

    private MitabParserListener listener;
    private InteractorFactory interactorFactory;
    private boolean hasFinished = false;
    private StringBuilder builder = new StringBuilder(82);

    private static final String INTERPRO_PATTERN = "^IPR\\d{6}$";

    /**
     * <p>Constructor for AbstractInteractionLineParser.</p>
     *
     * @param stream a {@link java.io.InputStream} object.
     */
    public AbstractInteractionLineParser(InputStream stream) {
        super(stream);
    }

    /**
     * <p>Constructor for AbstractInteractionLineParser.</p>
     *
     * @param stream a {@link java.io.InputStream} object.
     * @param encoding a {@link java.lang.String} object.
     */
    public AbstractInteractionLineParser(InputStream stream, String encoding) {
        super(stream, encoding);
    }

    /**
     * <p>Constructor for AbstractInteractionLineParser.</p>
     *
     * @param stream a {@link java.io.Reader} object.
     */
    public AbstractInteractionLineParser(Reader stream) {
        super(stream);
    }

    /**
     * <p>Constructor for AbstractInteractionLineParser.</p>
     *
     * @param tm a {@link psidev.psi.mi.jami.tab.io.parser.MitabLineParserTokenManager} object.
     */
    public AbstractInteractionLineParser(MitabLineParserTokenManager tm) {
        super(tm);
    }

    /** {@inheritDoc} */
    @Override
    public MitabParserListener getParserListener() {
        return listener;
    }

    /** {@inheritDoc} */
    @Override
    public void setParserListener(MitabParserListener listener) {
        this.listener = listener;
    }

    /**
     * <p>Getter for the field <code>interactorFactory</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.factory.InteractorFactory} object.
     */
    public InteractorFactory getInteractorFactory() {
        if (interactorFactory == null){
            interactorFactory = new MitabInteractorFactory();
        }
        return interactorFactory;
    }

    /**
     * <p>Setter for the field <code>interactorFactory</code>.</p>
     *
     * @param interactorFactory a {@link psidev.psi.mi.jami.factory.InteractorFactory} object.
     */
    public void setInteractorFactory(InteractorFactory interactorFactory) {
        this.interactorFactory = interactorFactory;
    }

    @Override
    void fireOnInvalidSyntax(int numberLine, int numberColumn, int mitabColumn, Exception e) {
        if (this.listener != null){
            this.listener.onInvalidSyntax(new DefaultFileSourceContext(new MitabSourceLocator(numberLine, numberColumn, mitabColumn)), e);
        }
    }

    @Override
    void reachEndOfFile() {
        this.hasFinished = true;
    }

    /** {@inheritDoc} */
    @Override
    public void ReInit(InputStream stream) {
        hasFinished = false;
        super.ReInit(stream);
    }

    /** {@inheritDoc} */
    @Override
    public void ReInit(InputStream stream, String encoding) {
        hasFinished = false;
        super.ReInit(stream, encoding);
    }

    /** {@inheritDoc} */
    @Override
    public void ReInit(Reader stream) {
        hasFinished = false;
        super.ReInit(stream);
    }

    /** {@inheritDoc} */
    @Override
    public void ReInit(MitabLineParserTokenManager tm) {
        hasFinished = false;
        super.ReInit(tm);
    }

    /**
     * <p>hasFinished.</p>
     *
     * @return a boolean.
     */
    public boolean hasFinished() {
        return hasFinished;
    }

    @Override
    StringBuilder resetStringBuilder() {
        builder.setLength(0);
        return builder;
    }

    /**
     * <p>initialiseInteractionIdentifiers.</p>
     *
     * @param interactionIds a {@link java.util.Collection} object.
     * @param interaction a T object.
     */
    protected void initialiseInteractionIdentifiers(Collection<MitabXref> interactionIds, T interaction){

        Iterator<MitabXref> refsIterator = interactionIds.iterator();
        while (refsIterator.hasNext()){
            MitabXref ref = refsIterator.next();

            if (XrefUtils.isXrefFromDatabase(ref, Xref.IMEX_MI, Xref.IMEX) && XrefUtils.doesXrefHaveQualifier(ref, Xref.IMEX_PRIMARY_MI, Xref.IMEX_PRIMARY)){
                interaction.getXrefs().add(ref);
            }
            else if (XrefUtils.isXrefFromDatabase(ref, Checksum.RIGID_MI, Checksum.RIGID) || XrefUtils.isXrefFromDatabase(ref, null, Checksum.IRIGID)){
                createChecksumFromId(interaction, ref);
            }
            else{
                interaction.getIdentifiers().add(ref);
            }
        }
    }

    /**
     * <p>createInteractorFrom.</p>
     *
     * @param uniqueId a {@link java.util.Collection} object.
     * @param altid a {@link java.util.Collection} object.
     * @param aliases a {@link java.util.Collection} object.
     * @param taxid a {@link java.util.Collection} object.
     * @param type a {@link java.util.Collection} object.
     * @param xref a {@link java.util.Collection} object.
     * @param checksum a {@link java.util.Collection} object.
     * @param line a int.
     * @param column a int.
     * @param mitabColumn a int.
     * @return a {@link psidev.psi.mi.jami.model.Interactor} object.
     */
    protected Interactor createInteractorFrom(Collection<MitabXref> uniqueId, Collection<MitabXref> altid, Collection<MitabAlias> aliases, Collection<MitabOrganism> taxid, Collection<MitabCvTerm> type, Collection<MitabXref> xref, Collection<MitabChecksum> checksum, int line, int column, int mitabColumn){
        boolean hasId = !uniqueId.isEmpty() || !altid.isEmpty();
        boolean hasAlias = !aliases.isEmpty();
        boolean hasOtherFields = !taxid.isEmpty() || !checksum.isEmpty() || !type.isEmpty() || !xref.isEmpty();
        Interactor interactor = null;
        String shortName;
        String fullName = null;

        // find shortName first
        // the interactor is empty
        if (!hasId && !hasAlias && !hasOtherFields){
            return null;
        }
        // the interactor name will be unknown but needs to be created
        else if (!hasId && !hasAlias){
            if (this.listener != null){
                listener.onMissingInteractorIdentifierColumns(line, column, mitabColumn);
            }
            shortName = MitabUtils.UNKNOWN_NAME;
        }
        else{
            // first retrieve what will be the name of the interactor
            String [] names = findInteractorShortNameAndFullNameFrom(uniqueId, altid, aliases, line, column, mitabColumn);
            if (names.length == 1){
                shortName = names[0];
            }
            else if (names.length == 2){
                shortName = names[0];
                fullName = names[1];
            }
            else{
                shortName = MitabUtils.UNKNOWN_NAME;
            }
        }

        // fire event if several uniqueIds
        if (uniqueId.size() > 1 && listener != null){
            listener.onSeveralUniqueIdentifiers(uniqueId);
        }

        // find interactor type
        if (getInteractorFactory() instanceof MitabInteractorFactory){
            interactor = ((MitabInteractorFactory)getInteractorFactory()).createInteractorFromInteractorTypes(type, shortName);
        }
        else if (!type.isEmpty()){
            interactor = getInteractorFactory().createInteractorFromInteractorType(type.iterator().next(), shortName);
        }
        // we don't have an interactor type, use identifiers
        if (interactor == null && !uniqueId.isEmpty()){
            interactor = getInteractorFactory().createInteractorFromIdentityXrefs(uniqueId, shortName);

            if (interactor == null && !altid.isEmpty()){
                interactor = getInteractorFactory().createInteractorFromIdentityXrefs(altid, shortName);
                // we still don't know which interactor it is
                if (interactor == null){
                    interactor = getInteractorFactory().createInteractor(shortName, null);
                }
            }
            // we still don't know which interactor it is
            else if (interactor == null){
                interactor = getInteractorFactory().createInteractor(shortName, null);
            }
        }
        else if (interactor == null && !altid.isEmpty()){
            interactor = getInteractorFactory().createInteractorFromIdentityXrefs(altid, shortName);

            // we still don't know which interactor it is
            if (interactor == null){
                interactor = getInteractorFactory().createInteractor(shortName, null);
            }
        }
        // we don't have an interactor type, and we don't have identifiers, create an unknown participant
        else if (interactor == null){
            interactor = getInteractorFactory().createInteractor(shortName, null);
        }

        // set fullName
        interactor.setFullName(fullName);

        if (hasId){
            // add unique ids first
            interactor.getIdentifiers().addAll(uniqueId);

            // add alternative identifiers
            fillInteractorWithAlternativeIdentifiers(altid, interactor);
        }
        if (hasAlias){
            fillInteractorWithAliases(aliases, interactor);
        }

        // add checksum
        interactor.getChecksums().addAll(checksum);

        // if we have an interactor set, we can retrieve the components from the xrefs
        if (interactor instanceof InteractorPool){
            processInteractorPool(xref, (InteractorPool) interactor);
        }
        // add all xrefs
        else{
            interactor.getXrefs().addAll(xref);
        }
        // set organism
        initialiseOrganism(taxid, interactor);
        // if several types fire event
        if (type.size() > 1 && listener != null){
            listener.onSeveralCvTermsFound(type, type.iterator().next(), type.size() + " interactor types were found and only the first one will be loaded.");
        }

        // set source locator
        ((FileSourceContext)interactor).setSourceLocator(new MitabSourceLocator(line, column, mitabColumn));

        if (!hasAlias){
            listener.onMissingInteractorName(interactor, (FileSourceContext)interactor);
        }

        return interactor;
    }

    /**
     * <p>processInteractorPool.</p>
     *
     * @param xref a {@link java.util.Collection} object.
     * @param interactor a {@link psidev.psi.mi.jami.model.InteractorPool} object.
     */
    protected void processInteractorPool(Collection<MitabXref> xref, InteractorPool interactor) {
        InteractorPool pool = (InteractorPool)interactor;
        for (Xref ref : xref){
            // we have a component of the interactor pool
            if (XrefUtils.doesXrefHaveQualifier(ref, Xref.INTERACTOR_SET_QUALIFIER_MI, Xref.INTERACTOR_SET_QUALIFIER)){
                Interactor subInteractor = interactorFactory.createInteractorFromDatabase(ref.getDatabase(), ref.getId().toLowerCase());
                if (subInteractor != null){
                    subInteractor.getIdentifiers().add(new MitabXref(ref.getDatabase(), ref.getId(), ref.getVersion(), CvTermUtils.createIdentityQualifier()));
                    ((MitabInteractor)subInteractor).setSourceLocator(((MitabXref)ref).getSourceLocator());
                }
                // create a default interactor
                else{
                    subInteractor = interactorFactory.createInteractor(ref.getId().toLowerCase(), CvTermUtils.createUnknownInteractorType());
                    subInteractor.getIdentifiers().add(new MitabXref(ref.getDatabase(), ref.getId(), ref.getVersion(), CvTermUtils.createIdentityQualifier()));
                    ((MitabInteractor)subInteractor).setSourceLocator(((MitabXref)ref).getSourceLocator());
                }

                // add the component to the interactor pool
                pool.add(subInteractor);
            }
            // we have a simple xref
            else{
                pool.getXrefs().add(ref);
            }
        }
    }

    /**
     * <p>findInteractorShortNameAndFullNameFrom.</p>
     *
     * @param uniqueId a {@link java.util.Collection} object.
     * @param altid a {@link java.util.Collection} object.
     * @param aliases a {@link java.util.Collection} object.
     * @param line a int.
     * @param column a int.
     * @param mitabColumn a int.
     * @return an array of {@link java.lang.String} objects.
     */
    protected String[] findInteractorShortNameAndFullNameFrom(Collection<MitabXref> uniqueId, Collection<MitabXref> altid, Collection<MitabAlias> aliases, int line, int column, int mitabColumn){

        MitabAlias[] names = MitabUtils.findBestShortNameAndFullNameFromAliases(aliases);
        if (names != null){
            MitabAlias shortName = null;
            MitabAlias fullName = null;
            if (names.length == 1){
                shortName = names[0];
                // do not need to keep the shortname as it is loaded as a shortname
                if (shortName.getType() != null && MitabUtils.DISPLAY_SHORT.equals(shortName.getType().getShortName())){
                    aliases.remove(shortName);
                }
                return new String[]{shortName.getName()};
            }
            else if (names.length == 2){
                shortName = names[0];
                fullName = names[1];
                if (shortName.getType() != null && MitabUtils.DISPLAY_SHORT.equals(shortName.getType().getShortName())){
                    aliases.remove(shortName);
                }
                if (fullName.getType() != null && MitabUtils.DISPLAY_LONG.equals(fullName.getType().getShortName())){
                    aliases.remove(shortName);
                }
                return new String[]{shortName.getName(), fullName.getName()};
            }
        }

        MitabXref shortNameFromAltid = MitabUtils.findBestShortNameFromAlternativeIdentifiers(altid);
        if (shortNameFromAltid != null){
            return new String[]{shortNameFromAltid.getId()};
        }
        else if (!uniqueId.isEmpty()){
            return new String[]{uniqueId.iterator().next().getId()};
        }
        else if (!altid.isEmpty()){
            if (this.listener != null){
                listener.onEmptyUniqueIdentifiers(line, column, mitabColumn);
            }

            return new String[]{altid.iterator().next().getId()};
        }
        else if (this.listener != null){
            listener.onEmptyUniqueIdentifiers(line, column, mitabColumn);
        }

        return null;
    }

    /**
     * <p>fillInteractorWithAlternativeIdentifiers.</p>
     *
     * @param altid a {@link java.util.Collection} object.
     * @param interactor a {@link psidev.psi.mi.jami.model.Interactor} object.
     */
    protected void fillInteractorWithAlternativeIdentifiers(Collection<MitabXref> altid, Interactor interactor){

        Iterator<MitabXref> refsIterator = altid.iterator();
        while (refsIterator.hasNext()){
            MitabXref ref = refsIterator.next();

            // gene name is alias
            if (XrefUtils.doesXrefHaveQualifier(ref, Alias.GENE_NAME_MI, Alias.GENE_NAME)){
                createAliasFromAltId(interactor, ref);
            }
            // gene name synonym is alias
            else if (XrefUtils.doesXrefHaveQualifier(ref, Alias.GENE_NAME_SYNONYM_MI, Alias.GENE_NAME)){
                createAliasFromAltId(interactor, ref);
            }
            // short label is alias
            else if (XrefUtils.doesXrefHaveQualifier(ref, null, MitabUtils.SHORTLABEL)){
                createAliasFromAltId(interactor, ref);
            }
            // display short is alias
            else if (XrefUtils.doesXrefHaveQualifier(ref, null, MitabUtils.DISPLAY_SHORT)){
                createAliasFromAltId(interactor, ref);
            }
            // display long is alias
            else if (XrefUtils.doesXrefHaveQualifier(ref, null, MitabUtils.DISPLAY_LONG)){
                createAliasFromAltId(interactor, ref);
            }
            // database is rogid so we have a checksum
            else if (XrefUtils.isXrefFromDatabase(ref, Checksum.ROGID_MI, Checksum.ROGID)){
                createChecksumFromAltId(interactor, ref);

            }
            // database is irogid so we have a checksum
            else if (XrefUtils.isXrefFromDatabase(ref, null, Checksum.IROGID)){
                createChecksumFromAltId(interactor, ref);
            }
            // we have a simple xref
            else {
                interactor.getIdentifiers().add(ref);
            }
        }
    }

    /**
     * <p>fillInteractorWithAliases.</p>
     *
     * @param aliases a {@link java.util.Collection} object.
     * @param interactor a {@link psidev.psi.mi.jami.model.Interactor} object.
     */
    protected void fillInteractorWithAliases(Collection<MitabAlias> aliases, Interactor interactor){

        Iterator<MitabAlias> aliasIterator = aliases.iterator();
        while (aliasIterator.hasNext()){
            MitabAlias alias = aliasIterator.next();

            // we have a smile
            if (AliasUtils.doesAliasHaveType(alias, Checksum.SMILE_MI, Checksum.SMILE) ||
                    AliasUtils.doesAliasHaveType(alias, Checksum.SMILE_MI, Checksum.SMILE_SHORT)){
                createChecksumFromAlias(interactor, alias);

            }
            // we have inchi key
            else if (AliasUtils.doesAliasHaveType(alias, Checksum.INCHI_KEY_MI, Checksum.INCHI_KEY)){
                createChecksumFromAlias(interactor, alias);
            }
            // we have standard inchi
            else if (AliasUtils.doesAliasHaveType(alias, Checksum.STANDARD_INCHI_KEY_MI, Checksum.STANDARD_INCHI_KEY)){
                createChecksumFromAlias(interactor, alias);
            }
            // we have inchi
            else if (AliasUtils.doesAliasHaveType(alias, Checksum.INCHI_MI, Checksum.INCHI) ||
                    AliasUtils.doesAliasHaveType(alias, Checksum.INCHI_MI, Checksum.INCHI_SHORT)){
                createChecksumFromAlias(interactor, alias);
            }
            // we have rogid
            else if (AliasUtils.doesAliasHaveType(alias, Checksum.ROGID_MI, Checksum.ROGID) ||
                    AliasUtils.doesAliasHaveType(alias, null, Checksum.IROGID)){
                createChecksumFromAlias(interactor, alias);
            }
            // we have a simple alias
            else {
                interactor.getAliases().add(alias);
            }
        }
    }

    /**
     * <p>initialiseOrganism.</p>
     *
     * @param organisms a {@link java.util.Collection} object.
     * @param interactor a {@link psidev.psi.mi.jami.model.Interactor} object.
     */
    protected void initialiseOrganism(Collection<MitabOrganism> organisms, Interactor interactor){

        if (organisms.size() > 1){
            Iterator<MitabOrganism> organismsIterator = organisms.iterator();
            int taxid=0;
            String commonName=null;
            int commonNameLength = 0;
            int fullNameLength = 0;
            MitabOrganism currentOrganism=null;
            boolean hasSeveralOrganisms = false;
            do {

                MitabOrganism organism = organismsIterator.next();
                if (currentOrganism == null){
                    currentOrganism = organism;
                    commonName = organism.getCommonName();
                    commonNameLength = commonName.length();
                    fullNameLength = commonName.length();
                    taxid = organism.getTaxId();
                }
                // we have same organism
                else if (organism.getTaxId() == taxid){
                    // we have a new common name
                    if (organism.getCommonName() != null && organism.getCommonName().length() < commonNameLength){
                        if (currentOrganism.getScientificName() == null){
                            currentOrganism.setScientificName(currentOrganism.getCommonName());
                        }
                        // we have a synonym for the organism
                        else {
                            currentOrganism.getAliases().add(AliasUtils.createAlias(Alias.SYNONYM, Alias.SYNONYM_MI, currentOrganism.getCommonName()));
                        }
                        currentOrganism.setCommonName(organism.getCommonName());
                        commonNameLength = organism.getCommonName().length();
                    }
                    // we have a full name
                    else if (currentOrganism.getScientificName() == null){
                        currentOrganism.setScientificName(organism.getCommonName());
                        fullNameLength = organism.getCommonName().length();
                    }
                    // we have a new fullname
                    else if (organism.getCommonName().length() < fullNameLength) {
                        currentOrganism.getAliases().add(AliasUtils.createAlias(Alias.SYNONYM, Alias.SYNONYM_MI, currentOrganism.getScientificName()));
                        currentOrganism.setScientificName(organism.getCommonName());
                        fullNameLength = organism.getCommonName().length();
                    }
                    // we have a synonym for the organism
                    else {
                        currentOrganism.getAliases().add(AliasUtils.createAlias(Alias.SYNONYM, Alias.SYNONYM_MI, organism.getCommonName()));
                    }
                }
                else{
                    hasSeveralOrganisms = true;
                }

            } while(organismsIterator.hasNext());

            if (listener != null && hasSeveralOrganisms){
                listener.onSeveralOrganismFound(organisms);
            }

            interactor.setOrganism(currentOrganism);
        }
        else if (!organisms.isEmpty()){
            interactor.setOrganism(organisms.iterator().next());
        }
    }

    /**
     * <p>createChecksumFromId.</p>
     *
     * @param interaction a {@link psidev.psi.mi.jami.model.Interaction} object.
     * @param ref a {@link psidev.psi.mi.jami.tab.extension.MitabXref} object.
     */
    protected void createChecksumFromId(Interaction interaction, MitabXref ref) {
        // create checksum from xref
        MitabChecksum checksum = new MitabChecksum(ref.getDatabase(), ref.getId(), ref.getSourceLocator());
        interaction.getChecksums().add(checksum);
        if (listener != null){
            listener.onSyntaxWarning(ref, "Found a Checksum in the interaction identifiers column. Will load it as a checksum.");
        }
    }

    /**
     * <p>createChecksumFromAltId.</p>
     *
     * @param interactor a {@link psidev.psi.mi.jami.model.Interactor} object.
     * @param ref a {@link psidev.psi.mi.jami.tab.extension.MitabXref} object.
     */
    protected void createChecksumFromAltId(Interactor interactor, MitabXref ref) {
        // create checksum from xref
        MitabChecksum checksum = new MitabChecksum(ref.getDatabase(), ref.getId(), ref.getSourceLocator());
        interactor.getChecksums().add(checksum);
        if (listener != null){
            listener.onSyntaxWarning(ref, "Found a Checksum in the alternative identifiers column. Will load it as a checksum.");
        }
    }

    /**
     * <p>createAliasFromAltId.</p>
     *
     * @param interactor a {@link psidev.psi.mi.jami.model.Interactor} object.
     * @param ref a {@link psidev.psi.mi.jami.tab.extension.MitabXref} object.
     */
    protected void createAliasFromAltId(Interactor interactor, MitabXref ref) {
        // create alias from xref
        MitabAlias alias = new MitabAlias(ref.getDatabase().getShortName(), ref.getQualifier(), ref.getId(), ref.getSourceLocator());
        interactor.getAliases().add(alias);
        if (listener != null){
            listener.onSyntaxWarning(ref, "Found an Alias in the alternative identifiers column. Will load it as a checksum.");
        }
    }

    /**
     * <p>createChecksumFromAlias.</p>
     *
     * @param interactor a {@link psidev.psi.mi.jami.model.Interactor} object.
     * @param alias a {@link psidev.psi.mi.jami.tab.extension.MitabAlias} object.
     */
    protected void createChecksumFromAlias(Interactor interactor, MitabAlias alias) {
        // create checksum from alias
        MitabChecksum checksum = new MitabChecksum(alias.getType(), alias.getName(), alias.getSourceLocator());
        interactor.getChecksums().add(checksum);
        if (listener != null){
            listener.onSyntaxWarning(alias, "Found a Checksum in the aliases column. Will load it as a checksum.");
        }
    }

    /**
     * <p>initialiseExpansionMethod.</p>
     *
     * @param expansion a {@link java.util.Collection} object.
     * @param interaction a T object.
     */
    protected void initialiseExpansionMethod(Collection<MitabCvTerm> expansion, T interaction){
        if (expansion.size() > 1){
            if (getParserListener() != null){
                getParserListener().onSeveralCvTermsFound(expansion, expansion.iterator().next(), expansion.size()+" complex expansions found. Only the first one will be loaded.");
            }
            interaction.getAnnotations().add(new MitabAnnotation(expansion.iterator().next()));
        }
        else if (!expansion.isEmpty()){
            interaction.getAnnotations().add(new MitabAnnotation(expansion.iterator().next()));
        }
    }

    /**
     * <p>createInteraction.</p>
     *
     * @return a T object.
     */
    protected abstract T createInteraction();

    /**
     * <p>processTextFor.</p>
     *
     * @param feature a {@link psidev.psi.mi.jami.tab.extension.MitabFeature} object.
     * @param text a {@link java.lang.String} object.
     */
    protected void processTextFor(MitabFeature feature, String text){
        if (text != null){
            if (Pattern.matches(INTERPRO_PATTERN, text)){
                feature.setInterpro(text);
            }
            else{
                feature.setText(text);
            }
        }
    }
}
