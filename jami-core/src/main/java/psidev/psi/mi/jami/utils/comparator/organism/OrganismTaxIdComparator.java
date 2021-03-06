package psidev.psi.mi.jami.utils.comparator.organism;

import psidev.psi.mi.jami.model.Organism;

import java.util.Comparator;

/**
 * Simple organism comparator based on the taxIds.
 * - Two organisms which are null are equals
 * - The organism which is not null is before null.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>21/12/12</pre>
 */
public class OrganismTaxIdComparator implements Comparator<Organism>{

    private static OrganismTaxIdComparator organismTaxIdComparator;

    /**
     * Creates a new OrganismComparator
     */
    public OrganismTaxIdComparator() {
    }

    /**
     * Comparison is based on taxIds only.
     * - Two organisms which are null are equals
     * - The organism which is not null is before null.
     *
     * @param organism1 a {@link psidev.psi.mi.jami.model.Organism} object.
     * @param organism2 a {@link psidev.psi.mi.jami.model.Organism} object.
     * @return a int.
     */
    public int compare(Organism organism1, Organism organism2) {

        int EQUAL = 0;
        int BEFORE = -1;
        int AFTER = 1;

        if (organism1 == organism2){
            return EQUAL;
        }
        else if (organism1 == null){
            return AFTER;
        }
        else if (organism2 == null){
            return BEFORE;
        }
        else {
            int taxid1 = organism1.getTaxId();
            int taxid2 = organism2.getTaxId();

            if (taxid1 < taxid2){
                return BEFORE;
            }
            else if (taxid1 > taxid2){
                return AFTER;
            }
            else {
                return EQUAL;
            }
        }
    }

    /**
     * Use OrganismTaxIdComparator to know if two organisms are equals.
     *
     * @param organism1 a {@link psidev.psi.mi.jami.model.Organism} object.
     * @param organism2 a {@link psidev.psi.mi.jami.model.Organism} object.
     * @return true if the two organisms are equal
     */
    public static boolean areEquals(Organism organism1, Organism organism2){
        if (organismTaxIdComparator == null){
            organismTaxIdComparator = new OrganismTaxIdComparator();
        }

        return organismTaxIdComparator.compare(organism1, organism2) == 0;
    }

    /**
     * <p>hashCode</p>
     *
     * @param organism a {@link psidev.psi.mi.jami.model.Organism} object.
     * @return the hashcode consistent with the equals method for this comparator
     */
    public static int hashCode(Organism organism){
        if (organismTaxIdComparator == null){
            organismTaxIdComparator = new OrganismTaxIdComparator();
        }

        if (organism == null){
            return 0;
        }

        int hashcode = 31;
        hashcode = 31*hashcode + organism.getTaxId();

        return hashcode;
    }
}
