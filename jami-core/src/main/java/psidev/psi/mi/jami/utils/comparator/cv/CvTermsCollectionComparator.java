package psidev.psi.mi.jami.utils.comparator.cv;

import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.utils.comparator.CollectionComparator;

import java.util.Comparator;

/**
 * Comparator for a collection of CvTerms
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>20/12/12</pre>
 */
public class CvTermsCollectionComparator extends CollectionComparator<CvTerm> {
    /**
     * Creates a new cvTerm CollectionComparator. It requires a Comparator for the cvTerms in the Collection
     *
     * @param cvTermComparator a {@link java.util.Comparator} object.
     */
    public CvTermsCollectionComparator(Comparator<CvTerm> cvTermComparator) {
        super(cvTermComparator);
    }
}
