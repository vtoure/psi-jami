package psidev.psi.mi.jami.utils.comparator.parameter;

import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Parameter;
import psidev.psi.mi.jami.model.ParameterValue;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Simple comparator for Parameter
 *
 * It first compares parameter types, then parameter units and then it uses ParameterValueComparator for comparing parameter values
 * It will also compare the uncertainty.
 * - Two parameters which are null are equals
 * - The parameter which is not null is before null.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>18/12/12</pre>
 */
public abstract class ParameterComparator implements Comparator<Parameter> {

    private Comparator<CvTerm> cvTermComparator;
    private ParameterValueComparator valueComparator;

    /**
     * Creates a new ParameterComparator
     *
     * @param cvTermComparator : cv term comparator to compare parameter types and units. It is required
     */
    public ParameterComparator(Comparator<CvTerm> cvTermComparator){
        if(cvTermComparator == null){
            throw new IllegalArgumentException("The CvTerm comparator is required for comparing parameter types and units. It cannot be null");
        }
        this.cvTermComparator = cvTermComparator;
        valueComparator = new ParameterValueComparator();
    }

    /**
     * Creates a new ParameterComparator
     *
     * @param cvTermComparator : cv term comparator to compare parameter types and units. It is required
     * @param valueComparator : parameter value comparator. If null, it will create a new ParameterValueComparator
     */
    public ParameterComparator(Comparator<CvTerm> cvTermComparator, ParameterValueComparator valueComparator){
        if(cvTermComparator == null){
            throw new IllegalArgumentException("The CvTerm comparator is required for comparing parameter types and units. It cannot be null");
        }
        this.cvTermComparator = cvTermComparator;
        this.valueComparator = valueComparator != null ? valueComparator : new ParameterValueComparator();
    }

    /**
     * <p>Getter for the field <code>cvTermComparator</code>.</p>
     *
     * @return a {@link java.util.Comparator} object.
     */
    public Comparator<CvTerm> getCvTermComparator() {
        return cvTermComparator;
    }

    /**
     * <p>Getter for the field <code>valueComparator</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.utils.comparator.parameter.ParameterValueComparator} object.
     */
    public ParameterValueComparator getValueComparator() {
        return valueComparator;
    }

    /**
     * It first compares parameter types, then parameter units and then it uses ParameterValueComparator for comparing parameter values
     * It will also compare the uncertainty.
     * - Two parameters which are null are equals
     * - The parameter which is not null is before null.
     *
     * @param parameter1 a {@link psidev.psi.mi.jami.model.Parameter} object.
     * @param parameter2 a {@link psidev.psi.mi.jami.model.Parameter} object.
     * @return a int.
     */
    public int compare(Parameter parameter1, Parameter parameter2) {
        int EQUAL = 0;
        int BEFORE = -1;
        int AFTER = 1;

        if (parameter1 == parameter2){
            return EQUAL;
        }
        else if (parameter1 == null){
            return AFTER;
        }
        else if (parameter2 == null){
            return BEFORE;
        }
        else {
            CvTerm type1 = parameter1.getType();
            CvTerm type2 = parameter2.getType();

            int comp = cvTermComparator.compare(type1, type2);
            if (comp != 0){
                return comp;
            }

            // compare units
            CvTerm unit1 = parameter1.getUnit();
            CvTerm unit2 = parameter2.getUnit();

            int comp2 = cvTermComparator.compare(unit1, unit2);
            if (comp2 != 0){
                return comp2;
            }

            // compare values
            ParameterValue value1 = parameter1.getValue();
            ParameterValue value2 = parameter2.getValue();

            int comp3 = valueComparator.compare(value1, value2);
            if (comp3 != 0){
                return comp3;
            }

            // compare uncertainty
            BigDecimal uncertainty1 = parameter1.getUncertainty();
            BigDecimal uncertainty2 = parameter2.getUncertainty();
            if (uncertainty1 == null && uncertainty2 == null){
                return EQUAL;
            }
            else if (uncertainty1 == null){
                return AFTER;
            }
            else if (uncertainty2 == null){
                return BEFORE;
            }
            else {
                return uncertainty1.compareTo(uncertainty2);
            }
        }
    }
}
