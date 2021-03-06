package psidev.psi.mi.jami.listener;

import psidev.psi.mi.jami.model.CvTerm;

/**
 * CvTerm change listener
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>11/06/13</pre>
 */
public interface CvTermChangeListener extends AnnotationsChangeListener<CvTerm>, XrefsChangeListener<CvTerm>, IdentifiersChangeListener<CvTerm>, AliasesChangeListener<CvTerm> {

    /**
     * Listen to the event where the shortName of a cv term has been changed.
     *
     * @param cv : updated cv
     * @param oldShortName  : old shortName
     */
    public void onShortNameUpdate(CvTerm cv, String oldShortName);

    /**
     * Listen to the event where the fullName of a cv term has been changed.
     * If oldFullName is null, it means that the fullName of the cv term has been initialised.
     * If the current fullName of the cv term is null, it means that the fullName has been reset
     *
     * @param cv  : updated cv
     * @param oldFullName : the old fullName
     */
    public void onFullNameUpdate(CvTerm cv, String oldFullName);

    /**
     * Listen to the event where the MI identifier of a cv term has been changed.
     * If oldMI is null, it means that a MI identifier has been added to the cv term.
     * If the MI of the cv term is null, it means that the MI identifier of the cv term has been removed
     *
     * @param cv : updated cv
     * @param oldMI : the old MI
     */
    public void onMIIdentifierUpdate(CvTerm cv, String oldMI);

    /**
     * Listen to the event where the MOD identifier of a cv term has been changed.
     * If oldMOD is null, it means that a MOD identifier has been added to the cv term.
     * If the MOD of the cv term is null, it means that the MOD identifier of the cv term has been removed
     *
     * @param cv : updated cv
     * @param oldMOD : the old MOD
     */
    public void onMODIdentifierUpdate(CvTerm cv, String oldMOD);

    /**
     * Listen to the event where the PAR identifier of a cv term has been changed.
     * If oldPAR is null, it means that a PAR identifier has been added to the cv term.
     * If the PAR of the cv term is null, it means that the PAR identifier of the cv term has been removed
     *
     * @param cv : updated cv
     * @param oldPAR : the old PAR
     */
    public void onPARIdentifierUpdate(CvTerm cv, String oldPAR);
}
