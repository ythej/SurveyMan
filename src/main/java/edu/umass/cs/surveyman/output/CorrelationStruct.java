package edu.umass.cs.surveyman.output;

import edu.umass.cs.surveyman.qc.CoefficentsAndTests;
import edu.umass.cs.surveyman.survey.Question;

public class CorrelationStruct {

    public final CoefficentsAndTests coefficientType;
    public final double coefficientValue;
    public final Question thingA;
    public final Question thingB;
    public final int numSamplesA;
    public final int numSamplesB;
    protected final String COEFFICIENTTYPE = "coefficientType";
    protected final String COEFFICIENTVALUE = "coefficientValue";
    protected final String THINGA = "thingA";
    protected final String NUMSAMPLESA = "numSamplesA";
    protected final String THINGB = "thingB";
    protected final String NUMSAMPLESB = "numSamplesB";


    public CorrelationStruct(
            CoefficentsAndTests coefficientType,
            double coefficientValue,
            Question thingA,
            Question thingB,
            int numSamplesA,
            int numSamplesB)
    {
        this.coefficientType = coefficientType;
        this.coefficientValue = coefficientValue;
        this.thingA = thingA;
        this.thingB = thingB;
        this.numSamplesA = numSamplesA;
        this.numSamplesB = numSamplesB;
    }

    public String jsonize()
    {
        return String.format(
                "{ " +
                        "\"%s\" : \"%s\"," +
                        "\"%s\" : %f," +
                        "\"%s\" : \"%s\"," +
                        "\"%s\" : %d," +
                        "\"%s\" : \"%s\"," +
                        "\"%s\" : %d" +
                "}",
                this.COEFFICIENTTYPE, this.coefficientType,
                this.COEFFICIENTVALUE, this.coefficientValue,
                this.THINGA, this.thingA.quid,
                this.numSamplesA, this.numSamplesA,
                this.THINGB, this.thingB.quid,
                this.NUMSAMPLESB, this.numSamplesB
        );
    }

    @Override
    public String toString()
    {
        return String.format(
                //"Coefficient type:\t%s\nCoefficient Value:\t%f\nNumber of Samples %s:\t%d\nNumber of Samples %s:\t%d",
                "%s\t%f\t%s\t%d\t%s\t%d",
                this.coefficientType,
                this.coefficientValue,
                this.thingA.getClass().getName(),
                this.numSamplesA,
                this.thingB.getClass().getName(),
                this.numSamplesB);
    }
}
